package de.schulzt.dnbmigration.entities;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.*;


@Entity
public class Author implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private UUID id;

	public Author() {
		
	}
	
	public Author(String title) {
		super();
		this.title = title;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(nullable = false)
	private String title;
	
}
