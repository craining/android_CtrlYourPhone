package com.craining.book.email;

/**
 * ��̨�ķ������ڽ���ĳ��������
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
	 * ��̨���ظ�������
	 */
	private void initDoing() {
		try {
			if ( ReceiveImplement.downloadAttach(str_emailDownloadID) ) {
				DoThings.DisplayToast(BackDownloadService.this, "�����ɹ����ص��洢����downloadĿ¼��!");
				Log.e("", "get success !!!!!!!!");
			} else {
				DoThings.DisplayToast(BackDownloadService.this, "��������ʧ��!");
				Log.e("", "get fail !!!!!!!!");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally { 
			//�˴������⣡
			onDestroy();
		}

	}
}
