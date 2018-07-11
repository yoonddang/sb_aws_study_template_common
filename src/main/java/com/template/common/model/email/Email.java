package com.template.common.model.email;

public class Email {

    private String subject;
    private String content;
    private String receiver;
    private String articleId;
    private String writer;
    private String rgstDate;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getRgstDate() {
        return rgstDate;
    }

    public void setRgstDate(String rgstDate) {
        this.rgstDate = rgstDate;
    }
}
