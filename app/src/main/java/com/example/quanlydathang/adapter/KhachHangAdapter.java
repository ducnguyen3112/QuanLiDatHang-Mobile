package com.example.quanlydathang.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlydathang.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydathang.dao.KhachHangDao;
import com.example.quanlydathang.dto.KhachHangDto;
import com.example.quanlydathang.ui.KhachHang.UpdateKhachHangActivity;

import java.util.List;

public class KhachHangAdapter extends RecyclerView.Adapter<KhachHangAdapter.KhachHangViewHolder> {

    private Context context;
    private List<KhachHangDto> list;

    public KhachHangAdapter(Context context, List<KhachHangDto> list) {
        this.context = context;
        this.list = list;
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
                intent.putExtra("KH", dto);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Thông báo");
        builder.setMessage("Bạn có chắc chắn muốn xóa khách hàng này ?");
        builder.setPositiveButton("Xác Nhận", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                KhachHangDao khachHangDao = new KhachHangDao(context);
                long result = khachHangDao.deleteKH(id);
                if(result == -1){
                    Toast.makeText(context, "Failed to Delete.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "Successfully Deleted.", Toast.LENGTH_SHORT).show();
                }
                list = khachHangDao.getListKH();
                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    @Override
    public int getItemCount() {
        if (this.list != null) {
            return list.size();
        }
        return 0;
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
