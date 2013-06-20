package com.craining.book.myWidget;

import com.craining.book.CtrlYourPhone.CtrlYourPhone;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyAppService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public boolean onUnbind(Intent i) {
		return false;
	}

	@Override
	public void onRebind(Intent i) {
	}

	@Override
	public void onCreate() {
		CtrlYourPhone.jumptoCtrlmanage = true;
		Log.e("", "service create");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.e("", "service start");
		Intent i = new Intent();
		i.setClass(MyAppService.this, CtrlYourPhone.class);
		startActivity( i );
		
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
	}


}
