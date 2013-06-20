package com.craining.book.email;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import android.os.Environment;
import android.util.Log;

import com.craining.book.DoThings.UsedVerbs;

public class ReceiveImplement {
	private MimeMessage mimeMessage = null;
	private String saveAttachPath = "";// 附件下载后的存放目录
	private StringBuffer bodytext = new StringBuffer();

	public static String[] args;
	public static String subjectview;

	public Store store;
	// 存放邮件内容的StringBuffer对象
	private String dateformat = "yy-MM-dd　HH:mm";// 默认的日前显示格式

	/**
	 * 　*　构造函数,初始化一个MimeMessage对象 　
	 */
	public ReceiveImplement() {
	}

	public ReceiveImplement(MimeMessage mimeMessage) {
		this.mimeMessage = mimeMessage;
	}

	public void setMimeMessage(MimeMessage mimeMessage) {
		this.mimeMessage = mimeMessage;
	}

	/**
	 * 　*　获得发件人的地址和姓名 　
	 */
	public String getFrom1() throws Exception {
		InternetAddress address[] = (InternetAddress[]) mimeMessage.getFrom();
		String from = address[0].getAddress();
		if ( from == null ) {
			from = "";
		}

		String personal = address[0].getPersonal();
		if ( personal == null ) {
			personal = "";
		}
		String fromaddr = personal + "<" + from + ">";
		return fromaddr;
	}

	/**
	 * 　*　获得邮件的收件人，抄送，和密送的地址和姓名，根据所传递的参数的不同
	 * 　*　"to"----收件人　"cc"---抄送人地址　"bcc"---密送人地址 　
	 * 
	 * @throws Exception
	 */
	public String getMailAddress(String type) {
		String mailaddr = "";
		try {
			String addtype = type.toUpperCase();
			InternetAddress[] address = null;
			if ( addtype.equals("TO") || addtype.equals("CC") || addtype.equals("BBC") ) {
				if ( addtype.equals("TO") ) {
					address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.TO);
				} else if ( addtype.equals("CC") ) {
					address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.CC);
				} else {
					address = (InternetAddress[]) mimeMessage.getRecipients(Message.RecipientType.BCC);
				}
				if ( address != null ) {
					for (int i = 0; i < address.length; i++) {
						String email = address[i].getAddress();
						if ( email == null )
							email = "";
						else {
							email = MimeUtility.decodeText(email);
						}
						String personal = address[i].getPersonal();
						if ( personal == null )
							personal = "";
						else {
							personal = MimeUtility.decodeText(personal);
						}
						String compositeto = personal + "<" + email + ">";
						mailaddr += "," + compositeto;
					}
					mailaddr = mailaddr.substring(1);
				}
			} else {
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return mailaddr;
	}

	/**
	 * 　　*　获得邮件主题 　　
	 */
	public String getSubject() {
		String subject = "";
		try {
			subject = MimeUtility.decodeText(mimeMessage.getSubject());
			if ( subject == null )
				subject = "";
		} catch (Exception e) {
			// TODO: handle exception
		}
		return subject;
	}

	/**
	 * 　　*　获得邮件发送日期 　　
	 */
	public String getSendDate() throws Exception {
		Date senddate = mimeMessage.getSentDate();
		SimpleDateFormat format = new SimpleDateFormat(dateformat);
		return format.format(senddate);
	}

	/**
	 * 　　*　解析邮件，把得到的邮件内容保存到一个StringBuffer对象中，解析邮件
	 * 　　*　主要是根据MimeType类型的不同执行不同的操作，一步一步的解析 　　
	 */
	public void getMailContent(Part part) throws Exception {
		String contenttype = part.getContentType();
		int nameindex = contenttype.indexOf("name");
		boolean conname = false;
		if ( nameindex != -1 )
			conname = true;
		if ( part.isMimeType("text/plain") && !conname ) {
			bodytext.append((String) part.getContent());
		} else if ( part.isMimeType("text/html") && !conname ) {
			bodytext.append((String) part.getContent());
		} else if ( part.isMimeType("multipart/*") ) {
			Multipart multipart = (Multipart) part.getContent();
			int counts = multipart.getCount();
			for (int i = 0; i < counts; i++) {
				getMailContent(multipart.getBodyPart(i));
			}
		} else if ( part.isMimeType("message/rfc822") ) {
			getMailContent((Part) part.getContent());
		} else {
		}
	}

