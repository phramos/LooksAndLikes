package com.au.uow.looksandlikes;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Look")
public class Look extends ParseObject {

	public Look() {
		// A default constructor is required.
	}

	public ParseUser getAuthor() {
		return getParseUser("author");
	}

	public void setAuthor(ParseUser user) {
		put("author", user);
	}

	public Double getRating() {
		return getDouble("rating");
	}

	public void setRating(Double rating) {
		put("rating", rating);
	}
	
	public Integer getTotalVotes() {
		return getInt("totalVotes");
	}
	
	public void setTotalVotes(Integer votes) {
		put("totalVotes", votes);
	}

	public ParseFile getPhotoFile() {
		return getParseFile("photo");
	}

	public void setPhotoFile(ParseFile file) {
		put("photo", file);
	}
	
	//Calculate and set the new rating
	public void updateRatingAverage(Double rating){
		Integer curretTotalVotes = getTotalVotes();
		Integer newTotalVotes = curretTotalVotes+1;
		Double currentRating = getRating();
		Double newRating = (curretTotalVotes * currentRating + rating)/newTotalVotes;  
		//save the new values on the cloud
		setRating(newRating);
		setTotalVotes(newTotalVotes);
	}

}
