/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.doogetha.buildtool.server.webapp.websocket;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thorsten
 * @param <S> type of session
 */
public class SessionManager<S> {
    
    protected static Map<Class,SessionManager> instances = Collections.synchronizedMap(new HashMap<>());
    
    protected Map<Object,S> sessions = Collections.synchronizedMap(new HashMap<>());
    
    protected Class<S> sessionType;
    
    public SessionManager(Class<S> sessionType) {
        this.sessionType = sessionType;
    }
    
    public static <S> SessionManager<S> getInstance(Class<S> sessionType) {
        SessionManager<S> s = instances.get(sessionType);
        if (s == null) {
            s = new SessionManager<>(sessionType);
            instances.put(sessionType, s);
        }
        return s;
    }
    
    public S getSession(Object key) {
        S s = sessions.get(key);
        if (s == null) {
            try {
                s = sessionType.newInstance();
                sessions.put(key, s);
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(SessionManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return s;
    }
    
    public void removeSession(Object key) {
        sessions.remove(key);
    }
}
