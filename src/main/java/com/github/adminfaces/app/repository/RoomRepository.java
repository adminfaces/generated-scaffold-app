package com.github.adminfaces.app.repository;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;
import com.github.adminfaces.app.model.Room;

@Repository
public interface RoomRepository extends EntityRepository<Room,Long> {

}