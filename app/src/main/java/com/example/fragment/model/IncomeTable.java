package com.example.fragment.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class IncomeTable extends BmobObject {
    private String type;
    private Double money;
    private BmobDate date;
    private String free;
    private String user;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public BmobDate getDate() {
        return date;
    }

    public void setDate(BmobDate date) {
        this.date = date;
    }

    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
