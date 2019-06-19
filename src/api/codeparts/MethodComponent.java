package api.codeparts;

// Represents a method component (System.out.println(text)) for example
public class MethodComponent implements ContentComponent {

    private String method;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
