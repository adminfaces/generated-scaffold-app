package com.github.adminfaces.app.service;

import com.github.adminfaces.app.model.Talk;
import com.github.adminfaces.app.service.TalkService;
import com.github.adminfaces.app.model.Speaker;  
import com.github.adminfaces.app.model.Room;  
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
public class TalkServiceIt {

    @Inject
    TalkService talkService;

    @Test
    @DataSet(value="talk.yml")
    public void shouldFindTalk() {
        Talk talk = talkService.findById(-1L);
        assertThat(talk).isNotNull();
    } 

    @Test
    @DataSet(cleanBefore = true, disableConstraints = true)
    public void shouldInsertTalk() {
        Talk talk = new Talk();
        talk.setId(new Random().nextLong());
        talk.setTitle(randomString(100));
        talk.setDate(new Date());
        Talk savedTalk = talkService.saveOrUpdate(talk);
        assertThat(savedTalk.getId()).isNotNull();
    } 

    @Test
    @DataSet(value="talk.yml")
    public void shouldRemoveTalk() {
        assertThat(talkService.count()).isEqualTo(1L);
        Talk talk = talkService.findById(-1L);
        assertThat(talk).isNotNull();
        talkService.remove(talk);
        assertThat(talkService.count()).isEqualTo(0L);
    }

    @Test
    @DataSet(value="talk.yml", disableConstraints = true)
    public void shouldUpdateTalk() {
        Talk talk = talkService.findById(-1L);
        assertThat(talk).isNotNull();
        String title = randomString(100);
        talk.setTitle(title);
        Date date = new Date();
        talk.setDate(date);
        talk = talkService.saveOrUpdate(talk);
        assertThat(talk.getTitle()).isEqualTo(title);
        assertThat(talk.getDate()).isEqualTo(date);
    }
    
    public String randomString(int size) {
        String value = UUID.randomUUID().toString();
        if(value.length() > size) {
            value = value.substring(0,size-1);
        }
        return value;    
    }

}