package com.craining.book.dlgCtrl;

import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.craining.book.CtrlYourPhone.R;
import com.craining.book.DoThings.DoThings;
import com.craining.book.DoThings.SimpleCrypto;
import com.craining.book.DoThings.UsedVerbs;
import com.craining.book.dlgDoes.ChangeHost;
import com.craining.book.email.EmailLogDlg;

public class OthersOperaDlg extends Activity {

	private Button btn_ChangeHostInfo = null;
	private Button btn_CheckCtrl = null;
	private Button btn_LoginEmail = null;
	// private Button btn_AddWidget = null;
	private Button btn_return = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.other_selectdlg);
		setTitle(getString(R.string.other_select_title));

		btn_ChangeHostInfo = (Button) this.findViewById(R.id.button_changehost);
		btn_CheckCtrl = (Button) this.findViewById(R.id.button_checkctrl);
		btn_LoginEmail = (Button) this.findViewById(R.id.button_loginEmail);
		// btn_AddWidget = (Button) this.findViewById(R.id.button_addWidget);
		btn_return = (Button) this.findViewById(R.id.button_other_return);

		btn_ChangeHostInfo.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// 更改邮箱
				Intent i = new Intent();
				i.setClass(OthersOperaDlg.this, ChangeHost.class);
				OthersOperaDlg.this.finish();
				startActivity(i);

			}
		});
		btn_CheckCtrl.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// 验证是否受控
				try {
					checkUnderCtrl();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					DoThings.DisplayToast(OthersOperaDlg.this, getString(R.string.ctrl_sendcommandfail));
					e.printStackTrace(); 
				} 
			}
		});
		btn_LoginEmail.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {

				// //离线测试显示邮件页面
				// UsedVerbs.oneEmailContent = new Vector<String>();
				// Intent i = new Intent();
				// i.setClass(OthersOperaDlg.this, EmailContentRead.class);
				// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// Bundle mBundle = new Bundle();
				// mBundle.putString("EMAILID", "ID");
				// mBundle.putString("FROMINFOEMAIL", "yes");
				// i.putExtras(mBundle);
				// OthersOperaDlg.this.finish();
				// startActivity(i);

				Intent i = new Intent();
				i.setClass(OthersOperaDlg.this, EmailLogDlg.class);
				OthersOperaDlg.this.finish();
				startActivity(i);

			}
		});

		// btn_AddWidget.setOnClickListener(new Button.OnClickListener() {
		// public void onClick(View v) {
		// // 添加Widget
		// Intent i = new Intent(OthersOperaDlg.this, ShowWidget.class);
		// OthersOperaDlg.this.finish();
		// startActivity(i);
		// }
		// });
		btn_return.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				OthersOperaDlg.this.finish();
			}
		});

	}
	
	private void checkUnderCtrl() throws Exception {
		//check::pwd
		String command = "check::" + UsedVerbs.nowCtrlerPwd;
		String cypher = SimpleCrypto.encrypt(UsedVerbs.str_Key, command);
		DoThings.sendMsg(OthersOperaDlg.this, UsedVerbs.nowCtrlingNum, cypher);
		DoThings.DisplayToast(OthersOperaDlg.this, getString(R.string.checkunderctrl_tip));
		OthersOperaDlg.this.finish();
	}

}
