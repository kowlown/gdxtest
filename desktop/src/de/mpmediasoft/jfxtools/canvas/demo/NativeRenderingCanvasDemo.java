package de.mpmediasoft.jfxtools.canvas.demo;

import com.badlogic.drop.Drop;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.mpmediasoft.jfxtools.canvas.NativeRenderingCanvas;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;
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
        var panel = new JPanel();
        var dummyCanvas = new Canvas();
        panel.add(dummyCanvas);
        final SwingNode swingNode = new SwingNode();
        //SwingUtilities.invokeAndWait(() -> {
            swingNode.setContent(panel);
        //});

        canvas = new NativeRenderingCanvas(dummyCanvas);

        Label label = new Label("This is JavaFX");
        label.setMouseTransparent(true);
        label.setStyle("-fx-font-size: 64pt; -fx-font-family: Arial; -fx-font-weight: bold; -fx-text-fill: white; -fx-opacity: 0.8;");

        root.getChildren().addAll(swingNode, canvas.getRoot(), label);

        Scene scene = new Scene(root, 800, 600);
        scene.setOnKeyPressed((e)->{
            canvas.keyPressed(e);
        });
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

    public static void main(String[] args)  {
         NativeRenderingCanvasDemo.main(args);
    }
}