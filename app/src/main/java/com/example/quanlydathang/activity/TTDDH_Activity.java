package com.example.quanlydathang.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlydathang.R;
import com.example.quanlydathang.adapter.TTDDH_Adapter;
import com.example.quanlydathang.dao.DonHangDao;
import com.example.quanlydathang.dao.TTDDH_DAO;
import com.example.quanlydathang.dto.DonHangDto;
import com.example.quanlydathang.dto.KhachHangDto;
import com.example.quanlydathang.dto.Product;
import com.example.quanlydathang.dto.TTDDH_DTO;
import com.example.quanlydathang.utils.Constants;
import com.example.quanlydathang.utils.CustomAlertDialog;
import com.example.quanlydathang.utils.CustomToast;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class TTDDH_Activity extends AppCompatActivity {
    public static int width;

    private DonHangDto mDonHangDto;
    private KhachHangDto mKhachHangDto;

    private TTDDH_DAO ttddh_dao;
    private List<TTDDH_DTO> ttddh_dtoList;
    private TTDDH_Adapter ttddh_adapter;

    private RecyclerView rcvTTDDH;
    private TextView tvMaDH, tvNgayDH, tvTenKH, tvSDT, tvDiaChi, tvTongTien;
    private Button  btnThemSP, btnCapNhat;

    private SearchView searchView;

    private EditText etMail_dialogSendMail;
    private Button btnHuy_dialogSendMail, btnGui_dialogSendMail;

    private String mPDFPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ttddh);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle == null) {
            CustomToast.makeText(TTDDH_Activity.this,"Không lấy được dữ liệu đơn hàng!", CustomToast.LENGTH_LONG,CustomToast.ERROR).show();
            finish();
        }

        DisplayMetrics metrics=getResources().getDisplayMetrics();
        width = metrics.widthPixels;

        ttddh_dao = new TTDDH_DAO(this);

        mDonHangDto = new DonHangDto(bundle.getInt("maDH"), bundle.getString("ngayDH"), bundle.getInt("maKH"), bundle.getString("tenKH"));
