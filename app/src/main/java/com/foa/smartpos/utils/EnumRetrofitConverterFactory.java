package com.foa.smartpos.utils;
import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Leverage the SerializedName annotation to serialize enum objects in retrofit parameters.
 *
 * from https://stackoverflow.com/a/35801262/204023
 */

public class EnumRetrofitConverterFactory extends Converter.Factory {
    @Override
    public Converter<?, String> stringConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        Converter<?, String> converter = null;
        if (type instanceof Class && ((Class<?>)type).isEnum()) {
            converter = value -> EnumUtils.GetSerializedNameValue((Enum) value);
        }
        return converter;
    }
}

class EnumUtils {
    @Nullable
    static public <E extends Enum<E>> String GetSerializedNameValue(E e) {
        String value = null;
        try {
            value = e.getClass().getField(e.name()).getAnnotation(SerializedName.class).value();
        } catch (NoSuchFieldException exception) {
            exception.printStackTrace();
        }
        return value;
    }
}