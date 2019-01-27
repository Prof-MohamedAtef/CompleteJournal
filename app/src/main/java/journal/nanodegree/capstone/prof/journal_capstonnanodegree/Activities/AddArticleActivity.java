package journal.nanodegree.capstone.prof.journal_capstonnanodegree.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import br.com.safety.audio_recorder.AudioListener;
import br.com.safety.audio_recorder.AudioRecordButton;
import br.com.safety.audio_recorder.AudioRecording;
import br.com.safety.audio_recorder.RecordingItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.R;
import journal.nanodegree.capstone.prof.journal_capstonnanodegree.helpers.Config;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AddArticleActivity extends AppCompatActivity {

    private Date Now;
    private Calendar calendar;
    private int year;
    private int month;
    private int day;
    String imageFileName;
    String currentImagePAth;

    @BindView(R.id.camera)
    ImageView Camera;

    @BindView(R.id.ImageReport)
    ImageView ImageReport;

    @BindView(R.id.DateTime)
    TextView DateTime;

    public static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 55;
    private String IMAGE_TYPE="image/*";
    final static int SELECT_PICTURE=12;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    protected static final int GALLERY_PICTURE = 1;
    final static int RESULT_LOAD_IMAGE=11;
    String selectedImagePath;
    Uri selectedImage;
    Bitmap bitmap;
    Bitmap imageBitmap;
    BitmapFactory.Options imageBitmapOp;
    String [] filePathColumn;
    private boolean HasImage=false;
    private String UploadedImage1;
    private String DATA_KEY="data";
    private java.lang.String SampleDateFormat_KEY="yyyyMMdd_HHmmss";
    private String JPEG_KEY="JPEG_";
    private java.lang.String JPG_EXTENSION=".jpg";
    private String FILE_EXTENSION="file:";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_article);
        ButterKnife.bind(this);
                calendar = new Calendar() {
                    @Override
                    protected void computeTime() {
                    }

                    @Override
                    protected void computeFields() {

                    }

                    @Override
                    public void add(int field, int amount) {

                    }

                    @Override
                    public void roll(int field, boolean up) {

                    }

                    @Override
                    public int getMinimum(int field) {
                        return 0;
                    }

                    @Override
                    public int getMaximum(int field) {
                        return 0;
                    }

                    @Override
                    public int getGreatestMinimum(int field) {
                        return 0;
                    }

                    @Override
                    public int getLeastMaximum(int field) {
                        return 0;
                    }
                };
        Camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialougeChooseCameraOrGallery();
            }
        });
    }

    private void DialougeChooseCameraOrGallery() {
        Intent pickIntent = new Intent();
        pickIntent.setType(IMAGE_TYPE);
        pickIntent.setAction(Intent.ACTION_GET_CONTENT);
        Intent takePhotoIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        String pickTitle = this
                .getResources()
                .getString(R.string.chooser_Intent_select_or_take_picture); // Or
        // get
        // from
        // strings.xml
        Intent chooserIntent = Intent.createChooser(pickIntent,
                pickTitle);
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                new Intent[] { takePhotoIntent });
        startActivityForResult(chooserIntent, SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED&&data!=null){
            ActivityCompat.requestPermissions(AddArticleActivity.this,
                    new String[]{WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get(DATA_KEY);
                setBitmapToImageView(imageBitmap);
                try{
                    createImageFile();
                    addPicToPhone();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            else if (requestCode == GALLERY_PICTURE && resultCode == RESULT_OK) {
                if (data != null) {
                    Bundle selectedImage = data.getExtras();
                    filePathColumn = new String[]{MediaStore.Images.Media.DATA};
                    String filePath =  MediaStore.Images.Media.DATA ;
                    Bitmap  imagebitmap=(Bitmap)selectedImage.get(DATA_KEY);
                    Config.imageBitmap=imagebitmap.toString();
                    Cursor c = getContentResolver().query(Uri.parse(filePath), filePathColumn, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePathColumn[0]);
                    selectedImagePath = c.getString(columnIndex);
                    Config.image_name=selectedImagePath;
                    c.close();
                    if (selectedImagePath != null) {
                        bitmap = BitmapFactory.decodeFile(selectedImagePath);
                        bitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, false);
                        ImageReport.setImageBitmap(bitmap);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.canceled),
                            Toast.LENGTH_SHORT).show();
                }
            }else if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
                selectedImage = data.getData();
                filePathColumn = new String[]{MediaStore.Images.Media.DATA};
                imageBitmap= LoadThenDecodeBitmap();
                setBitmapToImageView(imageBitmap);
            }else if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK){
                selectedImage = data.getData();
                filePathColumn = new String[]{MediaStore.Images.Media.DATA};
                if (selectedImage!=null){
                    imageBitmap= LoadThenDecodeBitmap();
                    setBitmapToImageView(imageBitmap);
                }else {
                    try {
                        getUriFromFile(data);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageBitmap= LoadThenDecodeBitmap();

                    setBitmapToImageView(imageBitmap);
                }
            }
            if (Config.currentImagePAth!=null){
                UploadedImage1= Config.currentImagePAth;
            }else if (Config.imageBitmap!=null){
                UploadedImage1= Config.imageBitmap;
            }else if (Config.selectedImagePath!=null){
                UploadedImage1=Config.selectedImagePath;
            }
        }
    }

    private void getUriFromFile(Intent data) throws IOException {
        Bundle extras = data.getExtras();
//        Bitmap var_Bitmap = (Bitmap) extras.get(DATA_KEY);
        imageBitmap = (Bitmap) extras.get(DATA_KEY);
        try{
            createImageFile();
            addPicToPhone();
        }catch (Exception e){
            e.printStackTrace();
        }
        /*

         */
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        var_Bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//        byte[] bytes = stream.toByteArray();
//        try {
//            OutputStream out;
//            String root = Environment.getExternalStorageDirectory().getAbsolutePath()+"/";
//            File createDir = new File(root+"macro2"+File.separator);
//            boolean isCreated=createDir.mkdir();
//            boolean isExists=createDir.exists();
//        File AbsoluteFile=createDir.getAbsoluteFile();
//        String AbsolutePath=createDir.getAbsolutePath();
//        String FileName=AbsolutePath+ File.separator +"macro2.jpg";
//            if (AbsolutePath!=null){
//                File file = new File(root + "macro2" + File.separator +"macro2.jpg");
//                File file = new File(FileName);
//                file.createNewFile();
//                out = new FileOutputStream(file);
//                out.write(bytes);
//                out.close();
//                selectedImage= Uri.fromFile(file);
//            }
//        } catch (IOException e) {
            // e.printStackTrace();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Now= Calendar.getInstance().getTime();
        DateTime.setText(Now.toString());
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    private Bitmap LoadThenDecodeBitmap(){
        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        selectedImagePath= cursor.getString(columnIndex);
        imageBitmap= decodeSampledBitmapFromResource(selectedImagePath,100,100);
        Config.selectedImagePath=selectedImagePath;
        Config.imageBitmap=imageBitmap.toString();
        Config.image_name=selectedImagePath;
        return imageBitmap;
    }

    public static Bitmap decodeSampledBitmapFromResource(String selectedImagePath, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath,options);
        Config.selectedImagePath=selectedImagePath;
        Config.image_name=selectedImagePath;
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
//        return BitmapFactory.decodeResource(res, resId, options);
        return BitmapFactory.decodeFile(selectedImagePath, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    private void  addPicToPhone(){
        Intent mediaScanIntent= new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f= new File(currentImagePAth);
        Uri contentUri= Uri.fromFile(f);
        selectedImage=contentUri;
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    private void CreateImageFileName(){
        String timpstamp = new SimpleDateFormat(SampleDateFormat_KEY).format(new Date());
        imageFileName = timpstamp+"1" + JPEG_KEY ;
        Config.image_name=imageFileName;
    }

    private File createImageFile() throws IOException {
        //create image name
        File image = null;
        CreateImageFileName();
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        Config.StorageDir=storageDirectory;
//        if (ContextCompat.checkSelfPermission(this,
//                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
//                new AlertDialog.Builder(this)
//                        .setTitle(R.string.title_write_external_storage_permission)
//                        .setMessage(R.string.text_write_external_permission)
//                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
//                                ActivityCompat.requestPermissions(AddArticleActivity.this,
//                                        new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                                        MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
//                            }
//                        })
//                        .create()
//                        .show();

//            } else {
                // No explanation needed; request the permission
//                ActivityCompat.requestPermissions((Activity) getApplicationContext(),
//                        new String[]{Manifest.permission.MEDIA_CONTENT_CONTROL},55);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
//            }
//        } else {


            // Permission has already been granted
//        }
        image = CreateTempFileMethod(storageDirectory);
        return image;
    }

    @NonNull
    private File CreateTempFileMethod(File storageDirectory) throws IOException {
        File image;
        image = File.createTempFile(imageFileName, JPG_EXTENSION, storageDirectory);
        //save file name
        currentImagePAth = FILE_EXTENSION + image.getAbsolutePath();
        Config.currentImagePAth=currentImagePAth;
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // Writing-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        //Request Writing updates:
                        Toast.makeText(getApplicationContext(), "Permission permitted to read your External storage", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private void setBitmapToImageView(Bitmap imageBitmap) {
        if (ImageReport.getDrawable()==null){
            ImageReport.setImageBitmap(imageBitmap);
            HasImage=true;
        }
    }
}