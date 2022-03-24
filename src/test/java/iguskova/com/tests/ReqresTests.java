package iguskova.com.tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;

public class ReqresTests {

    @BeforeAll
    static void setup(){
        RestAssured.baseURI = "https://reqres.in";
    }

    @Test
    @DisplayName("Создать пользователя")
    void createUser(){
        String data = "{ \"name\": \"morpheus\", " +
                "\"job\": \"leader\"}";


        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/users")
                .then()
                .statusCode(201)
                .body("name", is("morpheus"), "job", is("leader"));
    }

    @Test
    @DisplayName("Найти пользователя")
    void findUser(){

        given()
                .when()
                .get("/api/users/2")
                .then()
                .statusCode(200)
                .body("data.id", is(2),
                        "data.email", is("janet.weaver@reqres.in"));
    }

    @Test
    @DisplayName("Пользователь не найден")
    void userNotFound(){

        given()
                .when()
                .get("/api/users/23")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Успешная регистрация")
    void registerSuccessful(){
        String data= "{ \"email\": \"eve.holt@reqres.in\", "+
                "\"password\": \"pistol\"}";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/register")
                .then()
                .statusCode(200)
                .body("id",is(4),"token",is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    @DisplayName("Неуспешная регистрация")
    void registerUnsuccessful(){

        String data = "{ \"email\": \"sydney@fife\"}";

        given()
                .contentType(JSON)
                .body(data)
                .when()
                .post("/api/register")
                .then()
                .statusCode(400)
                .body("error",is("Missing password"));
    }
}