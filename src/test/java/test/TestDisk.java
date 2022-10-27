package test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lib.TestAssertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TestDisk

{
    public static String token ="y0_AgAAAABllBKiAADLWwAAAADSQvnLTpUNCXOJSM2yAfYst7cdPlN99F8";

    public static RequestSpecification requestSpec(){

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept","application/json");
        headers.put("Authorization", "OAuth "+token);

        return given()
                .headers(headers);

    }




    @Test

    public void renameFile() {
        String foldername = "testapifolder";
        String copyfilename = "testfile.txt";
        String newCopufilename = "renamedtestfile.txt";

       //CREATE FOLDER
        Response response = given()
                .spec(requestSpec())
                .when()
                .put("https://cloud-api.yandex.net/v1/disk/resources?path="+foldername)
                .andReturn();

        TestAssertions.assertResponseEquals(response, 201);


        //COPY_FILE


        Map <String, String> copyParams =new HashMap<>();
        copyParams.put("from", "disk:/"+copyfilename);
        copyParams.put("path", "disk:/"+foldername+"/"+copyfilename);
        copyParams.put("overwrite", "false");

        Response responseFileCopy = given()
                .spec(requestSpec())
                .queryParams(copyParams)
                .log().all()
                .when()
                .post("https://cloud-api.yandex.net/v1/disk/resources/move")
                .andReturn();






      //RENAME FILE

        Map <String, String> renameParams =new HashMap<>();
        renameParams.put("from", "disk:/"+foldername+"/"+copyfilename);
        renameParams.put("path", "disk:/"+foldername+"/"+newCopufilename);
        renameParams.put("overwrite", "true");

        Response responseFileRename = given()
                .spec(requestSpec())
                .queryParams(renameParams)
                .log().all()
                .when()
                .post("https://cloud-api.yandex.net/v1/disk/resources/move")
                .andReturn();

        responseFileRename.prettyPrint();

        String link = "https://cloud-api.yandex.net/v1/disk/resources?path="+"disk%3A%2F"+foldername+"%2F"+newCopufilename;

        TestAssertions.assertResponseEquals(responseFileRename, 201);
        TestAssertions.assertJsonByName(responseFileRename,"href", link);
    }







}
