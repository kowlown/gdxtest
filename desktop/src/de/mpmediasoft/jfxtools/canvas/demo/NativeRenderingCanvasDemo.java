package de.mpmediasoft.jfxtools.canvas.demo;

import de.mpmediasoft.jfxtools.canvas.NativeRenderingCanvas;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

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
    public static void main(String[] args) {
        NativeRenderingCanvasDemo.main(args);
    }
}