package com.sdl.odata.jpa;

import com.sdl.odata.api.parser.TargetType;
import com.sdl.odata.api.service.ODataRequestContext;

public class EdmUtil {

    public static Class<?> getEdmEntityClass(ODataRequestContext requestContext, TargetType targetType) {
		String typeName = targetType.typeName();
		return requestContext.getEntityDataModel().getType(typeName).getJavaType();
    }
}
