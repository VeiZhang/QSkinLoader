package org.qcode.qskinloader.attrhandler;

import android.view.View;
import android.widget.TextView;

import org.qcode.qskinloader.IResourceManager;
import org.qcode.qskinloader.ISkinAttrHandler;
import org.qcode.qskinloader.entity.SkinAttr;
import org.qcode.qskinloader.entity.SkinAttrName;
import org.qcode.qskinloader.entity.SkinConstant;

/**
 * <pre>
 *     author : VeiZhang
 *     blog   : http://tiimor.cn
 *     time   : 2017/5/17
 *     desc   : 切换文本大小
 * </pre>
 */

class TextSizeAttrHandler implements ISkinAttrHandler
{

	@Override
	public void apply(View view, SkinAttr skinAttr, IResourceManager resourceManager)
	{
        if (null == view || null == skinAttr || !(SkinAttrName.TEXT_SIZE.equals(skinAttr.mAttrName)))
        {
            return;
        }

        if (!(view instanceof TextView))
            return;

        TextView textView = (TextView) view;
        if (SkinConstant.RES_TYPE_NAME_DIMEN.equals(skinAttr.mAttrValueTypeName))
        {
            int textSize = resourceManager.getDimen(skinAttr.mAttrValueRefId, skinAttr.mAttrValueTypeName, skinAttr.mAttrValueRefName);
            textView.setTextSize(textSize);
        }
	}
}
