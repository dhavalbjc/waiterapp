package in.app.waiter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";
	  public static int notificationCount=0;
	  public static boolean secondTime=false;
	  public static boolean received=false;
	private Controller aController = null;
	
    public GCMIntentService() {
    	// Call extended class Constructor GCMBaseIntentService
        super(Config.GOOGLE_SENDER_ID);
    }

    /**
     * Create a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context,String title, String message, String imei) {
        int icon = R.drawable.ic_launcher;
        long when = System.currentTimeMillis();

		NotificationManager notificationManager = (NotificationManager)
        context.getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
	            context);

       // Notification notification = new Notification(icon, message, when);

        Intent notificationIntent = new Intent(context, SplashScreen.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);

       /* notificationIntent.putExtra("name", title);
        notificationIntent.putExtra("message", message);
        notificationIntent.putExtra("imei", imei);*/

        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
       // notification.setLatestEventInfo(context, title, message,intent);
        String[] multilineDescription = new String[] { "New Message", message };
        String description;
        if (null == multilineDescription || multilineDescription.length == 0) {
            description = "No more information.";
        } else {
            description = multilineDescription[0];

            for (int i=1; i<multilineDescription.length; i++) {
                description += System.getProperty("line.separator");
                description += multilineDescription[i];
            }
        }
        ++notificationCount;
        if(notificationCount>1){
        	message= notificationCount + " messages";
        }
        Notification notification = builder.setContentIntent(intent)
	            .setSmallIcon(icon).setTicker("WaiterApp").setWhen(when)
	            .setAutoCancel(true).setContentTitle("WaiterApp")
	            .setStyle(new NotificationCompat.BigTextStyle().bigText(description))
	            .setContentText(message).build();


        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;

        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");

        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_SOUND;

        notificationManager.notify(0, notification);


      //  String[] multilineDescription = new String[] { "line 1", "another very long line that will get wrapped." };

      /*  NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
        .setSmallIcon(icon)
        .setContentTitle(title)
        .setContentText("Pull down for more information");
        String description;
        if (null == multilineDescription || multilineDescription.length == 0) {
            description = "No more information.";
        } else {
            description = multilineDescription[0];

            for (int i=1; i<multilineDescription.length; i++) {
                description += System.getProperty("line.separator");
                description += multilineDescription[i];
            }
        }

        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(description));
*/
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {


    	//Get Global Controller Class object (see application tag in AndroidManifest.xml)
    	if(aController == null)
           aController = (Controller) getApplicationContext();

    	Log.i(TAG, "---------- onRegistered -------------");
        Log.i(TAG, "Device registered: regId = " + registrationId);
        aController.displayRegistrationMessageOnScreen(context, "Your device registred with GCM");
        Log.d("NAME", ServerRegister.name);

        aController.register(context, ServerRegister.name, ServerRegister.email, registrationId,ServerRegister.imei);

        DBAdapter.addDeviceData(ServerRegister.name, ServerRegister.email, registrationId, ServerRegister.imei);



    }

    /**
     * Method called on device unregistred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
    	if(aController == null)
            aController = (Controller) getApplicationContext();
    	Log.i(TAG, "---------- onUnregistered -------------");
        Log.i(TAG, "Device unregistered");
        aController.displayRegistrationMessageOnScreen(context, getString(R.string.gcm_unregistered));
        aController.unregister(context, registrationId,ServerRegister.imei);
    }

    /**
     * Method called on Receiving a new message from GCM server
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {

    	if(aController == null)
            aController = (Controller) getApplicationContext();

    	Log.i(TAG, "---------- onMessage -------------");
        String message = intent.getExtras().getString("message");

        //Log.i("GCM","message : "+message);
        //String[] StringAll;
    	//StringAll = message.split("\\^");
    	//StringAll = message.split("\\^");
    	if(message.contains("^")){
    		String[] StringAll;
    		StringAll = message.split("\\^");
    		String rid=StringAll[StringAll.length-1];
    		String code=StringAll[0];
    		try{
    		if(Integer.parseInt(rid)==DBAdapter.getRestaurantId()){
    			if(Integer.parseInt(code)==132)
    			{
    				DBAdapter.updateCartTTT(Integer.parseInt(StringAll[1]), "cart_paid", "true");
    				aController.CartUpdate(context, "Hello");
    			}
    			if(Integer.parseInt(code)==25)
    			{
    				for (CartItems citem : DBAdapter.getAllCartItemsDataByCartId(Integer.parseInt(StringAll[1]))) {
						DBAdapter.deleteCartitem(citem.get_id());
					}
		    		DBAdapter.deleteCart(Integer.parseInt(StringAll[1]));
    				aController.CartUpdate(context, "Hello");
    			}
    			if(Integer.parseInt(code)==141)
    			{
    				DBAdapter.updateCartItemStatus(Integer.parseInt(StringAll[1]), StringAll[2]);

    				aController.CartUpdate(context, "Hello");
    			}
    			aController.MessageAction(context, message);
        		//generateNotification(context, message,message,message);
    		}

    		}catch (Exception e) {
				// TODO: handle exception
			}


    	}
    	else{try{
    		StringBuffer items=new StringBuffer();
    		Gson gson = new GsonBuilder().create();
    		//Data d=new Data();
    		//ModelCart m=new ModelCart();
    		ModelCart cart=gson.fromJson(message,ModelCart.class);
    		//generateNotification(context, cart.getTableName(),cart.getTotal(),cart.getTime());
    		//aController.MessageAction(context, message);
    	//ModelCart dbCart=DBAdapter.getCartData(cart.getId());
    		if(cart.getRetroId()==DBAdapter.getRestaurantId() ){
    	if(!DBAdapter.CheckIsDataAlreadyInDBorNot("tbl_cart", "cart_id",cart.getId()))
    	//if((dbCart==null))
    		{
    		DBAdapter.addCartData(cart);

    		//items.append(cart.getTableName());

    		for (CartItems cartItem : cart.getProducts()) {
    			if(cartItem.get_item().get_flag().equals("I")){
    				items.append("/n");
    				items.append(cartItem.get_quantity()+" ");
    				items.append(cartItem.get_item().get_name());

				DBAdapter.addCartitems(cartItem, cart.getId());
				for (Extra extraItem : cartItem.get_item().get_extra()) {
					int rid=extraItem.get_id();
					extraItem.set_id(extraItem.get_refId());
					DBAdapter.addCartitemsExtra(extraItem, cartItem.get_id(),rid);

				}
    			}
    			/*else{
    				items.append("/n");
    				items.append("   with");
    				Extra e=new Extra();
    				e.set_id(cartItem.get_id());
    				e.set_name(cartItem.get_item().get_name());
    				e.set_price(cartItem.get_item().get_price());
    				items.append(cartItem.get_item().get_name());
    				//DBAdapter.addCartitemsExtra(e, cartItem.get_itemidAscid(), cartItem.get_item().get_id());
    			}*/
			}

    		aController.CartUpdate(context, "Hello");
    		received=true;
    		generateNotification(context, "New Order for Table "+ cart.getTableName(),"New Order for Table "+ cart.getTableName(),cart.getTime());
    	}
    	else
    	{
    		Staff staff=DBAdapter.getStaffData(Main.getEmail(context));
    		if(!cart.getUser().equals(staff.get_staff_fname())){
    		DBAdapter.updateCart(cart.getId(), cart.getTableName(), cart.getTotal());
    		for (CartItems citem : DBAdapter.getAllCartItemsDataByCartId(cart.getId())) {
				DBAdapter.deleteCartitem(citem.get_id());
			}
    		for (CartItems cartItem : cart.getProducts()) {
    			if(cartItem.get_item().get_flag().equals("I")){

				DBAdapter.addCartitems(cartItem, cart.getId());
				for (Extra extraItem : cartItem.get_item().get_extra()) {
					int rid=extraItem.get_id();
					extraItem.set_id(extraItem.get_refId());
					DBAdapter.addCartitemsExtra(extraItem, cartItem.get_id(),rid);

				}

    			}
    		}
    		/*for (CartItems cartItem : cart.getProducts()) {
    			if(cartItem.get_item().get_flag().equals("I")){
				DBAdapter.addCartitems(cartItem, cart.getId());
    			}
    			else{
    				Extra e=new Extra();
    				e.set_id(cartItem.get_id());
    				e.set_name(cartItem.get_item().get_name());
    				e.set_price(cartItem.get_item().get_price());

    				DBAdapter.addCartitemsExtra(e, cartItem.get_itemidAscid(), cartItem.get_item().get_id());
    			}
			}*/

    		aController.CartUpdate(context, "Hello");
    		received=true;
    		generateNotification(context,"Update Order for Table "+ cart.getTableName(),"Update Order for Table "+ cart.getTableName(),cart.getTime());
    	}
    	}
    	}

    	}
    	catch(Exception e)
    	{

    	}
    	}


		/*if (StringLength > 0) {

			title   = StringAll[0];
			imei    = StringAll[1];
			message = StringAll[2];
		}*/

		 // Call broadcast defined on ShowMessage.java to show message on ShowMessage.java screen
       //  aController.displayMessageOnScreen(context, name,message,seq);

         // Store new message data in sqlite database
         //UserData userdata = new UserData(1,imei,title,message);
        // DBAdapter.addUserData(userdata);

        // generate notification to notify user


    }

    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {

    	if(aController == null)
            aController = (Controller) getApplicationContext();

    	Log.i(TAG, "---------- onDeletedMessages -------------");
        String message = getString(R.string.gcm_deleted, total);

        String title = "DELETED";
        // aController.displayMessageOnScreen(context, message);

        // generate notification to notify user
        generateNotification(context,title, message,"");
    }

    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {

    	if(aController == null)
            aController = (Controller) getApplicationContext();

    	Log.i(TAG, "---------- onError -------------");
        Log.i(TAG, "Received error: " + errorId);

        aController.displayRegistrationMessageOnScreen(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {

    	if(aController == null)
            aController = (Controller) getApplicationContext();

    	Log.i(TAG, "---------- onRecoverableError -------------");

        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        aController.displayRegistrationMessageOnScreen(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }

}
