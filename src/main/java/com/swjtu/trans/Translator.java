package com.swjtu.trans;

import com.swjtu.lang.Lang;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class Translator {
    public String url;
    public Map<Lang, String> langMap;
    public CloseableHttpClient httpClient;
    public Map<String, String> params;

    public Translator(String url) {
        this.url = url;
        this.httpClient = HttpClients.createDefault();
        this.params = new HashMap<String, String>();
        this.langMap = new HashMap<Lang, String>();
    }

    public String run(Lang from, Lang to, String text) {
        String result = "Translate failed !";
        getParams(from, to, text);
        try {
            result = parses(query());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public abstract void getParams(Lang from, Lang to, String text);
    public abstract String query() throws Exception;
    public abstract String parses(String text) throws IOException;
}
