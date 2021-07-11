package io.github.backstreettoy.nullsafe.impl.matchers;

import java.beans.PropertyDescriptor;

public class ReturnTypeMatcher extends AbstractMatcher {

    private Class<?> clazz;

    public ReturnTypeMatcher(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean match(PropertyDescriptor propertyDescriptor) {
        Class<?> propertyType = propertyDescriptor.getPropertyType();
        return clazz.isAssignableFrom(propertyType);
    }
}
