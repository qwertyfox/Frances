package com.qwertyfox.print;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to merge all the document into a single file.
 * The XWPFDocument is basically higher level representation of .docx file
 * The .docx file cannot be opened whole as once and Apache POI expects it to be in stream. Both while reading and writing
 * The final merged document is saved in memory first as there are XML elements that cannot be modified once the file is written
 * Once all the modification is done to the file, the in-memory document is then written to "Combined document.docx"
 *
 */

public class WordMerge {

    private final OutputStream outputStream;
    private final List<InputStream> inputStreamList;
    private XWPFDocument document;

    public WordMerge(OutputStream outputStream) {
        this.outputStream = outputStream;
        inputStreamList = new ArrayList<>();
    }


    public void add(InputStream stream) throws Exception {
        inputStreamList.add(stream);

        // Used to open a stream of Open Packaging Conventions Package which is container for office documents
        OPCPackage srcPackage = OPCPackage.open(stream);

        //The src1Document is the source document being opened which is being streamed to the OPCPackage. It is written in memory
        XWPFDocument src1Document = new XWPFDocument(srcPackage);
        if (inputStreamList.size() == 1) {
            document = src1Document;
        } else {
            CTBody srcBody = src1Document.getDocument().getBody();
            // the document is in-memory document that has the merger of all the document in the directory
            document.getDocument().addNewBody().set(srcBody);

        }
    }

    public void doMerge() throws Exception {
        document.write(outputStream);
    }

    public void close() throws Exception {
        outputStream.flush();
        outputStream.close();


        // the input steam is opened and unique to each files sent, thats why they are closed one by one
        for (InputStream input : inputStreamList) {
            input.close();
        }
    }

}