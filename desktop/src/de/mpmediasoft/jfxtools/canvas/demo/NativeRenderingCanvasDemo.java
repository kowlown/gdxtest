package de.mpmediasoft.jfxtools.canvas.demo;

import de.mpmediasoft.jfxtools.canvas.NativeRenderingCanvas;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;


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
        var root = new BorderPane();
        var main = new StackPane();
        var panel = new JPanel();
        var dummyCanvas = new Canvas();
        panel.add(dummyCanvas);
        final SwingNode swingNode = new SwingNode();
        //SwingUtilities.invokeAndWait(() -> {
            swingNode.setContent(panel);
        //});

        MenuBar menuBar = new MenuBar();

        // --- Menu File
        Menu menuFile = new Menu("File");
        var item = new MenuItem("Test");
        menuFile.getItems().addAll(item);

        // --- Menu Edit
        Menu menuEdit = new Menu("Edit");

        // --- Menu View
        Menu menuView = new Menu("View");

        menuBar.getMenus().addAll(menuFile, menuEdit, menuView);




        canvas = new NativeRenderingCanvas(dummyCanvas);

        Label label = new Label("This is JavaFX");
        label.setMouseTransparent(true);
        label.setStyle("-fx-font-size: 64pt; -fx-font-family: Arial; -fx-font-weight: bold; -fx-text-fill: white; -fx-opacity: 0.8;");

        main.getChildren().addAll(swingNode, canvas.getRoot(), label);

        root.setTop(menuBar);
        root.setCenter(main);
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