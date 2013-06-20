package com.craining.book.msgReceive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.craining.book.DoThings.SimpleCrypto;
import com.craining.book.DoThings.UsedVerbs;
import com.craining.book.dlgDoes.ReadMsg;

/**
 * �������ŵĽ���
 * 
 * @author Ruin
 * 
 */
public class CtrlMsgReceive extends BroadcastReceiver {

	String receiveMsg = "";
	private static final String TAG = "ReceivedMsg";

	@Override
	public void onReceive(Context context, Intent intent) {

		SmsMessage[] msg = null;

		if ( intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED") ) {

			Bundle bundle = intent.getExtras();
			if ( bundle != null ) {
				Object[] pdusObj = (Object[]) bundle.get("pdus");
				msg = new SmsMessage[pdusObj.length];
				int mmm = pdusObj.length;
				for (int i = 0; i < mmm; i++)
					msg[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
			}

			int msgLength = msg.length;
			for (int i = 0; i < msgLength; i++) {
				String msgTxt = msg[i].getMessageBody();

				// ��÷����˺���
				String getFromNum = "";
				for (SmsMessage currMsg : msg) {
					getFromNum = currMsg.getDisplayOriginatingAddress();
				}

				if ( !listenTheMsg(context, getFromNum, msgTxt) ) {
					// ɾ��������

					long id = getThreadId(context);
					Uri mUri = Uri.parse("content://sms/conversations/" + id);
					context.getContentResolver().delete(mUri, null, null);

				}

				abortBroadcast();
			}

		} else if ( intent.getAction().equals("android.provider.Telephony.SMS_SEND") ) {
			Log.i(TAG, "++++++++++++++++++++++++MSG______SEND");
		}

		return;
	}

	/**
	 * ����յ��Ķ���
	 */
	public boolean listenTheMsg(Context context, String from, String msgText) {

//		String getCommand = "";
//		// �Զ��Ž��н����ж��Ƿ�Ϊָ��
//		try {
//			getCommand = SimpleCrypto.decrypt(UsedVerbs.str_Key, msgText);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		Log.i(TAG, "++++++++++++++++++++++++++++++++++++++++============>" + getCommand);
		String[] msgBody = msgText.split("::");
		if ( msgBody[0].equals("ReceiveMsg") && msgBody.length == 3 ) {
			// �յ����ܶ���ReceiveMsg::����::����
			String cypherMsgContent = msgBody[1];
			String cypherMsgPwd = msgBody[2];
			try {
				cypherMsgPwd = SimpleCrypto.decrypt(UsedVerbs.str_Key, cypherMsgPwd);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Intent i = new Intent();
			i.setClass(context, ReadMsg.class);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Bundle mBundle = new Bundle();
			mBundle.putString("MSGCONTENT", cypherMsgContent);
			mBundle.putString("MSGPWD", cypherMsgPwd);
			mBundle.putString("FROM", from);
			i.putExtras(mBundle);
			context.startActivity(i);

			return false;
		} else if( msgBody[0].equals("GPS Text")){
			String getPosition = msgBody[1];
			String[] xOy = getPosition.split("-");
			String str_x = xOy[0];
			String str_y = xOy[1];
			Uri uri = Uri.parse("geo:"+ str_x + "," + str_y);     
			Intent it = new Intent(Intent.ACTION_VIEW, uri);   
			context.startActivity(it);

			
		}

		return true;
	}

	/**
	 * ���Ҫɾ�����ŵ�ID
	 * <p>
	 * ԭ��: ��Ϊ����ɾ������յ���һ�����ţ�������ֻ�谴ʱ����е���Ȼ���ȡ��һ�����ŵ��߳�ID������
	 * <p>
	 * �÷�: ��ReceiveMsg�е��õ�: <br>
	 * 
	 * @return
	 */
	public long getThreadId(Context context) {
		long threadId = 0;
		String SMS_READ_COLUMN = "read";
		String WHERE_CONDITION = SMS_READ_COLUMN + " = 0";
		String SORT_ORDER = "date DESC";
		int count = 0;

		Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), new String[]{"_id", "thread_id", "address", "person", "date", "body"}, WHERE_CONDITION, null, SORT_ORDER);

		if ( cursor != null ) {
			try {
				count = cursor.getCount();
				if ( count > 0 ) {
					cursor.moveToFirst();
					threadId = cursor.getLong(1);
				}
			} finally {
				cursor.close();
			}
		}
		Log.e(TAG, "++++++++++++++++++++++++++++++++++++++++============>msgID:   " + threadId);

		return threadId;
	}

}
