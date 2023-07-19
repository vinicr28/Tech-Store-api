package com.ericsson.rampup.dto;

import com.ericsson.rampup.resources.view.View;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.Serial;
import java.io.Serializable;

public class ReportOrderWeekDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonView(View.Base.class)
    private Integer orderQuantity;
    @JsonView(View.Base.class)
    private Integer dayOfWeek;

    public ReportOrderWeekDTO(Integer dayOfWeek, Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuatity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
}
