package api.format;

import java.io.IOException;

public abstract class AbstractFormatDefinition {

    public abstract String getMainContent();

    public abstract String getCategory(int id);

    public abstract AbstractCategoryDefinition<?> getCategoryDefinition(int id);

    public abstract void setMainContent(String mainContent) throws IOException;

    public abstract void setCategory(int id, String content) throws IOException;

    public abstract AbstractFormatDefinition createFormat();
}
