package com.riotapps.word.hooks;

import android.os.Parcel;
import android.os.Parcelable;

import com.riotapps.word.utils.Constants;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Player implements Parcelable{
	
	public Player(){}
	
	private String id;
	
	@Expose
	@SerializedName("n_n")
	private String nickname;
	
	@Expose
	@SerializedName("f_n")
	private String firstName;
	
	@Expose
	@SerializedName("l_n")
	private String lastname;
	
	@Expose
	@SerializedName("e_m")
	private String email;
	
	private String gravatar;
	
	@Expose
	private String password;
	
	@Expose
	private String fb = "";
	
	@SerializedName("a_t")
	private String auth_token;
	
	@SerializedName("n_w")
	private int numWins = 0; //num wins
	
	@SerializedName("n_l")
	private int numLosses = 0; //num losses
	
	@SerializedName("n_d")
	private int numDraws = 0; //num draws
	
	private String badge_drawable = "";

	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return this.id;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname.trim();
	}
	public String getNickname() {
		return this.nickname;
	}
	public void setEmail(String email) {
		this.email = email.trim();
	}
	public String getEmail() {
		return this.email;
	}
	public void setPassword(String password) {
		this.password = password.trim();
	}
	public String getPassword() {
		return this.password;
	}
	public void setFB(String fb) {
		this.fb = fb.trim();
	}
	public String getFB() {
		return this.fb;
	}
	public void setAuthToken(String auth_token) {
		this.auth_token = auth_token.trim();
	}
	public String getAuthToken() {
		return this.auth_token;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public boolean isFacebookUser(){
		return this.fb.length() > 0;
	}
	
	public int getNumGames(){
		return this.numWins + this.numLosses + this.numDraws;
	}
	
	public String getFullName(){
		return this.firstName + " " + this.lastname;
	}
	
	public String getAbbreviatedName(){
		return this.firstName + (this.lastname.length() > 0 ? " " + this.lastname.substring(0,1) + "." : "");
	}
	
	public int getNumWins() {
		return numWins;
	}
	public void setNumWins(int numWins) {
		this.numWins = numWins;
	}
	public int getNumLosses() {
		return numLosses;
	}
	public void setNumLosses(int numLosses) {
		this.numLosses = numLosses;
	}
	public int getNumDraws() {
		return numDraws;
	}
	public void setNumDraws(int numDraws) {
		this.numDraws = numDraws;
	}

	
	public String getGravatar() {
		return gravatar;
	}
	public void setGravatar(String gravatar) {
		this.gravatar = gravatar;
	}
	
	public String getFb() {
		return fb;
	}
	public void setFb(String fb) {
		this.fb = fb;
	}
	public String getAuth_token() {
		return auth_token;
	}
	public void setAuth_token(String auth_token) {
		this.auth_token = auth_token;
	}
	
	
	public String getBadgeDrawable(){
		if (this.numWins == 0) {
			return Constants.BADGE_0;
		}
		if (this.numWins >= 1 && this.numWins <= 4) {
			return Constants.BADGE_1_4;
		}
		if (this.numWins >= 5 && this.numWins <= 9) {
			return Constants.BADGE_5_9;
		}
		if (this.numWins >= 10 && this.numWins <= 14) {
			return Constants.BADGE_10_14;
		}
		if (this.numWins >= 15 && this.numWins <= 19) {
			return Constants.BADGE_1_4;
		}
		if (this.numWins >= 1 && this.numWins <= 4) {
			return Constants.BADGE_15_19;
		}
		if (this.numWins >= 20 && this.numWins <= 24) {
			return Constants.BADGE_20_24;
		}
		if (this.numWins >= 25 && this.numWins <= 29) {
			return Constants.BADGE_25_29;
		}
		if (this.numWins >= 30 && this.numWins <= 39) {
			return Constants.BADGE_30_39;
		}
		if (this.numWins >= 40 && this.numWins <= 49) {
			return Constants.BADGE_40_49;
		}
		if (this.numWins >= 50 && this.numWins <= 59) {
			return Constants.BADGE_50_59;
		}
		if (this.numWins >= 60 && this.numWins <= 69) {
			return Constants.BADGE_60_69;
		}
		if (this.numWins >= 70 && this.numWins <= 79) {
			return Constants.BADGE_70_79;
		}
		if (this.numWins >= 80 && this.numWins <= 89) {
			return Constants.BADGE_80_89;
		}
		if (this.numWins >= 90 && this.numWins <= 99) {
			return Constants.BADGE_90_99;
		}
		if (this.numWins >= 100 && this.numWins <= 124) {
			return Constants.BADGE_100_124;
		}
		if (this.numWins >= 125 && this.numWins <= 149) {
			return Constants.BADGE_125_149;
		}
		if (this.numWins >= 150 && this.numWins <= 174) {
			return Constants.BADGE_150_174;
		}
		if (this.numWins >= 175 && this.numWins <= 199) {
			return Constants.BADGE_175_199;
		}
		if (this.numWins >= 200 && this.numWins <= 224) {
			return Constants.BADGE_200_224;
		}
		if (this.numWins >= 225 && this.numWins <= 249) {
			return Constants.BADGE_225_249;
		}
		if (this.numWins >= 250 && this.numWins <= 274) {
			return Constants.BADGE_250_274;
		}
		if (this.numWins >= 275 && this.numWins <= 299) {
			return Constants.BADGE_275_299;
		}
		if (this.numWins >= 300 && this.numWins <= 349) {
			return Constants.BADGE_300_349;
		}
		if (this.numWins >= 350 && this.numWins <= 399) {
			return Constants.BADGE_350_399;
		}
		if (this.numWins >= 400 && this.numWins <= 449) {
			return Constants.BADGE_400_449;
		}
		if (this.numWins >= 450 && this.numWins <= 499) {
			return Constants.BADGE_450_499;
		}
		if (this.numWins >= 500 && this.numWins <= 599) {
			return Constants.BADGE_500_599;
		}
		if (this.numWins >= 600 && this.numWins <= 699) {
			return Constants.BADGE_600_699;
		}
		if (this.numWins >= 700 && this.numWins <= 799) {
			return Constants.BADGE_700_799;
		}
		if (this.numWins >= 800 && this.numWins <= 899) {
			return Constants.BADGE_800_899;
		}
		if (this.numWins >= 900 && this.numWins <= 999) {
			return Constants.BADGE_900_999;
		}
		if (this.numWins >= 1000 && this.numWins <= 1249) {
			return Constants.BADGE_1000_1249;
		}
		if (this.numWins >= 1250 && this.numWins <= 1499) {
			return Constants.BADGE_1250_1499;
		}
		if (this.numWins >= 1500 && this.numWins <= 1749) {
			return Constants.BADGE_1500_1749;
		}
		if (this.numWins >= 1750 && this.numWins <= 1999) {
			return Constants.BADGE_1750_1999;
		}
		if (this.numWins >= 2000 && this.numWins <= 2499) {
			return Constants.BADGE_2000_2499;
		}
		if (this.numWins >= 2500 && this.numWins <= 2999) {
			return Constants.BADGE_2500_2999;
		}
		if (this.numWins >= 3000 && this.numWins <= 3999) {
			return Constants.BADGE_3000_3999;
		}
		if (this.numWins >= 4000 && this.numWins <= 4999) {
			return Constants.BADGE_4000_4999;
		}
		if (this.numWins >= 5000) {
			return Constants.BADGE_5000;
		}
		//fallthrough
		return Constants.BADGE_0;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(this.id);
		out.writeString(this.nickname);
		out.writeString(this.firstName);
		out.writeString(this.lastname);
		out.writeString(this.email);
		out.writeString(this.password);
		out.writeString(this.fb);
		out.writeString(this.auth_token);
		out.writeInt(this.numWins);
		out.writeInt(this.numLosses);
		out.writeInt(this.numDraws);
		out.writeString(this.badge_drawable);
	}
	
	public static final Parcelable.Creator<Player> CREATOR
    			= new Parcelable.Creator<Player>() {
			public Player createFromParcel(Parcel in) {
				return new Player(in);
			}

			public Player[] newArray(int size) {
				return new Player[size];
			}
	};

	private Player(Parcel in) {
		// same order as writeToParcel
		this.id = in.readString();
		this.nickname = in.readString();
		this.firstName = in.readString();
		this.lastname = in.readString();
		this.email = in.readString();
		this.password = in.readString();
		this.fb = in.readString();
		this.auth_token = in.readString();
		this.numWins = in.readInt();
		this.numLosses = in.readInt();
		this.numDraws = in.readInt();
		this.badge_drawable = in.readString();
	}

}