	/**
	 * 　*　获得邮件正文内容 　　
	 */
	public String getBodyText() {
		return bodytext.toString();
	}

	/**
	 * 　　*　判断此邮件是否需要回执，如果需要回执返回"true",否则返回"false" 　　
	 * 
	 * @throws MessagingException
	 */
	public boolean getReplySign() throws MessagingException {
		boolean replysign = false;
		String needreply[] = mimeMessage.getHeader("Disposition-Notification-To");
		if ( needreply != null ) {
			replysign = true;
		}
		return replysign;
	}

	/**
	 * 　获得此邮件的Message-ID 　　
	 * 
	 * @throws MessagingException
	 */
	public String getMessageId() throws MessagingException {
		return mimeMessage.getMessageID();
	}

	/**
	 * 　*　【判断此邮件是否已读，如果未读返回返回false,反之返回true】 　　
	 * 
	 * @throws MessagingException
	 */
	public boolean isNew() throws MessagingException {
		boolean isnew = false;
		Flags flags = ((Message) mimeMessage).getFlags();
		Flags.Flag[] flag = flags.getSystemFlags();
		for (int i = 0; i < flag.length; i++) {
			if ( flag[i] == Flags.Flag.SEEN ) {
				isnew = true;
				break;
			}
		}
		return isnew;
	}

	/**
	 * 　*　判断此邮件是否包含附件 　
	 * 
	 * @throws MessagingException
	 */
	public boolean isContainAttach(Part part) throws Exception {
		boolean attachflag = false;
		if ( part.isMimeType("multipart/*") ) {
			Multipart mp = (Multipart) part.getContent();
			// 获取附件名称可能包含多个附件
			for (int j = 0; j < mp.getCount(); j++) {
				BodyPart mpart = mp.getBodyPart(j);
				String disposition = mpart.getDescription();
				if ( (disposition != null) && ((disposition.equals(Part.ATTACHMENT)) || (disposition.equals(Part.INLINE))) ) {
					attachflag = true;
				} else if ( mpart.isMimeType("multipart/*") ) {
					attachflag = isContainAttach((Part) mpart);
				} else {
					String contype = mpart.getContentType();
					if ( contype.toLowerCase().indexOf("application") != -1 )
						attachflag = true;
					if ( contype.toLowerCase().indexOf("name") != -1 )
						attachflag = true;
				}
			}
		} else if ( part.isMimeType("message/rfc822") ) {
			attachflag = isContainAttach((Part) part.getContent());
		}
		return attachflag;
	}

	/**
	 * 　*　【保存附件】 　
	 * 
	 * @throws Exception
	 * @throws IOException
	 * @throws MessagingException
	 * @throws Exception
	 */
	public void saveAttachMent(Part part) throws Exception {
		String fileName = "";
		if ( part.isMimeType("multipart/*") ) {
			Multipart mp = (Multipart) part.getContent();
			for (int i = 0; i < mp.getCount(); i++) {
				BodyPart mpart = mp.getBodyPart(i);
				String disposition = mpart.getDisposition();
				if ( (disposition != null) && ((disposition.equals(Part.ATTACHMENT)) || (disposition.equals(Part.INLINE))) ) {
					fileName = mpart.getFileName();
					if ( fileName.toLowerCase().indexOf("gbk") != -1 ) {
						fileName = MimeUtility.decodeText(fileName);
					}
					saveFile(fileName, mpart.getInputStream());
				} else if ( mpart.isMimeType("multipart/*") ) {
					saveAttachMent(mpart);
				} else {
					fileName = mpart.getFileName();
					if ( (fileName != null) && (fileName.toLowerCase().indexOf("GB2312") != -1) ) {
						fileName = MimeUtility.decodeText(fileName);
						saveFile(fileName, mpart.getInputStream());
					}
				}
			}
		} else if ( part.isMimeType("message/rfc822") ) {
			saveAttachMent((Part) part.getContent());
		}
	}

	/**
	 * 　*　【设置附件存放路径】 　
	 */
	public void setAttachPath(String attachpath) {
		this.saveAttachPath = attachpath;
	}

	/**
	 * 　*　【设置日期显示格式】 　
	 */
	public void setDateFormat(String format) {
		this.dateformat = format;
	}

	/**
	 * 　*　【获得附件存放路径】 　
	 */

	public String getAttachPath() {
		return saveAttachPath;
	}

