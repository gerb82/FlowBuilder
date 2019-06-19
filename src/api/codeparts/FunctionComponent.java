package api.codeparts;

import java.util.ArrayList;
import java.util.LinkedList;

// Represents a function component
public class FunctionComponent {

    private LinkedList<ContentComponent> content = new LinkedList<>();
    private ArrayList<VariableComponent> parameters = new ArrayList<>();

    public LinkedList<ContentComponent> getContent() {
        return content;
    }

    public ArrayList<VariableComponent> getParameters() {
        return parameters;
    }
}
