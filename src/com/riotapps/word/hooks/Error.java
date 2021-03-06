package com.riotapps.word.hooks;

import java.util.TreeMap;

public class Error {
	private int code;
	
	public enum ErrorType{
		NO_TRANSLATION(0),
		INCORRECT_PASSWORD(1),
		EMAIL_NOT_SUPPLIED(2),
		NICKNAME_IN_USE(3),
		NICKNAME_NOT_SUPPLIED(4),
		EMAIL_IN_USE(5),
		UNAUTHORIZED(6),
		FB_USER_EMAIL_ALREADY_IN_USE(7);
				
		private final int value;
		private ErrorType(int value) {
		    this.value = value;
		 }
		
	  public int value() {
		    return value;
		  }
		
	  private static TreeMap<Integer, ErrorType> _map;
	  static {
		_map = new TreeMap<Integer, ErrorType>();
	    for (ErrorType num: ErrorType.values()) {
	    	_map.put(new Integer(num.value()), num);
	    }
	    //no translation
	    if (_map.size() == 0){
	    	_map.put(new Integer(0), NO_TRANSLATION);
	    }
	  }
	  
	  public static ErrorType lookup(int value) {
		  return _map.get(new Integer(value));
	  	}
	}
	
 
	
	private String message;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public ErrorType getErrorType(){
		return ErrorType.lookup(this.code);
	}
}
