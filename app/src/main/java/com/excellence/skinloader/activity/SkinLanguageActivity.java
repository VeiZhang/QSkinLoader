package com.excellence.skinloader.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.excellence.basetoolslibrary.baseadapter.CommonAdapter;
import com.excellence.basetoolslibrary.baseadapter.ViewHolder;
import com.excellence.skinloader.R;
import com.excellence.skinloader.skin.BaseActivity;
import com.excellence.skinloader.skin.SkinChangeHelper;


import java.util.Locale;

public class SkinLanguageActivity extends BaseActivity implements AdapterView.OnItemClickListener
{
	private GridView mGridView = null;
	private String[] mLanguages = new String[] { "English", "中文" };

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_skin_language);

		mGridView = (GridView) findViewById(R.id.language_grid);
		mGridView.setAdapter(new LanguageAdapter(this, mLanguages, R.layout.text_item));
		mGridView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		Configuration config = getResources().getConfiguration();
		switch (position)
		{
		case 0:
			config.setLocale(Locale.ENGLISH);
			break;

		case 1:
			config.setLocale(Locale.CHINESE);
			break;
		}
		getResources().updateConfiguration(config, getResources().getDisplayMetrics());

		System.out.println(getResources().getConfiguration().locale);

		SkinChangeHelper.getInstance().changeSkinByPackageSuffix(getPackageName(), "", new SkinChangeHelper.OnSkinChangeListener()
		{
			@Override
			public void onSuccess()
			{
				Toast.makeText(SkinLanguageActivity.this, "语言切换成功", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onError()
			{
				Toast.makeText(SkinLanguageActivity.this, "语言切换失败", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private class LanguageAdapter extends CommonAdapter<String>
	{

		public LanguageAdapter(Context context, String[] datas, @LayoutRes int layoutId)
		{
			super(context, datas, layoutId);
		}

		@Override
		public void convert(ViewHolder viewHolder, String item, int position)
		{
			viewHolder.setText(R.id.language_item_text, item);
		}

	}
}
