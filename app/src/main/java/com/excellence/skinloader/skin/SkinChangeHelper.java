package com.excellence.skinloader.skin;

import android.content.Context;

import com.excellence.skinloader.SkinLoaderApplication;

import org.qcode.qskinloader.ILoadSkinListener;
import org.qcode.qskinloader.SkinManager;
import org.qcode.qskinloader.resourceloader.impl.ConfigChangeResourceLoader;
import org.qcode.qskinloader.resourceloader.impl.SuffixResourceLoader;

import java.io.File;

/**
 * qqliu
 * 2016/9/26.
 */
public class SkinChangeHelper {

    public static final String TAG = SkinChangeHelper.class.getSimpleName();

    private static volatile SkinChangeHelper mInstance;
    private final Context mContext;

    private SkinChangeHelper() {
        mContext = SkinLoaderApplication.getAppContext();
        mIsDefaultMode = SkinConfigHelper.isDefaultSkin();
    }

    public static SkinChangeHelper getInstance() {
        if (null == mInstance) {
            synchronized (SkinChangeHelper.class) {
                if (null == mInstance) {
                    mInstance = new SkinChangeHelper();
                }
            }
        }
        return mInstance;
    }

    private volatile boolean mIsDefaultMode = false;

    private volatile boolean mIsSwitching = false;

    public void init(Context context) {
        SkinManager.getInstance().init(context);
    }

    public boolean isDefaultMode() {
        return mIsDefaultMode;
    }

    public boolean isSwitching() {
        return mIsSwitching;
    }

    public void restoreDefaultSkinByConfig(OnSkinChangeListener listener)
    {
        mIsSwitching = true;
        mIsDefaultMode = true;
        //基于UIMode换肤只能通过改回配置才能换肤
        changeSkinByConfig(ConfigChangeResourceLoader.MODE_DAY, listener);
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
		SkinManager.getInstance().restoreDefault(SkinConstant.DEFAULT_SKIN, new MyLoadSkinListener(listener));
	}

    /**
     * 需要修改未安装的APK的路径和文件名，有局限性，容易忽视资源文件路径，需要手动push，比较麻烦
     * /data/data/com.excellence.skinloader.cache/Resource.skin
     *
     * @param listener
     */
    public void changeSkinByApk(OnSkinChangeListener listener) {
        SkinUtils.copyAssetSkin(mContext);

        File skin = new File(
                SkinUtils.getTotalSkinPath(mContext));

        if (skin == null || !skin.exists()) {
            UIUtil.showToast(mContext, "皮肤初始化失败，请检查皮肤文件是否存在");
            return;
        }

        mIsSwitching = true;
        mIsDefaultMode = false;

        SkinManager.getInstance().loadAPKSkin(
                skin.getAbsolutePath(), new MyLoadSkinListener(listener));
    }

    public void changeLanguageConfigByPackageSuffix(String packageName, String suffix, OnSkinChangeListener listener)
    {
        mIsSwitching = true;
        mIsDefaultMode = false;
        String local = mContext.getResources().getConfiguration().locale.toString();
        SkinManager.getInstance().loadLanguageSkin(packageName, local, suffix, new MyLoadSkinListener(listener));
    }

    public void changeSkinByPackageSuffix(String packageName, String suffix, OnSkinChangeListener listener)
    {
        mIsSwitching = true;
        mIsDefaultMode = false;
        SkinManager.getInstance().loadAPKSkin(packageName, suffix, new MyLoadSkinListener(listener));
    }

    public void changeSkinBySuffix(String skinIdentifier, OnSkinChangeListener listener) {
        mIsSwitching = true;
        mIsDefaultMode = false;
        SkinManager.getInstance().loadSkin(skinIdentifier,
        new SuffixResourceLoader(mContext),
        new MyLoadSkinListener(listener));
    }

    public void changeSkinByConfig(String mode, OnSkinChangeListener listener) {
        mIsSwitching = true;
        mIsDefaultMode = false;
        SkinManager.getInstance().loadSkin(mode,
                new ConfigChangeResourceLoader(mContext),
                new MyLoadSkinListener(listener));
    }

    private class MyLoadSkinListener implements ILoadSkinListener {

        private final OnSkinChangeListener mListener;

        public MyLoadSkinListener(OnSkinChangeListener listener) {
            mListener = listener;
        }

        @Override
        public void onLoadStart(String skinIdentifier) {
        }

        @Override
        public void onSkinLoadSuccess(String skinIdentifier, String suffix) {
            mIsSwitching = false;

            //存储皮肤标识
            SkinConfigHelper.saveSkinIdentifier(skinIdentifier);
            SkinConfigHelper.saveSkinIdentifierSuffix(suffix);

            UITaskRunner.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    if(null != mListener) {
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
            SkinConfigHelper.saveLanguageIdentifier(languageIdentifier);
            SkinConfigHelper.saveLanguageLocal(local);

            UITaskRunner.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    if(null != mListener) {
                        mListener.onSuccess();
                    }
                }
            });
		}

        @Override
        public void onLoadFail(String skinIdentifier) {
            mIsSwitching = false;

            UITaskRunner.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    if (null != mListener) {
                        mListener.onError();
                    }
                }
            });
        }
    };

    public interface OnSkinChangeListener {
        void onSuccess();

        void onError();
    }
}
