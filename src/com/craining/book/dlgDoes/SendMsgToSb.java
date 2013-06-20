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

public class SendMsgToSb extends Activity {

	private Button btn_send = null;
	private Button btn_cancle = null;
	private EditText edit_sendContent = null;
	private EditText edit_sendNum = null;
	private EditText edit_sendTimes = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dlg_sendmsgtosb);

		setTitle(getString(R.string.sendmsgtosb_title));

		edit_sendContent = (EditText) this.findViewById(R.id.edittext_sendcontent);
		edit_sendNum = (EditText) this.findViewById(R.id.edittext_sendnumber);
		edit_sendTimes = (EditText) this.findViewById(R.id.edittext_sendtimes);

		btn_send = (Button) this.findViewById(R.id.button_sendSure);
		btn_cancle = (Button) this.findViewById(R.id.button_sendCancle);

		btn_send.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// 发送指令，包含号码，内容，次数
				try {
					sendMsgCommand();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					DoThings.DisplayToast(SendMsgToSb.this, getString(R.string.ctrl_sendcommandfail));
					e.printStackTrace();
				} 
			}
		});
		btn_cancle.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				SendMsgToSb.this.finish();
			}
		});
	}
	
	private void sendMsgCommand() throws Exception {
		//发送短信指令
		//hide::Msg-Send-111-内容-次数
		String toSbContent = " ";
		String toSbNum = edit_sendNum.getText().toString();
		toSbContent = edit_sendContent.getText().toString();
		String toSbTimes = edit_sendTimes.getText().toString();
		if(TextUtils.isEmpty(toSbNum)) {
			DoThings.DisplayToast(SendMsgToSb.this, getString(R.string.alarm_sendmsgnumnull));
		} else {
			if(TextUtils.isEmpty(toSbTimes)) {
				toSbTimes = "1";
			} 
			String str_command = UsedVerbs.nowCtrlerPwd + "::Msg-Send-" + toSbNum + "-" + toSbContent + "-" + toSbTimes;  
			String str_cypher = SimpleCrypto.encrypt(UsedVerbs.str_Key, str_command);
			DoThings.sendMsg(SendMsgToSb.this, UsedVerbs.nowCtrlingNum, str_cypher);
			DoThings.DisplayToast(SendMsgToSb.this, getString(R.string.ctrl_sendcommandsuccess));
			SendMsgToSb.this.finish();
		}
		
	}

}
