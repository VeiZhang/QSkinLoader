package org.qcode.qskinloader.resourceloader.impl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import org.qcode.qskinloader.IResourceManager;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2017/4/28
 *     desc   : 语言资源管理器
 *              执行哪种管理器，就在当前界面刷新哪种管理器；其他的监听管理器则在其他界面onResume的时候执行
 * </pre>
 */

public class LanguageResourceManager implements IResourceManager {

    private Context mContext;
    private Resources mDefaultResources;
    private String mPackageName;
    private Resources mResources;
    private String mSuffix = null;

	public LanguageResourceManager(Context context, String pkgName, Resources resources)
	{
		mContext = context;
		mPackageName = pkgName;
		mResources = resources;
		mDefaultResources = mContext.getResources();
	}

	public LanguageResourceManager(Context context, String suffix, String pkgName, Resources resources)
	{
		this(context, pkgName, resources);
		mSuffix = suffix;
	}

    @Override
    public void setBaseResource(String skinIdentifier, IResourceManager baseResource) {
        //do nothing
    }

    @Override
    public String getSkinIdentifier() {
        return null;
    }

    @Override
    public boolean isDefault() {
        return false;
    }

    @Override
    public int getColor(int resId) {
        return -1;
    }

    @Override
    public int getColor(int resId, String resName) {
        return -1;
    }

    public Drawable getDrawable(int resId) {
        return null;
    }

    @SuppressLint("NewApi")
    public Drawable getDrawable(int resId, String resName) {
        return null;
    }

    @Override
    public ColorStateList getColorStateList(int resId) {
        return null;
    }

    /**
     * 读取ColorStateList
     * @param resId
     * @return
     */
    @Override
    public ColorStateList getColorStateList(int resId, String resName) {
        return null;
    }

    @Override
    public ColorStateList getColorStateList(int resId, String typeName, String resName) {
        return null;
    }

    /** VeiZhang Text **/
    @Override
    public String getString(int resId, String typeName, String resName)
    {
        String trueResName = appendSuffix(resName);
        int trueResId = mResources.getIdentifier(trueResName, typeName, mPackageName);
        String text = mResources.getString(trueResId);
        return text;
    }

    /** VeiZhang TextSize **/
    @Override
    public int getDimen(int resId, String typeName, String resName) throws Resources.NotFoundException {
        return 0;
    }

    private String getResKey(String skinPackageName, String resName) {
        return (null == skinPackageName ? "" : skinPackageName) + "_" + resName;
    }

    private String appendSuffix(String resName) {
        return resName + mSuffix;
    }
}
