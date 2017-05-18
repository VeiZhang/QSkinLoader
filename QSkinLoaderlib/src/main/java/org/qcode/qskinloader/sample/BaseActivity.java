package org.qcode.qskinloader.sample;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;

import org.qcode.qskinloader.IActivitySkinEventHandler;
import org.qcode.qskinloader.ISkinActivity;
import org.qcode.qskinloader.SkinManager;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : https://veizhang.github.io/
 *     time   : 2017/4/12
 *     desc   : 换肤Activity的父类示例；所有换肤的Activity都需要实现ISkinActivity接口
 * </pre>
 */

public abstract class BaseActivity extends Activity implements ISkinActivity
{
	private IActivitySkinEventHandler mSkinEventHandler;
	private boolean mFirstTimeApplySkin = true;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFormat(PixelFormat.RGBA_8888);

		mSkinEventHandler = SkinManager.newActivitySkinEventHandler()
                .setSwitchSkinImmediately(isSwitchSkinImmediately())
                .setSupportSkinChange(isSupportSkinChange())
				.setSupportAllViewSkin(isSupportAllViewSkin())
				.setWindowBackgroundResource(getWindowBackgroundResource())
                .setNeedDelegateViewCreate(true);
		mSkinEventHandler.onCreate(this);
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		mSkinEventHandler.onStart();
	}

	/**
	 * Activity首次onResume时，尽量让子类的view都添加到view树内
	 */
	@Override
	protected void onResume()
	{
		super.onResume();
		if (mFirstTimeApplySkin)
		{
			mSkinEventHandler.onViewCreated();
			mFirstTimeApplySkin = false;
		}
		mSkinEventHandler.onResume();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		super.onWindowFocusChanged(hasFocus);
		mSkinEventHandler.onWindowFocusChanged(hasFocus);
	}

	@Override
	protected void onPause()
	{
		super.onPause();
		mSkinEventHandler.onPause();
	}

	@Override
	protected void onStop()
	{
		super.onStop();
		mSkinEventHandler.onStop();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		mSkinEventHandler.onDestroy();
	}

	/**
	 * 告知当前界面是否支持换肤
	 *
	 * @return {@code true}:支持换肤<br>{@code false}:不支持
	 */
	@Override
	public boolean isSupportSkinChange()
	{
		return true;
	}

	/**
	 * 告知当切换皮肤时，是否立刻刷新当前界面，减轻换肤时性能压力
	 *
	 * @return {@code true}:立刻刷新<br>{@code false}:表示在界面onResume时刷新
	 */
	@Override
	public boolean isSwitchSkinImmediately()
	{
		return false;
	}

	/**
	 * true：默认支持所有View换肤，不用添加skin:enable="true"，不想支持则设置skin:enable="false"
	 * false：默认不支持所有View换肤，对需要换肤的View添加skin:enable="true"
	 *
	 * @return
	 */
	@Override
	public boolean isSupportAllViewSkin() {
		return true;
	}

	/**
	 * 当前界面在换肤时收到的回调，可以在此回调内做一些其他事情
	 * 比如：通知WebView内的页面切换到夜间模式等
	 */
	@Override
	public void handleSkinChange()
	{

	}

	/**
	 * 当前界面在切换语言时收到的回调
	 */
	@Override
	public void handleLanguageChange()
	{

	}

	/**
	 * 当前界面在切换字体大小时收到的回调
	 */
	@Override
	public void handleSizeChange() {

	}

	/**
	 * 告知当前界面Window的background资源，换肤时会寻找对应的资源替换
	 */
	protected int getWindowBackgroundResource()
	{
		return 0;
	}
}
