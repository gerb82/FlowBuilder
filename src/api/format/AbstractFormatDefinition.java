package api.format;

public abstract class AbstractFormatDefinition {

    public abstract String getMainContent();

    public abstract String getCategory(int id);

    public abstract AbstractCategoryDefinition<?> getCategoryDefinition(int id);
}
