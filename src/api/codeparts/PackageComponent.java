package api.codeparts;

import java.util.ArrayList;

public class PackageComponent {

    private ArrayList<FileComponent> files = new ArrayList<>();
    private ArrayList<PackageComponent> packages = new ArrayList<>();
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<FileComponent> getFiles() {
        return files;
    }

    public ArrayList<PackageComponent> getPackages() {
        return packages;
    }
}
