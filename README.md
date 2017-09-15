# RichText
[![](https://jitpack.io/v/developergx/RichText.svg)](https://jitpack.io/#developergx/RichText)<br><br>
用于加载 HTML 标签语言，显示 or 编辑 文字与图片混合排序<br>
在编辑的时候，支持从中间插入文字或图片<br><br>
#### 效果图：
![](https://github.com/developergx/RichText/blob/master/preview.gif)<br><br>
#### 使用方法
* Step 1: Add the JitPack repository to your build file<br>
　　　Add it in your root build.gradle at the end of repositories:<br>
 ```xml
　allprojects {
　　　repositories {
　　　　　...
　　　　　maven { url 'https://jitpack.io' }
　　　}
　}
  ```
* Step 2: Add the dependency
 ```xml
　dependencies {
　　　　compile 'com.github.developergx:RichText:1.0.0'
　}
  ```
* Step 3: xml 中使用
 ```xml
　<com.gx.richtextlibrary.RichTextView
        android:id="@+id/loading_richtext"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:textSize="14sp"
        android:textColor="#797979"
        android:scrollbars="none"
        android:fadingEdge="none"
        android:overScrollMode="never" />
  ```
* Step 4: 代码中使用
 ```xml
　// 加载HTML，使用正则表达式区分出文字和图片，然后加载
  richTextView.post(new Runnable() {
      @Override
      public void run() {
          richTextView.clearAllLayout();
          List<String> list = StringUtils.cutStringByImgTag(" 需要加载的 HTML ");
          for (String str:list){
              if(str.contains("<img")){
                  if(str.contains("src=")){
                      richTextView.createImageView(richTextView.getLastIndex(),StringUtils.getImgSrc(str));
                  }
              }else{
                  richTextView.createTextView(richTextView.getLastIndex(), Html.fromHtml(str).toString());
              }
          }
      }
  });
  ```
  <br>
HTML 必须是以下格式<br>
 ```xml
 文字文字文字 <img src="url"/> 文字文字文字 <img src="url"/>

 /*
  * 其中 <img 标签内容可随便写，只要保证内容包含 src="url" 就可以
  * 如果HTML格式不是以上所描述，会漏掉某些字符的解析，如果非要更改HTML格式，请下载之后自行修改正则表达式
  */
 ```
#### 以上是显示的方法步骤，具体的编辑使用方法，请参考完整代码！<br><br>
That's it! The first time you request a project JitPack checks out the code, builds it and serves the build artifacts.<br>
