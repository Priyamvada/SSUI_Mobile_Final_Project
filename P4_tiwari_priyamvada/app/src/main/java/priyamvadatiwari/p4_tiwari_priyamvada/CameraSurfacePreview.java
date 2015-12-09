package priyamvadatiwari.p4_tiwari_priyamvada;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.qualcomm.snapdragon.sdk.face.FacialProcessing;

import java.io.IOException;

/**
 * Created by priyamvadatiwari on 12/6/15.
 */
public class CameraSurfacePreview extends SurfaceView implements SurfaceHolder.Callback{

    private SurfaceHolder mHolder;
    private Camera mCamera;
    Context mContext;
    Paint mCanvasPaint, successPaint;
    boolean mAligned;

    Display display;
    private static int displayAngle;

    public CameraSurfacePreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mContext = context;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mAligned = true;

        mCanvasPaint = new Paint();
        mCanvasPaint.setColor(Color.LTGRAY);
        mCanvasPaint.setStrokeWidth(1);

        successPaint = new Paint();
        successPaint.setColor(Color.CYAN);
        successPaint.setStrokeWidth(4);

        getHolder().setType( SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS );
    }

    public CameraSurfacePreview(Context context) {
        super(context);
        mContext = context;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mCanvasPaint = new Paint();
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void setAligned(boolean aligned) {
        if(mAligned != aligned) {
            mAligned = aligned;
            this.postInvalidate();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)    {
    // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d("TAG", "Error starting camera preview: " + e.getMessage());
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)    {

        try {
            setWillNotDraw(false);
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        }
        catch (IOException e)   {
            Log.d("TAG", "Error setting camera preview: " + e.getMessage());
        }
        display = ((WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.stopPreview();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if(!mAligned)   {
            canvas.drawLine(0, 0, this.getWidth(), this.getHeight(), mCanvasPaint);
        }   else    {
            canvas.drawLine(this.getWidth() * 0.25f, this.getHeight() * 0.25f, this.getWidth() * 0.75f, this.getHeight() * 0.75f, successPaint);
            canvas.drawLine(this.getWidth() * 0.75f, this.getHeight() * 0.25f, this.getWidth() * 0.25f, this.getHeight() * 0.75f, successPaint);
        }
        canvas.save();
    }
}
