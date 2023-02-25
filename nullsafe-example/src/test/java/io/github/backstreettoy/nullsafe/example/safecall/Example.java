package io.github.backstreettoy.nullsafe.example.safecall;

import org.junit.Assert;

import io.github.backstreettoy.nullsafe.NullSafe;
import io.github.backstreettoy.nullsafe.example.dataobject.Address;
import io.github.backstreettoy.nullsafe.example.dataobject.User;

/**
 * @author backstreettoy
 */
public class Example {

    public static void main(String[] args) {
        // Creating a test data object.
        User user = new User();
        user.setId(1L);
        user.setUserName(null);

        // Notice the difference between the homeAddress and companyAddress properties.
        Address homeAddress = new Address();
        homeAddress.setProvince("Any");
        homeAddress.setStreet("Eastern Road");
        user.setHomeAddress(homeAddress);

        user.setCompanyAddress(null);

        // Wrap the original object by nullsafe.
        user = NullSafe.safeCall(user).get();

        Assert.assertEquals(1d, (double)user.getId(), 0d);
        // It is ok because homeAddress property is not null.
        String provinceOfHomeAddress = user.getHomeAddress().getProvince();
        // Normal it will raise a NPE for the companyAddress property is null.
        // But it won't because nullSafe wrapped the User object and created a
        // stub object for the null property.
        String proviceOfCompany = user.getCompanyAddress().getProvince();
        Assert.assertEquals("", proviceOfCompany);
    }
}
