package io.github.backstreettoy.nullsafe.impl.matchers;

public final class Matchers {

    public static SimplePropertyMatcher property(String filedName) {
        return new SimplePropertyMatcher(filedName);
    }

    /**
     * Match if property name matches regular expression.
     * @param regex regular expression
     * @return
     */
    public static RegexPropertyMatcher propertyMatch(String regex) {
        return new RegexPropertyMatcher(regex);
    }

    public static ReturnTypeMatcher returnType(Class<?> clazz) {
        return new ReturnTypeMatcher(clazz);
    }

    public static AnyMatcher any(AbstractMatcher... matchers) {
        throw new UnsupportedOperationException();
    }
}
