package com.v2gogo.project.domain.home.theme;

import java.io.Serializable;

public class UploadErrorInfo implements Serializable
{

	private static final long serialVersionUID = 2044034140834996936L;

	private int code;
	private String message;

	public int getCode()
	{
		return code;
	}

	public void setCode(int code)
	{
		this.code = code;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	@Override
	public String toString()
	{
		return "UploadErrorInfo [code=" + code + ", message=" + message + "]";
	}

}
