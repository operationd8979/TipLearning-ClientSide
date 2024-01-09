package com.example.tiplearning.service;


import com.example.tiplearning.model.runtime.MarkRecord;
import com.example.tiplearning.model.runtime.QuizResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {

    @GET("user/gets/3")
    Call<List<QuizResponse>> getQuizzes(@Query("userId") String userId);

    @POST("user/marks")
    Call<MarkRecord> mark(@Body MarkRecord markRecord,@Query("userId") String userId);


}