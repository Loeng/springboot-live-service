package com.sgc.comm.util;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * 发送邮件的工具类
 * 
 * @author lenovo
 *
 */
public class MailUtils {

	/**
	 * 发送普通邮件
	 */
	public static void sendMail(String recMail, String title, String content) {
		try {
			// 1.创建Session（连接邮件服务器）
			/**
			 * 参数一：邮箱服务器的参数 参数二：用户登录验证数据
			 */
			Properties props = new Properties();
			// 邮箱服务器地址
			props.setProperty("mail.smtp.host", "smtp.sina.com");
			// 是否验证登录（用户名和密码加密）
			props.setProperty("mail.smtp.auth", "true");
			Session session = Session.getInstance(props, new Authenticator() {

				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("ericxu_12345@sina.com", "eric12345");
				}

			});

			// 打开调式模式（查看邮件发送的过程）
			session.setDebug(true);

			// 2.创建一封邮件
			MimeMessage mail = new MimeMessage(session);
			// 发件人
			mail.setFrom(new InternetAddress("ericxu_12345@sina.com"));
			// 收件人
			mail.setRecipient(RecipientType.TO, new InternetAddress(recMail));
			// 标题
			mail.setSubject(title);
			// 正文
			mail.setContent(content, "text/html;charset=utf-8");

			// 3.发送邮件
			Transport.send(mail);
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("发送邮件失败：" + e.getMessage());
		}
	}
}
