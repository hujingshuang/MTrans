package com.swjtu.trans.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swjtu.lang.LANG;
import com.swjtu.trans.AbstractTranslator;
import com.swjtu.util.Util;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public final class TencentTranslator extends AbstractTranslator {
    private static final String url = "https://fanyi.qq.com/api/translate";

    public TencentTranslator(){
        super(url);
    }

    @Override
    public void setLangSupport() {
        langMap.put(LANG.ZH, "zh");
        langMap.put(LANG.EN, "en");
        langMap.put(LANG.JP, "jp");
        langMap.put(LANG.KOR, "kr");
        langMap.put(LANG.FRA, "fr");
        langMap.put(LANG.RU, "ru");
        langMap.put(LANG.DE, "de");
    }

    @Override
    public void setFormData(LANG from, LANG to, String text) {
        formData.put("source", langMap.get(from));
        formData.put("target", langMap.get(to));
        formData.put("sourceText", text);
        formData.put("sessionUuid", "translate_uuid" + String.valueOf(System.currentTimeMillis()));
    }

    @Override
    public String query() throws Exception {
        HttpPost request = new HttpPost(url);
        request.setEntity(new UrlEncodedFormEntity(Util.map2list(formData), "UTF-8"));

        request.setHeader("Cookie", "fy_guid=d4480e20-1644-4a47-a98d-787cfa244fd2; qtv=bbbc7118b32d7a9a; qtk=DTmfpOAn6b6HWTGtjW7w5a/FOommFjJPAre3GpaRUzPCQSaqY3gOSzKYEFyRYwKnjUN3M9D0V59LVNGDKchtj+RBld2oqSAVvEaAQVVLApTHDB52kdQYQYKAsa2NLnl4lIUbr6pYKN5469mS5hjcmQ==;");
        request.setHeader("Origin", "http://fanyi.qq.com");

        CloseableHttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();

        String result = EntityUtils.toString(entity, "utf-8");

        EntityUtils.consume(entity);
        response.getEntity().getContent().close();
        response.close();

        return result;
    }

    @Override
    public String parses(String text) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return  mapper.readTree(text).path("translate").findPath("targetText").toString();
    }
}
