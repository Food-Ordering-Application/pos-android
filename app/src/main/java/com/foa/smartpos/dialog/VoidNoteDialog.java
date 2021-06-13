package com.foa.smartpos.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.foa.smartpos.R;

public class VoidNoteDialog extends Dialog implements View.OnClickListener{
	private EditText cashierNoteEditText;
	private Button btnOk;
	private Button btnCancel;
	private NoteReceiveListener listener;

	public VoidNoteDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dialog_void_note);

		cashierNoteEditText = findViewById(R.id.cashierNoteEditText);
		btnOk = findViewById(R.id.btnDoneTopping);
		btnCancel = findViewById(R.id.btnCancel);
		btnOk.setEnabled(false);

		btnOk.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		cashierNoteEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				if (cashierNoteEditText.getText().length()>0){
					btnOk.setEnabled(true);
				}
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnCancel:
			dismiss();
			break;
		case R.id.btnDoneTopping:
			if(listener != null)
				listener.onFinish(cashierNoteEditText.getText().toString());
			break;
		default:
			break;
		}
	}

	public void setNoteReceiveListener(NoteReceiveListener listener)
    {
    	this.listener = listener;
    }

    public interface NoteReceiveListener {
        void onFinish(String note);
    }


}
