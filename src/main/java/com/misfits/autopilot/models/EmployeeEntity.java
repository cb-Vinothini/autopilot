package com.misfits.autopilot.models;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="TBL_EMPLOYEES")
public class EmployeeEntity {

    @Id
    @GeneratedValue
    @ApiModelProperty(example = "081")
    private Long id;

    @Column(name="first_name")
    @ApiModelProperty(example = "Vinothini")
    private String firstName;

    @Column(name="last_name")
    @ApiModelProperty(example = "Meganathan")
    private String lastName;

    @Column(name="email", nullable=false, length=200)
    @ApiModelProperty(example = "vinothini@chargebee.com")
    private String email;

    //Setters and getters left out for brevity.

    @Override
    public String toString() {
        return "EmployeeEntity [id=" + id + ", firstName=" + firstName +
                ", lastName=" + lastName + ", email=" + email   + "]";
    }
}
