package tests;

import models.*;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;
import static specs.TestSpecs.statusCodeResponseSpec;
import static specs.TestSpecs.requestSpecification;

public class ReqresTests extends TestBase {

    @DisplayName("Создание нового пользователя")
    @Test
    void createUserTest() {
        CreateUserRequestModel createUserRequestModel = new CreateUserRequestModel();
        createUserRequestModel.setName("morpheus");
        createUserRequestModel.setJob("leader");

        CreateUserResponseModel createUserResponseModel = step("Делаем запрос", () ->
                given(requestSpecification)
                        .body(createUserRequestModel)

                        .when()
                        .post("users")

                        .then()
                        .spec(statusCodeResponseSpec(201))
                        .extract().as(CreateUserResponseModel.class)
        );

        step("Проверяем ответ", () -> {
            assertEquals("morpheus", createUserResponseModel.getName());
            assertEquals("leader", createUserResponseModel.getJob());
            assertNotNull(createUserResponseModel.getId());
            assertNotNull(createUserResponseModel.getCreatedAt());
        });
    }

    @DisplayName("Получение одного пользователя")
    @Test
    void getSingleUserTest() {
        UserResponseModel userResponseModel = step("Делаем запрос", () ->
                given(requestSpecification)
                        .when()
                        .get("users/{id}", 2)

                        .then()
                        .spec(statusCodeResponseSpec(200))
                        .extract().as(UserResponseModel.class)
        );

        step("Проверяем ответ", () -> {
            assertEquals(2, userResponseModel.getUserModel().getId());
            assertEquals("janet.weaver@reqres.in", userResponseModel.getUserModel().getEmail());
            assertEquals("Weaver", userResponseModel.getUserModel().getLastName());
            assertEquals("https://reqres.in/img/faces/2-image.jpg", userResponseModel.getUserModel().getAvatar());
            assertEquals("https://reqres.in/#support-heading", userResponseModel.getSupportInformationModel().getUrl());
            assertEquals("To keep ReqRes free, contributions towards server costs are appreciated!", userResponseModel.getSupportInformationModel().getText());
        });
    }

    @DisplayName("Пользователь не существует")
    @Test
    void singleUserNotFoundTest() {
        UserResponseModel userResponseModel = step("Делаем запрос, получаем ответ 404", () ->
                given(requestSpecification)
                        .when()
                        .get("users/{id}", 23)

                        .then()
                        .spec(statusCodeResponseSpec(404))
                        .extract().as(UserResponseModel.class)
        );

        step("Проверяем ответ", () -> {
            assertNull(userResponseModel.getUserModel());
            assertNull(userResponseModel.getSupportInformationModel());
        });
    }

    @DisplayName("Изменение пользователя")
    @Test
    void updateUserTest() {
        UpdateUserRequestModel updateUserRequestModel = new UpdateUserRequestModel();
        updateUserRequestModel.setName("morpheus");
        updateUserRequestModel.setJob("zion resident");

        UpdateUsersResponseModel updateUsersResponseModel = step("Делаем запрос", () ->
                given(requestSpecification)
                        .body(updateUserRequestModel)

                        .when()
                        .put("users/{id}", "2")

                        .then()
                        .spec(statusCodeResponseSpec(200))
                        .extract().as(UpdateUsersResponseModel.class)
        );

        step("Проверяем ответ", () -> {
            assertEquals("morpheus", updateUsersResponseModel.getName());
            assertEquals("zion resident", updateUsersResponseModel.getJob());
            assertNotNull(updateUsersResponseModel.getUpdatedAt());
        });
    }

    @DisplayName("Удаление пользователя")
    @Test
    void deleteUserTest() {
        String response = step("Делаем запрос", () ->
                given(requestSpecification)
                        .when()
                        .delete("users/{id}", 5)

                        .then()
                        .spec(statusCodeResponseSpec(204))
                        .extract().asString()
        );

        step("Проверяем ответ", () ->
                assertEquals("", response)
        );
    }
}
