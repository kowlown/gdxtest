package de.mpmediasoft.jfxtools.canvas;

import com.badlogic.drop.Drop;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import java.awt.*;
import java.nio.ByteBuffer;

/**
 * The JNI interface to the native renderer.
 *
 * @author Michael Paus
 */
public class NativeRenderer {
    private byte[] gdxBuffer;
    private ByteBuffer buffer;
    private int bufferCount;
    private int width;
    private int height;
    private int colorModel;
    private int singleBufferSize;


    private int currentBufferIndex;
    private Drop drop;
    private Canvas canvas;
    private LwjglApplication app;

    public NativeRenderer(Canvas dummyCanvas) {
        canvas = dummyCanvas;
    }

    // Initialization and disposal:

    public void setGdxBuffer(byte[] buffer) {
        gdxBuffer = buffer;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void init() {
        canvas.setVisible(false);
        canvas.setSize(800, 600);
        var config = new LwjglApplicationConfiguration();
        config.title = "Drop";
        config.height = 600;
        config.width = 800;
        config.allowSoftwareMode = true;

       /* config.setTitle("Drop");
        config.setWindowedMode(800,600);
        config.setInitialVisible(false);
*/
        drop = new Drop();
        drop.setJfxRenderer(this);
        app = new LwjglApplication(drop, config, canvas);
    }

    public void dispose() {
        Gdx.app.exit();
    }

    // Canvas creation and rendering:

    public ByteBuffer createCanvas(int width, int height, int numBuffers, int nativeColorModel) {
        this.width = width;
        this.height = height;
        bufferCount = numBuffers;
        colorModel = nativeColorModel;
        currentBufferIndex = 0;

        singleBufferSize = width*height* 4;

        buffer = ByteBuffer.allocate(singleBufferSize * numBuffers);

        return buffer;
    }

    public int render() {
        currentBufferIndex += 1;
        if(currentBufferIndex >= bufferCount) currentBufferIndex = 0;

        var currentGdxBuffer = gdxBuffer;

        if(currentGdxBuffer != null) {

            buffer.position(currentBufferIndex*singleBufferSize);
            buffer.put(currentGdxBuffer);
            buffer.rewind();
        }

        return currentBufferIndex;
    }

    public void mouseMove(int x, int y) {
        drop.setBucket(x, y);
    }

    public void moveTo(int x, int y) {

    }

    // TODO: zoom, rotate, ...

}