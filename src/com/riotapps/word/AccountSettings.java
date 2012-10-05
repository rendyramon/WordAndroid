package com.riotapps.word;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectTimeoutException;

 
import com.riotapps.word.hooks.Error.ErrorType;
import com.riotapps.word.hooks.ErrorService;
import com.riotapps.word.hooks.Player;
import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.utils.AsyncNetworkRequest;
import com.riotapps.word.utils.Constants;
import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.ui.DialogManager;
import com.riotapps.word.utils.ServerResponse;
import com.riotapps.word.utils.Enums.RequestType;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AccountSettings extends FragmentActivity implements View.OnClickListener{
		private static final String TAG = AccountSettings.class.getSimpleName();
		
	    final Context context = this;		
		 
		EditText tEmail;
		EditText tNickname;
		EditText tPassword;
		
		//EditText 
		
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.accountsettings);
	        
	        Player player = PlayerService.getPlayerFromLocal();		 
	        PlayerService.loadPlayerInHeader(this);
	        
	        tEmail = (EditText)findViewById(R.id.tEmail);
	        tNickname = (EditText)findViewById(R.id.tNickname);
	        tPassword = (EditText)findViewById(R.id.tPassword);	        

	        tEmail.setText(player.getEmail());
	        tNickname.setText(player.getNickname());
	        
	        Button bSave = (Button) findViewById(R.id.bSave);
	        bSave.setOnClickListener(this);
	        Button bSavePwd = (Button) findViewById(R.id.bSavePwd);
	        bSavePwd.setOnClickListener(this);
	    }

	    @Override 
	    public void onClick(View v) {
	    	switch(v.getId()){  
	        case R.id.bSave:  
	            this.processAccountUpdate();
	            break; 
	    	case R.id.bSavePwd:  
	    		this.processPassword();
	    		break;  
	    	}  
	      }
	    
	    private void processAccountUpdate(){
	    	
	    	try {
				EditText tEmail = (EditText) findViewById(R.id.tEmail);
				EditText tNickname = (EditText) findViewById(R.id.tNickname);
				
				String json = PlayerService.setupAccountUpdate(this, tEmail.getText().toString(), tNickname.getText().toString());
				
				//kick off thread
				new NetworkTask(this, RequestType.POST, getString(R.string.progress_updating), json, false).execute(Constants.REST_PLAYER_UPDATE_ACCOUNT);
				
			} 
			catch (DesignByContractException e) {
				//e.printStackTrace();
				DialogManager.SetupAlert(this.context, getString(R.string.oops), e.getMessage(), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);  
			}
	    }
    
	    private void processPassword(){
	    	
	    	try {
				EditText tConfirmPassword = (EditText) findViewById(R.id.tConfirmPassword);
				EditText tPassword = (EditText) findViewById(R.id.tPassword);
				
				String json = PlayerService.setupPasswordChange(this, tPassword.getText().toString(), tConfirmPassword.getText().toString());
				
				//kick off thread
				new NetworkTask(this, RequestType.POST, getString(R.string.progress_updating), json, true).execute(Constants.REST_PLAYER_CHANGE_PASSWORD);
				
			} 
			catch (DesignByContractException e) {
				//e.printStackTrace();
				DialogManager.SetupAlert(this.context, getString(R.string.oops), e.getMessage(), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);  
			}
	    }
	    
	    private class NetworkTask extends AsyncNetworkRequest{
			
	    	AccountSettings context;
	    	Boolean isPasswordChange = false;
			
			public NetworkTask(AccountSettings ctx, RequestType requestType,
					String shownOnProgressDialog, String jsonPost, Boolean isPasswordChange) {
				super(ctx, requestType, shownOnProgressDialog, jsonPost);
				this.context = ctx;
				this.isPasswordChange = isPasswordChange;
			 
			}

			@Override
			protected void onPostExecute(ServerResponse serverResponseObject) {
				// TODO Auto-generated method stub
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
			         
			         Log.i(AccountSettings.TAG, "StatusCode: " + statusCode);
 
			         switch(statusCode){  
			             case 200:  
			             case 201: {   
			                //update text
			            	 if (this.isPasswordChange){
			            		Player player = PlayerService.handleChangePasswordResponse(this.context, iStream);
			            		 
			            		EditText tConfirmPassword = (EditText) findViewById(R.id.tConfirmPassword);
			     				EditText tPassword = (EditText) findViewById(R.id.tPassword);
			     				tConfirmPassword.setText("");
			     				tPassword.setText("");
			     				
			     			    DialogManager.SetupAlert(this.context, this.context.getString(R.string.success), this.context.getString(R.string.password_changed_successfully),Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);
						           
			            	 }
			            	 else{
			            		 Player player = PlayerService.handleUpdateAccountResponse(this.context, iStream);
			            		 
			            		 //clear image cache just in case email was changed.
			            		 PlayerService.clearImageCache(this.context);
			            		 PlayerService.loadPlayerInHeader(this.context);
				     			 DialogManager.SetupAlert(this.context, this.context.getString(R.string.success), this.context.getString(R.string.account_changed_successfully),Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);
			            	 }
			            		 
			                 break;  

			             }//end of case 200 & 201 
			             case 401:
			            	 ErrorType errorType = ErrorService.translateError(this.context, iStream);
			            	 
			            	 String errorMessage;
			            	 
			            	 switch (errorType){
			            	 	case INCORRECT_PASSWORD:
					            	errorMessage = this.context.getString(R.string.validation_incorrect_password);
			            	 		break; 
			            	 	case EMAIL_NOT_SUPPLIED:
			            	 		errorMessage = this.context.getString(R.string.validation_email_not_supplied);
			            	 		break;
			            	 	case NICKNAME_IN_USE:
			            	 		errorMessage = this.context.getString(R.string.validation_nickname_already_taken);
			            	 		break;
			            	 	case EMAIL_IN_USE:
			            	 		errorMessage = this.context.getString(R.string.validation_email_already_taken);
			            	 		break;
			            	 	case NICKNAME_NOT_SUPPLIED:
			            	 		errorMessage = this.context.getString(R.string.validation_nickname_not_supplied);
			            	 		break;
			            	 	case UNAUTHORIZED:
			            	 		errorMessage = this.context.getString(R.string.validation_unauthorized);
			            	 		break;
			            	 	default:
			            	 		errorMessage = this.context.getString(R.string.validation_unspecified_error);
			            	 		break;		            	 		
			            	 }
			            	 
			            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.sorry), errorMessage);
			            	 break;
			            	 
			             case 404:
			             //case Status code == 422
			            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.sorry), this.context.getString(R.string.validation_404_error), Constants.DEFAULT_DIALOG_CLOSE_TIMER_MILLISECONDS);  
			            	 break;
			             case 422: 
			             case 500:

			            	 DialogManager.SetupAlert(this.context, this.context.getString(R.string.oops), statusCode + " " + response.getStatusLine().getReasonPhrase(), 0);  
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