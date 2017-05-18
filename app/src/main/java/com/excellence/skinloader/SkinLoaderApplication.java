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
	private SkinChangeHelper mSkinChangeHelper = null;
	private SkinConfigHelper mSkinConfigHelper = null;

	@Override
	public void onCreate()
	{
		super.onCreate();
		mInstance = this;
		mSkinChangeHelper = SkinChangeHelper.getInstance();
		mSkinConfigHelper = SkinConfigHelper.getInstance();
		mSkinChangeHelper.init();

		// 恢复保存皮肤
		restoreSkin();
		// 恢复保存语言
		restoreLanguage();
		// 恢复保存字体大小
		restoreSize();
	}

	private void restoreSize()
	{
		mSkinChangeHelper.changeSizeSkinByPackageSuffix(mSkinConfigHelper.getSizeIdentifier(), mSkinConfigHelper.getSizeIdentifierSuffix(), null);
	}

	private void restoreLanguage()
	{
		Configuration config = getResources().getConfiguration();
		Locale locale = Locale.getDefault();
		String language = mSkinConfigHelper.getLanguageLocal();
		if (!TextUtils.isEmpty(language))
			locale = new Locale(language);
		config.locale = locale;
		getResources().updateConfiguration(config, getResources().getDisplayMetrics());
		mSkinChangeHelper.changeLanguageConfigByPackageSuffix(mSkinConfigHelper.getLanguageIdentifier(), mSkinConfigHelper.getLanguageIdentifierSuffix(), null);
	}

	private void restoreSkin()
	{
		/** 只能使用一套换肤机制，用哪种，就用哪种restore方法恢复默认皮肤 **/
		/** 使用切换的方式读取保存的皮肤，不同的换肤方式，目前使用根据已安装的包名换肤 **/
		/**
		 * <ul>
		 *     <li>{@link SkinChangeHelper#restoreDefaultSkinByConfig(SkinChangeHelper.OnSkinChangeListener)}</li>
		 *     <li>{@link SkinChangeHelper#restoreDefaultSkinByAPKOrPackageOrSuffix(SkinChangeHelper.OnSkinChangeListener)}</li>
		 * </ul>
		 */
		mSkinChangeHelper.changeSkinByPackageSuffix(mSkinConfigHelper.getSkinIdentifier(), mSkinConfigHelper.getSkinIdentifierSuffix(), null);
	}

	public static SkinLoaderApplication getInstance()
	{
		return mInstance;
	}
}
