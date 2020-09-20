package main;

public class ConsoleLauncher {

    public static void main(String[] args) {
        JarClassLoader jcl = new JarClassLoader();
        try {
            if (args.length >= 1 && args[0].equals("hl"))
                jcl.invokeMain("main.HeadlessLauncher", args);
            else
                jcl.invokeMain("main.GameManager", args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    } // main()

} // class MyAppLauncher
