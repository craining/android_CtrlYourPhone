package com.craining.book.dlgDoes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.craining.book.CtrlYourPhone.R;
import com.craining.book.DoThings.DoThings;
import com.craining.book.DoThings.SimpleCrypto;

public class ReadMsg extends Activity {
	private TextView text_cyphercontent = null;
	private EditText edittext_msgpwd = null;
	private Button btn_encodeandredo = null;
	private Button btn_giveup = null;

	private String cyphercontent = "";
	private String msgpwd = "";
	private String fromwho = "";
	private boolean decodesuccess = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dlg_receivemsg);
		setTitle("��ȡ���ܶ��ţ�");

		Bundle bundle = getIntent().getExtras();
		cyphercontent = bundle.getString("MSGCONTENT");
		msgpwd = bundle.getString("MSGPWD");
		fromwho = bundle.getString("FROM");

		text_cyphercontent = (TextView) findViewById(R.id.text_showcyphercontent);
		edittext_msgpwd = (EditText) findViewById(R.id.edittext_msgpwd);
		btn_encodeandredo = (Button) findViewById(R.id.button_decodeandredo);
		btn_giveup = (Button) this.findViewById(R.id.button_giveupdecodemsg);
		text_cyphercontent.setText(cyphercontent);

		btn_encodeandredo.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if ( decodesuccess ) {
					//�ظ�
					SendSecretMsg.fromRelay = true;
					SendSecretMsg.relyToWho = fromwho;
					Intent i = new Intent();
					i.setClass(ReadMsg.this, SendSecretMsg.class);
					ReadMsg.this.finish();
					startActivity( i );
					
				} else {
					// ����
					String get_ctrlpwd = edittext_msgpwd.getText().toString();
					if ( TextUtils.isEmpty(get_ctrlpwd) ) {
						get_ctrlpwd = "";
					}
					if ( msgpwd.equals(get_ctrlpwd) ) {
						String clearcontent = "";
						try {
							edittext_msgpwd.setVisibility(View.GONE);
							clearcontent = SimpleCrypto.decrypt(get_ctrlpwd, cyphercontent);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							edittext_msgpwd.setText("");
							DoThings.DisplayToast(ReadMsg.this, "������ȷ��������ʧ�ܣ������ԡ�");
							e.printStackTrace();
						} finally {
							DoThings.DisplayToast(ReadMsg.this, "���ܳɹ���");
							decodesuccess = true;
							btn_encodeandredo.setText("�ظ�");
							text_cyphercontent.setText(clearcontent);
							btn_giveup.setText("�˳�");
						}

					} else {
						edittext_msgpwd.setText("");
						DoThings.DisplayToast(ReadMsg.this, "�������������...");
					}
				}
			}
		});

		btn_giveup.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				if ( !decodesuccess ) {
					showDelDlg(ReadMsg.this);
				} else {
					ReadMsg.this.finish();
				}
			}
		});

	}

	private void showDelDlg(Context context) {
		AlertDialog.Builder testDialog = new AlertDialog.Builder(context);
		testDialog.setTitle("���棺");
		testDialog.setMessage("\n\t\t����δ���ܳɹ���ȷ��Ҫ������");
		testDialog.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				ReadMsg.this.finish();
			}
		});
		testDialog.setNegativeButton(getString(R.string.ctrl_cancle), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		})

		.create();
		testDialog.show();
	}

}
