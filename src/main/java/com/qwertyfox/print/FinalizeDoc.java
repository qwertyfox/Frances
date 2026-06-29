package com.qwertyfox.print;

import com.qwertyfox.move.MoveDoc;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinalizeDoc {

    private Map<String, List<String>> docsToCombineList = new HashMap<>();

    public Map<String, List<String>> getDocsToCombineList () {
        return docsToCombineList;
    }

    public void finalizeDoc() {
        readConfig();
        try {
            combineDocs();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void readConfig () {
        java.nio.file.Path dirLoc = Paths.get("config/print config.txt");

        try(BufferedReader br = Files.newBufferedReader(dirLoc)) {
            String line;
            while((line = br.readLine()) != null) {
                if(line.contains("#") || line.isEmpty()) {
                    continue;
                }

                String split[] = line.split(":");
                String docName = split [0];

                String namesBlob[] = split[1].split(",");

                List<String> docsList = new ArrayList<>();
                for(int i = 0; i < namesBlob.length; i ++) {
                    docsList.add(namesBlob[i]);
                }

                docsToCombineList.put(docName, docsList);
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void combineDocs() throws Exception {
        for(String masterDoc : docsToCombineList.keySet()) {
            FileOutputStream out = new FileOutputStream("Dir/"+masterDoc + ".docx");
            WordMerge wordMerge = new WordMerge(out);
            MoveDoc moveDoc = new MoveDoc();

            List<String> fileNames = docsToCombineList.get(masterDoc);

            for(int i = 0; i < fileNames.size(); i++) {
                wordMerge.add(Files.newInputStream(Paths.get("Dir/" + fileNames.get(i)+".docx")));
                moveDoc.moveDoc(fileNames.get(i)+".docx");
            }

            wordMerge.doMerge();
            wordMerge.close();
        }
    }


}
