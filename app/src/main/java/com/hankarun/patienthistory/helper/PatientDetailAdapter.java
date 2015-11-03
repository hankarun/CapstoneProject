package com.hankarun.patienthistory.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.model.Answer;

import java.util.ArrayList;

public class PatientDetailAdapter extends RecyclerView.Adapter<PatientDetailAdapter.ViewHolder> {
    ArrayList<Answer> mAnswerList;
    Context mContext;

    public PatientDetailAdapter(Context contex, ArrayList<Answer> answerArrayList) {
        mAnswerList = answerArrayList;
        mContext = contex;
    }

    @Override
    public PatientDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Answer answer = mAnswerList.get(position);

        holder.numberText.setText(position+"");
        holder.questionText.setText(answer.getmQuestion());
        holder.answerText.setText(answer.getmAnswer() ? "Evet" : "Hayır");

        if (answer.getmAnswer()) {
            holder.answerText.setTextColor(mContext.getResources().getColor(android.R.color.holo_green_light));
            holder.mLinearLayout.setBackgroundColor(mContext.getResources().getColor(android.R.color.darker_gray));
        } else {
            holder.answerText.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_light));
            holder.mLinearLayout.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return mAnswerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mLinearLayout;
        public TextView numberText;
        public TextView questionText;
        public TextView answerText;


        public ViewHolder(View v) {
            super(v);
            mLinearLayout = (LinearLayout) v.findViewById(R.id.answerLayout);
            numberText = (TextView) v.findViewById(R.id.numberTextView);
            questionText = (TextView) v.findViewById(R.id.questionTextView);
            answerText = (TextView) v.findViewById(R.id.answerTextView);
        }
    }
}
