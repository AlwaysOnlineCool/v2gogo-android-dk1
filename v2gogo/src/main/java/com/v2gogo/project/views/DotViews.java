package com.v2gogo.project.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.v2gogo.project.R;

/**
 * 轮播指示器
 * 
 * @author houjun
 */
public class DotViews extends LinearLayout
{

	private Context context;
	private int normalBG;
	private int selectBG;
	private int totalSize;

	public DotViews(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.context = context;
		normalBG = R.drawable.icons_circle_default;
		selectBG = R.drawable.icons_circle;
	}

	/**
	 * 设置小圆点的背景, 调用应该在setSize() 之前
	 * 
	 * @param normalBG
	 *            正常的背景 注意:参数为drawable资源ID
	 * @param selectBG
	 *            选中时的背景 注意:参数为drawable资源ID
	 */
	public void setViewerBG(int normalBG, int selectBG)
	{
		this.normalBG = normalBG;
		this.selectBG = selectBG;
	}

	/**
	 * 设置指示器数量
	 * 
	 * @param size
	 */
	public void setSize(int size)
	{
		removeAllViews();
		totalSize = size;
		for (int i = 0; i < size; i++)
		{
			ImageView imageView = new ImageView(context);
			LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			imageView.setScaleType(ScaleType.CENTER);
			if (i == 0)
			{
				imageView.setImageResource(selectBG);
			}
			else
			{
				params.leftMargin = 15;
				imageView.setImageResource(normalBG);
			}
			addView(imageView, params);
		}
	}

	public void select(int select)
	{
		if (select < getChildCount())
		{
			for (int i = 0; i < totalSize; i++)
			{
				if (i == select)
				{
					ImageView current = ((ImageView) (getChildAt(select)));
					if (current != null)
					{
						current.setImageResource(selectBG);
					}
				}
				else
				{
					ImageView pre = ((ImageView) (getChildAt(i)));
					if (pre != null)
					{
						pre.setImageResource(normalBG);
					}
				}
			}
		}
	}
}
