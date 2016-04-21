package com.v2gogo.project.domain.home.subject;

import java.io.Serializable;
import java.util.List;

/**
 * 功能：专题更多文章实体
 * 
 * @ahthor：黄荣星
 * @date:2015-12-7
 * @version::V1.0
 */
public class SubjectMoreArticle implements Serializable
{
	/**
	 * serialVersionUID：
	 */
	private static final long serialVersionUID = 1L;
	private List<SubjectArticle> infos;
	private int page;
	private int pageCount;

	public List<SubjectArticle> getInfos()
	{
		return infos;
	}

	public void setInfos(List<SubjectArticle> infos)
	{
		this.infos = infos;
	}

	public int getPage()
	{
		return page;
	}

	public void setPage(int page)
	{
		this.page = page;
	}

	public int getPageCount()
	{
		return pageCount;
	}

	public void setPageCount(int pageCount)
	{
		this.pageCount = pageCount;
	}

}
