package com.sdl.odata.jpa;

import com.sdl.odata.api.processor.datasource.ODataDataSourceException;

/**
 * OData JPA project base exception.
 */
public class ODataJpaException extends ODataDataSourceException {

    public ODataJpaException(String message) {
        super(message);
    }

    public ODataJpaException(String message, Throwable e) {
        super(message, e);
    }
}
