package org.qcode.qskinloader.base.utils;

import android.content.res.Resources;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2017/7/14
 *     desc   :
 * </pre>
 */

public class ResourceUtils {
	
	/**
	 * get resource id
	 *
	 * @param resources
	 * @param targetName target resource entry name
	 * @param defType resource type name
	 * @param defPackage package name
	 * @param sourceName source resource entry name
	 * @return resource id
	 */
	public static int getIdentifier(Resources resources, String targetName, String defType, String defPackage, String sourceName) {
		int resId = resources.getIdentifier(targetName, defType, defPackage);
		if (resId == 0)
			resId = resources.getIdentifier(sourceName, defType, defPackage);
		return resId;
	}
	
	/**
	 * get resource id, such as drawable or mipmap
	 *
	 * @param resources
	 * @param targetName target resource entry name
	 * @param defPackage package name
	 * @param sourceName source resource entry name
	 * @param defTypes resource type names
	 * @return
	 */
	public static int getIdentifier(Resources resources, String targetName, String defPackage, String sourceName, String... defTypes) {
		int resId = 0;
		if (defTypes == null || defTypes.length == 0)
			return resId;
		for (String type : defTypes) {
			resId = resources.getIdentifier(targetName, type, defPackage);
			if (resId != 0)
				return resId;
		}
		
		for (String type : defTypes) {
			resId = resources.getIdentifier(sourceName, type, defPackage);
			if (resId != 0)
				return resId;
		}
		return resId;
	}
	
}
