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

public class AdapterToMon extends BaseAdapter {
    Activity context;
    ArrayList<ToMon> listtm;

    public AdapterToMon(Activity context, ArrayList<ToMon> listtm) {
        this.context = context;
        this.listtm = listtm;
    }

    @Override
    public int getCount() {
        return listtm.size();
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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.listview_row_tm, null);

        TextView txtmagv = (TextView) row.findViewById(R.id.txtmagv);
        TextView txtchuyenmon = (TextView) row.findViewById(R.id.txtchuyenmon);
        TextView txtchucvu = (TextView) row.findViewById(R.id.txtchucvu);
        TextView txtmatt = (TextView) row.findViewById(R.id.txtmatt);
        ImageView imgAva = (ImageView) row.findViewById(R.id.imgAva);

        Button btnSua = (Button) row.findViewById(R.id.btnSua);
        Button btnXoa = (Button) row.findViewById(R.id.btnXoa);

        ToMon toMon = listtm.get(i);
        txtchuyenmon.setText(toMon.chuyenmon);
        txtchucvu.setText(toMon.chucvu);
        txtmatt.setText(toMon.matt);

        txtmagv.setText(toMon.magv);

        Bitmap bmAva = BitmapFactory.decodeByteArray(toMon.anhtm, 0, toMon.anhtm.length);
        imgAva.setImageBitmap(bmAva);

        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, updateGV.class);
                intent.putExtra("MaGV",toMon.magv);
                context.startActivity(intent);
            }
        });

        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setIcon(android.R.drawable.ic_delete);
                builder.setTitle("Xác nhận xóa");
                builder.setMessage("Bạn có chắc chắn muốn xóa Giáo viên này?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delete(toMon.magv);
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

    private void delete(String magv) {
        SQLiteDatabase database = Database.initDatabase(context,"QL_TruongHoc.sqlite");
        database.delete("ToMon","MaGV = ?", new String[] {magv});

        listtm.clear();

        Cursor cursor = database.rawQuery("Select * from ToMon", null);
        while (cursor.moveToNext()){
            String ma = cursor.getString(0);
            String chuyenmon = cursor.getString(1);
            String chucvu = cursor.getString(2);
            String matt = cursor.getString(3);
            byte[] anhtm = cursor.getBlob(4);

            listtm.add(new ToMon(ma, chuyenmon, chucvu, matt, anhtm));
        }
        notifyDataSetChanged();
    }
}
