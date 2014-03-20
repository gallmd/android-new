package com.gall.remote.adapters;
/**
 * Contains image and text to display in a row element in a list
 * @author Matt Gall
 *
 */
public class RowItem {
	
	private int imageID;
	private String title;
	
	public RowItem(int imageID, String title){
		this.imageID = imageID;
		this.title = title;
	}
	
	//getters
	public int getImageID(){
		return imageID;
	}
	
	public String getTitle(){
		return title;
	}
	

	
	//setters
	public void setImageID(int i){
		this.imageID = i;
	}
	
	public void setTitle(String t){
		this.title = t;	
	}
	

	
	//toString
	
	public String toString(){
		return (title);
	}

}
