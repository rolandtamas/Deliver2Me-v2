package com.app.deliver2me.notificationpack;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface APIService {
    @Headers(
            {"Authorization: key=AAAAUVRBsbY:APA91bEsSgM7xDoywPxr6qg7gUAR3DbwfauQEZ82xthmqHvmwzVmK1X13Dmg62WcoylKFkd2TeByQbKSyAVerH8qpf-hyOxsbKxjKy4xVLCqBeezN9_a8eeVt3am-4I9E4-sGi_pFxWE",
                    "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body NotificationBuilder builder);

}
