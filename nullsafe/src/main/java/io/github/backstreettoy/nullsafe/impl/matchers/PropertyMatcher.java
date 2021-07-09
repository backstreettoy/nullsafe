package io.github.backstreettoy.nullsafe.impl.matchers;

import java.beans.PropertyDescriptor;

public class PropertyMatcher extends AbstractMatcher {

    public PropertyMatcher(String name) {

    }

    @Override
    public boolean match(PropertyDescriptor propertyDescriptor) {
        return false;
    }
}
