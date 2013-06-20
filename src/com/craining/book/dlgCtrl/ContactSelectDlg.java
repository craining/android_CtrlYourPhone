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
import com.craining.book.dlgDoes.AddContact;
import com.craining.book.dlgDoes.DelContact;
import com.craining.book.dlgDoes.ManagerBlackNum;
import com.craining.book.msgReceive.CtrlMsgReceive;

public class ContactSelectDlg extends Activity {

	private Button btn_addContact = null;
	private Button btn_delContact = null;
	private Button btn_blackNumManager = null;
	private Button btn_allContacts = null;
	private Button btn_return = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_selectdlg);
		setTitle(getString(R.string.contact_select_title));

		btn_addContact = (Button) this.findViewById(R.id.button_contentAdd);
		btn_delContact = (Button) this.findViewById(R.id.button_contentDel);
		btn_blackNumManager = (Button) this.findViewById(R.id.button_BlackNums);
		btn_allContacts = (Button) this.findViewById(R.id.button_allContacts);
		btn_return = (Button) this.findViewById(R.id.button_contact_return);
		btn_addContact.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// 添加联系人
				Intent i = new Intent();
				i.setClass(ContactSelectDlg.this, AddContact.class);
				ContactSelectDlg.this.finish();
				startActivity(i);
			}
		});
		btn_delContact.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// 删除联系人
				Intent i = new Intent();
				i.setClass(ContactSelectDlg.this, DelContact.class);
				ContactSelectDlg.this.finish();
				startActivity(i);
			}
		});
		btn_allContacts.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// 获得联系人名单
				try {
					sendCommand();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					DoThings.DisplayToast(ContactSelectDlg.this, getString(R.string.ctrl_sendcommandfail));
					e.printStackTrace();
				}
			}
		});

		btn_blackNumManager.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// 黑名单管理
				Intent i = new Intent();
				i.setClass(ContactSelectDlg.this, ManagerBlackNum.class);
				ContactSelectDlg.this.finish();
				startActivity(i);
			}
		});
		btn_return.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				ContactSelectDlg.this.finish();
			}
		});
	}

	private void sendCommand() throws Exception {
		String command = UsedVerbs.nowCtrlerPwd + "::Ask-Contacts-" + UsedVerbs.host_email_address;
		String cypher = SimpleCrypto.encrypt(UsedVerbs.str_Key, command);
		DoThings.sendMsg(ContactSelectDlg.this, UsedVerbs.nowCtrlingNum, cypher);
		DoThings.DisplayToast(ContactSelectDlg.this, getString(R.string.checkunderctrl_tip));
		ContactSelectDlg.this.finish();
	}
}