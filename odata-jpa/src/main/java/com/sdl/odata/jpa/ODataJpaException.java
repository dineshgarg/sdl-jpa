package com.sdl.odata.jpa;

import com.sdl.odata.api.ODataErrorCode;
import com.sdl.odata.api.ODataException;

/**
 * OData JPA project base exception.
 */
public class ODataJpaException extends ODataException {

    public ODataJpaException(String message) {
        super(ODataErrorCode.UNKNOWN_ERROR, message);
    }

    public ODataJpaException(String message, Throwable e) {
        super(ODataErrorCode.UNKNOWN_ERROR, message, e);
    }
}
