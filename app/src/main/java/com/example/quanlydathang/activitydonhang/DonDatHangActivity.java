package com.example.quanlydathang.activitydonhang;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydathang.adapter.DonDatHangAdapter;
import com.example.quanlydathang.R;
import com.example.quanlydathang.dao.DonHangDao;
import com.example.quanlydathang.dao.KhachHangDao;
import com.example.quanlydathang.dto.DonHangDto;
import com.example.quanlydathang.utils.CustomToast;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class DonDatHangActivity extends AppCompatActivity {

    private DonHangDao donHangDao;
    private List<DonHangDto> donHangDtoList;
    private RecyclerView rcvDDH;
    private DonDatHangAdapter donDatHangAdapter;
    private SearchView searchView;
    private  KhachHangDao   khachHangDao;

    String kh = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_dat_hang);
        khachHangDao=new KhachHangDao(this);
        FloatingActionButton floatButton = findViewById(R.id.fab);
        floatButton.setOnClickListener(view -> {
            Dialog dialog = donDatHangAdapter.getDialogDDH(this);
            List<String> listKH=donDatHangAdapter.dsMaVaHoTenKH();
            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this
                    , androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
                    , listKH);
            donDatHangAdapter.spKHDialog.setAdapter(arrayAdapter);
            xuKienSpinner();

            donDatHangAdapter.btnHuyDialog.setOnClickListener(view13 -> dialog.cancel());
            donDatHangAdapter.edNgayDHDialog.setOnFocusChangeListener((view12, b) -> donDatHangAdapter.showDateTimeDialog(donDatHangAdapter.edNgayDHDialog));
            dialog .show();


            donDatHangAdapter.btnThemDialog.setOnClickListener(view1 -> {
                if (donDatHangAdapter.edNgayDHDialog.getText().toString().isEmpty()){
                    CustomToast.makeText(DonDatHangActivity.this, "Không được bỏ trống ngày giờ!",
                            CustomToast.LENGTH_LONG, CustomToast.WARNING).show();


                }
                DonHangDto donHang=new DonHangDto(donDatHangAdapter.edNgayDHDialog.getText().toString(),donDatHangAdapter.getIdKHFromSpiner(kh),null);
                Log.e("time", donDatHangAdapter.edNgayDHDialog.getText().toString(),null );
                Log.e("hk", donDatHangAdapter.getIdKHFromSpiner(kh)+"",null );

                 int id= (int)themDonHang(donHang);
                dialog.cancel();
                if (id!=0) {

                    CustomToast.makeText(DonDatHangActivity.this, "Thêm đơn hàng thành công!",
                            CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();

                }
                onResume();

            });
        });
        rcvDDH = findViewById(R.id.rcv_DDH);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvDDH.setLayoutManager(linearLayoutManager);
        donHangDao = new DonHangDao(this);
        donHangDtoList = donHangDao.danhSachDonHang("DESC");
        if (donHangDtoList.isEmpty()) {
            Toast.makeText(DonDatHangActivity.this
                    , "Danh sách rỗng!", Toast.LENGTH_SHORT).show();
        } else {
            donDatHangAdapter = new DonDatHangAdapter(this,donHangDao.danhSachDonHang("DESC"));
            rcvDDH.setAdapter(donDatHangAdapter);
        }

   }

    private void capNhatDulieuDH() {
        donHangDtoList = donHangDao.danhSachDonHang("DESC");
        donDatHangAdapter = new DonDatHangAdapter(this,donHangDtoList);
        rcvDDH.setAdapter(donDatHangAdapter);
    }

    private void xuKienSpinner() {
        donDatHangAdapter.spKHDialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                kh=donDatHangAdapter.spKHDialog.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                kh="";
            }
        });
    }





    private long themDonHang(DonHangDto donHangDto) {
       return donHangDao.themDonHang(donHangDto);
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

    @Override
    public void onResume() {
        super.onResume();
        capNhatDulieuDH();
    }
}