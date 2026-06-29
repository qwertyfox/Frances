package com.qwertyfox.print;

import com.qwertyfox.model.ProductDataStructure;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;

public class WriteToDoc {

    private static final WriteToDoc INSTANCE = new WriteToDoc();

    private static final String FONT_FAMILY = "Calibri";

    private static final int TABLE_WIDTH = 9000;
    private static final int PRODUCT_COLUMN_WIDTH = 7000;
    private static final int QTY_COLUMN_WIDTH = 1500;

    private static final int NORMAL_ROW_HEIGHT = 14 * 20; // 14 pt
    private static final int GAP_ROW_HEIGHT = 14 * 20;     // 10 pt

    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("HH:mm");

    private WriteToDoc() {
    }

    public static WriteToDoc getInstance() {
        return INSTANCE;
    }

    public void createTable() {

        Map<String, TreeMap<String, Integer>> finalMap =
                ProductDataStructure.getInstance().getFinalMapOfProducts();

        String analysedInfo = "Analysed on: "
                + LocalDate.now()
                + " at: "
                + LocalTime.now().format(TIME_FORMATTER);

        File outputDirectory = new File("Dir/");

        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        for (String breadType : finalMap.keySet()) {

            TreeMap<String, Integer> orderList = finalMap.get(breadType);

            if (orderList == null || orderList.isEmpty()) {
                continue;
            }

            createDocumentForBreadType(breadType, orderList, analysedInfo);
        }
    }

    private void createDocumentForBreadType(
            String breadType,
            TreeMap<String, Integer> orderList,
            String analysedInfo
    ) {

        try (XWPFDocument document = new XWPFDocument()) {

            addFooter(document, analysedInfo);
            addTitle(document, breadType);

            XWPFTable table = document.createTable();
            table.setWidth(TABLE_WIDTH);

            createHeaderRow(table);
            createGapRow(table);

            int breadTypeTotalOrders = 0;

            for (String productName : orderList.keySet()) {

                int orderNumber = orderList.get(productName);
                breadTypeTotalOrders += orderNumber;

                XWPFTableRow row = table.createRow();
                ensureTwoCells(row);

                setCellText(row.getCell(0), productName);
                setCellText(row.getCell(1), String.valueOf(orderNumber));

                setCellWidth(row.getCell(0), PRODUCT_COLUMN_WIDTH);
                setCellWidth(row.getCell(1), QTY_COLUMN_WIDTH);

                centerCellText(row.getCell(1));
                setRowHeight(row, NORMAL_ROW_HEIGHT);
            }

            createSeparatorRow(table);
            createTotalRow(table, breadTypeTotalOrders);
            addEmptyLineAfterTable(document);

            String fileName = sanitiseFileName(breadType) + ".docx";
            File outputFile = new File("Dir/" + fileName);

            try (FileOutputStream out = new FileOutputStream(outputFile)) {
                document.write(out);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addEmptyLineAfterTable(XWPFDocument document) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setSpacingBetween(1.0);
        paragraph.setSpacingBefore(0);
        paragraph.setSpacingAfter(0);

        XWPFRun run = createRunWithFont(paragraph);
        run.setText("");
    }

    private void addTitle(XWPFDocument document, String breadType) {

        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);

        paragraph.setSpacingBefore(0);
        paragraph.setSpacingAfter(0);
        paragraph.setSpacingBetween(1.0);

        XWPFRun run = createRunWithFont(paragraph);
        run.setBold(true);
        run.setFontSize(16);
        run.setText(breadType);
    }

    private void addFooter(XWPFDocument document, String footerText) {

        XWPFHeaderFooterPolicy footerPolicy = document.createHeaderFooterPolicy();

        XWPFFooter footer =
                footerPolicy.createFooter(XWPFHeaderFooterPolicy.DEFAULT);

        XWPFParagraph footerParagraph = footer.createParagraph();
        footerParagraph.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun footerRun = createRunWithFont(footerParagraph);
        footerRun.setFontSize(9);
        footerRun.setText(footerText);
    }

    private void createHeaderRow(XWPFTable table) {

        XWPFTableRow headerRow = table.getRow(0);
        ensureTwoCells(headerRow);

        setBoldCellText(headerRow.getCell(0), "Product");
        setBoldCellText(headerRow.getCell(1), "Qty");

        setCellWidth(headerRow.getCell(0), PRODUCT_COLUMN_WIDTH);
        setCellWidth(headerRow.getCell(1), QTY_COLUMN_WIDTH);

        centerCellText(headerRow.getCell(0));
        centerCellText(headerRow.getCell(1));

        setRowHeight(headerRow, NORMAL_ROW_HEIGHT);
    }

    private void createGapRow(XWPFTable table) {

        XWPFTableRow row = table.createRow();
        ensureTwoCells(row);

        setCellText(row.getCell(0), " ");
        setCellText(row.getCell(1), " ");

        setCellWidth(row.getCell(0), PRODUCT_COLUMN_WIDTH);
        setCellWidth(row.getCell(1), QTY_COLUMN_WIDTH);

        setRowHeight(row, GAP_ROW_HEIGHT);
    }

    private void createSeparatorRow(XWPFTable table) {

        XWPFTableRow row = table.createRow();
        ensureTwoCells(row);

        setCellText(row.getCell(0), " ");
        setCellText(row.getCell(1), " ");

        setCellWidth(row.getCell(0), PRODUCT_COLUMN_WIDTH);
        setCellWidth(row.getCell(1), QTY_COLUMN_WIDTH);

        setRowHeight(row, GAP_ROW_HEIGHT);
    }

    private void createTotalRow(XWPFTable table, int total) {

        XWPFTableRow totalRow = table.createRow();
        ensureTwoCells(totalRow);

        setBoldCellText(totalRow.getCell(0), "Total");
        setBoldCellText(totalRow.getCell(1), String.valueOf(total));

        setCellWidth(totalRow.getCell(0), PRODUCT_COLUMN_WIDTH);
        setCellWidth(totalRow.getCell(1), QTY_COLUMN_WIDTH);

        centerCellText(totalRow.getCell(1));
        setRowHeight(totalRow, NORMAL_ROW_HEIGHT);
    }

    private void ensureTwoCells(XWPFTableRow row) {

        while (row.getTableCells().size() < 2) {
            row.createCell();
        }
    }

    private void setCellText(XWPFTableCell cell, String text) {

        removeExistingParagraphs(cell);

        XWPFParagraph paragraph = cell.addParagraph();
        cleanParagraphSpacing(paragraph);

        XWPFRun run = createRunWithFont(paragraph);
        run.setText(text);

        verticallyCenterCell(cell);
    }

    private void setBoldCellText(XWPFTableCell cell, String text) {

        removeExistingParagraphs(cell);

        XWPFParagraph paragraph = cell.addParagraph();
        cleanParagraphSpacing(paragraph);

        XWPFRun run = createRunWithFont(paragraph);
        run.setBold(true);
        run.setText(text);

        verticallyCenterCell(cell);
    }

    private void removeExistingParagraphs(XWPFTableCell cell) {

        int paragraphCount = cell.getParagraphs().size();

        for (int i = paragraphCount - 1; i >= 0; i--) {
            cell.removeParagraph(i);
        }
    }

    private void cleanParagraphSpacing(XWPFParagraph paragraph) {

        paragraph.setSpacingBefore(0);
        paragraph.setSpacingAfter(0);
        paragraph.setSpacingBetween(1.0);
    }

    private void verticallyCenterCell(XWPFTableCell cell) {

        cell.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.CENTER);
    }

