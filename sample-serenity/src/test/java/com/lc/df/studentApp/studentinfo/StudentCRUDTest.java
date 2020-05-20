package com.lc.df.studentApp.studentinfo;
/*
Created 
By Nikita
*/

import com.lc.df.studentApp.model.StudentPojo;
import com.lc.df.studentApp.testbase.TestBase;
import com.lc.df.studentApp.utils.TestUtils;
import io.restassured.http.ContentType;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.yecht.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.hasValue;
import static org.junit.Assert.assertThat;

@RunWith(SerenityRunner.class)
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StudentCRUDTest extends TestBase {

    static String firstName = "PRIMEUSER" + TestUtils.getRandomValue();
    static String lastName = "PRIMEUSER" + TestUtils.getRandomValue();
    static String programme = "Automation Testing";
    static String email = TestUtils.getRandomValue() + "xyz@yahoo.com";
    static int studentID;

    @Title("This will create new student")
    @Test
    public void test001() {
        List<String>courses  = new ArrayList<>();
        courses.add("ABC JAVA Tutorials");
        courses.add("DevOPPS");

        StudentPojo studentPojo=new StudentPojo();
        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        studentPojo.setCourses(courses);
    //Using SerenityRest Class
        SerenityRest.rest().given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .body(studentPojo)
                .post()
                .then().log().all().statusCode(201);
     }

    @Title("Verify if the student was added to the Application")
    @Test
    public void test002() {
        String p1 = "findAll{it.firstName=='";

        String p2 = "'}.get(0)";

        HashMap<String, Object> value = SerenityRest.rest().given()
                .when()
                .get("/list")
                .then()
                .statusCode(200)
                .extract()
                .path(p1 + firstName + p2);
        assertThat(value, hasValue(firstName));
        studentID = (int) value.get("id");
    }
    @Title("Update the user information and verify the updated information")
    @Test
    public void test03() {

        String p1 = "findAll{it.firstName=='";
        String p2 = "'}.get(0)";

        firstName = firstName+"_Updated";

        List<String> courses = new ArrayList<>();
        courses.add("JAVA");
        courses.add("API");

        StudentPojo studentPojo = new StudentPojo();
        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        studentPojo.setCourses(courses);
        //Using SerenityRest class
        SerenityRest.rest().given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .body(studentPojo)
                .put("/"+ studentID)
                .then().log().all().statusCode(200);

        HashMap<String, Object> value = SerenityRest.rest().given()
                .when()
                .get("/list")
                .then()
                .statusCode(200)
                .extract()
                .path(p1 + firstName + p2);
        System.out.println(value);
        assertThat(value, hasValue(firstName));

    }

    @Title("Delete the student and verify if the student is deleted!")
    @Test
    public void test04() {
        SerenityRest.rest()
                .given()
                .when()
                .delete("/"+studentID)
                .then()
                .statusCode(204);

        SerenityRest.rest()
                .given()
                .when()
                .get("/"+studentID)
                .then().statusCode(404);
    }
}
