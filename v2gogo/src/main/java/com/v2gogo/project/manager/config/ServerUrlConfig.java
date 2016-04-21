package com.v2gogo.project.manager.config;

/**
 * 访问服务器url配置
 * 
 * @author houjun
 */
public class ServerUrlConfig
{
	// APP_ID 替换为你的应用从官方网站申请到的合法appId
	public static final String APP_ID = "wxd930ea5d5a258f4f";

	// public static final String SERVER_URL = "http://app.v2gogo.com";
	// public static final String SERVER_URL = "http://api.v2gogo.com";
	public static final String SERVER_URL = "http://test-api.v2gogo.com";

	// public static final String SERVER_URL = "http://192.168.1.117:9999";
	// public static final String SERVER_URL = "http://192.168.1.8:9999";// 耀东
	// public static final String SERVER_URL = "http://192.168.1.2:9999";
	// public static final String SERVER_URL = "http://192.168.1.32:9999";// 孟广宇

	/**
	 * 支付通知url
	 */
	// public static final String PAY_NOTIFY_SERVER_URL = "http://api.v2gogo.com";
	// public static final String PAY_NOTIFY_SERVER_URL = "http://app.v2gogo.com";

	public static final String PAY_NOTIFY_SERVER_URL = "http://test-api.v2gogo.com";

	// 微信支付
	// public static final String PAY_WEIXIN_APPID = "wxc2f5f910fa9d0189";// 应用ID
	public static final String PAY_WEIXIN_APPID = "wx3ce6531c8637641f";// 应用ID（公益支付)
	public static final String PAY_WEIXIN_PARTERID = "1299244501";// 商户ID
	public static final String PAY_WEIXIN_MIYAO = "GZwJzpXxjsYxG20151224WJZPGZBXGZL";// 密钥
}
