/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doogetha.buildtool.server.webapp.rest.service;

import com.doogetha.buildtool.server.db.entity.JobExe;
import com.doogetha.buildtool.server.ejb.JobExeBean;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
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
@RequestScoped
public class JobExeResource {
    
    @EJB
    private JobExeBean jobExeEjb;
    
    @GET
    @Produces({"application/json"})
    public JobExe getJob(@PathParam("unit") String unit, @PathParam("name") String name, @QueryParam("set") String setState) {
        JobExe ret = jobExeEjb.getJob(unit, name, setState);
        if ("pending".equals(setState)) {
            // notify about new pending jobs for this unit
            Object semaphore = JobExeListResource.getPollingSemaphore(unit);
            synchronized (semaphore) { 
                semaphore.notifyAll();
            }
        }
        return ret;
    }
    
    @DELETE
    public void deleteJob(@PathParam("unit") String unit, @PathParam("name") String name) {
        jobExeEjb.deleteJob(unit, name);
    }
}
