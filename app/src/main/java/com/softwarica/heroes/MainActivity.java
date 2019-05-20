package com.softwarica.heroes;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import api.HeroesApi;
import model.Heroes;
import model.ImageResponse;
import model.url;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private EditText etName, etDesc;
    private Button btnAdd, btnByField, btnByMap, btnGet;
    private TextView tvResult;
    private ImageView ivImage;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String imagePath, imageName;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etName = findViewById(R.id.etName);
        etDesc = findViewById(R.id.etDesc);
        btnAdd = findViewById(R.id.btnAdd);
        btnGet = findViewById(R.id.btnGet);
//        tvResult = findViewById(R.id.tvResult);
        btnByField = findViewById(R.id.btnAddByField);
        btnByMap = findViewById(R.id.btnAddByMap);
        ivImage = findViewById(R.id.ivImage);
        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView = findViewById(R.id.recyclerViewMain);



        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowseImage();
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





    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, projection,null,null,null);
        Cursor cursor = loader.loadInBackground();
        int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(colIndex);
        cursor.close();
        return result;
    }
    private void previewImage(String ivImag) {
        File imgFile = new File(ivImag);
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ivImage.setImageBitmap(myBitmap);
        }
    }

    private void saveDataByField() {
        String name = etName.getText().toString();
        String desc = etDesc.getText().toString();

        Retrofit retrofit = url.getInstance();
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

        SaveImageOnly();
        String name = etName.getText().toString();
        String desc = etDesc.getText().toString();

        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("desc", desc);
        map.put("image", imageName);

        Retrofit retrofit = url.getInstance();
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


        Retrofit retrofit = url.getInstance();
        HeroesApi heroesApi = retrofit.create(HeroesApi.class);

        Call<List<Heroes>> listCall = heroesApi.getHeroes();

        listCall.enqueue(new Callback<List<Heroes>>() {
            @Override
            public void onResponse(Call<List<Heroes>> call, Response<List<Heroes>> response) {
                List<Heroes> heroes = response.body();
                for(Heroes hero: heroes){
                    String content = "";
                    content += "ID: " + hero.get_id() + "\n";
                    content += "Image: " + hero.getImage() + "\n";
                    content += "Name: " + hero.getName() + "\n";
                    content += "Desc: " + hero.getDesc() + "\n";
//                    tvResult.append(content);
                }
//                List<Heroes> heroesList = new ArrayList<>();
//                heroesList = response.body();
                HeroesAdapter heroesAdapter = new HeroesAdapter(MainActivity.this, heroes);
                recyclerView.setAdapter(heroesAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
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

        Retrofit retrofit = url.getInstance();
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

    //For image browsing
    private void BrowseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    //Previewing image into imageView
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(data == null){
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
            }
        }
        Uri uri = data.getData();
        imagePath = getRealPathFromUri(uri);
        previewImage(imagePath);
    }

    private void StrictMode(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    //Saving Image to database
    private void SaveImageOnly(){
        File file = new File(imagePath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("imageFile", file.getName(), requestBody);

        HeroesApi heroesApi = url.getInstance().create(HeroesApi.class);
        Call<ImageResponse> responseCall = heroesApi.uploadImage(body);

        StrictMode();
        try{
            Response<ImageResponse> imageResponseResponse = responseCall.execute();
            imageName = imageResponseResponse.body().getFilename();
        } catch (IOException e) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        refreshList();
    }

    private void refreshList() {
        showData();
        swipeRefreshLayout.setRefreshing(false);
    }
}
