package com.riotapps.word.utils;

public class Constants {

	/**============================================
	 * requestType
	 *=============================================*/
	public static final int GET_REQUEST = 1;
	public static final int POST_REQUEST = 2;
	public static final int PUT_REQUEST = 3;
	public static final int DELETE_REQUEST = 4;


	/**=============================================
	 * responseHandleBy
	 *==============================================*/
	public static final int USER_LISTING = 100;
	public static final int USER_ADD_OR_EDIT_DONE = 101;	
	public static final int USER_LISTING_DELETE = 102;

	/**=============================================
	 * creatingXMLParser for class
	 *==============================================*/
	public static final int XML_PARSER_FOR_USERS = 200;
	public static final int XML_PARSER_FOR_ERRORS = 201;

	/**=============================================
	 * mode of page
	 *==============================================*/
	public static final String MODE_OF_PAGE = "MODE_OF_PAGE";
	public static final int PAGE_ADD = 300;
	public static final int PAGE_EDIT = 301;	

	/**=============================================
	 * the web
	 *==============================================*/
	public static final String REST_URL_SITE = "http://";
	public static final String FACEBOOK_API_ID = "314938401925933";
	public static final String REST_CREATE_PLAYER = "players/create";
	
	/**=============================================
	 * rails
	 *==============================================*/
	public static final String REST_METHOD = "_method";
	public static final String PUT_VERB = "PUT";
	public static final String DELETE_VERB = "DELETE";
}//end class Constants
 
