package com.excellence.skinloader.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.excellence.skinloader.R;
import com.excellence.skinloader.skin.SkinChangeHelper;

import org.qcode.qskinloader.sample.BaseActivity;

public class SkinTextSizeActivity extends BaseActivity implements View.OnClickListener
{
	private TextView mSmallSizeBtn = null;
	private TextView mNormalSizeBtn = null;
	private TextView mBigSizeBtn = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_skin_text_size);

		mSmallSizeBtn = (TextView) findViewById(R.id.small_text);
		mNormalSizeBtn = (TextView) findViewById(R.id.normal_text);
		mBigSizeBtn = (TextView) findViewById(R.id.big_text);

		mSmallSizeBtn.setOnClickListener(this);
		mNormalSizeBtn.setOnClickListener(this);
		mBigSizeBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		String suffix = "";
		switch (v.getId())
		{
		case R.id.small_text:
			suffix = "_small";
			break;

		case R.id.normal_text:
			suffix = "_normal";
			break;

		case R.id.big_text:
			suffix = "_big";
			break;
		}

		SkinChangeHelper.getInstance().changeSizeSkinByPackageSuffix(getPackageName(), suffix, new SkinChangeHelper.OnSkinChangeListener()
		{
			@Override
			public void onSuccess()
			{
				Toast.makeText(SkinTextSizeActivity.this, "字体大小切换成功", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onError()
			{
				Toast.makeText(SkinTextSizeActivity.this, "字体大小切换失败", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
