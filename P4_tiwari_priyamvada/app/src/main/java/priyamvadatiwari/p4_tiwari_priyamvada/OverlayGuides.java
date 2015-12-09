package priyamvadatiwari.p4_tiwari_priyamvada;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by priyamvadatiwari on 12/8/15.
 */
public class OverlayGuides {
    private float vertices[] = {
            -1.0f,  1.0f, 0.0f,  // 0, Top Left
            -1.0f, -1.0f, 0.0f,  // 1, Bottom Left
            1.0f, -1.0f, 0.0f,  // 2, Bottom Right
    };

    private float verticalVertices[] = {
            -1.0f,  1.0f, 0.0f,  // 0, Top Left
            -1.0f, -1.0f, 0.0f, // 1, Bottom left
    }, horizontalVertices[] = {
            -1.0f, -1.0f, 0.0f,  // 1, Bottom Left
            1.0f, -1.0f, 0.0f,  // 2, Bottom Right
    };


    // Our vertex buffer.
    private FloatBuffer verticalVtxBuffer, horizontalVtxBuffer;

    private float red, blue, green;

    public OverlayGuides(float scale, float red, float green, float blue, int w, int h) {
        vertices = new float[] {
                -w * scale, -h * scale, 0.0f, // V1 - first vertex
                w * scale, -h * scale, 0.0f, // V2 - second vertex
                -w * scale,  h * scale, 0.0f,
        };
        this.red = red;
        this.green = green;
        this.blue = blue;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(3 * 3 * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        verticalVtxBuffer = byteBuffer.asFloatBuffer();
        verticalVtxBuffer.put(vertices);
        verticalVtxBuffer.flip();
    }

    /**
     * This function draws our square on screen.
     * @param gl
     */
    public void draw(GL10 gl) {
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        // set the colour for the triangle
        gl.glColor4f(red, green, blue, 0.2f);
        // Point to our vertex buffer
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, verticalVtxBuffer);
        // Draw the vertices as triangle strip
        gl.glDrawArrays(GL10.GL_TRIANGLES, 0, vertices.length / 3);
        // Disable the client state before leaving
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
