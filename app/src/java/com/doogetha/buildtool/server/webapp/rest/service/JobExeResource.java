/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doogetha.buildtool.server.webapp.rest.service;

import com.doogetha.buildtool.server.db.entity.JobExe;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.bean.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 *
 * @author thorsten
 */
@Path("/jobs/{unit}/{name}")
@Stateless
public class JobExeResource {
    
    @PersistenceContext(unitName = "buildtool-warPU")
    private EntityManager em;

    @GET
    @Produces({"application/json"})
    public JobExe getJob(@PathParam("unit") String unit, @PathParam("name") String name, @QueryParam("set") String setState) {
        JobExe job = null;
        try {
            job = em.createNamedQuery("JobExe.findByUnitAndName", JobExe.class).setParameter("unit", unit).setParameter("name", name).getSingleResult();
        } catch (Exception ex) {
            job = null;
        }
        if (setState != null) {
            if (job == null) job = new JobExe(name, unit);
                
            job.setState(setState);
            job.setLastmodified(System.currentTimeMillis());
            
            if (em.contains(job)) {
                em.merge(job);
            } else {
                em.persist(job);
            }
        }
        return job;
    }
}
