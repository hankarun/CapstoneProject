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

public class PatientDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Answer> mAnswerList;
    private Context mContext;

    public PatientDetailAdapter(Context contex, ArrayList<Answer> answerArrayList) {
        mAnswerList = answerArrayList;
        mContext = contex;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View v;
        switch (viewType){
            case 1:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item1, parent, false);
                viewHolder = new ViewHolder1(v);
                break;
            case 2:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
                viewHolder = new ViewHolder(v);
                break;
            case 3:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item2, parent, false);
                viewHolder = new ViewHolder2(v);
                break;
        }
        return viewHolder;
    }

    public boolean isHeader(int position) {
        return mAnswerList.get(position).getmId() == -1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder1, int position) {
        if (isHeader(position)) {
            Answer answer = mAnswerList.get(position);
            ViewHolder holder = (ViewHolder) holder1;
            holder.questionText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 4));
            holder.questionText.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            holder.questionText.setText(answer.getmQuestionGroup());
            holder.questionText.setTextColor(mContext.getResources().getColor(android.R.color.white));
            holder.questionText.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
            holder.answerText.setVisibility(View.GONE);
        } else {
            switch (getItemViewType(position)) {
                case 1:
                    setType2(holder1,position);
                    break;
                case 2:
                    setType1(holder1, position);
                    break;
                case 3:
                    setType3(holder1,position);
                    break;
            }
        }
    }

    private void setType3(RecyclerView.ViewHolder holder1, int position) {
        ViewHolder2 holder = (ViewHolder2) holder1;
        Answer answer = mAnswerList.get(position);

        holder.questionText.setText(answer.getmQuestion());

        holder.questionDetail.setText(answer.getmDetail());

    }

    private void setType2(RecyclerView.ViewHolder holder1, int position) {
        ViewHolder1 holder = (ViewHolder1) holder1;
        Answer answer = mAnswerList.get(position);

        holder.questionText.setText(answer.getmQuestion());
        holder.answerText.setVisibility(View.VISIBLE);
        holder.answerText.setText(answer.getmAnswer() ? mContext.getString(R.string.yes) : mContext.getString(R.string.no));

        holder.questionDetail.setText(answer.getmDetail());

        if (answer.getmAnswer()) {
            holder.answerText.setTextColor(mContext.getResources().getColor(android.R.color.holo_green_light));
        } else {
            holder.answerText.setTextColor(mContext.getResources().getColor(android.R.color.holo_red_light));
            holder.mLinearLayout.setBackgroundColor(mContext.getResources().getColor(android.R.color.white));
        }
    }

    private void setType1(RecyclerView.ViewHolder holder1, int position) {
        ViewHolder holder = (ViewHolder) holder1;
        Answer answer = mAnswerList.get(position);

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

    @Override
    public int getItemCount() {
        return mAnswerList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mAnswerList.get(position).getmQuestionType();
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder {
        public LinearLayout mLinearLayout;
        public TextView questionText;
        public TextView questionDetail;


        public ViewHolder2(View v) {
            super(v);
            mLinearLayout = (LinearLayout) v.findViewById(R.id.answerLayout);
            questionText = (TextView) v.findViewById(R.id.questionTextView);
            questionDetail = (TextView) v.findViewById(R.id.questionDetailTextView);
        }
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder {
        public LinearLayout mLinearLayout;
        public TextView questionText;
        public TextView answerText;
        public TextView questionDetail;


        public ViewHolder1(View v) {
            super(v);
            mLinearLayout = (LinearLayout) v.findViewById(R.id.answerLayout);
            questionText = (TextView) v.findViewById(R.id.questionTextView);
            answerText = (TextView) v.findViewById(R.id.answerTextView);
            questionDetail = (TextView) v.findViewById(R.id.questionDetailTextView);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mLinearLayout;
        public TextView questionText;
        public TextView answerText;


        public ViewHolder(View v) {
            super(v);
            mLinearLayout = (LinearLayout) v.findViewById(R.id.answerLayout);
            questionText = (TextView) v.findViewById(R.id.questionTextView);
            answerText = (TextView) v.findViewById(R.id.answerTextView);
        }
    }
}
