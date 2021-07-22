package io.github.backstreettoy.nullsafe.impl.proxywrap;

/**
 * @author backstreettoy
 */
public interface SafeCallWrapped {

    void __impl(Object target);

    Object __getimpl();
}
