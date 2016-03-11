package org.xdevs23.net.http;

/**
 * This is what you looked for!
 * Converts an error code to a string message
 */
public class HttpStatusCodeHelper {

    /**
     * HTTP Status codes according to Wikipedia
     */
    public enum HttpStatusCode {

        // Information
        CONTINUE                                (100),
        SWITCHING_PROTOCOLS                     (101),
        PROCESSING                              (102),

        // Successful operation
        SUCCESS_OK                              (200),  // Default
        SUCCESS_CREATED                         (201),
        SUCCESS_ACCEPTED                        (202),
        SUCCESS_NON_AUTHORITATIVE_INFORMATION   (203),
        SUCCESS_NO_CONTENT                      (204),
        SUCCESS_RESET_CONTENT                   (205),
        SUCCESS_PARTIAL_CONTENT                 (206),
        SUCCESS_MULTI_STATUS                    (207),
        SUCCESS_ALREADY_REPORTED                (208),
        SUCCESS_IM_USED                         (226),

        // Redirection
        REDIRECT_MULTIPLE_CHOICES               (300),
        REDIRECT_MOVED_PERMANENTLY              (301),
        REDIRECT_FOUND                          (302),
        REDIRECT_SEE_OTHER                      (303),
        REDIRECT_NOT_MODIFIED                   (304),
        REDIRECT_USE_PROXY                      (305),
        @Deprecated
        REDIRECT_SWITCH_PROXY                   (306),  // Not used anymore, is now reserved
        REDIRECT_TEMPORARY                      (307),
        REDIRECT_PERMANENT                      (308),

        // Client error
        ERROR_BAD_REQUEST                       (400),
        ERROR_UNAUTHORIZED                      (401),
        ERROR_PAYMENT_REQUIRED                  (402),
        ERROR_FORBIDDEN                         (403),
        ERROR_NOT_FOUND                         (404),  // Popular
        ERROR_METHOD_NOT_ALLOWED                (405),
        ERROR_NOT_ACCEPTABLE                    (406),
        ERROR_PROXY_AUTHENTICATION_REQUIRED     (407),
        ERROR_REQUEST_TIME_OUT                  (408),
        ERROR_CONFLICT                          (409),
        ERROR_GONE                              (410),
        ERROR_LENGTH_REQUIRED                   (411),
        ERROR_PRECONDITION_FAILED               (412),
        ERROR_REQUEST_ENTITY_TOO_LARGE          (413),
        ERROR_REQUEST_URL_TOO_LONG              (414),
        ERROR_UNSUPPORTED_MEDIA_TYPE            (415),
        ERROR_REQUESTED_RANGE_NOT_SATISFIABLE   (416),
        ERROR_EXPECTATION_FAILED                (417),
        ERROR_IM_A_TEAPOT                       (418),  // :D (is HTCPCP april fool)
        ERROR_POLICY_NOT_FULFILLED              (420),
        ERROR_MISDIRECTED_REQUEST               (421),
        ERROR_UNPROCESSABLE_ENTITY              (422),
        ERROR_LOCKED                            (423),
        ERROR_FAILED_DEPENDENCY                 (424),
        ERROR_UNORDERED_COLLECTION              (425),
        ERROR_UPGRADE_REQUIRED                  (426),  // For TLS
        ERROR_PRECONDITION_REQUIRED             (428),
        ERROR_TOO_MANY_REQUESTS                 (429),  // o_O DDOS?
        ERROR_REQUEST_HEADER_FIELDS_TOO_LARGE   (431),
        ERROR_UNAVAILABLE_FOR_LEGAL_REASONS     (451),  // NEW! (17 Dec 2015)
        ERROR_NO_RESPONSE                       (444),  // For nginx
        ERROR_REQ_ONLY_AFTER_APPROPRIATE_ACTION (449),  // For MS Exchange Server

        // Server error
        INTERNAL_SERVER_ERROR                   (500),
        NOT_IMPLEMENTED                         (501),
        BAD_GATEWAY                             (502),
        SERVICE_UNAVAILABLE                     (503),
        GATEWAY_TIMEOUT                         (504),
        HTTP_VERSION_NOT_SUPPORTED              (505),
        VARIANT_ALSO_NEGOTIATES                 (506),
        SERVER_INSUFFICIENT_STORAGE             (507),
        SERVER_ERROR_LOOP_DETECTED              (508),
        SERVER_BANDWIDTH_LIMIT_EXCEEDED         (509),
        SERVER_NOT_EXTENDED                     (510),

        // Other (these are not HTTP status codes)
        ERR_NAME_NOT_RESOLVED                   (100800),
        DNS_PROBE_FINISHED_NO_INTERNET          (100850),
        DNS_NOT_REACHABLE                       (100851),
        ERR_NO_INTERNET                         (100900),
        ERR_BAD_URL                             (100960),
        ERR_AUTHENTICATION                      (100980),
        ERR_CONNECT                             (100984),
        ERR_UNSUPPORTED_AUTH_SCHEME             (120100),
        ERR_UNSUPPORTED_SCHEME                  (120101),
        ERR_FAILED_SSL_HANDSHAKE                (500551),
        ERR_FILE                                (685113),
        IO_ERROR                                (101013),
        TOO_MANY_REDIRECTS                      (990099),

        UNKNOWN(0),
        OTHER(Integer.MIN_VALUE),
        NONE (Integer.MAX_VALUE)

        ;


        private int statusCode;

        HttpStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public int getNumVal() {
            return this.statusCode;
        }

        @Override
        public String toString() {
            return this.name();
        }

    }

    public static String getStatusCodeString(int code) {
        for ( HttpStatusCode c : HttpStatusCode.values() )
            if(c.getNumVal() == code) return c.toString();

        return HttpStatusCode.UNKNOWN.toString();
    }

}
