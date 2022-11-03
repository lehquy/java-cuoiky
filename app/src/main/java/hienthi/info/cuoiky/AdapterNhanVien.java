package hienthi.info.cuoiky;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterNhanVien extends BaseAdapter {
    Activity context;
    ArrayList<NhanVien> listnv;

    public AdapterNhanVien(Activity context, ArrayList<NhanVien> listnv) {
        this.context = context;
        this.listnv = listnv;
    }

    @Override
    public int getCount() {
        return listnv.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.listview_row, null);

        TextView txtmanv = (TextView) row.findViewById(R.id.txtmanv);
        TextView txthoten = (TextView) row.findViewById(R.id.txthoten);
        TextView txtngaysinh = (TextView) row.findViewById(R.id.txtngaysinh);
        TextView txtgioitinh = (TextView) row.findViewById(R.id.txtgioitinh);
        TextView txtsdt = (TextView) row.findViewById(R.id.txtsdt);
        TextView txtphongban = (TextView) row.findViewById(R.id.txtphongban);
        ImageView imgAva = (ImageView) row.findViewById(R.id.imgAva);

        Button btnSua = (Button) row.findViewById(R.id.btnSua);
        Button btnXoa = (Button) row.findViewById(R.id.btnXoa);

        NhanVien nhanVien = listnv.get(i);
        txthoten.setText(nhanVien.hoten);
        txtngaysinh.setText(formatter.format(nhanVien.ngaysinh));
        txtgioitinh.setText(nhanVien.gioitinh);
        txtsdt.setText(nhanVien.sdt);
        txtphongban.setText(nhanVien.phongban);
        txtmanv.setText(nhanVien.manv);

        Bitmap bmAva = BitmapFactory.decodeByteArray(nhanVien.anh, 0, nhanVien.anh.length);
        imgAva.setImageBitmap(bmAva);

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, UpdateNV.class);
                intent.putExtra("MaNV",nhanVien.manv);
                context.startActivity(intent);
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa nhân viên này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete(nhanVien.manv);
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return row;
    }

    private void delete(String maNv) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        SQLiteDatabase database = Database.initDatabase(context,"QL_TruongHoc.sqlite");
        database.delete("NhanVien","MaNV = ?", new String[] {maNv});

        listnv.clear();

        Cursor cursor = database.rawQuery("Select * from NhanVien", null);
        while (cursor.moveToNext()){
            String ma = cursor.getString(0);
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

            listnv.add(new NhanVien(ma, hoten, ngaysinh, gioitinh, sdt, phongban, anh));
        }
        notifyDataSetChanged();
    }
}
