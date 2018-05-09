# MTrans 项目介绍
本项目 [**多源翻译器**](https://github.com/hujingshuang/MTrans) (Multisource Translator, MTrans)，是集多种常见在线翻译于一身的轻量级翻译器。通过程序向所支持的在线翻译服务器发送 HTTP 请求，获取并解析返回的结果，为使用者提供便利。目前，本项目暂不提供在线翻译功能，如有需要，开发者可基于此进行二次开发。
目前支持 **翻译源** 及 **语种** 如下:

| 翻译源 | 服务器地址 | 支持语种 | 方式
| :-: | :-: | :-: | :-: |
| [百度翻译](http://fanyi.baidu.com/) | http://fanyi.baidu.com/v2transapi | 中文、英语、日语、韩语、法语、俄语、德语 | 互译
| [有道翻译](http://fanyi.youdao.com/) | http://fanyi.youdao.com/translate_o?smartresult=dict&smartresult=rule | 中文、英语、日语、韩语、法语、俄语 | 互译
| [谷歌翻译](http://translate.google.cn/) | https://translate.google.cn/translate_a/single | 中文、英语、日语、韩语、法语、俄语、德语 | 互译
| [腾讯翻译君](http://fanyi.qq.com/) | http://fanyi.qq.com/api/translate | 中文、英语、日语、韩语、法语、俄语、德语 | 互译
| [欧米翻译](http://www.omifanyi.com/) | http://www.omifanyi.com/transSents.do | 中文、英语 | 互译
| [TryCan](http://www.trycan.com/) | http://fanyi.trycan.com/Transfer.do | 中文、英语 | 互译
| [金山爱词霸](http://fy.iciba.com/) | http://fy.iciba.com/ajax.php?a=fy | 中文、英语、日语、韩语、法语、德语 | 互译
| [搜狗翻译](http://fanyi.sogou.com/) | http://fanyi.sogou.com/reventondc/translate | 中文、英语、日语、韩语、法语、俄语、德语 | 互译

# 一、MTrans 使用说明
## 1. 环境配置
本项目使用 IDEA + Maven 进行开发，请在 pom.xml 中添加如下依赖。
``` xml
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.5.5</version>
</dependency>

<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.9.5</version>
</dependency>
```

## 2. 应用举例
``` java
import com.swjtu.lang.Lang;
import com.swjtu.querier.Querier;
import com.swjtu.trans.*;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        Querier querier = Querier.getQuerier();                                  // 获取查询器

        querier.setParams(Lang.ZH, Lang.EN, "如果这都不算爱，我有什么好悲哀！");    // 设置参数

        querier.attach(new Baidu());                                             // 向查询器中添加 Baidu 翻译器
        querier.attach(new Youdao());                                            // 向查询器中添加 Youdao 翻译器
        querier.attach(new Google());                                            // 向查询器中添加 Google 翻译器
        querier.attach(new Tencent());                                           // 向查询器中添加 Tencent 翻译器
        querier.attach(new Omi());                                               // 向查询器中添加 Omi 翻译器
        querier.attach(new Trycan());                                            // 向查询器中添加 Trycan 翻译器
        querier.attach(new Iciba());                                             // 向查询器中添加 Iciba 翻译器
        querier.attach(new Sogou());                                             // 向查询器中添加 Sogou 翻译器

        List<String> result = querier.execute();                                 // 执行查询并接收查询结果

        for (String str : result) {
            System.out.println(str);
        }
    }
}
```

## 3. 包和类说明

### 1、包/类 一览表
本项目中主要定义了如下几个包，其命名及作用如下表：

| 包名 | 包含类 | 说明
| :- | :-: | :-: |
| com.swjtu.lang | Lang | 支持的语种列表
| com.swjtu.util | Util | 工具包
| com.swjtu.querier | Querier | 查询器
| com.swjtu.trans | Translator、Baidu、Youdao、Google、Tencent、Omi、Trycan、Iciba、Sogou | 翻译器

### 2、类说明
- Lang 枚举：定义所支持或将支持的语种，统一并规范了语种列表。
``` java
public enum Lang {
    ZH,             // 中文
    EN,             // 英语
    JP,             // 日语
    JPKA,           // 日语假名
    TH,             // 泰语
    ...
}
```

- Util 类：包含并实现了一些实用方法。
``` java
public static List<NameValuePair> map2list(Map<String, String> mapParams);              // 将 Map 转换成 List
public static String getUrlWithQueryString(String url, Map<String, String> params);     // 生成 URL
// 各种格式的 MD5
public static String md5(String input);
public static String md5(File file);
public static String md5(InputStream in);
```

- Querier 类：定义了 Querier 类，该类是一个线程安全的单例，同时使用了观察者模式。该类包含了一个集合，集合中的元素为翻译器，通过 setParams() 设定好参数后，执行 execute() 方法便可发送翻译请求，同时返回解析好的翻译结果。可以通过 attach() 和 detach() 方法向集合中添加或移除翻译器。
``` java
private List<Translator> translators;                               // 集合

public static Querier getQuerier();                                 // 获取单例

public void setParams(Lang from, Lang to, String text);             // from 源语种，to 目标语种，text 待翻译内容
public List<String> execute() {
    List<String> result = new ArrayList<String>();
    for (Translator translator : translators) {
        result.add(translator.run(from, to, text));                 // 执行具体翻译器自身的 run() 方法
    }
    return result;
}
public void attach(Translator translator);
public void detach(Translator translator);
```

- Translator 类：定义了抽象的翻译器类 Translator，并定义了相关的抽象方法和具体方法。
``` java
public String run(Lang from, Lang to, String text) {
    String result = "";
    getParams(from, to, text);
    try {
        result = parses(query());
    } catch (Exception e) {
        e.printStackTrace();
    }
    return result;
}

public abstract void getParams(Lang from, Lang to, String text);    // 获得 HTTP 请求参数
public abstract String query() throws Exception;                    // 发起 HTTP 请求并接收返回结果
public abstract String parses(String text) throws IOException;      // 解析返回结果
```

- Baidu、Youdao、Google、Tencent、Omi、Trycan、Iciba 等类：定义了具体翻译器类，它们均由 Translator 类派生而来，并根据自身不同的请求参数或请求方式等，实现其父类抽象方法。如 Google 类：
``` java
public final class Google extends Translator {
    ...
    @Override
    public void getParams(Lang from, Lang to, String text) {
        params.put("client", "t");
        params.put("sl", langMap.get(from));
        params.put("tl", langMap.get(to));
        ...
        params.put("kc", "0");
        params.put("tk", token(text));                                  // 需要获取 token
        params.put("q", text);
    }

    @Override
    public String query() throws Exception {
        URIBuilder uri = new URIBuilder(url);
        for (String key : params.keySet()) {
            String value = params.get(key);
            uri.addParameter(key, value);
        }
        HttpUriRequest request = new HttpGet(uri.toString());           // 通过 GET 方式进行请求
        CloseableHttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();

        String result = EntityUtils.toString(entity, "UTF-8");

        EntityUtils.consume(entity);
        response.getEntity().getContent().close();
        response.close();

        return result;
    }

    @Override
    public String parses(String text) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(text).get(0).get(0).get(0).toString();
    }
    ...
}
```

# 二、提供的 API

本项目封装了若干接口，并通过 Querier 单例类暴露出来的 **5个** API方法，非常简单易于使用，[详见实例](https://github.com/hujingshuang/MTrans/blob/master/src/test/java/com/swjtu/Test.java):
``` java
public static Querier getQuerier();                              // 获取查询器
public void setParams(Lang from, Lang to, String text);          // 设置查询器参数
public List<String> execute();                                   // 执行查询并返回结果
public void attach(Translator translator);                       // 向查询器中添加翻译器
public void detach(Translator translator);                       // 移除查询器中的翻译器
```

# 三、如何扩展？
本项目支持并提供了主流的在线翻译器服务，通过提供的 API 接口可方便的进行翻译任务。同时，考虑到用户潜在的需求，现介绍如何基于本项目进行扩展，达到二次开发的目的。在扩展本项目之前，用户需对项目源码及 HTTP 知识有一定的了解。

## 1、扩展语种
项目代码中枚举 Lang 定义了大部分常用的语种，若所支持的语种不能满足用户的需求时，用户可自行扩展。
- 确定目标翻译器支持的语种及该语种代号；
- 将所需语种自定义代号添加到枚举中；
- 在翻译器的构造函数中将代号映射添加到 langMap 变量中；

举例：如对 Youdao 翻译器添加 **西班牙语** 的支持：
- 步骤一：通过查询有道翻译服务器所支持的语种列表可知，支持西班牙语种且其代号为:`es`

- 步骤二：添加语种自定义代号
	``` java
	public enum Lang {
		ZH,             // 中文
		EN,             // 英语
		JP,             // 日语
		JPKA,           // 日语假名
		TH,             // 泰语
		FRA,            // 法语
		SPA,            // 西班牙语    <---  添加语种(自定义语种代号)
		KOR             // 韩语
		....
	}
	```
- 步骤三：添加代号映射
	``` java
	public Youdao() {
		super(url);
		langMap.put(Lang.ZH, "zh-CHS");
		langMap.put(Lang.EN, "en");
		langMap.put(Lang.JP, "ja");
		langMap.put(Lang.KOR, "ko");
		langMap.put(Lang.FRA, "fr");
		langMap.put(Lang.RU, "ru");
		langMap.put(Lang.SPA, "es");                  // 添加代号映射
	}
	```

## 2、扩展翻译器
开发者通过继承 Translator 类来定义自己的翻译器类，并实现该类中的如下抽象方法：
``` java
// 用于设置请求参数
public abstract void getParams(Lang from, Lang to, String text);
// 发送 HTTP 请求并接收返回结果(通常为 JSON 或 XML 字符串，根据用户请求结果而定)
public abstract String query() throws Exception;
// 解析字符串，提取翻译结果
public abstract String parses(String text) throws IOException;
```
> 注意：对于某些需要设置 Cookie 的 HTTP 请求，请先获取并设置好 Cookie 再进行请求。通常，在 Chrome 浏览器中按下 F12 键，并在 Console 控制台中输入: document.cookie 即可查看。