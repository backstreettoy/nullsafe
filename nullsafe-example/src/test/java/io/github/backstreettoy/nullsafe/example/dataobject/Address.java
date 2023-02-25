package io.github.backstreettoy.nullsafe.example.dataobject;

/**
 * @author backstreettoy
 */
public class Address {
    private String street;

    private String province;

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
