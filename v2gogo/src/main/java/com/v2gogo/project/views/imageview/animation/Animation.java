package com.v2gogo.project.views.imageview.animation;

public interface Animation
{

	/**
	 * Transforms the view.
	 * 
	 * @param view
	 * @param diffTime
	 * @return true if this animation should remain active. False otherwise.
	 */
	public boolean update(com.v2gogo.project.views.imageview.GestureImageView view, long time);

}
