package RestAssuredTest;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import static io.restassured.RestAssured.given;

public class FirstRestAssuredTest {
    private String path = System.getProperty("user.dir") + "/src/test/resources/TestData.xlsx";
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private FileInputStream fis;
    private DataFormatter formatter = new DataFormatter();

    
    public void setupExcel() throws IOException {
        if (workbook == null) {
            fis = new FileInputStream(path);
            workbook = new XSSFWorkbook(fis);
            sheet = workbook.getSheetAt(0);
        }
    }

    //Fetches data from a specific cell.
    public String getCellData(int rowNum, int colNum) throws IOException {
        setupExcel(); 
        XSSFRow row = sheet.getRow(rowNum);
        if (row == null) return "";
        return formatter.formatCellValue(row.getCell(colNum));
    }

    /**
     * Writes API response ID and Status back to the Excel file.
     */
    public void writeSpecificRow(int rowNum, String id, int status) throws IOException {
        setupExcel();
        XSSFRow row = sheet.getRow(rowNum);
        if (row == null) row = sheet.createRow(rowNum);

        // Column index 2 is 'C', 3 is 'D'
        row.createCell(2).setCellValue(id != null ? id : "N/A"); 
        row.createCell(3).setCellValue(String.valueOf(status)); 
        
        // Use FileOutputStream in a try-with-resources to ensure it closes
        try (FileOutputStream fos = new FileOutputStream(path)) {
            workbook.write(fos);
        }
    }

    /**
     * Helper for Data-Driven testing if using TestNG @DataProvider.
     */
    public Object[][] getPostTestData() throws IOException {
        setupExcel();
        int totalRows = sheet.getLastRowNum();
        Object[][] data = new Object[totalRows][3];

        for (int i = 1; i <= totalRows; i++) {
            data[i - 1][0] = getCellData(i, 0); // FirstName
            data[i - 1][1] = getCellData(i, 1); // LastName
            data[i - 1][2] = i;                 // Row Index
        }
        return data;
    }

    /**
     * Cleanup method to release the file lock.
     * Call this in your Cucumber @After hook.
     */
    public void closeResources() throws IOException {
        if (workbook != null) {
            workbook.close();
        }
        if (fis != null) {
            fis.close();
        }
    }
}
