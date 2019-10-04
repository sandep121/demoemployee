package com.sandeep.demoemployee.entity;

import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Designation
{
    @Id
    @Column
    int roleId;
    @Column
    String role;
    @Column
    double priority;

    public Designation(int roleId, String role, double priority) {
        this.roleId = roleId;
        this.role = role;
        this.priority = priority;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Designation{" +
                "roleId=" + roleId +
                ", role='" + role + '\'' +
                ", priority=" + priority +
                '}';
    }
}
