package priyamvadatiwari.p4_tiwari_priyamvada;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaActionSound;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import priyamvadatiwari.p4_tiwari_priyamvada.utils.MediaFileHandlers;
import priyamvadatiwari.p4_tiwari_priyamvada.utils.Statistics;


public class NativeCameraActivity extends Activity implements Camera.PreviewCallback {

    Camera cameraObj;
    FrameLayout previewFrame;
    TextView captureText;
    CameraSurfacePreview mPreview;
    MediaActionSound actionSound;
    MediaPlayer mediaBuffer;
    boolean mAligned = false;

    SensorManager mSensorManager;
    Sensor mAccelerometer;
    OverlaySurfaceView mOverlaySurface;

    float[] mAccelleroOutput = null;
    float[] mMagnetoOutput = null;

    private static final int FRONT_CAMERA_INDEX = 1;
    private static final int BACK_CAMERA_INDEX = 0;
    private static final double g = 10;

    SensorEventListener mSensorListener = new SensorEventListener() {
        private int acceleroSensorCounter = 0;
        private int acceleroSensorPrecision = 100;
        private float acceleroALPHA = 0.2f;

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            Sensor sensor = event.sensor;

            if (sensor.getType() == Sensor.TYPE_ACCELEROMETER)    {
                acceleroSensorCounter++;
                mAccelleroOutput = Statistics.lowPass(event.values, mAccelleroOutput, acceleroALPHA);
                double xValue = Double.parseDouble(Float.toString(mAccelleroOutput[0])),
                        yValue = Double.parseDouble(Float.toString(mAccelleroOutput[1])),
                        xAngle, yAngle;
                xValue = (Math.abs(xValue) > g)? g: xValue;
                yValue = (Math.abs(yValue) > g)? g: yValue;
                xAngle = Math.toDegrees(Math.asin(xValue / g));
                yAngle = Math.toDegrees(Math.asin(yValue / g));

                if( this.angleInThreshold(xAngle, 1) || this.angleInThreshold(yAngle, 1))    {
                    mOverlaySurface.setRendererAngles(0, 0);
                }   else    {
                    mOverlaySurface.setRendererAngles(Math.abs(xAngle), Math.abs(yAngle));
                    Log.d("Rotation", ", thetaX: " + Double.toString(xAngle)
                            + ", thetaY: " + Double.toString(yAngle));
                }
                if( this.angleInThreshold(xAngle, 1) || this.angleInThreshold(yAngle, 1))    {
                    mPreview.setAligned(true);
                    mOverlaySurface.setAligned(true);
                    if(mAligned)  {
                        mAligned = false;
                    }
                }   else    {
                    mPreview.setAligned(false);
                    mOverlaySurface.setAligned(false);
                    if(! mAligned)  {
                        mAligned = true;

                        if(acceleroSensorCounter > acceleroSensorPrecision) {
                            mediaBuffer = MediaPlayer.create(NativeCameraActivity.this, R.raw.capture_command_perfect);
                            mediaBuffer.start();
                            acceleroSensorCounter = 0;
                        }
                    }
                }
            }
            if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                mMagnetoOutput = event.values;
            if (mAccelleroOutput != null && mMagnetoOutput != null) {
                float R[] = new float[9];
                float I[] = new float[9];
                boolean success = SensorManager.getRotationMatrix(R, I, mAccelleroOutput, mMagnetoOutput);
                if (success) {
                    float orientation[] = new float[3];
                    SensorManager.getOrientation(R, orientation);
                    Log.d("ORIENTATION OUTPUT", ", 0: " + Float.toString(orientation[0])
                            + ", 1: " + Float.toString(orientation[1])
                            + ", 2: " + Float.toString(orientation[2]));
                }
            }
            /*if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                Log.d("ACCELERO OUTPUT", ", 0: " + Float.toString(event.values[0])
                        + ", 1: " + Float.toString(event.values[1])
                        + ", 2: " + Float.toString(event.values[2]));
                float[] rotation = new float[9], orientation = new float[3];
                SensorManager.getOrientation(rotation, orientation);
                Log.d("ORIENTATION OUTPUT", ", 0: " + Float.toString(orientation[0])
                        + ", 1: " + Float.toString(orientation[1])
                        + ", 2: " + Float.toString(orientation[2]));
            }
            if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
                Log.d("ORIENTATION OUTPUT", ", 0: " + Float.toString(event.values[0])
                        + ", 1: " + Float.toString(event.values[1]));
            }*/
        }

