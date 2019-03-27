/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.app.bean;

import com.github.adminfaces.persistence.bean.CrudMB;
import com.github.adminfaces.app.model.Speaker;
import com.github.adminfaces.app.service.SpeakerService;
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
import com.github.adminfaces.app.model.Talk;   

import com.github.adminfaces.app.model.Address;      

@Named
@ViewScoped
public class SpeakerFormMB extends CrudMB<Speaker> implements Serializable {

    @Inject
    SpeakerService speakerService;

    @Inject
    public void initService() {
        setCrudService(speakerService);
    }

    @Override
    public void afterRemove() {
        try {
            super.afterRemove();//adds remove message
            Faces.redirect("speaker/speaker-list.xhtml");
            clear(); 
            sessionFilter.clear(SpeakerFormMB.class.getName());//removes filter saved in session for this bean.
        } catch (Exception e) {
            log.log(Level.WARNING, "",e);
        }
    }

 
    @Override
    public Speaker createEntity() {
        Speaker speaker = super.createEntity();
        speaker.setAddress(new Address());
        return speaker;  
    }

}
