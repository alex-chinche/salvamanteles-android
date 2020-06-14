package com.example.salvamanteles;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface apiInterface {
        @FormUrlEncoded
        @POST("createUser")
        Call<Token> register(@Field("name") String name, @Field("email") String email, @Field("password") String password);
        @POST("loginUser")
        Call<Token> login(@Field("email") String email, @Field("password") String password);
    }

