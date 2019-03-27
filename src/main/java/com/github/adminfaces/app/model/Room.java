package com.github.adminfaces.app.model;

import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.HashSet;
import com.github.adminfaces.app.model.Talk;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import com.github.adminfaces.persistence.model.PersistenceEntity;

@Entity
public class Room implements Serializable, PersistenceEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	private static final long serialVersionUID = 1L;
	@Version
	@Column(name = "version")
	private int version;

	@Column(length = 20)
	@NotNull
	private String name;

	@Column
	@NotNull
	private Short capacity;

	@Column
	private Boolean hasWifi;

	@OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
	private Set<Talk> talks = new HashSet<Talk>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Room)) {
			return false;
		}
		Room other = (Room) obj;
		if (id != null) {
			if (!id.equals(other.id)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Short getCapacity() {
		return capacity;
	}

	public void setCapacity(Short capacity) {
		this.capacity = capacity;
	}

	public Boolean getHasWifi() {
		return hasWifi;
	}

	public void setHasWifi(Boolean hasWifi) {
		this.hasWifi = hasWifi;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (name != null && !name.trim().isEmpty())
			result += "name: " + name;
		if (capacity != null)
			result += ", capacity: " + capacity;
		if (hasWifi != null)
			result += ", hasWifi: " + hasWifi;
		return result;
	}

	public Set<Talk> getTalks() {
		return this.talks;
	}

	public void setTalks(final Set<Talk> talks) {
		this.talks = talks;
	}
}