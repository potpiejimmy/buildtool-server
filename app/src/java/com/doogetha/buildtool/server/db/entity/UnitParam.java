/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doogetha.buildtool.server.db.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author thorsten
 */
@Entity
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UnitParam.findByUnitAndName", query = "SELECT b FROM UnitParam b WHERE b.unit=:unit AND b.name=:name")})
public class UnitParam implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Column(length = 64)
    @Id
    private String name;

    @Column(length = 64)
    @Id
    private String unit;
    
    @Column(length = 2048)
    @NotNull
    private String value;
    
    public UnitParam() {
    }
    
    public UnitParam(String name, String unit) {
        this.name = name;
        this.unit = unit;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += name != null ? name.hashCode() : 0;
        hash += unit != null ? unit.hashCode() : 0;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UnitParam)) {
            return false;
        }
        UnitParam other = (UnitParam) object;
        if (name == null || unit == null) return false;
        return (name.equals(other.getName()) && unit.equals(other.getUnit()));
    }

    @Override
    public String toString() {
        return "com.doogetha.buildtool.server.db.entity.UnitParam[ name=" + name + ", unit=" + unit + " ]";
    }
    
}
