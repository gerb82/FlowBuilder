package java.format;

import api.format.AbstractFormatDefinition;
import api.format.Reader;

import java.io.File;
import java.io.IOException;

public class JavaReader extends Reader {

    @Override
    protected AbstractFormatDefinition formatSetup(File file) throws IOException {
        if(file.getName().endsWith(".java")){
            super.raw = true;
            return new JavaRawFormat();
        } else if(file.getName().endsWith(".jfbf")){
            super.raw = false;
            return new JavaFlowBuilderFormat();
        } else {
            throw new IOException("Invalid file type!");
        }
    }
}
