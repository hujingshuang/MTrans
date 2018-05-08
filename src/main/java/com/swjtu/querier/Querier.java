package com.swjtu.querier;

import com.swjtu.lang.Lang;
import com.swjtu.trans.Translator;

import java.util.ArrayList;
import java.util.List;

public final class Querier {
    private Lang from;
    private Lang to;
    private String text;

    private List<Translator> translators;

    private static class QuerierHolder {
        public static Querier querier = new Querier();
    }

    public static Querier getQuerier() {
        return QuerierHolder.querier;
    }

    private Querier() {
        translators = new ArrayList<Translator>();
    }

    public List<String> execute() {
        List<String> result = new ArrayList<String>();
        for (Translator translator : translators) {
            result.add(translator.run(from, to, text));
        }
        return result;
    }

    public void setParams(Lang from, Lang to, String text) {
        this.from = from;
        this.to = to;
        this.text = text;
    }

    public void attach(Translator translator){
        translators.add(translator);
    }

    public void detach(Translator translator) {
        translators.remove(translator);
    }
}
