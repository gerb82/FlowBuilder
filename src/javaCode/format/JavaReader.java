package javaCode.format;

import api.codeparts.PackageComponent;
import api.format.AbstractFormatDefinition;
import api.format.Reader;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Node;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class JavaReader extends Reader {

    @Override
    protected AbstractFormatDefinition formatSetup(File file) throws IOException {
        if (file.getName().endsWith(".java")) {
            super.raw = true;
            return new JavaRawFormat(file);
        } else if (file.getName().endsWith(".jfbf")) {
            super.raw = false;
            return new JavaFlowBuilderFormat(file);
        } else {
            throw new IOException("Invalid file type!");
        }
    }

    @Override
    protected void addToRoot(File file) {
        if(JavaRawFormat.src.getName() == null){
            JavaRawFormat.src.setName("src");
        }
        explorer(file);
    }

    protected static PackageComponent explorer(File file) {
        if (JavaRawFormat.src.getName().endsWith(file.getParentFile().getName())) {
            FilteredList<Node> pick = JavaRawFormat.src.getPackages().filtered(node -> ((PackageComponent) node).getName().endsWith(file.getName()));
            if (!pick.isEmpty())
                return (PackageComponent) pick.get(0);
            else {
                PackageComponent newPick = new PackageComponent(false);
                newPick.setName(file.getName());
                JavaRawFormat.src.getPackages().add(newPick);
                return newPick;
            }
        } else {
            PackageComponent result = explorer(file.getParentFile());
            FilteredList<Node> pick = result.getPackages().filtered(node -> ((PackageComponent) node).getName().equals(file.getName()));
            if (!pick.isEmpty())
                return (PackageComponent) pick.get(0);
            else {
                PackageComponent newPick = new PackageComponent(false);
                newPick.setName(file.getName());
                result.getPackages().add(newPick);
                return newPick;
            }
        }
    }
}
