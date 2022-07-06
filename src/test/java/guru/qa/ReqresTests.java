package guru.qa;

import io.restassured.RestAssured;
import io.restassured.matcher.ResponseAwareMatcher;
import io.restassured.response.Response;
import org.hamcrest.Matcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ReqresTests {

@BeforeEach
void openPage(){
RestAssured.baseURI = "https://reqres.in";
}

    @Test
    @DisplayName("Получение отдельного пользователя")
    void getSingleUser (){
    String firstname = "Janet",
            lastname = "Weaver",
            email = "janet.weaver@reqres.in";
    int id = 2;

    given()
            .get(baseURI + "/api/users/2")
            .then()
                .log().body()
                .log().status()
                .statusCode(200)
            .body("data.id", equalTo(id))
            .body("data.email", is(email))
            .body("data.first_name", is(firstname))
            .body("data.last_name", is(lastname));

    }

    @Test
    @DisplayName("Проверка успешного логина")
    void userLoginSuccess(){
        String body = "{ \"email\": \"eve.holt@reqres.in\", " +
                "\"password\": \"cityslicka\" }";

        given()
                .log().uri()
                .log().body()
                .body(body)
                .contentType(JSON)
                .when()
                .post(baseURI + "/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    @DisplayName("Проверка неуспешного логина")
    void userLoginFailed(){
     String email = "{ \"email\": \"sydney@fife\" }",
             error = "Missing password";

        given()
                .log().uri()
                .log().body()
                .body(email)
                .contentType(JSON)
                .when()
                .post(baseURI + "/api/login")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is(error));
    }

    @Test
    @DisplayName("Проверка удаления пользователя")
    void userDelete(){
    when()
            .delete(baseURI + "/api/users/2")
            .then()
            .log().status()
            .statusCode(204);


    }

    @Test
    @DisplayName("Проверка удаленной страницы")
    void pageNotFound(){
    when()
        .get(baseURI + "/api/unknown/23")
            .then()
            .statusCode(404);
    }






}
