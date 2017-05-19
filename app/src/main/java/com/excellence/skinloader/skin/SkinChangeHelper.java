package com.excellence.skinloader.skin;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.excellence.skinloader.SkinLoaderApplication;

import org.qcode.qskinloader.ILoadSkinListener;
import org.qcode.qskinloader.ISkinManager;
import org.qcode.qskinloader.SkinManager;
import org.qcode.qskinloader.resourceloader.impl.SuffixResourceLoader;

import java.util.Locale;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : https://veizhang.github.io/
 *     time   : 2017/5/17
 *     desc   : 切换接口示例
 * </pre>
 */
public class SkinChangeHelper
{

	public static final String TAG = SkinChangeHelper.class.getSimpleName();

	private static volatile SkinChangeHelper mInstance;
	private final Context mContext;
	private Handler mHandler = null;
    private SkinConfigHelper mSkinConfigHelper = null;
    private ISkinManager mSkinManager = null;
	private volatile boolean mIsDefaultMode = false;
	private volatile boolean mIsSwitching = false;

	private SkinChangeHelper()
	{
		mContext = SkinLoaderApplication.getInstance();
		mHandler = new Handler(Looper.getMainLooper());
        mSkinConfigHelper = SkinConfigHelper.getInstance();
		mIsDefaultMode = mSkinConfigHelper.isDefaultSkin();
        mSkinManager = SkinManager.getInstance();
        mSkinManager.init(mContext);
    }

	public static SkinChangeHelper getInstance()
	{
		if (null == mInstance)
		{
			synchronized (SkinChangeHelper.class)
			{
				if (null == mInstance)
				{
					mInstance = new SkinChangeHelper();
				}
			}
		}
		return mInstance;
	}

	/**
	 * 是否是默认皮肤
	 *
	 * @return {@code true}:是<br>{@code false}:否
	 */
	public boolean isDefaultMode()
	{
		return mIsDefaultMode;
	}

	/**
	 * 是否正在切换
	 *
	 * @return {@code true}:是<br>{@code false}:否
	 */
	public boolean isSwitching()
	{
		return mIsSwitching;
	}

	/**
	 * 通过APK、包名、后缀换肤的方式，恢复默认皮肤
	 *
	 * @param listener 监听恢复成功或失败
	 */
	public void restoreDefaultSkinByAPKOrPackageOrSuffix(OnSkinChangeListener listener)
	{
		mIsSwitching = true;
		mIsDefaultMode = true;
		mSkinManager.restoreDefault(SkinConfigHelper.DEFAULT_SKIN, new LoadSkinListener(listener));
	}

	/**
	 * 通过后缀查找本应用中的资源，换肤
	 *
	 * @param skinIdentifier 后缀标识
	 * @param listener 监听换肤成功或失败
	 */
	public void changeSkinBySuffix(String skinIdentifier, OnSkinChangeListener listener)
	{
		mIsSwitching = true;
		mIsDefaultMode = false;
		mSkinManager.loadSkin(skinIdentifier, new SuffixResourceLoader(mContext), new LoadSkinListener(listener));
	}

	/**
	 * 通过包名、后缀查找指定的已安装应用中的资源，换肤
	 *
	 * @param packageName 包名
	 * @param suffix 后缀标识
	 * @param listener 监听换肤成功或失败
	 */
	public void changeSkinByPackageSuffix(String packageName, String suffix, OnSkinChangeListener listener)
	{
		mIsSwitching = true;
		mIsDefaultMode = false;
		mSkinManager.loadAPKSkin(packageName, suffix, new LoadSkinListener(listener));
	}

	/**
	 * 通过指定包名、后缀查找已安装应用中的资源，切换语言
	 *
	 * @param packageName 包名
	 * @param suffix 后缀标识
	 * @param listener 监听换肤成功或失败
	 */
    public void changeLanguageConfigByPackageSuffix(String packageName, String suffix, OnSkinChangeListener listener)
    {
        mIsSwitching = true;
        mIsDefaultMode = false;
        String local = mContext.getResources().getConfiguration().locale.toString();
        mSkinManager.loadLanguageSkin(packageName, local, suffix, new LoadSkinListener(listener));
    }

    /**
     * 通过指定包名、后缀查找已安装应用中的资源，切换字体大小
     *
     * @param packageName 包名
     * @param suffix 后缀标识
     * @param listener 监听换肤成功或失败
     */
    public void changeSizeSkinByPackageSuffix(String packageName, String suffix, OnSkinChangeListener listener)
	{
		mIsSwitching = true;
		mIsDefaultMode = false;
		mSkinManager.loadSizeSkin(packageName, suffix, new LoadSkinListener(listener));
	}

	private class LoadSkinListener implements ILoadSkinListener
	{

		private final OnSkinChangeListener mListener;

		public LoadSkinListener(OnSkinChangeListener listener)
		{
			mListener = listener;
		}

		@Override
		public void onLoadStart(String skinIdentifier)
		{
		}

		@Override
		public void onSkinLoadSuccess(String skinIdentifier, String suffix)
		{
			mIsSwitching = false;

			// 存储皮肤标识
            mSkinConfigHelper.saveSkinIdentifier(skinIdentifier);
            mSkinConfigHelper.saveSkinIdentifierSuffix(suffix);

			mHandler.post(new Runnable()
			{
				@Override
				public void run()
				{
					if (null != mListener)
					{
						mListener.onSuccess();
					}
				}
			});
		}

		@Override
		public void onLanguageLoadSuccess(String languageIdentifier, String languageIdentifierSuffix, String local)
		{
			mIsSwitching = false;

			// 存储语言标识
            Locale languageLocale = mContext.getResources().getConfiguration().locale;
            mSkinConfigHelper.saveLanguageIdentifier(languageIdentifier);
            mSkinConfigHelper.saveLanguageIdentifierSuffix(languageIdentifierSuffix);
            mSkinConfigHelper.saveLanguageLocaleLang(languageLocale.getLanguage());
            mSkinConfigHelper.saveLanguageLocaleCountry(languageLocale.getCountry());

			mHandler.post(new Runnable()
			{
				@Override
				public void run()
				{
					if (null != mListener)
					{
						mListener.onSuccess();
					}
				}
			});
		}

		@Override
		public void onSizeLoadSuccess(String sizeIdentifier, String suffix) {
			mIsSwitching = false;

			// 存储字体大小标识
            mSkinConfigHelper.saveSizeIdentifier(sizeIdentifier);
            mSkinConfigHelper.saveSizeIdentifierSuffix(suffix);

			mHandler.post(new Runnable()
			{
				@Override
				public void run()
				{
					if (null != mListener)
					{
						mListener.onSuccess();
					}
				}
			});
		}

		@Override
		public void onLoadFail(String skinIdentifier)
		{
			mIsSwitching = false;

			mHandler.post(new Runnable()
			{
				@Override
				public void run()
				{
					if (null != mListener)
					{
						mListener.onError();
					}
				}
			});
		}
	}

	public interface OnSkinChangeListener
	{
		void onSuccess();

		void onError();
	}
}
