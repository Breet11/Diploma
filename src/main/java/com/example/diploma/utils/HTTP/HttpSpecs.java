package com.example.diploma.utils.HTTP;

public final class HttpSpecs {
    public static final String ANY_SUBPATH = "/**";

    private HttpSpecs() {
    }

    public static final class Car {
        public static final String ROOT = "/cars";
        public static final String GET_CATALOG = "";
        public static final String CREATE = "";
        public static final String GET_CATALOG_ENDPOINT = ROOT;
        public static final String CREATE_ENDPOINT = ROOT;

        private Car() {
        }
    }

    public static final class CarBrand {
        public static final String ROOT = "/car-brands";
        public static final String GET_ALL = "";
        public static final String CREATE = "";
        public static final String GET_ALL_ENDPOINT = ROOT;
        public static final String CREATE_ENDPOINT = ROOT;

        private CarBrand() {
        }
    }

    public static final class CarModel {
        public static final String ROOT = "/car-models";
        public static final String GET_ALL = "";
        public static final String CREATE = "";
        public static final String GET_ALL_ENDPOINT = ROOT;
        public static final String CREATE_ENDPOINT = ROOT;

        private CarModel() {
        }
    }

    public static final class CarSpecs {
        public static final String ROOT = "/car-specs";
        public static final String GET_ALL = "";
        public static final String CREATE = "";
        public static final String GET_ALL_ENDPOINT = ROOT;
        public static final String CREATE_ENDPOINT = ROOT;

        private CarSpecs() {
        }
    }

    public static final class Engine {
        public static final String ROOT = "/engines";
        public static final String GET_ALL = "";
        public static final String CREATE = "";
        public static final String GET_ALL_ENDPOINT = ROOT;
        public static final String CREATE_ENDPOINT = ROOT;

        private Engine() {
        }
    }

    public static final class EngineSpecs {
        public static final String ROOT = "/engine-specs";
        public static final String GET_ALL = "";
        public static final String CREATE = "";
        public static final String GET_ALL_ENDPOINT = ROOT;
        public static final String CREATE_ENDPOINT = ROOT;

        private EngineSpecs() {
        }
    }

    public static final class EngineType {
        public static final String ROOT = "/engine-types";
        public static final String GET_ALL = "";
        public static final String CREATE = "";
        public static final String GET_ALL_ENDPOINT = ROOT;
        public static final String CREATE_ENDPOINT = ROOT;

        private EngineType() {
        }
    }

    public static final class LoyaltyRule {
        public static final String ROOT = "/loyalty-rules";
        public static final String GET_ALL = "";
        public static final String CREATE = "";
        public static final String GET_ALL_ENDPOINT = ROOT;
        public static final String CREATE_ENDPOINT = ROOT;

        private LoyaltyRule() {
        }
    }

    public static final class Rental {
        public static final String ROOT = "/rentals";
        public static final String CREATE = "";
        public static final String CALCULATE_PRICE = "/price";
        public static final String GET_ADMIN_ORDERS = "/admin";
        public static final String GET_MY_ORDERS = "/my";
        public static final String UPDATE_STATUS = "/{uuid}/status";
        public static final String CREATE_ENDPOINT = ROOT;
        public static final String CALCULATE_PRICE_ENDPOINT = ROOT + CALCULATE_PRICE;
        public static final String GET_ADMIN_ORDERS_ENDPOINT = ROOT + GET_ADMIN_ORDERS;
        public static final String GET_MY_ORDERS_ENDPOINT = ROOT + GET_MY_ORDERS;
        public static final String UPDATE_STATUS_ENDPOINT = ROOT + UPDATE_STATUS;

        private Rental() {
        }
    }

    public static final class User {
        private User() {
        }

        public static final class Auth {
            public static final String ROOT = "/user";
            public static final String LOGIN = "/login";
            public static final String REGISTER = "/register";
            public static final String LOGIN_ENDPOINT = ROOT + LOGIN;
            public static final String REGISTER_ENDPOINT = ROOT + REGISTER;

            private Auth() {
            }
        }

        public static final class Profile {
            public static final String ROOT = "/profile";
            public static final String ME = "/me";
            public static final String ME_ENDPOINT = ROOT + ME;
            public static final String ALL_ENDPOINTS = ROOT + ANY_SUBPATH;

            private Profile() {
            }
        }

        public static final class Management {
            public static final String ROOT = "/users";
            public static final String ROOT_ENDPOINT = ROOT;

            private Management() {
            }
        }
    }

    public static final class Admin {
        public static final String ROOT = "/admin";
        public static final String ALL_ENDPOINTS = ROOT + ANY_SUBPATH;

        private Admin() {
        }
    }
}
