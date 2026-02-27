//package TestSetup;
//
//import io.restassured.response.Response;
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.DataProvider;
//import org.testng.annotations.Test;
//
//import java.io.*;
//import java.util.*;
//
//import static io.restassured.RestAssured.given;
//
//public class ExcelReader {
//
//    private String path = System.getProperty("user.dir") + "/src/test/resources/TestData.xlsx";
//    private XSSFWorkbook workbook;
//    private XSSFSheet sheet;
//    private DataFormatter formatter = new DataFormatter();
//
//    // =========================================================================
//    // SECTION 1: TESTNG DATA DRIVEN METHODS (For PostRequest.java style)
//    // =========================================================================
//
//    @Test(dataProvider = "apiData")
//    public void testPostAndSaveResponse(String name, String job, int rowNum) throws IOException {
//        Map<String, String> body = new HashMap<>();
//        body.put("name", name);
//        body.put("job", job);
//
//        Response response = given()
//                .baseUri("https://reqres.in")
//                .contentType("application/json")
//                .body(body)
//                .when()
//                .post("/api/users"); // Fixed path to /api/users
//
//        String generatedId = response.jsonPath().getString("id");
//        int statusCode = response.getStatusCode();
//
//        // Write ID to Col C (Index 2) and Status to Col D (Index 3)
//        writeResultToMemory(rowNum, generatedId, statusCode);
//    }
//
//    @DataProvider(name = "apiData")
//    public Object[][] getTestData() throws IOException {
//        loadWorkbook();
//        sheet = workbook.getSheetAt(0);
//        int totalRows = sheet.getLastRowNum();
//        Object[][] data = new Object[totalRows][3];
//
//        for (int i = 1; i <= totalRows; i++) {
//            XSSFRow row = sheet.getRow(i);
//            if (row != null) {
//                data[i - 1][0] = formatter.formatCellValue(row.getCell(0)); // Name
//                data[i - 1][1] = formatter.formatCellValue(row.getCell(1)); // Job
//                data[i - 1][2] = i; // Row Index
//            }
//        }
//        return data;
//    }
//
//    // =========================================================================
//    // SECTION 2: FUNCTIONAL / CUCUMBER METHODS (For APISteps.java style)
//    // =========================================================================
//
//    /**
//     * Used by Cucumber Steps to get a specific row as a Map {Header -> Value}
//     */
//    public Map<String, String> getRowData(String sheetName, int rowNum) throws IOException {
//        loadWorkbook();
//        Sheet targetSheet = workbook.getSheet(sheetName);
//        Row headerRow = targetSheet.getRow(0);
//        Row dataRow = targetSheet.getRow(rowNum);
//
//        Map<String, String> map = new LinkedHashMap<>();
//        for (int c = 0; c < headerRow.getLastCellNum(); c++) {
//            String header = formatter.formatCellValue(headerRow.getCell(c));
//            String value = formatter.formatCellValue(dataRow.getCell(c));
//            map.put(header, value);
//        }
//        return map;
//    }
//
//    // =========================================================================
//    // SECTION 3: UTILITIES & LIFECYCLE
//    // =========================================================================
//
//    private void loadWorkbook() throws IOException {
//        if (workbook == null) {
//            try (FileInputStream fis = new FileInputStream(path)) {
//                workbook = new XSSFWorkbook(fis);
//            }
//        }
//    }
//
//    public synchronized void writeResultToMemory(int rowNum, String id, int status) throws IOException {
//        loadWorkbook();
//        sheet = workbook.getSheetAt(0);
//        XSSFRow row = sheet.getRow(rowNum);
//        if (row == null) row = sheet.createRow(rowNum);
//
//        row.createCell(2).setCellValue(id != null ? id : "N/A");
//        row.createCell(3).setCellValue(String.valueOf(status));
//    }
//
//    @AfterClass
//    public void tearDown() throws IOException {
//        if (workbook != null) {
//            try (FileOutputStream fos = new FileOutputStream(path)) {
//                workbook.write(fos);
//                System.out.println("Excel file updated and saved successfully.");
//            } finally {
//                workbook.close();
//                workbook = null; // Reset for next run
//            }
//        }
//    }
//}
