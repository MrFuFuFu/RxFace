RxFace
=====================

用 RxJava, Retrofit, Okhttp 处理人脸识别的简单用例

## Overview

这是一个人脸识别的简单 Demo, 使用了 [FacePlusPlus](http://www.faceplusplus.com.cn/) 的接口。他们的`/detection/detect` 人脸识别接口可以使用普通的 `get` 也可以用 `post` 传递图片二进制流的形式。其中 `post` 的时候遇到了相当多的坑，下面会提。

该 demo 的网络请求库使用了 [Retrofit](https://github.com/square/retrofit) 并集成了 [OkHttp](https://github.com/square/okhttp)，使用 [RxJava](https://github.com/ReactiveX/RxJava) 进行封装，方便以流的形式处理网络回调以及图片处理，View 的注入框架用了 [ButterKnife](https://github.com/JakeWharton/butterknife)，图片加载使用 [Glide](https://github.com/bumptech/glide)。

## Versions

### v1.1

1. 增加了 compose 复用 work thread 处理数据，然后在 main thread 处理结果的逻辑:你可以在这片文章看到更多：[RxWeekend——RxJava周末狂欢](http://www.jianshu.com/p/ce228f517586)
2. mSubscription.unsubscribe();
3. 增加了Service 返回错误情况的处理

### v1.0



## Main difficulties

当直接使用 `get` 通过传图片 Url 拿到人脸识别数据的话是相当简单的，如下请求链接只要使用 `Retrofit` 的 `get` 请求的 `@QueryMap` 传递参数即可: [Get数据Demo](http://apicn.faceplusplus.com/v2/detection/detect?api_key=7cd1e10dc037bbe9e6db2813d6127475&api_secret=gruCjvStG159LCJutENBt6yzeLK_5ggX&url=http://imglife.gmw.cn/attachement/jpg/site2/20111014/002564a5d7d21002188831.jpg)。


主要存在的困难点是，当获取本地图片，再使用 `post` 传二进制图片数据时，`post` 要使用 `MultipartTypedOutput`，可参考 [stackoverflow](http://stackoverflow.com/questions/25249042/retrofit-multiple-images-attached-in-one-multipart-request/25260556#25260556) 的回答。然而，这样并没有结束，根据 FacePlusPlus 提供的 SDK Sample 里的 Httpurlconnection 的得到的 `post` 请求头是这样的：

 `[Content-Disposition: form-data; name="api_key", Content-Type: text/plain; charset=US-ASCII, Content-Transfer-Encoding: 8bit]`
 
 `[Content-Disposition: form-data; name="img"; filename="NoName", Content-Type: application/octet-stream, Content-Transfer-Encoding: binary]`
 
 而使用 `Retrofit` 默认实现的话，我们这样来实现：
 
 ```java
 public static MultipartTypedOutput mulipartData(Bitmap bitmap, String boundary){
    byte[] data = getBitmapByte(bitmap);
    MultipartTypedOutput multipartTypedOutput = new MultipartTypedOutput();
    multipartTypedOutput.addPart("api_key", new TypedString(Constants.API_KEY));
    multipartTypedOutput.addPart("api_secret", new TypedString(Constants.API_SECRET));
    multipartTypedOutput.addPart("img", new TypedByteArray("application/octet-stream", data));
    return multipartTypedOutput;
}
 ```
 
 根据 Sample 的请求头，`RestAdapter` 的请求头参数我们这样设置来：
 
 ```java
 private RequestInterceptor mRequestInterceptor = new RequestInterceptor() {
    @Override
    public void intercept(RequestFacade request) {
        request.addHeader("connection", "keep-alive");
        request.addHeader("Content-Type", "multipart/form-data; boundary="+ getBoundary() + "; charset=UTF-8");
    }
};
 ```
 
 
但是！！！它得到的String参数的头是这样的，这里没有贴出其他的差异，

 ```java
Content-Disposition: form-data; name="api_key"
Content-Type: text/plain; charset=UTF-8
Content-Length: 32
Content-Transfer-Encoding: 8bit
 ``` 
 
所以需要重写三个类:

`MultipartTypedOutput` 为 final 类，所以重写为 `CustomMultipartTypedOutput`，并使其构造函数，增加 boundary 的设置；

`TypedString `默认的编码格式是`UTF-8`，所以重写为 `AsciiTypeString`类，使其编码格式改为 `US-ASCII`；

`TypedByteArray` 默认的的 `fileName()` 方法返回的是 null，而当传图片数据时需要 fileName 为 "NoName"，所以重写为 `CustomTypedByteArray` 类，设置其 fileName 为 "NoName"。
 
 同时需要注意的是在设置 `RestAdapter` 的 header 时，其 boundary 一定要和 `CustomMultipartTypedOutput` 的 boundary 相同，否则服务端无法匹配的！（这个地方，一时没注意，被整了一个多小时才发现！！） 
 
 最后 body 的传参，这样来得到：
 
 ```java
 public static CustomMultipartTypedOutput mulipartData(Bitmap bitmap, String boundary){
    byte[] data = getBitmapByte(bitmap);
    CustomMultipartTypedOutput multipartTypedOutput = new CustomMultipartTypedOutput(boundary);
    multipartTypedOutput.addPart("api_key", "8bit", new AsciiTypeString(Constants.API_KEY));
    multipartTypedOutput.addPart("api_secret", "8bit", new AsciiTypeString(Constants.API_SECRET));
    multipartTypedOutput.addPart("img", new CustomTypedByteArray("application/octet-stream", data));
    return multipartTypedOutput;
}
 ```


## Preview

![image_screen](https://raw.githubusercontent.com/MrFuFuFu/RxFace/master/images/image_screen.png)

![movie_screen](https://raw.githubusercontent.com/MrFuFuFu/RxFace/master/images/movie_screen.gif)


## More about me

* [MrFu-傅圆的个人博客](http://mrfu.me/)



## Acknowledgments

* [Glide](https://github.com/bumptech/glide) -Glide
* [Retrofit](https://github.com/square/retrofit) - Retrofit
* [OkHttp](https://github.com/square/okhttp) - OkHttp
* [RxJava](https://github.com/ReactiveX/RxJava) - RxJava
* [ButterKnife](https://github.com/JakeWharton/butterknife) - ButterKnife



License
============

    Copyright 2015 MrFu

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.