package com.example.marko.app1.RESTService;

import com.example.marko.app1.model.Comment;
import com.example.marko.app1.model.LikeDislike;
import com.example.marko.app1.model.Post;
import com.example.marko.app1.model.PostTags;
import com.example.marko.app1.model.Tag;
import com.example.marko.app1.model.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RESTApi {

    String BASE_URL = "http://192.168.0.14:8080/api/";

    @GET("posts")
    Call<List<Post>> getPosts();

    @GET("tags")
    Call<List<Tag>> getTags();

    @GET("posts/{id}")
    Call<Post> getPostById(@Path("id") int id);

    @GET("posts/{id}/comments")
    Call<List<Comment>> getCommentsByPostId(@Path("id") int id);

    @GET("posts/{id}/tags")
    Call<List<Tag>> getTagsByPostId(@Path("id") int id);

    @GET("users/login")
    Call<User> authenticate(@Query("username") String username, @Query("password") String password);


    @GET("posts/{id}/user")
    Call<User> getUserByPostId(@Path("id") int id);

    @GET("users/{id}")
    Call<User> getUserById(@Path("id") int id);

    @GET("likeDislike/users/{userId}/posts/{postId}")
    Call<LikeDislike> getLikeDislike(@Path("userId") int userId, @Path("postId") int postId);


    @GET("likeDislike/users/{userId}/comments/{commentId}")
    Call<LikeDislike> getLikeDislikeComment(@Path("userId") int userId, @Path("commentId") int commentId);


    @GET("posts/filter/{keyword}")
    Call<List<Post>> getFilteredPosts(@Path("keyword") String keyword);

    @Multipart
    @POST("users/{id}/photo")
    Call<String> uploadUserPhoto(@Path("id") int id, @Part MultipartBody.Part photo, @Part("photo")RequestBody photoName);


    @Multipart
    @POST("users/upload")
    Call<ResponseBody> upload(@Part MultipartBody.Part filePart);

    @Multipart
    @POST("posts/create")
    Call<ResponseBody> createPost(@Part MultipartBody.Part filePart,
                                  @Part("authorId") int authorId,
                                  @Part("title") String title,
                                  @Part("description") String description,
                                  @Part("location") String location,
                                  @Part("tags") String[]  tags);


    @FormUrlEncoded
    @POST("comments/create")
    Call<Comment> createComment(@Field("title") String title,
                                @Field("description") String description,
                                @Field("authorId") int authorId,
                                @Field("postId") int postId);

    @FormUrlEncoded
    @POST("likeDislike/create")
    Call<ResponseBody> createLikeDislike(@Field("isLike") boolean isLike,
                                         @Field("isPost") boolean isPost,
                                         @Field("userId") int authorId,
                                         @Field("entityId") int entityId);

    @DELETE("likeDislike/delete/{id}")
    Call<ResponseBody> deleteLikeDislike(@Path("id") int likeDislikeId);


    @DELETE("posts/{id}")
    Call<ResponseBody> deletePost(@Path("id") int postId);

    @DELETE("comments/{id}")
    Call<ResponseBody> deleteComment(@Path("id") int commentId);

    @PUT("likeDislike/update/{id}")
    Call<String> updateLikeDislike(@Path("id") int id);
}
