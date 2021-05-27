package com.pollfishqa.quality;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PollfishQARestTest  {

    String surveyName = "Survey"+getRandomId();

    public String getRandomId(){
        return UUID.randomUUID().toString().substring(0,5);
    }

    /**
     * Register new researcher
     */
    @Test
    public void test1() {
        given().contentType(ContentType.JSON)
                .body(getRegistrationBody().toString())
                .when()
                .post("https://www.pollfish.com/api/v3/user/register")
                .then().statusCode(200);
    }

    /**
     * Create new survey
     */
    @Test
    public void test2() {
        given().contentType(ContentType.JSON)
                .body(getCreateSurveyBody()).log().all()
                .when()
                .put("https://www.pollfish.com/api/v1/survey?name="+surveyName+"&folderId="+getFolderId())
                .then().log().all()
                .statusCode(200);
    }

    /**
     * Log out
     */
    @Test
    public void test3(){
        given().when().get("https://www.pollfish.com/logout").then().statusCode(303);
    }

    public String getFolderId(){
        return given().contentType(ContentType.JSON)
                .when()
                .get("https://www.pollfish.com/api/v3/folders/treeMetadata?folderId=-1")
                .jsonPath().get("$.data.id");
    }

    public JSONObject getRegistrationBody(){
        return new JSONObject()
                .put("email", "test2@test" + getRandomId() + ".com")
                .put("password", getRandomId() + "pwd")
                .put("fullName", "QA" + getRandomId() + " TEST")
                .put("companyName", "Pollfish")
                .put("companyEmail", "")
                .put("qualification", "2")
                .put("referral", 3)
                .put("otherReferral", "")
                .put("legal", JSONObject.NULL)
                .put("newsletter", true)
                .put("consentTypes", new JSONArray().put("GDPR").put("Newsletter"))
                .put("isDeveloper", false);

    }

    public JSONObject getCreateSurveyBody(){
        return new JSONObject()
                .put("waitingForApproval", false)
                .put("name", surveyName)
                .put("numberOfCompletedSurveys", 600)
                .put("running", false)
                .put("isThirdParty", false)
                .put("locked", false)
                .put("version", 3)
                .put("id", getRandomId())
                .put("remaining", 0)
                .put("statusName", "draft");

    }
}
