package api.codeparts;

import java.util.ArrayList;

// Represents a loop component
public class LoopComponent implements ContentComponent {

    private ArrayList<ArrayList<ContentComponent>> content = new ArrayList<>();
    private VariableComponent paramter;

    public ArrayList<ArrayList<ContentComponent>> getContent() {
        return content;
    }

    public VariableComponent getParamter() {
        return paramter;
    }

    public void setParamter(VariableComponent paramter) {
        this.paramter = paramter;
    }
}
