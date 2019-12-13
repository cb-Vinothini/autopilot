package com.misfits.autopilot.models.entity;

import com.chargebee.models.enums.EntityType;
import com.chargebee.models.enums.EventType;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

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
    private EventType eventType;


    @Column(name="workflow_id", nullable=false)
    @ApiModelProperty(example = "Add shipping charges when subscription shipping address is in US")
    private Long workflowId;


    @CreationTimestamp
    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="modified_at", nullable=false)
    private LocalDateTime modifiedAt;


    @Column(name="entityType", nullable=false, length=200)
    @Enumerated(EnumType.ORDINAL)
    @ApiModelProperty(example = "CUSTOMER",hidden = true)
    private EntityType entityType;


}
