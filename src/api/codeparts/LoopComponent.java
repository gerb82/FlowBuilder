package api.codeparts;

import java.util.ArrayList;

// Represents a loop component
public class LoopComponent implements Scannable{

    private String[] tempContent;
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

    @Override
    public void setTempContent(String[] tempContent) {
        this.tempContent = tempContent;
    }

    @Override
    public String[] getTempContent() {
        return tempContent;
    }
}
