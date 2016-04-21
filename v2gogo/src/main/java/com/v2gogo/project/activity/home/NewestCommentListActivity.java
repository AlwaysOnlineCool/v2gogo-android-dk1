package com.v2gogo.project.activity.home;

import com.v2gogo.project.manager.home.CommentManager;

public class NewestCommentListActivity extends BaseCommentListActivity
{

	@Override
	public String getCommentListType()
	{
		return CommentManager.NEWEST_COMMENT_LIST_KEYWORD;
	}
}
