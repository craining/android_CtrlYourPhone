package com.craining.book.dlgCtrl;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.craining.book.CtrlYourPhone.R;
import com.craining.book.DoThings.DoThings;
import com.craining.book.DoThings.SimpleCrypto;
import com.craining.book.DoThings.UsedVerbs;
import com.craining.book.dlgDoes.ManagerBlackNum;

public class CheckSelectDlg extends Activity {

	private Button btn_CheckLocation = null;
	private Button btn_ChenckMsgRecord = null;
	private Button btn_CheckConversationRecord = null;
	private Button btn_return = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_selectdlg);
		setTitle(getString(R.string.check_select_title));

		btn_CheckLocation = (Button) this.findViewById(R.id.button_check_local);
		btn_ChenckMsgRecord = (Button) this.findViewById(R.id.button_check_msgrecord);
		btn_CheckConversationRecord = (Button) this.findViewById(R.id.button_checkconversationrecord);
		btn_return = (Button) this.findViewById(R.id.button_check_return);

		btn_CheckLocation.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// 查询位置
				try {
					checkPosition();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					DoThings.DisplayToast(CheckSelectDlg.this, getString(R.string.ctrl_sendcommandfail));
					e.printStackTrace();
				} 
			}
		});

		btn_ChenckMsgRecord.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// 查询短信记录
				try {
					checkMsg();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					DoThings.DisplayToast(CheckSelectDlg.this, getString(R.string.ctrl_sendcommandfail));
					e.printStackTrace();
				} 
			}
		});

		btn_CheckConversationRecord.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// 查询通话记录
				try {
					checkConversation();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					DoThings.DisplayToast(CheckSelectDlg.this, getString(R.string.ctrl_sendcommandfail));
					e.printStackTrace();
				} 
			}
		});
		btn_return.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				CheckSelectDlg.this.finish();
			}
		});

	}
	private void checkPosition() throws Exception {
		// pwd::Ask-Position
		String command = UsedVerbs.nowCtrlerPwd + "::Ask-Position";
		String cypher = SimpleCrypto.encrypt(UsedVerbs.str_Key, command);
		DoThings.sendMsg(CheckSelectDlg.this, UsedVerbs.nowCtrlingNum, cypher);
		DoThings.DisplayToast(CheckSelectDlg.this, getString(R.string.checkunderctrl_tip));
		CheckSelectDlg.this.finish();
	}

	private void checkMsg() throws Exception {
		// pwd::Ask-Msg-email
		String command = UsedVerbs.nowCtrlerPwd + "::Ask-Msg-" + UsedVerbs.host_email_address;
		String cypher = SimpleCrypto.encrypt(UsedVerbs.str_Key, command);
		DoThings.sendMsg(CheckSelectDlg.this, UsedVerbs.nowCtrlingNum, cypher);
		DoThings.DisplayToast(CheckSelectDlg.this, getString(R.string.checkunderctrl_tip));
		CheckSelectDlg.this.finish();
	}

	private void checkConversation() throws Exception {
		// pwd::Ask-Conversation-email
		String command = UsedVerbs.nowCtrlerPwd + "::Ask-Conversation-" + UsedVerbs.host_email_address;
		String cypher = SimpleCrypto.encrypt(UsedVerbs.str_Key, command);
		DoThings.sendMsg(CheckSelectDlg.this, UsedVerbs.nowCtrlingNum, cypher);
		DoThings.DisplayToast(CheckSelectDlg.this, getString(R.string.checkunderctrl_tip));
		CheckSelectDlg.this.finish();
	}

}
