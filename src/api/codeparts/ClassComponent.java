package api.codeparts;

import java.util.ArrayList;

public class ClassComponent implements ContentComponent {

    private ArrayList<FunctionComponent> functions = new ArrayList<>();
    private ArrayList<VariableComponent> variables = new ArrayList<>();

    public ArrayList<FunctionComponent> getFunctions() {
        return functions;
    }

    public ArrayList<VariableComponent> getVariables() {
        return variables;
    }
}
