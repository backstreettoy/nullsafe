**If you want to GET RID OF NullPointerException, you could try me.**

**This utility provides a mechanism for the NullPointerException free method invocation.**

# Example

```
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
        
        // It is ok because homeAddress property is not null.
        Assert.assertEquals("Any", user.getHomeAddress().getProvince());

        // Normal it will raise a NPE for the companyAddress property is null.
        // But it won't because nullSafe wrapped the User object and created a
        // stub object for the null property.
        String proviceOfCompany = user.getCompanyAddress().getProvince();
        Assert.assertEquals("", proviceOfCompany);
```

# Usage
The only way you need do is to wrap the target object by NullSafe.safeCall method. 
Besides, you could use additional static method in NullSafe such as evalMatch for convenience.

# How it works
This utility wraps the target object and properties by nested dynamic proxies. 
Each proxy inherits from the class of target object and wrap the target object likes an
automatic Optional class to prevent NullPointerException occur.

This utility won't pollute your data object.
The structure of proxies graph show you how proxies works,
proxy object created as a sub-class of target object class and point to the target object, 
the original object contains data has no knowledge about proxy
for it do not hold any reference to proxy object.

![Structure of proxies](https://github.com/backstreettoy/nullsafe/blob/cbfc229d867581465064ec0f019a123802f26d03/architecture.png)


# Notice
* The current state of this utility is in function previewing stage, it is just for evaluating and demo show.

* The behavior of proxy object could not be all the same as target object, the difference mainly in the result of hashcode, equals, getClass, notify, notifyAll, wait and clone.




