package hienthi.info.cuoiky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    final String DATABASE_NAME = "QL_TruongHoc.sqlite";
    SQLiteDatabase database;

    ListView listViewNv;
    ArrayList<NhanVien> listnv;
    AdapterNhanVien adapterNhanVien;
    Button btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        readData();
    }

    private void addControls() {
        listViewNv =(ListView) findViewById(R.id.listViewNV);
        listnv = new ArrayList<>();
        adapterNhanVien = new AdapterNhanVien(this, listnv);
        listViewNv.setAdapter(adapterNhanVien);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddNV.class);
                startActivity(intent);
            }
        });
    }

    private void readData(){
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        database = Database.initDatabase(this,DATABASE_NAME);
        Cursor cursor = database.rawQuery("Select * from NhanVien", null);
        listnv.clear();

        for (int i =0; i<cursor.getCount(); i++){
            cursor.moveToPosition(i);
            String manv = cursor.getString(0);
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
            listnv.add(new NhanVien(manv, hoten, ngaysinh, gioitinh, sdt, phongban, anh));
        }
        adapterNhanVien.notifyDataSetChanged();

    }
}