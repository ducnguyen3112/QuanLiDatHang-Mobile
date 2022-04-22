package com.example.quanlydathang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydathang.R;
import com.example.quanlydathang.dto.PDFDonHang;

import java.util.List;

public class PdfAdapter extends RecyclerView.Adapter<PdfAdapter.viewHolder> {

    Context context;
    List<PDFDonHang> list;

    public PdfAdapter() {
    }

    public PdfAdapter(Context context, List<PDFDonHang> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pdf_donhang, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        PDFDonHang product = list.get(position);
        holder.id.setText(product.getProduct().getMaSP()+"");
        holder.name.setText(product.getProduct().getTenSP());
        holder.price.setText(product.getProduct().getDonGia()+"");
        holder.quantity.setText(product.getChiTietDonHang().getSoLuong()+"");
        holder.total.setText(""+product.getChiTietDonHang().getSoLuong()*product.getProduct().getDonGia());
    }

    @Override
    public int getItemCount() {
        if (this.list != null) {
            return list.size();
        }
        return 0;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView id, name, price, quantity, total;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);
            total = itemView.findViewById(R.id.total);
        }
    }
}
