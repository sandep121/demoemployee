package com.sandeep.demoemployee.entity;

public class NewEmployee extends CrudeEmployee
{
    private boolean replace;

    public NewEmployee() {
    }

    public boolean isReplace() {
        return replace;
    }

    public void setReplace(boolean replace) {
        this.replace = replace;
    }
}
