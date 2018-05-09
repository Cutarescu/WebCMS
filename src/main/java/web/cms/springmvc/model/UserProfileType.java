package web.cms.springmvc.model;

import java.io.Serializable;

public enum UserProfileType implements Serializable{
    GUEST("GUEST", "ROLE_GUEST"),
    EDITOR("EDITOR", "ROLE_EDITOR"),
	AUTHOR("AUTHOR", "ROLE_AUTHOR"),
	ADMIN("ADMIN", "ROLE_ADMIN");
	
	String userProfileType;
	String role;
	
	private UserProfileType(String userProfileType, String role){
		this.userProfileType = userProfileType;
		this.role = role;
	}
	
	public String getUserProfileType(){
		return userProfileType;
	}
	
	public String getRole(){
        return role;
    }
}
