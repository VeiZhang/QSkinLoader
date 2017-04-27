package com.excellence.skinloader.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.excellence.skinloader.R;
import com.excellence.skinloader.skin.BaseActivity;
import com.excellence.skinloader.skin.SkinChangeHelper;

public class SkinByAPKFileActivity extends BaseActivity implements View.OnClickListener
{
	private Button mButton = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_apkskin);

		mButton = (Button) findViewById(R.id.button);
		mButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		boolean isDefaultSkin = SkinChangeHelper.getInstance().isDefaultMode();
		if (isDefaultSkin)
		{
			SkinChangeHelper.getInstance().changeSkinByApk(new SkinChangeHelper.OnSkinChangeListener()
			{
				@Override
				public void onSuccess()
				{
					Toast.makeText(SkinByAPKFileActivity.this, "换肤成功", Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onError()
				{
					Toast.makeText(SkinByAPKFileActivity.this, "未找到资源APK", Toast.LENGTH_SHORT).show();
				}
			});
		}
		else
		{
			SkinChangeHelper.getInstance().restoreDefaultSkinByAPKOrPackageOrSuffix(new SkinChangeHelper.OnSkinChangeListener()
			{
				@Override
				public void onSuccess()
				{
					Toast.makeText(SkinByAPKFileActivity.this, "恢复默认皮肤成功", Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onError()
				{
					Toast.makeText(SkinByAPKFileActivity.this, "恢复默认皮肤失败", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
}
