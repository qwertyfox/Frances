package com.qwertyfox.processing;

import com.qwertyfox.model.ProductDataStructure;
import org.apache.poi.ss.usermodel.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ReadSheet2 {

    private ProductDataStructure productDataStructure = ProductDataStructure.getInstance();

    private Map<String, String> breadNameMap = new HashMap<>();

    public void readSheet(String path) {

        // load config and names
        loadBreadNameMap();

        try (InputStream inputStream = Files.newInputStream(Paths.get(path))) {
            Workbook workbook = WorkbookFactory.create(inputStream);

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();

            String currentSubHeading = "";

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                String checkValue = row.getCell(0).getStringCellValue();
                if (!checkValue.contains(" - ")) {
                    currentSubHeading = checkValue;
                    continue;
                }

                String split[] = checkValue.split(" - ");
                int number = (int) row.getCell(1).getNumericCellValue();

                String breadName = "";
                String productName;

                if(number == 0) {
                    //Cases with - in subheading like Wrap Standard - open
                    String word = "";
                    if((word = breadNameMap.get(checkValue)) != null) {
                        currentSubHeading = word;
                        breadName = word;
                    }
                }else {

                    breadName = breadNameMap.get(currentSubHeading);
                }

                if(breadName == null) {
//                    continue;
                    breadName = currentSubHeading;
                }
                productName = breadName.concat(" - ").concat(split[split.length - 1]);


                if(number == 0 || breadName.isEmpty() || productName.isEmpty()) {
                    // cases where word Open has 0 number cause the subheading "Wrap Standard - open" is taken as product name "open" and number as 0
                    continue;
                }
                productDataStructure.addToFinalList(breadName, productName, number);

            }
            productDataStructure.printFinalList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBreadNameMap() {
        java.nio.file.Path dirLoc = Paths.get("config/bread name config.txt");

        try (BufferedReader br = Files.newBufferedReader(dirLoc)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("#") || line.isEmpty()) {
                    continue;
                }

                String split[] = line.split(":");
                String mapKey = split[0];
                String mapValue = split[1];

                breadNameMap.put(mapKey, mapValue);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
