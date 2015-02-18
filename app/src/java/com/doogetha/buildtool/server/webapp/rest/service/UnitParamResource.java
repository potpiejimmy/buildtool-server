/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doogetha.buildtool.server.webapp.rest.service;

import com.doogetha.buildtool.server.db.entity.UnitParam;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 *
 * @author thorsten
 */
@Path("/params/{unit}/{name}")
@Stateless
public class UnitParamResource {
    
    @PersistenceContext(unitName = "buildtool-warPU")
    private EntityManager em;

    @GET
    @Produces({"application/json"})
    public UnitParam getParam(@PathParam("unit") String unit, @PathParam("name") String name) {
        UnitParam param;
        try {
            param = em.createNamedQuery("UnitParam.findByUnitAndName", UnitParam.class).setParameter("unit", unit).setParameter("name", name).getSingleResult();
        } catch (Exception ex) {
            param = null;
        }
        return param;
    }
    
    @POST
    @Produces({"application/json"})
    @Consumes({"application/json"})
    public UnitParam setParam(@PathParam("unit") String unit, @PathParam("name") String name, UnitParam param) {
        UnitParam existing = getParam(unit, name);
        if (existing != null) {
            existing.setValue(param.getValue());
            em.merge(existing);
        } else {
            param.setUnit(unit);
            param.setName(name);
            em.persist(param);
        }
        return param;
    }
}
