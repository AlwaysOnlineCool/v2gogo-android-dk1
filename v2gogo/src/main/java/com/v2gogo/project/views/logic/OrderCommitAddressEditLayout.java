package com.v2gogo.project.views.logic;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.v2gogo.project.R;
import com.v2gogo.project.utils.common.StringUtil;

/**
 * 提交订单地址编辑的Linearlayout
 * 
 * @author houjun
 */
public class OrderCommitAddressEditLayout extends LinearLayout implements View.OnClickListener
{
	private LinearLayout mAddRessLayout;
	private TextView mTvName;
	private TextView mTvPhone;
	private TextView mTvAddress;
	private Button mResetAddress;
	private Button mEmptyResetAddress;
	private IonEditAddressCallback mAddressCallback;

	private JSONArray mJsonObj;
	private String[] mProvinceDatas;
	private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	private Map<String, String[]> mAreasDataMap = new HashMap<String, String[]>();

	public OrderCommitAddressEditLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		initViews(context);
	}

	public OrderCommitAddressEditLayout(Context context)
	{
		super(context);
		initViews(context);
	}

	public void setOnAddressCallback(IonEditAddressCallback mAddressCallback)
	{
		this.mAddressCallback = mAddressCallback;
	}

	private void initViews(Context context)
	{
		initJsonData(context);
		initDatas();
		View view = LayoutInflater.from(context).inflate(R.layout.commit_order_edit_address_layout, null);
		mAddRessLayout = (LinearLayout) view.findViewById(R.id.commit_order_edit_layout_container_layout);
		mTvAddress = (TextView) view.findViewById(R.id.commit_order_edit_layout_address);
		mTvName = (TextView) view.findViewById(R.id.commit_order_edit_layout_username);
		mTvPhone = (TextView) view.findViewById(R.id.commit_order_edit_layout_phone);
		mResetAddress = (Button) view.findViewById(R.id.commit_order_edit_layout_reset_address);
		mEmptyResetAddress = (Button) view.findViewById(R.id.commit_order_edit_layout_empty_reset_address);
		mResetAddress.setOnClickListener(this);
		mEmptyResetAddress.setOnClickListener(this);
		this.addView(view);
	}

	public boolean isEmptyName()
	{
		return TextUtils.isEmpty(mTvName.getText().toString());
	}

	public boolean isEmptyAddress()
	{
		return TextUtils.isEmpty(mTvAddress.getText().toString());
	}

	public boolean isEmptyPhone()
	{
		return TextUtils.isEmpty(mTvPhone.getText().toString());
	}

	public boolean isMobileNO()
	{

		Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");

		Matcher m = p.matcher(mTvPhone.getText().toString());

		//System.out.println(m.matches() + "---");

		return m.matches();
	}

	public String getName()
	{
		return StringUtil.replaceBlank(mTvName.getText().toString().trim());
	}

	public String getAddress()
	{
		return StringUtil.replaceBlank(mTvAddress.getText().toString().trim());
	}

	public String getPhone()
	{
		return StringUtil.replaceBlank(mTvPhone.getText().toString().trim());
	}

	/**
	 * 设置地址相关信息
	 * 
	 * @param name
	 * @param phone
	 * @param address
	 */
	public void setAddRessInfos(String name, String phone, String address)
	{
		if (TextUtils.isEmpty(address) && TextUtils.isEmpty(phone) && TextUtils.isEmpty(address))
		{
			mAddRessLayout.setVisibility(View.GONE);
			mEmptyResetAddress.setVisibility(View.VISIBLE);
		}
		else
		{
			// address = "贵州省黔东南苗族侗族自治州黄平县";
			String startAddressString = address;
			// 2016-4-14 收货人地址合法性校验规则 author: AlwaysOnline 黔东南苗族侗族自治州
			if (address.contains("省"))
			{
				String beforeAddressString = "";
				String cityAddress = "";
				int pIndex = address.indexOf("省");
				if (address.substring(0, pIndex + 1).equals("贵州省"))
				{
					address = address.substring(pIndex + 1, address.length());
					if (address.contains("州"))
					{
						String[] city = mCitisDatasMap.get("贵州省");
						for (int i = 0; i < city.length; i++)
						{
							int cIndex = address.indexOf("州");
							if (address.substring(0, cIndex + 1).equals(city[i]))
							{
								beforeAddressString = address.substring(0, cIndex + 1);
								address = address.substring(cIndex + 1, address.length());
								mAddRessLayout.setVisibility(View.VISIBLE);
								mEmptyResetAddress.setVisibility(View.GONE);
								mTvAddress.setText(startAddressString);
								mTvName.setText(name);
								mTvPhone.setText(phone);
								break;
							}
							else
							{
								mAddRessLayout.setVisibility(View.GONE);
								mEmptyResetAddress.setVisibility(View.VISIBLE);
								continue;
							}
						}
						if (beforeAddressString != null && !beforeAddressString.equals(""))
						{
							String[] areaStrings = mAreasDataMap.get(beforeAddressString);
							if (address.contains("市"))
							{
								int aIndex = address.indexOf("市");
								for (int i = 0; i < areaStrings.length; i++)
								{
									if (address.substring(0, aIndex + 1).equals(areaStrings[i]))
									{
										beforeAddressString = address;
										address = address.substring(aIndex + 1, address.length());
										mAddRessLayout.setVisibility(View.VISIBLE);
										mEmptyResetAddress.setVisibility(View.GONE);
										mTvAddress.setText(startAddressString);
										mTvName.setText(name);
										mTvPhone.setText(phone);
										break;
									}
									else
									{
										mAddRessLayout.setVisibility(View.GONE);
										mEmptyResetAddress.setVisibility(View.VISIBLE);
										continue;
									}
								}
							}
							else if (address.contains("区"))
							{
								int aIndex = address.indexOf("区");
								for (int i = 0; i < areaStrings.length; i++)
								{
									if (address.substring(0, aIndex + 1).equals(areaStrings[i]))
									{
										beforeAddressString = address;
										address = address.substring(aIndex + 1, address.length());
										mAddRessLayout.setVisibility(View.VISIBLE);
										mEmptyResetAddress.setVisibility(View.GONE);
										mTvAddress.setText(startAddressString);
										mTvName.setText(name);
										mTvPhone.setText(phone);
										break;
									}
									else
									{
										mAddRessLayout.setVisibility(View.GONE);
										mEmptyResetAddress.setVisibility(View.VISIBLE);
										continue;
									}
								}
							}
							else if (address.contains("县"))
							{
								int aIndex = address.indexOf("县");
								for (int i = 0; i < areaStrings.length; i++)
								{
									if (address.substring(0, aIndex + 1).equals(areaStrings[i]))
									{
										beforeAddressString = address;
										address = address.substring(aIndex + 1, address.length());
										mAddRessLayout.setVisibility(View.VISIBLE);
										mEmptyResetAddress.setVisibility(View.GONE);
										mTvAddress.setText(startAddressString);
										mTvName.setText(name);
										mTvPhone.setText(phone);
										break;
									}
									else
									{
										mAddRessLayout.setVisibility(View.GONE);
										mEmptyResetAddress.setVisibility(View.VISIBLE);
										continue;
									}
								}
							}
						}
					}
					else if (address.contains("市"))
					{
						String[] city = mCitisDatasMap.get("贵州省");
						for (int i = 0; i < city.length; i++)
						{
							int cIndex = address.indexOf("市");
							if (address.substring(0, cIndex + 1).equals(city[i]))
							{
								beforeAddressString = address;
								cityAddress = address.substring(0, cIndex + 1);
								address = address.substring(cIndex + 1, address.length());
								mAddRessLayout.setVisibility(View.VISIBLE);
								mEmptyResetAddress.setVisibility(View.GONE);
								mTvAddress.setText(startAddressString);
								mTvName.setText(name);
								mTvPhone.setText(phone);
								break;
							}
							else
							{
								mAddRessLayout.setVisibility(View.GONE);
								mEmptyResetAddress.setVisibility(View.VISIBLE);
								continue;
							}
						}
						if (beforeAddressString != null && !beforeAddressString.equals(""))
						{
							String[] areaStrings = mAreasDataMap.get(cityAddress);
							if (address.contains("市"))
							{
								int aIndex = address.indexOf("市");
								for (int i = 0; i < areaStrings.length; i++)
								{
									if (address.substring(0, aIndex + 1).equals(areaStrings[i]))
									{
										beforeAddressString = address;
										address = address.substring(aIndex + 1, address.length());
										mAddRessLayout.setVisibility(View.VISIBLE);
										mEmptyResetAddress.setVisibility(View.GONE);
										mTvAddress.setText(startAddressString);
										mTvName.setText(name);
										mTvPhone.setText(phone);
										break;
									}
									else
									{
										mAddRessLayout.setVisibility(View.GONE);
										mEmptyResetAddress.setVisibility(View.VISIBLE);
										continue;
									}
								}
							}
							else if (address.contains("区"))
							{
								int aIndex = address.indexOf("区");
								for (int i = 0; i < areaStrings.length; i++)
								{
									if (address.substring(0, aIndex + 1).equals(areaStrings[i]))
									{
										beforeAddressString = address;
										address = address.substring(aIndex + 1, address.length());
										mAddRessLayout.setVisibility(View.VISIBLE);
										mEmptyResetAddress.setVisibility(View.GONE);
										mTvAddress.setText(startAddressString);
										mTvName.setText(name);
										mTvPhone.setText(phone);
										break;
									}
									else
									{
										mAddRessLayout.setVisibility(View.GONE);
										mEmptyResetAddress.setVisibility(View.VISIBLE);
										continue;
									}
								}
							}
							else if (address.contains("县"))
							{
								int aIndex = address.indexOf("县");
								for (int i = 0; i < areaStrings.length; i++)
								{
									if (address.substring(0, aIndex + 1).equals(areaStrings[i]))
									{
										beforeAddressString = address;
										address = address.substring(aIndex + 1, address.length());
										mAddRessLayout.setVisibility(View.VISIBLE);
										mEmptyResetAddress.setVisibility(View.GONE);
										mTvAddress.setText(startAddressString);
										mTvName.setText(name);
										mTvPhone.setText(phone);
										break;
									}
									else
									{
										mAddRessLayout.setVisibility(View.GONE);
										mEmptyResetAddress.setVisibility(View.VISIBLE);
										continue;
									}
								}
							}
							else
							{
								mAddRessLayout.setVisibility(View.GONE);
								mEmptyResetAddress.setVisibility(View.VISIBLE);
							}
						}
					}
					else
					{
						mAddRessLayout.setVisibility(View.GONE);
						mEmptyResetAddress.setVisibility(View.VISIBLE);
					}
				}
				else
				{
					mAddRessLayout.setVisibility(View.GONE);
					mEmptyResetAddress.setVisibility(View.VISIBLE);
				}
			}
			else
			{
				mAddRessLayout.setVisibility(View.GONE);
				mEmptyResetAddress.setVisibility(View.VISIBLE);
			}

		}
	}

	public interface IonEditAddressCallback
	{
		public void onEditAddress();
	}

	@Override
	public void onClick(View view)
	{
		switch (view.getId())
		{
			case R.id.commit_order_edit_layout_reset_address:
				if (mAddressCallback != null)
				{
					mAddressCallback.onEditAddress();
				}
				break;

			case R.id.commit_order_edit_layout_empty_reset_address:
				if (mAddressCallback != null)
				{
					mAddressCallback.onEditAddress();
				}
				break;
		}
	}

	/**
	 * 从文件中读取地址数据
	 */
	private void initJsonData(Context context)
	{
		try
		{
			StringBuffer sb = new StringBuffer();
			InputStream is = context.getAssets().open("v2gogocity.txt");
			int len = -1;
			byte[] buf = new byte[1024];
			while ((len = is.read(buf)) != -1)
			{
				sb.append(new String(buf, 0, len, "utf-8"));
			}
			is.close();
			mJsonObj = new JSONArray(sb.toString());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * 解析数据
	 */
	private void initDatas()
	{
		try
		{
			JSONArray jsonArray = mJsonObj;
			mProvinceDatas = new String[jsonArray.length()];
			for (int i = 0; i < jsonArray.length(); i++)
			{
				JSONObject jsonP = jsonArray.getJSONObject(i);
				String province = jsonP.getString("state");

				mProvinceDatas[i] = province;

				JSONArray jsonCs = null;
				try
				{
					/**
					 * Throws JSONException if the mapping doesn't exist or is
					 * not a JSONArray.
					 */
					jsonCs = jsonP.getJSONArray("cities");
				}
				catch (Exception e1)
				{
					continue;
				}
				String[] mCitiesDatas = new String[jsonCs.length()];
				for (int j = 0; j < jsonCs.length(); j++)
				{
					JSONObject jsonCity = jsonCs.getJSONObject(j);
					String city = jsonCity.getString("city");
					mCitiesDatas[j] = city;
					JSONArray jsonAreas = null;
					try
					{
						/**
						 * Throws JSONException if the mapping doesn't exist or
						 * is not a JSONArray.
						 */
						jsonAreas = jsonCity.getJSONArray("areas");
					}
					catch (Exception e)
					{
						continue;
					}

					String[] mAreasDatas = new String[jsonAreas.length()];
					for (int k = 0; k < jsonAreas.length(); k++)
					{
						String area = jsonAreas.getString(k);
						mAreasDatas[k] = area;
					}
					mAreasDataMap.put(city, mAreasDatas);
				}
				mCitisDatasMap.put(province, mCitiesDatas);
			}

		}
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		mJsonObj = null;
	}

}
