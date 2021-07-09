package io.github.backstreettoy.nullsafe.impl.matchers;

import java.beans.PropertyDescriptor;

public abstract class AbstractMatcher {

    public abstract boolean match(PropertyDescriptor propertyDescriptor);
}
