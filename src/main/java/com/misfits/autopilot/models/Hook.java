package com.misfits.autopilot.models;

import com.chargebee.models.enums.EventType;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;

@Entity
@Table(name="hooks")
public class Hook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Long id;

    @Column(name="eventType", nullable=false, length=200)
    @Enumerated(EnumType.ORDINAL)
    @ApiModelProperty(example = "CUSTOMER_CREATED")
    @Basic
    private EventType eventType;


}
