package com.example.quanlydathang.adapter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydathang.R;
import com.example.quanlydathang.activitydonhang.DonDatHangActivity;
import com.example.quanlydathang.dao.DonHangDao;
import com.example.quanlydathang.dao.KhachHangDao;
import com.example.quanlydathang.dto.DonHangDto;
import com.example.quanlydathang.dto.KhachHangDto;

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
    private KhachHangDao khachHangDao;
    String kh="";

    public DonDatHangAdapter(Context context, List<DonHangDto> donHangDtoList) {
        this.context=context;
        this.donHangDtoList = donHangDtoList;
        this.donHangDtoListOld = donHangDtoList;
        donHangDao=new DonHangDao(context);
        khachHangDao =new KhachHangDao(context);
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
           deleteDialog(id );
        });
       holder.itemView.setOnClickListener(view -> {
           String[] items={"Sửa","Xem chi tiết"};
           AlertDialog.Builder builder=new AlertDialog.Builder(context);
           builder.setItems(items, (dialogInterface, i) -> {
               if (i==0){
                   Dialog dialog = getDialogDDH(context);
                   tvMaDHDialog.setVisibility(View.VISIBLE);
                    int maDhDialog=donHang.getMaDH();
                    tvTieuDeDialog.setText("Sửa đơn hàng");
                    tvMaDHDialog.setText(maDhDialog+"");
                    btnThemDialog.setText("Cập nhập");

                   List<String> listKH=dsMaVaHoTenKH();
                   ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(context
                            , androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
                           , listKH);
                   xuKienSpinner();
                   spKHDialog.setAdapter(arrayAdapter);
                    btnHuyDialog.setOnClickListener(view1 -> dialog.cancel());
                    edNgayDHDialog.setOnFocusChangeListener((view1, b) -> showDateTimeDialog(edNgayDHDialog));
                   btnThemDialog.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view1) {
                           if (edNgayDHDialog.getText().toString().isEmpty()){
                                Toast.makeText(context
                                        , "Không được bỏ trống ngày giờ!", Toast.LENGTH_SHORT).show();
                            }
                            DonHangDto donHang1 =new DonHangDto();
                            donHang1.setMaDH(Integer.valueOf(tvMaDHDialog.getText().toString()));
                            donHang1.setNgayDH(edNgayDHDialog.getText().toString());
                            donHang1.setMaKH(getIdKHFromSpiner(kh));
                            donHangDao.suaDonHang(donHang1);
                            dialog.cancel();
                            Toast.makeText(context
                                    , "Sửa đơn đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                            ((DonDatHangActivity)context).onResume();
                       }
                   });
                   dialog.show();

               }else{

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
        return dialog;
    }
    public void deleteDialog(int id){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setMessage("Bán muốn xóa đơn hàng: "+id+"?")
                .setPositiveButton("Xóa", (dialogInterface, i) -> {
                    if (donHangDao.xoaDonHang((int)id)){
                        donHangDao.xoaDonHang(id);
                        ((DonDatHangActivity)context).onResume();
                        Toast.makeText(context
                                , "Xóa đơn hàng thành công!", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(context
                                , "Lỗi!không thể xóa đơn hàng.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", (dialogInterface, i) -> {

                });
        builder.create().show();
    }
    public List<String> dsMaVaHoTenKH(){
        List<String> ds=new ArrayList<>();
        List<KhachHangDto> dsKH= khachHangDao.getListKH();
        for (KhachHangDto item :
                dsKH) {
            ds.add(item.getId()+" - "+item.getName());
        }
        return ds;
    }
    public int getIdKHFromSpiner(String str){
        String[] words=str.split("-");
        if (!str.isEmpty()){
            return Integer.valueOf(words[0].trim());
        }
        return 0;
    }
    public void xuKienSpinner() {
        spKHDialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                kh=spKHDialog.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                kh="";
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
}
