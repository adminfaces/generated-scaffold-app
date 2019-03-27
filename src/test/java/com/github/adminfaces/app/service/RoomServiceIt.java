package com.github.adminfaces.app.service;

import com.github.adminfaces.app.model.Room;
import com.github.adminfaces.app.service.RoomService;
import com.github.database.rider.cdi.api.DBUnitInterceptor;
import com.github.database.rider.core.api.dataset.DataSet;
import javax.inject.Inject;
import org.apache.deltaspike.jpa.api.transaction.Transactional;
import org.apache.deltaspike.testcontrol.api.junit.CdiTestRunner;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
 
@RunWith(CdiTestRunner.class)
@DBUnitInterceptor
@Transactional
public class RoomServiceIt {

    @Inject
    RoomService roomService;

    @Test
    @DataSet(value="room.yml")
    public void shouldFindRoom() {
        Room room = roomService.findById(-1L);
        assertThat(room).isNotNull();
    } 

    @Test
    @DataSet(cleanBefore = true, disableConstraints = true)
    public void shouldInsertRoom() {
        Room room = new Room();
        room.setId(new Random().nextLong());
        room.setName(randomString(20));
        room.setCapacity(new Short((short) 42));
        Room savedRoom = roomService.saveOrUpdate(room);
        assertThat(savedRoom.getId()).isNotNull();
    } 

    @Test
    @DataSet(value="room.yml")
    public void shouldRemoveRoom() {
        assertThat(roomService.count()).isEqualTo(1L);
        Room room = roomService.findById(-1L);
        assertThat(room).isNotNull();
        roomService.remove(room);
        assertThat(roomService.count()).isEqualTo(0L);
    }

    @Test
    @DataSet(value="room.yml", disableConstraints = true)
    public void shouldUpdateRoom() {
        Room room = roomService.findById(-1L);
        assertThat(room).isNotNull();
        String name = randomString(20);
        room.setName(name);
        Short capacity = new Short((short) 42);
        room.setCapacity(capacity);
        room = roomService.saveOrUpdate(room);
        assertThat(room.getName()).isEqualTo(name);
        assertThat(room.getCapacity()).isEqualTo(capacity);
    }
    
    public String randomString(int size) {
        String value = UUID.randomUUID().toString();
        if(value.length() > size) {
            value = value.substring(0,size-1);
        }
        return value;    
    }

}