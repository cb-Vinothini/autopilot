package com.misfits.autopilot.models.entity;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="ACTION")
public class Action {

    @Id
    @GeneratedValue
    @ApiModelProperty(hidden = true)
    private Long id;

    @Column(name="api_name", nullable = false)
    @ApiModelProperty(example = "subscriptions.add_charge")
    private String apiName;

    @Column(name="api_parameters", nullable = false)
    @Type(type="text")
    @ApiModelProperty(example = "{'amount=50'}")
    private String apiParameters;

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

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public void setApiParameters(String apiParameters) {
        this.apiParameters = apiParameters;
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

    public String getApiName() {
        return apiName;
    }

    public String getApiParameters() {
        return apiParameters;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }
}