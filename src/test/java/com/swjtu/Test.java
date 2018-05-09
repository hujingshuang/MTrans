package com.swjtu;

import com.swjtu.lang.Lang;
import com.swjtu.querier.Querier;
import com.swjtu.trans.*;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        Querier querier = Querier.getQuerier();

        querier.setParams(Lang.EN, Lang.ZH, "Happiness is a way station between too much and too little.");

        querier.attach(new Baidu());
        querier.attach(new Youdao());
        querier.attach(new Google());
        querier.attach(new Tencent());
        querier.attach(new Omi());
        querier.attach(new Trycan());
        querier.attach(new Iciba());
        querier.attach(new Sogou());

        List<String> result = querier.execute();
        for (String str : result) {
            System.out.println(str);
        }
    }
}
