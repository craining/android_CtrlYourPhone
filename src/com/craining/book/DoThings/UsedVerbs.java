package com.craining.book.DoThings;

import java.io.File;
import java.util.Vector;

public class UsedVerbs {

	public static final String PAKAGE_DATA_PATH = "data/data/com.craining.book.CtrlYourPhone/";
	public static final String SDCARD_EMAILFILE_DIR = "/Emails/";
	//�����Ƿ��Ѿ���¼������
	public static final File ALREADY_LOGINEMIAL = new File(PAKAGE_DATA_PATH + "logined/");
	//�����Ƿ���ʾ��ʾ
	public static final File EMAIL_HIDETIP = new File(PAKAGE_DATA_PATH + "emailtiphide/");
	// ���������ļ���
	public static File SAVE_EMAILADDRESS_FILE = new File(PAKAGE_DATA_PATH + "myEmailAddress.txt");
	public static File SAVE_EMAILPWD_FILE = new File(PAKAGE_DATA_PATH + "myEmailPwd.txt");
	public static File SAVE_EMAILAUTOLOGIN_FILE = new File(PAKAGE_DATA_PATH + "myAutoLogin.txt");
	public static String host_email_address = "";

	// ������ݿ��еı���������Ϣ
	public static Vector<String> numbers_underCtrl;
	public static Vector<String> name_underCtrl;
	public static Vector<String> pwd_underCtrl;
	public static Vector<String> numbers_black;
	public static Vector<String> numbers_black_owner;

	public static Vector<String> ctrling_numbers_black;

	// ��ŵ�ǰ�������ߵĺ���
	public static String nowCtrlingNum = "";
	public static int nowCtrlingId = 0;
	public static String nowCtrlerPwd = "";

	//
	public static File SAVE_CTRLINGNUM_FILE = new File(PAKAGE_DATA_PATH + "CtrlingNum.txt");

	// public static boolean returnFromAdd = false;
	public static boolean showAddDlg = false;

	public static String hostEmailName = "";
	public static String hostEmailPwd = "";
	public static String email_pop = "pop.163.com";
	public static String email_smtp = "smtp.163.com";

	// ��ȡ���ʼ���Ϣ
	// �ʼ����� �����������ˡ�ʱ�䡢���⡢���ݡ��Ƿ��������
	public static Vector<String> oneEmailContent;
	// ��ѯ�����������ʼ�
	public static Vector<String> getInfoEmailSub;
	public static Vector<String> getInfoEmailId;
	public static Vector<String> getInfoEmailTime;
	public static Vector<String> getInfoAttack;
	// �ռ����е������ʼ�
	public static Vector<String> getOtherEmailSub;
	public static Vector<String> getOtherEmailFrom;
	public static Vector<String> getOtherEmailId;
	public static Vector<String> getOtherAttack;
	// ���Ҫ��ȡ�ʼ����ݵ�ID
	public static String toGetEmailContentID = "";
	// Widget����ʱ��ʾ��text
	public static String strShowOnWidget = "";
	
//	//�洢��ǰ����
//	public final static  File SAVE_PHONENUMBER_FILE= new File(PAKAGE_DATA_PATH + "PhoneNo.txt");
//	public static  String str_myPhoneNo = ""; 
	
	//����
	public final static String str_Key = "OurAndroid";
}
