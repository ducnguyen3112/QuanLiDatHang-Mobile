package com.example.quanlydathang.activitydonhang;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydathang.adapter.DonDatHangAdapter;
import com.example.quanlydathang.R;
import com.example.quanlydathang.dao.DonHangDao;
import com.example.quanlydathang.dao.KhachHangDao;
import com.example.quanlydathang.dto.DonHangDto;
import com.example.quanlydathang.dto.KhachHangDto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DonDatHangActivity extends AppCompatActivity {

    private DonHangDao donHangDao;
    private List<DonHangDto> donHangDtoList;
    private RecyclerView rcvDDH;
    private DonDatHangAdapter donDatHangAdapter;
    private SearchView searchView;
    private  KhachHangDao   khachHangDao;

    private EditText edNgayDHDialog ;
    private TextView tvMaDHDialog ;
    private Spinner spKHDialog ;
    private Button btnThemDialog ;
    private Button btnHuyDialog ;
    String kh = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_dat_hang);
        khachHangDao=new KhachHangDao(this);
        FloatingActionButton floatButton = findViewById(R.id.fab);
        floatButton.setOnClickListener(view -> {
            Dialog dialog = getDialogDDH();
            List<String> listKH=dsMaVaHoTenKH();
            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this
                    , androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
                    , listKH);
            xuKienSpinner();
            spKHDialog.setAdapter(arrayAdapter);
            btnHuyDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            edNgayDHDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDateTimeDialog(edNgayDHDialog);
                }
            });
            dialog .show();


            btnThemDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (edNgayDHDialog.getText().toString().isEmpty()){
                        Toast.makeText(DonDatHangActivity.this
                                , "Không được bỏ trống ngày giờ!", Toast.LENGTH_SHORT).show();
                    }
                    DonHangDto donHang=new DonHangDto(edNgayDHDialog.getText().toString(),getIdKHFromSpiner(kh),null);
                     int id= (int)themDonHang(donHang);
                    dialog.cancel();
                    capNhatDulieuDH();
                    Toast.makeText(DonDatHangActivity.this
                            , "Thêm đơn hàng thành công!", Toast.LENGTH_SHORT).show();

                }
            });
        });
        rcvDDH = findViewById(R.id.rcv_DDH);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvDDH.setLayoutManager(linearLayoutManager);
        donHangDao = new DonHangDao(this);
        donHangDtoList = donHangDao.danhSachDonHang();
        if (donHangDtoList.isEmpty()) {
            Toast.makeText(DonDatHangActivity.this
                    , "Danh sách rỗng!", Toast.LENGTH_SHORT).show();
        } else {
            donDatHangAdapter = new DonDatHangAdapter(donHangDao.danhSachDonHang());
            rcvDDH.setAdapter(donDatHangAdapter);
        }
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        donDatHangAdapter.setOnItemClickListener(new DonDatHangAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String[] items={"Sửa","Xem chi tiết"};
                AlertDialog.Builder b=new AlertDialog.Builder(DonDatHangActivity.this);
                b.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i==0) {
                            Dialog dialog = getDialogDDH();
                            tvMaDHDialog.setVisibility(View.VISIBLE);
                            int maDhDialog=donHangDtoList.get(position).getMaDH();
                            tvMaDHDialog.setText(maDhDialog+"");
                            btnThemDialog.setText("Cập nhập");
                            List<String> listKH=dsMaVaHoTenKH();
                            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(DonDatHangActivity.this
                                    , androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
                                    , listKH);
                            xuKienSpinner();
                            spKHDialog.setAdapter(arrayAdapter);
                            btnHuyDialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.cancel();
                                }
                            });
                            edNgayDHDialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    showDateTimeDialog(edNgayDHDialog);
                                }
                            });
                            btnThemDialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (edNgayDHDialog.getText().toString().isEmpty()){
                                        Toast.makeText(DonDatHangActivity.this
                                                , "Không được bỏ trống ngày giờ!", Toast.LENGTH_SHORT).show();
                                    }
                                    DonHangDto donHang=new DonHangDto();
                                    donHang.setMaDH(Integer.valueOf(tvMaDHDialog.getText().toString()));
                                    donHang.setNgayDH(edNgayDHDialog.getText().toString());
                                    donHang.setMaKH(getIdKHFromSpiner(kh));
                                    donHangDao.suaDonHang(donHang);
                                    dialog.cancel();
                                    capNhatDulieuDH();
                                    Toast.makeText(DonDatHangActivity.this
                                            , "Sửa đơn đặt hàng thành công!", Toast.LENGTH_SHORT).show();

                                }
                            });

                            dialog.show();
                        }else {
                            Toast.makeText(DonDatHangActivity.this
                                    , "bạn chọn xem chi tiết", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                b.show();
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });
    }

    private void capNhatDulieuDH() {
        donHangDtoList = donHangDao.danhSachDonHang();
        donDatHangAdapter = new DonDatHangAdapter(donHangDtoList);
        rcvDDH.setAdapter(donDatHangAdapter);
    }

    private void xuKienSpinner() {
        spKHDialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                kh=spKHDialog.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                kh="";
            }
        });
    }

    @NonNull
    private Dialog getDialogDDH() {
        Dialog dialog = new Dialog(DonDatHangActivity.this);
        dialog.setTitle("Hộp thoại xử lý");
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.themdonhang_dialog);
        tvMaDHDialog=dialog.findViewById(R.id.tvIdDonHangDialog);
        edNgayDHDialog = dialog.findViewById(R.id.etNgayDH);
        spKHDialog = dialog.findViewById(R.id.spKH);
        btnThemDialog = dialog.findViewById(R.id.btnthem);
        btnHuyDialog = dialog.findViewById(R.id.btnhuy);
        edNgayDHDialog.setInputType(InputType.TYPE_NULL);
        return dialog;
    }


    private void removeItem(int position) {

       long id= donHangDtoList.get(position).getMaDH();
       AlertDialog.Builder builder=new AlertDialog.Builder(this);
       builder.setMessage("Bán muốn xóa đơn hàng: "+id+"?")
               .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       if (donHangDao.xoaDonHang((int)id)){
                           Toast.makeText(DonDatHangActivity.this
                                   , "Xóa đơn hàng thành công!", Toast.LENGTH_SHORT).show();

                           capNhatDulieuDH();

                       }else {
                           Toast.makeText(DonDatHangActivity.this
                                   , "Lỗi!không thể xóa đơn hàng.", Toast.LENGTH_SHORT).show();
                       }
                   }
               })
               .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {

                   }
               });
       builder.create().show();

    }

    private long themDonHang(DonHangDto donHangDto) {
       return donHangDao.themDonHang(donHangDto);
    }

    private void showDateTimeDialog(EditText edNgayDH) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener
                = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(Calendar.YEAR, i);
                calendar.set(Calendar.MONTH, i1);
                calendar.set(Calendar.DAY_OF_MONTH, i2);

                TimePickerDialog.OnTimeSetListener timeSetListener
                        = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i3, int i4) {
                        calendar.set(Calendar.HOUR_OF_DAY, i3);
                        calendar.set(Calendar.MINUTE, i4);

                        SimpleDateFormat simpleDateFormat
                                = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                        edNgayDH.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };
                  new TimePickerDialog(DonDatHangActivity.this,
                          timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),
                          calendar.get(Calendar.MINUTE),true).show();
            }
        };
        new DatePickerDialog(DonDatHangActivity.this,
                dateSetListener, calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

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
                donDatHangAdapter.getFilter().filter(query);
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                donDatHangAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
    public List<String> dsMaVaHoTenKH(){
        List<String> ds=new ArrayList<>();
        List<KhachHangDto> dsKH= khachHangDao.getListKH();
        for (KhachHangDto item :
                dsKH) {
            ds.add(item.getId()+" - "+item.getName());
        }
        return ds;
    }
    public int getIdKHFromSpiner(String str){
        String[] words=str.split("-");
        if (str!=""){
            return Integer.valueOf(words[0].trim());
        }
        return 0;
    }
}