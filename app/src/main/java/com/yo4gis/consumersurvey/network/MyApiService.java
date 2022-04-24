package com.yo4gis.consumersurvey.network;




import com.yo4gis.consumersurvey.model.PostBuilding;
import com.yo4gis.consumersurvey.model.PostConsumerCreate;
import com.yo4gis.consumersurvey.model.ResponseAssignedTaskList;
import com.yo4gis.consumersurvey.model.ResponseBuilding;
import com.yo4gis.consumersurvey.model.ResponseConsumer;
import com.yo4gis.consumersurvey.model.ResponseGeoJson;
import com.yo4gis.consumersurvey.model.ResponseLogin;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface MyApiService {

    @POST("token")
    @FormUrlEncoded
    @Headers("Content-Type:application/x-www-form-urlencoded")
    Observable<ResponseLogin> postLoginAPI(@Field("username") String username,
                                           @Field("password") String password,
                                           @Field("grant_type") String grant_type);


    @POST("api/msit/createbuilding")
    Observable<ResponseBuilding> postBuildingAPI(@Body PostBuilding model);


  /*  @POST("api/msit/createfeature")
    Observable<ResponseCreatePoleFeature> postCreatePollAPI(@Body PostPole model);*/

    @GET
    Observable<ResponseAssignedTaskList> getAssignedTask(@Url String url);

    @GET
    Observable<ResponseGeoJson> getGeoJsonFeature(@Url String url);

    @GET
    Observable<ResponseBuilding> getBuildingID(@Url String url);

    @POST("api/msit/createConsumerAndLtLine")
    Observable<ResponseConsumer> postConsumerCreateAPI(@Body PostConsumerCreate model);

   /*

    @GET
    Observable<ResponseSection> getSectionList(@Url String url);

    @GET
    Observable<ResponseNINId> getNinID(@Url String url);



   /* @POST
    Observable<ResponseRegistration> postRegisterUserAPI(@Url String url, @Body PostRegistration model);

    @POST
    Observable<ResponseRegistration> postRegisterInternalUserAPI(@Url String url, @Body PostInternalRegistration model);

    @POST
    Observable<ResponseRegistration> postLoginAPI(@Url String url, @Body PostLogin model);

    @POST
    Observable<ResponseNewComplaint> postComplaintAPI(@Url String url, @Body PostNewComplaintRequest model);


    @Multipart
    @POST
    Observable<ResponseNewComplaint> postComplaintMultiAPI(@Url String url,
                                                            @Part MultipartBody.Part[] imagePart,
                                                            @Part MultipartBody.Part voicePart,
                                                            @Part MultipartBody.Part vedioPart,
                                                            @PartMap HashMap<String, RequestBody> txtmap);


   *//* @Multipart
    @POST
    Call<ResponseNewComplaint>postComplaintMultiAPI(@Url String url,
                               @Part MultipartBody.Part[] imagePart,
                               @Part MultipartBody.Part voicePart,
                               @Part MultipartBody.Part vedioPart,
                               @PartMap HashMap<String, RequestBody> txtmap

    );*//*
    //


    @POST
    Observable<ResponseNewComplaint> postComplaintResolverAPI(@Url String url, @Body PostComplaintResolverRequest model);

    @POST
    Observable<ResponseValidateNewComplaintAPI> postValidateNewRequestAPI(@Url String url, @Body PostValidateNewRequest model);

    @POST
    Observable<BaseResponse> postAssignBackComplaint(@Url String url, @Body PostAssignBackComplaint model);

    @POST
    Observable<ResponseBaseAPI> postSubmitHappinessMeter(@Url String url, @Body PostSubmitHappiness model);

    @POST
    Observable<ResponseBaseAPI> postSubmitInspectionForm(@Url String url, @Body PostSubmitInspectionForm model);

    @POST
    Observable<ResponseBaseAPI> postUpdateDeviceToken(@Url String url, @Body PostUpdateDeviceTokenRequest model);

    @GET
    Observable<ResponseCategoryAPI> getCategory(@Url String url);

    @GET
    Observable<ResponseOTP> getOtpAPI(@Url String url);

    @GET
    Observable<ResponseComplaintsListAPI> getAllComplaints(@Url String url);

    @GET
    Observable<ResponseComplaintDetailAPI> getComplaintDetail(@Url String url);

    @GET
    Observable<ResponseGetYanbuEyeUserDetail> getYanbuEyeUserAPI(@Url String url);

    @GET
    Observable<ResponseMyInspectionAPI> getMyInspection(@Url String url);

    @GET
    Observable<ResponseInspectionFormAPI> getInspectionForm(@Url String url);

    @GET
    Observable<ResponseAllLookUp> getResponseAllLookUp(@Url String url);*/

}
