package com.sandeep.demoemployee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
public class Designation
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @JsonIgnore
    private int dsgnId;
    @Column
    @JsonProperty(value = "jobTitle")
    private String role;
    @JsonIgnore
    private float lvlId;

    public Designation(int dsgnId, String role, float lvlId) {
        this.dsgnId = dsgnId;
        this.role = role;
        this.lvlId = lvlId;
    }

    public Designation() {
    }

    public int getDsgnId() {
        return dsgnId;
    }

    public void setDsgnId(int dsgnId) {
        this.dsgnId = dsgnId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public float getLvlId() {
        return lvlId;
    }

    public void setLvlId(float lvlId) {
        this.lvlId = lvlId;
    }
}
