package com.v2gogo.project.views.edittext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.v2gogo.project.R;

/**
 * 字数限制的edittext
 * 
 * @author houjun
 */
public class LimitedEditText extends LinearLayout
{

	private EditText content;// 定义一个文本输入框
	private TextView hasnum;// 用来显示剩余字数
	private TextView totalNum;

	private int num = 100;// 限制的最大字数　　

	@SuppressLint("NewApi")
	public LimitedEditText(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context);
	}

	public LimitedEditText(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public LimitedEditText(Context context)
	{
		super(context);
		init(context);
	}

	/**
	 * 字数限制的edittext
	 * 
	 * @param context
	 */
	private void init(Context context)
	{
		LayoutInflater layoutInflater = LayoutInflater.from(context);
		View view = layoutInflater.inflate(R.layout.limit_edit_text_layout, null);
		content = (EditText) view.findViewById(R.id.common_edittext_content);
		hasnum = (TextView) view.findViewById(R.id.common_left_num);
		totalNum = (TextView) view.findViewById(R.id.common_total_num);
		totalNum.setText(num + "");
		hasnum.setText(0 + "/");
		content.addTextChangedListener(new TextWatcher()
		{
			public void beforeTextChanged(CharSequence s, int start, int count, int after)
			{
			}

			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
			}

			public void afterTextChanged(Editable s)
			{
				int number = num - s.length();
				hasnum.setText("" + number + "/");
			}
		});
		this.addView(view);
	}

	public String getText()
	{
		return content.getText().toString();
	}

	public EditText getInputEditText()
	{
		return content;
	}
}
