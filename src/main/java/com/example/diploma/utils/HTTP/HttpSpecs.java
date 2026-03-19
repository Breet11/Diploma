package com.example.diploma.utils.HTTP;

import org.springframework.http.HttpMethod;

public class HttpSpecs {
    public static String REST = "rest/";
    public static class Car {

    }

    public static class CarBrand {

    }

    public static class CarModel {

    }

    public static class CarSpecs {

    }

    public static class Engine {

    }

    public static class EngineType {

    }

    public static class User {
        private static final String FEATURE_PATH = REST + "user/";
        public static class Auth{
            public static final HttpMethod HTTP_METHOD = HttpMethod.POST;
            public static final String PATH = FEATURE_PATH ;
            private Auth(){}
        }
    }
}
