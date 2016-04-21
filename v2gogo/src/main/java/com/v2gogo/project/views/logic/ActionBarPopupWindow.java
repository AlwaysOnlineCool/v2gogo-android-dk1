package com.v2gogo.project.views.logic;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;

import com.v2gogo.project.R;
import com.v2gogo.project.activity.MainTabActivity;
import com.v2gogo.project.activity.shop.GroupProductTypeWebViewActivity;
import com.v2gogo.project.utils.common.DensityUtil;
import com.ypy.eventbus.EventBus;

/**
 * 操作的popupwindow
 * 
 * @author houjun
 */
public class ActionBarPopupWindow extends PopupWindow implements OnClickListener
{

	private Button mBtnHome;
	private Button mBtnCollection;
	private Context mContext;
	private IonClickItemListener mIonClickItemListener;

	public ActionBarPopupWindow(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public ActionBarPopupWindow(Context context)
	{
		super(context);
		init(context);
	}

	/**
	 * 设置监听
	 * 
	 * @param clickItemListener
	 */
	public void setClickItemListener(IonClickItemListener clickItemListener)
	{
		mIonClickItemListener = clickItemListener;
	}

	@SuppressWarnings("deprecation")
	private void init(Context context)
	{
		mContext = context;
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.action_bar_popup_window_layout, null);
		mBtnHome = (Button) view.findViewById(R.id.action_bar_popup_window_home_btn);
		mBtnCollection = (Button) view.findViewById(R.id.action_bar_popup_window_collection_btn);
		Button category = (Button) view.findViewById(R.id.action_bar_popup_window_categary_btn);
		category.setOnClickListener(this);
		this.setWidth(DensityUtil.dp2px(context, 140f));
		this.setHeight(DensityUtil.dp2px(context, 135f));
		this.setContentView(view);
		this.setAnimationStyle(R.anim.anim_from_top_in);
		this.setFocusable(true);
		this.setOutsideTouchable(true);
		this.setBackgroundDrawable(new BitmapDrawable());
		regierListener();
	}

	private void regierListener()
	{
		mBtnHome.setOnClickListener(this);
		mBtnCollection.setOnClickListener(this);
	}

	/**
	 * 显示
	 * 
	 * @param view
	 */
	public void show(View view, int xoff, int yoff)
	{
		this.showAsDropDown(view, xoff, yoff);
	}

	@Override
	public void onClick(View view)
	{
		int index = 0;
		switch (view.getId())
		{
			case R.id.action_bar_popup_window_home_btn:
				Intent intent = new Intent(mContext, MainTabActivity.class);
				intent.putExtra(MainTabActivity.BACK, MainTabActivity.BACK_HOME);
				mContext.startActivity(intent);
				index = 0;
				break;

			case R.id.action_bar_popup_window_categary_btn:
				index = 1;
				break;
			case R.id.action_bar_popup_window_collection_btn:
				index = 2;
				break;

			default:
				break;
		}
		if (mIonClickItemListener != null)
		{
			this.dismiss();
			mIonClickItemListener.onPopupWindowClick(index);
		}
	}

	public interface IonClickItemListener
	{
		public void onPopupWindowClick(int position);
	}

}
