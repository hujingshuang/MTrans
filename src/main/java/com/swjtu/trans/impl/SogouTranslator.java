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

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public final class SogouTranslator extends AbstractTranslator {
    private static final String url = "http://fanyi.sogou.com/reventondc/translate";

    public SogouTranslator() {
        super(url);
    }

    @Override
    public void setLangSupport() {
        langMap.put(LANG.AUTO, "auto");
        langMap.put(LANG.ZH, "zh-CHS");
        langMap.put(LANG.EN, "en");
        langMap.put(LANG.JP, "ja");
        langMap.put(LANG.KOR, "ko");
        langMap.put(LANG.FRA, "fr");
        langMap.put(LANG.RU, "ru");
        langMap.put(LANG.DE, "de");
    }

    @Override
    public void setFormData(LANG from, LANG to, String text) {
        formData.put("from", langMap.get(from));
        formData.put("to", langMap.get(to));
        formData.put("client", "pc");
        formData.put("fr", "browser_pc");
        formData.put("text", text);
        formData.put("useDetect", "on");

        // 自动检测语种
        if (from == LANG.AUTO) {
            formData.put("useDetectResult", "on");
        } else {
            formData.put("useDetectResult", "off");
        }

        formData.put("needQc", "1");
        formData.put("uuid", token());
        formData.put("oxford", "on");
        formData.put("isReturnSugg", "off");
    }

    @Override
    public String query() throws Exception {
        HttpPost request = new HttpPost(Util.getUrlWithQueryString(url, formData));

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
        return mapper.readTree(text).path("translate").findPath("dit").toString();
    }

    private String token() {
        String result = "";
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("js");
        try {
            FileReader reader = new FileReader("./tk/Sogou.js");
            engine.eval(reader);
            if (engine instanceof Invocable) {
                Invocable invoke = (Invocable)engine;
                result = String.valueOf(invoke.invokeFunction("token"));
            }
        } catch (ScriptException | NoSuchMethodException | FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }
}
