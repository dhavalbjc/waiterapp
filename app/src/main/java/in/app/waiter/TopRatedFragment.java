package in.app.waiter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;

public class TopRatedFragment extends Fragment implements
		AbsListView.OnScrollListener, AbsListView.OnItemClickListener {
	public MyAdapterNew mAdapter;
	// Controller aController;
	public ArrayList<ModelCart> mCart = new ArrayList<ModelCart>();
	boolean needToRefresh = true;
	LocalBroadcastManager mgr;
	Button new_order;
	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			MainActivity.aController.acquireWakeLock(context);
			// Waking up mobile if it is sleeping
			try {
				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						mCart.clear();

						mCart.addAll(DBAdapter.getAllCartData());
						if (mCart.size() != 0) {
							new_order.setVisibility(View.GONE);
						} else {
							new_order.setVisibility(View.VISIBLE);
						}
						// String newMessage =
						// intent.getExtras().getString(Config.EXTRA_MESSAGE);
					/*
					 * Toast.makeText(context, "Got Message: " + newMessage,
					 * Toast.LENGTH_LONG).show();
					 */
						// onLoadMoreItems(cart);

						mAdapter.notifyDataSetChanged();
					}
				});
				// needToRefresh=true;
				// gridView.notifyAll();
				// gridView.getAdapter().notify();
				// gridView.setAdapter(mAdapter);
				// gridView.getAdapter().notifyAll();
				// gridView.refreshDrawableState();
				MainActivity.aController.releaseWakeLock();
			}catch (Exception e){}
		}
	};
	private StaggeredGridView gridView;

	@Override
	public View onCreateView(LayoutInflater inflater,
			final ViewGroup container, Bundle savedInstanceState) {

		// aController = (Controller) getApplicationContext();
		final View rootView = inflater.inflate(R.layout.fragment_top_rated,
				container, false);

		// gridView.addFooterView(footer);

		return rootView;
	}

	@Override
	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		gridView = (StaggeredGridView) getView().findViewById(R.id.grid_view);
		new_order = (Button) getView().findViewById(R.id.btn_New_order);
		if (savedInstanceState == null) {
			final LayoutInflater layoutInflater = getActivity()
					.getLayoutInflater();

			View header = layoutInflater.inflate(R.layout.new_order, null);
			Button new_order1 = (Button) header
					.findViewById(R.id.btn_New_order);
			// View footer =
			// layoutInflater.inflate(R.layout.list_item_header_footer, null);
			// TextView txtHeaderTitle = (TextView)
			// header.findViewById(R.id.txt_title);
			// TextView txtFooterTitle = (TextView)
			// footer.findViewById(R.id.txt_title);
			// txtHeaderTitle.setText("THE HEADER!");
			// txtFooterTitle.setText("THE FOOTER!");
			new_order1.setOnClickListener(new RelativeLayout.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Intent nextpage = new Intent(getActivity(),
							newOrderActivity.class);
					startActivity(nextpage);

				}
			});

			gridView.addHeaderView(header);
		}
	}

	@Override
	public void onStart() {

		super.onStart();

		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				mCart = DBAdapter.getAllCartData();
				if (mCart.size() != 0) {
					new_order.setVisibility(View.GONE);
				} else {
					new_order.setVisibility(View.VISIBLE);
				}
				// aController = (Controller) container.getContext();
				// Register custom Broadcast receiver to show messages on
				// activity
				mgr = LocalBroadcastManager.getInstance(getActivity());
				mgr.registerReceiver(mHandleMessageReceiver, new IntentFilter(
						Config.CART_UPDATE));
				// registerReceiver(mHandleMessageReceiver, new
				// IntentFilter(Config.DISPLAY_MESSAGE_ACTION));

				new_order
						.setOnClickListener(new RelativeLayout.OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								Intent nextpage = new Intent(getActivity(),
										newOrderActivity.class);
								// nextpage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								nextpage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								nextpage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								startActivity(nextpage);

							}
						});

				if (mCart == null) {
					mCart = DBAdapter.getAllCartData();
					if (mCart.size() != 0) {
						new_order.setVisibility(View.GONE);
					} else {
						new_order.setVisibility(View.VISIBLE);
					}
				}

				mAdapter = new MyAdapterNew(getActivity(), mCart, getWidth());

				gridView.setAdapter(mAdapter);
			}
		});
		// gridView.setOnScrollListener(this);
		gridView.setOnItemClickListener(this);
		// gridView.setAdapter(mAdapter);
		TextView emptyView = (TextView) getView().findViewById(R.id.emptytext);
		// startRunnable();
		gridView.setEmptyView(emptyView);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long id) {
				try {
					// gridView.setBackgroundDrawable(res.getDrawable(R.drawable.gridview_back));
					// dhaval v.setBackgroundResource(R.drawable.gridview_back);
					// TextView tv=(TextView)
					// getView().findViewById(R.id.grid_TableName);
					ModelCart m = (ModelCart) mAdapter.getItem(position - 1);
					/*
					 * Toast.makeText( container.getContext(),
					 * tv.getText().toString()+m.getId(),
					 * Toast.LENGTH_SHORT).show();
					 */
					Intent mIntent = new Intent(getActivity(),
							newOrderActivity.class);
					mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Bundle mBundle = new Bundle();
					mBundle.putString("ID", String.valueOf(m.getId()));
					mIntent.putExtras(mBundle);
					getActivity().startActivity(mIntent);

				} catch (Exception e) {

				}
				// v.setSelected(false);

				// Intent mIntent = new Intent(this, Example.class);

				// ModelCart m=(ModelCart) gridView.getChildAt(position);
			}
		});
		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				// TODO Auto-generated method stub
				// Toast.makeText(container.getContext(), "Item Long Clicked: "
				// + arg2, Toast.LENGTH_SHORT).show();
				try {
					// Resources res=arg1.getResources();
					// dhaval
					// arg1.setBackgroundResource(R.drawable.gridview_back);
					// final ModelCart
					// smember=(ModelCart)parent.getItemAtPosition(position);
					final ModelCart smember = (ModelCart) mAdapter
							.getItem(arg2 - 1);
					// Toast.makeText(container.getContext(),String.valueOf(smember.getId()),
					// Toast.LENGTH_SHORT).show();
					final Dialog dialog = new Dialog(getActivity());
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.staff_delete);
					// TextView tv_invite=(TextView)
					// dialog.findViewById(R.id.tv_invite);
					TextView tv_invite_cancel = (TextView) dialog
							.findViewById(R.id.tv_invite_cancel);
					// tv_invite.setText("Order");
					tv_invite_cancel.setText("Cancel Order");

					RelativeLayout Deletebtn = (RelativeLayout) dialog
							.findViewById(R.id.Rel_staff_delete);

					Deletebtn.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View arg0) {

							AlertDialog.Builder builder = new AlertDialog.Builder(
									getActivity());
							builder.setTitle("Delete Order?");
							builder.setMessage("Do you want to delete this order ??");
							builder.setPositiveButton("Delete",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											// DBAdapter.deleteStaff(smember.get_staff_id());
											// cardArrayAdapter.remove(smember);
											// cardArrayAdapter.notifyDataSetChanged();
											// Toast.makeText(container.getContext(),
											// smember.getId() ,
											// Toast.LENGTH_SHORT).show();
											try {
												ArrayList<HashMap<String, String>> wordList;
												wordList = new ArrayList<HashMap<String, String>>();
												HashMap<String, String> map = new HashMap<String, String>();
												map.put("oprn", "Delete");
												map.put("table", "Cart");
												map.put("id", String
														.valueOf(smember
																.getId()));
												map.put("rid",
														String.valueOf(DBAdapter
																.getRestaurantId()));
												wordList.add(map);
												Gson gson = new GsonBuilder()
														.create();
												crudonserver crudop = new crudonserver();
												crudop.callserverforcrudopern(gson
														.toJson(wordList),getActivity());

											} catch (Exception e) {
												// error=String.valueOf(e.getMessage());
												// Toast.makeText(context,Toast.LENGTH_LONG).show();
											}
											for (CartItems citem : DBAdapter
													.getAllCartItemsDataByCartId(smember
															.getId())) {
												DBAdapter.deleteCartitem(citem
														.get_id());
											}
											DBAdapter.deleteCart(smember
													.getId());
											TopRatedFragment.this.mCart.clear();
											TopRatedFragment.this.mCart
													.addAll(DBAdapter
															.getAllCartData());
											if (mCart.size() != 0) {
												new_order
														.setVisibility(View.GONE);
											} else {
												new_order
														.setVisibility(View.VISIBLE);
											}
											TopRatedFragment.this.mAdapter
													.notifyDataSetChanged();
											// gridView.setAdapter(null);
											// gridView.setAdapter(mAdapter);
											// Toast.makeText(getActivity(),"Deleted",Toast.LENGTH_LONG).show();
											dialog.cancel();
											// mAdapter.notifyDataSetChanged();
											dialog.dismiss();
										}
									});

							builder.setNegativeButton("No",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// TODO Auto-generated method stub
											dialog.dismiss();

										}
									});

							AlertDialog alert = builder.create();
							// if(myCartHash.size()>0){
							alert.show();
							dialog.cancel();
						}
					});
					dialog.getWindow().setLayout(getWidth(),
							LayoutParams.WRAP_CONTENT);
					dialog.setOnCancelListener(new Dialog.OnCancelListener() {

						@Override
						public void onCancel(DialogInterface dialog) {
							// DO SOME STUFF HERE
							mAdapter.notifyDataSetChanged();
						}

					});
					Staff staff = DBAdapter.getStaffData(Main
							.getEmail(getActivity()));
					if (staff != null) {
						if (!staff.get_staff_roll().equals("admin")
								&& !staff.get_staff_roll().equals("owner")
								&& !staff.get_staff_roll().equals("manager")) {

						} else {
							dialog.show();
						}
					}
					// arg1.setDrawingCacheBackgroundColor(res.g);

				} catch (Exception e) {
					Toast.makeText(getActivity(),
							String.valueOf(e.getMessage()), Toast.LENGTH_LONG)
							.show();
				}
				return true;
			}
		});

	}

	public void startRunnable() {
		Runnable r = new Runnable() {

			@Override
			public void run() {
				int i = 0;
				while (needToRefresh) {
					// if(i>20)
					// {
					needToRefresh = false;
					// }
					mAdapter.notifyDataSetChanged();
					try {
						// i++;
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		getActivity().runOnUiThread(r);
	}

	public void onLoadMoreItems(ModelCart cart) {
		// final ArrayList<String> sampleData = SampleData.generateSampleData();
		// for (String data : sampleData) {
		mCart.add(cart);
		// }
		// stash all the data in our backing store
		// mData.addAll(sampleData);
		// notify the adapter that we can update now
		mAdapter.notifyDataSetChanged();
		// mHasRequestedMore = false;
	}

	public void onPause() {
		try {
			// Unregister Broadcast Receiver
			// mgr.unregisterReceiver(mHandleMessageReceiver);
			super.onPause();
			// mAdapter.clear();
			// mAdapter.notifyDataSetChanged();

		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		// super.onPause();
	}

	@Override
	public void onStop() {
		try {
			// Unregister Broadcast Receiver
			// mgr.unregisterReceiver(mHandleMessageReceiver);

		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onStop();
	}

	@Override
	public void onDestroy() {
		try {
			// Unregister Broadcast Receiver
			mgr.unregisterReceiver(mHandleMessageReceiver);

		} catch (Exception e) {
			Log.e("UnRegister Receiver Error", "> " + e.getMessage());
		}
		super.onDestroy();
	}

	public int getWidth() {
		try {

			Display display = getActivity().getWindowManager()
					.getDefaultDisplay();
			Point screenSize = new Point();
			display.getSize(screenSize);
			int width = screenSize.x;
			WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
			// layoutParams.copyFrom(dialog.getWindow().getAttributes());
			layoutParams.width = (int) (width - (width * 0.05));
			// layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
			return layoutParams.width;
		} catch (Exception e) {
			// Toast.makeText(getActivity(),String.valueOf(e.getMessage()),Toast.LENGTH_LONG).show();
			return 400;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub

	}

}
