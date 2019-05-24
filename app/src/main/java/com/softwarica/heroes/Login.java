package com.softwarica.heroes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import api.HeroesApi;
import model.LoginSignupResponse;
import model.url;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
    private EditText etPassword, etUsername;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etPassword = findViewById(R.id.etPassword);
        etUsername = findViewById(R.id.etUsername);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });
    }

    private void checkLogin() {
        HeroesApi heroesApi = url.getInstance().create(HeroesApi.class);

        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        Call<LoginSignupResponse> loginSignupResponseCall = heroesApi.checkUser(username, password);

        loginSignupResponseCall.enqueue(new Callback<LoginSignupResponse>() {
            @Override
            public void onResponse(Call<LoginSignupResponse> call, Response<LoginSignupResponse> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(Login.this, "Either Username or Password is incorrect", Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    if(response.body().getSuccess()){
                        url.Cookie = response.headers().get("Set-Cookie");
                        Intent i = new Intent(Login.this, MainActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginSignupResponse> call, Throwable t) {
                Toast.makeText(Login.this, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
