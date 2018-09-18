package de.schulzt.dnbmigration.entities;

import java.util.UUID;

import org.springframework.data.domain.*;
import org.springframework.data.repository.*;

public interface AuthorRepository extends CrudRepository<Author, UUID>{
	

	Page<Author> findAll(Pageable pageable);

	Author findByTitleIgnoringCase(String name);	

}