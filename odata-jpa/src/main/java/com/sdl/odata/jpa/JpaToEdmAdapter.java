package com.sdl.odata.jpa;

import com.sdl.odata.jpa.annotation.JPAField;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class JpaToEdmAdapter {

	private final Class<?> jpaEntityClass;
	private final Class<?> edmEntityClass;

	public JpaToEdmAdapter(Class<?> jpaEntityClass, Class<?> edmEntityClass) {
		this.jpaEntityClass = jpaEntityClass;
		this.edmEntityClass = edmEntityClass;
	}

	public List<Object> toEdmEntities(List<?> jpaEntities) throws ODataJpaException {
		List<Object> edmEntities = new ArrayList<>();

		for (Object jpaEntity : jpaEntities) {

			if (jpaEntity.getClass() != jpaEntityClass) {
				throw new ODataJpaException("Invalid query response");
			}

			try {
				Object edmEntity = edmEntityClass.newInstance();
				for (Field edmField : edmEntityClass.getDeclaredFields()) {
					mapFieldValueToEdmEntity(jpaEntity, edmEntity, edmField);
				}
				edmEntities.add(edmEntity);
			} catch (Exception e) {
				// Ignore exceptions will just not call the setter. TODO check
				// with Oleg
			}
		}

		return edmEntities;
	}

	private void mapFieldValueToEdmEntity(Object jpaEntity, Object edmEntity, Field edmField) {

		try {
			JPAField jpaFieldAnnotation = edmField.getAnnotation(JPAField.class);

			if (jpaFieldAnnotation == null) {
				// same field should be present in the JPA Entity as no
				// annotation is specified

				String fieldName = StringUtils.capitalize(edmField.getName());
				String getterMethodName = edmField.getType() == Boolean.class ? "is" + fieldName : "get" + fieldName;
				String setterMethodName = "set" + fieldName;
				Method jpaMethod = jpaEntityClass.getMethod(getterMethodName);
				Object value = jpaMethod.invoke(jpaEntity);
				Method edmMethod = edmEntityClass.getMethod(setterMethodName, edmField.getType());
				edmMethod.invoke(edmEntity, value);

			} else {

				// scenario like city/name where child entity in JPA in
				// mapped to top level
				// entity in EDM.
				String fieldPath = jpaFieldAnnotation.path();
				String[] pathElements = fieldPath.split("/");

				// first element in the path should be present in the
				// top level JPA Entity
				Class<?> currentClass = jpaEntityClass;
				Object value = jpaEntity;
				for (int i = 0; i < pathElements.length; i++) {
					String fieldName = StringUtils.capitalize(pathElements[i]);

					// would be a complex type until the last
					// element so getter should start with 'get'
					String getterMethodName = "get" + fieldName;

					if (i == pathElements.length - 1) {

						// last element type should match the edmField type.
						// TODO check with Oleg if we want to
						// support different types in EDM versus JPA.
                        // In that case we would need to support type conversion.

						// here we will be returning a primitive value so we should
						// do the check for boolean to determine the correct method name.
						if (edmField.getType() == Boolean.class) {
							getterMethodName = "is" + fieldName;
						}
					}

					Method jpaMethod = currentClass.getMethod(getterMethodName);
					value = jpaMethod.invoke(value);
					currentClass = value.getClass();
				}

				String edmFieldName = StringUtils.capitalize(edmField.getName());
				String setterMethodName = "set" + edmFieldName;
				Method edmMethod = edmEntityClass.getMethod(setterMethodName, edmField.getType());
				edmMethod.invoke(edmEntity, value);
			}

		} catch (Exception e) {
			// log a warning here that the field could not be resolved.
			// not all fields will have getters.
			// Example persons in City.
			// TODO should we define another annotation to check
			// if the attribute is mapped at all?
		}
	}
}
