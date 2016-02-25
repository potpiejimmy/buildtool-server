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
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

/**
 *
 * @author thorsten
 */
@Path("/jobs/{unit}")
@Stateless
public class JobExeListResource {
    
    @PersistenceContext(unitName = "buildtool-warPU")
    private EntityManager em;

    public final static Object GLOBAL_NOTIFIER = new Object();

    @HEAD
    public void waitForChange() {
        synchronized(GLOBAL_NOTIFIER) {
            try { GLOBAL_NOTIFIER.wait(600000); }
            catch (Exception ex) {}
        }
    }
    
    @GET
    @Produces({"application/xml","application/json"})
    public List<JobExe> getJobList(@PathParam("unit") String unit, @QueryParam("state") String filterState, @QueryParam("wait") Boolean wait) {
        if (wait != null && wait) waitForChange();
        
        if (filterState != null) {
            return em.createNamedQuery("JobExe.findByUnitAndState", JobExe.class).setParameter("unit", unit).setParameter("state", filterState).getResultList();
        } else {
            return em.createNamedQuery("JobExe.findByUnit", JobExe.class).setParameter("unit", unit).getResultList();
        }
    }
    
    @DELETE
    public void deleteAllJobs(@PathParam("unit") String unit) {
        getJobList(unit, null, null).forEach(i -> {
            em.remove(i);
            // remove associated log session sockets
            SessionManager.getInstance(JobExeSession.class).removeSession(new UnitNamePK(i.getUnit(), i.getName()));
        });
    }
}
