package org.account.util;

import java.net.URI;
import java.net.URISyntaxException;

public final class Util {

    private Util() {
    }

    public static URI newURI(String uriPath) {
        try {
            return new URI(uriPath);
        } catch (URISyntaxException urise) {
            // throw as runtime exception since should not happen
            throw new IllegalArgumentException("Invalid URI path " + uriPath, urise);
        }
    }
}
