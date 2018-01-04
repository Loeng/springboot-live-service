package com.sgc.comm.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Security;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeliveredMail {
	public void transport(String mailProtocolHost, int mailProtocolPort,
		      String mailUser, String mailPwd, String subject, String content,
		      String from, Address[] recipients) {
		    try {
		        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
		      Properties props = new Properties();
		      props.put("mail.smtp.host", "smtp.sina.com");
		      props.setProperty("mail.smtp.auth", "true");
		      props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
		      props.setProperty("mail.transport.protocol", "smtp");
		      props.setProperty("mail.smtp.socketFactory.fallback", "false");
		      props.setProperty("mail.smtp.port", "465");
		      props.setProperty("mail.smtp.socketFactory.port", "465");
		      Session session = Session.getInstance(props);
		      session.setDebug(true);

		      Message msg = new MimeMessage(session);
		      MimeMultipart msgMultipart = new MimeMultipart("mixed");
		      msg.setContent(msgMultipart);

		      MimeBodyPart mbp = new MimeBodyPart();
		      msgMultipart.addBodyPart(mbp);
		      mbp.setDataHandler(new DataHandler(new ByteArrayDataSource(content
		          .getBytes("UTF-8"), "text/html;charset=UTF-8")));
		      msg.setSubject(MimeUtility.encodeText(subject, "utf-8", "B"));
		      msg.setFrom(new InternetAddress(from));
		      // msg.setDataHandler(new DataHandler(new
		      // ByteArrayDataSource(content.getBytes("UTF-8"),"text/html;charset=UTF-8")));
		      Transport transport = session.getTransport();
		      transport.addTransportListener(new TransportListener() {
		        @Override
		        public void messagePartiallyDelivered(TransportEvent te) {
		          for (Address add : te.getValidSentAddresses()) {
		            System.err.println("部分内容发送成功的邮件地址:" + add);
		          }
		        }

		        @Override
		        public void messageNotDelivered(TransportEvent te) {
		          for (Address add : te.getValidSentAddresses()) {
		            System.err.println("发送失败的邮件地址:" + add);
		          }
		        }

		        @Override
		        public void messageDelivered(TransportEvent te) {
		          for (Address add : te.getValidSentAddresses()) {
		            System.err.println("成功发送的邮件地址:" + add);
		          }

		        }
		      });
		      transport.connect(mailProtocolHost, mailProtocolPort, mailUser,
		          mailPwd);
		      transport.sendMessage(msg, recipients);
		      transport.close();
		    } catch (AddressException e) {
		      e.printStackTrace();
		    } catch (NoSuchProviderException e) {
		      e.printStackTrace();
		    } catch (MessagingException e) {
		      e.printStackTrace();
		    } catch (UnsupportedEncodingException e) {
		      e.printStackTrace();
		    }
		  }

	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(DeliveredMail.class);
		if (args == null || args.length < 1) {
			logger.info("请指定配置文件路径..如：cn.jugame.mail.delivered.DeliveredMail e:/abcd");
		} else {
			String configPath = args[0];
			PropertiesConfiguration cfg = null;
			try {
				File file = new File(configPath);
				if (file.isDirectory()) {
					cfg = new PropertiesConfiguration(configPath
							+ File.separator + "smtp.properties");
				} else {
					if (file.getAbsolutePath().endsWith(".properties")) {
						cfg = new PropertiesConfiguration(configPath);
					} else {
						logger.info("配置文件格式存在问题");
					}
				}
			} catch (ConfigurationException e) {
				e.printStackTrace();
			}
			if (cfg != null) {

				try {
					DeliveredMail dm = new DeliveredMail();
					// 1.读取发送邮件帐号
					BufferedReader brAccount = new BufferedReader(
							new FileReader(new File("account.txt")));
					String line;
					List<Map<String, String>> emailList = new ArrayList<Map<String, String>>();
					while ((line = brAccount.readLine()) != null) {
						String[] account = line.split(",");
						Map<String, String> accountMap = new HashMap<String, String>();
						accountMap.put("user", account[0]);
						accountMap.put("pwd", account[1]);
						emailList.add(accountMap);
					}
					// 2.读取接收人帐号
					BufferedReader brRecipients = new BufferedReader(
							new FileReader(new File("recipients.txt")));
					String lineRecipient;
					List<String> recipientList = new ArrayList<String>();
					while ((lineRecipient = brRecipients.readLine()) != null) {
						recipientList.add(lineRecipient);
					}
					// 3.读取发送内容
					BufferedReader brEmailContent = new BufferedReader(
							new FileReader(new File("emailContent.txt")));
					String emailSubject = "8868邮件";
					String lineEmailContent;
					StringBuffer emailContent = new StringBuffer();
					if (null != brEmailContent) {
						emailSubject = brEmailContent.readLine();
						while ((lineEmailContent = brEmailContent.readLine()) != null) {
							emailContent.append(lineEmailContent);
						}
					}
					// System.err.println("========================");
					// System.err.println(emailContent);
					// System.err.println("========================");

					Address[] recipients = new Address[recipientList.size()];
					for (int i = 0; i < recipientList.size(); i++) {
						try {
							recipients[i] = new InternetAddress(
									recipientList.get(i));
						} catch (AddressException e) {
							e.printStackTrace();
						}
					}
					String from = emailList.get(0).get("user");
					String account = from.substring(0, from.lastIndexOf("@"));
					// System.err.println(account);
					String password = emailList.get(0).get("pwd");
					String smtpName = from.substring(from.indexOf("@") + 1,
							from.lastIndexOf("."));
					// System.err.println(smtpName);

					String[] protocol = cfg.getString(smtpName).split(" ");
					String smtp = protocol[0];
					int port = Integer.parseInt(protocol[1]);
					System.err.println(smtp);
					int time = 0;
					if (recipients.length % 20 == 0) {
						time = recipients.length / 20;
					} else {
						time = recipients.length / 20 + 1;
					}
//					System.err.println("分" + time + "批次发");
					for (int i = 0; i < time; i++) {
						if (i != time - 1) {
							Address[] timeAddress = Arrays.copyOfRange(
									recipients, i == 0 ? 0 : (i * 20),
									i == 0 ? 20 : ((i + 1) * 20));
							dm.transport(smtp, port, account, password,
									emailSubject + i, emailContent.toString(),
									from, timeAddress);
						} else {
							Address[] timeAddress = Arrays.copyOfRange(
									recipients, i * 20, i * 20
											+ (recipients.length % 20));
							dm.transport(smtp, port, account, password,
									emailSubject + i, emailContent.toString(),
									from, timeAddress);
						}

					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		}

	}
}

