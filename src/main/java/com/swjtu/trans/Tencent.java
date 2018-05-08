package com.swjtu.trans;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swjtu.lang.Lang;
import com.swjtu.util.Util;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public final class Tencent extends Translator {
    private static final String url = "http://fanyi.qq.com/api/translate";

    public Tencent(){
        super(url);
        langMap.put(Lang.ZH, "zh");
        langMap.put(Lang.EN, "en");
        langMap.put(Lang.JP, "jp");
        langMap.put(Lang.KOR, "kr");
        langMap.put(Lang.FRA, "fr");
        langMap.put(Lang.RU, "ru");
        langMap.put(Lang.DE, "de");
    }

    @Override
    public void getParams(Lang from, Lang to, String text) {
        params.put("source", langMap.get(from));
        params.put("target", langMap.get(to));
        params.put("sourceText", text);
        params.put("sessionUuid", "translate_uuid" + String.valueOf(System.currentTimeMillis()));
    }

    @Override
    public String query() throws Exception {
        HttpPost request = new HttpPost(url);
        request.setEntity(new UrlEncodedFormEntity(Util.map2list(params), "UTF-8"));

        request.setHeader("Cookie", "fy_guid=d4480e20-1644-4a47-a98d-787cfa244fd2; qtv=efe677fe5967cd30; qtk=DSPFrzGwLjqdXjye/AizsoVJTyWyqsqHjqeHvMiT0Mvxx/lsHJyAiGBLUpXH6DSsgtK1Dm2M2LqCZljLJQZV0KYx9R1X5XKa7SMEeR4rK8VSS+Y/bxxEtibAI+aq48/o1zlCX0s/fdtwb5aef4y3IA==;");
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
