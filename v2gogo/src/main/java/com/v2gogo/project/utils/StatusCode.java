package com.v2gogo.project.utils;

/**
 * 状态码定义
 * 
 * @author houjun
 */
public class StatusCode
{

	/* ++++++++++++++++++++++++++++++++++++++++一般错误码code+++++++++++++++++++++++++++++++++++++++++ */
	/**
	 * 成功
	 */
	public static final int SUCCESS = 0;
	/**
	 * 失败
	 */
	public static final int FAIL = -1;

	/* ++++++++++++++++++++++++++++++++++++++++ 验证验证码code+++++++++++++++++++++++++++++++++++++++++ */

	/**
	 * 验证验证码失败
	 */
	public static final int CHECK_CODE_FAIL = 102;

	/**
	 * 输入验证码为空
	 */
	public static final int CHECK_CODE_NONE = 103;
	/**
	 * 获取系统保存验证码为空
	 */
	public static final int GET_CODE_NONE = 104;

	/* ++++++++++++++++++++++++++++++++++++++++ 用户code+++++++++++++++++++++++++++++++++++++++++ */

	/**
	 * 用户已经存在
	 */
	public static final int USER_REGISTED = 201;

	/**
	 * 用户不存在
	 */
	public static final int USER_NONE = 202;

	/* ++++++++++++++++++++++++++++++++++++++++ 用户签到+++++++++++++++++++++++++++++++++++++++++ */

	/**
	 * 用户已签到
	 */
	public static final int SIGNED = 203;

	/**
	 * 用户未签到
	 */
	public static final int UNSIGN = 204;

	/**
	 * 用户密码错误
	 */
	public static final int USER_PASS_ERROR = 205;
	/**
	 * 用户名为空
	 */
	public static final int USER_NAME_NONE = 206;

	/**
	 * 用户密码为空
	 */
	public static final int USER_PASS_NONE = 207;
	/**
	 * 设备唯一标识符为空
	 */
	public static final int DEVICETOKEN_NONE = 208;

	/**
	 * 用户已被冻结
	 */
	public static final int USER_LOCK = 209;
	/**
	 * 用户已被删除
	 */
	public static final int USER_DEL = 210;
	/**
	 * 用户呢称已存在
	 */
	public static final int USER_FULLNAME_USE = 211;
	/**
	 * 邀请用户不存在
	 */
	public static final int INV_USER_NONE = 212;
	/**
	 * 用户原密码为空（修改密码时用）
	 */
	public static final int USER_OLD_PASS_NONE = 213;
	/**
	 * 传入用户ID为空
	 */
	public static final int USER_ID_NONE = 214;
	/**
	 * 用户devicetoken为空
	 */
	public static final int USER_DEVICETOKEN_NONE = 215;

	/* ++++++++++++++++++++++++++++++++++++++++ 奖品+++++++++++++++++++++++++++++++++++++++++ */
	/**
	 * 没有找到奖品信息
	 */
	public static final int USER_PRIZE_NONE = 216;
	/**
	 * 奖品已经被领取
	 */
	public static final int USER_PRIZE_RECEIVIED = 217;
	/**
	 * 奖品不是自己的
	 */
	public static final int USER_PRIZE_NOT_SELF = 218;

	/**
	 * 用户原密码错误()
	 */
	public static final int USER_OLD_PASS_ERROR = 219;
	/**
	 * 用户被踢下线
	 */
	public static final int USER_OFFLINE = 220;
	/* +++++++++++++++++++++++++++++++ 文章+++++++++++++++++++++++++++++++++++++++++++++++++++ */

	/**
	 * 没有找到文章信息
	 */
	public static final int INFO_NONE = 301;

	/* +++++++++++++++++++++++++++++++++++ 投票+++++++++++++++++++++++++++++++++++++++++++++++++ */
	/**
	 * 没有找到投票信息
	 */
	public static final int INFO_OPTIONS_NONE = 401;

	/**
	 * 可用金币不足以完成此次投票
	 */
	public static final int COIN_NOT_SUPPOTR = 402;

	/**
	 * 已对当前活动投票，不能重复投票
	 */
	public static final int INFO_IS_PRAISED = 403;

