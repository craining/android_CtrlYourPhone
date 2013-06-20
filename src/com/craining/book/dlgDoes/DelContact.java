package com.craining.book.dlgDoes;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;

import com.craining.book.CtrlYourPhone.R;
import com.craining.book.DoThings.DoThings;
import com.craining.book.DoThings.SimpleCrypto;
import com.craining.book.DoThings.UsedVerbs;

public class DelContact extends Activity {

	private EditText edit_delNum = null;
	private Button btn_send = null;
	private Button btn_cancle = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dlg_delcontact);

		setTitle(getString(R.string.delcontact_title));

		edit_delNum = (EditText) this.findViewById( R.id.edittext_delContactNum );
		btn_send = (Button) this.findViewById(R.id.button_delContactSure);
		btn_cancle = (Button) this.findViewById(R.id.button_delContactCancle);

		btn_send.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				try {
					sendDelContact();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					DoThings.DisplayToast(DelContact.this, getString(R.string.ctrl_sendcommandfail));
					e.printStackTrace();
				} 
			}
		});
		btn_cancle.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				DelContact.this.finish();
			}
		});
	}

	private void sendDelContact() throws Exception {
		//删除联系人命令
		//pwd::Contact-Del-电话
		String delnum = edit_delNum.getText().toString();
		if(TextUtils.isEmpty(delnum)) {
			DoThings.DisplayToast(DelContact.this, getString(R.string.alarm_delcontactnull));
		} else {
			String command = UsedVerbs.nowCtrlerPwd + "::Contact-Del-" + delnum;
			String cypher = SimpleCrypto.encrypt(UsedVerbs.str_Key, command);
			DoThings.sendMsg(DelContact.this, UsedVerbs.nowCtrlingNum, cypher);
			DoThings.DisplayToast(DelContact.this, getString(R.string.ctrl_sendcommandsuccess));
			DelContact.this.finish();
		}
			
	}
}
