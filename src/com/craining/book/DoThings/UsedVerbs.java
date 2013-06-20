package com.craining.book.DoThings;

import java.io.File;
import java.util.Vector;

public class UsedVerbs {

	public static final String PAKAGE_DATA_PATH = "data/data/com.craining.book.CtrlYourPhone/";
	public static final String SDCARD_EMAILFILE_DIR = "/Emails/";
	//鉴别是否已经登录邮箱了
	public static final File ALREADY_LOGINEMIAL = new File(PAKAGE_DATA_PATH + "logined/");
	//鉴别是否显示提示
	public static final File EMAIL_HIDETIP = new File(PAKAGE_DATA_PATH + "emailtiphide/");
	// 保存邮箱文件的
	public static File SAVE_EMAILADDRESS_FILE = new File(PAKAGE_DATA_PATH + "myEmailAddress.txt");
	public static File SAVE_EMAILPWD_FILE = new File(PAKAGE_DATA_PATH + "myEmailPwd.txt");
	public static File SAVE_EMAILAUTOLOGIN_FILE = new File(PAKAGE_DATA_PATH + "myAutoLogin.txt");
	public static String host_email_address = "";

	// 存放数据库中的被控制者信息
	public static Vector<String> numbers_underCtrl;
	public static Vector<String> name_underCtrl;
	public static Vector<String> pwd_underCtrl;
	public static Vector<String> numbers_black;
	public static Vector<String> numbers_black_owner;

	public static Vector<String> ctrling_numbers_black;

	// 存放当前被控制者的号码
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

	// 获取的邮件信息
	// 邮件内容 包括：发件人、时间、主题、内容、是否包含附件
	public static Vector<String> oneEmailContent;
	// 查询操作反馈的邮件
	public static Vector<String> getInfoEmailSub;
	public static Vector<String> getInfoEmailId;
	public static Vector<String> getInfoEmailTime;
	public static Vector<String> getInfoAttack;
	// 收件箱中的其它邮件
	public static Vector<String> getOtherEmailSub;
	public static Vector<String> getOtherEmailFrom;
	public static Vector<String> getOtherEmailId;
	public static Vector<String> getOtherAttack;
	// 存放要读取邮件内容的ID
	public static String toGetEmailContentID = "";
	// Widget更新时显示的text
	public static String strShowOnWidget = "";
	
//	//存储当前号码
//	public final static  File SAVE_PHONENUMBER_FILE= new File(PAKAGE_DATA_PATH + "PhoneNo.txt");
//	public static  String str_myPhoneNo = ""; 
	
	//密码
	public final static String str_Key = "OurAndroid";
}
