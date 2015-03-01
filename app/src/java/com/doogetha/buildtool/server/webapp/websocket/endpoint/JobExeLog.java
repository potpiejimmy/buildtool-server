/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doogetha.buildtool.server.webapp.websocket.endpoint;

import com.doogetha.buildtool.server.db.entity.pk.UnitNamePK;
import com.doogetha.buildtool.server.webapp.websocket.JobExeSession;
import com.doogetha.buildtool.server.webapp.websocket.SessionManager;

/**
 *
 * @author thorsten
 */
public abstract class JobExeLog {
    
    public void releaseSessionIfDone(String unit, String name) {
        SessionManager<JobExeSession> sesmgr = SessionManager.getInstance(JobExeSession.class);
        UnitNamePK id = new UnitNamePK(unit, name);
        JobExeSession jsession = sesmgr.getSession(id);
        if (jsession.getPublisher() == null &&
            jsession.getSubscriber() == null) 
            sesmgr.removeSession(id);
    }
}
