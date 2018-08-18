package com.template.common.mail;


import com.template.common.model.common.Code;
import com.template.common.model.common.ResultInfo;
import com.template.common.model.email.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import static com.template.common.util.TimeUtil.getNowDateToString;

@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
public class MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailService.class);

    JavaMailSender javaMailSender = null;
    String from;

    public MailService(JavaMailSender javaMailSender, String from) {
        this.javaMailSender = javaMailSender;
        this.from = from;
    }

    public ResultInfo sendMail(String to, String subject, String content)
    {
        MimeMessagePreparator preparator = new MimeMessagePreparator()
        {
            public void prepare(MimeMessage mimeMessage) throws Exception
            {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
                mimeMessage.setFrom(new InternetAddress(from));
                mimeMessage.setSubject(subject);
                mimeMessage.setText(content, "utf-8", "html");
            }
        };

        ResultInfo resultInfo = new ResultInfo();

        try {
            javaMailSender.send(preparator);
            resultInfo.setCode(Code.SUCCESS);
            logger.info("emailSend...end");
            resultInfo.setMessage("이메일을 발송하였습니다.");
            //return body;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultInfo.setCode(Code.FAIL);
            resultInfo.setMessage("이메일 발송에 실패하였습니다.");
            //return body;
        } finally {
            logger.info("emailSend...end2");
            return resultInfo;
        }
    }

    public ResultInfo sendAlertEmail(Email email)
    {
        MimeMessagePreparator preparator = new MimeMessagePreparator()
        {
            public void prepare(MimeMessage mimeMessage) throws Exception
            {
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(email.getReceiver()));
                //mimeMessage.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(email.getReceiver()));
                mimeMessage.setFrom(new InternetAddress(from));
                mimeMessage.setSubject("배치 알림 - " + email.getSubject());
                //mimeMessage.setText(content, "utf-8", "html");
                Multipart multipart = new MimeMultipart();
                MimeBodyPart htmlPart = new MimeBodyPart();
                htmlPart.setContent("<strong>작성자</strong> : " + "배치서버" + "<br/><br/>"
                        //+ "<strong>한터차트 배치 알림</strong> : " + email.getSubject() + "<br/><br/>"
                        + "<strong>발생 시각</strong> : " + getNowDateToString() + "<br/><br/>"
                        + "<strong>에러 내용</strong> : " + email.getContent(), "text/html; charset=utf-8");
                multipart.addBodyPart(htmlPart);
                mimeMessage.setContent(multipart);
                mimeMessage.saveChanges();
            }
        };

        ResultInfo resultInfo = new ResultInfo();

        try {
            javaMailSender.send(preparator);
            resultInfo.setCode(Code.SUCCESS);
            logger.info("emailSend...end");
            resultInfo.setMessage("이메일을 발송하였습니다.");
            //return body;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            resultInfo.setCode(Code.FAIL);
            resultInfo.setMessage("이메일 발송에 실패하였습니다.");
            //return body;
        } finally {
            logger.info("emailSend...end2");
            return resultInfo;
        }
    }

}