//        CustomToast.makeText(TTDDH_Activity.this,mDonHangDto.toString(),CustomToast.LENGTH_LONG, CustomToast.CONFUSING).show();
        mKhachHangDto = ttddh_dao.TimKhachHang(mDonHangDto.getMaKH());

        setControl();

        btnCapNhat.setOnClickListener(view -> {
            if(ttddh_adapter.getTtddh_dtoListOld().isEmpty()) {
                CustomToast.makeText(this,"Đơn hàng rỗng!",CustomToast.LENGTH_SHORT,CustomToast.WARNING).show();
            }
            else {
                try {
                    createPDF();
                    CustomToast.makeText(this,"Xuất file PDF thành công!", Toast.LENGTH_SHORT,CustomToast.SUCCESS).show();
                    sendMail();
                }
                catch (FileNotFoundException e) {
                    CustomToast.makeText(this,"Xuất file PDF thất bại!",CustomToast.LENGTH_SHORT,CustomToast.ERROR).show();
                    e.printStackTrace();
                }
            }
        });

        btnThemSP.setOnClickListener(view -> {
            Dialog dialog = ttddh_adapter.getDialogThemSP_TTDDH(this);

            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout((6*width)/7, WindowManager.LayoutParams.WRAP_CONTENT);

            ttddh_adapter.btnHuy_dialogThemSP.setOnClickListener(view01 -> {
                dialog.cancel();
            });

            ttddh_adapter.btnThem_dialogThemSP.setOnClickListener(view02 -> {
                if(ttddh_adapter.etSoLuong_dialogThemSP.getText().toString().isEmpty()) {
                    CustomToast.makeText(TTDDH_Activity.this, "Vui lòng nhập số lượng!",
                            CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
                }
                else {
                    TTDDH_DTO ttddh_dto = new TTDDH_DTO();
                    ttddh_dto.setMaDH(mDonHangDto.getMaDH());
                    ttddh_dto.setMaSP(ttddh_adapter.selectedProductID());
                    ttddh_dto.setSL(Integer.parseInt(ttddh_adapter.etSoLuong_dialogThemSP.getText().toString()));

                    int id = (int) ttddh_dao.them_ttddh_dao(ttddh_dto);
                    if(id!=-1) {
                        CustomToast.makeText(TTDDH_Activity.this, "Thêm TTDDH thành công!",
                                CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                    }
                    else {
                        CustomToast.makeText(TTDDH_Activity.this, "Thêm TTDDH thất bại!",
                                CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                    }
                    dialog.cancel();
                    onResume();
                }
            });
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvTTDDH.setLayoutManager(linearLayoutManager);
        capNhatDuLieuTTDDH();
    }

    private void setControl() {
        rcvTTDDH = findViewById(R.id.rcv_ttddh);
        btnThemSP = findViewById(R.id.btnThemSP_ttddh);
        btnCapNhat = findViewById(R.id.btnCapNhat_ttddh);
        tvMaDH = findViewById(R.id.tvMaDH_ttddh);
        tvNgayDH = findViewById(R.id.tvNgayDH_ttddh);
        tvTenKH = findViewById(R.id.tvTenKH_ttddh);
        tvSDT = findViewById(R.id.tvSDT_ttddh);
        tvDiaChi = findViewById(R.id.tvDiaChi_ttddh);
        tvTongTien = findViewById(R.id.tvTongTien_ttddh);

        tvMaDH.setText(mDonHangDto.getMaDH()+"");
        tvNgayDH.setText(mDonHangDto.getNgayDH());
        tvTenKH.setText(mKhachHangDto.getName());
        tvSDT.setText(mKhachHangDto.getPhone());
        tvDiaChi.setText(mKhachHangDto.getAddress());
        tvTongTien.setText("-");
    }

    private void capNhatDuLieuTTDDH() {
        ttddh_dtoList = ttddh_dao.ttddh_dtoList();
        ttddh_adapter = new TTDDH_Adapter(this,ttddh_dtoList,mDonHangDto.getMaDH());
        rcvTTDDH.setAdapter(ttddh_adapter);
        tvTongTien.setText((ttddh_adapter.TongTien()) + " USD");
    }

    @Override
    public void onResume() {
        super.onResume();
        capNhatDuLieuTTDDH();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ttddh_adapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                ttddh_adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        if(!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        else if(ttddh_adapter.getTtddh_dtoListOld().isEmpty()) {
            xacNhanThoatTTDDH_Activity();
        }
        else {
            super.onBackPressed();
        }
    }

    public void createPDF() throws FileNotFoundException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();

        String child = "ChiTietDonDatHang-" + mDonHangDto.getMaDH() + ".pdf";
        File file = new File(pdfPath,child);
        OutputStream outputStream = new FileOutputStream(file);

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

//        tableListProducts.addCell(new Cell().add(new Paragraph("")));

        //tittle
        Text textTittle = new Text("CHI TIET DON DAT HANG").setFontSize(28).setBold();
        Paragraph paraTittle = new Paragraph();
        paraTittle.add(textTittle).setTextAlignment(TextAlignment.CENTER);
        document.add(paraTittle);
        document.add(new Paragraph("\n\n"));

        //table thong tin don dat hang
        float columnWidthTTDDH[] = {100,150,100,120,120};
        Table tableTTDDH = new Table(columnWidthTTDDH);
        //1
        tableTTDDH.addCell(new Cell().add(new Paragraph("Khach hang:")));
        tableTTDDH.addCell(new Cell().add(new Paragraph(mDonHangDto.getTenKH())));
        tableTTDDH.addCell(new Cell().add(new Paragraph("")));
        tableTTDDH.addCell(new Cell().add(new Paragraph("Ma don hang:")));
        tableTTDDH.addCell(new Cell().add(new Paragraph(mDonHangDto.getMaDH()+"")));
        //2
        tableTTDDH.addCell(new Cell().add(new Paragraph("SDT:")));
        tableTTDDH.addCell(new Cell().add(new Paragraph(mKhachHangDto.getPhone())));
        tableTTDDH.addCell(new Cell().add(new Paragraph("")));
        tableTTDDH.addCell(new Cell().add(new Paragraph("Ngay dat hang:")));
        tableTTDDH.addCell(new Cell().add(new Paragraph(mDonHangDto.getNgayDH())));
        //3
        tableTTDDH.addCell(new Cell().add(new Paragraph("Dia chi:")));
        tableTTDDH.addCell(new Cell().add(new Paragraph(mKhachHangDto.getAddress())));
        tableTTDDH.addCell(new Cell().add(new Paragraph("")));
        tableTTDDH.addCell(new Cell().add(new Paragraph("Tong gia tri:")));
        tableTTDDH.addCell(new Cell().add(new Paragraph(ttddh_adapter.TongTien() + " USD")));
        //
        document.add(tableTTDDH);
        document.add(new Paragraph("\n\n"));


        //table danh sach san pham
        float columnWidthListProducts[] = {40,90,290,90,90,100};
        Table tableListProducts = new Table(columnWidthListProducts);
        //0
        tableListProducts.addCell(new Cell().add(new Paragraph("STT")).setTextAlignment(TextAlignment.CENTER));
        tableListProducts.addCell(new Cell().add(new Paragraph("Hinh anh")).setTextAlignment(TextAlignment.CENTER));
        tableListProducts.addCell(new Cell().add(new Paragraph("Ten san pham")).setTextAlignment(TextAlignment.CENTER));
        tableListProducts.addCell(new Cell().add(new Paragraph("Don gia")).setTextAlignment(TextAlignment.CENTER));
        tableListProducts.addCell(new Cell().add(new Paragraph("So luong")).setTextAlignment(TextAlignment.CENTER));
        tableListProducts.addCell(new Cell().add(new Paragraph("Thanh tien")).setTextAlignment(TextAlignment.CENTER));
        //1-n
        int stt = 1;
        for(TTDDH_DTO ttddh_dto : ttddh_adapter.getTtddh_dtoListOld()) {
            Product product = ttddh_dao.TimSanPham(ttddh_dto.getMaSP());

            tableListProducts.addCell(new Cell().add(new Paragraph(stt+"")).setTextAlignment(TextAlignment.CENTER));

            Bitmap bitmap;
            if(product.getImage()!=null) {
                bitmap = BitmapFactory.decodeByteArray(product.getImage(), 0, product.getImage().length);

            }
            else {
                Drawable drawable = getDrawable(R.drawable.ic_product);
                bitmap = ((BitmapDrawable)drawable).getBitmap();
            }
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
            byte[] bitmapData = stream.toByteArray();
            ImageData imageData = ImageDataFactory.create(bitmapData);
            Image image = new Image(imageData);
            image.setWidth(80).setHeight(80);
            tableListProducts.addCell(new Cell().add(image));

            tableListProducts.addCell(new Cell().add(new Paragraph(product.getTenSP()+"")));
            tableListProducts.addCell(new Cell().add(new Paragraph(product.getDonGia()+"")).setTextAlignment(TextAlignment.CENTER));
            tableListProducts.addCell(new Cell().add(new Paragraph(ttddh_dto.getSL()+"")).setTextAlignment(TextAlignment.CENTER));
            tableListProducts.addCell(new Cell().add(new Paragraph((product.getDonGia()*ttddh_dto.getSL())+"")).setTextAlignment(TextAlignment.CENTER));

            stt++;
        }
        //
        document.add(tableListProducts);

        //
        Text tongCong = new Text("Tong cong: ").setFontSize(14).setItalic();
        Text tongTien = new Text(ttddh_adapter.TongTien()+" USD").setFontSize(14).setBold();
        document.add(new Paragraph().add(tongCong).add(tongTien).setTextAlignment(TextAlignment.RIGHT));

        //
        mPDFPath = file.getPath();
        document.close();
    }

    private void sendMail() {
        //
        Dialog dialog = getDialogSendMail();

        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout((6*width)/7, WindowManager.LayoutParams.WRAP_CONTENT);

        btnHuy_dialogSendMail.setOnClickListener(view -> {
            dialog.cancel();
        });

        btnGui_dialogSendMail.setOnClickListener(view -> {
            final String username = Constants.emailName;
            final String password = Constants.emailPws;
            String toMail = etMail_dialogSendMail.getText().toString();
            if(toMail.isEmpty()) {
                CustomToast.makeText(this,"Vui lòng nhập địa chỉ mail",CustomToast.LENGTH_SHORT,CustomToast.WARNING).show();
            }
            else {
                Properties properties = new Properties();
                properties.put("mail.smtp.auth","true");
                properties.put("mail.smtp.starttls.enable","true");
                properties.put("mail.smtp.host","smtp.gmail.com");
                properties.put("mail.smtp.port","587");

                Session session = Session.getInstance(properties,
                        new Authenticator() {
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username,password);
                            }
                        });
                try {
                    Message message = new MimeMessage(session);
                    message.setFrom(new InternetAddress(username));
                    message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(toMail));
                    message.setSubject("Gửi chi tiết đơn đặt hàng qua gmail");

                    Multipart emailContent = new MimeMultipart();

                    MimeBodyPart textBodyPart = new MimeBodyPart();
                    textBodyPart.setText(" Mã đơn hàng: " + mDonHangDto.getMaDH()
                            + "\n Tên khách hàng: " + mDonHangDto.getTenKH()
                            + "\n Ngày đặt: " + mDonHangDto.getNgayDH()
                            + "\n Tổng giá trị đơn hàng: " + ttddh_adapter.TongTien() + " USD");

                    MimeBodyPart pdfAttachment = new MimeBodyPart();
                    pdfAttachment.attachFile(mPDFPath);

                    emailContent.addBodyPart(textBodyPart);
                    emailContent.addBodyPart(pdfAttachment);

                    message.setContent(emailContent);

                    //
                    Transport.send(message);
                    CustomToast.makeText(this,"Gửi mail thành công!",CustomToast.LENGTH_SHORT,CustomToast.SUCCESS).show();
                }
                catch (MessagingException | IOException e) {
                    CustomToast.makeText(this,"Gửi mail thất bại!",CustomToast.LENGTH_SHORT,CustomToast.ERROR).show();
                    throw new RuntimeException();
                }
            }
            dialog.cancel();
        });
        StrictMode.ThreadPolicy threadPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(threadPolicy);
    }

    private Dialog getDialogSendMail() {
        Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);

        dialog.setContentView(R.layout.dialog_ttddh_sendmail);
        etMail_dialogSendMail = dialog.findViewById(R.id.etMail_dialogTTDDH_sendMail);
        btnHuy_dialogSendMail = dialog.findViewById(R.id.btnHuy_dialogTTDDH_sendMail);
        btnGui_dialogSendMail = dialog.findViewById(R.id.btnGui_dialogTTDDH_sendMail);

        return dialog;
    }

    private void xacNhanThoatTTDDH_Activity() {
        CustomAlertDialog alertDialog = new CustomAlertDialog(this);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setLayout((7*width)/8, WindowManager.LayoutParams.WRAP_CONTENT);
        alertDialog.show();

        alertDialog.setMessage("Đơn hàng rỗng! Nếu thoát đơn hàng sẽ bị xóa?");
        alertDialog.setBtnPositive("Xóa và Thoát");
        alertDialog.setBtnNegative("Ở lại");

        alertDialog.btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DonHangDao donHangDao = new DonHangDao(TTDDH_Activity.this);
                donHangDao.xoaDonHang(mDonHangDto.getMaDH());
                alertDialog.cancel();
                CustomToast.makeText(TTDDH_Activity.this,"Đơn hàng đã bị xóa do không có sản phẩm!",CustomToast.LENGTH_LONG,CustomToast.WARNING).show();
                finish();
            }
        });

        alertDialog.btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
    }
}




// ??