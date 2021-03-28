package de.mpmediasoft.jfxtools.canvas;

import com.badlogic.drop.Drop;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import java.nio.ByteBuffer;

/**
 * The JNI interface to the native renderer.
 *
 * @author Michael Paus
 */
public class NativeRenderer {
    byte[] gdxBuffer;
    private ByteBuffer buffer;
    int bufferCount;
    int width;
    int height;
    int colorModel;
    int singleBufferSize;

    int currentBufferIndex;
    Drop drop;
    Thread gdxThread;

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
        var config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Drop");
        config.setWindowedMode(800,600);
        config.setInitialVisible(false);

        drop = new Drop();
        drop.setJfxRenderer(this);
        gdxThread = new Thread(()-> new Lwjgl3Application(drop, config));
        gdxThread.start();

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

 /*       var intBuffer = buffer.asIntBuffer();
        for (var i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i, 0xFF0000FF);
        }
*/
        return buffer;
    }

    public int render() {
        currentBufferIndex += 1;
        if(currentBufferIndex >= bufferCount) currentBufferIndex = 0;

       /* Gdx.app.postRunnable(() -> {
            var pixmap = Pixmap.createFromFrameBuffer(0,0,width, height);
            gdxBuffer = pixmap.getPixels();
        });*/
        var currentGdxBuffer = gdxBuffer;

        if(currentGdxBuffer != null) {
            //fix colors RGBA -> BGRA
            for(int i = 0; i< gdxBuffer.length; i += 4) {
                var red = currentGdxBuffer[i];
                currentGdxBuffer[i] = currentGdxBuffer[i + 2];
                currentGdxBuffer[i + 2] = red;
            }

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