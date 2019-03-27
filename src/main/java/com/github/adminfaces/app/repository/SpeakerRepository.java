package com.github.adminfaces.app.repository;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;
import com.github.adminfaces.app.model.Speaker;

@Repository
public interface SpeakerRepository extends EntityRepository<Speaker,Long> {

}