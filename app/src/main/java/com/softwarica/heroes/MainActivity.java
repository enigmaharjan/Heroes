package com.softwarica.heroes;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.HeroesApi;
import model.Heroes;
import model.url;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private EditText etName, etDesc;
    private Button btnAdd, btnByField, btnByMap, btnGet;
    private TextView tvResult;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etName = findViewById(R.id.etName);
        etDesc = findViewById(R.id.etDesc);
        btnAdd = findViewById(R.id.btnAdd);
        btnGet = findViewById(R.id.btnGet);
        tvResult = findViewById(R.id.tvResult);
        btnByField = findViewById(R.id.btnAddByField);
        btnByMap = findViewById(R.id.btnAddByMap);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                tvResult.setText("");
            }
        });


        btnByMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataByMap();
            }
        });

        btnByField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataByField();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showData();
            }
        });
    }

    private void saveDataByField() {
        String name = etName.getText().toString();
        String desc = etDesc.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        HeroesApi heroesApi = retrofit.create(HeroesApi.class);

        Call<Void> call = heroesApi.addFieldHero(name, desc);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(MainActivity.this, "Successfully Inserted Hero", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Inserting Hero Unsuccessful", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void saveDataByMap() {

        String name = etName.getText().toString();
        String desc = etDesc.getText().toString();

        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("desc", desc);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        HeroesApi heroesApi = retrofit.create(HeroesApi.class);

        Call<Void> call = heroesApi.addMapHero(map);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(MainActivity.this, "Successfully Inserted Hero", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Inserting Hero Unsuccessful", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void showData() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        HeroesApi heroesApi = retrofit.create(HeroesApi.class);

        Call<List<Heroes>> listCall = heroesApi.getHeroes();

        listCall.enqueue(new Callback<List<Heroes>>() {
            @Override
            public void onResponse(Call<List<Heroes>> call, Response<List<Heroes>> response) {
                List<Heroes> heroes = response.body();
                for(Heroes hero: heroes){
                    String content = "";
                    content += "ID: " + hero.get_id() + "\n";
                    content += "Name: " + hero.getName() + "\n";
                    content += "Desc: " + hero.getDesc() + "\n";
                    tvResult.append(content);

                }
            }

            @Override
            public void onFailure(Call<List<Heroes>> call, Throwable t) {

            }
        });
    }

    private void saveData() {
        String name = etName.getText().toString();
        String desc = etDesc.getText().toString();

        Heroes heroes = new Heroes(name, desc);

        Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(url.BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
        HeroesApi heroesApi = retrofit.create(HeroesApi.class);

        Call<Void> call = heroesApi.addHero(heroes);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(MainActivity.this, "Successfully Inserted Hero", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Inserting Hero Unsuccessful", Toast.LENGTH_SHORT).show();

            }
        });



    }
}
