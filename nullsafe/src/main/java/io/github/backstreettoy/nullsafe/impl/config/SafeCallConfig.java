package io.github.backstreettoy.nullsafe.impl.config;

/**
 * @author backstreettoy
 */
public class SafeCallConfig implements Cloneable {


    private boolean throwExceptionWhenWrapMethodFail;

    public boolean isThrowExceptionWhenWrapMethodFail() {
        return throwExceptionWhenWrapMethodFail;
    }

    public void setThrowExceptionWhenWrapMethodFail(boolean throwExceptionWhenWrapMethodFail) {
        this.throwExceptionWhenWrapMethodFail = throwExceptionWhenWrapMethodFail;
    }

    @Override
    public SafeCallConfig clone() throws CloneNotSupportedException {
        SafeCallConfig config = (SafeCallConfig) super.clone();
        config.setThrowExceptionWhenWrapMethodFail(this.throwExceptionWhenWrapMethodFail);
        return config;
    }
}
