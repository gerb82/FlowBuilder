package api.format;

public abstract class AbstractCategoryDefinition<T> {

    protected T content;

    public T getContent(){
        return content;
    }

    public abstract void setContent(String content);

    public abstract String contentToString();
}
