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

public class MainToMon extends AppCompatActivity {
    final String DATABASE_NAME = "QL_TruongHoc.sqlite";
    SQLiteDatabase database;

    ListView listViewGv;
    ArrayList<ToMon> listtm;
    AdapterToMon adapterToMon;
    Button btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_to_mon);

        addControls();
        readData();
    }

    private void addControls() {
        listViewGv =(ListView) findViewById(R.id.listViewGV);
        listtm = new ArrayList<>();
        adapterToMon = new AdapterToMon(this, listtm);
        listViewGv.setAdapter(adapterToMon);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainToMon.this,AddGV.class);
                startActivity(intent);
            }
        });
    }

    private void readData(){
        database = Database.initDatabase(this,DATABASE_NAME);
        Cursor cursor = database.rawQuery("Select * from ToMon", null);
        listtm.clear();

        for (int i =0; i<cursor.getCount(); i++){
            cursor.moveToPosition(i);
            String magv = cursor.getString(0);
            String chuyenmon = cursor.getString(1);
            String chucvu = cursor.getString(2);
            String matt = cursor.getString(3);

            byte[]anh = cursor.getBlob(4);
            listtm.add(new ToMon(magv, chuyenmon, chucvu, matt, anh));
        }
        adapterToMon.notifyDataSetChanged();

    }
}