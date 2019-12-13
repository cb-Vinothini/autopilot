package com.misfits.autopilot.models;

import com.chargebee.models.enums.EntityType;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.stream.Stream;

@Entity
@Table(name="workflows")
public class Workflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(example = "081")
    private Long id;

    @Column(name="name", nullable=false)
    @ApiModelProperty(example = "Add Shipping Charges")
    private String name;

    @Column(name="description")
    @ApiModelProperty(example = "Add shipping charges when subscription shipping address is in US")
    private String description;

    @Column(name="type", nullable=false)
    @Enumerated(EnumType.ORDINAL)
    @ApiModelProperty(example = "hook")
    private WorkflowType type;

    @Column(name="entityType", nullable=false, length=200)
    @Enumerated(EnumType.ORDINAL)
    @ApiModelProperty(example = "subscription", allowableValues = "")
    @Basic
    private EntityType entityType;

    @CreationTimestamp
    @Column(name="createdAt", nullable=false)
    @ApiModelProperty(example = "1560319388818")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="modifiedAt", nullable=false)
    @ApiModelProperty(example = "1560319388818")
    private LocalDateTime modifiedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WorkflowType getType() {
        return type;
    }

    public void setType(WorkflowType type) {
        this.type = type;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
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

    public enum WorkflowType {
        HOOK, JOB;
    }
}
