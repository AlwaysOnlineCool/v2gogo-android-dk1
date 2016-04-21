package com.v2gogo.project.activity.home;

import com.v2gogo.project.manager.home.CommentManager;

public class HotCommentListActivity extends BaseCommentListActivity
{

	@Override
	public String getCommentListType()
	{
		return CommentManager.HOT_COMMENT_LIST_KEYWORD;
	}
}
