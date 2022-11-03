package hienthi.info.cuoiky;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;

public class AddGV extends AppCompatActivity {


    final String DATABASE_NAME = "QL_TruongHoc.sqlite";
    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;

    Button btnChup, btnChon, btnLuu, btnHuy;
    EditText edtMagv, edtchuyenmon, edtchucvu, edtmatt;
    ImageView imgAvaUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gv);

        addControls();
        addEvents();
    }

    private void addEvents(){
        btnChon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePhoto();
            }
        });

        btnChup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePicture();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insert();
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });
    }

    private void addControls() {
        btnChup =(Button) findViewById(R.id.btnChup);
        btnChon =(Button) findViewById(R.id.btnChon);
        btnLuu =(Button) findViewById(R.id.btnLuu);
        btnHuy =(Button) findViewById(R.id.btnHuy);

        edtMagv =(EditText) findViewById(R.id.edtMagv);
        edtchuyenmon =(EditText) findViewById(R.id.edtchuyenmon);
        edtchucvu =(EditText) findViewById(R.id.edtchucvu);
        edtmatt =(EditText) findViewById(R.id.edtmatt);

        imgAvaUp =(ImageView) findViewById(R.id.imgAvaUp);
    }

    private void takePicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    private  void choosePhoto(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_CHOOSE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_CHOOSE_PHOTO){

                try {
                    Uri imageUri = data.getData();
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    imgAvaUp.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else if (requestCode == REQUEST_TAKE_PHOTO){
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                imgAvaUp.setImageBitmap(bitmap);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void insert(){
        String magv = edtMagv.getText().toString();
        String chuyemon = edtchuyenmon.getText().toString();
        String chucvu = edtchucvu.getText().toString();
        String matt = edtmatt.getText().toString();

        byte[] anh = getByArrayFromImageView(imgAvaUp);

        ContentValues contentValues = new ContentValues();
        contentValues.put("MaGV",magv);
        contentValues.put("ChuyenMon",chuyemon);
        contentValues.put("ChucVu",chucvu);
        contentValues.put("MaTT",matt);
        contentValues.put("AnhGv",anh);

        SQLiteDatabase database = Database.initDatabase(this,"QL_TruongHoc.sqlite");
        database.insert("ToMon",null,contentValues);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void cancel(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private byte[] getByArrayFromImageView(ImageView imgv){

        BitmapDrawable drawable = (BitmapDrawable) imgv.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byeArray = stream.toByteArray();
        return byeArray;
    }
}