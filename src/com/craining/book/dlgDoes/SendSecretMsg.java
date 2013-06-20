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

public class SendSecretMsg extends Activity {

	private Button btn_send = null;
	private Button btn_cancle = null;
	private EditText edit_towho = null;
	private EditText edit_secretContent = null;
	private EditText edit_secretPwd = null;

	public static boolean fromRelay = false;
	public static String relyToWho = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dlg_sendsecretmsg);

		setTitle(getString(R.string.msg_sendsecretmsg_title));

		edit_towho = (EditText) this.findViewById(R.id.edittext_sendsecretmsgto);
		edit_secretContent = (EditText) this.findViewById(R.id.edittext_sendsecretmsgcontent);
		edit_secretPwd = (EditText) this.findViewById(R.id.edittext_sendsecretmsgpwd);
		btn_send = (Button) this.findViewById(R.id.button_sendsecretmsgSure);
		btn_cancle = (Button) this.findViewById(R.id.button_sendsecretmsgCancle);
		edit_towho.setFocusable(false);
		if ( fromRelay ) {
			edit_towho.setText(relyToWho);
			edit_towho.setEnabled(false);
			fromRelay = false;
		} else {
			edit_towho.setText(UsedVerbs.nowCtrlingNum);
			edit_towho.setFocusable(true);
		}
		
		btn_send.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// 获取阅读密码，发送加密短信
				try {
					sendSecrteMsg();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					DoThings.DisplayToast(SendSecretMsg.this, getString(R.string.msg_sendsecretmsg_fial));
					e.printStackTrace();
				}
			}
		});
		btn_cancle.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				SendSecretMsg.this.finish();
			}
		});
	}
	
	private void sendSecrteMsg() throws Exception {
		// 发送加密信息
		// ReceiveMsg::密文::密码
		String Msg_to = edit_towho.getText().toString();
		String Msg_content = edit_secretContent.getText().toString();
		String Msg_pwd = edit_secretPwd.getText().toString();
		if ( TextUtils.isEmpty(Msg_content) || TextUtils.isEmpty(Msg_pwd) || TextUtils.isEmpty(Msg_to) ) {
			DoThings.DisplayToast(SendSecretMsg.this, getString(R.string.alarm_sendsecretmsgnull));
		} else {
			String cypher_content = SimpleCrypto.encrypt(Msg_pwd, Msg_content);
			Msg_pwd = SimpleCrypto.encrypt(UsedVerbs.str_Key, Msg_pwd);
			String command = "ReceiveMsg::" + cypher_content + "::" + Msg_pwd;
			// String cypher = SimpleCrypto.encrypt(UsedVerbs.str_Key, command);
			DoThings.sendMsg(SendSecretMsg.this, Msg_to, command);
			DoThings.DisplayToast(SendSecretMsg.this, getString(R.string.msg_sendsecretmsg_success));
			SendSecretMsg.this.finish();
		}

	}
}
