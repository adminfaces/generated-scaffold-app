package com.github.adminfaces.app.bean;

import com.github.adminfaces.persistence.bean.CrudMB;
import com.github.adminfaces.persistence.util.Messages;
import com.github.adminfaces.template.exception.BusinessException;
import org.omnifaces.cdi.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

import com.github.adminfaces.app.model.Speaker;
import com.github.adminfaces.app.service.SpeakerService;

import static com.github.adminfaces.persistence.util.Messages.addDetailMessage;
import static com.github.adminfaces.persistence.util.Messages.getMessage;
import static com.github.adminfaces.template.util.Assert.has;
import java.util.*;
import org.primefaces.PrimeFaces;
import com.github.adminfaces.app.model.Talk;   

@Named
@ViewScoped
public class SpeakerListMB extends CrudMB<Speaker> implements Serializable {

    @Inject
    SpeakerService speakerService;

    List<Talk> speakerTalks;
 
    Map<Long,Boolean> showTalksDetailMap = new HashMap<>();//used to show details in datatable rows    

    @Inject
    public void initService() {
        setCrudService(speakerService);
    }

    public void delete() {
        int deletedEntities = 0;
        for (Speaker selected : selectionList) {
        	deletedEntities++;
        	speakerService.remove(selected);
        }
        selectionList.clear();
        addDetailMessage(deletedEntities + " speaker(s) deleted successfully!");
        clear();
    }

    /**
     * Used in datatable footer to display current search criteria
     */
    public String getSearchCriteria() {
        StringBuilder sb = new StringBuilder(21);
        Speaker speakerFilter = filter.getEntity();
 
        Long id = null;
        if (filter.hasParam("id")) {
            id = filter.getLongParam("id");
        } else if (has(speakerFilter.getId())) {
            id = speakerFilter.getId();
        }
        if (has(id)) {
  
	        sb.append("<b>id</b>: ").append(id).append(", ");
        }
 
        String firstname = null;
        if (filter.hasParam("firstname")) {
            firstname = (String)filter.getParam("firstname");
        } else if (has(speakerFilter.getFirstname())) {
            firstname = speakerFilter.getFirstname();
        }
        if (has(firstname)) {
  
	        sb.append("<b>firstname</b>: ").append(firstname).append(", ");
        }
 
        String surname = null;
        if (filter.hasParam("surname")) {
            surname = (String)filter.getParam("surname");
        } else if (has(speakerFilter.getSurname())) {
            surname = speakerFilter.getSurname();
        }
        if (has(surname)) {
  
	        sb.append("<b>surname</b>: ").append(surname).append(", ");
        }
 
        String bio = null;
        if (filter.hasParam("bio")) {
            bio = (String)filter.getParam("bio");
        } else if (has(speakerFilter.getBio())) {
            bio = speakerFilter.getBio();
        }
        if (has(bio)) {
  
	        sb.append("<b>bio</b>: ").append(bio).append(", ");
        }
 
        String twitter = null;
        if (filter.hasParam("twitter")) {
            twitter = (String)filter.getParam("twitter");
        } else if (has(speakerFilter.getTwitter())) {
            twitter = speakerFilter.getTwitter();
        }
        if (has(twitter)) {
  
	        sb.append("<b>twitter</b>: ").append(twitter).append(", ");
        }
        Set<Talk> talks = null;
        if (filter.hasParam("talks")) {
           talks = (Set<Talk>) filter.getParam("talks");
        } else if (has(speakerFilter.getTalks())) {
            talks = speakerFilter.getTalks();
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
        speakerTalks = speakerService.getTalksBySpeakerId(id);  
    }
    
    public List<Talk> getSpeakerTalks() {
        return speakerTalks;
    }

    public void setspeakerTalks(List<Talk> speakerTalks) {
        this.speakerTalks = speakerTalks;
    }

    public Map<Long,Boolean> getShowTalksDetailMap() {
        return showTalksDetailMap;
    }

}