package com.foa.smartpos.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.foa.smartpos.R;

public class NetworkWarningDialog extends Dialog implements View.OnClickListener{

	private Context context;
	private Button btnGoToOffline;

	private SaleModeChangeListener listener;

	public NetworkWarningDialog(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dialog_network_warning);

		btnGoToOffline = findViewById(R.id.btnGoToOffline);

		btnGoToOffline.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnGoToOffline:
			dismiss();
			if (listener!=null)
			listener.onFinish(true);
			break;
		default:
			break;
		}
	}

	public void setSaleModeChangeListener(SaleModeChangeListener listener)
    {
    	this.listener = listener;
    }

    public interface SaleModeChangeListener {
        void onFinish(boolean result);
    }

}
