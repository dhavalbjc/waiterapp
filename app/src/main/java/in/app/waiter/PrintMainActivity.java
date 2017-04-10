package in.app.waiter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zj.btsdk.BluetoothService;
import com.zj.btsdk.PrintPic;

import java.util.Map.Entry;

public class PrintMainActivity extends Activity
{
private static final int REQUEST_ENABLE_BT = 2;
private static final int REQUEST_CONNECT_DEVICE = 1;
/** Called when the activity is first created. */
EditText message;
Button printbtn;
BluetoothService mService = null;
private final  Handler mHandler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
        case BluetoothService.MESSAGE_STATE_CHANGE:
            switch (msg.arg1) {
            case BluetoothService.STATE_CONNECTED:
            	/*Toast.makeText(getApplicationContext(), "Connect successful",
                        Toast.LENGTH_SHORT).show();*/
            	 print_bt();
    			//btnClose.setEnabled(true);
    			//btnSend.setEnabled(true);
    			//btnSendDraw.setEnabled(true);
                break;
            case BluetoothService.STATE_CONNECTING:
            	Log.d("��������","��������.....");
                break;
            case BluetoothService.STATE_LISTEN:
            case BluetoothService.STATE_NONE:
            	Log.d("��������","�ȴ�����.....");
                break;
            }
            break;
        case BluetoothService.MESSAGE_CONNECTION_LOST:
            /*Toast.makeText(getApplicationContext(), "Device connection was lost",
                           Toast.LENGTH_SHORT).show();*/
		//	btnClose.setEnabled(false);
		//	btnSend.setEnabled(false);
		//	btnSendDraw.setEnabled(false);
            break;
        case BluetoothService.MESSAGE_UNABLE_CONNECT:
        	Toast.makeText(getApplicationContext(), "Unable to connect device",
                    Toast.LENGTH_SHORT).show();
        	break;
        }
    }

};
BluetoothDevice con_dev = null;

@Override
public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_print_main);
mService = new BluetoothService(this, mHandler);

if( mService.isAvailable() == false ){
    Toast.makeText(this, "Bluetooth is not available", Toast.LENGTH_LONG).show();
    finish();
}
message = (EditText)findViewById(R.id.message);
printbtn = (Button)findViewById(R.id.printButton);

printbtn.setOnClickListener(new OnClickListener() {

@Override
public void onClick(View v) {
connect();
}
});
}

@Override
public void onStart() {
	super.onStart();

	if( mService.isBTopen() == false)
	{
        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
	}

}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.print_main, menu);
	return true;
}

protected void connect() {
if(con_dev == null){
	SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref",0);

 for (Entry<String, ?> entry : pref.getAll().entrySet()) {
     Object val = entry.getValue();
     if (val == null) {
         //output.append(String.format("%s = <null>%n", entry.getKey()));
     } else {
    	 con_dev = mService.getDevByMac(String.valueOf(entry.getValue()));

         mService.connect(con_dev);


     }
 }
 if(con_dev == null){
	Intent serverIntent = new Intent(PrintMainActivity.this,DeviceListActivity.class);
	startActivityForResult(serverIntent,REQUEST_CONNECT_DEVICE);
 }
}
else{

print_bt();

}

}

private void print_bt() {

	String msg = message.getText().toString();
    if( msg.length() > 0 ){
        mService.sendMessage(msg+"\n", "GBK");
        Toast.makeText(this, "Print successful", Toast.LENGTH_LONG).show();
    }

}

@Override
protected void onDestroy() {

super.onDestroy();
if (mService != null)
	mService.stop();
mService = null;
}

@Override
public void onActivityResult(int requestCode, int resultCode, Intent data) {    
    switch (requestCode) {
    case REQUEST_ENABLE_BT:
        if (resultCode == Activity.RESULT_OK) {
        	Toast.makeText(this, "Bluetooth open successful", Toast.LENGTH_LONG).show();
        } else {
        	finish();
        }
        break;
    case  REQUEST_CONNECT_DEVICE:
    	if (resultCode == Activity.RESULT_OK) {
            String address = data.getExtras()
                                 .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
            con_dev = mService.getDevByMac(address);   
            
            mService.connect(con_dev);
            print_bt();
        }
        break;
    }
} 


@SuppressLint("SdCardPath")
private void printImage() {
	byte[] sendData = null;
	PrintPic pg = new PrintPic();
	pg.initCanvas(384);     
	pg.initPaint();
	pg.drawImage(0, 0, "/mnt/sdcard/icon.jpg");
	sendData = pg.printDraw();
	mService.write(sendData);
}
}