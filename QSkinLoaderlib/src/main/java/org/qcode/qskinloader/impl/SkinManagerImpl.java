package org.qcode.qskinloader.impl;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;

import org.qcode.qskinloader.IActivitySkinEventHandler;
import org.qcode.qskinloader.ILoadSkinListener;
import org.qcode.qskinloader.IResourceLoader;
import org.qcode.qskinloader.IResourceManager;
import org.qcode.qskinloader.ISkinAttrHandler;
import org.qcode.qskinloader.ISkinManager;
import org.qcode.qskinloader.attrhandler.SkinAttrFactory;
import org.qcode.qskinloader.attrhandler.SkinAttrUtils;
import org.qcode.qskinloader.base.observable.INotifyUpdate;
import org.qcode.qskinloader.base.observable.Observable;
import org.qcode.qskinloader.base.utils.CollectionUtils;
import org.qcode.qskinloader.base.utils.Logging;
import org.qcode.qskinloader.base.utils.StringUtils;
import org.qcode.qskinloader.entity.SkinAttrSet;
import org.qcode.qskinloader.resourceloader.ILoadResourceCallback;
import org.qcode.qskinloader.resourceloader.ResourceManager;
import org.qcode.qskinloader.resourceloader.impl.APKResourceLoader;
import org.qcode.qskinloader.resourceloader.impl.LanguageResourceLoader;

import java.util.List;

/**
 * 皮肤加载管理类对外实现接口
 * qqliu
 * 2016/9/24.
 */
public class SkinManagerImpl implements ISkinManager {

    private static final String TAG = "SkinManager";

    //单例相关
    private static volatile SkinManagerImpl mInstance;

    private SkinManagerImpl() {
    }

    public static SkinManagerImpl getInstance() {
        if (null == mInstance) {
            synchronized (SkinManagerImpl.class) {
                if (null == mInstance) {
                    mInstance = new SkinManagerImpl();
                }
            }
        }
        return mInstance;
    }

    private Context mContext;
    private IResourceManager mSkinResourceManager;
    private IResourceManager mLanguageResourceManager;

    private Observable<IActivitySkinEventHandler> mObservable;

    @Override
    public void init(Context context) {
        mContext = context.getApplicationContext();
        mSkinResourceManager = new ResourceManager(mContext);
        mLanguageResourceManager = new ResourceManager(mContext);
        mObservable = new Observable<IActivitySkinEventHandler>();
        new AsyncTask<String, Void, Void>() {

            @Override
            protected Void doInBackground(String... params) {
                return null;
            }

        }.execute("");
    }

    @Override
    public void restoreDefault(String defaultSkinIdentifier, ILoadSkinListener loadListener) {
        if (null != loadListener) {
            loadListener.onLoadStart(defaultSkinIdentifier);
        }

        //恢复ResourceManager的行为
        mSkinResourceManager.setBaseResource(null, null);
        mLanguageResourceManager.setBaseResource(null, null);

        refreshAllSkin();
        refreshAllLanguage();

        if (loadListener != null) {
            loadListener.onSkinLoadSuccess(defaultSkinIdentifier, null);
        }
    }

    private void refreshAllSkin() {
        //刷新正常的Activity内View的皮肤
        refreshSkin();

        //刷新框架内维护的View的皮肤,包括Dialog/popWindow/悬浮窗等应用场景
        applyWindowViewSkin();
    }

    /**
     * VeiZhang 语言切换刷新
     */
    private void refreshAllLanguage()
    {
        /** 刷新语言 **/
        refreshLanguage();

        /** VeiZhang **/
        applyWindowViewLanguage();
    }

    @Override
    public void loadAPKSkin(String skinPath, ILoadSkinListener loadListener) {
        loadSkin(skinPath, new APKResourceLoader(mContext), loadListener);
    }

    @Override
    public void loadSkin(final String skinIdentifier,
                         final IResourceLoader resourceLoader,
                         final ILoadSkinListener loadListener) {
        if(StringUtils.isEmpty(skinIdentifier)
                || null == resourceLoader) {
            if(null != loadListener) {
                loadListener.onLoadFail(skinIdentifier);
            }
            return;
        }

        //当前皮肤就是将要换肤的皮肤，则不执行后续行为
        if (skinIdentifier.equals(mSkinResourceManager.getSkinIdentifier())) {
            Logging.d(TAG, "load()| current skin matches target, do nothing");
            if(null != loadListener) {
                loadListener.onSkinLoadSuccess(skinIdentifier, null);
            }
            return;
        }

        resourceLoader.loadResource(skinIdentifier, new ILoadResourceCallback() {
            @Override
            public void onLoadStart(String identifier) {
                if (loadListener != null) {
                    loadListener.onLoadStart(skinIdentifier);
                }
            }

            @Override
            public void onLoadSuccess(String identifier, IResourceManager result) {
                Logging.d(TAG, "onSkinLoadSuccess() | identifier= " + identifier);
                mSkinResourceManager.setBaseResource(identifier, result);

                refreshAllSkin();

                Logging.d(TAG, "onSkinLoadSuccess()| notify update");
                if (loadListener != null) {
                    loadListener.onSkinLoadSuccess(skinIdentifier, null);
                }
            }

            @Override
            public void onLoadFail(String identifier, int errorCode) {
                mSkinResourceManager.setBaseResource(null, null);
                if (loadListener != null) {
                    loadListener.onLoadFail(skinIdentifier);
                }
            }
        });
    }

