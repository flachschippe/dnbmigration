package de.schulzt.dnbmigration.entities;

import java.util.UUID;

import org.springframework.data.domain.*;
import org.springframework.data.repository.*;

public interface KeywordRepository extends CrudRepository<Keyword, UUID>{
	

	Page<Book> findAll(Pageable pageable);

	Keyword findByTitleIgnoringCase(String name);	

}