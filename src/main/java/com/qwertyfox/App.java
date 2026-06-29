package com.qwertyfox;

import com.qwertyfox.print.FinalizeDoc;
import com.qwertyfox.print.WriteToDoc;
import com.qwertyfox.processing.ReadSheet;
import com.qwertyfox.processing.ReadSheet2;

public class App
{
    public static void main( String[] args ) {

        System.out.println( "Hello World!" );

        ReadSheet2 readSheet = new ReadSheet2();
        readSheet.readSheet("C:/Users/Raptor/Desktop/Item-Sales-Report-20260626135230 Report.xls");
        WriteToDoc.getInstance().createTable();

        FinalizeDoc finalizeDoc = new FinalizeDoc();
        finalizeDoc.finalizeDoc();

    }
}
