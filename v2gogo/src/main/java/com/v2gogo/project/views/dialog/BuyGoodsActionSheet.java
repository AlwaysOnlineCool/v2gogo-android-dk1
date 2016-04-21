package com.v2gogo.project.views.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.domain.shop.GoodsInfo;
import com.v2gogo.project.utils.common.ScreenUtil;
import com.v2gogo.project.views.logic.CartSteper;
import com.v2gogo.project.views.logic.CartSteper.Action;
import com.v2gogo.project.views.logic.CartSteper.IonChangedClickListener;

/**
 * 购买商品对话框
 * 
 * @author houjun
 */
public class BuyGoodsActionSheet extends Dialog implements IonChangedClickListener, android.view.View.OnClickListener
{
	private boolean mIsInitWidth = false;
	private View mContentView;
	private CartSteper mCartSteper;

	private TextView mPrice;
	private Button mBtnSure;

	private GoodsInfo mGoodsInfo;
	private IonSelectNumCallback mIonSelectNumCallback;

	public BuyGoodsActionSheet(Context context, boolean cancelable, OnCancelListener cancelListener)
	{
		super(context, cancelable, cancelListener);
		setDialogParams();
	}

	public BuyGoodsActionSheet(Context context, int theme)
	{
		super(context, theme);
		setDialogParams();
	}

	public BuyGoodsActionSheet(Context context)
	{
		super(context);
		setDialogParams();
	}

	private void setDialogParams()
	{
		Window dialogWindow = getWindow();
		dialogWindow.setWindowAnimations(R.style.style_dialog_aniamtion);
		dialogWindow.setGravity(Gravity.BOTTOM);
		this.setCanceledOnTouchOutside(true);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		if (null == mContentView)
		{
			mContentView = LayoutInflater.from(getContext()).inflate(R.layout.buy_goods_action_sheet_layout, null);
		}
		setContentView(mContentView);
		if (!mIsInitWidth)
		{
			Window dialogWindow = this.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			lp.width = ScreenUtil.getScreenWidth(getContext());
			dialogWindow.setAttributes(lp);
			mIsInitWidth = true;
		}
		mBtnSure = (Button) mContentView.findViewById(R.id.buy_goods_sheet_edit_dialog_sure);
		mPrice = (TextView) mContentView.findViewById(R.id.buy_goods_sheet_edit_dialog_price);
		mCartSteper = (CartSteper) mContentView.findViewById(R.id.buy_goods_sheet_edit_dialog_cartsteper);
		regiseterListener();
	}

	@Override
	public void dismiss()
	{
		try
		{
			super.dismiss();
		}
		catch (Exception exception)
		{
		}
	}

	@Override
	public void show()
	{
		try
		{
			super.show();
			if (mCartSteper != null)
			{
				mCartSteper.setStepperContent(1);
				setGoodsPrice();
			}
		}
		catch (Exception exception)
		{
		}
	}

	private void regiseterListener()
	{
		mCartSteper.setChangedClickListener(this);
		mBtnSure.setOnClickListener(this);
	}

	@Override
	public void onChangedAction(Action action, int value)
	{
		setGoodsPrice();
	}

	/**
	 * 设置商品信息
	 * 
	 * @param goodsInfo
	 */
	public void setGoodsData(GoodsInfo goodsInfo)
	{
		mGoodsInfo = goodsInfo;
		setGoodsPrice();
	}

	public void setOnSelectNumCallback(IonSelectNumCallback mIonSelectNumCallback)
	{
		this.mIonSelectNumCallback = mIonSelectNumCallback;
	}

	public interface IonSelectNumCallback
	{
		public void onYetSelectNum(int goodsNum, GoodsInfo goodsInfo);
	}

	@Override
	public void onClick(View arg0)
	{
		if (mIonSelectNumCallback != null)
		{
			BuyGoodsActionSheet.this.dismiss();
			int num = mCartSteper.getStepperContent();
			mIonSelectNumCallback.onYetSelectNum(num, mGoodsInfo);
		}
	}

	/**
	 * 设置商品价格
	 */
	private void setGoodsPrice()
	{
		if (mGoodsInfo != null)
		{
			int num = mCartSteper.getStepperContent();
			mPrice.setText(String.format(getContext().getString(R.string.goods_details_goods_price_tip), num * mGoodsInfo.getV2gogoPrice()));
		}
	}
}
