package java.format;

import api.codeparts.ClassComponent;
import api.format.AbstractCategoryDefinition;
import api.format.AbstractFormatDefinition;

public class JavaRawFormat extends AbstractFormatDefinition<ClassComponent> {

    public JavaRawFormat(){
        main = new AbstractCategoryDefinition<>() {
            @Override
            public void setContent(String content) {

            }

            @Override
            public String contentToString() {
                return content.toString();
            }
        };
    }
}
