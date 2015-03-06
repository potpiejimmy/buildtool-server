/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doogetha.buildtool.server.webapp.websocket.endpoint;

import com.doogetha.buildtool.server.db.entity.pk.UnitNamePK;
import com.doogetha.buildtool.server.webapp.websocket.JobExeSession;
import com.doogetha.buildtool.server.webapp.websocket.SessionManager;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author thorsten.liese
 */
@ServerEndpoint("/ws/exelog/out/{unit}/{name}")
public class JobExeLogOut {

    @OnOpen
    public void onOpen(Session session, @PathParam("unit") String unit, @PathParam("name") String name) {
        JobExeSession jsession = SessionManager.getInstance(JobExeSession.class).getSession(new UnitNamePK(unit, name));
        jsession.setSubscriber(session);
        // send last cached messages to new subscriber:
        jsession.resendRecentMessages();
    }

    @OnClose
    public void onClose(Session session, @PathParam("unit") String unit, @PathParam("name") String name) {
        SessionManager.getInstance(JobExeSession.class).getSession(new UnitNamePK(unit, name)).setSubscriber(null);
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("unit") String unit, @PathParam("name") String name) {
        try {
            session.getBasicRemote().sendText("Sorry, you cannot broadcast on this channel.\n");
        } catch (IOException ex) {
            Logger.getLogger(JobExeLogIn.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @OnError
    public void onError(Throwable t) {
        Logger.getLogger(JobExeLogOut.class.getName()).log(Level.SEVERE, null, t);
    }
    
}
