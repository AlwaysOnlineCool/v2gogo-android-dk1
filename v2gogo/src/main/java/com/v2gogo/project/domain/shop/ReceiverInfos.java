package com.v2gogo.project.domain.shop;

import java.io.Serializable;

/**
 * 收货人的信息实体类
 * 
 * @author houjun
 */
public class ReceiverInfos implements Serializable
{

	private static final long serialVersionUID = 282389727470130086L;

	private String id;
	private String phone;
	private String address;
	private String consignee;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getConsignee()
	{
		return consignee;
	}

	public void setConsignee(String consignee)
	{
		this.consignee = consignee;
	}

	@Override
	public String toString()
	{
		return "ReceiverInfos [id=" + id + ", phone=" + phone + ", address=" + address + ", consignee=" + consignee + "]";
	}
}
