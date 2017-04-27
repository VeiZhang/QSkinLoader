package org.qcode.qskinloader;

/**
 * 加载皮肤过程的事件回调
 *
 * a interface defines the skin loading progress.
 *
 * qqliu
 * 2016/9/24.
 */
public interface ILoadSkinListener {
    /***
     * 加载皮肤开始
     *
     * notify skin-loading begin event
     *
     * @param skinIdentifier
     */
    void onLoadStart(String skinIdentifier);

    /***
     * 加载皮肤完成
     * 需要保存皮肤标识、后缀标识，后缀标识可为空
     *
     * notify skin-loading success event
     *
     * @param skinIdentifier 皮肤标识
     * @param suffix 后缀标识
     */
    void onLoadSuccess(String skinIdentifier, String suffix);

    /***
     * 加载皮肤失败
     *
     * notify skin-loading fail event
     *
     * @param skinIdentifier
     */
    void onLoadFail(String skinIdentifier);
}
