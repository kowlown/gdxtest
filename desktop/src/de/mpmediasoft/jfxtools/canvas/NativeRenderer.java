package de.mpmediasoft.jfxtools.canvas;

import com.badlogic.drop.Drop;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.ScreenUtils;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * The JNI interface to the native renderer.
 *
 * @author Michael Paus
 */
public class NativeRenderer {
    private ByteBuffer gdxBuffer;
    private ByteBuffer buffer;
    int bufferCount;
    int width;
    int height;
    int colorModel;
    int singleBufferSize;

    int currentBufferIndex;
    LwjglApplication app;
    Drop drop;

    // Initialization and disposal:

    public void setGdxBuffer(ByteBuffer buffer) {
        gdxBuffer = buffer;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void init() {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Drop";
        config.width = 800;
        config.height = 600;
        drop = new Drop();
        app = new LwjglApplication(drop, config);
    }

    public void dispose() {
        app.stop();
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

        Gdx.app.postRunnable(() -> {
            var pixmap = Pixmap.createFromFrameBuffer(0,0,width, height);
            gdxBuffer = pixmap.getPixels();
        });
        var currentGdxBuffer = gdxBuffer;

        if(currentGdxBuffer != null) {
            byte[] lines = new byte[singleBufferSize];
            int numBytesPerLine = width * 4;
            for (int i = 0; i < height; i++) {
                currentGdxBuffer.position((height - i - 1) * numBytesPerLine);
                currentGdxBuffer.get(lines, i * numBytesPerLine, numBytesPerLine);
                for(int j = 0; j< numBytesPerLine; j+= 4) {
                    var offset = i * numBytesPerLine + j;
                    //fix colors RGBA -> BGRA
                    var red = lines[offset];
                    lines[offset] = lines[offset + 2];
                    lines[offset + 2] = red;
                }

            }

            buffer.position(currentBufferIndex*singleBufferSize);
            buffer.put(lines);
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