package io.github.backstreettoy.nullsafe.impl.matchers;

import java.beans.PropertyDescriptor;

public class AnyMatcher extends AbstractMatcher {

    @Override
    public boolean match(PropertyDescriptor propertyDescriptor) {
        return false;
    }
}
