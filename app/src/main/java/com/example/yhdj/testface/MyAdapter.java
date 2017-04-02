package com.example.yhdj.testface;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by yhdj on 2017/4/2.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private FaceRecognizeTwoBean mResultsBean;
    public MyAdapter(FaceRecognizeTwoBean mResultsBean){
        this.mResultsBean = mResultsBean;
    }
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.face_recognize_second,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        FaceRecognizeTwoBean.ResultsBean resultbean = mResultsBean.getResults().get(position);
        holder.index_i.setText(resultbean.getIndex_i());
        holder.index_y.setText(resultbean.getIndex_j());
        holder.score.setText(String.valueOf(resultbean.getScore()));
    }

    @Override
    public int getItemCount() {
        return mResultsBean.getResults().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView index_i;
        private TextView index_y;
        private TextView score;
        public ViewHolder(View itemView) {
            super(itemView);
            index_i = (TextView) itemView.findViewById(R.id.index_i);
            index_y = (TextView) itemView.findViewById(R.id.index_y);
            score = (TextView) itemView.findViewById(R.id.index_score);
        }
    }
}