    @Override
    public void loadAPKSkin(String packageName, String suffix, ILoadSkinListener loadSkinListener)
    {
        loadSkin(packageName, suffix, new APKResourceLoader(mContext, suffix, true), loadSkinListener);
    }

	@Override
	public void loadSkin(final String skinIdentifier, final String suffix, IResourceLoader resourceLoader, final ILoadSkinListener loadListener)
	{
        // 加载已安装的资源包中带后缀的皮肤的标识，需要加上suffix
        final String newSkinIdentifier = skinIdentifier + suffix;
        if(StringUtils.isEmpty(newSkinIdentifier)
                || null == resourceLoader) {
            if(null != loadListener) {
                loadListener.onLoadFail(newSkinIdentifier);
            }
            return;
        }

        //当前皮肤就是将要换肤的皮肤，则不执行后续行为
        if (newSkinIdentifier.equals(mSkinResourceManager.getSkinIdentifier())) {
            Logging.d(TAG, "load()| current skin matches target, do nothing");
            if(null != loadListener) {
                // 需要保存皮肤标识、后缀标识，后缀标识可为空
                loadListener.onSkinLoadSuccess(skinIdentifier, suffix);
            }
            return;
        }

        resourceLoader.loadResource(skinIdentifier, new ILoadResourceCallback() {
            @Override
            public void onLoadStart(String identifier) {
                if (loadListener != null) {
                    loadListener.onLoadStart(newSkinIdentifier);
                }
            }

            @Override
            public void onLoadSuccess(String identifier, IResourceManager result) {
                Logging.d(TAG, "onSkinLoadSuccess() | identifier= " + identifier);
                mSkinResourceManager.setBaseResource(identifier, result);

                refreshAllSkin();

                Logging.d(TAG, "onSkinLoadSuccess()| notify update");
                if (loadListener != null) {
                    // 需要保存皮肤标识、后缀标识，后缀标识可为空
                    loadListener.onSkinLoadSuccess(skinIdentifier, suffix);
                }
            }

            @Override
            public void onLoadFail(String identifier, int errorCode) {
                mSkinResourceManager.setBaseResource(null, null);
                if (loadListener != null) {
                    loadListener.onLoadFail(newSkinIdentifier);
                }
            }
        });
	}

	@Override
	public void loadLanguageSkin(String packageName, String local, String suffix, ILoadSkinListener loadListener)
	{
		loadLanguage(packageName, local, suffix, new LanguageResourceLoader(mContext, local, suffix), loadListener);
	}

    /**
     *
     * @param skinIdentifier 包名标识
     * @param languageLocal 语言标识
     * @param suffix 后缀标识
     * @param resourceLoader
     * @param loadListener
     */
	private void loadLanguage(final String skinIdentifier, final String languageLocal, final String suffix, IResourceLoader resourceLoader, final ILoadSkinListener loadListener)
	{
        // 加载已安装的资源包中带后缀，需要加上suffix
        final String newSkinIdentifier = skinIdentifier + suffix + "_" + languageLocal;
        if(StringUtils.isEmpty(newSkinIdentifier)
                || null == resourceLoader) {
            if(null != loadListener) {
                loadListener.onLoadFail(newSkinIdentifier);
            }
            return;
        }

        //当前语言就是将要切换的语言，则不执行后续行为
        if (newSkinIdentifier.equals(mLanguageResourceManager.getSkinIdentifier())) {
            Logging.d(TAG, "load()| current language matches target, do nothing");
            if(null != loadListener) {
                // 需要保存包名标识、语言标识
                loadListener.onLanguageLoadSuccess(newSkinIdentifier, languageLocal);
            }
            return;
        }

        resourceLoader.loadResource(skinIdentifier, new ILoadResourceCallback() {
            @Override
            public void onLoadStart(String identifier) {
                if (loadListener != null) {
                    loadListener.onLoadStart(newSkinIdentifier);
                }
            }

            @Override
            public void onLoadSuccess(String identifier, IResourceManager result) {
                Logging.d(TAG, "onSkinLoadSuccess() | identifier= " + identifier);
                mLanguageResourceManager.setBaseResource(identifier, result);

                refreshAllLanguage();

                Logging.d(TAG, "onSkinLoadSuccess()| notify update");
                if (loadListener != null) {
                    // 需要保存皮肤标识、后缀标识，后缀标识可为空
                    loadListener.onLanguageLoadSuccess(newSkinIdentifier, languageLocal);
                }
            }

            @Override
            public void onLoadFail(String identifier, int errorCode) {
                mLanguageResourceManager.setBaseResource(null, null);
                if (loadListener != null) {
                    loadListener.onLoadFail(newSkinIdentifier);
                }
            }
        });
	}

