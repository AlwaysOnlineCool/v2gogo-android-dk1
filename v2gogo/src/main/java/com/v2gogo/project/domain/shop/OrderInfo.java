package com.v2gogo.project.domain.shop;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;

import com.google.gson.annotations.SerializedName;
import com.v2gogo.project.R;
import com.v2gogo.project.utils.common.DateUtil;

/**
 * 订单实体类
 * 
 * @author houjun
 */
public class OrderInfo implements Serializable
{

	private static final long serialVersionUID = 1627029217054542910L;

	public static int ORDER_STATUS_NO_SEND = 1;// 待发货
	public static int ORDER_STATUS_NO_PAY = 0;// 待支付
	public static int ORDER_STATUS_YET_COMPLETED = 2;// 已完成
	public static int ORDER_STATUS_YET_CANCEL = -9;// 已取消

	// 评价
	public static int ORDER_STATUS_IS_NO_COMMENT = 0;// 未评价
	public static int ORDER_STATUS_IS_YES_COMMENT = 1;// 已评价

	private long createTime;// 订单生成时间
	private long predictTime;// 预计发货时间

	private String phone;// 收货人电话号码

	private float orderTotal;// 订单总金额
	private float poundate;// 手续费
	private float endTime;// 终止时间
	private float postage;// 邮费

	private String id;// 订单id
	private String expressCompary;// 快递公司
	private String orderNo;// 订单编号
	private String address;// 收货人地址
	private String userId;// 收货人id
	private String expressNo;// 快递单号
	private String consignee;
	private String mCreateTimeString;

	private int orderStatus;// 订单状态
	private int isComment;// 是否显示评价 0：未评价 1：已评价
	private int payType;// 在线支付类型
	private int payMethod;// 支付类型

	@SerializedName("orderProducts")
	private List<OrderGoodsInfo> mOrderGoodsInfos;

	public long getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(long createTime)
	{
		this.createTime = createTime;
	}

	public long getPredictTime()
	{
		return predictTime;
	}

	public void setPredictTime(long predictTime)
	{
		this.predictTime = predictTime;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public float getOrderTotal()
	{
		return orderTotal;
	}

	public void setOrderTotal(float orderTotal)
	{
		this.orderTotal = orderTotal;
	}

	public float getPoundate()
	{
		return poundate;
	}

	public void setPoundate(float poundate)
	{
		this.poundate = poundate;
	}

	public float getEndTime()
	{
		return endTime;
	}

	public void setEndTime(float endTime)
	{
		this.endTime = endTime;
	}

	public float getPostage()
	{
		return postage;
	}

	public void setPostage(float postage)
	{
		this.postage = postage;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getExpressCompary()
	{
		return expressCompary;
	}

	public void setExpressCompary(String expressCompary)
	{
		this.expressCompary = expressCompary;
	}

	public String getOrderNo()
	{
		return orderNo;
	}

	public void setOrderNo(String orderNo)
	{
		this.orderNo = orderNo;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getExpressNo()
	{
		return expressNo;
	}

	public void setExpressNo(String expressNo)
	{
		this.expressNo = expressNo;
	}

	public int getOrderStatus()
	{
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus)
	{
		this.orderStatus = orderStatus;
	}

	public int getPayType()
	{
		return payType;
	}

	public void setPayType(int payType)
	{
		this.payType = payType;
	}

	public int getPayMethod()
	{
		return payMethod;
	}

	public void setPayMethod(int payMethod)
	{
		this.payMethod = payMethod;
	}

	public List<OrderGoodsInfo> getOrderGoodsInfos()
	{
		return mOrderGoodsInfos;
	}

	public void setOrderGoodsInfos(List<OrderGoodsInfo> mOrderGoodsInfos)
	{
		this.mOrderGoodsInfos = mOrderGoodsInfos;
	}

	public String getConsignee()
	{
		return consignee;
	}

	public void setConsignee(String consignee)
	{
		this.consignee = consignee;
	}

	public String getCreateTimeString()
	{
		if (null == mCreateTimeString)
		{
			mCreateTimeString = DateUtil.convertStringWithTimeStamp(createTime);
		}
		return mCreateTimeString;
	}

	public int getIsComment()
	{
		return isComment;
	}

	public void setIsComment(int isComment)
	{
		this.isComment = isComment;
	}

	/**
	 * 返回订单状态提示文字
	 * 
	 * @return
	 */
	public String getOrderStatusString(Context context)
	{
		Resources resources = context.getResources();
		if (orderStatus == ORDER_STATUS_NO_PAY)
		{
			return resources.getString(R.string.order_state_no_pay_tip);
		}
		else if (orderStatus == ORDER_STATUS_NO_SEND)
		{
			return resources.getString(R.string.order_state_no_send_tip);
		}
		else if (orderStatus == ORDER_STATUS_YET_CANCEL)
		{
			return resources.getString(R.string.order_state_yet_cancal_tip);
		}
		else if (orderStatus == ORDER_STATUS_YET_COMPLETED)
		{
			return resources.getString(R.string.order_state_yet_complete_tip);
		}
		return null;
	}

	@Override
	public String toString()
	{
		return "OrderInfo [createTime=" + createTime + ", predictTime=" + predictTime + ", phone=" + phone + ", orderTotal=" + orderTotal + ", poundate="
				+ poundate + ", endTime=" + endTime + ", postage=" + postage + ", id=" + id + ", expressCompary=" + expressCompary + ", orderNo=" + orderNo
				+ ", address=" + address + ", userId=" + userId + ", expressNo=" + expressNo + ", consignee=" + consignee + ", orderStatus=" + orderStatus
				+ ", payType=" + payType + ", payMethod=" + payMethod + ", mOrderGoodsInfos=" + mOrderGoodsInfos + "]";
	}
}
