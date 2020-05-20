package in.reqres.testbase;

import io.restassured.RestAssured;
import org.junit.BeforeClass;

/**
 * Created By Nikita
 */
public class TestBase {
    @BeforeClass
    public static void inIt() {
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
    }

}
