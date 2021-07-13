package org.customer.exception;


public class CustomerNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;

    private static final String MSG_FORMAT = "Customer with id, %s not found";


    public CustomerNotFoundException(Long id) {
        super(String.format(MSG_FORMAT, id));
    }
}
