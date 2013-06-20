package com.craining.book.email;

/**
 * 后台的服务：用于进行某几个操作
 * 
 * @author Ruin
 */

import com.craining.book.DoThings.DoThings;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BackDownloadService extends Service {

	private static final String TAG = "CtrlThisPhone_Service";
	public static String str_emailDownloadID = "";
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public boolean onUnbind(Intent i) {
		Log.e(TAG, "++++++++++++++++++++++++++++++++++++++++============> TestService.onUnbind");
		return false;
	}

	@Override
	public void onRebind(Intent i) {
		Log.i(TAG, "++++++++++++++++++++++++++++++++++++++++============> TestService.onRebind");
	}

	@Override
	public void onCreate() {
		Log.i(TAG, "++++++++++++++++++++++++++++++++++++++++============> TestService.onCreate");
		
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.i(TAG, "++++++++++++++++++++++++++++++++++++++++============>TestService.onStart");
		initDoing();
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		Log.e(TAG, "++++++++++++++++++++++++++++++++++++++++============> TestService.onDestroy");
	}

	/**
	 * 后台下载附件操作
	 */
	private void initDoing() {
		try {
			if ( ReceiveImplement.downloadAttach(str_emailDownloadID) ) {
				DoThings.DisplayToast(BackDownloadService.this, "附件成功下载到存储卡的download目录中!");
				Log.e("", "get success !!!!!!!!");
			} else {
				DoThings.DisplayToast(BackDownloadService.this, "附件下载失败!");
				Log.e("", "get fail !!!!!!!!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally { 
			//此处有问题！
			onDestroy();
		}

	}
}
