package api;

import java.util.List;
import java.util.Map;

import model.Heroes;
import model.ImageResponse;
import model.LoginSignupResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface HeroesApi {
    @GET("heroes")
    Call<List<Heroes>> getHeroes(@Header("Cookie") String cookie);

    @GET("users/logout")
    Call<LoginSignupResponse> logout(@Header("Cookie") String cookie);

    @POST("heroes")
    Call<Void> addHero(@Header("Cookie") String cookie,@Body Heroes heroes);

    @FormUrlEncoded
    @POST("heroes")
    Call<Void> addFieldHero(@Header("Cookie") String cookie,@Field("image") String image, @Field("name") String name, @Field("desc") String desc);

    @FormUrlEncoded
    @POST("heroes")
    Call<Void> addMapHero(@Header("Cookie") String cookie,@FieldMap Map<String, String> map);

    @Multipart
    @POST("upload")
    Call<ImageResponse> uploadImage(@Header("Cookie") String cookie, @Part MultipartBody.Part img);

    @FormUrlEncoded
    @POST("users/login")
    Call<LoginSignupResponse> checkUser(@Field("username") String username, @Field("password") String password);
}
