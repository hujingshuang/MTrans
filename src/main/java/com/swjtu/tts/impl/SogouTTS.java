package com.swjtu.tts.impl;

import com.swjtu.lang.LANG;
import com.swjtu.tts.AbstractTTS;

public final class SogouTTS extends AbstractTTS {
    // 中、英文
    private final static String url1 = "http://fanyi.sogou.com/reventondc/synthesis";

    // 其他语种
    private final static String url2 = "http://fanyi.sogou.com/reventondc/microsoftGetSpeakFile";

    // 语速
    private final static int speed = 1;

    public SogouTTS() {
        super(url1);
        setLangSupport();
    }

    @Override
    public void setLangSupport() {
        langMap.put(LANG.ZH, "zh-CHS");
        langMap.put(LANG.EN, "en");
    }

    @Override
    public void setFormData(LANG source, String text) {
        formData.put("text", text);
        formData.put("speed", String.valueOf(speed));
        formData.put("lang", langMap.get(source));
        formData.put("from", "translateweb");
    }
}
