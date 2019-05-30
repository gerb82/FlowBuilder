package api.format;

import java.io.IOException;
import java.util.ArrayList;

public abstract class AbstractFormatDefinition<T> {

    protected ArrayList<AbstractCategoryDefinition<?>> categories;
    protected AbstractCategoryDefinition<T> main;

    public String getMainContent(){
        return main.contentToString();
    }

    public String getCategory(int id){
        return categories.get(id).contentToString();
    }

    public AbstractCategoryDefinition<?> getCategoryDefinition(int id){
        return categories.get(id);
    }

    public void setMainContent(String mainContent) throws IOException{
        main.setContent(mainContent);
    }

    public void setCategory(int id, String content) throws IOException{
        categories.get(id).setContent(content);
    }
}
