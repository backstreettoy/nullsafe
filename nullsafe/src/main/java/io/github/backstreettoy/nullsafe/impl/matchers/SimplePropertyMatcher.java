package io.github.backstreettoy.nullsafe.impl.matchers;

import java.beans.PropertyDescriptor;

public class SimplePropertyMatcher extends AbstractMatcher {

    private String name;

    public SimplePropertyMatcher(String name) {
        if (name == null) {
            throw new NullPointerException("name is null");
        }
        this.name = name;
    }

    @Override
    public boolean match(PropertyDescriptor propertyDescriptor) {
        return name.equals(propertyDescriptor.getName());
    }
}
