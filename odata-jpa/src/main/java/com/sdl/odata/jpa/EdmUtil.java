package com.sdl.odata.jpa;

import com.sdl.odata.api.parser.TargetType;
import com.sdl.odata.api.service.ODataRequestContext;

public class EdmUtil {

    public static Class<?> getEdmEntityClass(ODataRequestContext requestContext, TargetType targetType) {
        return getEdmEntityClass(requestContext, targetType.typeName());
    }

    public static Class<?> getEdmEntityClass(ODataRequestContext requestContext, String typeName) {
		return requestContext.getEntityDataModel().getType(typeName).getJavaType();
    }
}
