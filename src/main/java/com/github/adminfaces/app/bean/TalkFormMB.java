/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.app.bean;

import com.github.adminfaces.persistence.bean.CrudMB;
import com.github.adminfaces.app.model.Talk;
import com.github.adminfaces.app.service.TalkService;
import org.omnifaces.cdi.ViewScoped;
import org.omnifaces.util.Faces;
import javax.inject.Named;
import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import static com.github.adminfaces.persistence.util.Messages.addDetailMessage;
import static com.github.adminfaces.persistence.util.Messages.getMessage;

import java.util.*;
import com.github.adminfaces.app.model.Speaker;      
import com.github.adminfaces.app.model.Room;      


@Named
@ViewScoped
public class TalkFormMB extends CrudMB<Talk> implements Serializable {

    @Inject
    TalkService talkService;

    @Inject
    public void initService() {
        setCrudService(talkService);
    }

    @Override
    public void afterRemove() {
        try {
            super.afterRemove();//adds remove message
            Faces.redirect("talk/talk-list.xhtml");
            clear(); 
            sessionFilter.clear(TalkFormMB.class.getName());//removes filter saved in session for this bean.
        } catch (Exception e) {
            log.log(Level.WARNING, "",e);
        }
    }

    public List<Speaker> completeSpeaker(String query) {
        return talkService.getSpeakersByFirstname(query);
    }
    public List<Room> completeRoom(String query) {
        return talkService.getRoomsByName(query);
    }
 

}
