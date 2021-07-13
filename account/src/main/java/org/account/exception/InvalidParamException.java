package org.account.exception;


public class InvalidParamException extends Exception {

    private static final long serialVersionUID = 1L;

    private static final String MSG_FORMAT = "Invalid param, %s=%s due to %s";


    public InvalidParamException(String name, String value, String msg) {
        super(String.format(MSG_FORMAT, name, value, msg));
    }
}
