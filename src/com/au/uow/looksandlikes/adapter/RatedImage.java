package com.au.uow.looksandlikes.adapter;

import android.graphics.Bitmap;

public class RatedImage {
	private Bitmap image;
	private String title;
	private Long rating;

	public RatedImage(Bitmap image, String title, Long rating) {
		super();
		this.image = image;
		this.title = title;
		this.rating = rating;
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

}
