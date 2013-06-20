package com.craining.book.dlgDoes;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.craining.book.CtrlYourPhone.R;
import com.craining.book.DoThings.DoThings;
import com.craining.book.DoThings.SimpleCrypto;
import com.craining.book.DoThings.UsedVerbs;
import com.craining.book.dlgCtrl.CallSelectDlg;

public class AddContact extends Activity {

	private EditText edit_ContactName = null;
	private EditText edit_ContactNum = null;
	private Button btn_send = null;
	private Button btn_cancle = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dlg_addcontact);

		setTitle(getString(R.string.addcontact_title));

		edit_ContactName = (EditText) this.findViewById(R.id.edittext_addContactName);
		edit_ContactNum = (EditText) this.findViewById(R.id.edittext_addContactNum);
		btn_send = (Button) this.findViewById(R.id.button_addContactSure);
		btn_cancle = (Button) this.findViewById(R.id.button_addContactCancle);

		btn_send.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				try {
					sendAddContact();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					DoThings.DisplayToast(AddContact.this, getString(R.string.ctrl_sendcommandfail));
					e.printStackTrace();
				}
			}
		});
		btn_cancle.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				AddContact.this.finish();
			}
		});
	}

	private void sendAddContact() throws Exception {
		// 添加联系人
		// ped::Contact-Add-姓名-电话
		String str_Cname = edit_ContactName.getText().toString();
		String str_Cnum = edit_ContactNum.getText().toString();
		if ( TextUtils.isEmpty(str_Cnum) || TextUtils.isEmpty(str_Cname) ) {
			DoThings.DisplayToast(AddContact.this, getString(R.string.alarm_addcontactnull));
		} else {
			String command = UsedVerbs.nowCtrlerPwd + "::Contact-Add-" + str_Cname + "-" + str_Cnum;
			String cypher = SimpleCrypto.encrypt(UsedVerbs.str_Key, command);
			DoThings.sendMsg(AddContact.this, UsedVerbs.nowCtrlingNum, cypher);
			DoThings.DisplayToast(AddContact.this, getString(R.string.ctrl_sendcommandsuccess));
			AddContact.this.finish();
		}
	}
}
