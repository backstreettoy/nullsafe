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

# How it works
This utility wraps target object and properties by dynamic proxies nested, and each proxy acts as an automatic Optional class preventing from NullPointerException raising. It is not magic but it can save your time when dealing with many properties potentially nullable.

![Proxy structure](../architecture.png)
