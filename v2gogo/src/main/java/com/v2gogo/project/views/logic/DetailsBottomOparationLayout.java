package com.v2gogo.project.views.logic;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.v2gogo.project.R;

/**
 * 详情界面的底部操作栏（包含分享，点赞，评论）
 * 
 * @author
 */
public class DetailsBottomOparationLayout extends LinearLayout implements OnClickListener
{

	private TextView mCommentNum;
	private ImageButton mBtnShare;
	private ImageButton mBtnComment;
	private ImageButton mBtnCollection;
	private Button mBtnWriteComment;

	private IonClickOperationListener mClickOperationListener;

	public static enum ACTION
	{
		SHARE, COMMENT, WRITE_COMMENT, COLLECTIONS
	}

	public DetailsBottomOparationLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public DetailsBottomOparationLayout(Context context)
	{
		super(context);
		init(context);
	}

	public void setOnClickOperationListener(IonClickOperationListener mClickOperationListener)
	{
		this.mClickOperationListener = mClickOperationListener;
	}

	/**
	 * @param context
	 */
	private void init(Context context)
	{
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.details_bottom_oparation_layout, null);
		initViews(view);
		LinearLayout.LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		view.setLayoutParams(layoutParams);
		this.addView(view);
	}

	private void initViews(View view)
	{
		mBtnShare = (ImageButton) view.findViewById(R.id.details_bottom_operation_layout_share);
		mBtnWriteComment = (Button) view.findViewById(R.id.details_bottom_operation_layout_write);
		mBtnComment = (ImageButton) view.findViewById(R.id.details_bottom_operation_layout_comment);
		mCommentNum = (TextView) view.findViewById(R.id.details_bottom_operation_layout_comment_num);
		mBtnCollection = (ImageButton) view.findViewById(R.id.details_bottom_operation_layout_collections);
		mBtnShare.setOnClickListener(this);
		mBtnComment.setOnClickListener(this);
		mBtnCollection.setOnClickListener(this);
		mBtnWriteComment.setOnClickListener(this);
		mCommentNum.setTextSize(9f);
	}

	/**
	 * 设置已经收藏
	 */
	public void setYetCollections()
	{
		if (null != mBtnCollection)
		{
			mBtnCollection.setImageResource(R.drawable.ic_action_favor_on_normal);
		}
	}

	/**
	 * 操作栏点击事件回调
	 * 
	 * @author houjun
	 */
	public interface IonClickOperationListener
	{
		public void onClickOperation(ACTION name, View view);
	}

	/**
	 * method desc：设置评论是否可见
	 * 
	 * @param isVisibility
	 */
	public void setCommentUnClickable(int isVisibility)
	{
		mCommentNum.setVisibility(isVisibility);
		mBtnComment.setImageResource(R.drawable.ic_action_comment_gray);
		Drawable drawable = getResources().getDrawable(R.drawable.ic_action_write_gray);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		mBtnWriteComment.setCompoundDrawables(drawable, null, null, null);
		mBtnWriteComment.setTextColor(0xffcbcbcb);
		mBtnComment.setClickable(false);
		mBtnWriteComment.setClickable(false);

	}
	/**
	 * method desc：设置评论是否可见
	 * 
	 * @param isVisibility
	 */
	public void setCommentClickable(int isVisibility)
	{
		mCommentNum.setVisibility(isVisibility);
		mBtnComment.setImageResource(R.drawable.ic_action_comment_normal);
		Drawable drawable = getResources().getDrawable(R.drawable.ic_action_write_normal);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		mBtnWriteComment.setCompoundDrawables(drawable, null, null, null);
		mBtnWriteComment.setTextColor(0xff696969);
		mBtnComment.setClickable(true);
		mBtnWriteComment.setClickable(true);
		
	}

	/**
	 * 设置评论数
	 * 
	 * @param number
	 */
	public void setCommentNum(int number)
	{
		if (number == 0)
		{
			mCommentNum.setVisibility(View.GONE);
		}
		else
		{
			mCommentNum.setVisibility(View.VISIBLE);
			mCommentNum.setText(number + "");
		}
	}

	@Override
	public void onClick(View view)
	{
		ACTION action = null;
		switch (view.getId())
		{
			case R.id.details_bottom_operation_layout_share:
				action = ACTION.SHARE;
				break;

			case R.id.details_bottom_operation_layout_comment:
				action = ACTION.COMMENT;
				break;

			case R.id.details_bottom_operation_layout_collections:
				action = ACTION.COLLECTIONS;
				break;

			case R.id.details_bottom_operation_layout_write:
				action = ACTION.WRITE_COMMENT;
				break;

			default:
				break;
		}
		if (null != mClickOperationListener)
		{
			mClickOperationListener.onClickOperation(action, this);
		}
	}
}
