package com.sandeep.demoemployee.entity;

import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Employee
{
    @Column
    private String name;
    @Id
    @Column
    private int id;
    @Column
    private int managerId;
    @Column
    private int roleId;

    public Employee(String name, int id, int managerId, int roleId) {
        this.name = name;
        this.id = id;
        this.managerId = managerId;
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", managerId=" + managerId +
                ", roleId=" + roleId +
                '}';
    }
}
