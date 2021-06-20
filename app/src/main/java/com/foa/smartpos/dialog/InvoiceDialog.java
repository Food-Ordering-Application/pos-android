package com.foa.smartpos.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.foa.smartpos.R;
import com.foa.smartpos.utils.Constants;

public class InvoiceDialog extends Dialog {

	private String orderId;
	private WebView webView;
	private ProgressBar loadingProgress;
	public InvoiceDialog(Context context, String orderId) {
		super(context);
		this.orderId = orderId;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dialog_invoice);
		webView = findViewById(R.id.invoiceWebView);
		loadingProgress = findViewById(R.id.loadingProgress);
		loadInvoice();
	}

	private void loadInvoice()  {
		webView.getSettings().setLoadsImagesAutomatically(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		webView.loadUrl(Constants.BASE_URL+"/order/"+orderId+"/invoice");
	}

}
