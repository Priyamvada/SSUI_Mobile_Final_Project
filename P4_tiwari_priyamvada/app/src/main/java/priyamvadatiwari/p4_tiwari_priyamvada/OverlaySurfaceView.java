package priyamvadatiwari.p4_tiwari_priyamvada;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * Created by priyamvadatiwari on 12/8/15.
 */
public class OverlaySurfaceView extends GLSurfaceView {

    OverlayRenderer mRenderer;

    public OverlaySurfaceView (Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mRenderer = new OverlayRenderer(0, 0, this.getWidth(), this.getHeight());
        setRenderer(mRenderer);
    }

    public OverlaySurfaceView (Context context, AttributeSet attrs, double rotationX, double rotationY)
    {
        super(context, attrs);
        this.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        this.getHolder().setFormat(PixelFormat.TRANSLUCENT);
        mRenderer = new OverlayRenderer(rotationX, rotationY);
        setRenderer(mRenderer);
    }

    public void setRendererAngles(double rotationX, double rotationY)    {
        this.mRenderer.setThetaX((float)rotationX);
        this.mRenderer.setThetaY((float) rotationY);
    }

    public void setAligned(boolean aligned) {
        if(this.mRenderer.getAligned() != aligned) {
            this.mRenderer.setAligned(aligned);
            this.postInvalidate();
        }
    }

}
