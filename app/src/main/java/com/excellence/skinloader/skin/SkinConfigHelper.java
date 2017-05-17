package com.excellence.skinloader.skin;

import android.content.Context;
import android.text.TextUtils;

import com.excellence.basetoolslibrary.utils.DBUtils;
import com.excellence.skinloader.SkinLoaderApplication;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2017/5/12
 *     desc   :
 * </pre>
 */

public class SkinConfigHelper {
	
	private static final String		SKIN_CONFIG						= "skin_config";
	public static final String		PACKAGE_NAME					= SkinLoaderApplication.getInstance().getPackageName();
	
	/**
	 * skin identifier
	 */
	public static final String		CUSTOM_SKIN_IDENTIFIER			= PACKAGE_NAME + ".CUSTOM_SKIN_IDENTIFIER";
	
	/**
	 * skin suffix identifier
	 */
	public static final String		CUSTOM_SKIN_IDENTIFIER_SUFFIX	= PACKAGE_NAME + ".CUSTOM_SKIN_IDENTIFIER_SUFFIX";
	
	/**
	 * language identifier
	 */
	public static final String		CUSTOM_LANGUAGE_IDENTIFIER		= PACKAGE_NAME + ".CUSTOM_LANGUAGE_IDENTIFIER";
	
	/**
	 * language local identifier
	 */
	public static final String		CUSTOM_LANGUAGE_LOCAL			= PACKAGE_NAME + ".CUSTOM_LANGUAGE_LOCAL";
	
	/**
	 * default skin
	 */
	public static final String		DEFAULT_SKIN					= "default";
	
	private static SkinConfigHelper	mInstance						= null;
	
	private Context					mContext						= null;
	
	public static SkinConfigHelper getInstance() {
		if (mInstance == null)
			mInstance = new SkinConfigHelper();
		return mInstance;
	}
	
	private SkinConfigHelper() {
		mContext = SkinLoaderApplication.getInstance();
		DBUtils.init(SKIN_CONFIG);
	}
	
	/**
	 * get current skin identifier
	 *
	 * @return
	 */
	public String getSkinIdentifier() {
		return DBUtils.getString(mContext, CUSTOM_SKIN_IDENTIFIER, DEFAULT_SKIN);
	}
	
	/**
	 * get current skin suffxi identifier
	 *
	 * @return
	 */
	public String getSkinIdentifierSuffix() {
		return DBUtils.getString(mContext, CUSTOM_SKIN_IDENTIFIER_SUFFIX, "");
	}
	
	/**
	 * save skin identifier
	 *
	 * @param identifier
	 */
	public void saveSkinIdentifier(String identifier) {
		DBUtils.setSetting(mContext, CUSTOM_SKIN_IDENTIFIER, identifier);
	}
	
	/**
	 * save skin suffix identifier
	 *
	 * @param identifierSuffix
	 */
	public void saveSkinIdentifierSuffix(String identifierSuffix) {
		if (TextUtils.isEmpty(identifierSuffix) || identifierSuffix.equalsIgnoreCase("null"))
			identifierSuffix = "";
		DBUtils.setSetting(mContext, CUSTOM_SKIN_IDENTIFIER_SUFFIX, identifierSuffix);
	}
	
	/**
	 * is default skin
	 *
	 * @return {@code true}:yes<br>{@code false}:no
	 */
	public boolean isDefaultSkin() {
		return DEFAULT_SKIN.equals(getSkinIdentifier());
	}
	
	/**
	 * save language identifier
	 *
	 * @param identifier
	 */
	public void saveLanguageIdentifier(String identifier) {
		DBUtils.setSetting(mContext, CUSTOM_LANGUAGE_IDENTIFIER, identifier);
	}
	
	/**
	 * save language local identifier
	 *
	 * @param local
	 */
	public void saveLanguageLocal(String local) {
		if (TextUtils.isEmpty(local) || local.equalsIgnoreCase("null"))
			local = "";
		DBUtils.setSetting(mContext, CUSTOM_LANGUAGE_LOCAL, local);
	}
	
	/**
	 * get language local
	 *
	 * @return
	 */
	public String getLanguageLocal() {
		return DBUtils.getString(mContext, CUSTOM_LANGUAGE_LOCAL, null);
	}
}
