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
import com.sun.mail.imap.Utility;

public class DialToSb extends Activity {

	private Button btn_send = null;
	private Button btn_cancle = null;
	private EditText edit_number = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dlg_dialtosb);
		setTitle(getString(R.string.dialtosb_title));

		btn_send = (Button) this.findViewById(R.id.button_dialSure);
		btn_cancle = (Button) this.findViewById(R.id.button_dialCancle);
		edit_number = (EditText) this.findViewById( R.id.edittext_dialnumber );

		btn_send.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				//·¢ËÍ²¦´òµç»°µÄÃüÁî
				try {
					sendDialCommand();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					DoThings.DisplayToast(DialToSb.this, "ÃüÁî·¢ËÍÊ§°Ü£¡");
				}
			}
		});
		btn_cancle.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				DialToSb.this.finish();
			}
		});

	}
	
	private void sendDialCommand() throws Exception {
		//·¢ËÍ²¦´òµç»°µÄÃüÁî
		//pwd:Call-Dial-1111111
		String dialno = edit_number.getText().toString();
		if( TextUtils.isEmpty(dialno) ) {
			DoThings.DisplayToast(DialToSb.this, getString(R.string.alarm_dialnumbernull));
		} else {
			String str_command = UsedVerbs.nowCtrlerPwd + "::Call-Dial-" + dialno;
			String str_cypher = SimpleCrypto.encrypt(UsedVerbs.str_Key, str_command);
			DoThings.sendMsg(DialToSb.this, UsedVerbs.nowCtrlingNum, str_cypher);
			DoThings.DisplayToast(DialToSb.this, getString(R.string.ctrl_sendcommandsuccess));
			DialToSb.this.finish();
		}
	}

}
