package com.template.common.model.user;

import java.util.Date;

public class TemplateUser {

	private int userIdx;
	private String email;
	private String pass_word;
	private String role;
	private String role_name;
	private String nickname;
	private String birthday;
	private String gender;
	private String subemail;
	private String phone;
	private String state;
	private int login_count;
	private int coin;
	private String eventKey;
	private int today_love_count;
	private int limit_lovekiss;
	private Date expireDate;
	private String authExpireDate;
	private Date expire_ymdt;


	public int getUserIdx() {
		return userIdx;
	}

	public void setUserIdx(int userIdx) {
		this.userIdx = userIdx;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPass_word() {
		return pass_word;
	}

	public void setPass_word(String pass_word) {
		this.pass_word = pass_word;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSubemail() {
		return subemail;
	}

	public void setSubemail(String subemail) {
		this.subemail = subemail;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getLogin_count() {
		return login_count;
	}

	public void setLogin_count(int login_count) {
		this.login_count = login_count;
	}

	public int getCoin() {
		return coin;
	}

	public void setCoin(int coin) {
		this.coin = coin;
	}

	public String getEventKey() {
		return eventKey;
	}

	public void setEventKey(String eventKey) {
		this.eventKey = eventKey;
	}

	public int getToday_love_count() {
		return today_love_count;
	}

	public void setToday_love_count(int today_love_count) {
		this.today_love_count = today_love_count;
	}

	public int getLimit_lovekiss() {
		return limit_lovekiss;
	}

	public void setLimit_lovekiss(int limit_lovekiss) {
		this.limit_lovekiss = limit_lovekiss;
	}

	public Date getExpire_ymdt() {
		return expire_ymdt;
	}

	public void setExpire_ymdt(Date expire_ymdt) {
		this.expire_ymdt = expire_ymdt;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getAuthExpireDate() {
		return authExpireDate;
	}

	public void setAuthExpireDate(String authExpireDate) {
		this.authExpireDate = authExpireDate;
	}
}
