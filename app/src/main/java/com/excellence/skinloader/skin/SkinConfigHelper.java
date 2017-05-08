package com.excellence.skinloader.skin;

import android.text.TextUtils;

public class SkinConfigHelper {

    /***
     * 获取当前皮肤包的标识
     */
    public static String getSkinIdentifier() {
        return Settings.getInstance().getString(
                SkinConstant.CUSTOM_SKIN_IDENTIFIER,
                SkinConstant.DEFAULT_SKIN);
    }

    /**
     * 获取当前皮肤包后缀
     * @return
     */
    public static String getSkinIdentifierSuffix() {
        return Settings.getInstance().getString(SkinConstant.CUSTOM_SKIN_IDENTIFIER_SUFFIX, "");
    }

    /**
     * 保存皮肤包的标识
     */
    public static void saveSkinIdentifier(String identifier) {
        Settings.getInstance().setSetting(
                SkinConstant.CUSTOM_SKIN_IDENTIFIER,
                identifier);
    }

    /**
     * 保存皮肤后缀标识
     *
     * @param identifierSuffix
     */
    public static void saveSkinIdentifierSuffix(String identifierSuffix)
    {
        if (TextUtils.isEmpty(identifierSuffix) || identifierSuffix.equalsIgnoreCase("null"))
            identifierSuffix = "";
		Settings.getInstance().setSetting(SkinConstant.CUSTOM_SKIN_IDENTIFIER_SUFFIX, identifierSuffix);
	}

    /**
     * 是否默认皮肤
     */
    public static boolean isDefaultSkin() {
        return SkinConstant.DEFAULT_SKIN.equals(getSkinIdentifier());
    }

	/**
	 * 保存语言的标识
	 */
	public static void saveLanguageIdentifier(String identifier)
	{
		Settings.getInstance().setSetting(SkinConstant.CUSTOM_LANGUAGE_IDENTIFIER, identifier);
	}

	/**
	 * 保存语言标识
	 *
	 * @param local
	 */
	public static void saveLanguageLocal(String local)
	{
		if (TextUtils.isEmpty(local) || local.equalsIgnoreCase("null"))
			local = "";
		Settings.getInstance().setSetting(SkinConstant.CUSTOM_LANGUAGE_LOCAL, local);
	}

	public static String getLanguageLocal()
    {
        return Settings.getInstance().getString(SkinConstant.CUSTOM_LANGUAGE_LOCAL, null);
    }
}
