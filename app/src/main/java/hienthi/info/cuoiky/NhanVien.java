package hienthi.info.cuoiky;

import java.util.Date;

public class NhanVien {
    public String manv;
    public String hoten;
    public Date ngaysinh;
    public String gioitinh;
    public String sdt;
    public String phongban;
    public byte[] anh;

    public NhanVien(String manv, String hoten, Date ngaysinh, String gioitinh, String sdt, String phongban, byte[] anh) {
        this.manv = manv;
        this.hoten = hoten;
        this.ngaysinh = ngaysinh;
        this.gioitinh = gioitinh;
        this.sdt = sdt;
        this.phongban = phongban;
        this.anh = anh;
    }
}
