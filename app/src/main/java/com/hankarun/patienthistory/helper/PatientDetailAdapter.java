package com.hankarun.patienthistory.helper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.model.Answer;

import java.util.ArrayList;

public class PatientDetailAdapter extends RecyclerView.Adapter<PatientDetailAdapter.ViewHolder> {
    private ArrayList<Answer> mAnswerList;
    private Context mContext;

    public PatientDetailAdapter(Context contex, ArrayList<Answer> answerArrayList) {
        mAnswerList = answerArrayList;
        mContext = contex;
    }

    @Override
    public PatientDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(v);
    }

    public boolean isHeader(int position) {
        return mAnswerList.get(position).getmId() == -1;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Answer answer = mAnswerList.get(position);

        if (isHeader(position)) {
            holder.questionText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 4));
            holder.questionText.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            holder.questionText.setText(answer.getmQuestionGroup());
            holder.questionText.setTextColor(mContext.getResources().getColor(android.R.color.white));
            holder.questionText.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
            holder.answerText.setVisibility(View.GONE);
            holder.numberText.setVisibility(View.GONE);
        } else {
            holder.questionText.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 4));
            holder.questionText.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            holder.questionText.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
            holder.questionText.setTextColor(mContext.getResources().getColor(android.R.color.black));
            //holder.numberText.setText(position+"");
            holder.questionText.setText(answer.getmQuestion());
            holder.answerText.setVisibility(View.VISIBLE);
            //holder.numberText.setVisibility(View.VISIBLE);
            holder.answerText.setText(answer.getmAnswer() ? mContext.getString(R.string.yes) : mContext.getString(R.string.no));

            if (answer.getmAnswer()) {
                holder.answerText.setTextColor(mContext.getResources().getColor(android.R.color.holo_green_light));
                //holder.mLinearLayout.setBackgroundColor(mContext.getResources().getColor(android.R.color.darker_gray));
            } else {
                holder.answerText.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_light));
                holder.mLinearLayout.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
            }
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
