package com.qwertyfox.processing;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;

public class ReadSheet {

    public void readSheet(String fileName) {

        try(InputStream inputStream = Files.newInputStream(Paths.get("here .xls"))){
            Workbook workbook = WorkbookFactory.create(inputStream);

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.rowIterator();
            while (rowIterator.hasNext()){
                Row row = rowIterator.next();

                Iterator<Cell> cellIterator = row.cellIterator();

                while(cellIterator.hasNext()){
                    Cell cell = cellIterator.next();

                    switch (cell.getCellType()){
                        case NUMERIC -> System.out.print(cell.getNumericCellValue() + "\t");
                        case STRING -> System.out.print(cell.getStringCellValue() + "\t");
                    }
                }
                System.out.println("");
            }

        }catch (IOException e){
            e.printStackTrace();
        }

    }

}
