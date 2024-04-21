package com.spring.microservices.productservice;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;

// This is for integration test, as our server by default running in port 8080,
// so we choose a random port other than 8080 to run the integration test as
// while running integration tests it boot up the server
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

	// set up before function call

	// get the mongo db image running on docker, the service connection annotation
	// automatically configures the spring boot uri for mongodb while we have done
	// manually on application properties file

	@ServiceConnection
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

	// get the local random port
	@LocalServerPort
	private Integer port;

	// before running each test configure the host and port
	@BeforeEach
	void setUp(){
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	// start the mongodb container
	static {
		mongoDBContainer.start();
	}

	@Test
	void shouldCreateProduct() {
		// To create product we need request body
		// Multiline string (Java-14)
		String requestBody = """
				{
				    "name": "IPhone 15",
				    "description": "Smartphone",
				    "price": 1100
				}
				""";

		RestAssured.given()
				.contentType("application/json")
				.body(requestBody)
				.when()
				.post("/api/product")
				.then()
				.log().all()
				.statusCode(201)
				.body("id", Matchers.notNullValue())
				.body("name", Matchers.equalTo("IPhone 15"))
				.body("description", Matchers.equalTo("Smartphone"))
				.body("price", Matchers.equalTo(1100));
	}

	@Test
	void shouldGetProduct(){
		RestAssured.given()
				.when()
				.get("/api/product")
				.then()
				.assertThat()
				.contentType(ContentType.JSON)
				.statusCode(200)
				.and()
				.body("name", Matchers.equalTo("IPhone 15"))
				.body("description", Matchers.equalTo("Smartphone"))
				.body("price", Matchers.equalTo(1100));
	}

}
