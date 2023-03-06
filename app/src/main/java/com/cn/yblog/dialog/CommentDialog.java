package com.cn.yblog.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cn.yblog.R;
import com.google.android.material.textfield.TextInputLayout;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2023/1/2
 * <p>version: 1.0
 * <p>update: none
 */
public class CommentDialog extends DialogFragment {
    private Dialog mDialog;
    private EditText mEdtComment;
    private ImageButton mImbComment;
    private OnCommentListener mOnCommentListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mDialog = new Dialog(getActivity(), R.style.BottomDialog);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_comment);
        mDialog.setCanceledOnTouchOutside(true);

        Window window = mDialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.horizontalMargin = 0;
        params.verticalMargin = 0;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);

        mEdtComment = ((TextInputLayout) mDialog.findViewById(R.id.dialog_comment_til_comment)).getEditText();
        mImbComment = mDialog.findViewById(R.id.dialog_comment_btn_send);

        mImbComment.setOnClickListener(v -> {
            if (mOnCommentListener != null) {
                mOnCommentListener.comment(mEdtComment.getText().toString());
            }
        });


        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            mEdtComment.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mEdtComment, InputMethodManager.SHOW_IMPLICIT);
        }, 100);

        return mDialog;
    }

    public void setOnCommentListener(OnCommentListener onCommentListener) {
        mOnCommentListener = onCommentListener;
    }

    public interface OnCommentListener {
        /**
         * 评论
         *
         * @param comment 评论内容
         */
        void comment(String comment);
    }
}
