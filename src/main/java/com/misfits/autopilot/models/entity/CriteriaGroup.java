package com.misfits.autopilot.models.entity;

import com.chargebee.models.enums.EntityType;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name="criteria_groups")
public class CriteriaGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Long id;

    @Column(name="name", nullable=false)
    @ApiModelProperty(example = "")
    private String name;

    @Column(name="workflow_id", nullable=false)
    @ApiModelProperty(example = "")
    private Long workflowId;


    @Column(name="criteria_id", nullable=false)
    @ApiModelProperty(example = "")
    private Long criteriaId;


    @CreationTimestamp
    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="modified_at", nullable=false)
    private LocalDateTime modifiedAt;
}
