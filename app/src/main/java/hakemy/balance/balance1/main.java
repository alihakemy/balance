package hakemy.balance.balance1;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Parameter;
import java.security.Policy;
import java.security.acl.Permission;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class main extends AppCompatActivity implements TextureView.SurfaceTextureListener {

    private static int SELECT_IMAGE = 3;
//

    //
//    //image uri
    Uri uri;

//
//    static String code_call_vodafone = "*858*",orange_code="*102*",we="*555*",et="*556*";
//


    // permission request codes need to be < 256
    private static final int RC_HANDLE_CAMERA_PERM = 2;


    Camera camera;
    TextureView mTextureView;
    Bitmap bitmap1;

    private TextView textView;
    private ProgressBar progressBar;
    private ImageView imgFlash, capture;
    private Boolean onFlash = false;
    private Camera.Parameters parameters;
    private boolean per = false;


    Camera mCamera;
    CameraPreview mPreview;
    LinearLayout preview;
    boolean flash = false;
    int width;
    int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, RC_HANDLE_CAMERA_PERM);


        if (GetActionFromAnotherApp() != null) {
            uri = (Uri) GetActionFromAnotherApp();
            crop(uri);

        }
        imgFlash = (ImageView) findViewById(R.id.flash);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        preview = (LinearLayout) findViewById(R.id.camera_preview);

//            mTextureView = (TextureView) findViewById(R.id.textureView);
//            mTextureView.setSurfaceTextureListener(this);
//            mTextureView.setAlpha(1f);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.relative);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        draw draw = new draw(this);
        draw.setTop1(height / 2);  //y
        draw.setLeft1(0); //x
        draw.setBottom1((height / 2) + 70); // y end
        draw.setRight1(width);   // xend
        relativeLayout.addView(draw);


    }


    public void capture(View view) {
//        permission(PermissionCode);
//        capture =(ImageView) findViewById(R.id.capture);
//        Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
//        capture.startAnimation(rotate);
//        mSaveImage();
//        CropImage.activity(uri).setRequestedSize(50, 50);
//        new textRecognizer().execute(cropBitmap1());


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                mCamera.takePicture(null, null, mPicture);

            }
        });
        thread.start();

        //cropBitmap1();
        //new textRecognizer().execute(cropBitmap1());


    }


    public Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Matrix matrix = new Matrix();
            matrix.setRotate(90);
            bitmap1 = BitmapFactory.decodeByteArray(data, 0, data.length);
            bitmap1 = Bitmap.createBitmap(bitmap1, 0, 0, bitmap1.getWidth(), bitmap1.getHeight(), matrix, true);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                    bitmap1, preview.getWidth(), preview.getHeight(), false);

            new textRecognizer().execute(cropBitmap1(resizedBitmap));


            mCamera = Camera.open();
            mPreview = new CameraPreview(getApplicationContext(), mCamera);
            preview.removeAllViews();
            preview.addView(mPreview);


        }
    };


    public void On_Off_flash(View view) {

        if (flash) {
            flash = false;
            imgFlash.setImageResource(R.drawable.ic_flash_off_black_24dp);
        } else {
            flash = true;
            imgFlash.setImageResource(R.drawable.ic_flash_on_black_24dp);

        }
        mPreview.Flash(flash);
    }

    private Bitmap cropBitmap1(Bitmap bitmap) {

        // Bitmap bmp2 =BitmapFactory.decodeFile(uri.getPath());
        Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, 0, height / 2,
                width, 70);
        return croppedBitmap;
    }


    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        camera = Camera.open();
        camera.setDisplayOrientation(90);
        /* Set Auto focus */
        Camera.Parameters parameters = camera.getParameters();
        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            if (parameters.isSmoothZoomSupported()) {
                camera.startSmoothZoom(parameters.getMaxZoom());
            }
        }

        camera.setParameters(parameters);


        try {
            camera.setPreviewTexture(surface);
            camera.startPreview();


        } catch (IOException ioe) {
            // Something bad happened
        }


    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
        }
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {


        parameters = camera.getParameters();
        if (onFlash) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        } else {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        }

        List<String> focusModes = parameters.getSupportedFocusModes();
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            if (parameters.isSmoothZoomSupported()) {
                camera.startSmoothZoom(parameters.getMaxZoom());
            }
        }


        camera.setParameters(parameters);


    }


