package in.app.waiter;

public class menusynker {
	/*private Controller aController = (Controller) getApplicationContext();
	if(aController == null)
        aController = (Controller) getApplicationContext();*/
 	
public static void sync(String message)
{
	String[] StringAll;
	StringAll = message.split("\\^");
	
	int key=Integer.parseInt(StringAll[0]);
	switch (key) {
	case 00:
		try{
		//name    = StringAll[1];
		//seq = StringAll[2];
		Category cat =new Category();
		int id=Integer.parseInt(StringAll[1]);
		String name=StringAll[2];
		cat.set_id(id);
		cat.set_name(name);
		cat.set_sequence(id);
			//generateNotification(context, name,message,seq);
		if(!DBAdapter.CheckIsDataAlreadyInDBorNot("tbl_category", "category_id", id)){
		
		DBAdapter.addCategoryData(cat);
			//GamesFragment g=new GamesFragment();
			//g.addcat(cat);	
		manager.addcat(cat);}
		}catch(Exception e)	{}
		
		break;
	case 05:
		try{
		//name    = StringAll[1];
		//seq = StringAll[2];
		Category cat =new Category();
		int id=Integer.parseInt(StringAll[1]);
		String name=StringAll[2];
		cat.set_id(id);
		cat.set_name(name);
		cat.set_sequence(id);
			//generateNotification(context, name,message,seq);
		if(!DBAdapter.CheckIsDataAlreadyInDBorNot("tbl_category", "category_id", id)){
		
		DBAdapter.addCategoryData(cat);
			//GamesFragment g=new GamesFragment();
			//g.addcat(cat);	
		manager.addcat(cat);}
		
		Item item =new Item();
		int itemId=Integer.parseInt(StringAll[3]);
		String itemName=StringAll[4];
		String price=StringAll[5];
		String flag="I";
		int cid=Integer.parseInt(StringAll[1]);
		if(!DBAdapter.CheckIsDataAlreadyInDBorNot("tbl_item", "item_id", itemId))
		{
		//Category category=DBAdapter.getCategoryData(cid);	
		 
		item.set_id(itemId);
		item.set_name(itemName);
		item.set_price(price);
		item.set_flag(flag);
		item.set_sequence(id);
		item.set_category(cat);
		 DBAdapter.addItemData(item);
			//generateNotification(context, name,message,seq);
		
		//	GamesFragment g=new GamesFragment();
		//	g.addItem(item);		
			manager.addItem(item);
		}
		}catch(Exception e)	{}
		
		break;
	case 01:
		try{
		//name    = StringAll[1];
		//seq = StringAll[2];
		Item item =new Item();
		int id=Integer.parseInt(StringAll[1]);
		String name=StringAll[2];
		String price=StringAll[3];
		String flag=StringAll[4];
		int cid=Integer.parseInt(StringAll[6]);
		if(!DBAdapter.CheckIsDataAlreadyInDBorNot("tbl_item", "item_id", id))
		{
		Category cat=DBAdapter.getCategoryData(cid);	
		 
		item.set_id(id);
		item.set_name(name);
		item.set_price(price);
		item.set_flag(flag);
		item.set_sequence(id);
		item.set_category(cat);
		 DBAdapter.addItemData(item);
			//generateNotification(context, name,message,seq);
		
		//	GamesFragment g=new GamesFragment();
		//	g.addItem(item);		
			manager.addItem(item);
		}
		}catch(Exception e)	{}
		
		break;
	case 02:
		try{
		//name    = StringAll[1];
		//seq = StringAll[2];
		Extra extra =new Extra();
		int id=Integer.parseInt(StringAll[1]);
		String name=StringAll[2];
		String price=StringAll[3];		
		int itemid=Integer.parseInt(StringAll[5]);
		if(!DBAdapter.CheckIsDataAlreadyInDBorNot("tbl_extra", "extra_id", id))
		{
		//Category cat=DBAdapter.getCategoryData(cid);
		Item  i=DBAdapter.getItemData(itemid);
		 
		extra.set_id(id);
		extra.set_name(name);
		extra.set_price(price);		
		extra.set_sequence(id);
		extra.set_item(i);
		 DBAdapter.addExtraData(extra);
			//generateNotification(context, name,message,seq);
		
		//GamesFragment g=new GamesFragment();
		//g.addExtra(extra);	
		 manager.addExtra(extra);
			
		}	
		}catch(Exception e)	{}
		
		break;
	case 03:
		try{
		//name    = StringAll[1];
		//seq = StringAll[2];
		ModelCart cart =new ModelCart();
		int id=Integer.parseInt(StringAll[1]);
		String table=StringAll[2];
		String total=StringAll[3];	
		cart.setId(id);
		cart.setTableName(table);
		cart.setTotal(total);
		if(!DBAdapter.CheckIsDataAlreadyInDBorNot("tbl_cart", "cart_id", id))
		{
		DBAdapter.addCartData(cart);	
	
		}
		//manager.addCartData(cart);
			
			
		}catch(Exception e)	{}
		
		break;
	case 04:
		try{
		//name    = StringAll[1];
		//seq = StringAll[2];
		CartItems cartitem =new CartItems();
		int id=Integer.parseInt(StringAll[1]);
		String itemid=StringAll[2];
		String name=StringAll[3];
		String price=StringAll[4];
		String quantity=StringAll[5];
		String flag=StringAll[6];
		String cartid=StringAll[7];
		Item i=new Item();
		i.set_id(Integer.parseInt(itemid));
		i.set_name(name);
		i.set_price(price);
		i.set_flag(flag);
		cartitem.set_id(id);		
		cartitem.set_item(i);
		cartitem.set_quantity(Integer.parseInt(quantity));
		if(flag.equals("I"))
		{
			if(!DBAdapter.CheckIsDataAlreadyInDBorNot("tbl_cartitems", "cartitems_id", Integer.parseInt(cartid)))
			{
			DBAdapter.addCartitems(cartitem, Integer.parseInt(cartid));
			MainActivity main=new MainActivity();
			main.refreshGrid();
			}
		}
		else{
			Extra e=new Extra();
			e.set_id(Integer.parseInt(itemid));
			e.set_name(name);
			e.set_price(price);		
			if(!DBAdapter.CheckIsDataAlreadyInDBorNot("tbl_cartitems", "cartitems_id", e.get_id()))
			{
			DBAdapter.addCartitemsExtra(e, Integer.parseInt(cartid),e.get_id());
			MainActivity main=new MainActivity();
			main.refreshGrid();
			}
		}
		//TopRatedFragment t=new TopRatedFragment();
		//t.mAdapter.notifyDataSetChanged();
		//DBAdapter.addCartData(cart);				
		//manager.addCartData(cart);
			
			
		}catch(Exception e)	{}
		
		break;
	case 06:
		try{
	// add service
       // $message = "06"."^".$data[$i]->eid."^".$data[$i]->cat."^".$data[$i]->name."^".$data[$i]->qty."^".$data[$i]->param."^".$data[$i]->rid;
		ECharge echarge =new ECharge();
		int eid=Integer.parseInt(StringAll[1]);
		String cat=StringAll[2];
		String name=StringAll[3];
		String qty=StringAll[4];
		String param=StringAll[5];
		echarge.set_id(eid);
		echarge.set_category(cat);
		echarge.set_name(name);
		echarge.set_quantity(qty);
		echarge.set_param(param);

			//generateNotification(context, name,message,seq);
		if(DBAdapter.CheckIsDataAlreadyInDBorNot("tbl_echarge", "echarge_id", eid)){
			
			DBAdapter.addEChargeData(echarge);
		
		}
		}catch(Exception e)	{}
		
		break;
	case 07:
		try{
	// add service
       // $message = "07"."^".$data[$i]->pid."^".$data[$i]->protocol."^".$data[$i]->connection."^".$data[$i]->mac."^".$data[$i]->ip."^".$data[$i]->model.$data[$i]->paperwidth."^".$data[$i]->name."^".$data[$i]->printorder."^".$data[$i]->enable."^".$data[$i]->status."^".$data[$i]->rid;
			 
		Printer printer =new Printer();
		int pid=Integer.parseInt(StringAll[1]);
		String protocol=StringAll[2];
		String connection=StringAll[3];
		String mac=StringAll[4];
		String ip=StringAll[5];
		String model=StringAll[6];
		String paperwidth=StringAll[7];
		String name=StringAll[8];
		String printoder=StringAll[9];
		String enable=StringAll[10];
		String status=StringAll[11];
		
		
	        printer.set_id(pid);
	        printer.set_protocol(protocol);
	        printer.set_connection(connection);
	        printer.set_mac(mac);
	        printer.set_ip(ip);
	        printer.set_model(model);
	        printer.set_paperwidth(paperwidth);
	        printer.set_name(name);
	        printer.set_printorder(printoder);
	        printer.set_enable(enable);
	        printer.set_status(status);

			//generateNotification(context, name,message,seq);
		if(DBAdapter.CheckIsDataAlreadyInDBorNot("tbl_printer", "printer_id", pid)){
			
			DBAdapter.addPrinterData(printer);
		
		}
		}catch(Exception e)	{}
		
		break;
	case 100:
		try{
		//update category name
			//$message = "100"."^".$data[$i]->cid."^".$data[$i]->name;
			int id=Integer.parseInt(StringAll[1]);
			String name=StringAll[2];
			Category cat=DBAdapter.getCategoryData(id);
			if(cat!=null){				
			if(!cat.get_name().trim().equals(name.trim())){
			DBAdapter.updateCatName(id, name);
			manager.UpdateCatName(id, name);
			}
			}
			//group.set_name(price1.toString());
			/*for (int i = 0; i < GamesFragment.ExpListItems.size(); i++) {
				   if (GamesFragment.ExpListItems.get(i).get_id()==id) {					  
					   GamesFragment.ExpListItems.get(i).set_name(name);
						GamesFragment.ExpAdapter.notifyDataSetChanged();
						GamesFragment.expandAll();
				    break;
				  }
				}	*/
						
		}catch(Exception e)	{}
		
		break;
	case 101:
		try{
			//update category seq
			//$message = "101"."^".$data[$i]->cid."^".$data[$i]->seq;
			int id=Integer.parseInt(StringAll[1]);
			int seq=Integer.parseInt(StringAll[2]);
			Category cat=DBAdapter.getCategoryData(id);
			if(cat!=null){
			if(cat.get_sequence()!=seq){
			DBAdapter.addCatSeq(id, seq);
			manager.addCatSeq(id, seq);
			}
			}
			//group.set_name(price1.toString());
			/*for (int i = 0; i < GamesFragment.ExpListItems.size(); i++) {
				   if (GamesFragment.ExpListItems.get(i).get_id()==id) {					  
					   GamesFragment.ExpListItems.get(i).set_sequence(seq);
					   Collections.sort(GamesFragment.ExpListItems,new myCateComparable());
						GamesFragment.ExpAdapter.notifyDataSetChanged();
						GamesFragment.expandAll();
				    break;
				  }
				}	*/
			
			
		}catch(Exception e)	{}
		
		break;
	case 102:
		try{
			//update category seq
			//$message = "101"."^".$data[$i]->cid."^".$data[$i]->seq;
			int id1=Integer.parseInt(StringAll[1]);
			int seq1=Integer.parseInt(StringAll[2]);
			int id2=Integer.parseInt(StringAll[3]);
			int seq2=Integer.parseInt(StringAll[4]);
			Category cat1=DBAdapter.getCategoryData(id1);
			if(cat1!=null){
			if(cat1.get_sequence()!=seq1){
			DBAdapter.addCatSeq(id1, seq1);
			manager.addCatSeq(id1, seq1);
			}}
			Category cat2=DBAdapter.getCategoryData(id2);
			if(cat2!=null){
			if(cat2.get_sequence()!=seq2){
			DBAdapter.addCatSeq(id2, seq2);
			manager.addCatSeq(id2, seq2);
			}
			}
			//group.set_name(price1.toString());
			/*for (int i = 0; i < GamesFragment.ExpListItems.size(); i++) {
				   if (GamesFragment.ExpListItems.get(i).get_id()==id) {					  
					   GamesFragment.ExpListItems.get(i).set_sequence(seq);
					   Collections.sort(GamesFragment.ExpListItems,new myCateComparable());
						GamesFragment.ExpAdapter.notifyDataSetChanged();
						GamesFragment.expandAll();
				    break;
				  }
				}	*/
			
			
		}catch(Exception e)	{}
		
		break;
	case 110:
		try{
			//update Item name
			//$message = "110"."^".$data[$i]->id."^".$data[$i]->name;
			int id=Integer.parseInt(StringAll[1]);
			String name=StringAll[2];
			Item item=DBAdapter.getItemData(id);
			if(item!=null){
				if(!item.get_name().trim().equals(name.trim())){
			DBAdapter.updateItemName(id, name);
			manager.updateItemName(id, name);
				}
			}
			
		}catch(Exception e)	{}
		
		break;
	case 111:
		try{
			//update Item price
			//$message = "111"."^".$data[$i]->id."^".$data[$i]->price;
			int id=Integer.parseInt(StringAll[1]);
			String price=StringAll[2];
			Item item=DBAdapter.getItemData(id);
			if(item!=null){
				if(!item.get_price().trim().equals(price.trim())){
			DBAdapter.updateItemPrice(id, price);
			manager.updateItemPrice(id, price);
			}
			}
			//group.set_name(price1.toString());
			/*Item itm=DBAdapter.getItemData(id);
			boolean breakFlag=false;
			for (int i = 0; i < GamesFragment.ExpListItems.size(); i++) {
				   if (GamesFragment.ExpListItems.get(i).get_id()==itm.get_category().get_id()) {
					  
					   for (Item i1 : GamesFragment.ExpListItems.get(i).get_items()) {
						if(i1.get_id()==itm.get_id()){
							i1.set_price(price);
							GamesFragment.ExpAdapter.notifyDataSetChanged();
							GamesFragment.expandAll();
							 break;
						}
					}
					   
				
				  }
				   if(breakFlag){break;}
				}	*/
			
		}catch(Exception e)	{}
		
		break;
	case 112:
		try{
			//update Item seq
			//$message = "112"."^".$data[$i]->id."^".$data[$i]->seq;
			int id=Integer.parseInt(StringAll[1]);
			int seq=Integer.parseInt(StringAll[2]);
			Item item=DBAdapter.getItemData(id);
			if(item!=null){
				if(item.get_sequence()!=seq){
			DBAdapter.addItemSeq(id,seq);
			manager.addItemSeq(id,seq);
				}}
			//group.set_name(price1.toString());
		/*	Item itm=DBAdapter.getItemData(id);
			boolean breakFlag=false;
			for (int i = 0; i < GamesFragment.ExpListItems.size(); i++) {
				   if (GamesFragment.ExpListItems.get(i).get_id()==itm.get_category().get_id()) {
					  
					   for (Item i1 : GamesFragment.ExpListItems.get(i).get_items()) {
						if(i1.get_id()==itm.get_id()){
							i1.set_sequence(seq);
							Collections.sort(GamesFragment.ExpListItems.get(i).get_items(),new myItemComparable());
							GamesFragment.ExpAdapter.notifyDataSetChanged();
							GamesFragment.expandAll();
							 break;
						}
					}
					   										   
				  }
				   if(breakFlag){break;}
				}	*/
			
		}catch(Exception e)	{}
		
		break;
	case 113:
		try{
			//update item category
			//$message = "113"."^".$data[$i]->id."^".$data[$i]->cid;
			int id=Integer.parseInt(StringAll[1]);
			int cid=Integer.parseInt(StringAll[2]);
			Item item=DBAdapter.getItemData(id);
			if(item!=null){
				if(item.get_category().get_id()!=cid){
			DBAdapter.updateItemcategory(id, cid);	
			manager.updateItemcategory(id, cid);
				}}
			//group.set_name(price1.toString());
			/*Item itm=DBAdapter.getItemData(id);
			boolean breakFlag=false;
			for (int i = 0; i < GamesFragment.ExpListItems.size(); i++) {
				   if (GamesFragment.ExpListItems.get(i).get_id()==itm.get_category().get_id()) {
					  
					   for (Item i1 : GamesFragment.ExpListItems.get(i).get_items()) {
						if(i1.get_id()==itm.get_id()){
							i1.set_category(DBAdapter.getCategoryData(cid));
							GamesFragment.ExpAdapter.notifyDataSetChanged();
							GamesFragment.expandAll();
							 break;
						}
					}
				
				  }
				   if(breakFlag){break;}
				}	*/
		
			
		}catch(Exception e)	{}
		
		break;
	case 114:
		try{
			//update Item seq
			//$message = "112"."^".$data[$i]->id."^".$data[$i]->seq;
			int id1=Integer.parseInt(StringAll[1]);
			int seq1=Integer.parseInt(StringAll[2]);
			int id2=Integer.parseInt(StringAll[3]);
			int seq2=Integer.parseInt(StringAll[4]);
			Item item=DBAdapter.getItemData(id1);
			if(item!=null){
				if(item.get_sequence()!=seq1){
			DBAdapter.addItemSeq(id1,seq1);
			manager.addItemSeq(id1,seq1);
				}}
			Item item2=DBAdapter.getItemData(id2);
			if(item2!=null){
				if(item2.get_sequence()!=seq2){
			DBAdapter.addItemSeq(id2,seq2);
			manager.addItemSeq(id2,seq2);
				}}
			//group.set_name(price1.toString());
		/*	Item itm=DBAdapter.getItemData(id);
			boolean breakFlag=false;
			for (int i = 0; i < GamesFragment.ExpListItems.size(); i++) {
				   if (GamesFragment.ExpListItems.get(i).get_id()==itm.get_category().get_id()) {
					  
					   for (Item i1 : GamesFragment.ExpListItems.get(i).get_items()) {
						if(i1.get_id()==itm.get_id()){
							i1.set_sequence(seq);
							Collections.sort(GamesFragment.ExpListItems.get(i).get_items(),new myItemComparable());
							GamesFragment.ExpAdapter.notifyDataSetChanged();
							GamesFragment.expandAll();
							 break;
						}
					}
					   										   
				  }
				   if(breakFlag){break;}
				}	*/
			
		}catch(Exception e)	{}
		
		break;
	case 120:
		try{
			//update extra name 
			//$message = "120"."^".$data[$i]->id."^".$data[$i]->name;
			int id=Integer.parseInt(StringAll[1]);
			String name=StringAll[2];
			Extra extra=DBAdapter.getExtraData(id);
			if(extra!=null)
			{
				if(!extra.get_name().trim().equals(name.trim())){
			DBAdapter.updateExtraName(id, name);
			manager.updateExtraName(id, name);
			}}
			//group.set_name(price1.toString());
			/*Extra extra=DBAdapter.getExtraData(id);
			
			Item itm=DBAdapter.getItemData(extra.get_item().get_id());
			boolean breakFlag=false;
			for (int i = 0; i < GamesFragment.ExpListItems.size(); i++) {
				   if (GamesFragment.ExpListItems.get(i).get_id()==itm.get_category().get_id()) {
					  
					   for (Item i1 : GamesFragment.ExpListItems.get(i).get_items()) {
											   
						  
						   if(i1.get_id()==itm.get_id()){
							   for (Extra ex : i1.get_extra()) {
								if(ex.get_id()==extra.get_id())
								{
									ex.set_name(name);
									GamesFragment.ExpAdapter.notifyDataSetChanged();
									GamesFragment.expandAll();
									 breakFlag = true;
									break ;
									 
								}
								if(breakFlag){break;}
							}
							   if(breakFlag){break;}			
							
						}
						   if(breakFlag){break;}			   
				    
				  }
				}
				   if(breakFlag){break;}
				  
			}
			*/
			
		}catch(Exception e)	{}
		
		break;
	case 121:
		try{
			//update extra price 
			//$message = "121"."^".$data[$i]->id."^".$data[$i]->price;
			int id=Integer.parseInt(StringAll[1]);
			String price=StringAll[2];
			Extra extra=DBAdapter.getExtraData(id);
			if(extra!=null)
			{
				if(!extra.get_price().trim().equals(price.trim())){
			DBAdapter.updateExtraPrice(id,price);
			manager.updateExtraPrice(id,price);
				}}
			//group.set_name(price1.toString());
		/*	Extra extra=DBAdapter.getExtraData(id);
			boolean breakFlag=false;
			Item itm=DBAdapter.getItemData(extra.get_item().get_id());
			for (int i = 0; i < GamesFragment.ExpListItems.size(); i++) {
				   if (GamesFragment.ExpListItems.get(i).get_id()==itm.get_category().get_id()) {
					  
					   for (Item i1 : GamesFragment.ExpListItems.get(i).get_items()) {
											   
						  
						   if(i1.get_id()==itm.get_id()){
							   for (Extra ex : i1.get_extra()) {
								if(ex.get_id()==extra.get_id())
								{
									ex.set_price(price);
									
									GamesFragment.ExpAdapter.notifyDataSetChanged();
									GamesFragment.expandAll();
									 breakFlag = true;
									break ;
									 
								}
								if(breakFlag){break;}
							}
							   if(breakFlag){break;}			
							
						}
						   if(breakFlag){break;}			   
				    
				  }
				}
				   if(breakFlag){break;}
				  
			}*/
			
			
		}catch(Exception e)	{}
		
		break;
	case 122:
		try{
			//update extra seq 
			//$message = "122"."^".$data[$i]->id."^".$data[$i]->seq;
			int id=Integer.parseInt(StringAll[1]);
			int seq=Integer.parseInt(StringAll[2]);
			Extra extra=DBAdapter.getExtraData(id);
			if(extra!=null)
			{
				if(extra.get_sequence()!=seq){
			DBAdapter.addExtraSeq(id, seq);	
			manager.addExtraSeq(id, seq);	
					
				}}
			//group.set_name(price1.toString());
		/*	Extra extra=DBAdapter.getExtraData(id);
			
			Item itm=DBAdapter.getItemData(extra.get_item().get_id());
			boolean breakFlag=false;
			for (int i = 0; i < GamesFragment.ExpListItems.size(); i++) {
				   if (GamesFragment.ExpListItems.get(i).get_id()==itm.get_category().get_id()) {
					  
					   for (Item i1 : GamesFragment.ExpListItems.get(i).get_items()) {
											   
						  
						   if(i1.get_id()==itm.get_id()){
							   for (Extra ex : i1.get_extra()) {
								if(ex.get_id()==extra.get_id())
								{
									ex.set_sequence(seq);
									Collections.sort(i1.get_extra(),new myExtraComparable());
									GamesFragment.ExpAdapter.notifyDataSetChanged();
									GamesFragment.expandAll();
									 breakFlag = true;
									break ;
									 
								}
								if(breakFlag){break;}
							}
							   if(breakFlag){break;}			
							
						}
						   if(breakFlag){break;}			   
				    
				  }
				}
				   if(breakFlag){break;}
				  
			}*/
			
		
		}catch(Exception e)	{}
		
		break;
	case 123:
		try{
			//update extra seqCombo 
			// $message = "123"."^".$data[$i]->id1."^".$data[$i]->seq1."^".$data[$i]->id2."^".$data[$i]->seq2."^".$data[$i]->rid;
			int id1=Integer.parseInt(StringAll[1]);
			int seq1=Integer.parseInt(StringAll[2]);
			int id2=Integer.parseInt(StringAll[3]);
			int seq2=Integer.parseInt(StringAll[4]);
			Extra extra1=DBAdapter.getExtraData(id1);
			if(extra1!=null)
			{
				if(extra1.get_sequence()!=seq1){
			DBAdapter.addExtraSeq(id1, seq1);	
			manager.addExtraSeq(id1, seq1);	
					
				}}
			Extra extra2=DBAdapter.getExtraData(id2);
			if(extra2!=null)
			{
				if(extra2.get_sequence()!=seq2){
			DBAdapter.addExtraSeq(id2, seq2);	
			manager.addExtraSeq(id2, seq2);	
					
				}}
			
		
		}catch(Exception e)	{}
		
		break;
	
	case 130:
		try{
			//update cart table total 
			//$message = "130"."^".$data[$i]->id."^".$data[$i]->table1."^".$data[$i]->total."^".$data[$i]->rid;
			int id=Integer.parseInt(StringAll[1]);
			String table=StringAll[2];
			String total=StringAll[3];
			DBAdapter.updateCart(id,table,total);
			//aController.CartUpdate(context, "Hello");
			//MainActivity main=new MainActivity();
			//main.refreshGrid();
			
			
		
		}catch(Exception e)	{}
		
		break;
	
	case 150:
		try{
			//update restro name 
			// $message = "150"."^"."restro_name"."^".$data[$i]->value."^".$data[$i]->rid;
		 	DBAdapter.updateRestro(Integer.parseInt(StringAll[3]), StringAll[1].toString(), StringAll[2].toString());	
		
		}catch(Exception e)	{}
		
		break;
	case 160:
		try{
			//update echarge 
			// $message = "160"."^".$data[$i]->eid."^".$data[$i]->name."^".$data[$i]->qty."^".$data[$i]->param."^".$data[$i]->rid;
			DBAdapter.updateEChargebyId(Integer.parseInt(StringAll[1]), StringAll[2].toString(), StringAll[3].toString(), StringAll[4].toString());
		
		}catch(Exception e)	{}
		
		break;
	case 170:
		try{
			//update echarge 
			// $message = "160"."^".$data[$i]->eid."^".$data[$i]->name."^".$data[$i]->qty."^".$data[$i]->param."^".$data[$i]->rid;
			DBAdapter.updatePrinter(Integer.parseInt(StringAll[1]), StringAll[2].toString(), StringAll[3].toString(), StringAll[4].toString());
		
		}catch(Exception e)	{}
		
		break;
	
	case 20:
		try{
			//Delete category 
			//$message = "20"."^".$data[$i]->cid;
			int id=Integer.parseInt(StringAll[1]);
			if(DBAdapter.CheckIsDataAlreadyInDBorNot("tbl_category", "category_id", id))
			{
			DBAdapter.deleteCategory(id);
			manager.deleteCategory(id);
			}
			/*for (int i = 0; i < GamesFragment.ExpListItems.size(); i++) {
				   if (GamesFragment.ExpListItems.get(i).get_id()==id) {	
					   GamesFragment.myDepartments.remove(GamesFragment.ExpListItems.get(i).get_name());
					   GamesFragment.ExpListItems.remove(GamesFragment.ExpListItems.get(i));					   
						GamesFragment.ExpAdapter.notifyDataSetChanged();
						GamesFragment.expandAll();
				    break;
				  }
				}	*/
						
		}catch(Exception e)	{}
		
		break;
	case 21:
		try{
			//Delete Item 
			//$message = "21"."^".$data[$i]->id;
			int id=Integer.parseInt(StringAll[1]);
			
			//group.set_name(price1.toString());
			/*Item itm=DBAdapter.getItemData(id);
			 boolean breakFlag = false;
			for (int i = 0; i < GamesFragment.ExpListItems.size(); i++) {
				   if (GamesFragment.ExpListItems.get(i).get_id()==itm.get_category().get_id()) {
					 		int r=0;			  
					   for (Item i1 : GamesFragment.ExpListItems.get(i).get_items()) {
						 
						if(i1.get_id()==itm.get_id()){
							GamesFragment.ExpListItems.get(i).removeChild(r);
							//GamesFragment.ExpListItems.get(i).set_items(DBAdapter.geti)
							GamesFragment.ExpAdapter.notifyDataSetChanged();
							GamesFragment.expandAll();
							 break;
						}
						++r;
					}
					   
							   
				  }
				   if(breakFlag){break;}
				}	*/
			if(DBAdapter.CheckIsDataAlreadyInDBorNot("tbl_item", "item_id", id))
			{
			manager.deleteItem(id);
			DBAdapter.deleteItem(id);
			}
		
			
		}catch(Exception e)	{}
		
		break;
	case 22:
		try{
			//delete Extra 
			//$message = "22"."^".$data[$i]->id;
			int id=Integer.parseInt(StringAll[1]);
			
			
			//group.set_name(price1.toString());
		/*	Extra extra=DBAdapter.getExtraData(id);
				
			Item itm=DBAdapter.getItemData(extra.get_item().get_id());
			boolean breakFlag=false;
			for (int i = 0; i < GamesFragment.ExpListItems.size(); i++) {
				   if (GamesFragment.ExpListItems.get(i).get_id()==itm.get_category().get_id()) {
					  
					   for (Item i1 : GamesFragment.ExpListItems.get(i).get_items()) {
											   
						  
						   if(i1.get_id()==itm.get_id()){
							  int r=0;
							   for (Extra ex : i1.get_extra()) {
								  
								if(ex.get_id()==extra.get_id())
								{
									i1.removeChild(r);
									GamesFragment.ExpAdapter.notifyDataSetChanged();
									GamesFragment.expandAll();
									 breakFlag = true;
									break ;
									 
								}
								++r;
								if(breakFlag){break;}
							}
							   if(breakFlag){break;}			
							
						}
						   if(breakFlag){break;}			   
				    
				  }
				}
				   if(breakFlag){break;}
				  
			}*/
			if(DBAdapter.CheckIsDataAlreadyInDBorNot("tbl_extra", "extra_id", id))
			{
			manager.deleteExtra(id);
			DBAdapter.deleteExtra(id);
			}
		}catch(Exception e)	{}
		
		break;
	case 26:
		try{
			//delete Echarge
			//$message = "26"."^".$data[$i]->eid."^".$data[$i]->rid;
			int id=Integer.parseInt(StringAll[1]);			
			
			if(DBAdapter.CheckIsDataAlreadyInDBorNot("tbl_echarge", "echarge_id", id))
			{
			//manager.deleteExtra(id);
			DBAdapter.deleteServiceTax("Service");
			}
		}catch(Exception e)	{}
		
		break;
	case 27:
		try{
			//delete Echarge
			//$message = "26"."^".$data[$i]->eid."^".$data[$i]->rid;
			int id=Integer.parseInt(StringAll[1]);			
			
			if(DBAdapter.CheckIsDataAlreadyInDBorNot("tbl_printer", "printer_id", id))
			{
			//manager.deleteExtra(id);
			DBAdapter.deletePrinter(id);
			}
		}catch(Exception e)	{}
		
		break;
	case 28:
		try{
			//delete Echarge
			//$message = "26"."^".$data[$i]->eid."^".$data[$i]->rid;
			int id=Integer.parseInt(StringAll[1]);			
			
			if(DBAdapter.CheckIsDataAlreadyInDBorNot("tbl_staff", "sid", id))
			{
			//manager.deleteExtra(id);
			DBAdapter.deleteStaff(id);
			}
		}catch(Exception e)	{}
		
		break;

	default:
		break;
	}
}
}
