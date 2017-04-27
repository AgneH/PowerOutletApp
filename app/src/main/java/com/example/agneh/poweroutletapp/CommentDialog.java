package com.example.agneh.poweroutletapp;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Created by torke on 4/27/2017.
 */

public class CommentDialog extends DialogFragment implements TextView.OnEditorActionListener {

    public interface CommentDialogListener {
        void OnFinishCommentDialog(String outletid, String comment, int upvote);
    }

    private EditText txtComment;
    private RadioButton rdbUpvote;
    public CommentDialog() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container);
        txtComment = (EditText) view.findViewById(R.id.txtComment);
        rdbUpvote = (RadioButton) view.findViewById(R.id.rdbUpvote);
        txtComment.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        txtComment.setOnEditorActionListener(this);
        return view;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            String outletid = getArguments().getString("outletid");
            CommentDialogListener activity = (CommentDialogListener) getActivity();
            int upvote = rdbUpvote.isChecked()?1:0;
            activity.OnFinishCommentDialog(outletid,txtComment.getText().toString(),upvote);
            this.dismiss();
            return true;
        }
        return false;
    }


}
