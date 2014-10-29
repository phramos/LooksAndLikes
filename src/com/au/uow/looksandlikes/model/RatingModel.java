package com.au.uow.looksandlikes.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Rating")
public class RatingModel extends ParseObject {
	public RatingModel() {
		// A default constructor is required.
	}
	
	public RatingModel(ParseUser voter, ParseObject look, Integer rating) {
		setVoter(voter);
		setLook(look);
		setRating(rating);
	}
	
	public ParseUser getVoter(){
		return getParseUser("voter");
	}
	
	public void setVoter(ParseUser voter) {
		put("voter", voter);
	}
	
	public ParseObject getLook(){
		return getParseObject("look");
	}
	
	public void setLook(ParseObject look) {
		put("look", look);
	}
	
	public Integer getRating(){
		return getInt("look");
	}
	
	public void setRating(Integer rating){
		put("rating", rating);
	}
	
}
