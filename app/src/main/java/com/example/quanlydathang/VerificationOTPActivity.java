package com.example.quanlydathang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

public class VerificationOTPActivity extends AppCompatActivity {

    private EditText etOtp;
    private Button btnLogin;
    private TextView tvGuiLaiOtp;
    
    private String phoneNumber;
    private String verificationId;

    private FirebaseAuth mAuth;
    PhoneAuthProvider.ForceResendingToken mForceResendingToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_otp);
        anhXa();
        getDataIntent();
        mAuth=FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otpCode= etOtp.getText().toString().trim();
                xacNhanOtpClick(otpCode);
            }
        });
        tvGuiLaiOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guiLaiOTP();
            }
        });
    }

    private void guiLaiOTP() {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setForceResendingToken(mForceResendingToken)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(VerificationOTPActivity.this
                                        ,"Xác thực không thành công!",Toast.LENGTH_SHORT).show();
                                return;
                            }

                            @Override
                            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                super.onCodeSent(s, forceResendingToken);
                                verificationId=s;
                               mForceResendingToken= forceResendingToken;
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void xacNhanOtpClick(String otpCode) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otpCode);
        signInWithPhoneAuthCredential(credential);
    }

    public void getDataIntent(){
        phoneNumber=getIntent().getStringExtra("phoneNumber");
        verificationId =getIntent().getStringExtra("vertificationId");
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
                                Toast.makeText(VerificationOTPActivity.this
                                        ,"Mã OTP bạn vừa nhập không đúng!",Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                });
    }
    private void goToMainActivity(String phoneNumber) {
        Intent intent=new Intent(this,MainActivity.class);
        phoneNumber="0"+phoneNumber.substring(3);
        intent.putExtra("phoneNumber",phoneNumber);
        startActivity(intent);
    }
    private void anhXa(){
        etOtp =findViewById(R.id.etNhapMaOTP);
        btnLogin=findViewById(R.id.btnLoginOTP);
        tvGuiLaiOtp=findViewById(R.id.tvGuiLaiOTP);
    }
}