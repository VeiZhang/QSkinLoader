package com.excellence.skinloader.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.excellence.skinloader.R;
import com.excellence.skinloader.skin.BaseActivity;
import com.excellence.skinloader.skin.SkinChangeHelper;

public class SkinBySuffixActivity extends BaseActivity implements View.OnClickListener
{
	private ImageButton mButton = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suffix_skin);

		mButton = (ImageButton) findViewById(R.id.image_btn);
		mButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		boolean isDefaultSkin = SkinChangeHelper.getInstance().isDefaultMode();
		if (isDefaultSkin)
		{
			SkinChangeHelper.getInstance().changeSkinBySuffix("_night", new SkinChangeHelper.OnSkinChangeListener()
			{
				@Override
				public void onSuccess()
				{
					Toast.makeText(SkinBySuffixActivity.this, "换肤成功", Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onError()
				{
					Toast.makeText(SkinBySuffixActivity.this, "皮肤包不存在，请安装皮肤包", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(SkinBySuffixActivity.this, "切换默认皮肤成功", Toast.LENGTH_SHORT).show();
				}

				@Override
				public void onError()
				{
					Toast.makeText(SkinBySuffixActivity.this, "切换默认皮肤失败", Toast.LENGTH_SHORT).show();
				}
			});
		}
	}
}
