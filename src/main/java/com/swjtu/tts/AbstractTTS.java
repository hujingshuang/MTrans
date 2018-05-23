package com.swjtu.tts;

import com.swjtu.http.AbstractHttpAttribute;
import com.swjtu.http.HttpParams;
import com.swjtu.lang.LANG;
import com.swjtu.util.Util;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.io.*;

/**
 * AbstractTTS is an abstract base class for all TTS,
 * including several (abstract) functions. By setting
 * parameters, the request is sent to the target server,
 * and the return is converted to the result of the speech.
 *
 * @see com.swjtu.tts.impl.BaiduTTS
 * @see com.swjtu.tts.impl.GoogleTTS
 * @see com.swjtu.tts.impl.SogouTTS
 * @see com.swjtu.tts.impl.TencentTTS
 * @see com.swjtu.tts.impl.YoudaoTTS
 */
public abstract class AbstractTTS extends AbstractHttpAttribute implements HttpParams{

    public AbstractTTS(String url) {
        super(url);
        setLangSupport();
    }

    @Override
    public String run(LANG from, LANG to, String text) {
        return null;
    }

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

    /**
     * Initialize the supported language mapping.
     */
    public abstract void setLangSupport();

    @Override
    public String query() throws IOException {
        String uri = Util.getUrlWithQueryString(url, formData);
        HttpGet request = new HttpGet(uri);

        HttpResponse response = httpClient.execute(request);
        InputStream is = response.getEntity().getContent();

        // 将 TTS 结果保存为 mp3 音频文件，以待转换文本的 md5 码作为部分文件名
        StringBuilder saveFile = new StringBuilder();
        saveFile.append("./tts/")
                .append(this.getClass().getName())
                .append("-")
                .append(Util.md5(uri))
                .append(".mp3");

        File file = new File(saveFile.toString());
        FileOutputStream fos = new FileOutputStream(file);
        byte[] buffer = new byte[4096];
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
        }
        fos.close();
        is.close();

        return saveFile.toString();
    }

    @Override
    public abstract void setFormData(LANG source, String text );

    @Override
    public void setFormData(LANG from, LANG to, String text) {}
}
