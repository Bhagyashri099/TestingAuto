package POM_Pages;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map; // ADD THIS IMPORT

import org.apache.poi.xssf.usermodel.*;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;

public class PostRequest {
    private String path = System.getProperty("user.dir") + "/src/test/resources/TestData.xlsx";
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private FileInputStream fis;

     public Map<String, String> getDataByRow(int rowNum) throws IOException {
        if (workbook == null) {
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheetAt(0);
        }
        
        Map<String, String> data = new HashMap<>();
        XSSFRow row = sheet.getRow(rowNum);
        
        if (row != null) {
            // Col 0 = name, Col 1 = lastname 
            data.put("name", row.getCell(0).getStringCellValue());
            data.put("lastname", row.getCell(1).getStringCellValue());
            
        }
        return data;
    }

    @DataProvider(name = "apiData")
    public Object[][] getTestData() throws IOException {
        if (workbook == null) {
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheetAt(0);
        }
        int totalRows = sheet.getLastRowNum();
        Object[][] data = new Object[totalRows][3];
        for (int i = 1; i <= totalRows; i++) {
            data[i-1][0] = sheet.getRow(i).getCell(0).getStringCellValue();
            data[i-1][1] = sheet.getRow(i).getCell(1).getStringCellValue();
           
            data[i-1][2] = i; 
            

        }
        return data;
    }
    public void writeSpecificRow(int rowNum, String id, int status) throws IOException {
        String finalId = (id == null) ? "N/A" : id;
        

        XSSFRow row = sheet.getRow(rowNum);
        if (row == null) {
            row = sheet.createRow(rowNum);
        }
        
       
        XSSFCell cell2 = row.getCell(2);
        if (cell2 == null) cell2 = row.createCell(2);
        cell2.setCellValue(finalId);
        
        XSSFCell cell3 = row.getCell(3);
        if (cell3 == null) cell3 = row.createCell(3);
        cell3.setCellValue(status);

        // 2. Write and CLOSE both the stream and the workbook
        try (FileOutputStream fos = new FileOutputStream(path)) {
            workbook.write(fos);
            // Closing the workbook here is critical if this is the end of your operation
            workbook.close(); 
        }
    }

    
    
}
