package com.github.adminfaces.app.service;

import com.github.adminfaces.persistence.model.Filter;
import com.github.adminfaces.persistence.service.CrudService;
import com.github.adminfaces.template.exception.BusinessException;
import org.apache.deltaspike.data.api.criteria.Criteria;
import com.github.adminfaces.app.model.Talk;
import com.github.adminfaces.app.model.Talk_;
import com.github.adminfaces.app.bean.AppLists;
import com.github.adminfaces.app.repository.TalkRepository;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;
import com.github.adminfaces.app.model.Speaker;      
import com.github.adminfaces.app.model.Speaker_;   
import com.github.adminfaces.app.model.Room;      
import com.github.adminfaces.app.model.Room_;   
import javax.persistence.criteria.JoinType;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import static com.github.adminfaces.template.util.Assert.has;


 
@Stateless
public class TalkService extends CrudService<Talk, Long> implements Serializable {

    @Inject
    protected TalkRepository talkRepository;// you can use repositories to extract complex queries from your service

    @Inject
    protected AppLists appLists;

    @Override
    public void afterAll(Talk talk) {
        appLists.clearTalks();//invalidate Talk list cache
    }

    /** 
     * This method is used for (real) pagination and is called by lazy 
     * PrimeFaces datatable on list page
     *
     * @param filter holds restrictions populated on the list page
     * @return a criteria populated with given restrictions 
     */ 
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    protected Criteria<Talk, Talk> configRestrictions(Filter<Talk> filter) {
        Criteria<Talk, Talk> criteria = criteria();
        
        if (filter.hasParam(Talk_.id.getName())) {
            criteria.eq(Talk_.id, filter.getLongParam(Talk_.id.getName()));   
        }  
        if (filter.hasParam(Talk_.title.getName())) {
            criteria.eq(Talk_.title, (String)filter.getParam(Talk_.title.getName()));   
        }  
        if (filter.hasParam(Talk_.description.getName())) {
            criteria.eq(Talk_.description, (String)filter.getParam(Talk_.description.getName()));   
        }  
        if (filter.hasParam(Talk_.date.getName())) {
            criteria.eq(Talk_.date, (Date)filter.getParam(Talk_.date.getName()));   
        }  
        if (filter.hasParam(Talk_.speaker.getName())) {
            criteria.join(Talk_.speaker,
                where(Speaker.class, JoinType.LEFT)
               .eq(Speaker_.id, ((Speaker) filter.getParam("speaker")).getId()));
        }  
        if (filter.hasParam(Talk_.room.getName())) {
            criteria.join(Talk_.room,
                where(Room.class, JoinType.LEFT)
               .eq(Room_.id, ((Room) filter.getParam("room")).getId()));
        }  

        //create restrictions based on filter entity
        if (has(filter.getEntity())) {
            Talk filterEntity = filter.getEntity();
            
	        if (has(filterEntity.getId())) {
                 criteria.eq(Talk_.id, filterEntity.getId());   
	        }  
	        if (has(filterEntity.getTitle())) {
                 criteria.eq(Talk_.title, filterEntity.getTitle());   
	        }  
	        if (has(filterEntity.getDescription())) {
                 criteria.eq(Talk_.description, filterEntity.getDescription());   
	        }  
	        if (has(filterEntity.getDate())) {
                 criteria.eq(Talk_.date, filterEntity.getDate());   
	        }  
	        if (has(filterEntity.getSpeaker())) {
                 criteria.join(Talk_.speaker,
                    where(Speaker.class, JoinType.LEFT)
                    .eq(Speaker_.id, filterEntity.getSpeaker().getId()));
	        }  
	        if (has(filterEntity.getRoom())) {
                 criteria.join(Talk_.room,
                    where(Room.class, JoinType.LEFT)
                    .eq(Room_.id, filterEntity.getRoom().getId()));
	        }  
        }
        return criteria;
    }
    
    public void beforeInsert(Talk talk) {
        validate(talk);
    }

    public void beforeUpdate(Talk talk) {
        validate(talk);
    }


    public void validate(Talk talk) {
        BusinessException be = new BusinessException();

        /** just an example of validation
        if (!car.hasModel()) {
            be.addException(new BusinessException("Car model cannot be empty"));
        }
        if (!car.hasName()) {
            be.addException(new BusinessException("Car name cannot be empty"));
        }

        if (!has(car.getPrice())) {
            be.addException(new BusinessException("Car price cannot be empty"));
        } 

        if (count(criteria()
                .eqIgnoreCase(Car_.name, car.getName())
                .notEq(Car_.id, car.getId())) > 0) {

            be.addException(new BusinessException("Car name must be unique"));
        }
        **/

        //if there is exceptions enqueued then throw them. Each business exception will be transformed into a JSF error message and displayed on the page
        if (has(be.getExceptionList())) {
            throw be;
        }
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Speaker> getSpeakersByFirstname(String query) {
        return criteria(Speaker.class)
               .likeIgnoreCase(Speaker_.firstname, "%" + query + "%")
               .getResultList();
    }
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Room> getRoomsByName(String query) {
        return criteria(Room.class)
               .likeIgnoreCase(Room_.name, "%" + query + "%")
               .getResultList();
    }

 
}
