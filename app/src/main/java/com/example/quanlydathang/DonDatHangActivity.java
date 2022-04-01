package com.example.quanlydathang;

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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydathang.dao.DonHangDao;
import com.example.quanlydathang.dto.DonHangDto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class DonDatHangActivity extends AppCompatActivity {

    private DonHangDao donHangDao;
    private List<DonHangDto> donHangDtoList;
    private RecyclerView rcvDDH;
    private DonDatHangAdapter donDatHangAdapter;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_dat_hang);

        FloatingActionButton floatButton = findViewById(R.id.fab);
        floatButton.setOnClickListener(view -> {
            Dialog dialog = new Dialog(DonDatHangActivity.this);
            dialog.setTitle("Hộp thoại xử lý");
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.themdonhang_dialog);
            EditText edNgayDH = dialog.findViewById(R.id.etNgayDH);
            edNgayDH.setInputType(InputType.TYPE_NULL);
            Spinner spKH = dialog.findViewById(R.id.spKH);
            Button btnThem = dialog.findViewById(R.id.btnthem);
            Button btnHuy = dialog.findViewById(R.id.btnhuy);
            btnHuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                }
            });
            edNgayDH.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDateTimeDialog(edNgayDH);
                }
            });
            dialog.show();

            btnThem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DonHangDto donHang=new DonHangDto(edNgayDH.getText().toString(),1,"John");
                     int id= (int)themDonHang(donHang);
                    dialog.cancel();
                    donHangDtoList=donHangDao.danhSachDonHang();
                    donDatHangAdapter = new DonDatHangAdapter(donHangDtoList);
                    rcvDDH.setAdapter(donDatHangAdapter);
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
                updateOrShowDetailItem(position);
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });
    }

    private void updateOrShowDetailItem(int position) {
        String[] items={"Sửa","Xem chi tiết"};
        AlertDialog.Builder b=new AlertDialog.Builder(this);
        b.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i==0) {
                    Toast.makeText(DonDatHangActivity.this
                            , "bạn chọn sửa", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(DonDatHangActivity.this
                            , "bạn chọn xem chi tiết", Toast.LENGTH_SHORT).show();
                }
            }
        });
        b.show();
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

                           donHangDtoList=donHangDao.danhSachDonHang();
                           donDatHangAdapter = new DonDatHangAdapter(donHangDtoList);
                           rcvDDH.setAdapter(donDatHangAdapter);

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
}