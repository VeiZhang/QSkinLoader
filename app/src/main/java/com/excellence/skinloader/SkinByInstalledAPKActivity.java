package com.excellence.skinloader;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.excellence.skinloader.skin.BaseActivity;
import com.excellence.skinloader.skin.SkinChangeHelper;

public class SkinByInstalledAPKActivity extends BaseActivity implements View.OnClickListener
{
	private Button mRoundButton = null;
	private Button mBlueButton = null;
	private Button mRedButton = null;
	private Button mEmptySuffix = null;
	private String mSuffix = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_installed_apkskin);

		mRoundButton = (Button) findViewById(R.id.round_btn);
		mBlueButton = (Button) findViewById(R.id.blue_btn);
		mRedButton = (Button) findViewById(R.id.red_btn);
		mEmptySuffix = (Button) findViewById(R.id.no_suffix_btn);
		mRoundButton.setOnClickListener(this);
		mBlueButton.setOnClickListener(this);
		mRedButton.setOnClickListener(this);
		mEmptySuffix.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.round_btn:
			mSuffix = "_round";
			break;

		case R.id.blue_btn:
			mSuffix = "_blue";
			break;

		case R.id.red_btn:
			mSuffix = "_red";
			break;

		case R.id.no_suffix_btn:
			mSuffix = "";
			break;
		}
		refreshSkin();
	}

	private void refreshSkin()
	{
		SkinChangeHelper.getInstance().changeSkinByPackageSuffix("com.excellence.skinresource", mSuffix, new SkinChangeHelper.OnSkinChangeListener()
		{
			@Override
			public void onSuccess()
			{
				Toast.makeText(SkinByInstalledAPKActivity.this, "换肤成功", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onError()
			{
				Toast.makeText(SkinByInstalledAPKActivity.this, "换肤失败，请确定皮肤包是否安装", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
