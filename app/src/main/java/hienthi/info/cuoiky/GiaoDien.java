package hienthi.info.cuoiky;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GiaoDien extends AppCompatActivity {
    Button btnNV, btnTM, btnLH;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giao_dien);
        btnNV = (Button) findViewById(R.id.btnNV);
        btnTM = (Button) findViewById(R.id.btnTM);
        btnLH = (Button) findViewById(R.id.btnLH);

        btnNV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GiaoDien.this,MainActivity.class);
                startActivity(intent);
            }
        });

        btnTM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GiaoDien.this,MainToMon.class);
                startActivity(intent);
            }
        });

        btnLH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GiaoDien.this,MainLopHoc.class);
                startActivity(intent);
            }
        });
    }
}