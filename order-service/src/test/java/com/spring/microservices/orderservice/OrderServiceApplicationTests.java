package com.spring.microservices.orderservice;

import com.spring.microservices.orderservice.stub.InventoryStubs;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.testcontainers.containers.MySQLContainer;

import static org.hamcrest.MatcherAssert.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class OrderServiceApplicationTests {

	@ServiceConnection
	static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.3.0");
	@LocalServerPort
	private Integer port;

	@BeforeEach
	void setUp(){
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}

	static {
		mySQLContainer.start();
	}

	@Test
	void shouldSubmitOrder() {
		String orderRequest = """
				{
				    "skuCode":"iphone_15",
				    "price":1000,
				    "quantity":1
				}
				""";

		// Wiremock test
		InventoryStubs.stubInventoryCall("iphone_15",1);

		var responseString = RestAssured.given()
				.contentType(ContentType.JSON)
				.body(orderRequest)
				.when()
				.post("/api/order")
				.then()
				.log().all()
				.statusCode(HttpStatus.SC_CREATED)
				.extract()
				.body()
				.asString();

		assertThat(responseString, Matchers.is("Order placed successfully."));
	}

}
