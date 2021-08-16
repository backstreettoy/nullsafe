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
