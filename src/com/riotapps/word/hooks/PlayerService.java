package com.riotapps.word.hooks;

import com.riotapps.word.R;
import com.riotapps.word.utils.ApplicationContext;
import com.riotapps.word.utils.DesignByContractException;
import com.riotapps.word.utils.PreconditionException;
import com.riotapps.word.utils.Check;
import com.riotapps.word.utils.Validations;
 

public class PlayerService {

	public Player GetPlayer(String id){
		//retrieve player from server
		//convert using gson
		//return player to caller
		return new Player();
	}
	
	
	public Player SavePlayer(String id){
		//retrieve player from server
		//convert using gson
		//return player to caller
		
		//check validations here
		
		return new Player();
	}
	
	
	public Player PutPlayer(String email, String nickname, String password) throws DesignByContractException{
		//retrieve player from server
		//convert using gson
		//return player to caller
		//Player player = new Player();
		
		//player.setEmail(email);
	//	player.se
		
		Check.Require(email.length() > 0, ApplicationContext.getAppContext().getString(R.string.validation_email_required));
		Check.Require(Validations.ValidateEmail(email) == true, ApplicationContext.getAppContext().getString(R.string.validation_email_invalid));
	 
		
		
		//check validations here
		
		return new Player();
	}
}