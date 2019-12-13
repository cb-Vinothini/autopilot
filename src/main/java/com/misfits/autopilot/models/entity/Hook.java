package com.misfits.autopilot.models.entity;

import com.chargebee.models.enums.EntityType;
import com.chargebee.models.enums.EventType;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@IdClass(HookCompositeKey.class)
@Table(name="hooks")
public class Hook {

    @Id
    @Column(name="eventType", nullable=false, length=200)
    @Enumerated(EnumType.ORDINAL)
    @ApiModelProperty(example = "SUBSCRIPTION_CREATED")
    private EventType eventType;

    @Id
    @Column(name="workflow_id", nullable=false)
    private Long workflowId;

    @CreationTimestamp
    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="modified_at", nullable=false)
    private LocalDateTime modifiedAt;

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }
}
