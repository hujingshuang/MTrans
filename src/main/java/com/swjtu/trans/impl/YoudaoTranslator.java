package com.swjtu.trans.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swjtu.lang.LANG;
import com.swjtu.trans.AbstractTranslator;
import com.swjtu.util.Util;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public final class YoudaoTranslator extends AbstractTranslator {
    private static final String url = "http://fanyi.youdao.com/translate_o?smartresult=dict&smartresult=rule";

    public YoudaoTranslator(){
        super(url);
    }

    @Override
    public void setLangSupport() {
        langMap.put(LANG.ZH, "zh-CHS");
        langMap.put(LANG.EN, "en");
        langMap.put(LANG.JP, "ja");
        langMap.put(LANG.KOR, "ko");
        langMap.put(LANG.FRA, "fr");
        langMap.put(LANG.RU, "ru");
    }

    @Override
    public void setFormData(LANG from, LANG to, String text) {
        String slat = String.valueOf(System.currentTimeMillis() + (long)(Math.random() * 10 + 1));
        String sign = Util.md5("fanyideskweb" + text + slat + "ebSeFb%=XZ%T[KZ)c(sy!");

        formData.put("i", text);
        formData.put("from", langMap.get(from));
        formData.put("to", langMap.get(to));
        formData.put("smartresult", "dict");
        formData.put("client", "fanyideskweb");
        formData.put("salt", slat);
        formData.put("sign", sign);
        formData.put("doctype", "json");
        formData.put("version", "2.1");
        formData.put("keyfrom", "fanyi.web");
        formData.put("action", "FY_BY_CLICKBUTTION");
        formData.put("typoResult", "false");
    }

    @Override
    public String query() throws Exception {
        HttpPost request = new HttpPost(Util.getUrlWithQueryString(url, formData));

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
