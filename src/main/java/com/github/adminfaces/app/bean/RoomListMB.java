package com.github.adminfaces.app.bean;

import com.github.adminfaces.persistence.bean.CrudMB;
import com.github.adminfaces.persistence.util.Messages;
import com.github.adminfaces.template.exception.BusinessException;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

import com.github.adminfaces.app.model.Room;
import com.github.adminfaces.app.service.RoomService;

import static com.github.adminfaces.persistence.util.Messages.addDetailMessage;
import static com.github.adminfaces.persistence.util.Messages.getMessage;
import static com.github.adminfaces.template.util.Assert.has;
import java.util.*;
import org.primefaces.PrimeFaces;
import com.github.adminfaces.app.model.Talk;   

@Named
@ViewScoped
public class RoomListMB extends CrudMB<Room> implements Serializable {

    @Inject
    RoomService roomService;

    List<Talk> roomTalks;
 
    Map<Long,Boolean> showTalksDetailMap = new HashMap<>();//used to show details in datatable rows    

    @Inject
    public void initService() {
        setCrudService(roomService);
    }

    public void delete() {
        int deletedEntities = 0;
        for (Room selected : selectionList) {
        	deletedEntities++;
        	roomService.remove(selected);
        }
        selectionList.clear();
        addDetailMessage(deletedEntities + " room(s) deleted successfully!");
        clear();
    }

    /**
     * Used in datatable footer to display current search criteria
     */
    public String getSearchCriteria() {
        StringBuilder sb = new StringBuilder(21);
        Room roomFilter = filter.getEntity();
 
        Long id = null;
        if (filter.hasParam("id")) {
            id = filter.getLongParam("id");
        } else if (has(roomFilter.getId())) {
            id = roomFilter.getId();
        }
        if (has(id)) {
  
	        sb.append("<b>id</b>: ").append(id).append(", ");
        }
 
        String name = null;
        if (filter.hasParam("name")) {
            name = (String)filter.getParam("name");
        } else if (has(roomFilter.getName())) {
            name = roomFilter.getName();
        }
        if (has(name)) {
  
	        sb.append("<b>name</b>: ").append(name).append(", ");
        }
 
        Short capacity = null;
        if (filter.hasParam("capacity")) {
            capacity = (Short)filter.getParam("capacity");
        } else if (has(roomFilter.getCapacity())) {
            capacity = roomFilter.getCapacity();
        }
        if (has(capacity)) {
  
	        sb.append("<b>capacity</b>: ").append(capacity).append(", ");
        }
 
        Boolean hasWifi = null;
        if (filter.hasParam("hasWifi")) {
            hasWifi = filter.getBooleanParam("hasWifi");
        } else if (has(roomFilter.getHasWifi())) {
            hasWifi = roomFilter.getHasWifi();
        }
        if (has(hasWifi)) {
  
	        sb.append("<b>hasWifi</b>: ").append(hasWifi).append(", ");
        }
        Set<Talk> talks = null;
        if (filter.hasParam("talks")) {
           talks = (Set<Talk>) filter.getParam("talks");
        } else if (has(roomFilter.getTalks())) {
            talks = roomFilter.getTalks();
        }
        if (has(talks)) {
            sb.append("<b>talks</b>: ");
            for (Talk talk : talks) {
                sb.append(talk.getTitle()).append(", ");
            }
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

    public void showTalksDetail(Long id) {
        this.showTalksDetailMap.clear();//show details of one row at a time
        this.showTalksDetailMap.put(id,true);
        roomTalks = roomService.getTalksByRoomId(id);  
    }
    
    public List<Talk> getRoomTalks() {
        return roomTalks;
    }

    public void setroomTalks(List<Talk> roomTalks) {
        this.roomTalks = roomTalks;
    }

    public Map<Long,Boolean> getShowTalksDetailMap() {
        return showTalksDetailMap;
    }

}