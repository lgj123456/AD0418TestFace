package com.example.yhdj.testface;

import java.util.List;

/**
 * Created by yhdj on 2017/4/2.
 */

public class FaceverifyUserBean {
    /**
     * result_num : 1
     * results : [47.617233276367]
     * log_id : 103682599
     */

    private int result_num;
    private long log_id;
    private List<Double> results;

    public int getResult_num() {
        return result_num;
    }

    public void setResult_num(int result_num) {
        this.result_num = result_num;
    }

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
    }

    public List<Double> getResults() {
        return results;
    }

    public void setResults(List<Double> results) {
        this.results = results;
    }
}
