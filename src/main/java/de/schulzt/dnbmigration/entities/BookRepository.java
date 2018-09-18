package de.schulzt.dnbmigration.entities;

import java.util.UUID;

import org.springframework.data.domain.*;
import org.springframework.data.repository.*;

public interface BookRepository extends CrudRepository<Book, UUID>{
	

	Page<Book> findAll(Pageable pageable);

	Book findByTitleIgnoringCase(String title);	

}
