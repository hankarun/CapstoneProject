package com.hankarun.patienthistory.helper;

import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.model.Question;

import java.util.ArrayList;


public class QuestionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Question> mQuestions;

    public QuestionAdapter(ArrayList<Question> myDataset) {
        mQuestions = myDataset;
    }

    public ArrayList<Question> getmQuestions() {
        return mQuestions;
    }

    private final int YesNoQ = 2;
    private final int TextQ = 3;
    private final int YesNoTQ = 1;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        View v;

        switch (viewType) {
            case YesNoQ:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_card_layout, parent, false);
                viewHolder = new ViewHolder1(v);
                break;
            case YesNoTQ:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.questioncard_layout1, parent, false);
                viewHolder = new ViewHolder2(v);
                break;
            case TextQ:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.questioncard_layout2, parent, false);
                viewHolder = new ViewHolder3(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        //TODO use holder depending question type.
        switch (getItemViewType(position)) {
            case YesNoTQ:
                setupType2(holder, position);
                break;
            case YesNoQ:
                setupType1(holder, position);
                break;
            case TextQ:
                setupType3(holder, position);
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        return mQuestions.get(position).getmType();
    }

    @Override
    public int getItemCount() {
        return mQuestions.size();
    }

    private void setupType3(RecyclerView.ViewHolder holder1, final int position) {
        final ViewHolder3 holder = (ViewHolder3) holder1;
        holder.textInputLayout.setHint(mQuestions.get(position).getmQuestion());

        holder.mQuestionDetail.setText(mQuestions.get(position).getmAnswerDetail());

        holder.mQuestionDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mQuestions.get(position).setmAnswerDetail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupType2(RecyclerView.ViewHolder holder1, final int position) {
        final ViewHolder2 holder = (ViewHolder2) holder1;
        holder.mQuestionText.setText(mQuestions.get(position).getmQuestion());

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (holder.mYesNoGroup.getCheckedRadioButtonId()) {
                    case R.id.radioYes:
                        holder.mNo.setChecked(true);
                        mQuestions.get(position).setmAnswer(false);
                        holder.mQuestionDetail.setVisibility(View.GONE);
                        mQuestions.get(position).setmAnswerDetail("");
                        break;
                    case R.id.radioNo:
                        mQuestions.get(position).setmAnswer(true);
                        holder.mYes.setChecked(true);
                        holder.mQuestionDetail.setVisibility(View.VISIBLE);
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
                holder.mQuestionDetail.setVisibility(View.VISIBLE);
            }
        });

        holder.mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuestions.get(position).setmAnswer(false);
                holder.mQuestionDetail.setVisibility(View.GONE);
                mQuestions.get(position).setmAnswerDetail("");
            }
        });

        holder.mQuestionDetail.setText(mQuestions.get(position).getmAnswerDetail());

        holder.mQuestionDetail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mQuestions.get(position).setmAnswerDetail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupType1(RecyclerView.ViewHolder holder1, final int position) {
        final ViewHolder1 holder = (ViewHolder1) holder1;
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

    public class ViewHolder3 extends RecyclerView.ViewHolder {
        public EditText mQuestionDetail;
        public LinearLayout mCardView;
        public TextInputLayout textInputLayout;

        public ViewHolder3(View v) {
            super(v);
            textInputLayout = (TextInputLayout) v.findViewById(R.id.questionDetailInput);
            mCardView = (LinearLayout) v.findViewById(R.id.cardView);
            mQuestionDetail = (EditText) v.findViewById(R.id.input_qDetail);
        }
    }

    public class ViewHolder2 extends RecyclerView.ViewHolder {
        public EditText mQuestionDetail;
        public TextView mQuestionText;
        public RadioGroup mYesNoGroup;
        public RadioButton mYes;
        public RadioButton mNo;
        public LinearLayout mCardView;

        public ViewHolder2(View v) {
            super(v);
            mQuestionText = (TextView) v.findViewById(R.id.questionText);
            mYesNoGroup = (RadioGroup) v.findViewById(R.id.radioYesNo);
            mYes = (RadioButton) v.findViewById(R.id.radioYes);
            mNo = (RadioButton) v.findViewById(R.id.radioNo);
            mCardView = (LinearLayout) v.findViewById(R.id.cardView);
            mQuestionDetail = (EditText) v.findViewById(R.id.input_qDetail);
        }
    }

    public class ViewHolder1 extends RecyclerView.ViewHolder {
        public TextView mQuestionText;
        public RadioGroup mYesNoGroup;
        public RadioButton mYes;
        public RadioButton mNo;
        public LinearLayout mCardView;

        public ViewHolder1(View v) {
            super(v);
            mQuestionText = (TextView) v.findViewById(R.id.questionText);
            mYesNoGroup = (RadioGroup) v.findViewById(R.id.radioYesNo);
            mYes = (RadioButton) v.findViewById(R.id.radioYes);
            mNo = (RadioButton) v.findViewById(R.id.radioNo);
            mCardView = (LinearLayout) v.findViewById(R.id.cardView);
        }
    }
}