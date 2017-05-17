package com.excellence.skinloader;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.excellence.basetoolslibrary.utils.ActivityUtils;
import com.excellence.skinloader.activity.SkinByAPKFileActivity;
import com.excellence.skinloader.activity.SkinByInstalledAPKActivity;
import com.excellence.skinloader.activity.SkinBySuffixActivity;
import com.excellence.skinloader.activity.SkinLanguageActivity;
import com.excellence.skinloader.activity.SkinTextSizeActivity;
import com.excellence.skinloader.fragment.MainFragment;
import org.qcode.qskinloader.sample.BaseActivity;

public class MainActivity extends BaseActivity implements AdapterView.OnItemClickListener
{
	private String[] mNames = new String[] { "当前APK内后缀换肤", "未安装的资源APK换肤", "已安装的APK换肤", "语言切换", "字体大小切换" };
	private GridView mGridView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initView();
	}

	private void initView()
	{
		MainFragment fragment = new MainFragment();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.add(R.id.frame_layout, fragment).commit();

		mGridView = (GridView) findViewById(R.id.gridview);
		mGridView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mNames));
		mGridView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		switch (position)
		{
		case 0:
			ActivityUtils.startAnotherActivity(MainActivity.this, SkinBySuffixActivity.class);
			break;

		case 1:
			ActivityUtils.startAnotherActivity(MainActivity.this, SkinByAPKFileActivity.class);
			break;

		case 2:
			ActivityUtils.startAnotherActivity(MainActivity.this, SkinByInstalledAPKActivity.class);
			break;

		case 3:
			ActivityUtils.startAnotherActivity(MainActivity.this, SkinLanguageActivity.class);
			break;

		case 4:
			ActivityUtils.startAnotherActivity(MainActivity.this, SkinTextSizeActivity.class);
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (event.getAction() == KeyEvent.ACTION_DOWN)
		{
			switch (keyCode)
			{
			case KeyEvent.KEYCODE_BACK:
			case KeyEvent.KEYCODE_ESCAPE:
				android.os.Process.killProcess(android.os.Process.myPid());
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
}
