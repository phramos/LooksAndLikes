package com.au.uow.looksandlikes;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;


@ParseClassName("UserProfile")
public class UserProfile extends ParseObject {

	public UserProfile() {
		// A default constructor is required.
	}

	public ParseUser getUser() {
		return getParseUser("user");
	}

	public void setUser(ParseUser user) {
		put("user", user);
	}

    public String getFacebookId() {
        return getString("facebookId");
    }

    public void setFacebookId(String facebookId) {
        put("facebookId", facebookId);
    }

	public ParseFile getProfileImage() {
		return getParseFile("profileImage");
	}

	public void setProfileImage(ParseFile file) {
		put("profileImage", file);
	}

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getGender() {
        return getString("gender");
    }

    public void setGender(String gender) {
        put("gender", gender);
    }

    public String getLocation() {
        return getString("location");
    }

    public void setLocation(String location) {
        put("location", location);
    }

    public String getEmail() {
        return getString("email");
    }

    public void setEmail(String email) {
        put("email", email);
    }

}
