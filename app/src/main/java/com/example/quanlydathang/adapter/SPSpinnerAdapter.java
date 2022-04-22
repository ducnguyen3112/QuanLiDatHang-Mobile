package com.example.quanlydathang.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.quanlydathang.R;
import com.example.quanlydathang.dto.KhachHangDto;
import com.example.quanlydathang.dto.Product;

import java.util.List;

public class SPSpinnerAdapter extends BaseAdapter {
    private Context context;
    public List<Product> list;

    public SPSpinnerAdapter(Context context, List<Product> list){
        this.context=context;
        this.list=list;
    }
    @Override
    public int getCount() {
        return list!=null ? list.size():0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rootView  = LayoutInflater.from(context).inflate(R.layout.item_kh_spinner,
                viewGroup,false);
        TextView tvTen=rootView.findViewById(R.id.tvTenKHSpinner);
        ImageView ivAvatar=rootView.findViewById(R.id.ivKHSpinner);

        tvTen.setText(list.get(i).getMaSP()+" - "+list.get(i).getTenSP());
        if(list.get(i).getImage()!=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(list.get(i).getImage(), 0, list.get(i).getImage().length);
            ivAvatar.setImageBitmap(bitmap);
        }
        return rootView;
    }
}
