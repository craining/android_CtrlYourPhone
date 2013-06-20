package com.craining.book.CtrlYourPhone;

import java.util.Vector;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.craining.book.DataBase.UnderCtrlDB;
import com.craining.book.DoThings.DoThings;
import com.craining.book.DoThings.UsedVerbs;
import com.craining.book.dlgCtrl.CallSelectDlg;
import com.craining.book.dlgCtrl.CheckSelectDlg;
import com.craining.book.dlgCtrl.ContactSelectDlg;
import com.craining.book.dlgCtrl.MsgSelectDlg;
import com.craining.book.dlgCtrl.OthersOperaDlg;

/**
 * 
 * @author ruin
 * 
 */
public class CtrlYourPhone extends Activity implements OnClickListener {

	private Button btn_Ctrl_Call = null;
	private Button btn_Ctrl_Msg = null;
	private Button btn_Ctrl_Contact = null;
	private Button btn_Ctrl_Check = null;
	private Button btn_changeHost = null;
	private Spinner spinner_SelectUnderCtrl = null;
	private TextView textview_ShowChrlWho = null;
	private TextView textview_ShowOpero = null;
	private Button btn_Manage = null;
	private Button btn_Exit = null;
	private Button btn_About = null;
	private Button btn_AddUnderCtrlOrAddEmailFile = null;
	private View view_1 = null;
	private Animation myAnimation_Translate = null;
	// ��ʾ�ı�����������
	private ArrayAdapter<String> adapter_AllUnderCtrls;

	private String[] name_AllUnderCtrls;

