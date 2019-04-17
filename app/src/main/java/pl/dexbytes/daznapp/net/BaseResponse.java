package pl.dexbytes.daznapp.net;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public interface BaseResponse<T> extends Callback<T> {
    @Override
    default void onResponse(final Call<T> call, final Response<T> response) {
        if (!response.isSuccessful()) {
            Error error = new Error();
            error.setError(getErrorBodyString(response));
            error.setCode(response.code());
            onError(error);
        } else {
            onSuccess(response.body());
        }
    }

    default String getErrorBodyString(final Response<T> response) {
        try {
            if (response.errorBody() != null) {
                return response.errorBody().string();
            } else return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    void onSuccess(T response);

    void onError(Error error);

    @Override
    default void onFailure(Call<T> call, Throwable t) {
        t.printStackTrace();
        Error error = new Error();
        error.setError(t.getMessage());
        onError(error);
    }

    class Error{
        private int code;
        private String error;


        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}