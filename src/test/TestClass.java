package test;

import java.io.Serializable;

public class TestClass implements Serializable{

    private static String test = "a test indeed";
    private int magicNum = 4;
    public boolean tired = true;

    public void helloWorld(){
        System.out.println("hello world");
    }

    public static void testMethod(){
        System.out.println("that's one method of doing things");
    }
}
