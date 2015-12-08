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
        mRenderer = new OverlayRenderer();
        setRenderer(mRenderer);

    }

}
