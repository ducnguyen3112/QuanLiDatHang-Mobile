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

import java.util.List;

public class KHSpinnerAdapter extends BaseAdapter {
    private Context context;
    public List<KhachHangDto> khachHangDtos;

    public KHSpinnerAdapter(Context context,List<KhachHangDto> khachHangDtos){
        this.context=context;
        this.khachHangDtos=khachHangDtos;
    }
    @Override
    public int getCount() {
        return khachHangDtos!=null ? khachHangDtos.size():0;
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

        tvTen.setText(khachHangDtos.get(i).getId()+" - "+khachHangDtos.get(i).getName());
        if(khachHangDtos.get(i).getImage()!=null){
            Bitmap bitmap = BitmapFactory.decodeByteArray(khachHangDtos.get(i).getImage(), 0, khachHangDtos.get(i).getImage().length);
            ivAvatar.setImageBitmap(bitmap);
        }else {
            ivAvatar.setImageResource(R.drawable.custom_person_icon);
        }
        return rootView;
    }
}
