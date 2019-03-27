package com.github.adminfaces.app.repository;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;
import com.github.adminfaces.app.model.Talk;

@Repository
public interface TalkRepository extends EntityRepository<Talk,Long> {

}