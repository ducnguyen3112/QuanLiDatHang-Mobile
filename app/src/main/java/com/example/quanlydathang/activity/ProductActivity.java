package com.example.quanlydathang.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.appcompat.widget.SearchView;
import com.example.quanlydathang.MainActivity;
import com.example.quanlydathang.R;
import com.example.quanlydathang.adapter.ProductAdapterRecyclerView;
import com.example.quanlydathang.dao.ProductDao;
import com.example.quanlydathang.dto.Product;
import com.example.quanlydathang.utils.Constants;
import com.example.quanlydathang.utils.CustomToast;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {
    private LinearLayout titleLinearLayout;
    private Toolbar toolbar;
    private Button buttonAdd;
    private NestedScrollView nestedScrollView;
    private ProgressBar progressBar;
    private ImageButton buttonDelete;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private Menu menu;

    private final ArrayList<Product> list = new ArrayList<>();
    private ProductDao dao ;
    private ProductAdapterRecyclerView productAdapterRecyclerView;

    private GridLayoutManager gm;
    private LinearLayoutManager lm;

    private int currentTypeDisplay = Constants.LIST;
    private int currentPosition;
    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_san_pham);
        setControl();
        loadMoreData();
        handleClickAdd();
        setActionBar();
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    private void loadMoreData() {
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                int totalPage  = totalPage();
                if (page <= totalPage && scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    ++page;
                    progressBar.setVisibility(View.VISIBLE);
                    dao.countLoadDBTypeDisplay(list, page, Constants.numberItem_layout, currentTypeDisplay);
                    productAdapterRecyclerView.notifyDataSetChanged();
                }
                if (page >= totalPage && scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()){
                    progressBar.setVisibility(View.GONE);
                    CustomToast.makeText(ProductActivity.this, "Đã hiển thị hết sản phẩm!!",
                            CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();

                }
            }
        });
    }

    private int totalPage() {
        int totalPage = 0;
        int totalItem = dao.totalItem();
        int count = totalItem % Constants.numberItem_layout;
        if(count > 0){
            totalPage = (totalItem / Constants.numberItem_layout) + 1;
        }
        return totalPage;
    }

    private void setCurrentPosition() {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        switch (currentTypeDisplay) {
            case Constants.GRID:
                currentPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
                break;
            case Constants.LIST:
                currentPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                break;
        }
    }

    private void setActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main);
            }
        });
    }

    private void setControl() {
        toolbar = findViewById(R.id.toolbar);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonDelete = findViewById(R.id.buttonDelete);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        progressBar = findViewById(R.id.progressBar);
        titleLinearLayout = findViewById(R.id.titleLinearLayout);
        recyclerView = findViewById(R.id.recyclerView);

        gm = new GridLayoutManager(this, 2);
        lm = new LinearLayoutManager(this);

        productAdapterRecyclerView = new ProductAdapterRecyclerView(ProductActivity.this, list);
        recyclerView.setAdapter(productAdapterRecyclerView);

        dao = new ProductDao(getApplicationContext());

        currentTypeDisplay = Constants.LIST;
        titleLinearLayout.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(lm);

        setTypeDisplayRecyclerView(Constants.LIST);

        dao.countLoadDBTypeDisplay(list, page, Constants.numberItem_layout, currentTypeDisplay);
        productAdapterRecyclerView.notifyDataSetChanged();

        if(dao.totalItem() <= Constants.numberItem_layout){
            progressBar.setVisibility(View.GONE);
            CustomToast.makeText(ProductActivity.this, "Đã hiển thị hết sản phẩm!!",
                    CustomToast.LENGTH_SHORT, CustomToast.WARNING).show();
        }else{
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void setTypeDisplayRecyclerView(int typeDisplay) {
        if (list == null || list.isEmpty()) {
            return;
        }
        currentTypeDisplay = typeDisplay;
        for (Product product : list) {
            product.setTypeDisplay(typeDisplay);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_product, menu);
        this.menu = menu;
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.searchviewProduct).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                productAdapterRecyclerView.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productAdapterRecyclerView.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_view) {
            setCurrentPosition();
            onClickChangTypeDisplay();
        }
        return true;
    }

    private void onClickChangTypeDisplay() {
        if (currentTypeDisplay == Constants.GRID) {
            titleLinearLayout.setVisibility(View.VISIBLE);
            setTypeDisplayRecyclerView(Constants.LIST);
            recyclerView.setLayoutManager(lm);
        } else if (currentTypeDisplay == Constants.LIST) {
            titleLinearLayout.setVisibility(View.INVISIBLE);
            setTypeDisplayRecyclerView(Constants.GRID);
            recyclerView.setLayoutManager(gm);
        }
        productAdapterRecyclerView.notifyDataSetChanged();
        setIconMenu();
        recyclerView.scrollToPosition(currentPosition);
    }

    private void setIconMenu() {
        switch (currentTypeDisplay) {
            case Constants.GRID:
                menu.getItem(1).setIcon(R.drawable.ic_baseline_list_24);
                break;
            case Constants.LIST:
                menu.getItem(1).setIcon(R.drawable.ic_baseline_grid_on_24);
                break;
        }
    }

    public void handleClickAdd() {
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductActivity.this, AddProductActivity.class);
                intent.putExtra("MASP", 0);
                /*startActivityForResult(intent, Constants.RESULT_PRODUCT_ACTIVITY);*/
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.RESULT_PRODUCT_ACTIVITY:
                list.clear();
                dao.countLoadDBTypeDisplay(list, page, Constants.numberItem_layout, currentTypeDisplay);
                productAdapterRecyclerView.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }
}