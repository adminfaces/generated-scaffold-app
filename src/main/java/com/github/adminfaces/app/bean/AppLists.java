/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adminfaces.app.bean;

import com.github.adminfaces.persistence.service.CrudService;
import com.github.adminfaces.persistence.service.Service;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import com.github.adminfaces.app.model.Room;
import javax.enterprise.inject.Produces;
import com.github.adminfaces.app.model.Speaker;
import com.github.adminfaces.app.model.Talk;

/**
 *
 * Cache for lists used across multiple pages
 */
@Named
@ApplicationScoped
public class AppLists implements Serializable {

	@Inject
	@Service
	private CrudService<Room, Long> roomService;
	private Set<Room> rooms;

	@Inject
	@Service
	private CrudService<Speaker, Long> speakerService;
	private Set<Speaker> speakers;

	@Inject
	@Service
	private CrudService<Talk, Long> talkService;
	private Set<Talk> talks;

	@Produces
	@Named("allRooms")
	public Set<Room> allRooms() {
		if (rooms == null) {
			rooms = new HashSet<>(roomService.criteria().getResultList());
		}
		return rooms;
	}

	public void clearRooms() {
		rooms = null;
	}

	@Produces
	@Named("allSpeakers")
	public Set<Speaker> allSpeakers() {
		if (speakers == null) {
			speakers = new HashSet<>(speakerService.criteria().getResultList());
		}
		return speakers;
	}

	public void clearSpeakers() {
		speakers = null;
	}

	@Produces
	@Named("allTalks")
	public Set<Talk> allTalks() {
		if (talks == null) {
			talks = new HashSet<>(talkService.criteria().getResultList());
		}
		return talks;
	}

	public void clearTalks() {
		talks = null;
	}

}
