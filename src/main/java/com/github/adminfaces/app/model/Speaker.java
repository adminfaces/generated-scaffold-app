package com.github.adminfaces.app.model;

import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;
import javax.persistence.Version;
import com.github.adminfaces.app.model.Talk;
import java.util.Set;
import java.util.HashSet;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import com.github.adminfaces.app.model.Address;
import javax.persistence.Embedded;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.github.adminfaces.persistence.model.PersistenceEntity;

@Entity
public class Speaker implements Serializable, PersistenceEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;
	private static final long serialVersionUID = 1L;
	@Version
	@Column(name = "version")
	private int version;

	@Column
	@NotNull
	private String firstname;

	@Column
	@NotNull
	private String surname;

	@Column(length = 2000)
	@Size(max = 2000)
	private String bio;

	@Column
	private String twitter;

	@OneToMany(mappedBy = "speaker", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Talk> talks = new HashSet<Talk>();

	@Embedded
	private Address address;

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
		if (!(obj instanceof Speaker)) {
			return false;
		}
		Speaker other = (Speaker) obj;
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

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public Set<Talk> getTalks() {
		return this.talks;
	}

	public void setTalks(final Set<Talk> talks) {
		this.talks = talks;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName() + " ";
		if (firstname != null && !firstname.trim().isEmpty())
			result += "firstname: " + firstname;
		if (surname != null && !surname.trim().isEmpty())
			result += ", surname: " + surname;
		if (bio != null && !bio.trim().isEmpty())
			result += ", bio: " + bio;
		if (twitter != null && !twitter.trim().isEmpty())
			result += ", twitter: " + twitter;
		return result;
	}
}