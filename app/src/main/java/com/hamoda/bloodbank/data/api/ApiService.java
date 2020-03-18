package com.hamoda.bloodbank.data.api;

import com.hamoda.bloodbank.data.model.client.Client;
import com.hamoda.bloodbank.data.model.contact.Contact;
import com.hamoda.bloodbank.data.model.donation.createNewDonation.CreateNewDonation;
import com.hamoda.bloodbank.data.model.donation.donationDetails.DonationDetails;
import com.hamoda.bloodbank.data.model.donation.donationRequests.DonationRequests;
import com.hamoda.bloodbank.data.model.generalResponse.GeneralResponse;
import com.hamoda.bloodbank.data.model.getNotification.GetNotification;
import com.hamoda.bloodbank.data.model.posts.Post.Posts;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET("blood-types")
    Call<GeneralResponse> getBloodTypes();

    @GET("governorates")
    Call<GeneralResponse> getGovernorates();

    @GET("cities")
    Call<GeneralResponse> getCities(@Query("governorate_id") int governorateId);

    @GET("categories")
    Call<GeneralResponse> getCategories();

    @POST("signup")
    @FormUrlEncoded
    Call<Client> onSignUp(@Field("name") String name,
                          @Field("email") String email,
                          @Field("birth_date") String birthDate,
                          @Field("city_id") int cityId,
                          @Field("phone") String phone,
                          @Field("donation_last_date") String donationLastDate,
                          @Field("password") String password,
                          @Field("password_confirmation") String passwordConfirmation,
                          @Field("blood_type_id") int bloodTypeId);


    @POST("login")
    @FormUrlEncoded
    Call<Client> onLogin(@Field("phone") String phone,
                         @Field("password") String password);


    @POST("profile")
    @FormUrlEncoded
    Call<Client> editProfileData(@Field("name") String name
            , @Field("email") String email
            , @Field("birth_date") String birthday
            , @Field("city_id") int cityId
            , @Field("phone") String phone
            , @Field("donation_last_date") String donationLastDate
            , @Field("password") String password
            , @Field("password_confirmation") String passwordConfirmation
            , @Field("blood_type_id") int bloodTypeId
            , @Field("api_token") String apiToken);


    @GET("posts")
    Call<Posts> getAllPosts(@Query("api_token") String apiToken, @Query("page") int page);

    @GET("my-favourites")
    Call<Posts> getFavouritesPosts(@Query("api_token") String apiToken, @Query("page") int page);

    @GET("posts")
    Call<Posts> getPostsFilter(@Query("api_token") String apiToken, @Query("page") int page
            , @Query("keyword") String keyword, @Query("category_id") int categoryId);


    @GET("post")
    Call<Posts> displayPostDetails(@Query("api_token") String apiToken
            , @Query("post_id") int postId, @Query("page") int page);

    @POST("post-toggle-favourite")
    @FormUrlEncoded
    Call<Posts> addAndRemovePosts(@Field("post_id") int postId, @Field("api_token") String apiToken);


    @GET("donation-requests")
    Call<DonationRequests> getDonation(@Query("api_token") String apiToken, @Query("page") int page);


    @GET("donation-requests")
    Call<DonationRequests> getFilterDonation(@Query("api_token") String apiToken, @Query("blood_type_id") int bloodTypeId
            , @Query("governorate_id") int governorateId, @Query("page") int page);


    @POST("donation-request")
    @FormUrlEncoded
    Call<CreateNewDonation> createDonationRequest(@Field("api_token") String apiToken
            , @Field("patient_name") String patientName
            , @Field("patient_age") String patientAge
            , @Field("blood_type_id") int bloodTypeId
            , @Field("bags_num") String bagsNum
            , @Field("hospital_name") String hospitalName
            , @Field("hospital_address") String hospitalAddress
            , @Field("city_id") int cityId
            , @Field("phone") String phone
            , @Field("notes") String notes
            , @Field("latitude") double latitude
            , @Field("longitude") double longitude);


    @GET("donation-request")
    Call<DonationDetails> displayDonationDetails(@Query("api_token") String apiToken,
                                                 @Query("donation_id") int donationId);

    @POST("contact")
    @FormUrlEncoded
    Call<Contact> setConatctMessage(@Field("api_token") String apiToken
            , @Field("title") String title
            , @Field("message") String message);


    @GET("notifications")
    Call<GetNotification> getAllNotification(@Query("api_token") String apiToken
            , @Query("page") int page);

}