package com.craining.book.dlgCtrl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.craining.book.CtrlYourPhone.R;
import com.craining.book.DoThings.DoThings;
import com.craining.book.DoThings.SimpleCrypto;
import com.craining.book.DoThings.UsedVerbs;
import com.craining.book.dlgDoes.DialToSb;

public class CallSelectDlg extends Activity {

	private Button btn_Callsb = null;
	private Button btn_CallUnderCtrl = null;
	private Button btn_CallHost = null;
	private Button btn_NoCallOut = null;
	private Button btn_return = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_selectdlg);

		setTitle(getString(R.string.cal_select_title));

		btn_Callsb = (Button) this.findViewById(R.id.button_callsb);
		btn_CallUnderCtrl = (Button) this.findViewById(R.id.button_callunderctrl);
		btn_CallHost = (Button) this.findViewById(R.id.button_callctrl);
		btn_NoCallOut = (Button) this.findViewById(R.id.button_nocallout);
		btn_return = (Button) this.findViewById(R.id.button_call_return);
		btn_Callsb.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// 给某人打电话
				Intent i = new Intent();
				i.setClass(CallSelectDlg.this, DialToSb.class);
				CallSelectDlg.this.finish();
				startActivity(i);
			}
		});
		btn_CallHost.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// 给控制者打电话
				try {
					sendCallCommand(true);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					DoThings.DisplayToast(CallSelectDlg.this, getString(R.string.ctrl_sendcommandfail));
					e.printStackTrace();
				}
			}
		});
		btn_CallUnderCtrl.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// 给受控者打电话
				CallSelectDlg.this.finish();
//				DoThings does = new DoThings();
				DoThings.callSomebody(CallSelectDlg.this, UsedVerbs.nowCtrlingNum);
			}
		});
		btn_NoCallOut.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// 禁止其拨打电话
				try {
					sendCallCommand(false);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					DoThings.DisplayToast(CallSelectDlg.this, getString(R.string.ctrl_sendcommandfail));
					e.printStackTrace();
				} 

			}
		});
		btn_return.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				CallSelectDlg.this.finish();
			}
		});

	}

	private void sendCallCommand(boolean callme) throws Exception {

		if ( callme ) {
			// 给控制者打电话
			// pwd::Call-Dial-me
			String str_command1 = UsedVerbs.nowCtrlerPwd + "::Call-me";
			String str_cypher1 = SimpleCrypto.encrypt(UsedVerbs.str_Key, str_command1);
			DoThings.sendMsg(CallSelectDlg.this, UsedVerbs.nowCtrlingNum, str_cypher1);
			DoThings.DisplayToast(CallSelectDlg.this, getString(R.string.ctrl_sendcommandsuccess));
			CallSelectDlg.this.finish();
		} else {
			// 禁止拨出电话
			// pws::Call-Dial-estop
			String str_command2 = UsedVerbs.nowCtrlerPwd + "::Call-estop";
			String str_cypher2 = SimpleCrypto.encrypt(UsedVerbs.str_Key, str_command2);
			DoThings.sendMsg(CallSelectDlg.this, UsedVerbs.nowCtrlingNum, str_cypher2);
			DoThings.DisplayToast(CallSelectDlg.this, getString(R.string.ctrl_sendcommandsuccess));
			CallSelectDlg.this.finish();
		}
	}

}
