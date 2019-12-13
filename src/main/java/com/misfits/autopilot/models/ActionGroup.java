package com.misfits.autopilot.models;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="ACTION_GROUP")
public class ActionGroup {

    @Id
    @GeneratedValue
    @ApiModelProperty(hidden = true)
    private Long id;

    @Column(name="workflow_id", nullable = false)
    @ApiModelProperty(example = "123")
    private Long workFlowId;

    @Column(name="action_id", nullable = false)
    @ApiModelProperty(example = "789")
    private Long actionId;

    @CreationTimestamp
    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setWorkFlowId(Long workFlowId) {
        this.workFlowId = workFlowId;
    }

    public void setActionId(Long actionId) {
        this.actionId = actionId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    // Getters

    public Long getId() {
        return id;
    }


    public Long getWorkFlowId() {
        return workFlowId;
    }


    public Long getActionId() {
        return actionId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }
}
