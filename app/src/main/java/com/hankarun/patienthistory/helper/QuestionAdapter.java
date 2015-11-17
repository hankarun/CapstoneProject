package com.hankarun.patienthistory.helper;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.model.Question;

import java.util.ArrayList;


public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    private ArrayList<Question> mQuestions;

    public QuestionAdapter(ArrayList<Question> myDataset) {
        mQuestions = myDataset;
    }

    public ArrayList<Question> getmQuestions() {
        return mQuestions;
    }

    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_card_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final QuestionAdapter.ViewHolder holder, final int position) {

        /*int total = mQuestions.size();

        if(position%2==0){
            holder.mQuestionNumber.setText(position + 1 + "");
        }
        else{
            holder.mQuestionNumber.setText(position + 1 + "");
        }*/

        holder.mQuestionText.setText(mQuestions.get(position).getmQuestion());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (holder.mYesNoGroup.getCheckedRadioButtonId()) {
                    case R.id.radioYes:
                        holder.mNo.setChecked(true);
                        mQuestions.get(position).setmAnswer(false);
                        break;
                    case R.id.radioNo:
                        mQuestions.get(position).setmAnswer(true);
                        holder.mYes.setChecked(true);
                        break;
                }
            }
        });

        if (mQuestions.get(position).getmAnswer()) {
            holder.mYes.setChecked(true);
        } else {
            holder.mNo.setChecked(true);
        }

        holder.mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuestions.get(position).setmAnswer(true);
            }
        });

        holder.mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuestions.get(position).setmAnswer(false);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mQuestions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mQuestionNumber;
        public TextView mQuestionText;
        public RadioGroup mYesNoGroup;
        public RadioButton mYes;
        public RadioButton mNo;
        public LinearLayout mCardView;

        public ViewHolder(View v) {
            super(v);
            mQuestionNumber = (TextView) v.findViewById(R.id.questionNumberText);
            mQuestionText = (TextView) v.findViewById(R.id.questionText);
            mYesNoGroup = (RadioGroup) v.findViewById(R.id.radioYesNo);
            mYes = (RadioButton) v.findViewById(R.id.radioYes);
            mNo = (RadioButton) v.findViewById(R.id.radioNo);
            mCardView = (LinearLayout) v.findViewById(R.id.cardView);
        }
    }
}