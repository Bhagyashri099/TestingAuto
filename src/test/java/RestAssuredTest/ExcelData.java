//package RestAssuredTest;
//
//import static io.restassured.RestAssured.given;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import org.apache.poi.ss.usermodel.DataFormatter;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.DataProvider;
//import org.testng.annotations.Test;
//import io.restassured.response.Response;
//
//public class ExcelData {
//    private String path = System.getProperty("user.dir") + "/src/test/resources/TestData.xlsx";
//    private XSSFWorkbook workbook;
//    private XSSFSheet sheet;
//
//    @Test(dataProvider = "apiData")
//    public void testPostAndSaveResponse(String name, String job, int rowNum) {
//        // 1. Prepare Request Body
//        Map<String, String> body = new HashMap<>();
//        body.put("name", name);
//        body.put("job", job);
//
//        // 2. Send POST request (FIX: using correct /api path)
//        Response response = given()
//            .baseUri("https://reqres.in")
//            .contentType("application/json")
//            .body(body)
//        .when()
//            .post("users");
//
//        // 3. Extract Response Data
//        String generatedId = response.jsonPath().getString("id");
//        int statusCode = response.getStatusCode();
//
//        // 4. Update the workbook in memory
//        synchronized(workbook) { 
//            XSSFRow row = sheet.getRow(rowNum);
//            if (row == null) row = sheet.createRow(rowNum);
//            
//            // Write ID to Col C (Index 2) and Status to Col D (Index 3)
//            row.createCell(2).setCellValue(generatedId);
//            row.createCell(3).setCellValue(String.valueOf(statusCode));
//        }
//    }
//
//    @DataProvider(name = "apiData")
//    public Object[][] getTestData() throws IOException {
//        // FIX: Use try-with-resources to close stream immediately and release file lock
//        try (FileInputStream fis = new FileInputStream(path)) {
//            workbook = new XSSFWorkbook(fis);
//        }
//        sheet = workbook.getSheetAt(0);
//        DataFormatter formatter = new DataFormatter();
//        
//        int totalRows = sheet.getLastRowNum(); 
//        Object[][] data = new Object[totalRows][3]; 
//        
//        for (int i = 1; i <= totalRows; i++) {
//            XSSFRow row = sheet.getRow(i);
//            if (row != null) {
//                // FIX: Use DataFormatter to handle numeric/string cells safely
//                data[i-1][0] = formatter.formatCellValue(row.getCell(0)); // Name
//                data[i-1][1] = formatter.formatCellValue(row.getCell(1)); // Job
//                data[i-1][2] = i; // Store Row Index for writing back
//            }
//        }
//        return data;
//    }
//
//    @AfterClass
//    public void tearDown() throws IOException {
//        // 5. Save all changes to the file ONCE at the end for better performance
//        if (workbook != null) {
//            try (FileOutputStream fos = new FileOutputStream(path)) {
//                workbook.write(fos);
//                System.out.println("Excel file updated and saved successfully.");
//            } finally {
//                workbook.close();
//            }
//        }
//    }
//}
