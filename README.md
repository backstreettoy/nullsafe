**If you want to GET RID OF NullPointerException, you could try me. This utility provides a mechanism for the NullPointerException free method invocation. **

# Example

```
Cellphone cellphone = new Cellphone();
// Set the location of cellphone is null.
cellphone.setLocation(null);
Cellphone wrappedCellphone = NullSafe.safeCall(cellphone).get();

// Normally NullPointerException will be raised in getLongitude but here will not.
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
* Wrap the object by the safeCall method and get the wrapped object for evaluation.
* Use the eval method to de-wrap the value from the result of proxy object to get the real result.

# How it works
This utility wraps target object and properties by dynamic proxies nested. Each proxy inherited from the class of target object and wrap the target object acts as an automatic Optional class preventing NullPointerException from raising. 

This utility won't pollute your data object. The structure of proxies graph show you how proxies works, proxy object created as a sub class of target object class and point to the target object and target object has no knowledge about proxy because it won't hold any reference to proxy object.

![Structure of proxies](https://github.com/backstreettoy/nullsafe/blob/cbfc229d867581465064ec0f019a123802f26d03/architecture.png)


# Cautions
* This utility only prevents NullPointerException from raising in the invocation of getter method for that shadowing an exception from raising in write method will result in unexpected result.

* This utility won't work when the target object or method is final because it couldn't be overrided and an exception will raise. For the final classes in JDK such as Integer, some effort was taken so that these classes will behavior normally like non-final classes.

* Like the Optional class, operation must be end with get or orElse method, result of getter method must be de-wrapped by eval method which resolves the real value from proxy object or just return the original value from plain object.

* The behavior of proxy object could not be all the same as target object, the difference mainly in the result of hashcode, equals, getClass, notify, notifyAll, wait and clone.




