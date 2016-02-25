/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doogetha.buildtool.server.webapp.rest.service;

import com.doogetha.buildtool.server.db.entity.JobExe;
import com.doogetha.buildtool.server.db.entity.pk.UnitNamePK;
import com.doogetha.buildtool.server.webapp.websocket.JobExeSession;
import com.doogetha.buildtool.server.webapp.websocket.SessionManager;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 *
 * @author thorsten
 */
@Path("/jobs/{unit}/{name:.+}")
@Stateless
public class JobExeResource {
    
    @PersistenceContext(unitName = "buildtool-warPU")
    private EntityManager em;
    
    @GET
    @Produces({"application/json"})
    public JobExe getJob(@PathParam("unit") String unit, @PathParam("name") String name, @QueryParam("set") String setState) {
        JobExe job = em.find(JobExe.class, new UnitNamePK(unit, name));
        if (setState != null) {
            if (job == null) job = new JobExe(name, unit);
                
            job.setState(setState);
            job.setLastmodified(System.currentTimeMillis());
            
            if (em.contains(job)) {
                em.merge(job);
            } else {
                em.persist(job);
            }
            if (setState.equals("pending")) {
                // notify all about new pending jobs
                synchronized (JobExeListResource.GLOBAL_NOTIFIER) { 
                    JobExeListResource.GLOBAL_NOTIFIER.notifyAll();
                }
            }
        }
        return job;
    }
    
    @DELETE
    public void deleteJob(@PathParam("unit") String unit, @PathParam("name") String name) {
        JobExe job = getJob(unit, name, null);
        if (job != null) em.remove(job);
        // remove associated log session sockets
        SessionManager.getInstance(JobExeSession.class).removeSession(new UnitNamePK(unit, name));
    }
}
