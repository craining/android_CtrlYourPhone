package com.craining.book.email;

import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.craining.book.CtrlYourPhone.CtrlYourPhone;
import com.craining.book.CtrlYourPhone.R;
import com.craining.book.DoThings.DoThings;
import com.craining.book.DoThings.UsedVerbs;

public class EmailLogDlg extends Activity {

	private Button btn_emailLogin = null;
	private Button btn_emailCancle = null;
	private CheckBox checkbox_emailSavePwd = null;
	private CheckBox checkbox_emailAutoLogin = null;
	private EditText edit_emailPwd = null;
	private EditText edit_emailAddress = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dlg_emaillogin);
		setTitle(R.string.emaillog_title);

		btn_emailLogin = (Button) this.findViewById(R.id.button_emailLogSure);
		btn_emailCancle = (Button) this.findViewById(R.id.button_emailLogCancle);

		checkbox_emailSavePwd = (CheckBox) this.findViewById(R.id.checkbox_logsaveemailpwd);
		checkbox_emailAutoLogin = (CheckBox) this.findViewById(R.id.checkbox_logemailautologin);
		edit_emailPwd = (EditText) this.findViewById(R.id.edittext_logemailpwd);
		edit_emailAddress = (EditText) this.findViewById(R.id.edittext_logemailaddress);
		
		// 登录邮箱
		UsedVerbs.getInfoEmailSub = new Vector<String>();
		UsedVerbs.getInfoEmailId = new Vector<String>();
		UsedVerbs.getInfoEmailTime = new Vector<String>();
		UsedVerbs.getOtherEmailSub = new Vector<String>();
		UsedVerbs.getOtherEmailFrom = new Vector<String>();
		UsedVerbs.getOtherEmailId = new Vector<String>();
		UsedVerbs.getInfoAttack = new Vector<String>();
		UsedVerbs.getOtherAttack = new Vector<String>();
		
		checkEmailAndAutoLogin();

		checkbox_emailAutoLogin.setOnClickListener(new CheckBox.OnClickListener() {
			public void onClick(View v) {
				// 如果是自动登录，那么也将选定保存密码
				if ( checkbox_emailAutoLogin.isChecked() ) {
					checkbox_emailSavePwd.setChecked(true);
				}
			}
		});

		btn_emailLogin.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// 登录
				String str_emailPwd = edit_emailPwd.getText().toString();
				if ( TextUtils.isEmpty(str_emailPwd) ) {
					// 密码为空
					DoThings.DisplayToast(EmailLogDlg.this, getString(R.string.ctrl_inputemailpwdnull));
					restartThis();

				} else {
					// 如果保存密码
					if ( checkbox_emailSavePwd.isChecked() ) {
						UsedVerbs.hostEmailPwd = str_emailPwd;
						DoThings.writeFile(str_emailPwd, UsedVerbs.SAVE_EMAILPWD_FILE, false);
					} else {
						UsedVerbs.SAVE_EMAILPWD_FILE.delete();
					}
					// 如果自动登录
					if ( checkbox_emailAutoLogin.isChecked() ) {
						DoThings.writeFile("auto login email", UsedVerbs.SAVE_EMAILAUTOLOGIN_FILE, false);
					} else {
						UsedVerbs.SAVE_EMAILAUTOLOGIN_FILE.delete();
					}

					goIntoLogPage();
				}
			}
		});
		btn_emailCancle.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				EmailLogDlg.this.finish();
			}
		});

	}

	/**
	 * 启动前的检测 1、检测邮箱地址是否丢失 2、检测是否自动登录 3、检测是否记住密码
	 */
	private void checkEmailAndAutoLogin() {
		if(UsedVerbs.ALREADY_LOGINEMIAL.exists()) {
			goIntoLogPage();
		} 
		if ( DoThings.getEmailInfo(EmailLogDlg.this) ) {
			edit_emailAddress.setText(UsedVerbs.host_email_address);
			UsedVerbs.hostEmailName = UsedVerbs.host_email_address.substring(0, UsedVerbs.host_email_address.indexOf("@"));
			if ( UsedVerbs.SAVE_EMAILPWD_FILE.exists() ) {
				UsedVerbs.hostEmailPwd = DoThings.getinfo(UsedVerbs.SAVE_EMAILPWD_FILE);
				edit_emailPwd.setText(UsedVerbs.hostEmailPwd);
				checkbox_emailSavePwd.setChecked(true);
			}
		} else {
			Intent i = new Intent();
			i.setClass(EmailLogDlg.this, CtrlYourPhone.class);
			EmailLogDlg.this.finish();
			startActivity(i);
		}
		if ( UsedVerbs.SAVE_EMAILAUTOLOGIN_FILE.exists() ) {
			// 如果是自动登录
			goIntoLogPage();
		}
	}

	// private void showemailnullAlarmDlg(Context context) {
	// AlertDialog.Builder testDialog = new AlertDialog.Builder(context);
	// testDialog.setTitle("警告：");
	// testDialog.setMessage( getString(R.string.alarm_emailaddressnull) );
	// testDialog.setNeutralButton(" 确  定 ", new
	// DialogInterface.OnClickListener() {
	// @Override
	// public void onClick(DialogInterface dialog, int whichButton) {
	//
	// Intent i = new Intent();
	// i.setClass(EmailLogDlg.this, CtrlYourPhone.class);
	// EmailLogDlg.this.finish();
	// startActivity( i );
	// }
	// }).create();
	// testDialog.show();
	//
	// }

	/**
	 * 重启Activity
	 */
	private void restartThis() {
		Intent i = new Intent();
		i.setClass(EmailLogDlg.this, EmailLogDlg.class);
		EmailLogDlg.this.finish();
		startActivity(i);
	}

	/**
	 * 进入登录页面
	 */
	private void goIntoLogPage() {
		Intent i = new Intent();
		i.setClass(EmailLogDlg.this, EmailTitleRead.class);
		EmailLogDlg.this.finish();
		startActivity(i);
	}
}
