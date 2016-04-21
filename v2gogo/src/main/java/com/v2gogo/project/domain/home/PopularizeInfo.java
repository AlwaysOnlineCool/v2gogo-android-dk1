package com.v2gogo.project.domain.home;

import java.io.Serializable;
import java.util.List;

/**
 * 推广实体类
 * 
 * @author houjun
 */
public class PopularizeInfo implements Serializable
{
	private static final long serialVersionUID = 3319411353609479418L;

	public static final int TYPE_POPULARIZE_ARTICE = 0;// 文章
	public static final int TYPE_POPULARIZE_GOODS = 1;// 商品
	public static final int TYPE_POPULARIZE_PRIZE = 2;// 奖品
	public static final int TYPE_POPULARIZE_WEBSITE = 3;// 自定义
	public static final int TYPE_POPULARIZE_SUBJECT = 4;// 专题
	public static final int TYPE_POPULARIZE_CONCER_FOOT = 1001;// 加载更多--底部百姓关注文章

	private int srctype;
	private String name;
	private List<PopularizeItemInfo> infos;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public int getSrctype()
	{
		return srctype;
	}

	public void setSrctype(int srctype)
	{
		this.srctype = srctype;
	}

	public List<PopularizeItemInfo> getInfos()
	{
		return infos;
	}

	public void setInfos(List<PopularizeItemInfo> infos)
	{
		this.infos = infos;
	}

	public boolean isEmpty()
	{
		return null == infos || infos.size() == 0;
	}

}
