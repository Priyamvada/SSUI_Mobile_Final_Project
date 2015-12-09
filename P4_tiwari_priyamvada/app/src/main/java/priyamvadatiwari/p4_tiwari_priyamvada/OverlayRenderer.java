package priyamvadatiwari.p4_tiwari_priyamvada;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by priyamvadatiwari on 12/8/15.
 */
public class OverlayRenderer implements GLSurfaceView.Renderer{

    private OverlayGuides mGuides;
    private float mThetaX, mThetaY;
    private float red = 0, green = 0.25f, blue = 0.5f, alphaVal = 0.01f;
    boolean mAligned = false;

    public OverlayRenderer(float xDeg, float yDeg, int w, int h)   {
        super();
        mThetaX = xDeg;
        mThetaY = yDeg;
    }

    public OverlayRenderer(double rotationX, double rotationY)   {
        super();
    }

    public float getThetaX() {
        return mThetaX;
    }

    public float getThetaY() {
        return mThetaY;
    }

    public void setThetaX(float xDeg) {
        mThetaX = xDeg;
    }

    public void setThetaY(float yDeg) {
        mThetaY = yDeg;
    }

    public void setAligned(boolean aligned) {
        if(mAligned != aligned) {
            mAligned = aligned;
        }
    }

    public boolean getAligned() {
        return mAligned;
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int w, int h)   {
        if(mAligned)    {
            alphaVal = 0.01f;
            green = 0.10f;
            blue = 0.2f;
        }   else {
            alphaVal = 0.01f;
        }
        mGuides = new OverlayGuides(0.004f, red, green, blue, alphaVal, w, h);
        gl10.glViewport((int) mThetaX, (int) mThetaY, w, h);
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
        gl10.glClearColor(0f, 0f, 0f, 0f);
        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl10.glMatrixMode(GL10.GL_MODELVIEW);
        gl10.glLoadIdentity();
        gl10.glTranslatef(0f, -0.3f, -5.0f);
        gl10.glRotatef(Math.min(mThetaX, mThetaY), 0, 0, 1);
        mGuides.draw(gl10);
    }
}
