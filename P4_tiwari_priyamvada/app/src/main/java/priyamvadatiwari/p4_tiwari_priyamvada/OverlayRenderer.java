package priyamvadatiwari.p4_tiwari_priyamvada;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by priyamvadatiwari on 12/8/15.
 */
public class OverlayRenderer implements GLSurfaceView.Renderer{

    private OverlayGuides mGuides;

    @Override
    public void onSurfaceChanged(GL10 gl10, int w, int h)   {
        mGuides = new OverlayGuides(0.5f, 1, 0, 0);
        gl10.glViewport(0, 0, w, h);
        // for a fixed camera, set the projection too
        float ratio = (float) w / h;
        gl10.glMatrixMode(GL10.GL_PROJECTION);
        gl10.glLoadIdentity();
        gl10.glFrustumf(-ratio, ratio, -1, 1, 1, 10);
        gl10.glMatrixMode(GL10.GL_MODELVIEW);
        gl10.glLoadIdentity();
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        gl10.glClearColor(0f, 0f, 0f, 0f );
        gl10.glClear( GL10.GL_COLOR_BUFFER_BIT );
        gl10.glLoadIdentity();
        gl10.glTranslatef(0.0f, 0.0f, -5.0f);
        mGuides.draw(gl10);
    }
}
