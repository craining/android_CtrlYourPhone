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
	private String saveAttachPath = "";// �������غ�Ĵ��Ŀ¼
	private StringBuffer bodytext = new StringBuffer();

	public static String[] args;
	public static String subjectview;

	public Store store;
	// ����ʼ����ݵ�StringBuffer����
	private String dateformat = "yy-MM-dd��HH:mm";// Ĭ�ϵ���ǰ��ʾ��ʽ

	/**
	 * ��*�����캯��,��ʼ��һ��MimeMessage���� ��
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
	 * ��*����÷����˵ĵ�ַ������ ��
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
	 * ��*������ʼ����ռ��ˣ����ͣ������͵ĵ�ַ�����������������ݵĲ����Ĳ�ͬ
	 * ��*��"to"----�ռ��ˡ�"cc"---�����˵�ַ��"bcc"---�����˵�ַ ��
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
	 * ����*������ʼ����� ����
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
	 * ����*������ʼ��������� ����
	 */
	public String getSendDate() throws Exception {
		Date senddate = mimeMessage.getSentDate();
		SimpleDateFormat format = new SimpleDateFormat(dateformat);
		return format.format(senddate);
	}

	/**
	 * ����*�������ʼ����ѵõ����ʼ����ݱ��浽һ��StringBuffer�����У������ʼ�
	 * ����*����Ҫ�Ǹ���MimeType���͵Ĳ�ִͬ�в�ͬ�Ĳ�����һ��һ���Ľ��� ����
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
	 * ��*������ʼ��������� ����
	 */
	public String getBodyText() {
		return bodytext.toString();
	}

	/**
	 * ����*���жϴ��ʼ��Ƿ���Ҫ��ִ�������Ҫ��ִ����"true",���򷵻�"false" ����
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
	 * ����ô��ʼ���Message-ID ����
	 * 
	 * @throws MessagingException
	 */
	public String getMessageId() throws MessagingException {
		return mimeMessage.getMessageID();
	}

	/**
	 * ��*�����жϴ��ʼ��Ƿ��Ѷ������δ�����ط���false,��֮����true�� ����
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
	 * ��*���жϴ��ʼ��Ƿ�������� ��
	 * 
	 * @throws MessagingException
	 */
	public boolean isContainAttach(Part part) throws Exception {
		boolean attachflag = false;
		if ( part.isMimeType("multipart/*") ) {
			Multipart mp = (Multipart) part.getContent();
			// ��ȡ�������ƿ��ܰ����������
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
	 * ��*�������渽���� ��
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
	 * ��*�������ø������·���� ��
	 */
	public void setAttachPath(String attachpath) {
		this.saveAttachPath = attachpath;
	}

	/**
	 * ��*��������������ʾ��ʽ�� ��
	 */
	public void setDateFormat(String format) {
		this.dateformat = format;
	}

	/**
	 * ��*������ø������·���� ��
	 */

	public String getAttachPath() {
		return saveAttachPath;
	}

	/**
	 * ��*���������ı��渽����ָ��Ŀ¼� ��
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
			throw new Exception("�ļ�����ʧ��!");
		} finally {
			bos.close();
			bis.close();
		}
	}

	public static boolean getEmailSubject() throws Exception {

		if ( UsedVerbs.getInfoEmailId != null && UsedVerbs.getInfoEmailId.size() > 0 ) {
			// ���ԭ��Ϣ
			UsedVerbs.getInfoEmailId.removeAllElements();
			UsedVerbs.getInfoEmailSub.removeAllElements();
			UsedVerbs.getInfoEmailTime.removeAllElements();
		}
		if ( UsedVerbs.getOtherEmailId != null && UsedVerbs.getOtherEmailId.size() > 0 ) {
			// ���ԭ��Ϣ
			UsedVerbs.getOtherEmailId.removeAllElements();
			UsedVerbs.getOtherEmailSub.removeAllElements();
			UsedVerbs.getOtherEmailFrom.removeAllElements();
		}

		// UsedVerbs.getInfoEmailId.addElement("1");
		// UsedVerbs.getInfoEmailSub.addElement("�ҵ���Ϣ");
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

				pmm.setDateFormat("yy��MM��dd�� HH:mm");
				String time = pmm.getSendDate();
				String attach = "noattach";
				if ( pmm.isContainAttach((Part) message[i]) ) {
					attach = "haveattach";
				}
				if ( chargeUseful(sub) ) {
					// ��ò�ѯ�����������ʼ�
					UsedVerbs.getInfoEmailId.addElement(getID);
					UsedVerbs.getInfoEmailSub.addElement(sub);
					UsedVerbs.getInfoEmailTime.addElement(time);
					UsedVerbs.getInfoAttack.addElement(attach);
				} else {
					// ��������ʼ�
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

	// ��ȡ�ʼ�����
	public static boolean getContent(String mailID) throws Exception {

		if ( UsedVerbs.oneEmailContent.size() >= 0 ) {
			// ���ԭ����
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
					pmm.setDateFormat("yy��MM��dd�� HH:mm");
					String time = pmm.getSendDate();
					String whosend = pmm.getFrom1();
					pmm.getMailContent((Part) message[i]); // �������ݵĲ�ͬ�����ʼ�
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

	// ���ظ���
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
					pmm.getMailContent((Part) message[i]); // �������ݵĲ�ͬ�����ʼ�
					pmm.setAttachPath(Environment.getExternalStorageDirectory().getPath() + "/download/");
					// �����ʼ������ı���·��

					pmm.saveAttachMent((Part) message[i]); // ���渽��

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

	// ɾ���ʼ�
	public static boolean deleteMail(String delID) throws Exception {

		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);
		try {

			Store store = session.getStore("pop3");
			store.connect(UsedVerbs.email_pop, UsedVerbs.hostEmailName, UsedVerbs.hostEmailPwd);

			Folder folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);// ע���������ȡʱ������
			Message message[] = folder.getMessages();

			ReceiveImplement pmm = null;
			for (int i = 0; i < message.length; i++) {
				pmm = new ReceiveImplement((MimeMessage) message[i]);
				String getID = pmm.getMessageId();
				if ( getID.equals(delID) ) {
					message[i].setFlag(Flags.Flag.DELETED, true);
				}
			}
			folder.close(true);// ע���������ȡ������������ȷ��ɾ���Ĳ���....
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
		// ����жϹ���ʼ�
		String adWords = "�ʼ�����,";

		char[] allWords = adWords.toCharArray();
		int starti = 0;
		Vector<String> adWordsVector = new Vector<String>();
		for (int i = 0; i < adWords.length(); i++) {
			if ( String.valueOf(allWords[i]).equals(",") || String.valueOf(allWords[i]).equals("��") ) {
				String getone = adWords.substring(starti, i);
				adWordsVector.addElement(getone);
				starti = adWords.indexOf(allWords[i]) + 1;
			}
		}

		char[] s = sub.toCharArray();
		for (int m = 0; m < adWordsVector.size(); m++) {

			// ��õ�m������, ���ж��������Ƿ��иô���;
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