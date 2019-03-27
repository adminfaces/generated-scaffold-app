/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.app.bean;

import com.github.adminfaces.persistence.bean.CrudMB;
import com.github.adminfaces.app.model.Room;
import com.github.adminfaces.app.service.RoomService;
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


@Named
@ViewScoped
public class RoomFormMB extends CrudMB<Room> implements Serializable {

    @Inject
    RoomService roomService;

    @Inject
    public void initService() {
        setCrudService(roomService);
    }

    @Override
    public void afterRemove() {
        try {
            super.afterRemove();//adds remove message
            Faces.redirect("room/room-list.xhtml");
            clear(); 
            sessionFilter.clear(RoomFormMB.class.getName());//removes filter saved in session for this bean.
        } catch (Exception e) {
            log.log(Level.WARNING, "",e);
        }
    }

 

}
