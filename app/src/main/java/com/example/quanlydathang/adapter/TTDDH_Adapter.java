package com.example.quanlydathang.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quanlydathang.R;
import com.example.quanlydathang.activity.TTDDH_Activity;
import com.example.quanlydathang.dao.DonHangDao;
import com.example.quanlydathang.dao.ProductDao;
import com.example.quanlydathang.dao.TTDDH_DAO;
import com.example.quanlydathang.dto.Product;
import com.example.quanlydathang.dto.TTDDH_DTO;
import com.example.quanlydathang.utils.CustomToast;

import java.util.ArrayList;
import java.util.List;

public class TTDDH_Adapter extends RecyclerView.Adapter<TTDDH_Adapter.TTDDHViewHolder> {
    private Context context;
    private List<TTDDH_DTO> ttddh_dtoList;
    private TTDDH_DAO ttddh_dao;
    private DonHangDao donHangDao;
    private Product productDao;

    public Spinner spinnerSP_dialogThemSP;
    public EditText etSoLuong_dialogThemSP;
    public Button btnHuy_dialogThemSP;
    public Button btnThem_dialogThemSP;

    private EditText etSL_dialogSuaSL;
    private Button btnHuy_dialogSuaSL;
    private Button btnDongY_dialogSuaSL;

    public int sum = 0;

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

        holder.ibXoaSP.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Xóa sản phẩm " + product.getTenSP() + " khỏi đơn hàng?")
                    .setPositiveButton("Xóa", (dialogInterface, i) -> {
                        //
                        boolean boo = ttddh_dao.xoa_ttddh_dao(ttddh_dto);
                        if(boo) {
                            ((TTDDH_Activity)context).onResume();
                            CustomToast.makeText(context, "Xóa thành công",
                                    CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
                        }
                        else {
                            CustomToast.makeText(context, "Xóa thất bại!",
                                    CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
                        }
                    })
                    .setNegativeButton("Hủy", (dialogInterface, i) -> {
                        //
                    });
            builder.create().show();
        });

        holder.ibSuaSL.setOnClickListener(view -> {
            Dialog dialog = getDialogSuaSL_TTDDH(context);
            dialog.show();

            btnHuy_dialogSuaSL.setOnClickListener(view01 -> {
                dialog.cancel();
            });

            btnDongY_dialogSuaSL.setOnClickListener(view02 -> {
                String new_sl = etSL_dialogSuaSL.getText().toString();
                if(new_sl.isEmpty()) {
                    CustomToast.makeText(context, "Vui lòng nhập số lượng mới!",
                            CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
                }
                else {
                    TTDDH_DTO new_ttddh_dto = new TTDDH_DTO(ttddh_dto.getMaDH(),ttddh_dto.getMaSP(),Integer.parseInt(new_sl));
                    boolean boo = ttddh_dao.capNhat_ttddh_dao(new_ttddh_dto);
                    if(boo) {
                        CustomToast.makeText(context, "Cập nhật thành công!",
                                CustomToast.LENGTH_SHORT, CustomToast.SUCCESS).show();
                    }
                    else {
                        CustomToast.makeText(context, "Cập nhật thất bại",
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

        public TTDDHViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenSP = itemView.findViewById(R.id.tvTenSP_itemTTDDH);
            tvSL = itemView.findViewById(R.id.tvSL_itemTTDDH);
            tvDonGia = itemView.findViewById(R.id.tvDonGia_itemTTDDH);
            ibSuaSL = itemView.findViewById(R.id.ibSuaSL_itemTTDDH);
            ibXoaSP = itemView.findViewById(R.id.ibXoaSP_itemTTDDH);
        }
    }

    public Dialog getDialogThemSP_TTDDH(Context context) {
        Dialog dialog = new Dialog(context);
        dialog.setTitle("Thêm sản phẩm");
        dialog.setCancelable(false);

        dialog.setContentView(R.layout.dialog_ttddh_themsanpham);
        spinnerSP_dialogThemSP = dialog.findViewById(R.id.spinnerSP_dialogThemSP);
        etSoLuong_dialogThemSP = dialog.findViewById(R.id.etSL_dialogThemSP);
        btnHuy_dialogThemSP = dialog.findViewById(R.id.btnHuy_dialodThemSP);
        btnThem_dialogThemSP = dialog.findViewById(R.id.btnThem_dialodThemSP);

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
        dialog.setTitle("Sửa số lượng");
        dialog.setCancelable(false);

        dialog.setContentView(R.layout.dialog_ttddh_suasoluong);
        etSL_dialogSuaSL = dialog.findViewById(R.id.etSL_dialogSuaSL_ttddh);
        btnHuy_dialogSuaSL = dialog.findViewById(R.id.btnHuy_dialogSuaSL_ttddh);
        btnDongY_dialogSuaSL = dialog.findViewById(R.id.btnDongY_dialogSuaSL_ttddh);

        return dialog;
    }

    public int TongTien() {
        if(ttddh_dtoList==null) {
            return 0;
        }
        int sum = 0;
        for(TTDDH_DTO ttddh_dto : ttddh_dtoList) {
            Product product = ttddh_dao.TimSanPham(ttddh_dto.getMaSP());
            sum += ttddh_dto.getSL()*product.getDonGia();
        }
        return sum;
    }
}
