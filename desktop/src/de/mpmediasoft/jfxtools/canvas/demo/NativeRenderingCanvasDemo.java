package de.mpmediasoft.jfxtools.canvas.demo;

import com.badlogic.drop.Drop;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.mpmediasoft.jfxtools.canvas.NativeRenderingCanvas;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple demo to show how the NativeRenderingCanvas class is supposed to be used.
 *
 * @author Michael Paus
 */
public class NativeRenderingCanvasDemo extends Application {

    private NativeRenderingCanvas canvas;

    @Override
    public void init() {
        System.out.println("java.runtime.version: " + System.getProperty("java.runtime.version", "(undefined)"));
        System.out.println("javafx.runtime.version: " + System.getProperty("javafx.runtime.version", "(undefined)"));
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        var root = new StackPane();

        canvas = new NativeRenderingCanvas();

        Label label = new Label("This is JavaFX");
        label.setMouseTransparent(true);
        label.setStyle("-fx-font-size: 64pt; -fx-font-family: Arial; -fx-font-weight: bold; -fx-text-fill: white; -fx-opacity: 0.8;");

        root.getChildren().addAll(canvas.getRoot(), label);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        canvas.dispose();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

class NativeRenderingCanvasDemoLauncher {
    public static boolean restartJVM() {

        String osName = System.getProperty("os.name");

        // if not a mac return false
        if (!osName.startsWith("Mac") && !osName.startsWith("Darwin")) {
            return false;
        }

        // get current jvm process pid
        String pid = ManagementFactory.getRuntimeMXBean().getName().split("@")[0];
        // get environment variable on whether XstartOnFirstThread is enabled
        String env = System.getenv("JAVA_STARTED_ON_FIRST_THREAD_" + pid);

        // if environment variable is "1" then XstartOnFirstThread is enabled
        if (env != null && env.equals("1")) {
            return false;
        }

        // restart jvm with -XstartOnFirstThread
        String separator = System.getProperty("file.separator");
        String classpath = System.getProperty("java.class.path");
        String mainClass = System.getenv("JAVA_MAIN_CLASS_" + pid);
        String jvmPath = System.getProperty("java.home") + separator + "bin" + separator + "java";

        List<String> inputArguments = ManagementFactory.getRuntimeMXBean().getInputArguments();

        ArrayList<String> jvmArgs = new ArrayList<String>();

        jvmArgs.add(jvmPath);
        jvmArgs.add("-XstartOnFirstThread");
        jvmArgs.addAll(inputArguments);
        jvmArgs.add("-cp");
        jvmArgs.add(classpath);
        jvmArgs.add(mainClass);

        // if you don't need console output, just enable these two lines
        // and delete bits after it. This JVM will then terminate.
        //ProcessBuilder processBuilder = new ProcessBuilder(jvmArgs);
        //processBuilder.start();

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(jvmArgs);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;

            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public static void main(String[] args) {
        NativeRenderingCanvasDemo.main(args);
    }
}