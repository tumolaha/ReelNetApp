package com.learning.reelnet.common;

/**
 * Application-wide constants.
 */
public final class ApplicationConstants {
    
    private ApplicationConstants() {
        throw new AssertionError("Utility class should not be instantiated");
    }
    
    /**
     * Constants for security.
     */
    public static final class Security {
        public static final String TOKEN_PREFIX = "Bearer ";
        public static final String HEADER_STRING = "Authorization";
        public static final String ROLE_USER = "ROLE_USER";
        public static final String ROLE_ADMIN = "ROLE_ADMIN";
        public static final String ROLE_MODERATOR = "ROLE_MODERATOR";
        
        private Security() {
            throw new AssertionError("Utility class should not be instantiated");
        }

        public static Object getCurrentUserLogin() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'getCurrentUserLogin'");
        }
    }
    
    /**
     * Constants for profile names.
     */
    public static final class Profiles {
        public static final String DEVELOPMENT = "dev";
        public static final String PRODUCTION = "prod";
        public static final String TEST = "test";
        
        private Profiles() {
            throw new AssertionError("Utility class should not be instantiated");
        }
    }
    
    /**
     * Constants for API paths.
     */
    public static final class Api {
        public static final String API_BASE_PATH = "/api";
        public static final String API_VERSION = "/v1";
        public static final String API_BASE_URL = API_BASE_PATH + API_VERSION;
        
        private Api() {
            throw new AssertionError("Utility class should not be instantiated");
        }
    }
    
    /**
     * Constants for pagination defaults.
     */
    public static final class Pagination {
        public static final int DEFAULT_PAGE_SIZE = 20;
        public static final int MAX_PAGE_SIZE = 100;
        public static final int DEFAULT_PAGE_NUMBER = 0;
        
        private Pagination() {
            throw new AssertionError("Utility class should not be instantiated");
        }
    }
}