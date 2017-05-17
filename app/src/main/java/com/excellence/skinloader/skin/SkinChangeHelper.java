package com.excellence.skinloader.skin;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.excellence.skinloader.SkinLoaderApplication;

import org.qcode.qskinloader.ILoadSkinListener;
import org.qcode.qskinloader.SkinManager;
import org.qcode.qskinloader.resourceloader.impl.SuffixResourceLoader;

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
	private volatile boolean mIsDefaultMode = false;
	private volatile boolean mIsSwitching = false;

	private SkinChangeHelper()
	{
		mContext = SkinLoaderApplication.getInstance();
		mHandler = new Handler(Looper.getMainLooper());
		mIsDefaultMode = SkinConfigHelper.getInstance().isDefaultSkin();
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

	public void init()
	{
		SkinManager.getInstance().init(mContext);
	}

	public boolean isDefaultMode()
	{
		return mIsDefaultMode;
	}

	public boolean isSwitching()
	{
		return mIsSwitching;
	}

	/**
	 * 通过APK、包名、后缀换肤的方式，恢复默认皮肤
	 *
	 * @param listener
	 */
	public void restoreDefaultSkinByAPKOrPackageOrSuffix(OnSkinChangeListener listener)
	{
		mIsSwitching = true;
		mIsDefaultMode = true;
		SkinManager.getInstance().restoreDefault(SkinConfigHelper.DEFAULT_SKIN, new LoadSkinListener(listener));
	}

	public void changeSkinByPackageSuffix(String packageName, String suffix, OnSkinChangeListener listener)
	{
		mIsSwitching = true;
		mIsDefaultMode = false;
		SkinManager.getInstance().loadAPKSkin(packageName, suffix, new LoadSkinListener(listener));
	}

	public void changeSkinBySuffix(String skinIdentifier, OnSkinChangeListener listener)
	{
		mIsSwitching = true;
		mIsDefaultMode = false;
		SkinManager.getInstance().loadSkin(skinIdentifier, new SuffixResourceLoader(mContext), new LoadSkinListener(listener));
	}

    public void changeLanguageConfigByPackageSuffix(String packageName, String suffix, OnSkinChangeListener listener)
    {
        mIsSwitching = true;
        mIsDefaultMode = false;
        String local = mContext.getResources().getConfiguration().locale.toString();
        SkinManager.getInstance().loadLanguageSkin(packageName, local, suffix, new LoadSkinListener(listener));
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
			SkinConfigHelper.getInstance().saveSkinIdentifier(skinIdentifier);
			SkinConfigHelper.getInstance().saveSkinIdentifierSuffix(suffix);

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
		public void onLanguageLoadSuccess(String languageIdentifier, String local)
		{
			mIsSwitching = false;

			// 存储语言标识
			SkinConfigHelper.getInstance().saveLanguageIdentifier(languageIdentifier);
			SkinConfigHelper.getInstance().saveLanguageLocal(local);

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
