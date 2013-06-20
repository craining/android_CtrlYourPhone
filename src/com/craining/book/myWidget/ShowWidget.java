package com.craining.book.myWidget;

import java.util.ArrayList;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.craining.book.CtrlYourPhone.R;
import com.craining.book.DoThings.DoThings;
import com.craining.book.DoThings.UsedVerbs;

public class ShowWidget extends Activity {

	int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

	public ShowWidget() {
		super();
	}

	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.android_bg);
		setResult(RESULT_CANCELED);

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if ( extras != null ) {
			mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		if ( mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID ) {
			DoThings.DisplayToast(ShowWidget.this, getString(R.string.addwidget_fail));
			finish();
		} else if ( toShowWidget() ) {
			// ��ʾWidget�ɹ���
			DoThings.DisplayToast(ShowWidget.this, getString(R.string.addwidget_success));
			finish();
		}

	}

	// // ���Widget��ʾ����
	static String loadTitlePref(Context context, int appWidgetId) {

		return UsedVerbs.strShowOnWidget;
	}

	static void deleteTitlePref(Context context, int appWidgetId) {
		// ɾ�����沿��ʱ����

	}

	static void loadAllTitlePrefs(Context context, ArrayList<Integer> appWidgetIds, ArrayList<String> texts) {
		// ÿ�����沿��Ҫ��ʾ������

	}

	private boolean toShowWidget() {
		final Context context = ShowWidget.this;

		// ȡ��AppWidgetManagerʵ��
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		// ����AppWidget
		MyAppWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId, UsedVerbs.strShowOnWidget);
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_OK, resultValue);

		return true;
	}

}
