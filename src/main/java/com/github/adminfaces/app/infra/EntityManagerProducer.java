package com.github.adminfaces.app.infra;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class EntityManagerProducer {

	@PersistenceContext
	EntityManager em;

	@Produces
	public EntityManager produce() {
		return em;
	}
}
