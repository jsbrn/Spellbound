package main;

import java.util.Arrays;

public class ConsoleLauncher {

    public static void main(String[] args) {
        JarClassLoader jcl = new JarClassLoader();
        String argsCombined = Arrays.stream(args).reduce("", (total, next) -> total + next);
        try {
            if (argsCombined.contains("--headless"))
                jcl.invokeMain("main.HeadlessLauncher", args);
            else
                jcl.invokeMain("main.GameManager",
                        new String[]{argsCombined.contains("--dev") ? "false": "true"});
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
