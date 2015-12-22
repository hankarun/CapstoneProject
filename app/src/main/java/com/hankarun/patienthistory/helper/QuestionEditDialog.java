package com.hankarun.patienthistory.helper;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.model.Group;
import com.hankarun.patienthistory.model.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionEditDialog extends DialogFragment implements View.OnClickListener{
    private ArrayList<Group> mGroups;
    private Question mQuestion;
    private TextView mQuestionTextView;
    private Spinner mGroupsSpinner;
    private Spinner mQuestionTypeSpinner;
    private DialogInterface mDialogInterface;

    public static QuestionEditDialog newInstance(Question question,ArrayList<Group> groups) {
        QuestionEditDialog f = new QuestionEditDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putParcelable("question", question);
        args.putParcelableArrayList("groups",groups);
        f.setArguments(args);

        return f;
    }

    public void setListener(DialogInterface dialogInterface){
        mDialogInterface = dialogInterface;
    }

    private final String[] mGroupNames = {
                                            "Evet - Hayır ve Kullanıcı Girişli Soru Tipi",
                                            "Evet - Hayır Soru Tipi",
                                            "Sadece Kullanıcı Girişli Soru Tipi",};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGroups = getArguments().getParcelableArrayList("groups");
        mQuestion = getArguments().getParcelable("question");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.question_edit_dialog, container, false);
        mQuestionTextView = (TextView) rootView.findViewById(R.id.input_question_edit_text);
        mGroupsSpinner = (Spinner) rootView.findViewById(R.id.questionEditSpinner);

        Button mSaveButton = (Button) rootView.findViewById(R.id.saveButton);
        Button mCancelButton = (Button) rootView.findViewById(R.id.cancelButton);

        mQuestionTextView.setText(mQuestion.getmQuestion());

        final ArrayAdapter<Group> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, mGroups);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mGroupsSpinner.setAdapter(adapter);

        mGroupsSpinner.setSelection(mQuestion.getmGroupId() - 1);

        mQuestionTypeSpinner = (Spinner) rootView.findViewById(R.id.questionTypeSpinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, mGroupNames);
        mQuestionTypeSpinner.setAdapter(dataAdapter);

        Log.d("type ", mQuestion.getmType()+"");
        mQuestionTypeSpinner.setSelection(mQuestion.getmType()-1);


        mSaveButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveButton:
                mQuestion.setmQuestion(mQuestionTextView.getText().toString());
                mQuestion.setmGroupId(mGroupsSpinner.getSelectedItemPosition() + 1);
                mQuestion.setmType(mQuestionTypeSpinner.getSelectedItemPosition()+1);
                mDialogInterface.dialogCompleted(mQuestion);
                dismiss();
                break;
            case R.id.cancelButton:
                dismiss();
                break;
        }
    }
}
