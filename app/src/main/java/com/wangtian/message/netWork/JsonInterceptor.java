package com.wangtian.message.netWork;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @Author Archy Wang
 * @Date 2017/11/20
 * @Description
 */

public class JsonInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();

//        String token = original.header("x-access-token");
       /* if (TextUtils.isEmpty(token)){
            token = UserInfoUtil.getInstance().getUserToken();
            if (TextUtils.isEmpty(token)){
                token="";
            }
        }*/

        Request request = original.newBuilder()
//                .header("x-access-token", token)
                .method(original.method(), original.body())
                .build();


        Response response = chain.proceed(request);
        MediaType mediaType = response.body().contentType();
        ResponseBody body = response.body();
        if (body != null) {
            String content = body.string();
            Log.d("JsonInterceptor", "content = " + content);
            if (response.code() != 200) {
                throw new ToastException("系统异常，请稍后重试");
            }
            try {
                JSONObject responseJson = new JSONObject(content);
                if (responseJson.has(NetConstant.RESPONSE_CODE)) {
                    if (responseJson.getInt(NetConstant.RESPONSE_CODE) == 200) {
                        if (responseJson.has(NetConstant.RESPONSE_DATA)) {
                            Object data = responseJson.get(NetConstant.RESPONSE_DATA);
                            ResponseBody responseBody = ResponseBody.create(mediaType, data.toString());
                            return response.newBuilder().body(responseBody).build();
                        } else {
//                            throw new JSONException("Rsponse body does not has Response Data ");
                            String str = "{\n\t\"value\":1\n}";
                            return response.newBuilder().body(ResponseBody.create(mediaType, str)).build();
                        }

                    } else if (responseJson.getInt(NetConstant.RESPONSE_CODE) == 500 || responseJson.getInt(NetConstant.RESPONSE_CODE) == 1) {
                        if (responseJson.has(NetConstant.RESPONSE_MESSAGE)) {
                            throw new ToastException(String.valueOf(responseJson.get(NetConstant.RESPONSE_MESSAGE)));
                        }
                    } else {
                        throw new ToastException("系统异常，请稍后重试");
                    }
                } else {
                    throw new IOException("Response does not have Response Code");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            throw new IOException("ResponseBody is Empty from the service");
        }


        return null;
    }
}