        private boolean angleInThreshold(double angle, float threshold)  {
            if(90 - Math.abs(angle)%90 <= threshold || Math.abs(angle)%90 <= threshold)    {
                return true;
            }
            return false;
        }

    };

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {

            File pictureFile = MediaFileHandlers.getOutputMediaFile();
            if (pictureFile == null){
                Log.d("TAG", "Error creating media file, check storage permissions: ");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } catch (FileNotFoundException e) {
                Log.d("TAG", "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d("TAG", "Error accessing file: " + e.getMessage());
            }
        }
    };

    private void initSensors()  {
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        initSensor(mSensorManager, mAccelerometer, Sensor.TYPE_ACCELEROMETER);
//        initSensor(mSensorManager, mMagnetometer, Sensor.TYPE_MAGNETIC_FIELD);
    }

    private void initSensor(SensorManager sensorManager, Sensor sensor, int sensorType)   {
        sensor = sensorManager.getDefaultSensor(sensorType);
        mSensorManager.registerListener(mSensorListener, sensor, SensorManager.SENSOR_DELAY_GAME);
    }

    private static Camera getCameraInstance(int cameraIndex){
        Camera c = null;
        try {
            c = Camera.open(cameraIndex); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    private void startCamera(FrameLayout frameLayout)  {
        cameraObj = getCameraInstance(BACK_CAMERA_INDEX);
        mPreview = new CameraSurfacePreview(NativeCameraActivity.this, cameraObj);
        frameLayout.addView(mPreview);
        setCameraDisplayOrientation(NativeCameraActivity.this, BACK_CAMERA_INDEX, cameraObj);
        cameraObj.setPreviewCallback(NativeCameraActivity.this);
    }

    // Stop the camera preview, release the camera, and make the camera objects null
    private void stopCamera(FrameLayout frameLayout)   {
        if(cameraObj != null)   {
            cameraObj.stopPreview();
            cameraObj.setPreviewCallback(null);
            frameLayout.removeView(mPreview);
            cameraObj.release();
        }
        cameraObj = null;
    }

    public static void setCameraDisplayOrientation(Activity activity, int cameraId,
                                                   android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT );
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Paint guidePaint = new Paint();
        guidePaint.setColor(Color.RED);

        setContentView(R.layout.activity_main);

        previewFrame = (FrameLayout) findViewById(R.id.camera_preview_frame);
        startCamera(previewFrame);
        actionSound = new MediaActionSound();
        actionSound.load(MediaActionSound.SHUTTER_CLICK);

        mOverlaySurface = (OverlaySurfaceView)findViewById(R.id.overlay_surface);

        this.initSensors();

        previewFrame.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            actionSound.play(MediaActionSound.SHUTTER_CLICK);
                            cameraObj.takePicture(null, null, mPicture);
                            Thread.sleep(1000);
                        } catch(InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                        cameraObj.startPreview();
                    }
                }
        );

    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera)   {

    }

    @Override
    protected void onPause()    {
        super.onPause();
        stopCamera(previewFrame);
    }

    @Override
    protected void onDestroy()  {
        super.onDestroy();
    }

    @Override
    protected void onResume()   {
        super.onResume();
        if(cameraObj != null)   {
            stopCamera(previewFrame);
        }
        startCamera(previewFrame);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
