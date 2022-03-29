package com.example.quanlydathang;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydathang.dto.DonHangDto;

import java.util.ArrayList;
import java.util.List;

public class DonDatHangActivity extends AppCompatActivity {

    private RecyclerView rcvDDH;
    private DonDatHangAdapter donDatHangAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_dat_hang);


        rcvDDH=findViewById(R.id.rcv_DDH);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        rcvDDH.setLayoutManager(linearLayoutManager);

        donDatHangAdapter=new DonDatHangAdapter(getListDH());
        rcvDDH.setAdapter(donDatHangAdapter);

        RecyclerView.ItemDecoration itemDecoration=new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);

    }

    private List<DonHangDto> getListDH() {
        List<DonHangDto> list=new ArrayList<>();
        list.add(new DonHangDto(1,"31/12/2000 12:31",1,"Nguyen Van Duc"));
        list.add(new DonHangDto(2,"31/12/2000 12:31",1,"Nguyen Van Hai"));
        list.add(new DonHangDto(3,"31/12/2000 12:31",1,"Nguyen Van Nga"));
        list.add(new DonHangDto(4,"31/12/2000 12:31",1,"Nguyen Van Duc"));
        list.add(new DonHangDto(5,"31/12/2000 12:31",1,"Nguyen Van Duc"));
        list.add(new DonHangDto(6,"31/12/2000 12:31",1,"Nguyen Van Duc"));
        list.add(new DonHangDto(7,"31/12/2000 12:31",1,"Nguyen Van Duc"));
        list.add(new DonHangDto(8,"31/12/2000 12:31",1,"Nguyen Van Duc"));
        list.add(new DonHangDto(9,"31/12/2000 12:31",1,"Nguyen Van Duc"));
        list.add(new DonHangDto(10,"31/12/2000 12:31",1,"Nguyen Van Duc"));
        list.add(new DonHangDto(11,"31/12/2000 12:31",1,"Nguyen Van Duc"));
        list.add(new DonHangDto(12,"31/12/2000 12:31",1,"Nguyen Van Duc"));
        list.add(new DonHangDto(13,"31/12/2000 12:31",1,"Nguyen Van Duc"));
        list.add(new DonHangDto(14,"31/12/2000 12:31",1,"Nguyen Van Duc"));
        return list;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        SearchManager searchManager=(SearchManager)getSystemService(Context.SEARCH_SERVICE);
        searchView= (SearchView) menu.findItem(R.id.action_search).getActionView();
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
        if (!searchView.isIconified()){
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}