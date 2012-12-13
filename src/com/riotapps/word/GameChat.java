package com.riotapps.word;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectTimeoutException;

import com.google.gson.Gson;
import com.riotapps.word.hooks.Chat;
import com.riotapps.word.hooks.Game;
import com.riotapps.word.hooks.GameService;
import com.riotapps.word.hooks.PlayedWord;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.ui.GameAction.GameActionType;
import com.riotapps.word.utils.AsyncNetworkRequest;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.utils.ImageCache;
import com.riotapps.word.utils.ImageFetcher;
import com.riotapps.word.utils.Logger;
import com.riotapps.word.utils.ServerResponse;
import com.riotapps.word.utils.Enums.RequestType;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class GameChat extends FragmentActivity implements  View.OnClickListener{
	private static final String TAG = GameChat.class.getSimpleName();
	private Game game;
	private Player player;
	EditText etText;
	NetworkTask runningTask = null;
	private Context context;
	private ImageFetcher imageLoader;
	ListView lvChat;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamechat);
		this.context = this;
		
		
	 	Intent i = getIntent();
	 	String gameId = i.getStringExtra(Constants.EXTRA_GAME_ID);
	 	this.game = GameService.getGameFromLocal(gameId); 
		
	    this.player = PlayerService.getPlayerFromLocal(); 
	 	GameService.loadScoreboard(this, this.game, this.player);
	 	
	 	Button bSave = (Button) findViewById(R.id.bSave);
	 	bSave.setOnClickListener(this);
	 	this.etText = (EditText) findViewById(R.id.etText);
	 	
	 	this.imageLoader = new ImageFetcher(this, Constants.DEFAULT_AVATAR_SIZE, Constants.DEFAULT_AVATAR_SIZE, 0);
	    this.imageLoader.setImageCache(ImageCache.findOrCreateCache(this, Constants.IMAGE_CACHE_DIR));
	 	
	 	this.loadList();
	}
	
	
	private void loadList(){ 
		
		//Collections.reverse(this.game.getChats());
 
		ChatArrayAdapter adapter = new ChatArrayAdapter(this, this.game.getChats().toArray(new Chat[this.game.getChats().size()]));

		this.lvChat = (ListView) findViewById(R.id.lvChat);
		this.lvChat.setAdapter(adapter); 
	//	this.lvChat.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
	//	this.lvChat.setStackFromBottom(true);
	 
	}
	
	private class ChatArrayAdapter extends ArrayAdapter<Chat> {
	   	  private final GameChat context;
	   	  private final Chat[] values;
	   	  private String prevPlayerId = "";
	   	  private int prevSequentialCount = 0;
	   	  LayoutInflater inflater;
	   	//  public ArrayList<Integer> selectedIds = new ArrayList<Integer>();

	   	  public ChatArrayAdapter(GameChat context, Chat[] values) {
	   	    super(context, R.layout.gamechatitem, values);
	    	    this.context = context;
	    	    this.values = values;
 	    
	    	    this.inflater = (LayoutInflater) context
		    	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    	  }

	    	  @Override
	    	  public View getView(int position, View rowView, ViewGroup parent) {
	    		 
	    		  if ( rowView == null ) {
	    			  rowView = inflater.inflate(R.layout.gamechatitem, parent, false);
	    		  }
	    		  
		    	  Chat chat = values[position];
		    	 
		    	   TextView tvChat = (TextView) rowView.findViewById(R.id.tvChat);

	    		   ImageView ivPlayer = (ImageView)rowView.findViewById(R.id.ivPlayer);

	    		   
	    		   
		    	   if (this.prevPlayerId != chat.getPlayerId() || (this.prevPlayerId != chat.getPlayerId() && this.prevSequentialCount >= 4) ){
		    		   this.prevSequentialCount = 0;
		    		   Player player = context.game.getPlayerById(chat.getPlayerId());
		    	   
		    		   imageLoader.loadImage(player.getImageUrl(), ivPlayer);
		    		   ivPlayer.setVisibility(View.VISIBLE);
		    	   }
		    	   else{ 
		    		   ivPlayer.setVisibility(View.INVISIBLE);
		    		   this.prevSequentialCount += 1;
		    	   }
		    	   
		    	   tvChat.setText(chat.getText());
		    	   
		    	   this.prevPlayerId = chat.getPlayerId(); 
		    	   
		 
		    	//   Logger.d(TAG, "adapter position=" + position + " count=" + this.wordCount); 
		    	//   LinearLayout llBottomBorder = (LinearLayout)rowView.findViewById(R.id.llBottomBorder);
		    	 
		    	    
		    	  /* if (position == this.wordCount - 1){ //last item
		    		   Logger.d(TAG, "position=wordCount");
			   			RelativeLayout rlLineItem = (RelativeLayout)rowView.findViewById(R.id.rlItem);
			   			int bgLineItem = context.getResources().getIdentifier("com.riotapps.word:drawable/text_selector_bottom", null, null);
			   			rlLineItem.setBackgroundResource(bgLineItem);
			   			//LinearLayout llBottomBorder = (LinearLayout)rowView.findViewById(R.id.llBottomBorder);
			   			llBottomBorder.setVisibility(View.INVISIBLE);
		    	   }
		    	   else{
		    		   llBottomBorder.setVisibility(View.VISIBLE);
		    	   }
		    	  */
		    	   
		    	   return rowView;
	    	  } 
	} 	
	
	
	@Override
	public void onClick(View v) {

    	switch(v.getId()){  
	        case R.id.bSave:  
	        	saveChat();
				break;
    	}
		
	}
	
    private void saveChat(){	
    	
		try { 
			if ( this.etText.getText().toString().length() > 0) {
		
				String json = GameService.setupGameChat(context, this.game, etText.getText().toString());
				
				//kick off thread to cancel game on server
				runningTask = new NetworkTask(this, RequestType.POST, json,  getString(R.string.progress_sending));
				runningTask.execute(Constants.REST_GAME_CHAT);
			}
		} catch (DesignByContractException e) {
			 
			DialogManager.SetupAlert(context, context.getString(R.string.sorry), e.getMessage());  
		}
    }
    
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onPause called");
		super.onPause();
		if (this.runningTask != null){
    		this.runningTask.cancel(true);
    	}
	}
	  
    private class NetworkTask extends AsyncNetworkRequest{
			
	    	GameChat context;
	     		
    		public NetworkTask(GameChat ctx, RequestType requestType,
    				String json,
    				String shownOnProgressDialog) {
    			super(ctx, requestType, shownOnProgressDialog, json);
    			this.context = ctx;
    		 
    		}

    		@Override
    		protected void onPostExecute(ServerResponse serverResponseObject) {
    		 
    			super.onPostExecute(serverResponseObject);
    			
    			this.handleResponse(serverResponseObject);

    		}
     
    		private void handleResponse(ServerResponse serverResponseObject){
    		     HttpResponse response = serverResponseObject.response;   
    		     Exception exception = serverResponseObject.exception;   

    		     if(response != null){  

    		         InputStream iStream = null;  

    		         try {  
    		             iStream = response.getEntity().getContent();  
    		         } catch (IllegalStateException e) {  
    		             Log.e("in ResponseHandler -> in handleResponse() -> in if(response !=null) -> in catch ","IllegalStateException " + e);  
    		         } catch (IOException e) {  
    		             Log.e("in ResponseHandler -> in handleResponse() -> in if(response !=null) -> in catch ","IOException " + e);  
    		         }  

    		         int statusCode = response.getStatusLine().getStatusCode();  
    		         
    		         Log.i(GameChat.TAG, "StatusCode: " + statusCode);
    		       

    		         switch(statusCode){  
    		             case 200:  
    		             case 201:   
		            	   
		            	 		//refresh game board
		            	 		game = GameService.handleGameChatResponse(context, iStream);
		            	 		
		            	 		//refresh the list
		            			etText.setText("");
		            	 		loadList();
		            	 		
		            	 		break;
		            	 		//end of case 200 & 201 
    		           
    		             case 401:
    			             //case Status code == 422
    			            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.sorry), this.context.getString(R.string.validation_unauthorized));  
    			            	 break;
    		             case 404:
    		             //case Status code == 422
    		            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.sorry), this.context.getString(R.string.find_player_opponent_not_found), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);  
    		            	 break;
    		             case 422: 
    		             case 500:

    		            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.oops), statusCode + " " + response.getStatusLine().getReasonPhrase(), 0);  
    		            	 break;
    		         }  
    		     }else if (exception instanceof ConnectTimeoutException) {
    		    	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.oops), this.context.getString(R.string.msg_connection_timeout), 0);
    		     }else if(exception != null){  
    		    	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.oops), this.context.getString(R.string.msg_not_connected), 0);  

    		     }  
    		     else{  
    		         Log.v("in ResponseHandler -> in handleResponse -> in  else ", "response and exception both are null");  

    		     }//end of else  
    		}
    		
     
    	}


}
