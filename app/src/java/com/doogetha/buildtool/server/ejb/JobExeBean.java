/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doogetha.buildtool.server.ejb;

import com.doogetha.buildtool.server.db.entity.JobExe;
import com.doogetha.buildtool.server.db.entity.pk.UnitNamePK;
import com.doogetha.buildtool.server.webapp.websocket.JobExeSession;
import com.doogetha.buildtool.server.webapp.websocket.SessionManager;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author thorsten.liese
 */
@Stateless
public class JobExeBean {
    
    @PersistenceContext(unitName = "buildtool-warPU")
    private EntityManager em;

    public List<JobExe> getJobList(String unit, String filterState) {
        if (filterState != null) {
            return em.createNamedQuery("JobExe.findByUnitAndState", JobExe.class).setParameter("unit", unit).setParameter("state", filterState).getResultList();
        } else {
            return em.createNamedQuery("JobExe.findByUnit", JobExe.class).setParameter("unit", unit).getResultList();
        }
    }
    
    public JobExe getJob(String unit, String name, String setState) {
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
        }
        return job;
    }
    
    public void deleteAllJobs(String unit) {
        getJobList(unit, null).forEach(i -> {
            em.remove(i);
            // remove associated log session sockets
            SessionManager.getInstance(JobExeSession.class).removeSession(new UnitNamePK(i.getUnit(), i.getName()));
        });
    }
    
    public void deleteJob(String unit, String name) {
        JobExe job = getJob(unit, name, null);
        if (job != null) em.remove(job);
        // remove associated log session sockets
        SessionManager.getInstance(JobExeSession.class).removeSession(new UnitNamePK(unit, name));
    }

}
