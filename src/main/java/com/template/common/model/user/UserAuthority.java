package com.template.common.model.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserAuthority {

    private int role;
    private int max_rank;
    private boolean view_sales_volume;
    private boolean view_rank_detail;
    // 상세페이지 접근 제한 : 순위비교표, 그래프, 데이터 표 (익명,브론즈,실버)
    private boolean view_permit;
    private int view_rank_term;
    // 비로그인:조회불가 0:실시간,전일(브론즈) 1:전주 추가(실버), 2:전월 추가(골드), 3:전년 추가(vip), 999:마스터권한
    private int limit_lovekiss;
    private String url;

    private Date expire_ymdt;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getMax_rank() {
        return max_rank;
    }

    public void setMax_rank(int max_rank) {
        this.max_rank = max_rank;
    }

    public boolean getView_sales_volume() {
        return view_sales_volume;
    }

    public void setView_sales_volume(boolean view_sales_volume) {
        this.view_sales_volume = view_sales_volume;
    }

    public boolean isView_sales_volume() {
        return view_sales_volume;
    }

    public boolean isView_rank_detail() {
        return view_rank_detail;
    }

    public void setView_rank_detail(boolean view_rank_detail) {
        this.view_rank_detail = view_rank_detail;
    }

    public int getView_rank_term() {
        return view_rank_term;
    }

    public void setView_rank_term(int view_rank_term) {
        this.view_rank_term = view_rank_term;
    }

    public boolean isView_permit() {
        return view_permit;
    }

    public void setView_permit(boolean view_permit) {
        this.view_permit = view_permit;
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

}
