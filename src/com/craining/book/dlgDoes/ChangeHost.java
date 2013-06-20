package com.craining.book.dlgDoes;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.craining.book.CtrlYourPhone.R;
import com.craining.book.DoThings.DoThings;

public class ChangeHost extends Activity {

	private EditText edittext_getHostEmail = null;
	// private EditText edittext_getCtrlPwd = null;
//	private RadioButton radiobtn_changeOne = null;
//	private RadioButton radiobtn_changeAll = null;
	private Button btn_send = null;
	private Button btn_cancle = null;
//	private Thread threadTellAll;
//	private ProgressDialog waiteDialog;
//	
//	private static final int EMAIL_TELLALL = 1000;
//	private Handler tellAllHandler = new tellAllHandler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dlg_changehost);
		setTitle(getString(R.string.changehost_title));

		edittext_getHostEmail = (EditText) this.findViewById(R.id.edittext_hostemail);
		// edittext_getCtrlPwd = (EditText) this.findViewById(
		// R.id.edittext_ctrlpwd );

//		radiobtn_changeOne = (RadioButton) this.findViewById(R.id.RadioButton_tellthis);
//		radiobtn_changeOne.setChecked(true);
//		radiobtn_changeAll = (RadioButton) this.findViewById(R.id.RadioButton_tellall);

		btn_send = (Button) this.findViewById(R.id.button_changeSure);
		btn_cancle = (Button) this.findViewById(R.id.button_changeCancle);
		btn_send.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// 更改控制者信息
				String str_emailaddress = edittext_getHostEmail.getText().toString();
				if ( TextUtils.isEmpty(str_emailaddress) ) {
					DoThings.DisplayToast(ChangeHost.this, getString(R.string.ctrl_inputemailaddressnull));
				} else {
					if ( DoThings.saveEmailAddress(ChangeHost.this, str_emailaddress, false) ) {

//						if ( radiobtn_changeOne.isChecked() ) {
//							// 通知当前控制者
//							try {
//								changHost(true);
//							} catch (Exception e) {
//								// TODO Auto-generated catch block
//								DoThings.DisplayToast(ChangeHost.this, getString(R.string.changehost_tellthisfail));
//								e.printStackTrace();
//							}
//						} else if ( radiobtn_changeAll.isChecked() ) {
//							// 通知所有控制者
//							try {
//								changHost(false);
//							} catch (Exception e) {
//								// TODO Auto-generated catch block
//								DoThings.DisplayToast(ChangeHost.this, getString(R.string.changehosr_tellallfail));
//								e.printStackTrace();
//							}
//						}
						ChangeHost.this.finish();
					}
				}
			}
		});
		btn_cancle.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				ChangeHost.this.finish();
			}
		});
	}
//
//	private void changHost(boolean tellone) throws Exception {
//
//		if ( tellone ) {
//			sendChangHostCommand(UsedVerbs.nowCtrlingNum);
//			DoThings.DisplayToast(ChangeHost.this, getString(R.string.changehost_tellthissuccess));
//		} else {
//			
//			waiteDialog = new ProgressDialog(ChangeHost.this);
//			waiteDialog.setTitle(getString(R.string.changhost_waite));
//			waiteDialog.setMessage( getString(R.string.email_title_dlgcontent));
//			waiteDialog.show();
//
//			tellAllHandler.sendEmptyMessageDelayed(EMAIL_TELLALL, 0);
//		}
//	}
//
//	private void sendChangHostCommand(String toWho) throws Exception {
//		// hide:Other-Change-craining@163.com
//		String command = "hide:Other-Change-" + UsedVerbs.host_email_address;
//		String cypher = SimpleCrypto.enCrypt(UsedVerbs.str_Key, command);
//		DoThings.sendMsg(ChangeHost.this, toWho, cypher);
//	}
//
//	
//	 private class tellAllHandler extends Handler {
//	        @Override
//	            public void handleMessage(Message msg) {
//	            switch (msg.what) {
//	                case EMAIL_TELLALL: {
//	               //这里添加出发消息时的操作:
//	        			try {
//	        				int allUnderCtrl_count = UsedVerbs.numbers_underCtrl.size();
//	        				for (int i = 0; i < allUnderCtrl_count; i++) {
//	        					sendChangHostCommand(UsedVerbs.numbers_underCtrl.get(i));
//	        				}
//
//	        			} catch (Exception e) {
//	        				e.printStackTrace();
//	        			} finally {
//	        				waiteDialog.dismiss();
//	        				DoThings.DisplayToast(ChangeHost.this, getString(R.string.changehost_tellallsuccess));
//	        				ChangeHost.this.finish();
//	        			}
//	        			
//	                	break;
//	                }
//	                default:
//	                    break;
//	            }
//	        }
//	      } 

}
