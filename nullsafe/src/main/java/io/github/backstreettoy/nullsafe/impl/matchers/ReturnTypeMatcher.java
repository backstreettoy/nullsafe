package io.github.backstreettoy.nullsafe.impl.matchers;

import java.beans.PropertyDescriptor;

public class ReturnTypeMatcher extends AbstractMatcher {

    public ReturnTypeMatcher(Class<?> clazz) {

    }

    @Override
    public boolean match(PropertyDescriptor propertyDescriptor) {
        return false;
    }
}
