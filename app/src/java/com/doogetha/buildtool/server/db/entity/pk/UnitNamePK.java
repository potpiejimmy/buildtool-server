/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doogetha.buildtool.server.db.entity.pk;

/**
 *
 * @author thorsten.liese
 */
public class UnitNamePK implements java.io.Serializable {
    
    private String unit;
    private String name;
    
    public UnitNamePK() {}
    
    public UnitNamePK(String unit, String name) {
        this.unit = unit;
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public int hashCode() {
        return unit.hashCode() + name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof UnitNamePK)) return false;
        if (obj == null) return false;
        UnitNamePK pk = (UnitNamePK) obj;
        return pk.unit.equals(unit) && pk.name.equals(name);
    }
}
