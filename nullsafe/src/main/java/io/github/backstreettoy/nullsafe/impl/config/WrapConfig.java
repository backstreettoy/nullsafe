package io.github.backstreettoy.nullsafe.impl.config;

/**
 * @author backstreettoy
 */
public class WrapConfig implements Cloneable {

    private boolean enableConvert;
    private boolean silent;

    public boolean isEnableConvert() {
        return enableConvert;
    }

    public void setEnableConvert(boolean enableConvert) {
        this.enableConvert = enableConvert;
    }

    public boolean isSilent() {
        return silent;
    }

    public void setSilent(boolean silent) {
        this.silent = silent;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        WrapConfig copy = (WrapConfig)super.clone();
        copy.setSilent(silent);
        copy.setEnableConvert(enableConvert);
        return copy;
    }
}
