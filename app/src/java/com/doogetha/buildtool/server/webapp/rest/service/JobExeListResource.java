/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doogetha.buildtool.server.webapp.rest.service;

import com.doogetha.buildtool.server.db.entity.JobExe;
import com.doogetha.buildtool.server.ejb.JobExeBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

/**
 *
 * @author thorsten
 */
@Path("/jobs/{unit}")
@RequestScoped
public class JobExeListResource {
    
    @EJB
    private JobExeBean jobExeEjb;

    protected final static Map<String,Object> POLLING_NOTIFIERS = new HashMap<>();

    public static Object getPollingSemaphore(String unit) {
        unit = unit.toLowerCase();
        synchronized (POLLING_NOTIFIERS) {
            Object semaphore = POLLING_NOTIFIERS.get(unit);
            if (semaphore == null) {
                semaphore = new Object();
                POLLING_NOTIFIERS.put(unit, semaphore);
            }
            return semaphore;
        }
    }
    
    public void waitForChange(String unit) {
        Object semaphore = getPollingSemaphore(unit);
        synchronized(semaphore) {
            semaphore.notifyAll();
            try { semaphore.wait(120000); }
            catch (Exception ex) {}
        }
    }
    
    @GET
    @Produces({"application/xml","application/json"})
    public List<JobExe> getJobList(@Context HttpServletResponse hsr, @PathParam("unit") String unit, @QueryParam("state") String filterState, @QueryParam("wait") Boolean wait) {
        List<JobExe> result = null;
        result = jobExeEjb.getJobList(unit, filterState);
        if (wait != null && wait && result.isEmpty()) {
            waitForChange(unit);
            result = jobExeEjb.getJobList(unit, filterState);
        }
        return result;
    }
    
    @DELETE
    public void deleteAllJobs(@PathParam("unit") String unit) {
        jobExeEjb.deleteAllJobs(unit);
    }
}
