# Android换肤框架

<br>

[![Bintray][icon_Bintray]][Bintray]

<br>

**在原作者换肤的肩膀上，拓展集成切换语言等功能，并且上传公共依赖库，方便大家引用。**

## 目录<a name="目录">
* [引入](#引入)
* [源功能](#源功能)
    * [使用方式](#使用方式)
    * [换肤](#换肤)
* [拓展](#拓展)
    * [切换语言](#切换语言)
    * [切换字体大小](#切换字体大小)
* [注意](#注意)
* [版本更新](#版本更新)
* [感谢](#感谢)

## 引入
AndroidStudio添加jCenter远程依赖到module里的build.gradle：
```
dependencies {
    compile 'com.excellence:skinloader:1.2.1'
    // 或者直接使用最新版本
    // compile 'com.excellence:skinloader:+'
}
```
或者直接添加本地Library依赖
```
compile project(':QSkinLoaderlib')
```

<br>

## 源功能<a name="源功能">

### **[大神传送门][传送门]**


### 使用方式<a name="使用方式">
* 继承BaseActivity
    ```
    示例如org.qcode.qskinloader.sample.BaseActivity;
    让每个界面支持换肤，需要实现ISkinActivity接口
    ```
* BaseActivity中开启换肤控制
    ```java
    /**
     * 告知当前界面是否支持换肤
     *
     * @return {@code true}:支持换肤<br>{@code false}:不支持
     */
    @Override
    public boolean isSupportSkinChange()
    {
        return true;
    }

    /**
     * 告知当切换皮肤时，是否立刻刷新当前界面，减轻换肤时性能压力
     *
     * @return {@code true}:立刻刷新<br>{@code false}:表示在界面onResume时刷新
     */
    @Override
    public boolean isSwitchSkinImmediately()
    {
        return false;
    }

    /**
     * true：默认支持所有View换肤，不用添加skin:enable="true"，不想支持则设置skin:enable="false"
     * false：默认不支持所有View换肤，对需要换肤的View添加skin:enable="true"
     *
     * @return
     */
    @Override
    public boolean isSupportAllViewSkin() {
        return true;
    }
    ```

两种方式，让View支持换肤框架
* xml中添加标识

    skin的命名空间
    ```xml
    xmlns:skin="http://schemas.android.com/android/skin"
    ```
    然后对所有需要换肤的View增加属性
    ```xml
    <View
        skin:enable="true"
    />
    ```

* 动态创建标识
    ```java
    SkinManager
        .with(textview)
        .setViewAttrs(SkinAttrName.BACKGROUND, R.color.white)
        .addViewAttrs(SkinAttrName.TEXT_COLOR, R.color.black)
        .applySkin(false);
    ```

### 换肤<a name="换肤">
* 通过后缀查找当前应用中的资源，进行换肤
    ```java
    /**
     * 通过后缀查找本应用中的资源，换肤
     *
     * @param skinIdentifier 后缀标识
     * @param listener 监听换肤成功或失败
     */
    public void changeSkinBySuffix(String skinIdentifier, OnSkinChangeListener listener)
    {
        mIsSwitching = true;
        mIsDefaultMode = false;
        SkinManager.getInstance().loadSkin(skinIdentifier, new SuffixResourceLoader(mContext), new LoadSkinListener(listener));
    }
    ```

* 通过包名、后缀，查找指定的已安装应用中的资源，进行换肤
    ```java
    /**
     * 通过包名、后缀查找指定的已安装应用中的资源，换肤
     *
     * @param packageName 包名
     * @param suffix 后缀标识
     * @param listener 监听换肤成功或失败
     */
    public void changeSkinByPackageSuffix(String packageName, String suffix, OnSkinChangeListener listener)
    {
        mIsSwitching = true;
        mIsDefaultMode = false;
        SkinManager.getInstance().loadAPKSkin(packageName, suffix, new LoadSkinListener(listener));
    }
    ```

* 恢复默认皮肤
    ```java
    /**
     * 通过APK、包名、后缀换肤的方式，恢复默认皮肤
     *
     * @param listener 监听恢复成功或失败
     */
    public void restoreDefaultSkinByAPKOrPackageOrSuffix(OnSkinChangeListener listener)
    {
        mIsSwitching = true;
        mIsDefaultMode = true;
        SkinManager.getInstance().restoreDefault(SkinConfigHelper.DEFAULT_SKIN, new LoadSkinListener(listener));
    }
    ```

## 拓展

### 切换语言<a name="切换语言">
* 首先切换语言配置
    ```java
    Configuration config = getResources().getConfiguration();
    switch (position)
    {
    case 0:
        config.locale = Locale.ENGLISH;
        break;

    case 1:
        config.locale = Locale.CHINESE;
        break;
    }
    getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    ```

* 然后进行切换语言
    ```java
    /**
     * 通过指定包名、后缀查找已安装应用中的资源，切换语言
     *
     * @param packageName 包名
     * @param suffix 后缀标识
     * @param listener 监听换肤成功或失败
     */
    public void changeLanguageConfigByPackageSuffix(String packageName, String suffix, OnSkinChangeListener listener)
    {
        mIsSwitching = true;
        mIsDefaultMode = false;
        String local = mContext.getResources().getConfiguration().locale.toString();
        SkinManager.getInstance().loadLanguageSkin(packageName, local, suffix, new LoadSkinListener(listener));
    }
    ```

### 切换字体大小<a name="切换字体大小">
* 传入标识
    ```java
    String suffix = "";
    switch (v.getId())
    {
    case R.id.small_text:
        suffix = "_small";
        break;

    case R.id.normal_text:
        suffix = "_normal";
        break;

    case R.id.big_text:
        suffix = "_big";
        break;
    }

    SkinChangeHelper.getInstance().changeSizeSkinByPackageSuffix(getPackageName(), suffix, new SkinChangeHelper.OnSkinChangeListener()
    {
        @Override
        public void onSuccess()
        {
            Toast.makeText(SkinTextSizeActivity.this, "字体大小切换成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError()
        {
            Toast.makeText(SkinTextSizeActivity.this, "字体大小切换失败", Toast.LENGTH_SHORT).show();
        }
    });
    ```

* 切换字体大小
    ```java
    /**
     * 通过指定包名、后缀查找已安装应用中的资源，切换字体大小
     *
     * @param packageName 包名
     * @param suffix 后缀标识
     * @param listener 监听换肤成功或失败
     */
    public void changeSizeSkinByPackageSuffix(String packageName, String suffix, OnSkinChangeListener listener)
    {
        mIsSwitching = true;
        mIsDefaultMode = false;
        SkinManager.getInstance().loadSizeSkin(packageName, suffix, new LoadSkinListener(listener));
    }
    ```

## 注意<a name="注意">
* 不支持保存换肤、切换语言，重启应用会恢复默认皮肤和语言，需要自行恢复已保存的皮肤和语言

## 版本更新<a name="版本更新">
| 版本 | 描述 | 日期 |
| --- | ---- | --- |
| [1.2.1][SkinLoaderV1.2.1] | 是否默认支持所有的View换肤 | **2017-5-18** |
| [1.2.0][SkinLoaderV1.2.0] | 集成TextView切换字体大小功能，支持弹窗里TextView字体大小切换 | **2017-5-18** |
| [1.1.0][SkinLoaderV1.1.0] | 切换语言，切换Activity、Fragment、弹窗里TextView文本语言 | **2017-5-15** |
| [1.0.0][SkinLoaderV1.0.0] | 换肤，切换Activity、Fragment、弹窗里View的背景、颜色、图片 | **2017-5-12** |

## 感谢<a name="感谢">

> - [QQ Liu][QSkinLoader]
> - [fengtianyou][fengtianyou]

[Bintray]:https://bintray.com/veizhang/maven/skinloader "Bintray"
[icon_Bintray]:https://img.shields.io/badge/Bintray-v1.2.1-brightgreen.svg
[传送门]:https://github.com/VeiZhang/QSkinLoader/tree/VeiZhang/README
[QSkinLoader]:https://github.com/qqliu10u/QSkinLoader
[fengtianyou]:https://github.com/fengtianyou

[SkinLoaderV1.2.1]:https://bintray.com/veizhang/maven/skinloader/1.2.1
[SkinLoaderV1.2.0]:https://bintray.com/veizhang/maven/skinloader/1.2.0
[SkinLoaderV1.1.0]:https://bintray.com/veizhang/maven/skinloader/1.1.0
[SkinLoaderV1.0.0]:https://bintray.com/veizhang/maven/skinloader/1.0.0
