package com.ibtikar.apps.wayaaak.Fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.ibtikar.apps.wayaaak.R;

import java.util.zip.Inflater;

public class ConfirmOrderDialog extends DialogFragment {
    Button button;
    ConfirmFragmentButtonsListener listener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.confirm_dialog_fragment, container, false);
        button = rootView.findViewById(R.id.button2);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onContinueButtonClickLisener();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    public interface ConfirmFragmentButtonsListener
    {
        public void onContinueButtonClickLisener ();
    }
    public void setButtonListener (ConfirmFragmentButtonsListener listener)
    {
        this.listener = listener;
    }


}
