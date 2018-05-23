package com.swjtu;

import com.swjtu.lang.LANG;
import com.swjtu.querier.Querier;
import com.swjtu.trans.AbstractTranslator;
import com.swjtu.trans.impl.*;
import com.swjtu.tts.AbstractTTS;
import com.swjtu.tts.impl.*;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        // TTS
        Querier<AbstractTTS> querierTTS = new Querier<>();
        querierTTS.setParams(LANG.EN, "To be or not to be, that is a question.");

        querierTTS.attach(new BaiduTTS());
        querierTTS.attach(new YoudaoTTS());
        querierTTS.attach(new GoogleTTS());
        querierTTS.attach(new TencentTTS());
        querierTTS.attach(new SogouTTS());

        List<String> resultTTS = querierTTS.execute();
        for (String str : resultTTS) {
            System.out.println(str);
        }

        // translator
        Querier<AbstractTranslator> querierTrans = new Querier<>();
        querierTrans.setParams(LANG.EN, LANG.ZH,"Happiness is a way station between too much and too little.");

        querierTrans.attach(new BaiduTranslator());
        querierTrans.attach(new YoudaoTranslator());
        querierTrans.attach(new GoogleTranslator());
        querierTrans.attach(new TencentTranslator());
        querierTrans.attach(new OmiTranslator());
        querierTrans.attach(new TrycanTranslator());
        querierTrans.attach(new IcibaTranslator());
        querierTrans.attach(new SogouTranslator());

        List<String> resultTrans = querierTrans.execute();
        for (String str : resultTrans) {
            System.out.println(str);
        }
    }
}
