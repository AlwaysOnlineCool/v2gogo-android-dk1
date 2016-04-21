/**    
 * @{#} PayMethod.java Create on 2015-12-27 下午4:08:09    
 *    
 * Copyright (c) 2013 by BlueSky.    
 *
 *    
 * @author <a href="1084986314@qq.com">BlueSky</a>   
 * @version 1.0    
 */
package com.v2gogo.project.manager.shop;

import java.io.Serializable;

/**
 * 功能：支付方式实体
 * 
 * @ahthor：黄荣星
 * @date:2015-12-27
 * @version::V1.0
 */
public class PayMethod implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String id;
	private String payName;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getPayName()
	{
		return payName;
	}

	public void setPayName(String payName)
	{
		this.payName = payName;
	}

}
