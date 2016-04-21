package com.v2gogo.project.domain.profile;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ProvoiceInfo implements Serializable
{

	private static final long serialVersionUID = 8220478204387655324L;

	@SerializedName("state")
	private String state;

	@SerializedName("cities")
	private List<String> cities;

	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public List<String> getCities()
	{
		return cities;
	}

	public void setCities(List<String> cities)
	{
		this.cities = cities;
	}

	@Override
	public String toString()
	{
		return "ProvoiceInfo [state=" + state + ", cities=" + cities + "]";
	}

}
