package lib;

import io.restassured.response.Response;
import org.hamcrest.Matchers;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestAssertions {
    public static void assertResponseEquals(Response response, int expectedStatusCode)
    {
        assertEquals(
                expectedStatusCode,
                response.statusCode(),
                "Response code is not as expected"
        );

    }

    public static void assertJsonByName(Response response, String name, String expectedValue){

        String value = response.jsonPath().getString(name);
        assertEquals(expectedValue, value, "JSON value is not equals to expected value");

    }


}
