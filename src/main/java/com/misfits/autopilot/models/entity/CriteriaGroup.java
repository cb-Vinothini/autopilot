package com.misfits.autopilot.models.entity;

import com.chargebee.models.enums.EntityType;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@IdClass(CriteriaGroupCompositeKey.class)
@Table(name="criteria_groups")
public class CriteriaGroup {

    @Id
    @Column(name="workflow_id", nullable=false)
    @ApiModelProperty(example = "")
    private Long workflowId;

    @Id
    @Column(name="criteria_id", nullable=false)
    @ApiModelProperty(example = "")
    private Long criteriaId;

    @CreationTimestamp
    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="modified_at", nullable=false)
    private LocalDateTime modifiedAt;

    public Long getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Long workflowId) {
        this.workflowId = workflowId;
    }

    public Long getCriteriaId() {
        return criteriaId;
    }

    public void setCriteriaId(Long criteriaId) {
        this.criteriaId = criteriaId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
