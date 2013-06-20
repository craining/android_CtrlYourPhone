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
			// 显示Widget成功！
			DoThings.DisplayToast(ShowWidget.this, getString(R.string.addwidget_success));
			finish();
		}

	}

	// // 获得Widget显示内容
	static String loadTitlePref(Context context, int appWidgetId) {

		return UsedVerbs.strShowOnWidget;
	}

	static void deleteTitlePref(Context context, int appWidgetId) {
		// 删除桌面部件时调用

	}

	static void loadAllTitlePrefs(Context context, ArrayList<Integer> appWidgetIds, ArrayList<String> texts) {
		// 每个桌面部件要显示的内容

	}

	private boolean toShowWidget() {
		final Context context = ShowWidget.this;

		// 取得AppWidgetManager实例
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		// 更新AppWidget
		MyAppWidgetProvider.updateAppWidget(context, appWidgetManager, mAppWidgetId, UsedVerbs.strShowOnWidget);
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_OK, resultValue);

		return true;
	}

}
