package com.example.tiplearning.service;

import com.example.tiplearning.model.runtime.HttpResponse;
import com.example.tiplearning.model.runtime.RequestAuthGoogle;
import com.example.tiplearning.model.runtime.RequestLogin;
import com.example.tiplearning.model.runtime.RequestRegister;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("auth/login")
    Call<HttpResponse> login(@Body RequestLogin requestLogin);

    @POST("auth/authGoogle")
    Call<HttpResponse> authGoogle(@Body RequestAuthGoogle requestAuthGoogle);

    @POST("auth/register")
    Call<HttpResponse> register(@Body RequestRegister requestRegister);

}
