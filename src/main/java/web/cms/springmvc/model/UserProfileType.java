package web.cms.springmvc.model;

import java.io.Serializable;

public enum UserProfileType implements Serializable{
    GUEST("GUEST"),
    EDITOR("EDITOR"),
	AUTHOR("AUTHOR"),
	ADMIN("ADMIN");
	
	String userProfileType;
	
	private UserProfileType(String userProfileType){
		this.userProfileType = userProfileType;
	}
	
	public String getUserProfileType(){
		return userProfileType;
	}
	
}
