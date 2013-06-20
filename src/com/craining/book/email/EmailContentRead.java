package com.craining.book.email;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.craining.book.CtrlYourPhone.R;
import com.craining.book.DoThings.DoThings;
import com.craining.book.DoThings.UsedVerbs;

public class EmailContentRead extends Activity {
	private static final int MSG_UPDATEVIEW = 1011;
	private static final int MSG_DOWNLOADATTACH = 1012;
	private Handler hander_titleHandler = new contentHandler();
	private Thread getContent = null;
	private TextView text_showSubject = null;
	private TextView text_showDateAndFrom = null;
	private TextView text_showContent = null;
	private TextView text_showAttach = null;
	private TextView text_showContentTip = null;
	private Button btn_downLoadAttach = null;
	private ProgressDialog contentDialog = null;
	private String str_emailID = "";
	private String str_emailFrom = "";
	private String str_attachIncluded = "";
	private boolean gotContent = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_emailshow);
		setTitle(getString(R.string.email_title));
		text_showSubject = (TextView) this.findViewById(R.id.text_emailsub);
		text_showDateAndFrom = (TextView) this.findViewById(R.id.text_emailtimeandfrom);
		text_showContent = (TextView) this.findViewById(R.id.text_emailcontent);
		text_showAttach = (TextView) this.findViewById(R.id.text_emailAttach);
		text_showContentTip = (TextView) this.findViewById(R.id.text_emailcontenttip);
		btn_downLoadAttach = (Button) this.findViewById(R.id.button_DownLoadAttach);

		setProgressBarIndeterminateVisibility(true);
		Bundle bundle = getIntent().getExtras();
		str_emailID = bundle.getString("EMAILID");
		str_emailFrom = bundle.getString("FROMINFOEMAIL");
		str_attachIncluded = bundle.getString("ATTACHINCLUDED");
		activityInit();
	}

	private void activityInit() {
		contentDialog = new ProgressDialog(EmailContentRead.this);
		contentDialog.setTitle(getString(R.string.email_title_contenttitleget));
		contentDialog.setMessage(getString(R.string.email_title_dlgcontent));
		contentDialog.show();
		getContent = new getEmailContentThread();
		getContent.start();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_email_cotent, menu);
		super.onCreateOptionsMenu(menu);
		return true;
	}

	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch( item.getItemId() ) {
			case R.id.menu_emailcontent_del : {
				// ɾ��
				alarmDlg(EmailContentRead.this).show();
				break;
			}
			case R.id.menu_emailcontent_save : {
				// ��ȡ����
				saveEmail();
				break;
			}
			case R.id.menu_emailcontent_return : {
				// ����
				returnToEmailList();
				break;
			}
			default :
				break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	/**
	 * ����ʼ����ݵ��߳�
	 * 
	 * @author Ruin
	 * 
	 */
	class getEmailContentThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				// ��ȡ�ʼ�����
				Log.e("", "get Email !!!!!!!!");

				if ( ReceiveImplement.getContent(str_emailID) ) {
					gotContent = true;
				} else {
					gotContent = true;
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				hander_titleHandler.sendEmptyMessageDelayed(MSG_UPDATEVIEW, 0);
			}
		}
	}

	/**
	 * ��Ӧ��Ϣ������View
	 */
	private class contentHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch( msg.what ) {
				case MSG_UPDATEVIEW : {
					// ������ӳ�����Ϣʱ�Ĳ���:����View
					contentDialog.dismiss();
					updateView();
					break;
				} 
				case MSG_DOWNLOADATTACH: {
					//�������ظ�������
					BackDownloadService.str_emailDownloadID = str_emailID;
					startService(new Intent("com.craining.book.email.BackDownloadService"));
					break;
				}
				default :
					break;
			}
		}
	}

	private Dialog alarmDlg(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		// builder.setIcon(R.drawable.img2);
		builder.setTitle(getString(R.string.ctrl_alarm));
		builder.setMessage(getString(R.string.alarm_emaildelsure));

		builder.setPositiveButton(getString(R.string.ctrl_sure), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// ���ɾ������..........
				boolean delsuccess = false;
				try {
					if ( ReceiveImplement.deleteMail(str_emailID) ) {
						delsuccess = true;
					} else {
						delsuccess = false;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				if ( delsuccess ) {
					DoThings.DisplayToast(EmailContentRead.this, getString(R.string.ctrl_delsuccess));
					UsedVerbs.ALREADY_LOGINEMIAL.delete();
					returnToEmailList();
				} else {
					DoThings.showAlarmDlg(EmailContentRead.this, getString(R.string.alarm_emaildelfail));
				}
			}
		});
		builder.setNegativeButton(getString(R.string.ctrl_cancle), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		});
		return builder.create();
	}

	private void updateView() {
		setProgressBarIndeterminateVisibility(false);
		if ( gotContent && UsedVerbs.oneEmailContent.size() == 4 ) {
			// �����ȡ���ʼ�����
			text_showContentTip.setText(getString(R.string.email_content_tip));
			text_showSubject.setText(UsedVerbs.oneEmailContent.get(0));// ��ʾ�ʼ�����
			if ( str_emailFrom.equals("yes") || UsedVerbs.oneEmailContent.get(2).equals("") ) {// ������ܿ��߷�������Ϣ
				text_showDateAndFrom.setText(UsedVerbs.oneEmailContent.get(1) + getString(R.string.email_content_fromnull));// ��ʾ�ʼ�����ʱ�������������
			} else {
				text_showDateAndFrom.setText(UsedVerbs.oneEmailContent.get(1) + getString(R.string.email_content_from) + UsedVerbs.oneEmailContent.get(2));// ��ʾ�ʼ�����ʱ��ͷ�����
			}

			text_showContent.setText(UsedVerbs.oneEmailContent.get(3));// ��ʾ�ʼ�����

			if ( str_attachIncluded.equals("yes") ) {
//				text_showAttach.setText("��������");// ��ʾ��������
				btn_downLoadAttach.setVisibility(View.VISIBLE);
				btn_downLoadAttach.setOnClickListener(new Button.OnClickListener() {
					public void onClick(View v) {
						// ���ظ���(

						if ( DoThings.sdCardCanUsed() ) {
							DoThings.DisplayToast(EmailContentRead.this, "��̨������...");
							hander_titleHandler.sendEmptyMessageDelayed(MSG_DOWNLOADATTACH, 0);
							returnToEmailList();
						} else {
							DoThings.showAlarmDlg(EmailContentRead.this, getString(R.string.alarm_sdcardunpresent));
						}

					}
				});
			} else {
				// ��ʾ��������
//				text_showAttach.setText(getString(R.string.email_content_uncontainattach));
			}

		} else {
			// û�л�ȡ���ʼ�����
			text_showContentTip.setText(getString(R.string.alarm_getemailconrentfailtext));
			DoThings.showAlarmDlg(EmailContentRead.this, getString(R.string.alarm_getemailcontentfail));
		}
	}

	private void saveEmail() {
		if ( DoThings.sdCardCanUsed() ) {
			// д��洢��
			String fileContent = new String("");
			String fileName = new String("");
			fileName = DoThings.returnDateOrTime((int) 0) + "_" + DoThings.returnDateOrTime((int) 1);
			fileName = UsedVerbs.oneEmailContent.get(0) + fileName;
			Log.e("file name", fileName);
			// fileName = UsedVerbs.oneEmailContent.get(0) + "_"
			// + DoThings.returnDateOrTime((int) 0) + "_"
			// + DoThings.returnDateOrTime((int) 1);
			//
			// ���ʼ�д���ļ�
			File emailfileDir = new File(Environment.getExternalStorageDirectory().getPath() + UsedVerbs.SDCARD_EMAILFILE_DIR);
			File emailfile = new File(Environment.getExternalStorageDirectory().getPath() + UsedVerbs.SDCARD_EMAILFILE_DIR + fileName + ".txt");
			if ( !emailfileDir.exists() ) {
				emailfileDir.mkdir();
			}
			Log.e("file dir", Environment.getExternalStorageDirectory().getPath() + UsedVerbs.SDCARD_EMAILFILE_DIR + fileName + ".txt");
			fileContent = getString(R.string.email_contentsave_substr) + "��" + UsedVerbs.oneEmailContent.get(0) + "��\r\n" + getString(R.string.email_contentsave_timestr) + UsedVerbs.oneEmailContent.get(1) + "\r\n" + getString(R.string.email_contentsave_fromstr) + UsedVerbs.oneEmailContent.get(2) + "\r\n" + getString(R.string.email_contentsave_contentstr) + "\r\n" + UsedVerbs.oneEmailContent.get(3) + "\r\n";

			if ( DoThings.writeFile(fileContent, emailfile, true) ) {// д��һ����ʱ���������ļ�
				DoThings.DisplayToast(EmailContentRead.this, getString(R.string.email_contentsave_success));
			} else {
				DoThings.showAlarmDlg(EmailContentRead.this, getString(R.string.alarm_sdcardunpresent));
			}

		} else {
			DoThings.showAlarmDlg(EmailContentRead.this, getString(R.string.alarm_sdcardunpresent));
		}
	}

	private void returnToEmailList() {
		Intent i = new Intent();
		i.setClass(EmailContentRead.this, EmailTitleRead.class);
		EmailContentRead.this.finish();
		startActivity(i);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			returnToEmailList();
			return false;
		}

		return false;
	}

}