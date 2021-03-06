package com.example.quanlydathang.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydathang.R;
import com.example.quanlydathang.activity.AddProductActivity;
import com.example.quanlydathang.activitydonhang.DonDatHangActivity;
import com.example.quanlydathang.dao.ProductDao;
import com.example.quanlydathang.dto.Product;
import com.example.quanlydathang.utils.Constants;
import com.example.quanlydathang.utils.CustomAlertDialog;
import com.example.quanlydathang.utils.CustomToast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ProductAdapterRecyclerView
        extends RecyclerView.Adapter<ProductAdapterRecyclerView.MyViewHolder>
        implements Filterable {

    Context context;
    ArrayList<Product> list;
    ArrayList<Product> listOld;

    public ProductAdapterRecyclerView(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
        this.listOld = list;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tenSP, maSP, xuatXu, donGia;
        ImageButton delete;
        ImageView imageView;

        public MyViewHolder(@NonNull View view) {
            super(view);
            tenSP = view.findViewById(R.id.textViewTenSP);
            maSP = view.findViewById(R.id.textViewMaSP);
            xuatXu = view.findViewById(R.id.textViewXuatXu);
            donGia = view.findViewById(R.id.textViewDonGia);
            delete = view.findViewById(R.id.buttonDelete);
            imageView = view.findViewById(R.id.imageView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        Product product = list.get(position);
        return product.getTypeDisplay();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_list, parent, false);

        switch (viewType) {
            case Constants.GRID:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_grid, parent, false);
                break;
            case Constants.LIST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_list, parent, false);
                break;
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product product = list.get(position);
        setInfo(product, holder);
        handleClickDelete(product, holder);
        handleClickUpdate(product, holder.itemView);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    private void setInfo(Product product, MyViewHolder myViewHolder) {
        myViewHolder.maSP.setText("" + product.getMaSP());
        myViewHolder.tenSP.setText("" + product.getTenSP());
        myViewHolder.xuatXu.setText("" + product.getXuatXu());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,##0");
        myViewHolder.donGia.setText("" + decimalFormat.format(product.getDonGia()) + " ??");
        if (product.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(product.getImage(), 0, product.getImage().length);
            myViewHolder.imageView.setImageBitmap(bitmap);
        }
    }

    public void handleClickDelete(Product product, MyViewHolder myViewHolder) {
        myViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(product);
            }
        });
    }

    public void handleClickUpdate(Product product, View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setBackgroundColor(Color.rgb(141, 216, 158));
                Intent intent = new Intent(((Activity) context), AddProductActivity.class);
                intent.putExtra("MASP", product.getMaSP());
                ((Activity) context).startActivity(intent);
            }
        });
    }

    public void showDialog(Product product) {
        CustomAlertDialog alertDialog = new CustomAlertDialog(context);

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setLayout((7 * DonDatHangActivity.width) / 8, WindowManager.LayoutParams.WRAP_CONTENT);
        alertDialog.show();
        alertDialog.btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductDao db = new ProductDao(context.getApplicationContext());
                db.deleteProduct(product.getMaSP());
                db.countLoadDBAllTypeDisplay(list, product.getTypeDisplay());
                notifyDataSetChanged();

                CustomToast.makeText(context, "X??a s???n ph???m th??nh c??ng!",
                        CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                alertDialog.cancel();
            }
        });
        alertDialog.btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        alertDialog.setMessage("X??a s???n ph???m ?");
        alertDialog.setBtnPositive("X??a");
        alertDialog.setBtnNegative("H???y");
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSearch = charSequence.toString();
                if (strSearch.isEmpty()) {
                    list = listOld;
                } else {
                    ArrayList<Product> l = new ArrayList<>();
                    for (Product product : listOld) {
                        if (product.getTenSP().toLowerCase().contains(strSearch.toLowerCase())) {
                            l.add(product);
                        }
                    }
                    list = l;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (ArrayList<Product>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
