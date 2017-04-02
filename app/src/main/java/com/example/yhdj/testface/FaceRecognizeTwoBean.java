package com.example.yhdj.testface;

import java.util.List;

/**
 * Created by yhdj on 2017/4/2.
 */

public class FaceRecognizeTwoBean {

    /**
     * result_num : 1
     * results : [{"index_i":"0","index_j":"1","score":43.517723083496}]
     * log_id : 163400824
     */

    private long result_num;
    private long log_id;
    /**
     * index_i : 0
     * index_j : 1
     * score : 43.517723083496
     */

    private List<ResultsBean> results;

    public long getResult_num() {
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

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        private String index_i;
        private String index_j;
        private double score;

        public String getIndex_i() {
            return index_i;
        }

        public void setIndex_i(String index_i) {
            this.index_i = index_i;
        }

        public String getIndex_j() {
            return index_j;
        }

        public void setIndex_j(String index_j) {
            this.index_j = index_j;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }
    }
}
