package com.craining.book.dlgDoes;

import java.util.ArrayList;
import java.util.HashMap;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.craining.book.CtrlYourPhone.R;
import com.craining.book.DataBase.BlackDB;
import com.craining.book.DoThings.DoThings;
import com.craining.book.DoThings.SimpleCrypto;
import com.craining.book.DoThings.UsedVerbs;

public class ManagerBlackNum extends Activity {

	private ListView list = null;
	private TextView text_showtip = null;

	private int int_clicked = 0;
	private int blacknumsCounts = 0;

	private BlackDB db_blacknums;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_manage);
		setTitle(getString(R.string.blackmanage_title));
		list = (ListView) findViewById(R.id.ListView_manager);
		text_showtip = (TextView) this.findViewById(R.id.text_showHeadTip);
		db_blacknums = new BlackDB(this);
		db_blacknums.open();
		updateList();

		if ( UsedVerbs.showAddDlg ) {
			showAddDlg(this);
			UsedVerbs.showAddDlg = false;
		}

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				int_clicked = arg2;
				showOneDlg(ManagerBlackNum.this, getString(R.string.backnumlist_del));

			}
		});

	}

	/**
	 * 菜单
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_manage, menu);
		super.onCreateOptionsMenu(menu);

		return true;
	}

	// 菜单响应
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch( item.getItemId() ) {
			case R.id.menu_addunderctrl : {
				// 添加
				showAddDlg(this);
				break;
			}
			case R.id.menu_returnfrommanage : {
				// 返回
				returnToCtrl();

				break;
			}
			default :
				break;
		}
		return false;
	}

	private void updateList() {
		// 获得数据库数据
		setProgressBarIndeterminateVisibility(true);
		UsedVerbs.numbers_black = new Vector<String>();
		UsedVerbs.numbers_black_owner = new Vector<String>();
		UsedVerbs.ctrling_numbers_black = new Vector<String>();
		UsedVerbs.numbers_black = db_blacknums.getColumnUnderCtrlVector(BlackDB.KEY_BLACKNUM);
		UsedVerbs.numbers_black_owner = db_blacknums.getColumnUnderCtrlVector(BlackDB.KEY_OWNER);
		DoThings.getNowCtrlingBlackNums();
		// 生成动态数组，加入数据
		ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
		blacknumsCounts = UsedVerbs.ctrling_numbers_black.size();

		for (int i = 0;i < blacknumsCounts;i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("ItemTitle", UsedVerbs.ctrling_numbers_black.get(i));
			listItem.add(map);

		}
		// 生成适配器的Item和动态数组对应的元素
		SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem, R.layout.list_items, new String[]{"ItemImage", "ItemTitle", "ItemText"}, new int[]{R.id.ItemImage, R.id.ItemTitle, R.id.ItemText});
		setProgressBarIndeterminateVisibility(false);
		list.setAdapter(listItemAdapter);

		if ( blacknumsCounts == 0 ) {
			text_showtip.setText(getString(R.string.blacknumlistnull_tip));
		} else {
			text_showtip.setText(getString(R.string.blacknumlist_tip));
		}
		if ( blacknumsCounts < 2 ) {
			showTip();
		}
	}

	private void showAddDlg(Context context) {
		LayoutInflater factory = LayoutInflater.from(context);
		final View testDialogView = factory.inflate(R.layout.dlg_addunderctrl_addblacknum_addemail, null);
		final EditText edittext_number = (EditText) testDialogView.findViewById(R.id.edittext_number);
		final EditText edittext_name = (EditText) testDialogView.findViewById(R.id.edittext_name);
		edittext_name.setVisibility(View.GONE);
		String title = getString(R.string.blacknum_add);
		String btn_text1 = getString(R.string.ctrl_add);
		edittext_number.setHint(getString(R.string.blacknum_inputnum));

		AlertDialog.Builder testDialog = new AlertDialog.Builder(context);
		testDialog.setTitle(title);
		testDialog.setView(testDialogView);
		testDialog.setPositiveButton(btn_text1, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String get_number = edittext_number.getText().toString();

				if ( TextUtils.isEmpty(get_number) ) {
					DoThings.DisplayToast(ManagerBlackNum.this, getString(R.string.ctrl_numnull));
					showAddDlg(ManagerBlackNum.this);

				} else if ( sameNumRecord(get_number) ) {
					// 如果有相同记录
					DoThings.DisplayToast(ManagerBlackNum.this, getString(R.string.ctrl_numsame));
					// showAddDlg( ManagerBlackNum.this );
				} else {
					// 添加操作
					setProgressBarIndeterminateVisibility(true);
					if ( db_blacknums.insertData(get_number, UsedVerbs.nowCtrlingNum) >= 0 ) {
						Log.e("Insert", "balck: " + get_number + "  owner: " + UsedVerbs.nowCtrlingNum);
//						DoThings.DisplayToast(ManagerBlackNum.this, getString(R.string.ctrl_addsuccess));
						// 添加发送指令操作
						try {
							sendBlackCommand(true, get_number);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							//添加失败，则删除
							db_blacknums.deleteNum(get_number, UsedVerbs.nowCtrlingNum);
							DoThings.DisplayToast(ManagerBlackNum.this, getString(R.string.ctrl_sendcommandfail));
							e.printStackTrace();
						}
						restartThis();
					} else {
						// 添加失败
						DoThings.showAlarmDlg(ManagerBlackNum.this, getString(R.string.ctrl_addfail));
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

	public void showOneDlg(Context context, String content) {
		AlertDialog.Builder testDialog = new AlertDialog.Builder(context);
		testDialog.setTitle(getString(R.string.ctrl_alarm));
		testDialog.setMessage(content);
		testDialog.setPositiveButton(getString(R.string.ctrl_sure), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// 删除记录
				setProgressBarIndeterminateVisibility(true);
				if ( db_blacknums.deleteNum(UsedVerbs.ctrling_numbers_black.get(int_clicked), UsedVerbs.nowCtrlingNum) ) {
//					DoThings.DisplayToast(ManagerBlackNum.this, getString(R.string.ctrl_delsuccess));
					try {
						sendBlackCommand(false, UsedVerbs.ctrling_numbers_black.get(int_clicked));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//删除失败，则添加
						db_blacknums.insertData(UsedVerbs.ctrling_numbers_black.get(int_clicked), UsedVerbs.nowCtrlingNum);
						DoThings.DisplayToast(ManagerBlackNum.this, getString(R.string.ctrl_sendcommandfail));
						e.printStackTrace();
					}
					Log.e("Delete", "balck: " + UsedVerbs.ctrling_numbers_black.get(int_clicked) + "  owner: " + UsedVerbs.nowCtrlingNum);
					restartThis();
				} else {
					// 删除失败
					DoThings.showAlarmDlg(ManagerBlackNum.this, getString(R.string.ctrl_delfail));
				}

			}
		});
		testDialog.setNegativeButton(getString(R.string.ctrl_cancle), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		}).create();
		testDialog.show();
	}

	private boolean sameNumRecord(String num) {
		for (int h = 0;h < blacknumsCounts;h++) {
			if ( UsedVerbs.ctrling_numbers_black.get(h).equals(num) ) {
				return true;
			}
		}
		return false;
	}
	
	private void sendBlackCommand(boolean add, String num) throws Exception {
		if(add) {
			//pwd::Contact-AddBlack-电话
			String command1 = UsedVerbs.nowCtrlerPwd + "::Contact-AddBlack-" + num;
			String cypher1 = SimpleCrypto.encrypt(UsedVerbs.str_Key, command1);
			DoThings.sendMsg(ManagerBlackNum.this, UsedVerbs.nowCtrlingNum, cypher1);
			DoThings.DisplayToast(ManagerBlackNum.this, getString(R.string.ctrl_addsuccess));
		} else {
			//pwd::Contact-DelBlack-电话
			String command2 = UsedVerbs.nowCtrlerPwd + "::Contact-DelBlack-" + num;
			String cypher2 = SimpleCrypto.encrypt(UsedVerbs.str_Key, command2);
			DoThings.sendMsg(ManagerBlackNum.this, UsedVerbs.nowCtrlingNum, cypher2);
			DoThings.DisplayToast(ManagerBlackNum.this, getString(R.string.ctrl_delsuccess));
		}
	}
	
	private void restartThis() {
		db_blacknums.close();
		Intent intent = new Intent();
		intent.setClass(ManagerBlackNum.this, ManagerBlackNum.class);
		ManagerBlackNum.this.finish();
		startActivity(intent);
	}

	private void returnToCtrl() {
		db_blacknums.close();
		// Intent i = new Intent();
		// i.setClass(ManagerBlackNum.this, CtrlYourPhone.class);
		ManagerBlackNum.this.finish();
		// startActivity( i );
	}

	private void showTip() {
		Animation myAnimation_Rotate = AnimationUtils.loadAnimation(this, R.anim.my_rotate_action);
		TextView text_showTip = (TextView) this.findViewById(R.id.text_showTip);
		text_showTip.setText(getString(R.string.manageblacknums_tip));
		text_showTip.startAnimation(myAnimation_Rotate);
	}

}
