package com.misfits.autopilot.models.entity;

import com.chargebee.org.json.JSONArray;
import com.chargebee.org.json.JSONException;
import com.chargebee.org.json.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="actions")
public class Action {

    @Transient
    public List<Attribute> attributes;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private Long id;

    @Column(name="api_name", nullable = false)
    @ApiModelProperty(example = "Subscription.addChargeAtTermEnd")
    private String name;

    @Column(name="api_parameters", nullable = false)
    @Type(type="text")
    @JsonIgnore
    @ApiModelProperty(example = "[{\"name\":\"amount\",\"value\":\"300\"},{\"name\":\"description\",\"value\":\"Shipping Charge\"}]")
    private String apiParameters;

    @CreationTimestamp
    @JsonIgnore
    @Column(name="created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @JsonIgnore
    @Column(name="modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setAttributes(List<Attribute> attributes) { this.attributes = attributes; }

    // Getters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public List<Attribute> getAttributes() {return attributes;}

    public void convertValues() throws JSONException {
        JSONArray attr = new JSONArray();
        for (Attribute attribute : attributes){
            attr.put(new JSONObject().put("name", attribute.getName()).put("value", attribute.getValue()));
        }
        apiParameters = attr.toString();
    }

    public void reConvertValues() throws Exception {
        List<Attribute> attributes = new ArrayList<>();
        JSONArray array = new JSONArray(apiParameters);
        for(int i=0; i<array.length();i++) {
            String obj = array.getString(i);
            Attribute attr = new Gson().fromJson(obj, Attribute.class);
            attributes.add(attr);
        }
        this.attributes = attributes;
    }
}