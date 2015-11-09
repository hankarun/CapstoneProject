package com.hankarun.patienthistory.helper;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.hankarun.patienthistory.R;
import com.hankarun.patienthistory.model.Group;

public class GroupEditDialog extends DialogFragment implements View.OnClickListener {
    private TextView mGroupText;
    private TextView mGroupDetail;
    private DialogInterface mDialogInterface;
    private Group mGroup;


    public static GroupEditDialog newInstance(Group group) {
        GroupEditDialog f = new GroupEditDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putParcelable("group", group);
        f.setArguments(args);

        return f;
    }


    public void setListener(DialogInterface dialogInterface){
        mDialogInterface = dialogInterface;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGroup = getArguments().getParcelable("group");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.group_edit_dialog, container, false);
        mGroupText = (TextView) rootView.findViewById(R.id.input_group_text);
        mGroupDetail = (TextView) rootView.findViewById(R.id.input_group_detail_text);

        Button mSaveButton = (Button) rootView.findViewById(R.id.saveButton);
        Button mCancelButton = (Button) rootView.findViewById(R.id.cancelButton);

        mGroupDetail.setText(mGroup.getmGDetail());
        mGroupText.setText(mGroup.getmGText());

        mSaveButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveButton:
                mGroup.setmGText(mGroupText.getText().toString());
                mGroup.setmGDetail(mGroupDetail.getText().toString());
                mDialogInterface.dialogCompleted(mGroup);
                dismiss();
                break;
            case R.id.cancelButton:
                dismiss();
                break;
        }
    }
}
