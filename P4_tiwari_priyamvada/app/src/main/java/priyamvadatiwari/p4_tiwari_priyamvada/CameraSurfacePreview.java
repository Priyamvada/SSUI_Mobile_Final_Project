package priyamvadatiwari.p4_tiwari_priyamvada;

import android.content.Context;
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

    Display display;
    private static int displayAngle;

    public CameraSurfacePreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        mContext = context;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public CameraSurfacePreview(Context context) {
        super(context);
        mContext = context;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
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



        /*int dRotation = display.getRotation();
        FacialProcessing.PREVIEW_ROTATION_ANGLE angleEnum = FacialProcessing.PREVIEW_ROTATION_ANGLE.ROT_0;

        switch (dRotation)  {
            case 0: // Device is not rotated
                displayAngle = 90;
                angleEnum = FacialProcessing.PREVIEW_ROTATION_ANGLE.ROT_90;
                break;
            case 1: // Landscape Left
                displayAngle = 0;
                angleEnum = FacialProcessing.PREVIEW_ROTATION_ANGLE.ROT_0;
                break;
            case 2: //Device Upside down
                displayAngle = 270;
                angleEnum = FacialProcessing.PREVIEW_ROTATION_ANGLE.ROT_270;
                break;
            case 3: //Landscape Right
                displayAngle = 180;
                angleEnum = FacialProcessing.PREVIEW_ROTATION_ANGLE.ROT_180;
                break;
            default:
                displayAngle = 90;
                angleEnum = FacialProcessing.PREVIEW_ROTATION_ANGLE.ROT_90;
                break;
        }

        mCamera.setDisplayOrientation(displayAngle);*/

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)    {

        try {
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
}
