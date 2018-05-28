# MTrans 项目介绍

本项目 [**多源翻译**](https://github.com/hujingshuang/MTrans) (Multi-source Translation, MTrans)，提供了集多种主流的 **在线翻译** 及 **TTS** 功能于一身的轻量级服务。通过程序向所支持的在线目标服务器发送 HTTP 请求，获取并解析返回的结果，为使用者提供便利。目前，本项目**免费开源**，开发者可基于此进行二次开发。

目前支持 **源** 及 **语种** 如下:

| 翻译源 | 服务器地址 | 支持语种 | 方式
| :-: | :-: | :-: | :-: |
| [百度翻译](http://fanyi.baidu.com/) | http://fanyi.baidu.com/v2transapi | 中文、英语、日语、韩语、法语、俄语、德语 | 互译
| [有道翻译](http://fanyi.youdao.com/) | http://fanyi.youdao.com/translate_o | 中文、英语、日语、韩语、法语、俄语 | 互译
| [谷歌翻译](http://translate.google.cn/) | https://translate.google.cn/translate_a/single | 中文、英语、日语、韩语、法语、俄语、德语 | 互译
| [腾讯翻译君](http://fanyi.qq.com/) | http://fanyi.qq.com/api/translate | 中文、英语、日语、韩语、法语、俄语、德语 | 互译
| [欧米翻译](http://www.omifanyi.com/) | http://www.omifanyi.com/transSents.do | 中文、英语 | 互译
| [TryCan](http://www.trycan.com/) | http://fanyi.trycan.com/Transfer.do | 中文、英语 | 互译
| [金山爱词霸](http://fy.iciba.com/) | http://fy.iciba.com/ajax.php?a=fy | 中文、英语、日语、韩语、法语、德语 | 互译
| [搜狗翻译](http://fanyi.sogou.com/) | http://fanyi.sogou.com/reventondc/translate | 中文、英语、日语、韩语、法语、俄语、德语 | 互译



| TTS 源  | 服务器地址 | 支持语种
| :-: | :-: | :-: |
| [百度 TTS](http://fanyi.baidu.com/) | http://fanyi.baidu.com/gettts | 中文、英语、日语、韩语、法语、俄语、德语、泰语
| [有道 TTS](http://fanyi.youdao.com/) | http://tts.youdao.com/fanyivoice | 英语、日语、韩语、法语
| [谷歌 TTS](http://translate.google.cn/) | https://translate.google.cn/translate_tts | 中文、英语、日语、韩语、法语、俄语、德语
| [腾讯 TTS](http://fanyi.qq.com/) | http://audiodetect.browser.qq.com:8080/tts | 中文、英语、日语、韩语
| [搜狗 TTS](http://fanyi.sogou.com/) | http://fanyi.sogou.com/reventondc/synthesis | 中文、英语



# 一、快速开始
## 1、环境配置
本项目使用 **IDEA + Maven** 进行开发，请在 *pom.xml* 中添加如下依赖。
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


## 2、最小实例
- 最小翻译实例
    ``` java
    import com.swjtu.lang.LANG;
    import com.swjtu.querier.Querier;
    import com.swjtu.trans.AbstractTranslator;
    import com.swjtu.trans.impl.GoogleTranslator;

    import java.util.List;

    public class Test {
        public static void main(String[] args) {
            Querier<AbstractTranslator> querierTrans = new Querier<>();                   // 获取查询器

            querierTrans.setParams(LANG.ZH, LANG.EN, "如果这都不算爱，我有什么好悲哀！");    // 设置参数
            
            querierTrans.attach(new GoogleTranslator());                                  // 向查询器中添加 Google 翻译器

            List<String> result = querierTrans.execute();                                 // 执行查询并接收查询结果

            for (String str : result) {
                System.out.println(str);
            }
        }
    }
    ```
- 最小 TTS 实例
    ``` java
    import com.swjtu.lang.LANG;
    import com.swjtu.querier.Querier;
    import com.swjtu.tts.AbstractTTS;
    import com.swjtu.tts.impl.BaiduTTS;

    import java.util.List;

    public class Test {
        public static void main(String[] args) {
            Querier<AbstractTTS> querierTTS = new Querier<>();                          // 获取查询器

            querierTTS.setParams(LANG.EN, "To be or not to be, that is a question.");   // 设置参数
            
            querierTTS.attach(new BaiduTTS());                                          // 向查询器中添加 Google 翻译器

            List<String> result = querierTTS.execute();                                 // 执行查询并接收查询结果

            for (String str : result) {
                System.out.println(str);
            }
        }
    }
    ```


# 二、MTrans 使用说明
## 1、包/类 一览表
本项目中主要定义了如下几个包，其命名及作用如下表：

| 包名 | 包含类 | 说明
| :- | :-: | :-: |
| [com.swjtu.lang](https://github.com/hujingshuang/MTrans/tree/master/src/main/java/com/swjtu/lang) | [LANG](https://github.com/hujingshuang/MTrans/blob/master/src%2Fmain%2Fjava%2Fcom%2Fswjtu%2Flang%2FLang.java) | 枚举类型，支持的语种列表
| [com.swjtu.util](https://github.com/hujingshuang/MTrans/tree/master/src/main/java/com/swjtu/util) | [Util](https://github.com/hujingshuang/MTrans/blob/master/src%2Fmain%2Fjava%2Fcom%2Fswjtu%2Futil%2FUtil.java) | 工具包
| [com.swjtu.http](https://github.com/hujingshuang/MTrans/tree/master/src/main/java/com/swjtu/http) | [HttpParams](https://github.com/hujingshuang/MTrans/blob/master/src/main/java/com/swjtu/http/HttpParams.java)、[AbstractHttpAttribute](https://github.com/hujingshuang/MTrans/blob/master/src/main/java/com/swjtu/http/AbstractHttpAttribute.java) | HTTP 方法接口及抽象类
| [com.swjtu.querier](https://github.com/hujingshuang/MTrans/tree/master/src/main/java/com/swjtu/querier) | [Querier](https://github.com/hujingshuang/MTrans/blob/master/src/main/java/com/swjtu/querier/Querier.java) | 泛型，查询器
| [com.swjtu.trans](https://github.com/hujingshuang/MTrans/tree/master/src/main/java/com/swjtu/trans) | [AbstractTranslator](https://github.com/hujingshuang/MTrans/blob/master/src/main/java/com/swjtu/trans/AbstractTranslator.java) | 翻译器(抽象)类
| [com.swjtu.trans.impl](https://github.com/hujingshuang/MTrans/tree/master/src/main/java/com/swjtu/trans/impl) | [BaiduTranslator](https://github.com/hujingshuang/MTrans/blob/master/src/main/java/com/swjtu/trans/impl/BaiduTranslator.java)、[GoogleTranslator](https://github.com/hujingshuang/MTrans/blob/master/src/main/java/com/swjtu/trans/impl/GoogleTranslator.java)、[YoudaoTranslator](https://github.com/hujingshuang/MTrans/blob/master/src/main/java/com/swjtu/trans/impl/YoudaoTranslator.java)、[IcibaTranslator](https://github.com/hujingshuang/MTrans/blob/master/src/main/java/com/swjtu/trans/impl/IcibaTranslator.java)、<br>[OmiTranslator](https://github.com/hujingshuang/MTrans/blob/master/src/main/java/com/swjtu/trans/impl/OmiTranslator.java)、[SogouTranslator](https://github.com/hujingshuang/MTrans/blob/master/src/main/java/com/swjtu/trans/impl/SogouTranslator.java)、[TencentTranslator](https://github.com/hujingshuang/MTrans/blob/master/src/main/java/com/swjtu/trans/impl/TencentTranslator.java)、[TrycanTranslator](https://github.com/hujingshuang/MTrans/blob/master/src/main/java/com/swjtu/trans/impl/TrycanTranslator.java) | 翻译器实体类
| [com.swjtu.tts](https://github.com/hujingshuang/MTrans/tree/master/src/main/java/com/swjtu/tts) | [AbstractTTS](https://github.com/hujingshuang/MTrans/blob/master/src/main/java/com/swjtu/tts/AbstractTTS.java) | TTS 抽象类
| [com.swjtu.tts.impl](https://github.com/hujingshuang/MTrans/tree/master/src/main/java/com/swjtu/tts/impl) | [BaiduTTS](https://github.com/hujingshuang/MTrans/blob/master/src/main/java/com/swjtu/tts/impl/BaiduTTS.java)、[YoudaoTTS](https://github.com/hujingshuang/MTrans/blob/master/src/main/java/com/swjtu/tts/impl/YoudaoTTS.java)、[GoogleTTS](https://github.com/hujingshuang/MTrans/blob/master/src/main/java/com/swjtu/tts/impl/GoogleTTS.java)、[TencentTTS](https://github.com/hujingshuang/MTrans/blob/master/src/main/java/com/swjtu/tts/impl/TencentTTS.java)、[SogouTTS](https://github.com/hujingshuang/MTrans/blob/master/src/main/java/com/swjtu/tts/impl/SogouTTS.java) | TTS 实体类


## 2、类图

- [项目结构](https://github.com/hujingshuang/MTrans/tree/master/src/main/java/com/swjtu)

    ![总览](https://github.com/hujingshuang/MTrans/blob/master/source%2Ftest.png)

- [com.swjtu.http](https://github.com/hujingshuang/MTrans/tree/master/src/main/java/com/swjtu/http) 包 / 类图

    ![com.swjtu.http](https://github.com/hujingshuang/MTrans/blob/master/source%2Fhttp.png)

- [com.swjtu.querier](https://github.com/hujingshuang/MTrans/tree/master/src/main/java/com/swjtu/querier) 包 / 类图

    ![com.swjtu.querier](https://github.com/hujingshuang/MTrans/blob/master/source%2Fquerier.png)

- [com.swjtu.trans](https://github.com/hujingshuang/MTrans/tree/master/src/main/java/com/swjtu/trans) 包 / 类图

    ![com.swjtu.trans](https://github.com/hujingshuang/MTrans/blob/master/source%2Ftrans.png)

- [com.swjtu.trans.impl](https://github.com/hujingshuang/MTrans/tree/master/src/main/java/com/swjtu/trans/impl) 包 / 类图
    
    ![com.swjtu.trans.impl](https://github.com/hujingshuang/MTrans/blob/master/source%2Ftrans.impl.png)

- [com.swjtu.tts](https://github.com/hujingshuang/MTrans/tree/master/src/main/java/com/swjtu/tts) 包 / 类图
    
    ![com.swjtu.tts](https://github.com/hujingshuang/MTrans/blob/master/source%2Ftts.png)

- [com.swjtu.tts.impl](https://github.com/hujingshuang/MTrans/tree/master/src/main/java/com/swjtu/tts/impl) 包 / 类图

    ![com.swjtu.tts.impl](https://github.com/hujingshuang/MTrans/blob/master/source%2Ftts.impl.png)

- [com.swjtu.util](https://github.com/hujingshuang/MTrans/tree/master/src/main/java/com/swjtu/util) 包 / 类图

    ![com.swjtu.util](https://github.com/hujingshuang/MTrans/blob/master/source%2Futil.png)


## 3、类说明
- `LANG` 枚举：定义所支持或将支持的语种，统一并规范了语种列表。
    ``` java
    public enum LANG {
        ZH,             // 中文
        EN,             // 英语
        JP,             // 日语
        JPKA,           // 日语假名
        TH,             // 泰语
        ...
    }
    ```

- `Util` 类：包含并实现了一些实用方法。
    ``` java
    public static List<NameValuePair> map2list(Map<String, String> mapParams);              // 将 Map 转换成 List
    public static String getUrlWithQueryString(String url, Map<String, String> params);     // 生成 URL

    // 各种格式的 MD5
    public static String md5(String input);
    public static String md5(File file);
    public static String md5(InputStream in);
    ```

- `Querier` 类：定义了 `Querier` 类，使用了观察者模式。该类包含了一个集合，集合中的元素为翻译器类 或 TTS 类，通过 `setParams()` 设定好参数后，执行 `execute()` 方法发送请求，同时返回结果。可以通过 `attach()` 和 `detach()` 方法向集合中添加或移除元素。
    ``` java
    public final class Querier<T extends AbstractHttpAttribute> {
        private List<T> collection;                               // 集合

        ...

        public void setParams(LANG source, String text);          // TTS 参数设置， source 源语种，text 待转换为语音的内容
        public void setParams(LANG from, LANG to, String text);   // 翻译器参数设置，from 源语种，to 目标语种，text 待翻译内容
        
        public List<String> execute() {
            List<String> result = new ArrayList<String>();

            for (T element : collection) {
                if (element.getClass().getName().contains("Translator")) {
                    result.add(element.run(from, to, text));
                } else if (element.getClass().getName().contains("TTS")) {
                    result.add(element.run(from, text));
                }
            }
            return result;
        }

        public void attach(T element);
        public void detach(T element);

        ...
    }
    ```
- `HttpParams` 接口：定义了设置 HTTP 数据格式的接口方法
    ``` java
    public interface HttpParams {
        public void setFormData(LANG source, String text);           // 设置 TTS 参数的接口方法
        public void setFormData(LANG from, LANG to, String text);    // 设置翻译器参数的接口方法
    }
    ```

- `AbstractHttpAttribute` 类：与 HTTP 请求相关的请求及控制流程
    ``` java
    public abstract String query() throws Exception;
    public abstract String run(LANG source, String text);
    public abstract String run(LANG from, LANG to, String text);

    // 资源释放
    public void close(HttpEntity httpEntity, CloseableHttpResponse httpResponse);
    public void close();
    ```

- `AbstractTranslator` 类：继承自 `AbstractHttpAttribute` 类，并实现了 `HttpParams` 接口，定义了抽象的翻译器类。
    ``` java
    @Override
    public String run(LANG from, LANG to, String text) {
        String result = "";
        setFormData(from, to, text);
        try {
            result = parses(query());
        } catch (Exception e) {
            e.printStackTrace();
        }
        close();
        return result;
    }

    public abstract void setLangSupport();                              // 设置支持的语种
    public abstract String parses(String text) throws IOException;      // 解析返回结果
    ```
- `AbstractTTS` 类：继承自 `AbstractHttpAttribute` 类，并实现了 `HttpParams` 接口，定义了抽象的 TTS 类。
    ``` java
    @Override
    public String run(LANG source, String text) {
        String saveFile = null;
        setFormData(source, text);
        try {
            saveFile = query();
            System.out.println(saveFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        close();
        return saveFile;
    }

    public String query() throws IOException {
        ...

        // 将 TTS 结果保存为 mp3 音频文件，以待转换文本的 md5 码作为部分文件名
        StringBuilder saveFile = new StringBuilder();
        saveFile.append("./tts/")
                .append(this.getClass().getName())
                .append("-")
                .append(Util.md5(uri))
                .append(".mp3");
        
        ...
    }
    ```

# 三、提供的 API

本项目封装了若干方法，并通过 `Querier` 泛型类暴露出来的 **5个 API** 方法，非常简单易于使用，[详见实例](https://github.com/hujingshuang/MTrans/blob/master/src/test/java/com/swjtu/Test.java):
``` java
// 设置查询器参数
public void setParams(LANG source, String text);
public void setParams(LANG from, LANG to, String text);

public List<String> execute();                       // 执行查询并返回结果
public void attach(T element);                       // 向查询器中添加元素
public void detach(T element);                       // 移除查询器中的元素
```

# 四、如何扩展？
本项目支持并提供了主流的 **在线翻译** 及 **TTS** 服务，通过提供的 API 接口可方便的进行相关任务。同时，考虑到用户潜在的需求，现介绍如何基于本项目进行扩展，达到二次开发的目的。在扩展本项目之前，用户需对项目源码及 HTTP 知识有一定的了解。

## 1、扩展语种
项目代码中枚举 `LANG` 定义了大部分常用的语种，若所支持的语种不能满足用户的需求时，用户可自行扩展。
- 确定目标服务器支持的语种及该语种代号；
- 将所需语种自定义代号添加到枚举中；
- 在实体类 `setLangSupport()` 方法中，将代号映射添加到 `langMap` 变量中；

举例：如对 *Youdao* 翻译器添加 **西班牙语** 的支持：
- 步骤一：通过查询有道翻译服务器所支持的语种列表可知，支持西班牙语种且其代号为:`es`

- 步骤二：在 `LANG` 中，添加语种自定义代号：
	``` java
	public enum LANG {
		ZH,             // 中文
		EN,             // 英语
		JP,             // 日语
		JPKA,           // 日语假名
		TH,             // 泰语
		FRA,            // 法语
		SPA,            // 西班牙语    <---  添加语种(自定义语种代号)
		KOR,            // 韩语
		....
	}
	```
- 步骤三：在 `YoudaoTranslator` 类中，添加代号映射：
	``` java
    @Override
    public void setLangSupport() {
        langMap.put(LANG.ZH, "zh-CHS");
        langMap.put(LANG.EN, "en");
        langMap.put(LANG.JP, "ja");
        langMap.put(LANG.KOR, "ko");
        langMap.put(LANG.FRA, "fr");
        langMap.put(LANG.RU, "ru");
        langMap.put(LANG.SPA, "es");                  // 添加代号映射
    }
	```

## 2、扩展翻译器
开发者通过继承 `AbstractTranslator` 类来定义自己的翻译器类，并实现该类中的如下抽象方法：
``` java
// 添加语种支持
public abstract void setLangSupport();
// 用于设置请求参数
public abstract void setFormData(LANG from, LANG to, String text);
// 发送 HTTP 请求并接收返回结果(通常为 JSON 或 XML 字符串，根据用户请求结果而定)
public abstract String query() throws Exception;
// 解析字符串，提取翻译结果
public abstract String parses(String text) throws IOException;
```
> 注意：对于某些需要设置 Cookie 的 HTTP 请求，请先获取并设置好 Cookie 再进行请求。通常，在 Chrome 浏览器中按下 F12 键，并在 Console 控制台中输入: `document.cookie` 即可查看。

## 3、扩展 TTS
开发者通过继承 `AbstractTTS` 类来定义自己的 TTS 类，并实现该类中的如下抽象方法：
``` java
// 添加语种支持
public abstract void setLangSupport();
// 用于设置请求参数
public abstract void setFormData(LANG source, String text);
// 发送 HTTP 请求并接收返回结果(通常为 JSON 或 XML 字符串，根据用户请求结果而定)
public abstract String query() throws Exception;
```
> 注意：TTS 保存路径默认为：./tts/类名-md5(待转换内容).mp3`(如：com.swjtu.tts.impl.GoogleTTS-5757a2c16ce52b5427eb12f961d6362e.mp3)`