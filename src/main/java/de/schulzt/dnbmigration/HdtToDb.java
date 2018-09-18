package de.schulzt.dnbmigration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.UUID;

import javax.annotation.Resource;

import org.rdfhdt.hdt.exceptions.NotFoundException;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdt.triples.IteratorTripleString;
import org.rdfhdt.hdt.triples.TripleString;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class HdtToDb implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(HdtToDb.class, args);
	}
	
	@Resource
	private MigrationService migrationService;

	@Bean
	public MigrationService migrationService(){
		return  new MigrationService();
	}

	@Override
	public void run(String... args) throws Exception {
		migrationService().migrate();
	}	
	
	private static final String createBookTable = "\n" +
 			"DROP TABLE IF EXISTS public.\"Title\";\n" + 
			"\n" + 
			"CREATE TABLE public.\"Title\"\n" + 
			"(\n" + 
			"  id uuid PRIMARY KEY,\n" + 
			"  name text\n" + 
			");\n" +
 			"DROP TABLE IF EXISTS public.\"Keyword\";\n" + 
			"\n" + 
			"CREATE TABLE public.\"Keyword\"\n" + 
			"(\n" + 
			"  id uuid PRIMARY KEY,\n" + 
			"  name text\n" + 
			");\n" +
			"DROP TABLE IF EXISTS public.\"Keyword_Title\";\n" + 
			"\n" + 
			"CREATE TABLE public.\"Keyword_Title\"\n" + 
			"(\n" + 
			"  id uuid PRIMARY KEY,\n" + 
			"  keywordId uuid,\n" +
			"  titleId uuid\n" +
			");";


	
	public static void main_(String[] args) throws IOException, NotFoundException, SQLException, ClassNotFoundException {
		Class.forName("org.postgresql.Driver");
		Connection dbcon = DriverManager.getConnection("jdbc:postgresql://localhost:5432/dnbtitle",
				"postgres", "test");
		
		dbcon.createStatement().execute(createBookTable);
		HDT hdt = HDTManager.loadHDT("/home/tobias/Downloads/DNBTitel.hdt", null);
		IteratorTripleString it = hdt.search("", "http://purl.org/dc/elements/1.1/title", "");
		long count = it.estimatedNumResults();
		double i = 1;
		while(it.hasNext()) {
			System.out.println(i++ / count);
			TripleString title = it.next();
			
			IteratorTripleString itKeyword = hdt.search(title.getSubject().toString(), "http://purl.org/dc/terms/subject", "");	
			if(itKeyword.estimatedNumResults() > 0) {
				UUID titleUUID = UUID.randomUUID();
				String insertStr ="INSERT INTO public.\"Title\"(id, name) VALUES('" + titleUUID.toString() + "','" + title.getObject().toString().replace("'", "''") + "')";
				dbcon.createStatement().execute(insertStr);
				while(itKeyword.hasNext()) {
					UUID keywordUUID = UUID.randomUUID();
					TripleString keyword = itKeyword.next();
					String str ="INSERT INTO public.\"Keyword\"(id, name) VALUES('" + keywordUUID.toString() + "','" + keyword.getObject().toString().replace("'", "''") + "')";
					dbcon.createStatement().execute(str);
					String str2 ="INSERT INTO public.\"Keyword_Title\"(id, keywordId, titleId) VALUES(gen_random_uuid(),'" + keywordUUID.toString() + "','" + titleUUID.toString() + "')";
					dbcon.createStatement().execute(str2);
					
				}			
			}

		}
	}

}
