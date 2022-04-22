package com.example.quanlydathang.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydathang.R;
import com.example.quanlydathang.activity.PDFDonHangActivity;
import com.example.quanlydathang.activity.TTDDH_Activity;
import com.example.quanlydathang.activitydonhang.DonDatHangActivity;
import com.example.quanlydathang.dao.DonHangDao;
import com.example.quanlydathang.dao.KhachHangDao;
import com.example.quanlydathang.dto.DonHangDto;
import com.example.quanlydathang.dto.KhachHangDto;
import com.example.quanlydathang.utils.CustomToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DonDatHangAdapter extends RecyclerView.Adapter<DonDatHangAdapter.DDHViewHolder> implements Filterable {

    private Context context;
    private List<DonHangDto> donHangDtoList;
    private List<DonHangDto> donHangDtoListOld;
    private DonHangDao donHangDao;

    public EditText edNgayDHDialog ;
    public   TextView tvTieuDeDialog;
    public   TextView tvMaDHDialog ;
    public  Spinner spKHDialog ;
    public  Button btnThemDialog ;
    public   Button btnHuyDialog ;
    public KHSpinnerAdapter khSpinnerAdapter;

    private KhachHangDao khachHangDao;
    public  List<KhachHangDto> khachHangDtos;

    public static int kh=0;

    public DonDatHangAdapter(Context context, List<DonHangDto> donHangDtoList) {
        this.context=context;
        this.donHangDtoList = donHangDtoList;
        this.donHangDtoListOld = donHangDtoList;
        donHangDao=new DonHangDao(context);
        khachHangDao =new KhachHangDao(context);
        khachHangDtos=khachHangDao.getListKH();
    }

    @NonNull
    @Override
    public DDHViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dondathang,parent,false);
        return new DDHViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DDHViewHolder holder, int position) {
        DonHangDto donHang=donHangDtoList.get(position);
        if (donHang==null){
            return;
        }
        int id=donHang.getMaDH();
        holder.tvMaDH.setText(String.valueOf(id));
        holder.tvKH.setText(donHang.getTenKH());
        holder.tvNgayDH.setText(donHang.getNgayDH());
        holder.ibDelete.setOnClickListener(view -> {
           deleteDialog(id);
        });
       holder.itemView.setOnClickListener(view -> {
           /*String[] items={"Sửa","Xem chi tiết"};*/
           String[] items={"Sửa","Xem chi tiết", "In thông tin đơn hàng"};
           AlertDialog.Builder builder=new AlertDialog.Builder(context);
           builder.setItems(items, (dialogInterface, i) -> {
               if (i==0){
                   Dialog dialog = getDialogDDH(context);
                   tvMaDHDialog.setVisibility(View.VISIBLE);
                    int maDhDialog=donHang.getMaDH();
                    spKHDialog.setSelection(getIndexKH(donHang.getMaKH()));
                    edNgayDHDialog.setText(donHang.getNgayDH());
                    tvTieuDeDialog.setText("Sửa đơn hàng");
                    tvMaDHDialog.setText(maDhDialog+"");
                    btnThemDialog.setText("Cập nhập");
                    btnHuyDialog.setOnClickListener(view1 -> dialog.cancel());
                    edNgayDHDialog.setOnFocusChangeListener((view1, b) -> showDateTimeDialog(edNgayDHDialog));
                   btnThemDialog.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view1) {
                           if (edNgayDHDialog.getText().toString().isEmpty()){

                               CustomToast.makeText(context, "Không được bỏ trống ngày giờ!",
                                       CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
                               return;
                            }
                            DonHangDto donHang1 =new DonHangDto();
                            donHang1.setMaDH(Integer.valueOf(tvMaDHDialog.getText().toString()));
                            donHang1.setNgayDH(edNgayDHDialog.getText().toString());
                            donHang1.setMaKH(kh);
                            donHangDao.suaDonHang(donHang1);
                            dialog.cancel();

                           CustomToast.makeText(context, "Sửa đơn đặt hàng thành công!",
                                   CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();
                            ((DonDatHangActivity)context).onResume();
                       }
                   });
                   dialog.show();
                   dialog.getWindow().setLayout((6*DonDatHangActivity.width)/7, WindowManager.LayoutParams.WRAP_CONTENT);
               }
               else if (i==2){
                   Intent intent = new Intent(context, PDFDonHangActivity.class);
                   intent.putExtra("id",donHang.getMaDH());
                   ((Activity)context).startActivity(intent);
               }
               else {
                   //truyền thông tin và start activity TTDDH để cập nhật - linh
                   Intent intent = new Intent(context, TTDDH_Activity.class);
                   Bundle bundle = new Bundle();
                   bundle.putInt("maDH",donHang.getMaDH());
                   bundle.putString("ngayDH",donHang.getNgayDH());
                   bundle.putInt("maKH",donHang.getMaKH());
                   bundle.putString("tenKH",donHang.getTenKH());
                   intent.putExtras(bundle);
                   context.startActivity(intent);
               }
           });
           builder.show();
       });
    }

    public void showDateTimeDialog(EditText edNgayDH) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener
                = (datePicker, i, i1, i2) -> {
                    calendar.set(Calendar.YEAR, i);
                    calendar.set(Calendar.MONTH, i1);
                    calendar.set(Calendar.DAY_OF_MONTH, i2);

                    TimePickerDialog.OnTimeSetListener timeSetListener
                            = (timePicker, i3, i4) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, i3);
                                calendar.set(Calendar.MINUTE, i4);

                                SimpleDateFormat simpleDateFormat
                                        = new SimpleDateFormat("dd/MM/yyyy HH:mm");

                                edNgayDH.setText(simpleDateFormat.format(calendar.getTime()));
                            };
                    new TimePickerDialog(context,
                            timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),true).show();
                };
        new DatePickerDialog(context,
                dateSetListener, calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    public  Dialog  getDialogDDH(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setTitle("Hộp thoại xử lý");
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.themdonhang_dialog);
        tvTieuDeDialog=dialog.findViewById(R.id.tvTieuDeDialog);
        tvMaDHDialog=dialog.findViewById(R.id.tvIdDonHangDialog);
        edNgayDHDialog = dialog.findViewById(R.id.etNgayDH);
        spKHDialog = dialog.findViewById(R.id.spKH);
        btnThemDialog = dialog.findViewById(R.id.btnthem);
        btnHuyDialog = dialog.findViewById(R.id.btnhuy);
        edNgayDHDialog.setInputType(InputType.TYPE_NULL);
        khSpinnerAdapter =new KHSpinnerAdapter(context,khachHangDtos);
        spKHDialog.setAdapter(khSpinnerAdapter);
        spKHDialog.setDropDownVerticalOffset(150);
        spinnerListener();
        return dialog;
    }

    public void deleteDialog(int id){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage("Bán muốn xóa đơn hàng: "+id+"?")
                .setPositiveButton("Xóa", (dialogInterface, i) -> {
                    if (donHangDao.xoaDonHang((int)id)){

                        ((DonDatHangActivity)context).onResume();

                        CustomToast.makeText(context, "Xóa đơn hàng thành công!",
                                CustomToast.LENGTH_LONG, CustomToast.SUCCESS).show();

                    }else {

                        CustomToast.makeText(context, "Lỗi! không thể xóa đơn hàng.",
                                CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
                    }
                })
                .setNegativeButton("Hủy", (dialogInterface, i) -> {

                });
        builder.create().show();
    }

    public int getIndexKH(int maKH){
        int i=0;
        for ( KhachHangDto kh:khachHangDtos) {
            if (kh.getId()==maKH){
                return i;
            }
            i++;
        }
        return -1;
    }
    public void spinnerListener(){
        khachHangDao=new KhachHangDao(context);

        spKHDialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               kh= khachHangDtos.get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
               kh=0;
            }
        });
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

        public DDHViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaDH=itemView.findViewById(R.id.tvMaDH);
            tvKH=itemView.findViewById(R.id.tvtenKH);
            tvNgayDH=itemView.findViewById(R.id.tvNgayDH);
            ibDelete=itemView.findViewById(R.id.ib_delete);
        }
    }

    //tim KH - linh
    public String timTenKH(int id) {
        KhachHangDto khachHangDto = khachHangDao.getKHById(id);
        Log.e("khachHangDto", khachHangDto.getName()+"");
        return khachHangDto.getName();
    }
}
