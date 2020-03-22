package com.farshidabz.kindnesswall.data.remote.network

import com.farshidabz.kindnesswall.data.model.LoginResponseModel
import com.farshidabz.kindnesswall.data.model.requestsmodel.LoginUserRequestBodyBaseModel
import com.farshidabz.kindnesswall.data.model.requestsmodel.RegisterBaseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by Farshid Abazari since 25/10/19
 *
 * Usage: a interface to define end points
 *
 * How to call: just add in appInjector and repositoryImpl that you wanna use
 *
 */
interface AuthApi {
    @POST("register")
    fun registerUser(@Body registerModel: RegisterBaseModel): Response<Any>

    @POST("login")
    fun loginUser(@Body loginModel: LoginUserRequestBodyBaseModel): Response<LoginResponseModel>
}