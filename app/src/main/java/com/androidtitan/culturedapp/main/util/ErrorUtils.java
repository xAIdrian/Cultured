package com.androidtitan.culturedapp.main.util;

import com.androidtitan.culturedapp.main.domain.retrofit.ServiceGenerator;
import com.androidtitan.culturedapp.model.ApiError;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

/**
 * Created by amohnacs on 9/3/16.
 */

public class ErrorUtils {

    public static ApiError parseError(Response<?> response) {

        ApiError error;
        Converter<ResponseBody, ApiError> converter =
                ServiceGenerator.retrofit.responseBodyConverter(ApiError.class, new Annotation[0]);

        try {
            error = converter.convert(response.errorBody());
        } catch (IOException e) {
            return new ApiError();
        }

        return error;
    }

}
