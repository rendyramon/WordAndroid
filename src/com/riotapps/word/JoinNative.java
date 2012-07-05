package com.riotapps.word;

import com.riotapps.word.hooks.PlayerService;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.DesignByContractException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class JoinNative extends Activity implements View.OnClickListener{
	    /** Called when the activity is first created. */
		
	    final Context context = this;		
		Button bCancel; 
		Button bSave;
		EditText tEmail;
		EditText tNickname;
		EditText tPassword;
		
		//EditText 
		
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.joinnative);
	        
	        bCancel = (Button) findViewById(R.id.bCancel);
	        bSave = (Button) findViewById(R.id.bSave);
	        TextView tvTitle =(TextView)findViewById(R.id.title_join_native);
	        TextView tvSubTitle =(TextView)findViewById(R.id.sub_title_join_native);
	        TextView tvEmailLabel =(TextView)findViewById(R.id.email_label);
	        TextView tvEmailGuarantee =(TextView)findViewById(R.id.email_guarantee);
	        Typeface face=Typeface.createFromAsset(getAssets(),"fonts/FullDeceSans1.0.ttf");

	        tvTitle.setTypeface(face);
	        tvSubTitle.setTypeface(face);
	        tvEmailLabel.setTypeface(face);
	        tvEmailGuarantee.setTypeface(face);
	        
	        bCancel.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					finish();      
				}
			});
	        
	        
	        bSave.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					PlayerService playerSvc = new PlayerService();
					
					EditText tEmail = (EditText) findViewById(R.id.tEmail);
					EditText tNickname = (EditText) findViewById(R.id.tNickname);
					EditText tPassword = (EditText) findViewById(R.id.tPassword);
					
					try{
						//show spinner
						playerSvc.CreatePlayer(context, tEmail.getText().toString(), 
								tNickname.getText().toString(), 
								tPassword.getText().toString());
					} 
					catch (DesignByContractException dbEx) {
					 	DialogManager.SetupOKDialog(context, getString(R.string.oops), dbEx.getMessage());					}
					catch (Exception ex) {
						//log this, figure out logging
					 	DialogManager.SetupOKDialog(context, getString(R.string.error_title), getString(R.string.error_message));						
					}
				 
				}
			});
	    }

	    @Override 
	    public void onClick(View v) {
	        // do something when the button is clicked
	      }
	    
	}
