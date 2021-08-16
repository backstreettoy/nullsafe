**If you want to GET RID OF NullPointerException, you could try me.**
 


# Example


```
Cellphone cellphone = new Cellphone();
// Set the location of cellphone is null.
cellphone.setLocation(null);
Cellphone wrappedCellphone = NullSafe.safeCall(cellphone).get();

System.out.printf("Longitude:%.5f\n", NullSafe.eval(wrappedCellphone.getLocation().getLongitude()));
System.out.printf("Latitude:%.5f\n", NullSafe.eval(wrappedCellphone.getLocation().getLatitude()));
System.out.printf("Location exists:%s\n", NullSafe.evalMatch(wrappedCellphone.getLocation(), x -> x != null));
```
The output is

```
Longitude:null
Latitude:null
Location exists:false
```
# Usage
All utility methods provided in NullSafe class in fluent style, all you need do is to follow the two steps:
* Wrap the object by the safeCall and get the wrapped object.
* Get the real result from the wrapped object by eval method.

# How it works
This utility wraps target object and properties by dynamic proxies nested, and each proxy acts as an automatic Optional class preventing NullPointerException from  raising. It is not magic but it can save your time when dealing with many properties potentially nullable.

This utility won't pollute your data object. The structure of proxies graph show you how proxies works, proxy object created as a sub class of target object class and point to the target object and not vice versa, target object has no knowledge about proxy because it won't hold any reference to proxy object.

![Structure of proxies](https://github.com/backstreettoy/nullsafe/blob/cbfc229d867581465064ec0f019a123802f26d03/architecture.png)

This utility only prevents NullPointerException from raising in the invocation of getter method for that shadowing an exception from raising in write method will result in unexpected result.

# Cautions
* This utility won't work when the target object or method is final because it couldn't be overrided so that exception will raise if a final object passed to the safeCall method. However lots of class in JDK such as Integer is final and we take an effort to solve the problem for these final classes in JDK to make the utility more usable.

* Like the Optional class there must be end with get or orElse method, you must get the real result by eval method.



