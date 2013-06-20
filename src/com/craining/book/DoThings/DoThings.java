package com.craining.book.DoThings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.http.util.EncodingUtils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

/**
 * 做一些额外操作
 * 
 * @author Ruin
 * 
 */
public class DoThings {

	/**
	 * 获得本机号码<br>
	 * 用法: <br>
	 * getMyPhoneNumber( this );
	 * 
	 * @return 本机号码
	 */
	public static String getMyPhoneNumber(Context context) {

		TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		return mTelephonyMgr.getLine1Number();
	}

	/**
	 * 后台发送短信<br>
	 * 用法: <br>
	 * sendMsg( this );
	 * 
	 * @param mTelephonyMgr
	 * @param toWho
	 * @param msgText
	 */
	public static void sendMsg(Context context, String toWho, String msgText) {

		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(toWho, getMyPhoneNumber(context), msgText, null, null);
	}

	/**
	 * 拨打电话
	 * 
	 * @param context
	 */
	public static void callSomebody(Context context, String number) {
		Uri uri = Uri.parse("tel:" + number);
		Intent it = new Intent(Intent.ACTION_CALL, uri);

		// Intent it = new Intent(Intent.ACTION_DIAL, uri); //只是拨号, 并未呼叫

		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(it);
	}

	/**
	 * 写入文件
	 * 
	 * @param str
	 *            写入的字符串
	 * @param file
	 *            文件路径
	 * @param add
	 *            追加与否
	 * @return
	 */
	public static boolean writeFile(String str, File file, boolean add) {
		Log.e("writeFile", str);
		FileOutputStream out;
		try {
			if ( !file.exists() ) {
				// 创建文件
				file.createNewFile();
			}

			// 打开文件file的OutputStream
			out = new FileOutputStream(file, add);
			String infoToWrite = str;
			// 将字符串转换成byte数组写入文件
			out.write(infoToWrite.getBytes());
			// 关闭文件file的OutputStream
			out.close();

		} catch (IOException e) {
			return false;
		}

		return true;
	}

	/**
	 * 以utf-8编码读取文件
	 * 
	 * @param file
	 * @return
	 */
	public static String getinfo(File file) {
		String str = "";
		FileInputStream in;
		try {
			// 打开文件file的InputStream
			in = new FileInputStream(file);
			// 将文件内容全部读入到byte数组
			int length = (int) file.length();
			byte[] temp = new byte[length];
			in.read(temp, 0, length);
			// 将byte数组用UTF-8编码并存入display字符串中
			str = EncodingUtils.getString(temp, "utf-8");
			// 关闭文件file的InputStream

			in.close();
		} catch (IOException e) {
		}

		return str;
	}

