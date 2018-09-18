package de.schulzt.dnbmigration.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;


@Entity
public class Book implements Serializable {

	public Book() {
		
	}
	
	public Book(String title) {
		super();
		this.title = title;
		this.keywords = new ArrayList<Keyword>();
		this.authors = new ArrayList<Author>();
	}

	public UUID getId() {
		return id;
	}

	public List<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
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

	public List<Keyword> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<Keyword> keywords) {
		this.keywords = keywords;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private UUID id;

	@Column(nullable = false, columnDefinition="text")
	private String title;

	@ManyToMany(cascade = {CascadeType.MERGE})
	private List<Keyword> keywords;
	
	@ManyToMany(cascade = {CascadeType.MERGE})
	private List<Author> authors;	
	
}
