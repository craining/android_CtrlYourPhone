package com.craining.book.email;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.craining.book.CtrlYourPhone.R;
import com.craining.book.DoThings.DoThings;
import com.craining.book.DoThings.UsedVerbs;

public class EmailTitleRead extends Activity {

	private ListView list = null;
	private TextView text_showTip = null;
	private Button btn_returnEmailNull = null;
	private ImageView image_updown = null;
	private boolean tipShow = true;
	private TextView text_showEmailsNums = null;

	private boolean autoLogin = false;
	private Menu m_menu = null;
	private boolean autoItemClicked = false;
	private Handler oneSHandler = new titleHandler();
	private static final int SHOW_LISTVIEW = 1009;
	private static final int SHOW_LOGINERROR = 1010;
	private Thread thread_getEmailTitle = null;
	private ProgressDialog logDialog = null;
	private boolean loginsuccess = false;
	private static int exitCount = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_manage);
		setTitle(getString(R.string.email_title));
		list = (ListView) this.findViewById(R.id.ListView_manager);
		text_showTip = (TextView) this.findViewById(R.id.text_showHeadTip);
		text_showEmailsNums = (TextView) this.findViewById(R.id.text_shownumbers);
		btn_returnEmailNull = (Button) this.findViewById(R.id.button_returnEmailNull);
		image_updown = (ImageView) this.findViewById(R.id.image_updown);
		
		if(!UsedVerbs.ALREADY_LOGINEMIAL.exists()) {
			UsedVerbs.ALREADY_LOGINEMIAL.mkdir();
			makesureMenuItem();
			getAllEmail();
			
		} else {
			loginsuccess = true;
			updateView();
		}
		
	

		btn_returnEmailNull.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// �����Ի���˵��δ�յ�������ԭ��
			}
		});
		image_updown.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if(tipShow) {
					//����
					UsedVerbs.EMAIL_HIDETIP.mkdir();
					text_showTip.setVisibility(View.GONE);
					btn_returnEmailNull.setVisibility(View.GONE);
					image_updown.setImageResource(R.drawable.ctrl_down);
					tipShow = false;
				} else {
					//��ʾ
					if( UsedVerbs.EMAIL_HIDETIP.exists() ) {
						UsedVerbs.EMAIL_HIDETIP.delete();
					}
					text_showTip.setVisibility(View.VISIBLE);
					if(UsedVerbs.getInfoEmailId.size() == 0) {
						btn_returnEmailNull.setVisibility(View.VISIBLE);
					}
					image_updown.setImageResource(R.drawable.ctrl_up);
					tipShow = true;
				}
			}
		});

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				//
				UsedVerbs.oneEmailContent = new Vector<String>();
				String getEmailID = new String("");
				String attachIncluded = new String("no");
				String infoOrNot = new String("no");// �Ƿ����ܿ��߷������ʼ�
				if ( UsedVerbs.getInfoEmailId.size() == 0 ) {// ֻ���޹��ʼ���ʱ��
					getEmailID = UsedVerbs.getOtherEmailId.get(arg2);
				} else if ( arg2 < UsedVerbs.getInfoEmailId.size() ) {// ��������ܿ��߷���������ʼ���ʱ��
					getEmailID = UsedVerbs.getInfoEmailId.get(arg2);
					infoOrNot = "yes";
				} else {// ���ڷ����ʼ���������Ĳ��Ƿ����ʼ���ʱ��
					int getid = arg2 - UsedVerbs.getInfoEmailId.size();
					getEmailID = UsedVerbs.getOtherEmailId.get(getid);
					if ( UsedVerbs.getOtherAttack.get(getid).equals(getString(R.string.email_content_containattach)) ) {
						attachIncluded = "yes";
					}
				}

				if ( !getEmailID.equals("") ) {
					// ת���鿴�ʼ�����ҳ�棬��ҪBundle��ֵ
					Intent i = new Intent();
					i.setClass(EmailTitleRead.this, EmailContentRead.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Bundle mBundle = new Bundle();
					mBundle.putString("EMAILID", getEmailID);
					mBundle.putString("FROMINFOEMAIL", infoOrNot);
					mBundle.putString("ATTACHINCLUDED", attachIncluded);
					i.putExtras(mBundle);
					EmailTitleRead.this.finish();
					startActivity(i);
				} else {
					DoThings.DisplayToast(EmailTitleRead.this, getString(R.string.alarm_getemailidfail));
				}
			}
		});

	}

	public boolean onCreateOptionsMenu(Menu menu) {
		m_menu = menu;
		if ( autoItemClicked ) {
			menu.clear();
			autoItemClicked = false;
		}
		if ( autoLogin ) {
			getMenuInflater().inflate(R.menu.menu_email_title_unauto, menu);
		} else {
			getMenuInflater().inflate(R.menu.menu_email_title_auto, menu);

		}
		super.onCreateOptionsMenu(menu);
		return true;
	}

	// �˵���Ӧ
	public boolean onOptionsItemSelected(MenuItem item) {
		switch( item.getItemId() ) {
			case R.id.menu_email_refash : {
				// ˢ��
				UsedVerbs.ALREADY_LOGINEMIAL.delete();
				restartThis();
				break;
			}
			case R.id.menu_email_autolog : {
				//�Զ���¼
				autoItemClicked = true;
				DoThings.writeFile(UsedVerbs.hostEmailPwd, UsedVerbs.SAVE_EMAILPWD_FILE, false);
				DoThings.writeFile("auto login email", UsedVerbs.SAVE_EMAILAUTOLOGIN_FILE, false);
				autoLogin = true;
				onCreateOptionsMenu(m_menu);

				break;
			}
			case R.id.menu_email_unautolog : {
				//ȡ���Զ���¼
				autoItemClicked = true;
				UsedVerbs.SAVE_EMAILAUTOLOGIN_FILE.delete();
				autoLogin = false;
				onCreateOptionsMenu(m_menu);
				break;
			}
			case R.id.menu_email_exit : {
				// ����
				UsedVerbs.ALREADY_LOGINEMIAL.delete();
				EmailTitleRead.this.finish();
				break;
			}
			default :
				break;
		}
		return false;
	}

	private void updateView() {
		// ���ɶ�̬���飬��������
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		int otherEmailCounts = 0;
		int infoEmailCounts = 0;
		if ( UsedVerbs.getInfoEmailId != null && UsedVerbs.getInfoEmailId.size() > 0 ) {
			infoEmailCounts = UsedVerbs.getInfoEmailId.size();
			for (int i = 0; i < infoEmailCounts; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("ItemTitle", UsedVerbs.getInfoEmailSub.get(i));
				map.put("ItemText", UsedVerbs.getInfoEmailTime.get(i));
				map.put("ItemImage", R.drawable.mark_email_info);
				listItem.add(map);
			}
		}
		if ( UsedVerbs.getOtherEmailId != null && UsedVerbs.getOtherEmailId.size() > 0 ) {
			otherEmailCounts = UsedVerbs.getOtherEmailId.size();
			for (int iii = 0; iii < otherEmailCounts; iii++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("ItemTitle", UsedVerbs.getOtherEmailSub.get(iii));
				map.put("ItemText", UsedVerbs.getOtherEmailFrom.get(iii));
				if ( UsedVerbs.getOtherAttack.get(iii).equals(getString(R.string.email_content_containattach)) ) {
					map.put("ItemImage", R.drawable.mark_email_attach);
				}
				listItem.add(map);
			}
		}
		// ������������Item�Ͷ�̬�����Ӧ��Ԫ��
		SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem, R.layout.list_items, new String[]{"ItemImage", "ItemTitle", "ItemText"}, new int[]{R.id.ItemImage, R.id.ItemTitle, R.id.ItemText});

		list.setAdapter(listItemAdapter);
		
		
		//��ʾ�ʼ���Ŀ
		image_updown.setVisibility(View.VISIBLE);
		int all = otherEmailCounts + infoEmailCounts;
		text_showEmailsNums.setText("�ռ��乲�� " + all + "���ʼ�");
		text_showEmailsNums.setVisibility(View.VISIBLE);
		
		if ( loginsuccess ) {
			
				if ( infoEmailCounts == 0 ) {
					// δ��ȡ�������ʼ�
					text_showTip.setText(getString(R.string.email_titlelisttip2));
					btn_returnEmailNull.setVisibility(View.VISIBLE);
				} else {
					// ���ڷ����ʼ�
					text_showTip.setText(getString(R.string.email_titlelisttip1));
				}
				if( UsedVerbs.EMAIL_HIDETIP.exists() ) {
					//����
					tipShow = false;
					image_updown.setImageResource(R.drawable.ctrl_down);
					btn_returnEmailNull.setVisibility(View.GONE);
					text_showTip.setVisibility(View.GONE);
				} 
		} else {
			text_showTip.setText(getString(R.string.emaillogin_error));
			image_updown.setVisibility(View.GONE);
			text_showEmailsNums.setVisibility(View.GONE);
		}
		
	}

	private void getAllEmail() {
		logDialog = new ProgressDialog(EmailTitleRead.this);
		logDialog.setTitle(getString(R.string.email_title_dlgtitle));
		logDialog.setMessage(getString(R.string.email_title_dlgcontent));
		logDialog.show();

		thread_getEmailTitle = new getEmailTitleThread();
		thread_getEmailTitle.start();
	}

	/**
	 * ȷ��menuҪ��ʾ������
	 */
	private void makesureMenuItem() {
		if ( UsedVerbs.SAVE_EMAILAUTOLOGIN_FILE.exists() ) {
			autoLogin = true;
		} else {
			autoLogin = false;
		}
	}

	/**
	 * ����ʼ�������߳�
	 * 
	 * @author Ruin
	 * 
	 */
	class getEmailTitleThread extends Thread {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			try {
				// ��ȡ�ʼ�����
				Log.e("", "getting Email !!!!!!!!");
//				ReceiveImplement.getEmailSubject();
				if ( !ReceiveImplement.getEmailSubject() ) {
					// ��ȡʧ��
					loginsuccess = false;
					logDialog.dismiss();
					oneSHandler.sendEmptyMessageDelayed(SHOW_LOGINERROR, 0);
				} else {
					loginsuccess = true;
					oneSHandler.sendEmptyMessageDelayed(SHOW_LISTVIEW, 0);
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				logDialog.dismiss();
			}
		}
	}

	/**
	 * ��Ӧ��Ϣ������View
	 */
	private class titleHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch( msg.what ) {
				case SHOW_LISTVIEW : {
					// ������ӳ�����Ϣʱ�Ĳ���:����View
					updateView();
					break;
				}
				case SHOW_LOGINERROR : {
					// �����¼ʧ�ܣ�����ʾ����ȡ�����Զ���¼�ͼ�ס����
					updateView();
					UsedVerbs.SAVE_EMAILAUTOLOGIN_FILE.delete();
					// UsedVerbs.SAVE_EMAILPWD_FILE.delete();
					autoLogin = false;
					DoThings.showAlarmDlg(EmailTitleRead.this, getString(R.string.alarm_emailloginfail));
					break;
				}
				default :
					break;
			}
		}
	}

	private void restartThis() {
		Intent intent = new Intent();
		intent.setClass(EmailTitleRead.this, EmailTitleRead.class);
		EmailTitleRead.this.finish();
		startActivity(intent);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			
			exitCount ++;
			if(exitCount == 1) {
				DoThings.DisplayToast(EmailTitleRead.this, "�ٰ�һ�ν��˳�����");
			} else {
				exitCount = 0;
				UsedVerbs.ALREADY_LOGINEMIAL.delete();
				EmailTitleRead.this.finish();
			}
			
			return false;
		}

		return false;
	}
}
