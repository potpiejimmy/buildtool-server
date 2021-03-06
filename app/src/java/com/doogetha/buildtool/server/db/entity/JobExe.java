/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doogetha.buildtool.server.db.entity;

import com.doogetha.buildtool.server.db.entity.pk.UnitNamePK;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author thorsten
 */
@Entity
@IdClass(UnitNamePK.class)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "JobExe.findByUnit", query = "SELECT b FROM JobExe b WHERE b.unit=:unit ORDER BY b.lastmodified DESC"),
    @NamedQuery(name = "JobExe.deleteByUnit", query = "DELETE FROM JobExe b WHERE b.unit=:unit"),
    @NamedQuery(name = "JobExe.findByUnitAndState", query = "SELECT b FROM JobExe b WHERE b.unit=:unit AND b.state=:state")})
public class JobExe implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    private String name;

    @Id
    private String unit;
    
    @NotNull
    private String state;
    
    @NotNull
    private long lastmodified;

    public JobExe() {
    }
    
    public JobExe(String name, String unit) {
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getLastmodified() {
        return lastmodified;
    }

    public void setLastmodified(long lastmodified) {
        this.lastmodified = lastmodified;
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
        if (!(object instanceof JobExe)) {
            return false;
        }
        JobExe other = (JobExe) object;
        if (name == null || unit == null) return false;
        return (name.equals(other.getName()) && unit.equals(other.getUnit()));
    }

    @Override
    public String toString() {
        return "com.doogetha.buildtool.server.db.entity.JobExe[ name=" + name + ", unit=" + unit + " ]";
    }
    
}
