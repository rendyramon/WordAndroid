package com.riotapps.word;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectTimeoutException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.hooks.TransportAuthToken;
import com.riotapps.word.ui.GameSurfaceView;
import com.riotapps.word.utils.*;
import com.riotapps.word.utils.Enums.RequestType;
import com.riotapps.word.utils.Enums.ResponseHandlerType;

public class Splash  extends Activity {
   
	private static final String TAG = Splash.class.getSimpleName();

    final Context context = this;
    Splash me = this;
    Handler handler;
    long startTime = System.nanoTime();
    
    public void test(){}
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
 

        this.handlePreProcessing();
     }
    
    private void handlePreProcessing(){
		SharedPreferences settings = this.getSharedPreferences(Constants.USER_PREFS, 0);
	    String storedToken = settings.getString(Constants.USER_PREFS_USER_ID, "");
	       
	    if (storedToken.length() > 0){
	    	TransportAuthToken authToken = new TransportAuthToken();
			authToken.setToken(storedToken);
			Gson gson = new Gson();
			String json = gson.toJson(authToken);
			
			//ok lets call the server now
			new NetworkTask(this, RequestType.POST, json).execute(Constants.REST_AUTHENTICATE_PLAYER_BY_TOKEN);
	    }
	    else{
	    	 Log.w(TAG, "about to execute CheckConnectivityTask");
	    	new CheckConnectivityTask().execute("");
	     

	    }
    }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	private class CheckConnectivityTask extends AsyncTask<String, Void, Boolean> {

		 @Override
		    protected void onPostExecute(Boolean result) {
	    	 Log.w(TAG, " CheckConnectivityTask onPostExecute");

	    	 processConnectivityResults(result);
		    }

			@Override
			protected Boolean doInBackground(String... arg0) {
				 Log.w(TAG, " CheckConnectivityTask doInBackground");
				return checkInitialConnectivity();
			}
	  }

	
	private Boolean checkInitialConnectivity(){
		  NetworkConnectivity connection = new NetworkConnectivity(this);
	        //are we connected to the web?
	        boolean isConnected = connection.checkNetworkConnectivity();
	        
	        if (isConnected == false)  
	        {
	        	try {
					Thread.sleep(Constants.INITIAL_CONNECTIVITY_THREAD_SLEEP);
					isConnected = connection.checkNetworkConnectivity();
					
		        } 
	        	catch (InterruptedException e1) {
	        		isConnected = false;
					e1.printStackTrace();
				}
	        }
	        else{
	        	 long currentTime = System.nanoTime();
	        	 
            	 //default time in which to leave splash up
            	 if (Utils.convertNanosecondsToMilliseconds(currentTime -  this.startTime) < Constants.SPLASH_ACTIVITY_TIMEOUT){
            		 try {
            			 Log.w(TAG, " checkInitialConnectivity OK , about to sleep" + Utils.convertNanosecondsToMilliseconds(currentTime -  this.startTime)  + " milliseconds" ); 
						Thread.sleep(Constants.SPLASH_ACTIVITY_TIMEOUT - Utils.convertNanosecondsToMilliseconds(currentTime -  this.startTime));
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
            	 }
	        }
	        
	       return isConnected;
	}
	
	private void processConnectivityResults(Boolean connected){
	   	 Log.w(TAG, " processTaskResults" );  
		 if (connected == true) 
	        {
	        	Intent intent = new Intent(this, com.riotapps.word.Welcome.class);
	 	      	this.startActivity(intent); 	
	        }
		 else{
			 DialogManager.SetupAlert(context, getString(R.string.oops), getString(R.string.msg_not_connected), true, 0);
		 }
	}

	
	
	private class NetworkTask extends AsyncNetworkRequest{

		Splash context;
		
		public NetworkTask(Splash ctx, RequestType requestType,
				String jsonPost) {
			super(ctx, requestType, "", jsonPost);
			this.context = ctx;
		 
		}

		@Override
		protected void onPostExecute(ServerResponse serverResponseObject) {
			// TODO Auto-generated method stub
			super.onPostExecute(serverResponseObject);
			
			this.context.handleResponse(serverResponseObject);

		}
 
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
	         
	         Log.i(Splash.TAG, "StatusCode: " + statusCode);

	         switch(statusCode){  
	             case 200:  
	            	 try{
	            		 PlayerService.setContextPlayer(this.context, iStream);
	            		 
	            		 long currentTime = System.nanoTime();
		            	 
		            	 //default time in which to leave splash up
		            	 if (Utils.convertNanosecondsToMilliseconds(currentTime -  this.startTime) < Constants.SPLASH_ACTIVITY_TIMEOUT){
		            		 Thread.sleep(Constants.SPLASH_ACTIVITY_TIMEOUT - Utils.convertNanosecondsToMilliseconds(currentTime -  this.startTime));
		            	 }
	            		 
	            		 Intent intent = new Intent( this, com.riotapps.word.TestLanding.class);
			     	     this.startActivity(intent);
	            	 }
	            	 catch(Exception e){
	            		 Log.w(TAG, e.getLocalizedMessage());
	            		 DialogManager.SetupAlert(this, this.getString(R.string.sorry), e.getLocalizedMessage(), true, 0); 
	            	 }
	               break;
	             case 401:    
	                //unauthorized
	            	 //clear local storage and send to login
	            	 PlayerService.clearLocalStorage(this.context);
	            	 
	            	 long currentTime = System.nanoTime();
	            	 
	            	 //default time in which to leave splash up
	            	 if (currentTime -  this.startTime > Constants.SPLASH_ACTIVITY_TIMEOUT){
	            		 try {
							Thread.sleep(currentTime -  this.startTime);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
	            	 }
	            	 Intent intent = new Intent( this, com.riotapps.word.Welcome.class);
		     	     this.startActivity(intent);
		     	     break;  

	             case 404:
	            	 DialogManager.SetupAlert(this, this.getString(R.string.sorry), this.getString(R.string.server_404_error), true, 0);
	             case 422: 
	             case 500:

	            	 DialogManager.SetupAlert(this, this.getString(R.string.oops), statusCode + " " + response.getStatusLine().getReasonPhrase(), true, 0);  
	         }  
	     }else if (exception instanceof ConnectTimeoutException) {
	    	 DialogManager.SetupAlert(this, this.getString(R.string.oops), this.getString(R.string.msg_connection_timeout), 0);
	     }else if(exception != null){  
	    	 DialogManager.SetupAlert(this, this.getString(R.string.oops), this.getString(R.string.msg_not_connected), 0);  

	     }  
	     else{  
	         Log.v("in ResponseHandler -> in handleResponse -> in  else ", "response and exception both are null");  

	     }//end of else  
	}
}
	
 