    @Override
    public void applySkin(View view, boolean applyChild) {
        if (null == view) {
            return;
        }

        SkinAttrSet skinAttrSet = ViewSkinTagHelper.getSkinAttrs(view);
        SkinAttrUtils.applySkinAttrs(view, skinAttrSet, mSkinResourceManager);

        if (applyChild) {
            if (view instanceof ViewGroup) {
                //遍历子元素应用皮肤
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    applySkin(viewGroup.getChildAt(i), true);
                }
            }
        }
    }

    @Override
    public void applyLanguage(View view, boolean applyChild) {
        if (null == view) {
            return;
        }

        SkinAttrSet skinAttrSet = ViewSkinTagHelper.getSkinAttrs(view);
        SkinAttrUtils.applyLanguageAttrs(view, skinAttrSet, mLanguageResourceManager);

        if (applyChild) {
            if (view instanceof ViewGroup) {
                //遍历子元素应用皮肤
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    applyLanguage(viewGroup.getChildAt(i), true);
                }
            }
        }
    }

    @Override
    public void registerSkinAttrHandler(String attrName, ISkinAttrHandler skinAttrHandler) {
        SkinAttrFactory.registerSkinAttrHandler(attrName, skinAttrHandler);
    }

    @Override
    public void unregisterSkinAttrHandler(String attrName) {
        SkinAttrFactory.removeSkinAttrHandler(attrName);
    }

    public void setLanguageResourceManager(IResourceManager languageResourceManager)
    {
        if (null == languageResourceManager)
            return;
        mLanguageResourceManager = languageResourceManager;
    }

    public IResourceManager getLanguageResourceManager()
    {
        return mLanguageResourceManager;
    }

    public void setSkinResourceManager(IResourceManager skinResourceManager) {
        if(null == skinResourceManager) {
            return;
        }
        mSkinResourceManager = skinResourceManager;
    }

    public IResourceManager getSkinResourceManager() {
        return mSkinResourceManager;
    }

    @Override
    public void addObserver(IActivitySkinEventHandler observer) {
        mObservable.addObserver(observer);
    }

    @Override
    public void removeObserver(IActivitySkinEventHandler observer) {
        mObservable.removeObserver(observer);
    }

    @Override
    public void notifyUpdate(INotifyUpdate<IActivitySkinEventHandler> callback, String identifier, Object... params) {
        mObservable.notifyUpdate(callback, identifier, params);
    }

    /**刷新Window上添加的View的显示模式*/
    public void applyWindowViewSkin() {
        List<View> windowViewList = WindowViewManager.getInstance().getWindowViewList();
        if(CollectionUtils.isEmpty(windowViewList)) {
            return;
        }

        for(View view : windowViewList) {
            applySkin(view, true);
        }
    }

    /***
     * 告知外部观察者当前皮肤发生了变化
     */
    private void refreshSkin() {
        notifyUpdate(new INotifyUpdate<IActivitySkinEventHandler>() {
            @Override
            public boolean notifyEvent(
                    IActivitySkinEventHandler handler,
                    String identifier,
                    Object... params) {
                handler.handleSkinUpdate();
                return false;
            }
        }, null);
    }

    /**
     * VeiZhang 通知语言切换
     */
    private void refreshLanguage()
    {
        notifyUpdate(new INotifyUpdate<IActivitySkinEventHandler>() {
            @Override
            public boolean notifyEvent(
                    IActivitySkinEventHandler handler,
                    String identifier,
                    Object... params) {
                handler.handleLanguageUpdate();
                return false;
            }
        }, null);
    }

    /**
     * VeiZhang 通知Window上添加的View语言切换
     */
    public void applyWindowViewLanguage()
    {
        List<View> windowViewList = WindowViewManager.getInstance().getWindowViewList();
        if(CollectionUtils.isEmpty(windowViewList)) {
            return;
        }

        for(View view : windowViewList) {
            applyLanguage(view, true);
        }
    }
}
