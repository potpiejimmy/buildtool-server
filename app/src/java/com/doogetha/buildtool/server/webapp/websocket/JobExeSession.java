/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doogetha.buildtool.server.webapp.websocket;

import com.doogetha.buildtool.server.webapp.websocket.endpoint.JobExeLogIn;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.Session;

/**
 *
 * @author thorsten
 */
public class JobExeSession {

    private String lastMessage = null;
    
    private Session publisher = null;
    
    private Session subscriber = null;

    public synchronized String getLastMessage() {
        return lastMessage;
    }

    public synchronized void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public synchronized Session getPublisher() {
        return publisher;
    }

    public synchronized void setPublisher(Session publisher) {
        releaseEndpoint(this.publisher);
        this.publisher = publisher;
    }

    public synchronized Session getSubscriber() {
        return subscriber;
    }

    public synchronized void setSubscriber(Session subscriber) {
        releaseEndpoint(this.subscriber);
        this.subscriber = subscriber;
    }
    
    protected synchronized void releaseEndpoint(Session session) {
        if (session != null && session.isOpen()) {
            try {
                session.close(new CloseReason(CloseCodes.GOING_AWAY, "Sorry, replacing you with someone else."));
            } catch (IOException ex) {
                Logger.getLogger(JobExeSession.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public synchronized void publish(String message) {
        this.lastMessage = message;
        if (subscriber != null && subscriber.isOpen()) {
            try {
                subscriber.getBasicRemote().sendText(message);
            } catch (IOException ex) {
                try { subscriber.close(); } catch (IOException ex1) {}
                subscriber = null;
                Logger.getLogger(JobExeLogIn.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
