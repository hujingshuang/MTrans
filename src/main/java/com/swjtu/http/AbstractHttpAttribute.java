package com.swjtu.http;

import com.swjtu.lang.LANG;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * AbstractHttpAttribute is an abstract base class associated with HTTP.
 *
 * @see com.swjtu.trans.AbstractTranslator
 * @see com.swjtu.tts.AbstractTTS
 */
public abstract class AbstractHttpAttribute {
    public String url;
    public Map<String, String> formData;
    public Map<LANG, String> langMap;
    public CloseableHttpClient httpClient;

    public AbstractHttpAttribute(String url) {
        this.url = url;
        this.formData = new HashMap<>();
        this.langMap = new HashMap<>();
        this.httpClient = HttpClients.createDefault();
    }

    /**
     * Execute the translation or TTS task (send a POST or GET request to the server),
     * receive the result of translation or speech conversion., and return the content
     * or save file name as string.
     *
     * @return the string form of the translated result.
     * @throws Exception if the request fails
     */
    public abstract String query() throws Exception;


    /**
     * The control center includes parameter setting, sending HTTP request, receiving
     * and saving audio data.
     *
     * @param source source language
     * @param text the content to be converted into speech
     * @return the string form of the translated result.
     */
    public abstract String run(LANG source, String text);

    /**
     * The control center includes parameter setting, sending HTTP request, receiving
     * and parsing text data.
     *
     * @param from source language
     * @param to target language
     * @param text the content to be translated
     * @return the string form of the translated result.
     */
    public abstract String run(LANG from, LANG to, String text);

    /**
     * Release and close the resources of HTTP
     * @param httpEntity http entity
     * @param httpResponse http response
     */
    public void close(HttpEntity httpEntity, CloseableHttpResponse httpResponse) {
        try {
            EntityUtils.consume(httpEntity);
            if (null != httpResponse) {
                httpResponse.close();
            }
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Release and close the resources of HTTP
     */
    public void close() {
        try {
            if (null != httpClient) {
                httpClient.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
