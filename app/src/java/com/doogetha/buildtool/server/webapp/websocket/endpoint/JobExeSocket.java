/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doogetha.buildtool.server.webapp.websocket.endpoint;

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
@ServerEndpoint("/ws/{unit}/{name}")
public class JobExeSocket {

    @OnOpen
    public void onOpen(Session session) {
    }

    @OnClose
    public void onClose(Session session) {
    }

    @OnMessage
    public void onMessage(String message, Session session, @PathParam("unit") String unit, @PathParam("name") String name) {
        try {
            // ECHO message
            session.getBasicRemote().sendText("BUILDTOOL: " + message);
        } catch (IOException ex) {
            Logger.getLogger(JobExeSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @OnError
    public void onError(Throwable t) {
    }
    
}
