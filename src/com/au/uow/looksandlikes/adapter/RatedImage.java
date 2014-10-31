package com.au.uow.looksandlikes.adapter;

import java.io.Serializable;

import com.parse.ParseObject;

import android.graphics.Bitmap;

public class RatedImage implements Serializable {
	private Bitmap image;
	private String title;
	private Long rating;
	private Integer totalVotes;
	private String url;

	public RatedImage(Bitmap image, String title, Long rating,
			Integer totalVotes, String url) {
		super();
		this.image = image;
		this.title = title;
		this.rating = rating;
		this.totalVotes = totalVotes;
		this.url = url;
	}

	public Bitmap getImage() {
		return image;
	}

	public void setImage(Bitmap image) {
		this.image = image;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getRating() {
		return rating;
	}

	public void setRating(Long rating) {
		this.rating = rating;
	}

	public Integer getTotalVotes() {
		return totalVotes;
	}

	public void setTotalVotes(Integer totalVotes) {
		this.totalVotes = totalVotes;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
