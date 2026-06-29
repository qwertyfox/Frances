package com.qwertyfox.move;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class MoveDoc {

    public void moveDoc(String fileName) {
        Path source = Path.of("Dir",fileName);
        Path destination = Path.of("Dir","Temp Docs");
        Path target = destination.resolve(fileName);

        try{
            Files.createDirectories(destination);

            Files.move(source,target, StandardCopyOption.REPLACE_EXISTING);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
