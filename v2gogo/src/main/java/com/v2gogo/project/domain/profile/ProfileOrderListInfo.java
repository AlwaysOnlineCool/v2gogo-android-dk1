package com.v2gogo.project.domain.profile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.v2gogo.project.domain.shop.OrderInfo;

/**
 * 个人订单列表实体类
 * 
 * @author houjun
 */
public class ProfileOrderListInfo implements Serializable
{

	private static final long serialVersionUID = -835245437224647784L;

	private int count;

	@SerializedName("pageNo")
	private int page;

	@SerializedName("list")
	private List<OrderInfo> orderInfos;

	public int getCount()
	{
		return count;
	}

	public void setCount(int count)
	{
		this.count = count;
	}

	public int getPage()
	{
		return page;
	}

	public void setPage(int page)
	{
		this.page = page;
	}

	public List<OrderInfo> getOrderInfos()
	{
		return orderInfos;
	}

	public void setOrderInfos(List<OrderInfo> orderInfos)
	{
		this.orderInfos = orderInfos;
	}

	@Override
	public String toString()
	{
		return "ProfileOrderListInfo [count=" + count + ", page=" + page + ", orderInfos=" + orderInfos + "]";
	}

	/**
	 * 清除数据
	 */
	public void clear()
	{
		if (orderInfos != null)
		{
			orderInfos.clear();
		}
	}

	/**
	 * 添加数据
	 * 
	 * @param profileOrderListInfo
	 */
	public void addAll(ProfileOrderListInfo profileOrderListInfo)
	{
		if (profileOrderListInfo != null)
		{
			if (orderInfos == null)
			{
				orderInfos = new ArrayList<OrderInfo>();
			}
			if (null != profileOrderListInfo.getOrderInfos())
			{
				orderInfos.addAll(profileOrderListInfo.getOrderInfos());
			}
		}
	}

	/**
	 * 判断是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty()
	{
		return orderInfos == null || orderInfos.size() == 0;
	}

	/**
	 * @param orderInfo
	 */
	public boolean removeOrder(OrderInfo orderInfo)
	{
		boolean isHas = false;
		if (orderInfos != null && orderInfos.size() != 0)
		{
			if (orderInfos.contains(orderInfo))
			{
				orderInfos.remove(orderInfo);
				isHas = true;
			}
		}
		return isHas;
	}
}
