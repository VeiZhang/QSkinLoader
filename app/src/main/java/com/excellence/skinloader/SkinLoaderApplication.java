package com.excellence.skinloader;

import android.app.Application;
import android.content.Context;

import com.excellence.skinloader.skin.Settings;
import com.excellence.skinloader.skin.SkinChangeHelper;
import com.excellence.skinloader.skin.SkinConfigHelper;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : https://veizhang.github.io/
 *     time   : 2017/4/12
 *     desc   :
 * </pre>
 */

public class SkinLoaderApplication extends Application
{
	private static Context mContext = null;

	@Override
	public void onCreate()
	{
		super.onCreate();
		mContext = this;

		Settings.createInstance(mContext);
		SkinChangeHelper.getInstance().init(mContext);
		/** 只能使用一套换肤机制，用哪种，就用哪种restore方法恢复默认皮肤 **/
		/**
		 * <ul>
		 *     <li>{@link SkinChangeHelper#restoreDefaultSkinByConfig(SkinChangeHelper.OnSkinChangeListener)}</li>
		 *     <li>{@link SkinChangeHelper#restoreDefaultSkinByAPKOrPackageOrSuffix(SkinChangeHelper.OnSkinChangeListener)}</li>
		 * </ul>
		 */
		SkinChangeHelper.getInstance().changeSkinByPackageSuffix(SkinConfigHelper.getSkinIdentifier(), SkinConfigHelper.getSkinIdentifierSuffix(), null);
	}

	public static Context getAppContext()
	{
		return mContext;
	}
}
