package com.chen.parsexml;

import android.R.string;

public class News {
	private  String title;
	private  int commentCount;
	private  String author;
	private  String pubDate;
	
//	 News(String title,int commentCount,String author,String pubDate) {
//         this.title = title;
//         this.commentCount = commentCount;
//         this.author = author;
//         this.pubDate=pubDate;
//     }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	 
	 
}
