package org.hibernate.bugs;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}

	// Entities are auto-discovered, so just add them anywhere on class-path
	// Add your tests, using standard JUnit.
	@Test
	public void bytecodeEnhancementTest() throws Exception {

		// Two entities, both alike in dignity...
		// one using field style, one using method style
		// the field one doesn't dirty-check properly under bytecode-enhancement
		// edit pom.xml to comment/uncomment BE plugin - test passes without it.

		EntityManager entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		MyFieldEntity myFieldEntity = new MyFieldEntity();
		myFieldEntity.id = "myid";
		entityManager.persist(myFieldEntity);

		MyMethodEntity myMethodEntity = new MyMethodEntity();
		myMethodEntity.setId("myid");
		entityManager.persist(myMethodEntity);

		entityManager.getTransaction().commit();

		entityManager.getTransaction().begin();

		myFieldEntity.myfield = "myfieldvalue";
		myMethodEntity.setMyfield("mymethodvalue");

		entityManager.getTransaction().commit(); // dirty-check here broken, doesn't flush myFieldEntity
		entityManager.close();

		entityManager = entityManagerFactory.createEntityManager();
		entityManager.getTransaction().begin();

		myMethodEntity = entityManager.find(MyMethodEntity.class, "myid");
		assertEquals("mymethodvalue", myMethodEntity.getMyfield());

		myFieldEntity = entityManager.find(MyFieldEntity.class, "myid");
		assertEquals("myfieldvalue", myFieldEntity.myfield);

		entityManager.getTransaction().commit();
		entityManager.close();
	}
}
