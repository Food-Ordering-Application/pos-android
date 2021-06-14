package com.foa.smartpos.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;


import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ErrorCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.foa.smartpos.R;
import com.foa.smartpos.caching.CompletedDeliveryOrderCaching;
import com.foa.smartpos.model.Order;
import com.google.zxing.Result;

public class ScannerQRDialog extends Dialog{

	private static final int PERMISSION_REQUEST_CAMERA = 0;
	private Context context;
	private CodeScanner codeScanner;
	private QRScannerCallback listener;

	public ScannerQRDialog(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		setContentView(R.layout.dialog_scanner_qr);

		requestCamera();

	}

	private void requestCamera() {
		if (ActivityCompat.checkSelfPermission((Activity)context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
			startScanning();
		} else {
			if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.CAMERA)) {
				ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
			} else {
				ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
			}
		}
	}

	private void startScanning() {
		CodeScannerView scannerView = findViewById(R.id.scanner_view);
		TextView qrCodeTextView = findViewById(R.id.qrCodeTextView);
		codeScanner = new CodeScanner(context, scannerView);
		codeScanner.setCamera(CodeScanner.CAMERA_BACK);
		codeScanner.setFormats( CodeScanner.ALL_FORMATS);
		codeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
		codeScanner.setScanMode(ScanMode.SINGLE);
		codeScanner.setAutoFocusEnabled(true);
		codeScanner.setFlashEnabled(false);

		// Callbacks
		codeScanner.setDecodeCallback(result -> ((Activity)context).runOnUiThread(()->{
			try {
				qrCodeTextView.setText(result.getText().trim());
				Order order = CompletedDeliveryOrderCaching.getExistOrder(result.getText().trim());
				if (order==null){
					Toast.makeText(context, "Đơn hàng chưa sẵn sàng", Toast.LENGTH_SHORT).show();
				}else{
					Thread.sleep(1000);
					if (listener!=null) listener.onFound(order);
					dismiss();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}));
		codeScanner.setErrorCallback(error -> {
			((Activity)context).runOnUiThread(()->
					Toast.makeText(context, "Không thể quét mã",
							Toast.LENGTH_LONG).show()
			);
		});

		scannerView.setOnClickListener(view -> codeScanner.startPreview());
	}

	public void setQRScannerCallback(QRScannerCallback callback){
		this.listener = callback;
	}

	public interface QRScannerCallback{
		void onFound(Order order);
	}

}
