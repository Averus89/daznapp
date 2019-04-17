package pl.dexbytes.daznapp.net;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BaseUrlChangingInterceptor implements Interceptor {
    private static BaseUrlChangingInterceptor sInterceptor;

    private HttpUrl httpUrl;

    public static BaseUrlChangingInterceptor get() {
        if (sInterceptor == null) {
            sInterceptor = new BaseUrlChangingInterceptor();
        }
        return sInterceptor;
    }

    private BaseUrlChangingInterceptor() {
    }


    public void setInterceptor(String url) {
        httpUrl = HttpUrl.parse(url);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        if(httpUrl != null) {
            original = original.newBuilder()
                    .url(httpUrl)
                    .build();
        }
        return chain.proceed(original);
    }
}
