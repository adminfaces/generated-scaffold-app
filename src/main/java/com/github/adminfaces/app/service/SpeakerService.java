package com.github.adminfaces.app.service;

import com.github.adminfaces.persistence.model.Filter;
import com.github.adminfaces.persistence.service.CrudService;
import com.github.adminfaces.template.exception.BusinessException;
import org.apache.deltaspike.data.api.criteria.Criteria;
import com.github.adminfaces.app.model.Speaker;
import com.github.adminfaces.app.model.Speaker_;
import com.github.adminfaces.app.bean.AppLists;
import com.github.adminfaces.app.repository.SpeakerRepository;
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
public class SpeakerService extends CrudService<Speaker, Long> implements Serializable {

    @Inject
    protected SpeakerRepository speakerRepository;// you can use repositories to extract complex queries from your service

    @Inject
    protected AppLists appLists;

    @Override
    public void afterAll(Speaker speaker) {
        appLists.clearSpeakers();//invalidate Speaker list cache
    }

    /** 
     * This method is used for (real) pagination and is called by lazy 
     * PrimeFaces datatable on list page
     *
     * @param filter holds restrictions populated on the list page
     * @return a criteria populated with given restrictions 
     */ 
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    protected Criteria<Speaker, Speaker> configRestrictions(Filter<Speaker> filter) {
        Criteria<Speaker, Speaker> criteria = criteria();
        
        if (filter.hasParam(Speaker_.id.getName())) {
            criteria.eq(Speaker_.id, filter.getLongParam(Speaker_.id.getName()));   
        }  
        if (filter.hasParam(Speaker_.firstname.getName())) {
            criteria.eq(Speaker_.firstname, (String)filter.getParam(Speaker_.firstname.getName()));   
        }  
        if (filter.hasParam(Speaker_.surname.getName())) {
            criteria.eq(Speaker_.surname, (String)filter.getParam(Speaker_.surname.getName()));   
        }  
        if (filter.hasParam(Speaker_.bio.getName())) {
            criteria.eq(Speaker_.bio, (String)filter.getParam(Speaker_.bio.getName()));   
        }  
        if (filter.hasParam(Speaker_.twitter.getName())) {
            criteria.eq(Speaker_.twitter, (String)filter.getParam(Speaker_.twitter.getName()));   
        }  
        if (filter.hasParam(Speaker_.talks.getName())) {
            criteria.distinct().join(Speaker_.talks, where(Talk.class, JoinType.LEFT)
                .in(Talk_.id, toListOfIds((Set<Talk>) filter.getParam(Speaker_.talks.getName()), new Long[0])));   
        }  

        //create restrictions based on filter entity
        if (has(filter.getEntity())) {
            Speaker filterEntity = filter.getEntity();
            
	        if (has(filterEntity.getId())) {
                 criteria.eq(Speaker_.id, filterEntity.getId());   
	        }  
	        if (has(filterEntity.getFirstname())) {
                 criteria.eq(Speaker_.firstname, filterEntity.getFirstname());   
	        }  
	        if (has(filterEntity.getSurname())) {
                 criteria.eq(Speaker_.surname, filterEntity.getSurname());   
	        }  
	        if (has(filterEntity.getBio())) {
                 criteria.eq(Speaker_.bio, filterEntity.getBio());   
	        }  
	        if (has(filterEntity.getTwitter())) {
                 criteria.eq(Speaker_.twitter, filterEntity.getTwitter());   
	        }  
	        if (has(filterEntity.getTalks())) {
                 criteria.distinct().join(Speaker_.talks, where(Talk.class, JoinType.LEFT)
 	  	    .in(Talk_.id, toListOfIds(filterEntity.getTalks(), new Long[0])));   
	        }  
        }
        return criteria;
    }
    
    public void beforeInsert(Speaker speaker) {
        validate(speaker);
    }

    public void beforeUpdate(Speaker speaker) {
        validate(speaker);
    }

    @Override
    public void beforeRemove(Speaker speaker) {
        Criteria<Speaker, Speaker> speakerTalksCriteria = criteria().
            join(Speaker_.talks, where(Talk.class, JoinType.LEFT)
                .eq(Talk_.speaker, speaker));
        
        if(count(speakerTalksCriteria) > 0) {
            throw new BusinessException("Cannot remove "+speaker.getFirstname() + " because it has talks associated.");
        }

    }

    public void validate(Speaker speaker) {
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
    public List<Talk> getTalksBySpeakerId(Long speakerId) {
         return getEntityManager().createQuery("select r from Talk r where r.speaker.id =:id",Talk.class)
                .setParameter("id",speakerId)
                .getResultList(); 
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Speaker findById(Serializable id) {
        TypedQuery<Speaker> findById = getEntityManager().createQuery("select s from Speaker s left join fetch s.talks where s.id = :id", Speaker.class)
            .setParameter("id", id);
        try {
            return findById.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
 
}
