package com.v2gogo.project.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.v2gogo.project.manager.config.ServerUrlConfig;
import com.v2gogo.project.utils.common.LogUtil;
import com.ypy.eventbus.EventBus;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler
{

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		api = WXAPIFactory.createWXAPI(this, ServerUrlConfig.PAY_WEIXIN_APPID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req)
	{
	}

	@Override
	public void onResp(BaseResp resp)
	{
		LogUtil.e("WXPayEntryActivity", "onPayFinish, errCode = " + resp.errCode);
		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX)
		{
			EventBus.getDefault().post(resp);
			finish();
		}
	}
}
