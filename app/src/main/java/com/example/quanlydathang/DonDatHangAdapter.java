package com.example.quanlydathang;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydathang.dto.DonHangDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DonDatHangAdapter extends RecyclerView.Adapter<DonDatHangAdapter.DDHViewHolder> implements Filterable {

    private List<DonHangDto> donHangDtoList;
    private List<DonHangDto> donHangDtoListOld;
    private OnItemClickListener mlistener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onDeleteClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.mlistener=listener;
    }
    public DonDatHangAdapter(List<DonHangDto> donHangDtoList) {
        this.donHangDtoList = donHangDtoList;
        this.donHangDtoListOld = donHangDtoList;
    }

    @NonNull
    @Override
    public DDHViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dondathang,parent,false);
        return new DDHViewHolder(view,mlistener );
    }

    @Override
    public void onBindViewHolder(@NonNull DDHViewHolder holder, int position) {
        DonHangDto donHang=donHangDtoList.get(position);
        if (donHang==null){
            return;
        }
        holder.tvMaDH.setText(String.valueOf(donHang.getMaDH()));
        holder.tvKH.setText(donHang.getTenKH());
        holder.tvNgayDH.setText(donHang.getNgayDH());
    }

    @Override
    public int getItemCount() {
        if (donHangDtoList !=null){
            return donHangDtoList.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String strSearch = charSequence.toString();
                if (strSearch.isEmpty()){
                    donHangDtoList=donHangDtoListOld;
                }else {
                    List<DonHangDto> list=new ArrayList<>();
                    for (DonHangDto donHangDto : donHangDtoListOld)
                         {
                             if (donHangDto.getTenKH().toLowerCase().contains(
                                     strSearch.toLowerCase())){
                                 list.add(donHangDto);
                             }

                    }
                    donHangDtoList=list;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=donHangDtoList;
                return filterResults;

            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                donHangDtoList= (List<DonHangDto>) filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

    public class DDHViewHolder extends RecyclerView.ViewHolder{

        private TextView tvMaDH;
        private TextView tvKH;
        private TextView tvNgayDH;
        private ImageButton ibDelete;

        public DDHViewHolder(@NonNull View itemView,final OnItemClickListener listener) {
            super(itemView);
            tvMaDH=itemView.findViewById(R.id.tvMaDH);
            tvKH=itemView.findViewById(R.id.tvtenKH);
            tvNgayDH=itemView.findViewById(R.id.tvNgayDH);
            ibDelete=itemView.findViewById(R.id.ib_delete);
            itemView.setOnClickListener((view -> {
                if (listener!=null){
                    int position =getAbsoluteAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        listener.onItemClick(position);
                    }
                }
            }));
            ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener!=null){
                        int position=getAbsoluteAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }


    }
}
