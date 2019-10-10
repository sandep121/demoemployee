package com.sandeep.demoemployee.entity;

public class NewEmployee extends CrudeEmployee
{
    private boolean replace;

    public NewEmployee() {
    }

    public NewEmployee(Integer managerId, String empName, String designation, boolean replace) {
        super(managerId, empName, designation);
        this.replace = replace;
    }

    public boolean isReplace() {
        return replace;
    }

    public void setReplace(boolean replace) {
        this.replace = replace;
    }
}
