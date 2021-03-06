package com.example.quanlydathang.activitydonhang;

import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydathang.activity.TTDDH_Activity;
import com.example.quanlydathang.adapter.DonDatHangAdapter;
import com.example.quanlydathang.R;
import com.example.quanlydathang.dao.DonHangDao;
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
    public static int width;

    int kh=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_dat_hang);
        donHangDao = new DonHangDao(this);
        DisplayMetrics metrics=getResources().getDisplayMetrics();
         width=metrics.widthPixels;
        FloatingActionButton floatButton = findViewById(R.id.fab);
        floatButton.setOnClickListener(view -> {
            Dialog dialog = donDatHangAdapter.getDialogDDH(this);
            donDatHangAdapter.btnHuyDialog.setOnClickListener(view13 -> dialog.cancel());
            donDatHangAdapter.edNgayDHDialog.setOnFocusChangeListener((view12, b) -> donDatHangAdapter.showDateTimeDialog(donDatHangAdapter.edNgayDHDialog));
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout((6*width)/7, WindowManager.LayoutParams.WRAP_CONTENT);


            donDatHangAdapter.btnThemDialog.setOnClickListener(view1 -> {
                if (donDatHangAdapter.edNgayDHDialog.getText().toString().isEmpty()){
                    CustomToast.makeText(DonDatHangActivity.this, "Kh??ng ???????c b??? tr???ng ng??y gi???!",
                            CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
                    return;
                }
                DonHangDto donHang=new DonHangDto(donDatHangAdapter.edNgayDHDialog.getText().toString(),DonDatHangAdapter.kh,null);
                 int id= (int)themDonHang(donHang);

                dialog.cancel();
                if (id!=0) {
                    CustomToast.makeText(DonDatHangActivity.this, "Th??m ????n h??ng th??nh c??ng!",
                            CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
                    //chuy???n sang  TTDDH_Activity ????? th??m s???n ph???m - linh
                    donHang.setMaDH(id);
                    startTTDDH(donHang);
                }
                onResume();
            });
        });
        rcvDDH = findViewById(R.id.rcv_DDH);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvDDH.setLayoutManager(linearLayoutManager);
            capNhatDulieuDH();
   }
    private void capNhatDulieuDH() {
        donHangDtoList = donHangDao.danhSachDonHang("DESC");
        donDatHangAdapter = new DonDatHangAdapter(this,donHangDtoList);
        rcvDDH.setAdapter(donDatHangAdapter);
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
        capNhatDulieuDH();
        super.onResume();

    }

    private void startTTDDH(DonHangDto donHangDto) {
        //
        donHangDto.setTenKH(donDatHangAdapter.timTenKH(donHangDto.getMaKH()));
        Intent intent = new Intent(this, TTDDH_Activity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("maDH",donHangDto.getMaDH());
        bundle.putString("ngayDH",donHangDto.getNgayDH());
        bundle.putInt("maKH",donHangDto.getMaKH());
        bundle.putString("tenKH",donHangDto.getTenKH());
        intent.putExtras(bundle);
        this.startActivity(intent);
    }
}