package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;
import static io.restassured.http.ContentType.JSON;

public class TestSpecs {

    public static RequestSpecification requestSpecification = with()
            .filter(withCustomTemplates())
            .log().uri()
            .log().body()
            .log().headers()
            .contentType(JSON);

    public static ResponseSpecification statusCodeResponseSpec(int statusCode) {

        ResponseSpecification responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .log(STATUS)
                .log(BODY)
                .build();

        return responseSpecification;
    }
}
