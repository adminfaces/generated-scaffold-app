package com.github.adminfaces.app.bean;

import com.github.adminfaces.persistence.bean.CrudMB;
import com.github.adminfaces.persistence.util.Messages;
import com.github.adminfaces.template.exception.BusinessException;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

import com.github.adminfaces.app.model.Talk;
import com.github.adminfaces.app.service.TalkService;

import static com.github.adminfaces.persistence.util.Messages.addDetailMessage;
import static com.github.adminfaces.persistence.util.Messages.getMessage;
import static com.github.adminfaces.template.util.Assert.has;
import java.util.*;
import org.primefaces.PrimeFaces;
import com.github.adminfaces.app.model.Speaker;      
import com.github.adminfaces.app.model.Room;      

@Named
@ViewScoped
public class TalkListMB extends CrudMB<Talk> implements Serializable {

    @Inject
    TalkService talkService;

    @Inject
    public void initService() {
        setCrudService(talkService);
    }

    public void delete() {
        int deletedEntities = 0;
        for (Talk selected : selectionList) {
        	deletedEntities++;
        	talkService.remove(selected);
        }
        selectionList.clear();
        addDetailMessage(deletedEntities + " talk(s) deleted successfully!");
        clear();
    }

    /**
     * Used in datatable footer to display current search criteria
     */
    public String getSearchCriteria() {
        StringBuilder sb = new StringBuilder(21);
        Talk talkFilter = filter.getEntity();
 
        Long id = null;
        if (filter.hasParam("id")) {
            id = filter.getLongParam("id");
        } else if (has(talkFilter.getId())) {
            id = talkFilter.getId();
        }
        if (has(id)) {
  
	        sb.append("<b>id</b>: ").append(id).append(", ");
        }
 
        String title = null;
        if (filter.hasParam("title")) {
            title = (String)filter.getParam("title");
        } else if (has(talkFilter.getTitle())) {
            title = talkFilter.getTitle();
        }
        if (has(title)) {
  
	        sb.append("<b>title</b>: ").append(title).append(", ");
        }
 
        String description = null;
        if (filter.hasParam("description")) {
            description = (String)filter.getParam("description");
        } else if (has(talkFilter.getDescription())) {
            description = talkFilter.getDescription();
        }
        if (has(description)) {
  
	        sb.append("<b>description</b>: ").append(description).append(", ");
        }
 
        Date date = null;
        if (filter.hasParam("date")) {
            date = (Date)filter.getParam("date");
        } else if (has(talkFilter.getDate())) {
            date = talkFilter.getDate();
        }
        if (has(date)) {
  
	        sb.append("<b>date</b>: ").append(date).append(", ");
        }
 
        Speaker speaker = null;
        if (filter.hasParam("speaker")) {
            speaker = (Speaker)filter.getParam("speaker");
        } else if (has(talkFilter.getSpeaker())) {
            speaker = talkFilter.getSpeaker();
        }
        if (has(speaker)) {
            sb.append("<b>speaker</b>: ").append(speaker.getFirstname()).append(", ");
                    }
 
        Room room = null;
        if (filter.hasParam("room")) {
            room = (Room)filter.getParam("room");
        } else if (has(talkFilter.getRoom())) {
            room = talkFilter.getRoom();
        }
        if (has(room)) {
            sb.append("<b>room</b>: ").append(room.getName()).append(", ");
                    }
        int commaIndex = sb.lastIndexOf(",");
        if (commaIndex != -1) {
            sb.deleteCharAt(commaIndex);
        }
        if (sb.toString().trim().isEmpty()) {
            PrimeFaces.current().executeScript("jQuery('div[id=footer] .fa-filter').addClass('ui-state-disabled')");
            return getMessage("empty-search-criteria");
        } else {
            PrimeFaces.current().executeScript("jQuery('div[id=footer] .fa-filter').removeClass('ui-state-disabled')");
        }
        return sb.toString();
    }

    public List<Speaker> completeSpeaker(String query) {
        return talkService.getSpeakersByFirstname(query);
    }
    public List<Room> completeRoom(String query) {
        return talkService.getRoomsByName(query);
    }
}