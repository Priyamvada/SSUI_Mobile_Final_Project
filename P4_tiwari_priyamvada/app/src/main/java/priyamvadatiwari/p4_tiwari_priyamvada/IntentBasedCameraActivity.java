package priyamvadatiwari.p4_tiwari_priyamvada;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class IntentBasedCameraActivity extends Activity {

    private static final int FRONT_CAMERA_INDEX = 1;
    private static final int BACK_CAMERA_INDEX = 0;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private static final int CAPTURE_ACTION_VIDEO_CAM_REQUEST_CODE = 300;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    GLSurfaceView mOverlaySurface;

    private Uri fileUri, ImageFileUri, VideoFileUri;

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mOverlaySurface = (GLSurfaceView)findViewById(R.id.overlay_surface);

        Intent intent = new Intent(MediaStore.INTENT_ACTION_VIDEO_CAMERA);
        // create Intent to take a picture and return control to the calling application
        Intent imageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //create new Intent to record a video and return control to the calling application
        Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        VideoFileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);  // create a file to save the video
        ImageFileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image

        videoIntent.putExtra(MediaStore.EXTRA_OUTPUT, VideoFileUri);  // set the image file name
        videoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high
        imageIntent.putExtra(MediaStore.EXTRA_OUTPUT, ImageFileUri); // set the image file name

        intent.putExtra(MediaStore.EXTRA_OUTPUT, VideoFileUri);  // set the image file name
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high
        intent.putExtra(MediaStore.EXTRA_OUTPUT, ImageFileUri);

        // start the image capture Intent
//        startActivityForResult(imageIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        // start the Video Capture Intent
//        startActivityForResult(videoIntent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
        startActivityForResult(intent, CAPTURE_ACTION_VIDEO_CAM_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Image saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }
        }

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Video saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the video capture
            } else {
                // Video capture failed, advise user
            }
        }

        if (requestCode == CAPTURE_ACTION_VIDEO_CAM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Media saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
            } else {
            }
        }
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