	private boolean emallFileNull = false;
	public static boolean jumptoCtrlmanage = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_ctrl);

		myAnimation_Translate = AnimationUtils.loadAnimation(this, R.anim.my_translate_action);

		btn_Ctrl_Msg = (Button) this.findViewById(R.id.button_msg);
		btn_Ctrl_Call = (Button) this.findViewById(R.id.button_cal);
		btn_Ctrl_Contact = (Button) this.findViewById(R.id.button_contact);
		btn_Ctrl_Check = (Button) this.findViewById(R.id.button_check);
		btn_changeHost = (Button) this.findViewById(R.id.button_otherdoes);

		btn_AddUnderCtrlOrAddEmailFile = (Button) this.findViewById(R.id.button_addUnderCtrlOrEmailFileNull);
		btn_Manage = (Button) findViewById(R.id.button_Manage);
		btn_About = (Button) this.findViewById(R.id.button_about);
		btn_Exit = (Button) findViewById(R.id.button_Exit);

		spinner_SelectUnderCtrl = (Spinner) findViewById(R.id.spinner_ShowUnderCtrls);
		textview_ShowOpero = (TextView) findViewById(R.id.text_ctrlopera);
		textview_ShowChrlWho = (TextView) findViewById(R.id.text_ShowCtrlWho);

		view_1 = (View) this.findViewById(R.id.view_1);

		btn_Ctrl_Call.setOnClickListener(this);
		btn_Ctrl_Msg.setOnClickListener(this);
		btn_Ctrl_Contact.setOnClickListener(this);
		btn_Ctrl_Check.setOnClickListener(this);
		btn_changeHost.setOnClickListener(this);
		btn_About.setOnClickListener(this);
		btn_AddUnderCtrlOrAddEmailFile.setOnClickListener(this);
		btn_Manage.setOnClickListener(this);
		btn_Exit.setOnClickListener(this);

		btn_AddUnderCtrlOrAddEmailFile.setVisibility(View.GONE);

		
		// �ж��Ƿ񱣴��������,��ת����һ������
		checkEmailFile();

		// ���Spinner�¼�����
		spinner_SelectUnderCtrl.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// ������ʾ��ǰѡ�����
				arg0.setVisibility(View.VISIBLE);
				UsedVerbs.nowCtrlingId = arg2;
				DoThings.UpdateNowCtrling();

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});
	}

	public void onClick(View Button) {

		switch( Button.getId() ) {
			case R.id.button_cal : {
				// �绰����
				Intent intent = new Intent();
				intent.setClass(CtrlYourPhone.this, CallSelectDlg.class);
				startActivity(intent);

				break;
			}

			case R.id.button_msg : {
				// ���ſ���
				Intent intent = new Intent();
				intent.setClass(CtrlYourPhone.this, MsgSelectDlg.class);
				startActivity(intent);

				break;
			}

			case R.id.button_contact : {
				// ��ϵ�˿���
				Intent intent = new Intent();
				intent.setClass(CtrlYourPhone.this, ContactSelectDlg.class);
				startActivity(intent);

				break;
			}

			case R.id.button_check : {
				// ��ѯ����
				Intent intent = new Intent();
				intent.setClass(CtrlYourPhone.this, CheckSelectDlg.class);
				startActivity(intent);

				break;
			}

			case R.id.button_otherdoes : {
				// ��������
				Intent i = new Intent();
				i.setClass(CtrlYourPhone.this, OthersOperaDlg.class);
				startActivity(i);

				break;

			}
			case R.id.button_addUnderCtrlOrEmailFileNull : {
				// ����ܿ���
				if ( emallFileNull ) {
					showSaveEmailFileDlg(CtrlYourPhone.this);
				} else {
					UsedVerbs.showAddDlg = true;
					Intent i = new Intent();
					i.setClass(CtrlYourPhone.this, ManageUnderCtrl.class);
					CtrlYourPhone.this.finish();
					startActivity(i);
				}
				break;
			}
			case R.id.button_Manage : {
				// ����
				setProgressBarIndeterminateVisibility(true);
				Intent i = new Intent();
				i.setClass(CtrlYourPhone.this, ManageUnderCtrl.class);
				CtrlYourPhone.this.finish();
				startActivity(i);

				break;
			}

			case R.id.button_about : {
				// ����

				break;
			}
			case R.id.button_Exit : {

				this.finish();
				android.os.Process.killProcess(android.os.Process.myPid());

				break;
			}
			default :
				break;
		}
	}

	/**
	 * ��ȡ���ݿ�
	 */
	private void updateSavedUnderCtrlInfo(Context context) {
		UnderCtrlDB db_underCtrl = new UnderCtrlDB(context);

		// if(!UsedVerbs.SAVE_CTRLING_NUM.exists()) {
		// db_underCtrl.insertData("11111", "����");
		// db_underCtrl.insertData("2222", "����");
		// db_underCtrl.insertData("3333", "С��");
		// }

		db_underCtrl.open();
		UsedVerbs.numbers_underCtrl = new Vector<String>();
		UsedVerbs.name_underCtrl = new Vector<String>();
		UsedVerbs.numbers_underCtrl = db_underCtrl.getColumnUnderCtrlVector(UnderCtrlDB.KEY_NUM);
		UsedVerbs.name_underCtrl = db_underCtrl.getColumnUnderCtrlVector(UnderCtrlDB.KEY_NAME);
		UsedVerbs.pwd_underCtrl = db_underCtrl.getColumnUnderCtrlVector(UnderCtrlDB.KEY_CTRLPWD);
		db_underCtrl.close();
		//
		// for(int nn=0; nn<UsedVerbs.name_underCtrl.size(); nn++) {
		// Log.e("", UsedVerbs.name_underCtrl.get(nn));
		// Log.e("", UsedVerbs.numbers_underCtrl.get(nn));
		// }
	}

	/**
	 * Ϊ�ַ����鸳ֵ������ʾ�������ߵ�Spinner�б�
	 */
	private void getUnderCtrlNames() {
		int nums = UsedVerbs.name_underCtrl.size();
		if ( nums == 0 ) {
			btn_Ctrl_Msg.setVisibility(View.GONE);
			btn_Ctrl_Call.setVisibility(View.GONE);
			btn_Ctrl_Contact.setVisibility(View.GONE);
			btn_Ctrl_Check.setVisibility(View.GONE);
			btn_changeHost.setVisibility(View.GONE);
			spinner_SelectUnderCtrl.setVisibility(View.GONE);
			textview_ShowOpero.setVisibility(View.GONE);
			textview_ShowChrlWho.setVisibility(View.GONE);
			view_1.setVisibility(View.GONE);
			btn_AddUnderCtrlOrAddEmailFile.setVisibility(View.VISIBLE);
			btn_AddUnderCtrlOrAddEmailFile.setFocusable(false);
			btn_AddUnderCtrlOrAddEmailFile.setFocusable(true);
			btn_AddUnderCtrlOrAddEmailFile.startAnimation(myAnimation_Translate);
		} else {

			// ���û�еõ���ǰ�����ߺ��룬����ϴ��˳�ǰ���Ƶ��˵ĺ���
			if ( UsedVerbs.SAVE_CTRLINGNUM_FILE.exists() && UsedVerbs.nowCtrlingNum.equals("") ) {
				UsedVerbs.nowCtrlingNum = DoThings.getinfo(UsedVerbs.SAVE_CTRLINGNUM_FILE);
				for (int j = 0;j < nums;j++) {
					if ( UsedVerbs.nowCtrlingNum.equals(UsedVerbs.numbers_underCtrl.get(j)) ) {
						UsedVerbs.nowCtrlingId = j;
						UsedVerbs.nowCtrlerPwd = UsedVerbs.pwd_underCtrl.get(j);
						break;
					}
				}
			} else {
				DoThings.UpdateNowCtrling();
			}

			name_AllUnderCtrls = new String[nums];
			for (int i = 0;i < nums;i++) {
				name_AllUnderCtrls[i] = UsedVerbs.name_underCtrl.get(i);

			}

			// ����ѡ������ArrayAdapter����
			adapter_AllUnderCtrls = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, name_AllUnderCtrls);

			// ���������б�ķ��
			adapter_AllUnderCtrls.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			// ��adapter��ӵ�m_Spinner��
			spinner_SelectUnderCtrl.setAdapter(adapter_AllUnderCtrls);

			spinner_SelectUnderCtrl.setSelection(UsedVerbs.nowCtrlingId);
		
			Animation myAnimation_ScalSmall = AnimationUtils.loadAnimation(this, R.anim.my_alpha_action);
			spinner_SelectUnderCtrl.startAnimation(myAnimation_ScalSmall);
		}

	}

	private void checkEmailFile() {
		if ( !UsedVerbs.SAVE_EMAILADDRESS_FILE.exists() ) {
			emallFileNull = true;
			btn_Ctrl_Msg.setVisibility(View.GONE);
			btn_Ctrl_Call.setVisibility(View.GONE);
			btn_Ctrl_Contact.setVisibility(View.GONE);
			btn_Ctrl_Check.setVisibility(View.GONE);
			btn_changeHost.setVisibility(View.GONE);
			spinner_SelectUnderCtrl.setVisibility(View.GONE);
			textview_ShowOpero.setVisibility(View.GONE);
			textview_ShowChrlWho.setVisibility(View.GONE);
			view_1.setVisibility(View.GONE);

			btn_AddUnderCtrlOrAddEmailFile.setVisibility(View.VISIBLE);
			btn_AddUnderCtrlOrAddEmailFile.setText(getString(R.string.main_tip_nullemailfile));
			btn_AddUnderCtrlOrAddEmailFile.startAnimation(myAnimation_Translate);

		} else {
			if ( UsedVerbs.host_email_address.equals("") ) {
				// ���email��ַ
				UsedVerbs.host_email_address = DoThings.getinfo(UsedVerbs.SAVE_EMAILADDRESS_FILE);
			}
			Log.e("UsedVerbs.hostEmailName", UsedVerbs.host_email_address);
			// ��ȡ���ݿ��ñ����Ƶ���ϵ��
			// ���Ϊ��������ʾ��ϢΪ�� R.string.main_tip_null
			updateSavedUnderCtrlInfo(CtrlYourPhone.this);
			getUnderCtrlNames();
		}
	}

	private void showSaveEmailFileDlg(Context context) {
		LayoutInflater factory = LayoutInflater.from(context);
		// �õ��Զ���Ի���
		final View testDialogView = factory.inflate(R.layout.dlg_addunderctrl_addblacknum_addemail, null);
		final EditText edittext_number = (EditText) testDialogView.findViewById(R.id.edittext_number);
		final EditText edittext_emailaddress = (EditText) testDialogView.findViewById(R.id.edittext_name);
		edittext_number.setVisibility(View.GONE);
		String title = new String(getString(R.string.ctrl_saveemailtitle));
		String btn_text1 = new String(getString(R.string.ctrl_save));
		edittext_emailaddress.setHint(getString(R.string.ctrl_inputemailaddress));
		// �����Ի���
		AlertDialog.Builder testDialog = new AlertDialog.Builder(context);
		testDialog.setTitle(title);
		testDialog.setView(testDialogView);
		testDialog.setPositiveButton(btn_text1, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String str_emailaddress = edittext_emailaddress.getText().toString();
				if ( TextUtils.isEmpty(str_emailaddress) ) {
					DoThings.DisplayToast(CtrlYourPhone.this, "�����ַ����Ϊ�գ�");
					showSaveEmailFileDlg(CtrlYourPhone.this);
				} else {
					// �������ַд���ļ�
					if ( DoThings.saveEmailAddress(CtrlYourPhone.this, str_emailaddress, false) ) {
						restartThis();
					} else {
						showSaveEmailFileDlg(CtrlYourPhone.this);
					}
				}
			}
		});
		testDialog.setNegativeButton(getString(R.string.ctrl_cancle), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		}).create();
		testDialog.show();
	}

	private void restartThis() {
		Intent i = new Intent();
		i.setClass(CtrlYourPhone.this, CtrlYourPhone.class);
		CtrlYourPhone.this.finish();
		startActivity(i);
	}
	
}
