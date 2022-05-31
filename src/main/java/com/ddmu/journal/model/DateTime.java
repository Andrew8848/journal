package com.ddmu.journal.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
public class DateTime {

    @Column(length = 16)
    private Date date;

    @Column(length = 16)
    private Time time;

    public DateTime setDateTimeNow(){
        this.setDate(new Date(System.currentTimeMillis()));
        this.setTime(new Time(System.currentTimeMillis()));
        return this;
    }

}
