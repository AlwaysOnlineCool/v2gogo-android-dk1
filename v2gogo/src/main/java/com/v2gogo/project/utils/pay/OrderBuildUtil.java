package com.v2gogo.project.utils.pay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.v2gogo.project.manager.config.ServerUrlConfig;

/**
 * 订单生成工具
 * 
 * @author houjun
 */
public class OrderBuildUtil
{

	// 商户PID
	public static final String PARTNER = "2088811911002753";
	// 商户收款账号
	public static final String SELLER = "wjzp_gz@163.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANzZrUgEgORmK3cTv8pFFjoLP+GNrwWauhzobijKT1MBgjYnld2QDCcVil6KY5rragT90eAfW5c+3wuu79DfihRNe/e4NIQCRH0o+RZlCHkjY7BWqfPBPq2qAWalcsIchX5NgOcnLWuVcCpFOPw6IXhJsiaeW/xY5d7YwSYJFzEjAgMBAAECgYEAx5i7sODRFJr6IW5p4Ya65c6Q92qziZ8jIUk3dgoR0vUEF6mDGnE7an9fRL5R8wtcEBLNgX8VqgWaU9bQyrec87pEelG9asFBo0n4o+qxe2DKrffhEjICZkk59z7dq6IxBFmtA2mNCAX/kvJ/LGlK0Jqro4aFfGlk14HyJY8uXcECQQD3r7XUG4qJtiUbj+0ca5jAPqfUOqYKD20GFN9yPHmWP+dMcqi3kJWfJFQ7B32oJvFhngnZpDoMPmcIpCHeFt3zAkEA5ENfdBYVzc3Z3XPjqC2DGN9zxhj9gC/qtZMO0zRwngGVQ31V15wjL39ButRtIfpqQMa+zfZ6TmCq5axz6By8EQJAFPudu8pZgjEcE6mtvKqg1ih4r1IInvGPmuRJybO12TSws5lDEcn94u2A9T89NfGWoV5yFy4CfOcMc370H4xwYQJACG6otwwd0BV7p9+hGFReFGwkxbukmbirbfu5v3mfc2bRkcWuhyGAdz+3OOSiB3BNvojZAZjKWdzeU0+0lG2DUQJAft595bPa2uBEn7T9LKZjjgAGHpcZaS95VB/rdcVLRZhK+VxWQRqmXFP8FiHfa7syeYWlYYb9eQzwq7Ckgm50jQ==";

	/**
	 * 得到签名的订单信息
	 * 
	 * @param subject
	 *            商品名称
	 * @param body
	 *            商品详情
	 * @param price
	 *            商品价钱
	 * @param orderno
	 *            对应的订单号
	 * @return
	 */
	public static String getSignOrderInfo(String subject, String body, String price, String orderno)
	{
		String orderInfo = createOrderInfo(subject, body, price, orderno);
		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try
		{
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		// 完整的符合支付宝参数规范的订单信息
		return orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
	}

	/**
	 * 生成订单信息
	 * 
	 * @param subject
	 *            商品名称
	 * @param body
	 *            商品详情
	 * @param price
	 *            商品价钱
	 * @param orderno
	 *            对应的订单号
	 * @return
	 */
	private static String createOrderInfo(String subject, String body, String price, String orderno)
	{
		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + orderno + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + ServerUrlConfig.SERVER_URL+"/pay/notifyUrl" + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 待签名订单信息
	 */
	private static String sign(String content)
	{
		return SignUtil.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 */
	private static String getSignType()
	{
		return "sign_type=\"RSA\"";
	}
}
