package com.misfits.autopilot.models.entity;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@IdClass(ActionGroupCompositeKey.class)
@Table(name="action_groups")
public class ActionGroup {

    @Id
    @Column(name="workflow_id", nullable = false)
    @ApiModelProperty(example = "123")
    private Long workflowId;

    @Id
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

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
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

    public Long getWorkflowId() {
        return workflowId;
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
