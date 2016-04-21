package com.v2gogo.project.domain.home.subject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.v2gogo.project.domain.SliderInfo;
import com.v2gogo.project.domain.home.NavInfo;

/**
 * 功能：专题实体
 * 
 * @ahthor：黄荣星
 * @date:2015-11-17
 * @version::V1.0
 */
public class SubjectInfo implements Serializable
{
	private static final long serialVersionUID = 1L;

	private List<SliderInfo> sliders;// 轮播广告
	private List<NavInfo> navs;// 导航栏
	private List<SubjectArticle> infos;// 专题文章
	private List<AdInfo> ads;// 广告
	private List<SubjectArticle> moreArticleInfos;// 更多文章

	private List<String> groups;// 专题列表的组数

	private int pageCount;
	private int page;
	private String name;

	// 专题ID
	private String specialId;

	public String getSpecialId()
	{
		return specialId;
	}

	public void setSpecialId(String specialId)
	{
		this.specialId = specialId;
	}

	public List<SliderInfo> getSliders()
	{
		return sliders;
	}

	public void setSliders(List<SliderInfo> sliders)
	{
		this.sliders = sliders;
	}

	public List<NavInfo> getNavs()
	{
		return navs;
	}

	public void setNavs(List<NavInfo> navs)
	{
		this.navs = navs;
	}

	public List<SubjectArticle> getInfos()
	{
		return infos;
	}

	public void setInfos(List<SubjectArticle> infos)
	{
		this.infos = infos;
	}

	public List<AdInfo> getAds()
	{
		return ads;
	}

	public void setAds(List<AdInfo> ads)
	{
		this.ads = ads;
	}

	public List<SubjectArticle> getMoreArticleInfos()
	{
		return moreArticleInfos;
	}

	public void setMoreArticleInfos(List<SubjectArticle> moreArticleInfos)
	{
		this.moreArticleInfos = moreArticleInfos;
	}

	public int getPageCount()
	{
		return pageCount;
	}

	public void setPageCount(int pageCount)
	{
		this.pageCount = pageCount;
	}

	public int getPage()
	{
		return page;
	}

	public void setPage(int page)
	{
		this.page = page;
	}

	public List<String> getGroups()
	{
		if (groups == null)
		{
			groups = new ArrayList<String>();
			groups.add("推广文章");
			groups.add("广告");
			if (page > 0)
			{
				groups.add("百姓关注");
			}
		}

		return groups;
	}

	public void setGroups(List<String> groups)
	{
		this.groups = groups;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * 是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty()
	{
		return sliders == null || sliders.size() == 0 || navs == null || navs.size() == 0 || infos == null || infos.size() == 0 || ads == null
				|| ads.size() == 0;
	}

	/**
	 * 清除数据
	 */
	public void clear()
	{
		if (sliders != null)
		{
			sliders.clear();
		}
		if (navs != null)
		{
			navs.clear();
		}
		if (infos != null)
		{
			infos.clear();
		}
		if (ads != null)
		{
			ads.clear();
		}
		if (moreArticleInfos != null)
		{
			moreArticleInfos.clear();
		}
	}

	/**
	 * 添加数据
	 */
	public void addAll(SubjectInfo subjectInfo)
	{
		if (subjectInfo != null)
		{
			if (null == sliders)
			{
				sliders = new ArrayList<SliderInfo>();
			}
			if (null == navs)
			{
				navs = new ArrayList<NavInfo>();
			}
			if (null == infos)
			{
				infos = new ArrayList<SubjectArticle>();
			}
			if (null == ads)
			{
				ads = new ArrayList<AdInfo>();
			}
			if (subjectInfo.getSliders() != null)
			{
				sliders.addAll(subjectInfo.getSliders());
			}
			if (subjectInfo.getNavs() != null)
			{
				navs.addAll(subjectInfo.getNavs());
			}
			if (subjectInfo.getInfos() != null)
			{
				infos.addAll(subjectInfo.getInfos());
			}
			if (subjectInfo.getAds() != null)
			{
				ads.addAll(subjectInfo.getAds());
			}
		}
	}

}
