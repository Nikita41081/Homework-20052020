package in.reqres.userinfo;

/*
Created 
By Nikita
*/

import in.reqres.model.UserPojo;
import in.reqres.testbase.TestBase;
import io.restassured.http.ContentType;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.Test;

import static in.reqres.model.UserPojo.getRandomString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

public class UserCRUDTest extends TestBase {
    String name = "Nikita" + getRandomString(2);
    String job = "Tester"+getRandomString(2);
    String id;



    @Title("This test will create a new user record and verify its generated")
    @Test
    public void test001(){

        UserPojo userPojo = new UserPojo();

        userPojo.setName(name);
        userPojo.setJob(job);


        SerenityRest.rest()
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .body(userPojo)
                .post("/users")
                .then()
                .statusCode(201).log().body()
                .body("name",equalTo(name));

    }
    @Title("This test will get user from the list")
    @Test

    public void test002(){
        UserPojo userPojo = new UserPojo();

        userPojo.setName(name= "Michael");

        SerenityRest.rest()
                .given()
                .when()
                .get("/users?page=2")
                .then().statusCode(200)
                .log().body()
                .body("data.first_name",hasItems(name));


    }


    @Title("This test will update single user and verify its updated")
    @Test
    public void test003() {

        UserPojo userPojo = new UserPojo();
        userPojo.setName(name = name+"_new");
        userPojo.setJob(job= job+"_Modified");

        SerenityRest.rest().given()
                .header("Content-Type","application/json")
                .when()
                .body(userPojo)
                .put("/users/5")
                .then().statusCode(200)
                .log().body()
                .body("name",equalTo(name))
                .body("job",equalTo(job));


    }
    @Title("This test will delete single user ")
    @Test
    public void test004(){
        SerenityRest.rest().given()
                .when()
                .delete("/users/4")
                .then()
                .statusCode(204)
                .log().ifValidationFails();

    }

}
