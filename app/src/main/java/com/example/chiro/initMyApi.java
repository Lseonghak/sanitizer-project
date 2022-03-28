package com.example.chiro;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface initMyApi {
    //@통신 방식("통신 API명")
    @POST("/get_post.php")
    Call<LoginResponse> getLoginResponse(@Body LoginRequest loginRequest);
}