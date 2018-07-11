package com.template.common.model.board;

public class ReplyVO {

    private String idx;
    private String board_idx;
    private String reply_idx;
    private int reorder;
    private String writer;
    private String delete_flag;
    private String content;
    private String reg_ymdt;
    private String mod_ymdt;
    private String parent;
    private String depth;
    private String email;

    private String length;

    private int replyListCount;
    private int replyListDepthCount;

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public int getReplyListCount() {
        return replyListCount;
    }

    public void setReplyListCount(int replyListCount) {
        this.replyListCount = replyListCount;
    }

    public int getReplyListDepthCount() {
        return replyListDepthCount;
    }

    public void setReplyListDepthCount(int replyListDepthCount) {
        this.replyListDepthCount = replyListDepthCount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getReply_idx() {
        return reply_idx;
    }

    public void setReply_idx(String reply_idx) {
        this.reply_idx = reply_idx;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public int getReorder() {
        return reorder;
    }

    public void setReorder(int reorder) {
        this.reorder = reorder;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getDelete_flag() {
        return delete_flag;
    }

    public void setDelete_flag(String delete_flag) {
        this.delete_flag = delete_flag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReg_ymdt() {
        return reg_ymdt;
    }

    public void setReg_ymdt(String reg_ymdt) {
        this.reg_ymdt = reg_ymdt;
    }

    public String getMod_ymdt() {
        return mod_ymdt;
    }

    public void setMod_ymdt(String mod_ymdt) {
        this.mod_ymdt = mod_ymdt;
    }
}
