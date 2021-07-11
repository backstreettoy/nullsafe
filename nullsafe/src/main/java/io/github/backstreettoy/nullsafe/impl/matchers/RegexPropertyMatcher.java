package io.github.backstreettoy.nullsafe.impl.matchers;

import java.beans.PropertyDescriptor;

/**
 * @author backstreettoy
 */
public class RegexPropertyMatcher extends AbstractMatcher {

    private String expression;

    public RegexPropertyMatcher(String expression) {
        this.expression = expression;
    }

    @Override
    public boolean match(PropertyDescriptor propertyDescriptor) {
        return propertyDescriptor.getName().matches(expression);
    }
}
