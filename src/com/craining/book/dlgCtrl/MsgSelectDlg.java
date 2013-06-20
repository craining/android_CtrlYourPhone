package com.craining.book.dlgCtrl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.craining.book.CtrlYourPhone.R;
import com.craining.book.dlgDoes.SendMsgToSb;
import com.craining.book.dlgDoes.SendSecretMsg;

public class MsgSelectDlg extends Activity {

	private Button btn_SendMsg = null;
	private Button btn_sendSecretMsg = null;
	private Button btn_return = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.msg_selectdlg);
		setTitle(getString(R.string.msg_select_title));

		btn_SendMsg = (Button) this.findViewById(R.id.button_sendsb);
		btn_sendSecretMsg = (Button) this.findViewById(R.id.button_sendsecretmsg);
		btn_return = (Button) this.findViewById(R.id.button_msg_return);
		btn_SendMsg.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// ·¢ËÍ¶ÌÐÅ
				Intent i = new Intent();
				i.setClass(MsgSelectDlg.this, SendMsgToSb.class);
				MsgSelectDlg.this.finish();
				startActivity(i);

			}
		});
		btn_sendSecretMsg.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(MsgSelectDlg.this, SendSecretMsg.class);
				MsgSelectDlg.this.finish();
				startActivity(i);
			}
		});
		btn_return.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				MsgSelectDlg.this.finish();
			}
		});

	}

}
