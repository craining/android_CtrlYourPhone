package com.craining.book.CtrlYourPhone;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.craining.book.DataBase.BlackDB;
import com.craining.book.DataBase.UnderCtrlDB;
import com.craining.book.DoThings.DoThings;
import com.craining.book.DoThings.SimpleCrypto;
import com.craining.book.DoThings.UsedVerbs;

public class ManageUnderCtrl extends Activity {

	private ListView list = null;
	private TextView text_showtip = null;

	public final static int CONTEXTMENU_DEL = 100;
	public final static int CONTEXTMENU_CHANGE = 101;

	private int int_longClickedId = 0;
	private int underCtrlCounts = 0;

	private UnderCtrlDB db_underCtrl;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_manage);
		setTitle(getString(R.string.underctrl_title));
		list = (ListView) findViewById(R.id.ListView_manager);
		text_showtip = (TextView) this.findViewById(R.id.text_showHeadTip);

		db_underCtrl = new UnderCtrlDB(this);
		db_underCtrl.open();
		updateList();

		if ( UsedVerbs.showAddDlg ) {
			showAddOrChangeDlg(this, false);
			UsedVerbs.showAddDlg = false;
		}

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				UsedVerbs.nowCtrlingId = arg2;
				DoThings.UpdateNowCtrling();
				returnToCtrl();
			}
		});
		list.setOnItemLongClickListener(new OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Log.e("", "LongClicked" + arg2);
				int_longClickedId = arg2;
				return false;
			}
		});
		list.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
				menu.setHeaderTitle(getString(R.string.underctrllist_contextmenutitle));
				menu.add(0, CONTEXTMENU_CHANGE, 0, getString(R.string.ctrl_edit));// �޸�
				menu.add(0, CONTEXTMENU_DEL, 0, getString(R.string.ctrl_del));// ɾ��
			}
		});
	}

	/**
	 * �˵�
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_manage, menu);
		super.onCreateOptionsMenu(menu);

		return true;
	}
	
	// �˵���Ӧ
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch( item.getItemId() ) {
			case R.id.menu_addunderctrl : {
				// ���
				showAddOrChangeDlg(this, false);
				break;
			}
			case R.id.menu_returnfrommanage : {
				// ����
				returnToCtrl();

				break;
			}
			default :
				break;
		}
		return false;
	}

	// �����˵���Ӧ����
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// setTitle("����˳����˵�����ĵ�" + item.getItemId() + "����Ŀ");

		switch( item.getItemId() ) {
			case CONTEXTMENU_CHANGE : {
				// �޸�
				showAddOrChangeDlg(this, true);
				break;
			}
			case CONTEXTMENU_DEL : {
				// ɾ��
				showDelDlg(this, "��ȷ��Ҫȡ���Դ��ܿ��ߵĿ�����");
				break;
			}
			default :
				break;
		}

		return super.onContextItemSelected(item);
	}

	private void updateList() {
		// ���ɶ�̬���飬��������
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		underCtrlCounts = UsedVerbs.name_underCtrl.size();
		for (int i = 0; i < underCtrlCounts; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			if ( i == UsedVerbs.nowCtrlingId ) {
				map.put("ItemImage", R.drawable.mark_underctrl);
			}
			map.put("ItemTitle", UsedVerbs.name_underCtrl.get(i));
			map.put("ItemText", UsedVerbs.numbers_underCtrl.get(i));

			listItem.add(map);
		}
		// ������������Item�Ͷ�̬�����Ӧ��Ԫ��
		SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem, R.layout.list_items, new String[]{"ItemImage", "ItemTitle", "ItemText"}, new int[]{R.id.ItemImage, R.id.ItemTitle, R.id.ItemText});

		list.setAdapter(listItemAdapter);
		// ��ʾtextView
		if ( underCtrlCounts == 0 ) {
			text_showtip.setText(getString(R.string.underctrllistnull_tip));
		} else {
			text_showtip.setText(getString(R.string.underctrllist_tip));
		}
		if ( underCtrlCounts < 2 ) {
			showTip();
		}
	}

	public void showAddOrChangeDlg(Context context, final boolean changeEitherAdd) {
		LayoutInflater factory = LayoutInflater.from(context);
		// �õ��Զ���Ի���
		final View testDialogView = factory.inflate(R.layout.dlg_addunderctrl_addblacknum_addemail, null);
		final EditText edittext_number = (EditText) testDialogView.findViewById(R.id.edittext_number);
		final EditText edittext_name = (EditText) testDialogView.findViewById(R.id.edittext_name);
		final EditText edittext_ctrlpwd = (EditText) testDialogView.findViewById(R.id.edittext_ctrlpwd);
		edittext_ctrlpwd.setVisibility(View.VISIBLE);
		String title = new String(getString(R.string.addunderctrl_title));
		String btn_text1 = new String(getString(R.string.ctrl_add));
		if ( changeEitherAdd ) {
			edittext_number.setFocusable(false);
			title = getString(R.string.underctrllist_edit);
			btn_text1 = getString(R.string.ctrl_edit);
			edittext_name.setText(UsedVerbs.name_underCtrl.get(int_longClickedId));
			edittext_number.setHint(UsedVerbs.numbers_underCtrl.get(int_longClickedId) + getString(R.string.underctrl_cannotedit));
			edittext_ctrlpwd.setText(UsedVerbs.pwd_underCtrl.get(int_longClickedId));
		}

		// �����Ի���
		AlertDialog.Builder testDialog = new AlertDialog.Builder(context);
		testDialog.setTitle(title);
		testDialog.setView(testDialogView);
		testDialog.setPositiveButton(btn_text1, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String get_name = edittext_name.getText().toString();
				String get_addnum = edittext_number.getText().toString();
				String get_ctrlpwd = edittext_ctrlpwd.getText().toString();

				if ( changeEitherAdd ) {
					if ( TextUtils.isEmpty(get_name) || TextUtils.isEmpty(get_ctrlpwd) ) {
						DoThings.DisplayToast(ManageUnderCtrl.this, getString(R.string.ctrl_namenull));// ��������Ϊ��
						showAddOrChangeDlg(ManageUnderCtrl.this, true);
					} else {
						setProgressBarIndeterminateVisibility(true);
						// �޸Ĳ���
						// ɾ��ԭ��¼
						db_underCtrl.deleteNum(UsedVerbs.numbers_underCtrl.get(int_longClickedId));
						Log.e("DeletePre", UsedVerbs.numbers_underCtrl.get(int_longClickedId));
						// ����¼�¼
						db_underCtrl.insertData(UsedVerbs.numbers_underCtrl.get(int_longClickedId), get_name, get_ctrlpwd);
						UsedVerbs.numbers_underCtrl = db_underCtrl.getColumnUnderCtrlVector(UnderCtrlDB.KEY_NUM);
						UsedVerbs.name_underCtrl = db_underCtrl.getColumnUnderCtrlVector(UnderCtrlDB.KEY_NAME);
						UsedVerbs.pwd_underCtrl = db_underCtrl.getColumnUnderCtrlVector(UnderCtrlDB.KEY_CTRLPWD);
						// ����޸ĵ������ڿ��Ƶ���,��Ԥ�������Ϊ��һ����
						if ( UsedVerbs.numbers_underCtrl.get(int_longClickedId).equals(UsedVerbs.nowCtrlingNum) ) {
							UsedVerbs.nowCtrlingId = 0;
							DoThings.UpdateNowCtrling();
						}
						DoThings.DisplayToast(ManageUnderCtrl.this, getString(R.string.ctrl_changeSuccess));
						restartThis();
					}

				} else {
					if ( TextUtils.isEmpty(get_name) || TextUtils.isEmpty(get_addnum) || TextUtils.isEmpty(get_ctrlpwd) ) {
						DoThings.DisplayToast(ManageUnderCtrl.this, getString(R.string.ctrl_namenumnull));// �����ͺ��벻��Ϊ�գ�
						showAddOrChangeDlg(ManageUnderCtrl.this, false);
					} else if ( sameNumRecord(get_addnum) ) {
						// �������ͬ��¼
						DoThings.DisplayToast(ManageUnderCtrl.this, getString(R.string.ctrl_numsame));// �����Ѿ����ڣ�
						// showAddOrChangeDlg(ManageUnderCtrl.this,
						// false);
					} else {
						setProgressBarIndeterminateVisibility(true);
						// ��Ӳ���
						if ( db_underCtrl.insertData(get_addnum, get_name, get_ctrlpwd) >= 0 ) {
							UsedVerbs.numbers_underCtrl = db_underCtrl.getColumnUnderCtrlVector(UnderCtrlDB.KEY_NUM);
							UsedVerbs.name_underCtrl = db_underCtrl.getColumnUnderCtrlVector(UnderCtrlDB.KEY_NAME);
							UsedVerbs.pwd_underCtrl = db_underCtrl.getColumnUnderCtrlVector(UnderCtrlDB.KEY_CTRLPWD);
							// �����ӳɹ�����������
							try {
								sendCommands(true, get_addnum, get_ctrlpwd);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								// ���ʧ�ܣ���ӱ������ݿ�������ɾ��
								db_underCtrl.deleteNum(get_addnum);
								UsedVerbs.numbers_underCtrl = db_underCtrl.getColumnUnderCtrlVector(UnderCtrlDB.KEY_NUM);
								UsedVerbs.name_underCtrl = db_underCtrl.getColumnUnderCtrlVector(UnderCtrlDB.KEY_NAME);
								UsedVerbs.pwd_underCtrl = db_underCtrl.getColumnUnderCtrlVector(UnderCtrlDB.KEY_CTRLPWD);
								DoThings.showAlarmDlg(ManageUnderCtrl.this, getString(R.string.ctrl_addfail));
								e.printStackTrace();
							}
							// ��Ӻ������û�ÿ����ߣ���ѡ��ǰΪ�����ߣ���������Ʋ���ҳ��
							// ���ڵ�һ��ʹ��ʱ����
							if ( UsedVerbs.nowCtrlingNum.equals("") ) {
								UsedVerbs.nowCtrlingId = 0;
								DoThings.UpdateNowCtrling();
								returnToCtrl();
							} else {
								restartThis();
							}
						} else {
							DoThings.showAlarmDlg(ManageUnderCtrl.this, getString(R.string.ctrl_addfail));
						}
					}
				}
			}

		});

		testDialog.setNegativeButton(getString(R.string.ctrl_cancle), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		})

		.create();
		testDialog.show();

	}

	private void showDelDlg(Context context, String content) {
		AlertDialog.Builder testDialog = new AlertDialog.Builder(context);
		testDialog.setTitle(getString(R.string.ctrl_alarm));
		testDialog.setMessage(content);
		testDialog.setPositiveButton(getString(R.string.ctrl_sure), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				setProgressBarIndeterminateVisibility(true);
				// ɾ����¼
				// ɾ���ܿ��ߺ����¼
				String srtToDelNum = UsedVerbs.numbers_underCtrl.get(int_longClickedId);
				String strToDelName = UsedVerbs.name_underCtrl.get(int_longClickedId);
				String strToDelPwd = UsedVerbs.pwd_underCtrl.get(int_longClickedId);
				if ( db_underCtrl.deleteNum(srtToDelNum) ) {
					// ���ɾ���ɹ�
					Log.e("Delete", UsedVerbs.numbers_underCtrl.get(int_longClickedId));
					// ɾ���ܿ��ߺ�������¼
					BlackDB db_blacknums = new BlackDB(ManageUnderCtrl.this);
					db_blacknums.open();
					db_blacknums.deleteNumOfOwner(srtToDelNum);
					db_blacknums.close();
					UsedVerbs.numbers_underCtrl = db_underCtrl.getColumnUnderCtrlVector(UnderCtrlDB.KEY_NUM);
					UsedVerbs.name_underCtrl = db_underCtrl.getColumnUnderCtrlVector(UnderCtrlDB.KEY_NAME);
					UsedVerbs.pwd_underCtrl = db_underCtrl.getColumnUnderCtrlVector(UnderCtrlDB.KEY_CTRLPWD);
					// // ���ܿ��߷���������׽�ſ�����
					// try {
					// sendCommands(false, srtToDelNum, strToDelName);
					// } catch (Exception e) {
					// // TODO Auto-generated catch block
					// // ���ʧ�ܣ���������ӵ��������ݿ�
					// db_underCtrl.insertData(srtToDelNum, strToDelName,
					// strToDelPwd);
					// UsedVerbs.numbers_underCtrl =
					// db_underCtrl.getColumnUnderCtrlVector(UnderCtrlDB.KEY_NUM);
					// UsedVerbs.name_underCtrl =
					// db_underCtrl.getColumnUnderCtrlVector(UnderCtrlDB.KEY_NAME);
					// UsedVerbs.pwd_underCtrl =
					// db_underCtrl.getColumnUnderCtrlVector(UnderCtrlDB.KEY_CTRLPWD);
					// DoThings.showAlarmDlg(ManageUnderCtrl.this,
					// getString(R.string.ctrl_delfail));
					// e.printStackTrace();
					// }

					// ���ɾ���ĺ����ڴ�ʱ���ڿ��Ƶĺ���֮ǰ����Ҫ���µ�ǰ�ܿ���
					if ( int_longClickedId < UsedVerbs.nowCtrlingId ) {
						UsedVerbs.nowCtrlingId = UsedVerbs.nowCtrlingId - 1;
						DoThings.UpdateNowCtrling();
					}
					// ���ɾ�����ǵ�ǰ���ڿ��Ƶģ���Ԥ�������Ϊ��һ����
					if ( int_longClickedId == UsedVerbs.nowCtrlingId && UsedVerbs.name_underCtrl.size() != 0 ) {
						UsedVerbs.nowCtrlingId = 0;
						DoThings.UpdateNowCtrling();
					}
					restartThis();
				} else {
					DoThings.showAlarmDlg(ManageUnderCtrl.this, getString(R.string.ctrl_delfail));
				}
			}
		});
		testDialog.setNegativeButton(getString(R.string.ctrl_cancle), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		})

		.create();
		testDialog.show();
	}

	private boolean sameNumRecord(String num) {
		for (int h = 0; h < underCtrlCounts; h++) {
			if ( UsedVerbs.numbers_underCtrl.get(h).equals(num) ) {
				return true;
			}
		}
		return false;
	}

	private void sendCommands(boolean add, String number, String pwd) throws Exception {
		// if ( add ) {
		// ����ܿ��ߵ�����
		// pwd::UnderCtrl-Add-craining@163.com
/*		
		String str_AddCommand = pwd + "::UnderCtrl-Add-" + UsedVerbs.host_email_address;
		String str_cypher1 = SimpleCrypto.encrypt(UsedVerbs.str_Key, str_AddCommand);
		DoThings.sendMsg(ManageUnderCtrl.this, number, str_cypher1);
		DoThings.DisplayToast(ManageUnderCtrl.this, getString(R.string.ctrl_addsuccess));
*/		
		// } else {
		// // ɾ���ܿ��ߵ�����
		// // hide:UnderCtrl-Del
		// String str_DelCommand = pwd + ":UnderCtrl-Del";
		// String str_cypher2 = SimpleCrypto.enCrypt(UsedVerbs.str_Key,
		// str_DelCommand);
		// DoThings.sendMsg(ManageUnderCtrl.this, number, str_cypher2);
		// DoThings.DisplayToast(ManageUnderCtrl.this,
		// getString(R.string.ctrl_delsuccess));
		// }
	}

	private void restartThis() {
		db_underCtrl.close();
		Intent intent = new Intent();
		intent.setClass(ManageUnderCtrl.this, ManageUnderCtrl.class);
		ManageUnderCtrl.this.finish();
		startActivity(intent);
	}

	private void returnToCtrl() {
		db_underCtrl.close();
		Intent i = new Intent();
		i.setClass(ManageUnderCtrl.this, CtrlYourPhone.class);
		ManageUnderCtrl.this.finish();
		startActivity(i);
	}

	private void showTip() {
		Animation myAnimation_ScalSmall = AnimationUtils.loadAnimation(this, R.anim.my_scale_action_small);
		TextView text_showTip = (TextView) this.findViewById(R.id.text_showTip);
		text_showTip.setText(getString(R.string.manageunderctrllist_tip));
		text_showTip.startAnimation(myAnimation_ScalSmall);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ( keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0 ) {
			returnToCtrl();
			return false;
		}

		return false;
	}

}
