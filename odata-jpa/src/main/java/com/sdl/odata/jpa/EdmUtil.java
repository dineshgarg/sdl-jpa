package com.sdl.odata.jpa;

import com.sdl.odata.api.edm.model.EntityDataModel;
import com.sdl.odata.api.parser.TargetType;
import com.sdl.odata.api.service.ODataRequestContext;

public class EdmUtil {

	public static Class<?> getEdmEntityClass(ODataRequestContext requestContext, TargetType targetType) {
		EntityDataModel edm = requestContext.getEntityDataModel();
		String typeName = targetType.typeName();
		Class<?> edmClass = edm.getType(typeName).getJavaType();
		return edmClass;
	}

}
