package com.swjtu.trans;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swjtu.lang.Lang;
import com.swjtu.util.Util;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public final class Youdao extends Translator {
    private static final String url = "http://fanyi.youdao.com/translate_o?smartresult=dict&smartresult=rule";

    public Youdao(){
        super(url);
        langMap.put(Lang.ZH, "zh-CHS");
        langMap.put(Lang.EN, "en");
        langMap.put(Lang.JP, "ja");
        langMap.put(Lang.KOR, "ko");
        langMap.put(Lang.FRA, "fr");
        langMap.put(Lang.RU, "ru");
    }

    @Override
    public void getParams(Lang from, Lang to, String text) {
        String slat = String.valueOf(System.currentTimeMillis() + (long)(Math.random() * 10 + 1));
        String sign = Util.md5("fanyideskweb" + text + slat + "ebSeFb%=XZ%T[KZ)c(sy!");

        params.put("i", text);
        params.put("from", langMap.get(from));
        params.put("to", langMap.get(to));
        params.put("smartresult", "dict");
        params.put("client", "fanyideskweb");
        params.put("salt", slat);
        params.put("sign", sign);
        params.put("doctype", "json");
        params.put("version", "2.1");
        params.put("keyfrom", "fanyi.web");
        params.put("action", "FY_BY_CLICKBUTTION");
        params.put("typoResult", "false");
    }

    @Override
    public String query() throws Exception {
        HttpPost request = new HttpPost(Util.getUrlWithQueryString(url, params));

        request.setHeader("Cookie","OUTFOX_SEARCH_USER_ID=1799185238@10.169.0.83;");
        request.setHeader("Referer","http://fanyi.youdao.com/");
        request.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");

        CloseableHttpResponse httpResponse = httpClient.execute(request);
        HttpEntity httpEntity = httpResponse.getEntity();
        String result = EntityUtils.toString(httpEntity, "UTF-8");
        EntityUtils.consume(httpEntity);
        httpResponse.close();

        return result;
    }

    @Override
    public String parses(String text) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return  mapper.readTree(text).path("translateResult").findPath("tgt").toString();
    }
}
