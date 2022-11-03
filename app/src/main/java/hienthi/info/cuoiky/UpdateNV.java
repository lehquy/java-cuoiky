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
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateNV extends AppCompatActivity {
    final String DATABASE_NAME = "QL_TruongHoc.sqlite";
    final int REQUEST_TAKE_PHOTO = 123;
    final int REQUEST_CHOOSE_PHOTO = 321;
    String manv="";

    Button btnChup, btnChon, btnLuu, btnHuy;
    EditText edtHoten, edtNgaysinh, edtGt, edtSdt, edtPB;
    ImageView imgAvaUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_nv);

        addControls();
        addEvents();
        initUI();
    }

    private void initUI() {
        Intent intent = getIntent();
        manv = intent.getStringExtra("MaNV");
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        SQLiteDatabase database = Database.initDatabase(this,DATABASE_NAME);
        Cursor cursor = database.rawQuery("Select * from NhanVien where MaNV = ? ", new String[]{manv});
        cursor.moveToFirst();
        String hoten = cursor.getString(1);
        Date ngaysinh = null;
        try {
            ngaysinh = formatter.parse(cursor.getString(2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String gioitinh = cursor.getString(3);
        String sdt = cursor.getString(4);
        String phongban = cursor.getString(5);
        byte[]anh = cursor.getBlob(6);

        Bitmap bitmap = BitmapFactory.decodeByteArray(anh, 0 , anh.length);
        imgAvaUp.setImageBitmap(bitmap);
        edtHoten.setText(hoten);
        edtNgaysinh.setText(formatter.format(ngaysinh));
        edtGt.setText(gioitinh);
        edtSdt.setText(sdt);
        edtPB.setText(phongban);
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
                update();
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

        edtHoten =(EditText) findViewById(R.id.edtHoten);
        edtNgaysinh =(EditText) findViewById(R.id.edtNgaysinh);
        edtGt =(EditText) findViewById(R.id.edtGt);
        edtSdt =(EditText) findViewById(R.id.edtSdt);
        edtPB =(EditText) findViewById(R.id.edtPB);

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

    private void update(){
        String hoten = edtHoten.getText().toString();
        String ngaysinh = edtNgaysinh.getText().toString();
        String gioitinh = edtGt.getText().toString();
        String sdt = edtSdt.getText().toString();
        String phongban = edtPB.getText().toString();
        byte[] anh = getByArrayFromImageView(imgAvaUp);

        ContentValues contentValues = new ContentValues();
        contentValues.put("HoTen",hoten);
        contentValues.put("NgaySinh",ngaysinh);
        contentValues.put("GioiTinh",gioitinh);
        contentValues.put("SDT",sdt);
        contentValues.put("PhongBan",phongban);
        contentValues.put("Anh",anh);

        SQLiteDatabase database = Database.initDatabase(this,"QL_TruongHoc.sqlite");
        database.update("NhanVien",contentValues, "MaNV = ?",new String[]{manv});
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