package com.excellence.skinloader;

import android.app.Application;
import android.content.res.Configuration;
import android.text.TextUtils;

import com.excellence.skinloader.skin.SkinChangeHelper;
import com.excellence.skinloader.skin.SkinConfigHelper;

import java.util.Locale;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : https://veizhang.github.io/
 *     time   : 2017/4/12
 *     desc   : 恢复保存的皮肤
 * </pre>
 */

public class SkinLoaderApplication extends Application
{
	private static SkinLoaderApplication mInstance = null;

	@Override
	public void onCreate()
	{
		super.onCreate();
		mInstance = this;
		SkinChangeHelper.getInstance().init();
		/** 只能使用一套换肤机制，用哪种，就用哪种restore方法恢复默认皮肤 **/
		/**
		 * <ul>
		 *     <li>{@link SkinChangeHelper#restoreDefaultSkinByConfig(SkinChangeHelper.OnSkinChangeListener)}</li>
		 *     <li>{@link SkinChangeHelper#restoreDefaultSkinByAPKOrPackageOrSuffix(SkinChangeHelper.OnSkinChangeListener)}</li>
		 * </ul>
		 */

		SkinChangeHelper.getInstance().changeSkinByPackageSuffix(SkinConfigHelper.getInstance().getSkinIdentifier(), SkinConfigHelper.getInstance().getSkinIdentifierSuffix(), null);
		// 恢复默认语言
		Configuration config = getResources().getConfiguration();
		Locale locale = Locale.getDefault();
		String language = SkinConfigHelper.getInstance().getLanguageLocal();
		if (!TextUtils.isEmpty(language))
			locale = new Locale(language);
		config.locale = locale;
		getResources().updateConfiguration(config, getResources().getDisplayMetrics());
		SkinChangeHelper.getInstance().changeLanguageConfigByPackageSuffix(getPackageName(), "", null);
	}

	public static SkinLoaderApplication getInstance()
	{
		return mInstance;
	}
}
