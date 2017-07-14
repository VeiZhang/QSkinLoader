package org.qcode.qskinloader.resourceloader.impl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import org.qcode.qskinloader.IResourceManager;
import org.qcode.qskinloader.base.utils.HashMapCache;
import org.qcode.qskinloader.base.utils.ResourceUtils;
import org.qcode.qskinloader.entity.SkinConstant;

/**
 * 基于资源名称后缀拼接方式的资源管理类
 * qqliu
 * 2016/10/9.
 */
public class SuffixResourceManager implements IResourceManager {
    private static final String TAG = "SuffixResourceManager";

    private Context mContext;
    private Resources mResources;
    private String mSkinSuffix;
    private String mPackageName;

    private HashMapCache<String, Integer> mColorCache
            = new HashMapCache<String, Integer>(true);

    public SuffixResourceManager(Context context, String skinSuffix) {
        mContext = context;
        mPackageName = mContext.getPackageName();
        mResources = mContext.getResources();
        mSkinSuffix = skinSuffix;
    }

    @Override
    public void setBaseResource(String skinIdentifier, IResourceManager baseResource) {
        //do nothing
    }

    @Override
    public String getSkinIdentifier() {
        return mSkinSuffix;
    }

    @Override
    public boolean isDefault() {
        return false;
    }

    @Override
    public int getColor(int resId) {
        String resName = mResources.getResourceEntryName(resId);
        return getColor(resId, resName);
    }

    @Override
    public int getColor(int resId, String resName) {
        Integer color = mColorCache.getCache(resName);
        if (null != color) {
            return color;
        }

        String trueResName = appendSuffix(resName);
        int trueResId = ResourceUtils.getIdentifier(mResources, trueResName, SkinConstant.RES_TYPE_NAME_COLOR, mPackageName, resName);
        int trueColor = mResources.getColor(trueResId);
        mColorCache.addCache(trueResName, trueColor);
        return trueColor;
    }

    public Drawable getDrawable(int resId) {
        String resName = mResources.getResourceEntryName(resId);
        return getDrawable(resId, resName);
    }

    @SuppressLint("NewApi")
    public Drawable getDrawable(int resId, String resName) {
        String trueResName = appendSuffix(resName);
        int trueResId = ResourceUtils.getIdentifier(mResources, trueResName, mPackageName, resName, SkinConstant.RES_TYPE_NAME_DRAWABLE, SkinConstant.RES_TYPE_NAME_MIPMAP);

        Drawable trueDrawable;
        if (android.os.Build.VERSION.SDK_INT < 22) {
            trueDrawable = mResources.getDrawable(trueResId);
        } else {
            trueDrawable = mResources.getDrawable(trueResId, null);
        }

        return trueDrawable;
    }

    @Override
    public ColorStateList getColorStateList(int resId) {
        String resName = mResources.getResourceEntryName(resId);
        return getColorStateList(resId, resName);
    }

    /**
     * 加载指定资源颜色drawable,转化为ColorStateList，保证selector类型的Color也能被转换。
     * 无皮肤包资源返回默认主题颜色
     *
     * @param resId
     * @return
     */
    @Override
    public ColorStateList getColorStateList(int resId, String resName) {
        return getColorStateList(resId, SkinConstant.RES_TYPE_NAME_COLOR, resName);
    }

    @Override
    public ColorStateList getColorStateList(int resId, String typeName, String resName) {
        String trueResName = appendSuffix(resName);
        int trueResId = ResourceUtils.getIdentifier(mResources, trueResName, typeName, mPackageName, resName);
        return mResources.getColorStateList(trueResId);
    }

    /** VeiZhang Text **/
	@Override
	public String getString(int resId, String typeName, String resName)
	{
        String trueResName = appendSuffix(resName);
        int trueResId = ResourceUtils.getIdentifier(mResources, trueResName, typeName, mPackageName, resName);
        return mResources.getString(trueResId);
	}

    /** VeiZhang TextSize **/
    @Override
    public int getDimen(int resId, String typeName, String resName) throws Resources.NotFoundException {
        String trueResName = appendSuffix(resName);
        int trueResId = ResourceUtils.getIdentifier(mResources, trueResName, typeName, mPackageName, resName);
        return mResources.getDimensionPixelOffset(trueResId);
    }

    private String appendSuffix(String resName) {
        return resName + mSkinSuffix;
    }
}
