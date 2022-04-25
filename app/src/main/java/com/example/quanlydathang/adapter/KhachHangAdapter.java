package com.example.quanlydathang.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlydathang.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydathang.activity.KhachHang.KhachHangActivity;
import com.example.quanlydathang.activitydonhang.DonDatHangActivity;
import com.example.quanlydathang.dao.KhachHangDao;
import com.example.quanlydathang.dto.KhachHangDto;
import com.example.quanlydathang.activity.KhachHang.UpdateKhachHangActivity;
import com.example.quanlydathang.utils.CustomAlertDialog;
import com.example.quanlydathang.utils.CustomToast;

import java.util.ArrayList;
import java.util.List;

public class KhachHangAdapter extends RecyclerView.Adapter<KhachHangAdapter.KhachHangViewHolder> implements Filterable {

    private Context context;
    private List<KhachHangDto> list;
    private List<KhachHangDto> listOld;

    public KhachHangAdapter(Context context, List<KhachHangDto> list) {
        this.context = context;
        this.list = list;
        this.listOld = list;
    }



    @NonNull
    @Override
    public KhachHangAdapter.KhachHangViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_khach_hang, parent, false);
        return new KhachHangViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KhachHangAdapter.KhachHangViewHolder holder, int position) {
        KhachHangDto dto = this.list.get(position);
        if (dto == null) {
            return;
        }

        holder.tvId.setText(String.valueOf(dto.getId()));
        holder.tvName.setText(dto.getName());
        holder.tvPhone.setText(dto.getPhone());
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateKhachHangActivity.class);
                intent.putExtra("idKH", String.valueOf(dto.getId()));
                context.startActivity(intent);
            }
        });

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog(dto.getId());
            }
        });
    }

    void confirmDialog(int id){
        CustomAlertDialog alertDialog= new CustomAlertDialog(context);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().setLayout((7* KhachHangActivity.width)/8, WindowManager.LayoutParams.WRAP_CONTENT);
        alertDialog.show();
        alertDialog.btnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KhachHangDao khachHangDao = new KhachHangDao(context);
                long result = khachHangDao.deleteKH(id);
                if(result == -1){
                    CustomToast.makeText(context, "Xoá thất bại",
                            CustomToast.LENGTH_SHORT, CustomToast.ERROR).show();
                }else{
                    CustomToast.makeText(context, "Xoá thành công",
                            CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
                }
                list = khachHangDao.getListKH();
                notifyDataSetChanged();
                alertDialog.cancel();

            }
        });
        alertDialog.btnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });
        alertDialog.setMessage("Bạn muốn xóa khách hàng: "+id+" ?");
        alertDialog.setBtnPositive("Xóa");
        alertDialog.setBtnNegative("Hủy");
    }

    @Override
    public int getItemCount() {
        if (this.list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String str =  charSequence.toString();
                if(str.isEmpty()) {
                    list = listOld;
                }else {
                    List<KhachHangDto> listSearch = new ArrayList<>();
                    for(KhachHangDto item: listOld) {
                        if(item.getName().toLowerCase().contains(str.toLowerCase())) {
                            listSearch.add(item);
                        }
                    }

                    list = listSearch;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = list;

                return  filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (List<KhachHangDto>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class KhachHangViewHolder extends RecyclerView.ViewHolder {
        private TextView tvId, tvName, tvPhone;
        private ImageView ivEdit, ivDelete;

        public KhachHangViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }
}
