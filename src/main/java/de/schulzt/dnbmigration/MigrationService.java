package de.schulzt.dnbmigration;

import java.io.IOException;

import org.rdfhdt.hdt.exceptions.NotFoundException;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdt.triples.IteratorTripleString;
import org.rdfhdt.hdt.triples.TripleString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import de.schulzt.dnbmigration.entities.Author;
import de.schulzt.dnbmigration.entities.AuthorRepository;
import de.schulzt.dnbmigration.entities.Book;
import de.schulzt.dnbmigration.entities.BookRepository;
import de.schulzt.dnbmigration.entities.Keyword;
import de.schulzt.dnbmigration.entities.KeywordRepository;

public class MigrationService {
	@Autowired
	private BookRepository bookRepo;

	@Autowired
	private KeywordRepository keywordRepo;	
	
	@Autowired
	private AuthorRepository authorRepo;		
	
	@Value("${titleDatabasePath}")
	private String titleDatabasePath;
	
	@Value("${gndDatabasePath}")
	private String gndDatabasePath;	
	
	private String getStringFromObject(final TripleString ts) {
		return ts.getObject().toString().replace("^^<http://www.w3.org/2001/XMLSchema#string>", "");
	}
	
	public void migrate() throws IOException, NotFoundException {

		HDT titleHdt = HDTManager.mapHDT(titleDatabasePath, null);
		HDT gndHdt = HDTManager.mapHDT(gndDatabasePath, null);

		IteratorTripleString it = titleHdt.search("", "http://purl.org/dc/elements/1.1/title", "");
		long count = it.estimatedNumResults();
		double i = 1;
		while(it.hasNext()) {
			System.out.println(i++ / count);
			TripleString title = it.next();
			
			IteratorTripleString itKeyword;
			IteratorTripleString itAuthor;
			try {
				itKeyword = titleHdt.search(title.getSubject().toString(), "http://purl.org/dc/terms/subject", "");
				itAuthor = titleHdt.search(title.getSubject().toString(), "http://purl.org/dc/terms/creator", "");

				if(itKeyword.hasNext()) {
					Book book = new Book(getStringFromObject(title));
					

					while(itKeyword.hasNext()) {
						TripleString keyword = itKeyword.next();
						IteratorTripleString itKeywordText = gndHdt.search(keyword.getObject().toString(), "http://d-nb.info/standards/elementset/gnd#variantNameForThePlaceOrGeographicName", "");
						
						while(itKeywordText.hasNext()) {
							String keywordTitle = getStringFromObject(itKeywordText.next());
							Keyword foundKeyword = keywordRepo.findByTitleIgnoringCase(keywordTitle);
							if(foundKeyword == null) {
								foundKeyword = new Keyword(keywordTitle);
								keywordRepo.save(foundKeyword);
							}
							book.getKeywords().add(foundKeyword);
						}				
					}
					
					while(itAuthor.hasNext()) {
						TripleString author = itAuthor.next();
						IteratorTripleString itAuthorText = gndHdt.search(author.getObject().toString(), "http://d-nb.info/standards/elementset/gnd#preferredNameForThePerson", "");
						
						while(itAuthorText.hasNext()) {
							String authorTitle = getStringFromObject(itAuthorText.next());
							Author foundAuthor = authorRepo.findByTitleIgnoringCase(authorTitle);
							if(foundAuthor == null) {
								foundAuthor = new Author(authorTitle);
								authorRepo.save(foundAuthor);
							}
							book.getAuthors().add(foundAuthor);
						}				
					}					
					bookRepo.save(book);
				}	

			
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}	

		}		
		
	}
}