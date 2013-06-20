package com.craining.book.myWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.craining.book.CtrlYourPhone.CtrlYourPhone;
import com.craining.book.CtrlYourPhone.R;
import com.craining.book.email.EmailLogDlg;

public class MyAppWidgetProvider extends AppWidgetProvider {

	public static int[] use_appWidgetIds;

	// 周期更新时调用
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

		use_appWidgetIds = appWidgetIds;
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			String titlePrefix = ShowWidget.loadTitlePref(context, appWidgetId);
			updateAppWidget(context, appWidgetManager, appWidgetId, titlePrefix);
		}
	}

	// 当桌面部件删除时调用
	public void onDeleted(Context context, int[] appWidgetIds) {
		// 删除appWidget
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			ShowWidget.deleteTitlePref(context, appWidgetIds[i]);
		}
	}

	// 当AppWidgetProvider提供的第一个部件被创建时调用
	public void onEnabled(Context context) {
		// widgetDel = false;
		Log.e("WIDGET", "++++++++++++++++++++++++++++++++++++++++============> Oncreated");
		PackageManager pm = context.getPackageManager();
		pm.setComponentEnabledSetting(new ComponentName("com.craining.book.CtrlYourPhone", ".MyBroadcastReceiver"), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
	}

	// 当AppWidgetProvider提供的最后一个部件被删除时调用
	public void onDisabled(Context context) {
		// widgetDel = true;
		Log.e("WIDGET", "++++++++++++++++++++++++++++++++++++++++============> OnDel");
		PackageManager pm = context.getPackageManager();
		pm.setComponentEnabledSetting(new ComponentName("com.craining.book.CtrlYourPhone", ".MyBroadcastReceiver"), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
	}

	// 更新Widget
	public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, String titlePrefix) {

		Log.e("WIDGET", "++++++++++++++++++++++++++++++++++++++++============> UPDATE");

		// 构建RemoteViews对象来对桌面部件进行更新
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_provider);
		// 更新文本内容，指定布局的组件
		views.setTextViewText(R.id.appwidget_text, titlePrefix);

		linkButtons(context, views, true);// 添加Widget中的按钮点击事件

		// 将RemoteViews的更新传入AppWidget进行更新
		appWidgetManager.updateAppWidget(appWidgetId, views);

	}

	private static void linkButtons(Context context, RemoteViews views, boolean playerActive) {

		// 此为点击按钮的事件响应
		Intent intent1 = new Intent(context, CtrlYourPhone.class);
		PendingIntent pendingIntent1 = PendingIntent.getActivity(context, 0, intent1, 0);

		Intent intent2 = new Intent(context, EmailLogDlg.class);
		PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent2, 0);
		
//		views.setOnClickPendingIntent(R.id.img_doctrl, pendingIntent1);
		views.setOnClickPendingIntent(R.id.layout_widget, pendingIntent1);//打开控制页面
		views.setOnClickPendingIntent(R.id.img_doctrl, pendingIntent1);
		views.setOnClickPendingIntent(R.id.text_logemail, pendingIntent2);//登录邮箱
		

		/*
		 * 可以添加上一曲、播放与暂停、下一曲的按钮，对下面的代码进行修改
		 * 
		 * final ComponentName serviceName = new ComponentName(context,
		 * MusicPlayer.class); intent = new
		 * Intent(MediaPlaybackService.TOGGLEPAUSE_ACTION);
		 * intent.setComponent(serviceName); pendingIntent =
		 * PendingIntent.getService(context, 0 , intent, 0 );
		 * views.setOnClickPendingIntent(R.id.control_play, pendingIntent);
		 * intent = new Intent(MediaPlaybackService.NEXT_ACTION);
		 * intent.setComponent(serviceName); pendingIntent =
		 * PendingIntent.getService(context, 0 , intent, 0 /);
		 * views.setOnClickPendingIntent(R.id.control_next, pendingIntent);
		 */
	}

}
