package com.example.quanlydathang.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydathang.R;
import com.example.quanlydathang.activity.TTDDH_Activity;
import com.example.quanlydathang.activitydonhang.DonDatHangActivity;
import com.example.quanlydathang.dao.DonHangDao;
import com.example.quanlydathang.dao.KhachHangDao;
import com.example.quanlydathang.dao.ProductDao;
import com.example.quanlydathang.dao.TTDDH_DAO;
import com.example.quanlydathang.dto.Product;
import com.example.quanlydathang.dto.TTDDH_DTO;
import com.example.quanlydathang.utils.CustomAlertDialog;
import com.example.quanlydathang.utils.CustomToast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TTDDH_Adapter extends RecyclerView.Adapter<TTDDH_Adapter.TTDDHViewHolder> implements Filterable {
    private Context context;
    private List<TTDDH_DTO> ttddh_dtoList;
    private List<TTDDH_DTO> ttddh_dtoListOld;
    private TTDDH_DAO ttddh_dao;
    private DonHangDao donHangDao;
    private Product productDao;

    public Spinner spinnerSP_dialogThemSP;
    public EditText etSoLuong_dialogThemSP;
    public Button btnHuy_dialogThemSP;
    public Button btnThem_dialogThemSP;
    public SPSpinnerAdapter spSpinnerAdapter;
    public List<Product> listProduct;

    private EditText etSL_dialogSuaSL;
    private Button btnHuy_dialogSuaSL;
    private Button btnDongY_dialogSuaSL;

    public static int sp = 0;

    public TTDDH_Adapter(Context context, List<TTDDH_DTO> ds, int maDH) {
        this.context=context;
        ttddh_dao = new TTDDH_DAO(context);
        donHangDao = new DonHangDao(context);
        this.ttddh_dtoList = new ArrayList<>();
        for (TTDDH_DTO item : ds) {
            if(item.getMaDH() == maDH) {
                this.ttddh_dtoList.add(item);
            }
        }
        this.ttddh_dtoListOld = ttddh_dtoList;
        listProduct = ttddh_dao.getListProducts();
    }

    @NonNull
    @Override
    public TTDDHViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ttddh,parent,false);
        return new TTDDHViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TTDDHViewHolder holder, int position) {
        TTDDH_DTO ttddh_dto = ttddh_dtoList.get(position);
        if(ttddh_dto == null) {
            return;
        }

        Product product = ttddh_dao.TimSanPham(ttddh_dto.getMaSP());
        holder.tvTenSP.setText(product.getTenSP());
        holder.tvSL.setText(ttddh_dto.getSL()+"");
        holder.tvDonGia.setText(product.getDonGia()+" USD");
        if(product.getImage()!=null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(product.getImage(), 0, product.getImage().length);
            holder.ivAnhSP.setImageBitmap(bitmap);
        }

        holder.ibXoaSP.setOnClickListener(view -> {
            CustomAlertDialog alertDialog = new CustomAlertDialog(context);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.getWindow().setLayout((7* DonDatHangActivity.width)/8, WindowManager.LayoutParams.WRAP_CONTENT);
            alertDialog.show();

            Log.e("product.getTenSP()",product.getTenSP());
            alertDialog.setMessage("X??a s???n ph???m " + product.getTenSP() + " kh???i ????n h??ng?");
            alertDialog.setBtnPositive("X??a");
            alertDialog.setBtnNegative("H???y");

            alertDialog.btnPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean boo = ttddh_dao.xoa_ttddh_dao(ttddh_dto);
                    if(boo) {
                        ((TTDDH_Activity)context).onResume();
                        CustomToast.makeText(context, "X??a th??nh c??ng",
                                CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
                        }
                    else {
                        CustomToast.makeText(context, "X??a th???t b???i!",
                                CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
                    }
                    alertDialog.cancel();
                }
            });

            alertDialog.btnNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.cancel();
                }
            });
        });

        holder.ibSuaSL.setOnClickListener(view -> {
            Dialog dialog = getDialogSuaSL_TTDDH(context);

            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout((6* DonDatHangActivity.width)/7, WindowManager.LayoutParams.WRAP_CONTENT);

            btnHuy_dialogSuaSL.setOnClickListener(view01 -> {
                dialog.cancel();
            });

            btnDongY_dialogSuaSL.setOnClickListener(view02 -> {
                String new_sl = etSL_dialogSuaSL.getText().toString();
                if(new_sl.isEmpty()) {
                    CustomToast.makeText(context, "Vui l??ng nh???p s??? l?????ng m???i!",
                            CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
                }
                else {
                    TTDDH_DTO new_ttddh_dto = new TTDDH_DTO(ttddh_dto.getMaDH(),ttddh_dto.getMaSP(),Integer.parseInt(new_sl));
                    boolean boo = ttddh_dao.capNhat_ttddh_dao(new_ttddh_dto);
                    if(boo) {
                        CustomToast.makeText(context, "C???p nh???t th??nh c??ng!",
                                CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
                    }
                    else {
                        CustomToast.makeText(context, "C???p nh???t th???t b???i",
                                CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
                    }
                    dialog.cancel();
                    ((TTDDH_Activity)context).onResume();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        if(ttddh_dtoList==null) {
            return 0;
        }
        return ttddh_dtoList.size();
    }

    public class TTDDHViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTenSP, tvSL, tvDonGia;
        private ImageButton ibSuaSL, ibXoaSP;
        private ImageView ivAnhSP;

        public TTDDHViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenSP = itemView.findViewById(R.id.tvTenSP_itemTTDDH);
            tvSL = itemView.findViewById(R.id.tvSL_itemTTDDH);
            tvDonGia = itemView.findViewById(R.id.tvDonGia_itemTTDDH);
            ibSuaSL = itemView.findViewById(R.id.ibSuaSL_itemTTDDH);
            ibXoaSP = itemView.findViewById(R.id.ibXoaSP_itemTTDDH);
            ivAnhSP = itemView.findViewById(R.id.ivAnhSP_itemTTDDH);
        }
    }

    public Dialog getDialogThemSP_TTDDH(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setTitle("Th??m s???n ph???m");
        dialog.setCancelable(false);

        dialog.setContentView(R.layout.dialog_ttddh_themsanpham);
        spinnerSP_dialogThemSP = dialog.findViewById(R.id.spinnerSP_dialogThemSP);
        etSoLuong_dialogThemSP = dialog.findViewById(R.id.etSL_dialogThemSP);
        btnHuy_dialogThemSP = dialog.findViewById(R.id.btnHuy_dialodThemSP);
        btnThem_dialogThemSP = dialog.findViewById(R.id.btnThem_dialodThemSP);

        spSpinnerAdapter = new SPSpinnerAdapter(context,listProduct);
        spinnerSP_dialogThemSP.setAdapter(spSpinnerAdapter);
        spinnerSP_dialogThemSP.setDropDownVerticalOffset(150);

        spinnerListener();
        return dialog;
    }

    public List<String> dsMaVaTenSP() {
        List<String> list = new ArrayList<>();
        List<Product> listProducts = ttddh_dao.getListProducts();

        for(Product product : listProducts) {
            list.add(product.getMaSP() + " - " + product.getTenSP());
        }
        return list;
    }

    public Dialog getDialogSuaSL_TTDDH(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setTitle("S???a s??? l?????ng");
        dialog.setCancelable(false);

        dialog.setContentView(R.layout.dialog_ttddh_suasoluong);
        etSL_dialogSuaSL = dialog.findViewById(R.id.etSL_dialogSuaSL_ttddh);
        btnHuy_dialogSuaSL = dialog.findViewById(R.id.btnHuy_dialogSuaSL_ttddh);
        btnDongY_dialogSuaSL = dialog.findViewById(R.id.btnDongY_dialogSuaSL_ttddh);

        return dialog;
    }

    public String TongTien() {
        if(ttddh_dtoList==null) {
            return "0";
        }
        int sum = 0;
        for(TTDDH_DTO ttddh_dto : ttddh_dtoList) {
            Product product = ttddh_dao.TimSanPham(ttddh_dto.getMaSP());
            sum += ttddh_dto.getSL()*product.getDonGia();
        }
        return NumberFormat.getNumberInstance(Locale.US).format(sum);
    }

    public void spinnerListener(){
        spinnerSP_dialogThemSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sp = listProduct.get(i).getMaSP();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                sp = 0;
            }
        });
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String s = charSequence.toString();
                if(s.isEmpty()) {
                    ttddh_dtoList = ttddh_dtoListOld;
                }
                else {
                    List<TTDDH_DTO> list = new ArrayList<>();
                    for(TTDDH_DTO ttddh_dto : ttddh_dtoListOld) {
                        Product product = ttddh_dao.TimSanPham(ttddh_dto.getMaSP());

                        if(product.getTenSP().toLowerCase().contains(s.toLowerCase())) {
                            list.add(ttddh_dto);
                        }
                    }
                    ttddh_dtoList = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = ttddh_dtoList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                ttddh_dtoList = (List<TTDDH_DTO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public int selectedProductID() {
        return sp;
    }

    public List<TTDDH_DTO> getTtddh_dtoListOld() {
        return ttddh_dtoListOld;
    }
}