    private XWPFRun createRunWithFont(XWPFParagraph paragraph) {

        XWPFRun run = paragraph.createRun();
        run.setFontFamily(FONT_FAMILY);
        run.setFontSize(12);

        return run;
    }

    private void centerCellText(XWPFTableCell cell) {

        for (XWPFParagraph paragraph : cell.getParagraphs()) {
            paragraph.setAlignment(ParagraphAlignment.CENTER);
        }
    }

    private void setCellWidth(XWPFTableCell cell, int width) {

        CTTcPr tcPr = cell.getCTTc().isSetTcPr()
                ? cell.getCTTc().getTcPr()
                : cell.getCTTc().addNewTcPr();

        CTTblWidth tcWidth = tcPr.isSetTcW()
                ? tcPr.getTcW()
                : tcPr.addNewTcW();

        tcWidth.setW(BigInteger.valueOf(width));
        tcWidth.setType(STTblWidth.DXA);
    }

    private void setRowHeight(XWPFTableRow row, int rowHeight) {

        row.setHeight(rowHeight);

        CTTrPr trPr = row.getCtRow().isSetTrPr()
                ? row.getCtRow().getTrPr()
                : row.getCtRow().addNewTrPr();

        CTHeight height;

        if (trPr.sizeOfTrHeightArray() == 0) {
            height = trPr.addNewTrHeight();
        } else {
            height = trPr.getTrHeightArray(0);
        }

        height.setVal(BigInteger.valueOf(rowHeight));
        height.setHRule(STHeightRule.EXACT);
    }

    private String sanitiseFileName(String fileName) {

        return fileName
                .replaceAll("[\\\\/:*?\"<>|]", "_")
                .trim();
    }
}