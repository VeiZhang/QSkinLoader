package org.qcode.qskinloader.resourceloader.impl;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import org.qcode.qskinloader.IResourceLoader;
import org.qcode.qskinloader.base.utils.Logging;
import org.qcode.qskinloader.base.utils.StringUtils;
import org.qcode.qskinloader.resourceloader.ILoadResourceCallback;

import java.io.File;
import java.lang.reflect.Method;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2017/5/17
 *     desc   : 字体资源加载器
 * </pre>
 */

public class SizeResourceLoader implements IResourceLoader
{
    public static final String TAG = SizeResourceLoader.class.getSimpleName();

    private Context mContext;

    private boolean isLoaderByPackageName = true;

    private String mSuffix = null;

	// 加载已安装的资源包中带后缀的皮肤的标识，需要加上suffix
	private String mNewSkinIdentifier = null;

	public SizeResourceLoader(Context context, String suffix)
	{
		mContext = context;
		mSuffix = suffix;
	}

	@Override
	public void loadResource(String skinIdentifier, final ILoadResourceCallback loadCallBack)
	{
        if (StringUtils.isEmpty(skinIdentifier)) {
            return;
        }
        mNewSkinIdentifier = skinIdentifier + mSuffix;
        new AsyncTask<String, Void, SizeLoadResult>() {

            @Override
            protected void onPreExecute() {
                if (loadCallBack != null) {
                    loadCallBack.onLoadStart(mNewSkinIdentifier);
                }
            }

            @Override
            protected SizeLoadResult doInBackground(String... params) {
                if (null == mContext || null == params || params.length <= 0) {
                    return null;
                }

                try {
                    if (isLoaderByPackageName)
                    {
                        Log.e(TAG, "params---->" + params[0]);

                        Context skinContext = mContext.createPackageContext(params[0], Context.CONTEXT_IGNORE_SECURITY);
                        Resources superResources = skinContext.getResources();
                        return new SizeLoadResult(params[0], superResources);
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
                        return new SizeLoadResult(skinPkgName, skinResource);
                    }
                } catch (Exception ex) {
                    Logging.d(TAG, "doInBackground()| exception happened", ex);
                }

                return null;
            }

            @Override
            protected void onPostExecute(SizeLoadResult result) {
                if (null != result) {
                    if (loadCallBack != null) {
                        if (mSuffix == null)
                            loadCallBack.onLoadSuccess(mNewSkinIdentifier,
                                    new SizeResourceManager(
                                            mContext, result.pkgName, result.resources));
                        else
                            loadCallBack.onLoadSuccess(mNewSkinIdentifier,
                                    new SizeResourceManager(mContext, mSuffix, result.pkgName, result.resources));
                    }
                } else {
                    if (loadCallBack != null) {
                        loadCallBack.onLoadFail(mNewSkinIdentifier, -1);
                    }
                }
            }

        }.execute(skinIdentifier);
	}

    private static class SizeLoadResult {
        String pkgName;
        Resources resources;

        public SizeLoadResult(String pkgName, Resources resources) {
            this.pkgName = pkgName;
            this.resources = resources;
        }
    }
}
