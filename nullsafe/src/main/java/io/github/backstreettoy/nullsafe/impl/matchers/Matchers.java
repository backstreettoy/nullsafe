package io.github.backstreettoy.nullsafe.impl.matchers;

public final class Matchers {

    public static PropertyMatcher property(String filedName) {
        return new PropertyMatcher(filedName);
    }

    public static ReturnTypeMatcher returnType(Class<?> clazz) {
        return new ReturnTypeMatcher(clazz);
    }

    public static AnyMatcher any(AbstractMatcher... matchers) {
        throw new UnsupportedOperationException();
    }
}