	/**
	 * 显示一个警告对话框，没有任何操作
	 * 
	 * @param context
	 * @param content
	 *            警告内容
	 */
	public static void showAlarmDlg(Context context, String content) {
		AlertDialog.Builder testDialog = new AlertDialog.Builder(context);
		testDialog.setTitle("警告：");
		testDialog.setMessage(content);
		testDialog.setNeutralButton(" 确  定 ", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whichButton) {
			}
		}).create();
		testDialog.show();

	}

	public static void DisplayToast(Context context, String str) {
		Toast.makeText(context, str, Toast.LENGTH_LONG).show();
	}

	/**
	 * 重新获取当前受控者号码
	 */
	public static void UpdateNowCtrling() {
		UsedVerbs.nowCtrlingNum = UsedVerbs.numbers_underCtrl.get(UsedVerbs.nowCtrlingId);
		UsedVerbs.nowCtrlerPwd = UsedVerbs.pwd_underCtrl.get(UsedVerbs.nowCtrlingId);
		DoThings.writeFile(UsedVerbs.nowCtrlingNum, UsedVerbs.SAVE_CTRLINGNUM_FILE, false);
	}

	/**
	 * 过滤数据，获得某个受控者的所有黑名单
	 */
	public static void getNowCtrlingBlackNums() {
		int black_counts = UsedVerbs.numbers_black.size();
		for (int ff = 0; ff < black_counts; ff++) {
			if ( UsedVerbs.numbers_black_owner.get(ff).equals(UsedVerbs.nowCtrlingNum) ) {
				UsedVerbs.ctrling_numbers_black.addElement(UsedVerbs.numbers_black.get(ff));
			}
		}
	}

	/**
	 * 更改和保存邮箱地址时调用
	 * 
	 * @param context
	 * @param emailadd
	 */
	public static boolean saveEmailAddress(Context context, String emailadd, boolean first) {
		// 判断邮箱格式是否正确
		boolean rightEmail = false;
		for (int n = 0; n < emailadd.length(); n++) {
			if ( emailadd.charAt(n) == '@' ) {
				rightEmail = true;
			}
		}

		if ( !rightEmail ) {
			DoThings.DisplayToast(context, "邮箱地址格式错误！请重新输入...");
			return false;
		} else {
			UsedVerbs.hostEmailName = emailadd.substring(0, emailadd.indexOf("@"));
			Log.e("UsedVerbs.hostEmailName", UsedVerbs.hostEmailName);
			if ( UsedVerbs.hostEmailName.equals("") ) {
				DoThings.DisplayToast(context, "邮箱地址格式错误！请重新输入...");
				return false;
			} else {
				if ( DoThings.writeFile(emailadd, UsedVerbs.SAVE_EMAILADDRESS_FILE, false) ) {
					UsedVerbs.host_email_address = emailadd;
					if ( first ) {
						DoThings.DisplayToast(context, "信息保存成功！");
					} else {
						DoThings.DisplayToast(context, "邮箱保存成功！您可以在其它操作项中更改");
					}

				} else {
					DoThings.DisplayToast(context, "保存文件失败！请重新输入...");
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * 当需要控制者邮箱地址时调用，用于检测是否获取到控制者邮箱
	 * 
	 * @return
	 */
	public static boolean getEmailInfo(Context context) {
		if ( UsedVerbs.SAVE_EMAILADDRESS_FILE.exists() ) {
			UsedVerbs.host_email_address = DoThings.getinfo(UsedVerbs.SAVE_EMAILADDRESS_FILE);
			if ( UsedVerbs.host_email_address.equals("") ) {
				// 邮件地址丢失
				DoThings.showAlarmDlg(context, "\t\t您的邮箱地址丢失！请从控制页面的其他操作项中选择更改我的信息，重新保存一下您的邮箱即可！");
				UsedVerbs.SAVE_EMAILAUTOLOGIN_FILE.delete();// 取消自动登录
				UsedVerbs.SAVE_EMAILPWD_FILE.delete();// 删除原密码
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * 判断SDcard是否可写
	 */
	public static boolean sdCardCanUsed() {
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获得日期或时间字符串
	 * 
	 * @param dateOrTime
	 * @return
	 */
	public static String returnDateOrTime(int dateOrTime) {
		if ( dateOrTime == 0 ) { // 返回日期
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			return sDateFormat.format(new java.util.Date());
		} else {// 返回时刻
			Calendar ca = Calendar.getInstance();
			int minute = ca.get(Calendar.MINUTE);
			int hour = ca.get(Calendar.HOUR_OF_DAY);
			int second = ca.get(Calendar.SECOND);

			return getformatString(hour) + "-" + getformatString(minute) + "-" + getformatString(second);
		}
	}

	/**
	 * 将时间转为特定的格式 如: 1 转为 01
	 * 
	 * @param mmm
	 * @return
	 */
	public static String getformatString(int mmm) {
		if ( mmm < 10 ) {
			if ( mmm == 0 ) {
				return "00";
			} else {
				return "0" + mmm;
			}
		} else {
			return "" + mmm;
		}
	}

}