	/**
	 * 　*　【真正的保存附件到指定目录里】 　
	 */
	private void saveFile(String fileName, InputStream in) throws Exception {
		String osName = System.getProperty("os.name");
		String storedir = getAttachPath();
		String separator = "";
		if ( osName == null )
			osName = "";
		if ( osName.toLowerCase().indexOf("win") != -1 ) {
			separator = "\\";
			if ( storedir == null || storedir.equals("") )
				storedir = "D:\\tmp";
		} else {
//			separator = "/";
//			storedir = "/tmp";
		}
		File storefile = new File(MimeUtility.decodeText(storedir) + MimeUtility.decodeText(separator) + MimeUtility.decodeText(fileName));
		System.out.println("storefile's path: " + storefile.toString());
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(storefile));
			bis = new BufferedInputStream(in);
			int c;
			while ((c = bis.read()) != -1) {
				bos.write(c);
				bos.flush();
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			throw new Exception("文件保存失败!");
		} finally {
			bos.close();
			bis.close();
		}
	}

	public static boolean getEmailSubject() throws Exception {

		if ( UsedVerbs.getInfoEmailId != null && UsedVerbs.getInfoEmailId.size() > 0 ) {
			// 清空原信息
			UsedVerbs.getInfoEmailId.removeAllElements();
			UsedVerbs.getInfoEmailSub.removeAllElements();
			UsedVerbs.getInfoEmailTime.removeAllElements();
		}
		if ( UsedVerbs.getOtherEmailId != null && UsedVerbs.getOtherEmailId.size() > 0 ) {
			// 清空原信息
			UsedVerbs.getOtherEmailId.removeAllElements();
			UsedVerbs.getOtherEmailSub.removeAllElements();
			UsedVerbs.getOtherEmailFrom.removeAllElements();
		}

		// UsedVerbs.getInfoEmailId.addElement("1");
		// UsedVerbs.getInfoEmailSub.addElement("我的信息");
		// UsedVerbs.getInfoEmailTime.addElement("2011-2-16");
		// UsedVerbs.getOtherEmailId.addElement("ddd");
		// UsedVerbs.getOtherEmailSub.addElement("sss");
		// UsedVerbs.getOtherEmailFrom.addElement("fff");
		try {
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);

			Log.e("", UsedVerbs.email_pop + UsedVerbs.host_email_address + UsedVerbs.hostEmailPwd);
			Store store = session.getStore("pop3");
			store.connect(UsedVerbs.email_pop, UsedVerbs.host_email_address, UsedVerbs.hostEmailPwd);
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);
			Message message[] = folder.getMessages();
			ReceiveImplement pmm = null;

			for (int i = 0; i < message.length; i++) {
				pmm = new ReceiveImplement((MimeMessage) message[i]);

				String getID = pmm.getMessageId();
				String sub = pmm.getSubject();
				String fromWho = pmm.getFrom1();

				pmm.setDateFormat("yy年MM月dd日 HH:mm");
				String time = pmm.getSendDate();
				String attach = "noattach";
				if ( pmm.isContainAttach((Part) message[i]) ) {
					attach = "haveattach";
				}
				if ( chargeUseful(sub) ) {
					// 获得查询操作反馈的邮件
					UsedVerbs.getInfoEmailId.addElement(getID);
					UsedVerbs.getInfoEmailSub.addElement(sub);
					UsedVerbs.getInfoEmailTime.addElement(time);
					UsedVerbs.getInfoAttack.addElement(attach);
				} else {
					// 获得其它邮件
					UsedVerbs.getOtherEmailId.addElement(getID);
					UsedVerbs.getOtherEmailSub.addElement(sub);
					UsedVerbs.getOtherEmailFrom.addElement(fromWho);
					UsedVerbs.getOtherAttack.addElement(attach);
				}
			}
			folder.close(false);
			store.close();
			// } catch (NoSuchProviderException e) {
			// // TODO Auto-generated catch block
			// Log.e("", "Login fail NoProvider");
			// return false;
			//
			// } catch (MessagingException e) {
			// // TODO Auto-generated catch block
			// Log.e("", "Login fail Messaging");
			// return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e("", "Login fail");
			return false;
		}
		//
		return true;
	}

	// 读取邮件内容
	public static boolean getContent(String mailID) throws Exception {

		if ( UsedVerbs.oneEmailContent.size() >= 0 ) {
			// 清空原内容
			UsedVerbs.oneEmailContent.removeAllElements();
		}
		try {
			Properties props = new Properties();
			Session session = Session.getDefaultInstance(props, null);

			Store store = session.getStore("pop3");
			store.connect(UsedVerbs.email_pop, UsedVerbs.hostEmailName, UsedVerbs.hostEmailPwd);
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);
			Message message[] = folder.getMessages();
			ReceiveImplement pmm = null;
			for (int i = 0; i < message.length; i++) {
				pmm = new ReceiveImplement((MimeMessage) message[i]);
				String getID = pmm.getMessageId();
				if ( getID.equals(mailID) ) {
					String sub = pmm.getSubject();
					pmm.setDateFormat("yy年MM月dd日 HH:mm");
					String time = pmm.getSendDate();
					String whosend = pmm.getFrom1();
					pmm.getMailContent((Part) message[i]); // 根据内容的不同解析邮件
					String content = pmm.getBodyText();
					message[i].setFlag(Flags.Flag.SEEN, true);

					UsedVerbs.oneEmailContent.addElement(sub);
					UsedVerbs.oneEmailContent.addElement(time);
					UsedVerbs.oneEmailContent.addElement(whosend);
					UsedVerbs.oneEmailContent.addElement(content);
				}
			}
			folder.close(false);
			store.close();
			// } catch (NoSuchProviderException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// return false;
			//
			// } catch (MessagingException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// 下载附件
	public static boolean downloadAttach(String mailID) throws Exception {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		try {
			Store store = session.getStore("pop3");
			store.connect(UsedVerbs.email_pop, UsedVerbs.hostEmailName, UsedVerbs.hostEmailPwd);
			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);
			Message message[] = folder.getMessages();
			ReceiveImplement pmm = null;
			for (int i = 0; i < message.length; i++) {
				pmm = new ReceiveImplement((MimeMessage) message[i]);
				String getID = pmm.getMessageId();
				if ( getID.equals(mailID) ) {
					pmm.getMailContent((Part) message[i]); // 根据内容的不同解析邮件
					pmm.setAttachPath(Environment.getExternalStorageDirectory().getPath() + "/download/");
					// 设置邮件附件的保存路径

					pmm.saveAttachMent((Part) message[i]); // 保存附件

				}
			}
			folder.close(false);
			store.close();
			// } catch (NoSuchProviderException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// return false;
			//
			// } catch (MessagingException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// 删除邮件
	public static boolean deleteMail(String delID) throws Exception {

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		try {

			Store store = session.getStore("pop3");
			store.connect(UsedVerbs.email_pop, UsedVerbs.hostEmailName, UsedVerbs.hostEmailPwd);

			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);// 注意这里与读取时的区别
			Message message[] = folder.getMessages();

			ReceiveImplement pmm = null;
			for (int i = 0; i < message.length; i++) {
				pmm = new ReceiveImplement((MimeMessage) message[i]);
				String getID = pmm.getMessageId();
				if ( getID.equals(delID) ) {
					message[i].setFlag(Flags.Flag.DELETED, true);
				}
			}
			folder.close(true);// 注意这里与读取的区别，这里是确认删除的操作....
			store.close();
			// } catch (NoSuchProviderException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// return false;
			//
			// } catch (MessagingException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean chargeUseful(String sub) {
		// 添加判断广告邮件
		String adWords = "邮件反馈,";

		char[] allWords = adWords.toCharArray();
		int starti = 0;
		Vector<String> adWordsVector = new Vector<String>();
		for (int i = 0; i < adWords.length(); i++) {
			if ( String.valueOf(allWords[i]).equals(",") || String.valueOf(allWords[i]).equals("，") ) {
				String getone = adWords.substring(starti, i);
				adWordsVector.addElement(getone);
				starti = adWords.indexOf(allWords[i]) + 1;
			}
		}

		char[] s = sub.toCharArray();
		for (int m = 0; m < adWordsVector.size(); m++) {

			// 获得第m个词组, 并判断主题中是否含有该词语;
			String oneWord = adWordsVector.get(m);
			int onewordSize = oneWord.length();

			for (int j = 0; j < sub.length(); j++) {

				String two = String.valueOf(s[j]);
				String one = String.valueOf(oneWord.charAt(0));
				if ( two.equals(one) ) {

					if ( j + onewordSize < sub.length() ) {
						String findNext = sub.substring(j, j + onewordSize);
						if ( findNext.equals(oneWord) ) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}
}