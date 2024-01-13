package com.example.tiplearning.service;


import com.example.tiplearning.model.runtime.HttpResponse;
import com.example.tiplearning.model.runtime.MarkRecord;
import com.example.tiplearning.model.runtime.QuizResponse;
import com.example.tiplearning.model.runtime.RequestFindQuiz;
import com.example.tiplearning.model.runtime.UpdateInfoRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {

    @GET("user/gets/3")
    Call<List<QuizResponse>> getQuizzes(@Query("type") String type,@Query("userId") String userId);

    @POST("user/marks")
    Call<MarkRecord> mark(@Body MarkRecord markRecord,@Query("userId") String userId);

//    @POST("user/query")
//    Call<String> query(@Body RequestFindQuiz requestFindQuiz, @Query("userId") String userId);

    @GET("user/query/{questions}")
    Call<List<String>> query(@Path("questions") String questions, @Query("userId") String userId);

    @POST("user/updateInfo")
    Call<HttpResponse> updateInfo(@Body UpdateInfoRequest updateInfoRequest, @Query("userId") String userId);

    @GET("user/reportQuiz")
    Call<String> reportQuiz(@Query("quizId") String quizId,@Query("userId") String userId);

    @GET("user/createQuiz")
    Call<String> createQuiz(@Query("quizContent") String quizContent, @Query("quizType") String quizType,@Query("userId") String userId);

}