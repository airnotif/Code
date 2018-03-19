package com.e4project.airnotif;

import java.util.Date;

public class Notification {

    private Date date;

    Notification(Date date){
        this.date = date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    Date getDate() {
        return date;
    }
}
