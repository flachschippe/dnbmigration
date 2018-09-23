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
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories("de.schulzt.dnbdb")
@EntityScan("de.schulzt.dnbdb")

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
	

}
