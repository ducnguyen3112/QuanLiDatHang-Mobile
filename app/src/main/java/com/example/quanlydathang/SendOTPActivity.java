package com.example.quanlydathang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quanlydathang.dao.UserDao;
import com.example.quanlydathang.utils.CustomToast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class SendOTPActivity extends AppCompatActivity {
    private EditText etSDT;
    private TextView tvSendOTP;
    private ImageView ivBack;
    private ProgressBar pbLoad;

    private UserDao userDao;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_otp);
        anhXa();
        mAuth=FirebaseAuth.getInstance();
        userDao=new UserDao(this);
        tvSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guiMaOTP();
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void guiMaOTP() {
        String sdt=etSDT.getText().toString().trim();
        if (sdt.isEmpty()){

            CustomToast.makeText(SendOTPActivity.this, "Không được để trống tên số điện thoại!",
                    CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
            return;
        }
        if (userDao.getUserNameFromSDT(sdt)==null){
            CustomToast.makeText(SendOTPActivity.this, "Số điện thoại không đúng hoặc chưa được đăng kí!",
                    CustomToast.LENGTH_LONG, CustomToast.WARNING).show();
            return;
        }
        tvSendOTP.setVisibility(View.INVISIBLE);
        pbLoad.setVisibility(View.VISIBLE);
        sdt="+84"+ sdt.substring(1);
        Log.e("SDT", "guiMaOTP: "+sdt,null );
        verifyPhoneNumber(sdt);

    }
    public void verifyPhoneNumber(String phoneNumber){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                pbLoad.setVisibility(View.GONE);
                                tvSendOTP.setVisibility(View.VISIBLE);
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                                CustomToast.makeText(SendOTPActivity.this, "Xác thực không thành công!",
                                        CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                                pbLoad.setVisibility(View.GONE);
                                tvSendOTP.setVisibility(View.VISIBLE);

                                return;
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                pbLoad.setVisibility(View.GONE);
                                tvSendOTP.setVisibility(View.VISIBLE);

                                goToVerificationOTPActivity(phoneNumber,s);
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e("OTPVerify", "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            goToMainActivity(user.getPhoneNumber());
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.e("OTPVerify", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                CustomToast.makeText(SendOTPActivity.this, "Mã OTP bạn vừa nhập không đúng!",
                                        CustomToast.LENGTH_LONG, CustomToast.ERROR).show();
                                return;
                            }
                        }
                    }
                });
    }

    private void goToMainActivity(String phoneNumber) {
        Intent intent=new Intent(this,MainActivity.class);
        phoneNumber="0"+phoneNumber.substring(3);
        Log.e("SendOTPAC", "number"+phoneNumber , null);
        intent.putExtra("phoneNumber",phoneNumber);
        startActivity(intent);
    }
    private void goToVerificationOTPActivity(String phoneNumber, String s) {
        Intent intent=new Intent(this,VerificationOTPActivity.class);
        intent.putExtra("phoneNumber",phoneNumber);
        intent.putExtra("vertificationId",s);
        startActivity(intent);
    }
    public void anhXa(){
        etSDT=findViewById(R.id.etNhapSDT);
        tvSendOTP=findViewById(R.id.tvGuiMaOTP);
        ivBack=findViewById(R.id.ivBackSendOtp);
        pbLoad=findViewById(R.id.pbLoad);
    }
}