TheSceneryAlong
===============

## fork from https://github.com/John-Chen/TheSceneryAlong

## ｈ这是Android Studio 版本

“路上的风景”，基于高德矢量地图的一款记录轨迹和风景的软件。
主要功能：记录轨迹及风景、轨迹导入导出、离线地图下载。
算是自己独立设计、切图、开发测试并上线的第一款应用，功能比较简单，有相似需求的朋友可以参考下。

开发这个产品，也主要是想熟悉下应用上传流程，以及尝试下广告盈利模式。不过貌似添加广告后，应用推广很难，一般首发就不能带广告，所以暂时不想折腾了，代码开源，还能帮助下其他开发者，也算是有个开源项目了。

嗯，可能有一些bug，欢迎反馈。

详细介绍可以去　release　里下载使用。

使用说明：
要正常使用地图，地图key有效。

项目使用了一些开源项目：数据库GreenDao、消息总线EventBus、统一dialog样式的SimpleDialogFragment及其他UI控件。


几个lib project说明：

lib_map_gaode：移植的一个google地图的开源工具库；

CsqCommonLib：一些用到的开源控件及工具集合；

LibAppcompat_v7：google提供的ActionBar兼容包；

TsaDaoGenerator：GreenDao数据库文件生成工程，作为java工程导入。