	/**
	 * 投票活动已结束
	 */
	public static final int VOTE_YET_STOP = 404;

	/* +++++++++++++++++++++++++++++++ 评论++++++++++++++++++++++++++++++++++++++++++++++++++ */
	/**
	 * 评论内容为空
	 */
	public static final int COMMENT_NONE = 501;
	/**
	 * 评论源ID为空
	 */
	public static final int COMMENT_SRCID_NONE = 502;
	/**
	 * 评论文章ID为空
	 */
	public static final int COMMENT_INFOID_NONE = 503;
	/**
	 * 获取评论图片失败
	 */
	public static final int COMMENT_FILE_NONE = 504;
	/**
	 * 点赞评论ID为空
	 */
	public static final int COMMENT_ID_NONE = 505;
	/**
	 * 已经对评论点过赞
	 */
	public static final int COMMENT_PRAISIED = 506;

	/**
	 * 评论被限制
	 */
	public static final int COMMENT_YET_LIMIT = 507;

	/* +++++++++++++++++++++++++++ 收藏+++++++++++++++++++++++++++++++++++++++++++++++++ */
	/**
	 * 不能重复收藏
	 */
	public static final int COLLECT_EXIST = 601;
	/**
	 * 没有找到要收藏的文章信息
	 */
	public static final int COLLECT_INFO_EXIST = 602;
	/**
	 * 没有找到要收藏的奖品信息
	 */
	public static final int COLLECT_PRIZE_NOT_EXIST = 603;
	/**
	 * 没有找到要收藏的商品信息
	 */
	public static final int COLLECT_PRODUCT_NOT_EXIST = 604;

	/* +++++++++++++++++++++++++++ 兑换奖品++++++++++++++++++++++++++++++++++++++++++++ */
	/**
	 * 兑换奖品ID为空
	 */
	public static final int CONVER_PID_NONE = 701;
	/**
	 * 找到不兑换奖品信息
	 */
	public static final int CONVER_NONE = 702;
	/**
	 * 奖品数量不足
	 */
	public static final int CONVER_SUPPLY_NONE = 703;
	/**
	 * 用户金币不足
	 */
	public static final int CONVER_USER_COIN_NOT_ENOUGH = 704;
	/**
	 * 奖品不是可兑换奖品
	 */
	public static final int CONVER_NOT_PUB = 705;
	/**
	 * 奖品未开始兑换
	 */
	public static final int CONVER_NOT_START = 706;

	/**
	 * 超过最大兑换数
	 */
	public static final int YET_MAX_EXCHANGE_NUM = 707;

	// 电商

	// 商品
	/**
	 * 找不到商品信息
	 */
	public static final int PRODUCT_NONE = 801;

	/**
	 * 商品数量不足
	 */
	public static final int PRODUCT_SUPPLY_NO_ENOUGH = 802;

	/**
	 * 商品已下架
	 */
	public static final int GOODS_YET_DOWN = 803;

	// 订单
	/**
	 * 没有找到订单
	 */
	public static final int ORDER_NONE = 901;

	/**
	 * 订单不是当前用户的
	 */
	public static final int ORDER_IS_NOT_USERS = 902;

	/**
	 * 订单状态已更改
	 */
	public static final int ORDER_CHANGE = 903;
	/**
	 * 收货人为空
	 */
	public static final int ORDER_CONSIGNEE_NONE = 904;
	/**
	 * 收货电话为空
	 */
	public static final int ORDER_PHONE_NONT = 905;
	/**
	 * 收货地址为空
	 */
	public static final int ORDER_ADDRESS_NONE = 906;
	/**
	 * 订单商品信息为空
	 */
	public static final int ORDER_PRODUCT_INFO_NONE = 907;
	/**
	 * 订单金额有误
	 */
	public static final int ORDER_AMOUNT_ERROR = 908;
	/**
	 * 订单数量为0
	 */
	public static final int ORDER_SUPPLY_NO = 909;
	/**
	 * 订单状态错误
	 */
	public static final int ORDER_STATUS_ERROR = 910;
	/**
	 * 订单用户错误
	 */
	public static final int ORDER_USER_ERROR = 911;
	
	
	/**
	 * 验证用户失败
	 */
	public static final int CHECK_USER_ERROR = 2001;

}
