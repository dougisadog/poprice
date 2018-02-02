package poprice.wechat.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    private AuthoritiesConstants() {
    }

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String SERVICE = "ROLE_SERVICE";

    public static final String FINANCE = "ROLE_FINANCE";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
}
