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
 * ��һЩ�������
 * 
 * @author Ruin
 * 
 */
public class DoThings {

	/**
	 * ��ñ�������<br>
	 * �÷�: <br>
	 * getMyPhoneNumber( this );
	 * 
	 * @return ��������
	 */
	public static String getMyPhoneNumber(Context context) {

		TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		return mTelephonyMgr.getLine1Number();
	}

	/**
	 * ��̨���Ͷ���<br>
	 * �÷�: <br>
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
	 * ����绰
	 * 
	 * @param context
	 */
	public static void callSomebody(Context context, String number) {
		Uri uri = Uri.parse("tel:" + number);
		Intent it = new Intent(Intent.ACTION_CALL, uri);

		// Intent it = new Intent(Intent.ACTION_DIAL, uri); //ֻ�ǲ���, ��δ����

		it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(it);
	}

	/**
	 * д���ļ�
	 * 
	 * @param str
	 *            д����ַ���
	 * @param file
	 *            �ļ�·��
	 * @param add
	 *            ׷�����
	 * @return
	 */
	public static boolean writeFile(String str, File file, boolean add) {
		Log.e("writeFile", str);
		FileOutputStream out;
		try {
			if ( !file.exists() ) {
				// �����ļ�
				file.createNewFile();
			}

			// ���ļ�file��OutputStream
			out = new FileOutputStream(file, add);
			String infoToWrite = str;
			// ���ַ���ת����byte����д���ļ�
			out.write(infoToWrite.getBytes());
			// �ر��ļ�file��OutputStream
			out.close();

		} catch (IOException e) {
			return false;
		}

		return true;
	}

	/**
	 * ��utf-8�����ȡ�ļ�
	 * 
	 * @param file
	 * @return
	 */
	public static String getinfo(File file) {
		String str = "";
		FileInputStream in;
		try {
			// ���ļ�file��InputStream
			in = new FileInputStream(file);
			// ���ļ�����ȫ�����뵽byte����
			int length = (int) file.length();
			byte[] temp = new byte[length];
			in.read(temp, 0, length);
			// ��byte������UTF-8���벢����display�ַ�����
			str = EncodingUtils.getString(temp, "utf-8");
			// �ر��ļ�file��InputStream

			in.close();
		} catch (IOException e) {
		}

		return str;
	}

	/**
	 * ��ʾһ������Ի���û���κβ���
	 * 
	 * @param context
	 * @param content
	 *            ��������
	 */
	public static void showAlarmDlg(Context context, String content) {
		AlertDialog.Builder testDialog = new AlertDialog.Builder(context);
		testDialog.setTitle("���棺");
		testDialog.setMessage(content);
		testDialog.setNeutralButton(" ȷ  �� ", new DialogInterface.OnClickListener() {
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
	 * ���»�ȡ��ǰ�ܿ��ߺ���
	 */
	public static void UpdateNowCtrling() {
		UsedVerbs.nowCtrlingNum = UsedVerbs.numbers_underCtrl.get(UsedVerbs.nowCtrlingId);
		UsedVerbs.nowCtrlerPwd = UsedVerbs.pwd_underCtrl.get(UsedVerbs.nowCtrlingId);
		DoThings.writeFile(UsedVerbs.nowCtrlingNum, UsedVerbs.SAVE_CTRLINGNUM_FILE, false);
	}

	/**
	 * �������ݣ����ĳ���ܿ��ߵ����к�����
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
	 * ���ĺͱ��������ַʱ����
	 * 
	 * @param context
	 * @param emailadd
	 */
	public static boolean saveEmailAddress(Context context, String emailadd, boolean first) {
		// �ж������ʽ�Ƿ���ȷ
		boolean rightEmail = false;
		for (int n = 0; n < emailadd.length(); n++) {
			if ( emailadd.charAt(n) == '@' ) {
				rightEmail = true;
			}
		}

		if ( !rightEmail ) {
			DoThings.DisplayToast(context, "�����ַ��ʽ��������������...");
			return false;
		} else {
			UsedVerbs.hostEmailName = emailadd.substring(0, emailadd.indexOf("@"));
			Log.e("UsedVerbs.hostEmailName", UsedVerbs.hostEmailName);
			if ( UsedVerbs.hostEmailName.equals("") ) {
				DoThings.DisplayToast(context, "�����ַ��ʽ��������������...");
				return false;
			} else {
				if ( DoThings.writeFile(emailadd, UsedVerbs.SAVE_EMAILADDRESS_FILE, false) ) {
					UsedVerbs.host_email_address = emailadd;
					if ( first ) {
						DoThings.DisplayToast(context, "��Ϣ����ɹ���");
					} else {
						DoThings.DisplayToast(context, "���䱣��ɹ����������������������и���");
					}

				} else {
					DoThings.DisplayToast(context, "�����ļ�ʧ�ܣ�����������...");
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * ����Ҫ�����������ַʱ���ã����ڼ���Ƿ��ȡ������������
	 * 
	 * @return
	 */
	public static boolean getEmailInfo(Context context) {
		if ( UsedVerbs.SAVE_EMAILADDRESS_FILE.exists() ) {
			UsedVerbs.host_email_address = DoThings.getinfo(UsedVerbs.SAVE_EMAILADDRESS_FILE);
			if ( UsedVerbs.host_email_address.equals("") ) {
				// �ʼ���ַ��ʧ
				DoThings.showAlarmDlg(context, "\t\t���������ַ��ʧ����ӿ���ҳ���������������ѡ������ҵ���Ϣ�����±���һ���������伴�ɣ�");
				UsedVerbs.SAVE_EMAILAUTOLOGIN_FILE.delete();// ȡ���Զ���¼
				UsedVerbs.SAVE_EMAILPWD_FILE.delete();// ɾ��ԭ����
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	/**
	 * �ж�SDcard�Ƿ��д
	 */
	public static boolean sdCardCanUsed() {
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * ������ڻ�ʱ���ַ���
	 * 
	 * @param dateOrTime
	 * @return
	 */
	public static String returnDateOrTime(int dateOrTime) {
		if ( dateOrTime == 0 ) { // ��������
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			return sDateFormat.format(new java.util.Date());
		} else {// ����ʱ��
			Calendar ca = Calendar.getInstance();
			int minute = ca.get(Calendar.MINUTE);
			int hour = ca.get(Calendar.HOUR_OF_DAY);
			int second = ca.get(Calendar.SECOND);

			return getformatString(hour) + "-" + getformatString(minute) + "-" + getformatString(second);
		}
	}

	/**
	 * ��ʱ��תΪ�ض��ĸ�ʽ ��: 1 תΪ 01
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
