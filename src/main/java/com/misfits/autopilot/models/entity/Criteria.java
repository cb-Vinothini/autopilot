package com.misfits.autopilot.models.entity;


import com.chargebee.models.enums.EntityType;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="criterias")
public class Criteria {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    private Long id;

    @Column(name="name", nullable=false)
    @ApiModelProperty(example = "")
    private String name;

    @Column(name="parameter", nullable=false)
    @ApiModelProperty(example = "Add shipping charges when subscription shipping address is in US")
    private String parameter;


    @Column(name="operator", nullable=false)
    @Enumerated(EnumType.ORDINAL)
    @ApiModelProperty(example = "equals")
    private Operator operator;

    @Column(name="value", nullable=false)
    @ApiModelProperty(example = "Add shipping charges when subscription shipping address is in US")
    private String value;


    @Column(name="entityType", nullable=false, length=200)
    @Enumerated(EnumType.ORDINAL)
    @ApiModelProperty(example = "CUSTOMER",hidden = true)
    private EntityType entityType;

    @CreationTimestamp
    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name="modified_at", nullable=false)
    private LocalDateTime modifiedAt;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }


    public enum Operator {
        between("between", "between"),
        after("after", "is after"),
        before("before", "is before"),
        notEmpty("not_empty", "is present"),
        empty("empty", "is not present"),
        equals("equals", "equals"),
        notEquals("not_equals", "not equal to"),
        lessThan("less_than", "less than"),
        greaterThan("greater_than", "greater than"),
        lessThanEqual("less_than_equal", "less than or equal"),
        greaterThanEqual("greater_than_equal", "greater than or equal"),
        is("is", "is"),
        isNot("is_not", "is not"),
        includes("includes", "includes"),
        doesNotInclude("does_not_include", "does not include"),
        containsOnly("contains_only", "contains only"),
        contains("contains", "contains"),
        notContains("not_contains", "does not contain"),
        startsWith("starts_with", "starts with");

        String name ;
        String displayName ;

        Operator(String name,String displayName) {
            this.name = name;
            this.displayName = displayName;
        }
    }

}
