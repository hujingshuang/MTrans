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

public final class Omi extends Translator {
    private static final String url = "http://www.omifanyi.com/transSents.do";

    public Omi(){
        super(url);
        langMap.put(Lang.ZH, "c");
        langMap.put(Lang.EN, "e");
    }

    @Override
    public void getParams(Lang from, Lang to, String text) {
        params.put("languageType", langMap.get(from) + "2" + langMap.get(to));
        params.put("sentsToTrans", text);
    }

    @Override
    public String query() throws Exception {
        HttpPost request = new HttpPost(Util.getUrlWithQueryString(url, params));
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
        return mapper.readTree(text).path("sentsResults").get(1).get(0).toString();
    }
}
