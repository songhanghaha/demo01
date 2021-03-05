package com.example.storage.modules.sys.utils;

import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.storage.modules.sys.entity.MailValidate;
import com.example.storage.modules.sys.entity.UserInfo;

@Component
public class MailUtil {

	@Value("${tsp.email.timelimit}")
	private String TIMELIMIT;

	@Value("${tsp.email.url}")
	private String URL;

	@Value("${tsp.email.host}")
	private String HOST;

	@Value("${tsp.email.port}")
	private String PORT;

	@Value("${tsp.email.username}")
	private String FROM;

	@Value("${tsp.email.password}")
	private String PWD;

	@Value("${tsp.email.smtp}")
	private String SMTP;

	public MailValidate sendFindPwdEmail(UserInfo userInfo) {
		MailValidate mailValidate = new MailValidate();
		return mailValidate;
	}

	public MailValidate activateMail(UserInfo userInfo) {

		MailValidate mailValidate = new MailValidate();
		String targetMail = userInfo.getEmail();
		Long curTime = System.currentTimeMillis();
		Long acticateTime = curTime + Long.parseLong(TIMELIMIT);
		String token = CryptoUtils.getDigestOfString(targetMail + curTime);

		mailValidate.setTargetMail(userInfo.getEmail());
		mailValidate.setExpiryTime(acticateTime.toString());
		mailValidate.setToken(token);
		String title = "TEE开放平台账户激活邮件";
		String anchor = "<a href= " + URL + "/mail/validate/activatemail?token=" + token + "&email=" + targetMail + "><h1>点我完成注册</h1></a>";
		String content =
				"<table><tbody><tr><td>" + "感谢您注册使用TSP管理平台，您的用户名是：" + userInfo.getUsername()
						+ "，为保护您的账号不被他人恶意使用，我们需要验证您的邮箱," + "方便您忘记密码以后找回密码！" + "</td></tr>"
						+ "<tr><td>&nbsp;</td></tr>" + "<tr><td>" + anchor + "</td></tr>"
						+ "<tr><td>如果不能点击上面的文字链接地址，请复制以下地址并粘贴到浏览器的地址栏进行操作：</td></tr>"
						+ "<tr><td>" + URL + "/mail/validate/activatemail?token=" + token + "&email=" + targetMail + "</td></tr>"
						+ "<tr><td>&nbsp;</td></tr>"
						+ "</tbody></table>";

		ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
		scheduledThreadPool.schedule(() -> sendMail(targetMail, title, content), 1, TimeUnit.SECONDS);
		return mailValidate;
	}


	public boolean sendMail(String targetMail, String title, String content) {

		Properties properties = new Properties();
		properties.put("mail.smtp.host", HOST);
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", PORT);
		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		Session session = Session.getInstance(properties);
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(FROM, "KPP TSP MANAGER"));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(targetMail));
			message.setSubject(title);

			message.setContent(content, "text/html;charset=utf-8");
			message.setSentDate(new Date());
			message.saveChanges();

			Transport transport = session.getTransport(SMTP);
			transport.connect(FROM, PWD);
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

}
