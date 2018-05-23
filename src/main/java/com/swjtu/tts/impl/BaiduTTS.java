package com.swjtu.tts.impl;

import com.swjtu.lang.LANG;
import com.swjtu.tts.AbstractTTS;

public final class BaiduTTS extends AbstractTTS {
    private final static String url = "http://fanyi.baidu.com/gettts";
    private final static int spd = 3;

    public BaiduTTS() {
        super(url);
        setLangSupport();
    }

    @Override
    public void setLangSupport() {
        langMap.put(LANG.ZH, "zh");
        langMap.put(LANG.EN, "en");
        langMap.put(LANG.JP, "jp");
        langMap.put(LANG.KOR, "kor");
        langMap.put(LANG.FRA, "fra");
        langMap.put(LANG.RU, "ru");
        langMap.put(LANG.DE, "de");
        langMap.put(LANG.TH, "th");
    }

    @Override
    public void setFormData(LANG source, String text) {
        formData.put("lan", langMap.get(source));
        formData.put("text", text);
        formData.put("spd", String.valueOf(spd));
        formData.put("source", "web");
    }
}
