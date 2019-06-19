package api.format;

import com.sun.javaws.exceptions.InvalidArgumentException;

public abstract class AbstractCategoryDefinition<T> {

    protected T content;

    public T getContent(){
        return content;
    }

    public abstract void setContent(String content) throws IllegalArgumentException;

    public abstract String contentToString();
}
