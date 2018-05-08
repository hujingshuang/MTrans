package com.swjtu;

import com.swjtu.lang.Lang;
import com.swjtu.querier.Querier;
import com.swjtu.trans.*;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        Querier querier = Querier.getQuerier();

        querier.setParams(Lang.ZH, Lang.EN, "大家好，我是一个程序员，谢谢大家。");

        querier.attach(new Baidu());
        querier.attach(new Youdao());
        querier.attach(new Google());
        querier.attach(new Tencent());
        querier.attach(new Omi());
        querier.attach(new Trycan());
        querier.attach(new Iciba());

        List<String> result = querier.execute();
        for (String str : result) {
            System.out.println(str);
        }
    }
}

//package com.swjtu;
//
//import com.swjtu.lang.Lang;
//import com.swjtu.querier.Querier;
//import com.swjtu.trans.*;
//
//public class Test {
//    public static void main(String[] args) {
//        Querier querier = Querier.getQuerier();
//        querier.setParams(Lang.ZH, Lang.EN, "如果这都不算爱，我有什么好悲哀，谢谢你的慷慨，都是我活该，喔喔~~");
//        querier.attach(new Baidu());
//        System.out.println(querier.execute());
//    }
//}
