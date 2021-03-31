package de.mpmediasoft.jfxtools.canvas;

import com.badlogic.drop.Box2dLightTest;
import com.badlogic.drop.Drop;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import javafx.application.Platform;

import java.awt.*;
import java.nio.ByteBuffer;

/**
 * The JNI interface to the native renderer.
 *
 * @author Michael Paus
 */
public class NativeRenderer implements InputProcessor {
    private byte[] gdxBuffer;
    private ByteBuffer buffer;
    private int bufferCount;
    private int width;
    private int height;
    private int singleBufferSize;


    private int currentBufferIndex;
    private InputProcessor gdxInput;
    private Canvas canvas;
    private LwjglApplication app;
    private NativeRenderingCanvas nativeCanvas;

    public NativeRenderer(Canvas dummyCanvas, NativeRenderingCanvas nativeRenderingCanvas) {
        canvas = dummyCanvas;
        nativeCanvas = nativeRenderingCanvas;
    }

    // Initialization and disposal:

    public void setGdxBuffer(byte[] buffer) {
        gdxBuffer = buffer;
        var bufferIndex = render();
        Platform.runLater(()-> nativeCanvas.renderUpdate(bufferIndex, width, height));
    }

    // on mac this must be called when the canvas is isDisplayable
    public void init() {
        if(app != null)
            return;

        canvas.setVisible(false);
        canvas.setSize(800, 600);
        var config = new LwjglApplicationConfiguration();
        config.title = "Drop";
        config.height = 600;
        config.width = 800;
        config.allowSoftwareMode = true;

        //drop = new Drop();
        //drop.setJfxRenderer(this);
        var box = new Box2dLightTest();
        box.setJfxRenderer(this);
        gdxInput = box;

        app = new LwjglApplication(box, config, canvas);
    }

    public void dispose() {
        app.stop();
    }

    // Canvas creation and rendering:

    public ByteBuffer createCanvas(int width, int height, int numBuffers) {
        this.width = width;
        this.height = height;
        bufferCount = numBuffers;
        currentBufferIndex = 0;

        singleBufferSize = width*height* 4;

        buffer = ByteBuffer.allocate(singleBufferSize * numBuffers);
        canvas.setSize(width, height);

        init();
        return buffer;
    }

    public int render() {
        currentBufferIndex += 1;
        if(currentBufferIndex >= bufferCount) currentBufferIndex = 0;

        var currentGdxBuffer = gdxBuffer;
        var currentRenderBuffer = buffer;
        var currentSingleBufferSize = singleBufferSize;

        if(currentGdxBuffer != null && currentRenderBuffer.capacity() == 2*gdxBuffer.length && gdxBuffer.length == currentSingleBufferSize) {

            currentRenderBuffer.position(currentBufferIndex*currentSingleBufferSize);
            currentRenderBuffer.put(currentGdxBuffer);
            currentRenderBuffer.rewind();
        }

        return currentBufferIndex;
    }
/*
    public void mouseMove(int x, int y) {
        //drop.setBucket(x, y);
    }

    public void moveTo(int x, int y) {

    }
*/
    @Override
    public boolean keyDown(int keycode) {
        if(gdxInput == null)
            return false;
        return gdxInput.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if(gdxInput == null)
            return false;
        return gdxInput.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        if(gdxInput == null)
            return false;
        return gdxInput.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(gdxInput == null)
            return false;
        return gdxInput.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(gdxInput == null)
            return false;
        return gdxInput.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(gdxInput == null)
            return false;
        return gdxInput.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if(gdxInput == null)
            return false;
        return gdxInput.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        if(gdxInput == null)
            return false;
        return gdxInput.scrolled(amountX, amountY);
    }
}