//    public boolean mSaveImage() {
//
//        boolean result = false;
//        bitmap =  mPreview.getDrawingCache();
//        if (addJpgSignatureToGallery(bitmap))
//        {
//            result = true;
//            return result;
//        }
//        return result;
//    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap myBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(myBitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }


    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            File photo = new File(getAlbumStorageDir("Balance"), String.format("Signature_%d.png", System.currentTimeMillis()));
            uri = Uri.fromFile(photo);
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        main.this.sendBroadcast(mediaScanIntent);
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }


    /**
     * Bitmap input
     * Integer progress
     * String output
     */
    class textRecognizer extends AsyncTask<Bitmap, Integer, String> {


        private TextRecognizer textRecognizer;
        private Frame frame;
        private SparseArray<TextBlock> textBlockSparseArray;
        private StringBuilder stringBuilder;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.incrementProgressBy(values[0]);
        }

        @Override
        protected String doInBackground(Bitmap... bitmaps) {

            textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
            frame = new Frame.Builder().setBitmap(bitmaps[0]).build();

            textBlockSparseArray = textRecognizer.detect(frame);
            stringBuilder = new StringBuilder();
            for (int i = 0; i < textBlockSparseArray.size(); i++) {
                TextBlock textBlock = textBlockSparseArray.get(i);
                stringBuilder.append(textBlock.getValue());
            }


            StringBuilder stringBuilder1 = new StringBuilder();

            for (int i = 0; i < stringBuilder.toString().length(); i++) {
                if (Character.isDigit(stringBuilder.toString().charAt(i))) {
                    stringBuilder1.append(stringBuilder.toString().charAt(i)).toString().trim();

                }
            }

            return stringBuilder1.toString();
        }

        @Override
        protected void onPostExecute(String s) {

            imgFlash.setImageResource(R.drawable.ic_flash_off_black_24dp);

            flash = false;

            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();

            progressBar.setVisibility(View.GONE);

            Intent intent = new Intent(getApplicationContext(), CallTransation.class);

            intent.putExtra("number", s);
            startActivity(intent);


        }
    }


    /**
     * open Select Picture
     */
    public void SelectPicture() {

        uri = null;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);

    }


    /**
     * check Call permission
     *
     * @param code
     * @return
     */
    public boolean permission(int code) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, code);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, code);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, code);
        }


        return per;
    }

    /**
     * Return data which passed from another app ex(image_uri ,text,.....)
     *
     * @return
     */
    public Object GetActionFromAnotherApp() {

        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
                if (imageUri != null) {
                    return imageUri;
                }
            }
            //....


        }


        return null;
    }


    /**
     * @param uri1 get image uri to crop image
     */
    public void crop(Uri uri1) {
        CropImage.activity(uri1).start(this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RC_HANDLE_CAMERA_PERM) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                // Create our Preview view and set it as the content of our activity.
                mCamera = Camera.open();
                mPreview = new CameraPreview(this, mCamera);
                preview.addView(mPreview);


            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();

            }

        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        /**
         * when click on imageview to choice image
         */
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        uri = data.getData();
                        crop(uri);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }


        /**
         * to get croped image from activity crop
         */
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Bitmap mBitmap = null; // this is croped image
                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                new textRecognizer().execute(mBitmap);

                uri = null;

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        uri = null;
    }

    @Override
    protected void onPause() {

        super.onPause();
        uri = null;
    }

    @Override
    protected void onStop() {

        super.onStop();
        uri = null;
    }

}
