package org.qcode.qskinloader.resourceloader.impl;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import org.qcode.qskinloader.resourceloader.ILoadResourceCallback;
import org.qcode.qskinloader.IResourceLoader;
import org.qcode.qskinloader.base.utils.Logging;
import org.qcode.qskinloader.base.utils.StringUtils;

import java.io.File;
import java.lang.reflect.Method;

/**
 * 基于APK方式的资源加载器
 * qqliu
 * 2016/9/25.
 */
public class APKResourceLoader implements IResourceLoader {

    private static final String TAG = "APKResourceLoader";

    private Context mContext;

    private String mPackageName;

    private Resources mResources;

    private boolean isLoaderByPackageName = false;

    private String mSuffix = null;

    // 加载已安装的资源包中带后缀的皮肤的标识，需要加上suffix
    private String mNewSkinIdentifier = null;

    public APKResourceLoader(Context context) {
        this.mContext = context;
    }

	public APKResourceLoader(Context context, String suffix, boolean isLoaderByPackageName)
	{
        this(context);
        mSuffix = suffix;
		this.isLoaderByPackageName = isLoaderByPackageName;
	}

    @Override
    public void loadResource(final String skinIdentifier,
                             final ILoadResourceCallback loadCallBack) {
        if (StringUtils.isEmpty(skinIdentifier)) {
            return;
        }
        mNewSkinIdentifier = skinIdentifier + mSuffix;
        new AsyncTask<String, Void, APkLoadResult>() {

            @Override
            protected void onPreExecute() {
                if (loadCallBack != null) {
                    loadCallBack.onLoadStart(mNewSkinIdentifier);
                }
            }

            @Override
            protected APkLoadResult doInBackground(String... params) {
                if (null == mContext || null == params || params.length <= 0) {
                    return null;
                }

                try {
					if (isLoaderByPackageName)
					{
						Log.e(TAG, "params---->" + params[0]);

						Context skinContext = mContext.createPackageContext(params[0], Context.CONTEXT_IGNORE_SECURITY);
						Resources superResources = skinContext.getResources();
						return new APkLoadResult(params[0], superResources);
					}
					else
					{
                        String skinPkgPath = params[0];

                        File file = new File(skinPkgPath);
                        if (file == null || !file.exists()) {
                            return null;
                        }

                        PackageManager packageManager = mContext.getPackageManager();
                        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(
                                skinPkgPath, PackageManager.GET_ACTIVITIES);
                        String skinPkgName = packageInfo.packageName;

                        AssetManager assetManager = AssetManager.class.newInstance();
                        Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
                        addAssetPath.invoke(assetManager, skinPkgPath);

                        Resources superResources = mContext.getResources();
                        Resources skinResource = new Resources(
                                assetManager, superResources.getDisplayMetrics(),
                                superResources.getConfiguration());
                        return new APkLoadResult(skinPkgName, skinResource);
                    }
                } catch (Exception ex) {
                    Logging.d(TAG, "doInBackground()| exception happened", ex);
                }

                return null;
            }

            @Override
            protected void onPostExecute(APkLoadResult result) {
                if (null != result) {
                    if (loadCallBack != null) {
                        if (mSuffix == null)
                            loadCallBack.onLoadSuccess(mNewSkinIdentifier,
                                new APKResourceManager(
                                        mContext, result.pkgName, result.resources));
                        else
                            loadCallBack.onLoadSuccess(mNewSkinIdentifier,
                                    new APKResourceManager(mContext, mSuffix, result.pkgName, result.resources));
                    }
                } else {
                    if (loadCallBack != null) {
                        loadCallBack.onLoadFail(mNewSkinIdentifier, -1);
                    }
                }
            }

        }.execute(skinIdentifier);
    }

    private static class APkLoadResult {
        String pkgName;
        Resources resources;

        public APkLoadResult(String pkgName, Resources resources) {
            this.pkgName = pkgName;
            this.resources = resources;
        }
    }
}
