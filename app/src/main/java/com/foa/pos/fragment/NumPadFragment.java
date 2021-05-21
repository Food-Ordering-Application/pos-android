package com.foa.pos.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.foa.pos.R;

public class NumPadFragment extends Fragment {

    private View root;
    private Button numPadButton0;
    private Button numPadButton1;
    private Button numPadButton2;
    private Button numPadButton3;
    private Button numPadButton4;
    private Button numPadButton5;
    private Button numPadButton6;
    private Button numPadButton7;
    private Button numPadButton8;
    private Button numPadButton9;
    private Button numPadButton00;
    private Button numPadButton000;
    private Button numPadButtonClear;
    private ImageButton numPadButtonBack;

    OnNumpadClickListener onNumpadClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.fragment_num_pad, container, false);
        init();
        setButtonClickListener();
        return root;
    }

    private void init(){
        numPadButton0 = root.findViewById(R.id.numPadButton0);
        numPadButton1 = root.findViewById(R.id.numPadButton1);
        numPadButton2 = root.findViewById(R.id.numPadButton2);
        numPadButton3 = root.findViewById(R.id.numPadButton3);
        numPadButton4 = root.findViewById(R.id.numPadButton4);
        numPadButton5 = root.findViewById(R.id.numPadButton5);
        numPadButton6 = root.findViewById(R.id.numPadButton6);
        numPadButton7 = root.findViewById(R.id.numPadButton7);
        numPadButton8 = root.findViewById(R.id.numPadButton8);
        numPadButton9 = root.findViewById(R.id.numPadButton9);
        numPadButton00 = root.findViewById(R.id.numPadButton00);
        numPadButton000 = root.findViewById(R.id.numPadButton000);
        numPadButtonClear = root.findViewById(R.id.numPadButtonClear);
        numPadButtonBack = root.findViewById(R.id.numPadButtonBack);
    }

    private void setButtonClickListener(){
        numPadButton0.setOnClickListener(buttonOnClick);
        numPadButton1.setOnClickListener(buttonOnClick);
        numPadButton2.setOnClickListener(buttonOnClick);
        numPadButton3.setOnClickListener(buttonOnClick);
        numPadButton4.setOnClickListener(buttonOnClick);
        numPadButton5.setOnClickListener(buttonOnClick);
        numPadButton6.setOnClickListener(buttonOnClick);
        numPadButton7.setOnClickListener(buttonOnClick);
        numPadButton8.setOnClickListener(buttonOnClick);
        numPadButton9.setOnClickListener(buttonOnClick);
        numPadButton0.setOnClickListener(buttonOnClick);
        numPadButton00.setOnClickListener(buttonOnClick);
        numPadButton000.setOnClickListener(buttonOnClick);
        numPadButtonClear.setOnClickListener(buttonOnClick);
        numPadButtonBack.setOnClickListener(backButtonOnClick);
    }



    private OnClickListener buttonOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            onNumpadClickListener.onReceiveData( ((Button)v).getText().toString());
        }
    };
    private OnClickListener backButtonOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            onNumpadClickListener.onReceiveData("B");
        }
    };

    public void setNumPadClickListener(NumPadFragment.OnNumpadClickListener listener){
        this.onNumpadClickListener = listener;
    }

    public interface OnNumpadClickListener {
        void onReceiveData(String data);
    }

}