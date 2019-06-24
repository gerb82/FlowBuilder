package api.codeparts;

public interface Scannable extends ContentComponent {

    void setTempContent(String[] tempContent);
    String[] getTempContent();
}
