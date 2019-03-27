package com.github.adminfaces.app.service;

import com.github.adminfaces.persistence.model.Filter;
import com.github.adminfaces.persistence.service.CrudService;
import com.github.adminfaces.template.exception.BusinessException;
import org.apache.deltaspike.data.api.criteria.Criteria;
import com.github.adminfaces.app.model.Room;
import com.github.adminfaces.app.model.Room_;
import com.github.adminfaces.app.bean.AppLists;
import com.github.adminfaces.app.repository.RoomRepository;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.*;
import com.github.adminfaces.app.model.Talk;   
import com.github.adminfaces.app.model.Talk_;   
import javax.persistence.TypedQuery;
import javax.persistence.NoResultException;
import javax.persistence.criteria.JoinType;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import static com.github.adminfaces.template.util.Assert.has;


 
@Stateless
public class RoomService extends CrudService<Room, Long> implements Serializable {

    @Inject
    protected RoomRepository roomRepository;// you can use repositories to extract complex queries from your service

    @Inject
    protected AppLists appLists;

    @Override
    public void afterAll(Room room) {
        appLists.clearRooms();//invalidate Room list cache
    }

    /** 
     * This method is used for (real) pagination and is called by lazy 
     * PrimeFaces datatable on list page
     *
     * @param filter holds restrictions populated on the list page
     * @return a criteria populated with given restrictions 
     */ 
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    protected Criteria<Room, Room> configRestrictions(Filter<Room> filter) {
        Criteria<Room, Room> criteria = criteria();
        
        if (filter.hasParam(Room_.id.getName())) {
            criteria.eq(Room_.id, filter.getLongParam(Room_.id.getName()));   
        }  
        if (filter.hasParam(Room_.name.getName())) {
            criteria.eq(Room_.name, (String)filter.getParam(Room_.name.getName()));   
        }  
        if (filter.hasParam(Room_.capacity.getName())) {
            criteria.eq(Room_.capacity, (Short)filter.getParam(Room_.capacity.getName()));   
        }  
        if (filter.hasParam(Room_.hasWifi.getName())) {
            criteria.eq(Room_.hasWifi, filter.getBooleanParam(Room_.hasWifi.getName()));   
        }  
        if (filter.hasParam(Room_.talks.getName())) {
            criteria.distinct().join(Room_.talks, where(Talk.class, JoinType.LEFT)
                .in(Talk_.id, toListOfIds((Set<Talk>) filter.getParam(Room_.talks.getName()), new Long[0])));   
        }  

        //create restrictions based on filter entity
        if (has(filter.getEntity())) {
            Room filterEntity = filter.getEntity();
            
	        if (has(filterEntity.getId())) {
                 criteria.eq(Room_.id, filterEntity.getId());   
	        }  
	        if (has(filterEntity.getName())) {
                 criteria.eq(Room_.name, filterEntity.getName());   
	        }  
	        if (has(filterEntity.getCapacity())) {
                 criteria.eq(Room_.capacity, filterEntity.getCapacity());   
	        }  
	        if (has(filterEntity.getHasWifi())) {
                 criteria.eq(Room_.hasWifi, filterEntity.getHasWifi());   
	        }  
	        if (has(filterEntity.getTalks())) {
                 criteria.distinct().join(Room_.talks, where(Talk.class, JoinType.LEFT)
 	  	    .in(Talk_.id, toListOfIds(filterEntity.getTalks(), new Long[0])));   
	        }  
        }
        return criteria;
    }
    
    public void beforeInsert(Room room) {
        validate(room);
    }

    public void beforeUpdate(Room room) {
        validate(room);
    }

    @Override
    public void beforeRemove(Room room) {
        Criteria<Room, Room> roomTalksCriteria = criteria().
            join(Room_.talks, where(Talk.class, JoinType.LEFT)
                .eq(Talk_.room, room));
        
        if(count(roomTalksCriteria) > 0) {
            throw new BusinessException("Cannot remove "+room.getName() + " because it has talks associated.");
        }

    }

    public void validate(Room room) {
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
    public List<Talk> getTalksByRoomId(Long roomId) {
         return getEntityManager().createQuery("select r from Talk r where r.room.id =:id",Talk.class)
                .setParameter("id",roomId)
                .getResultList(); 
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Room findById(Serializable id) {
        TypedQuery<Room> findById = getEntityManager().createQuery("select s from Room s left join fetch s.talks where s.id = :id", Room.class)
            .setParameter("id", id);
        try {
            return findById.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
 
}
