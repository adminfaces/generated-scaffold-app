package com.github.adminfaces.app.service;

import com.github.adminfaces.app.model.Speaker;
import com.github.adminfaces.app.service.SpeakerService;
import com.github.adminfaces.app.model.Address;  
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
public class SpeakerServiceIt {

    @Inject
    SpeakerService speakerService;

    @Test
    @DataSet(value="speaker.yml")
    public void shouldFindSpeaker() {
        Speaker speaker = speakerService.findById(-1L);
        assertThat(speaker).isNotNull();
    } 

    @Test
    @DataSet(cleanBefore = true, disableConstraints = true)
    public void shouldInsertSpeaker() {
        Speaker speaker = new Speaker();
        speaker.setId(new Random().nextLong());
        speaker.setFirstname(randomString(100));
        speaker.setSurname(randomString(100));
        Address address = new Address();
        address.setStreet(randomString(50));
        address.setCity(randomString(50));
        address.setZipcode(new Random().nextInt());
        speaker.setAddress(address);
        Speaker savedSpeaker = speakerService.saveOrUpdate(speaker);
        assertThat(savedSpeaker.getId()).isNotNull();
    } 

    @Test
    @DataSet(value="speaker.yml")
    public void shouldRemoveSpeaker() {
        assertThat(speakerService.count()).isEqualTo(1L);
        Speaker speaker = speakerService.findById(-1L);
        assertThat(speaker).isNotNull();
        speakerService.remove(speaker);
        assertThat(speakerService.count()).isEqualTo(0L);
    }

    @Test
    @DataSet(value="speaker.yml", disableConstraints = true)
    public void shouldUpdateSpeaker() {
        Speaker speaker = speakerService.findById(-1L);
        assertThat(speaker).isNotNull();
        String firstname = randomString(100);
        speaker.setFirstname(firstname);
        String surname = randomString(100);
        speaker.setSurname(surname);
        String addressStreet = randomString(50);
        speaker.getAddress().setStreet(addressStreet); 
        String addressCity = randomString(50);
        speaker.getAddress().setCity(addressCity); 
        Integer addressZipcode = new Random().nextInt();
        speaker.getAddress().setZipcode(addressZipcode); 
        speaker = speakerService.saveOrUpdate(speaker);
        assertThat(speaker.getFirstname()).isEqualTo(firstname);
        assertThat(speaker.getSurname()).isEqualTo(surname);
        assertThat(speaker.getAddress().getStreet()).isEqualTo(addressStreet);
        assertThat(speaker.getAddress().getCity()).isEqualTo(addressCity);
        assertThat(speaker.getAddress().getZipcode()).isEqualTo(addressZipcode);
    }
    
    public String randomString(int size) {
        String value = UUID.randomUUID().toString();
        if(value.length() > size) {
            value = value.substring(0,size-1);
        }
        return value;    
    }

}