package iguskova.com.tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.is;

public class ReqresTests {

    @Test
    void createUser(){
        String data = "{ \"name\": \"morpheus\", " +
                "\"job\": \"leader\"}";


        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .statusCode(201)
                .body("name", is("morpheus"), "job", is("leader"));
    }

    @Test
    void findUser(){

        given()
                .when()
                .get("https://reqres.in/api/users/2")
                .then()
                .statusCode(200)
                .body("data.id", is(2),
                        "data.email", is("janet.weaver@reqres.in"));
    }

    @Test
    void userNotFound(){

        given()
                .when()
                .get("https://reqres.in/api/users/23")
                .then()
                .statusCode(404);
    }

    @Test
    void registerSuccessful(){
        String data= "{ \"email\": \"eve.holt@reqres.in\", "+
                "\"password\": \"pistol\"}";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(200)
                .body("id",is(4),"token",is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void registerUnsuccessful(){

        String data = "{ \"email\": \"sydney@fife\"}";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(400)
                .body("error",is("Missing password"));
    }
}

