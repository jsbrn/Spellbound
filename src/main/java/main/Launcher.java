package main;

public class Launcher {
    public static void main(String[] args) {
        JarClassLoader jcl = new JarClassLoader();
        try {
            jcl.invokeMain("main.GameManager", args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
