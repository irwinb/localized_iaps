Localized IAPs
==============

Localize your in-app products for Android.


Tutorial
==============

1. Login to your [publisher portal](portal and enter the "In-app Products and Subscriptions" section) and enter the "In-app Products and Subscriptions" section.

2. Chose export to CSV.
![Alt Export to CSV](http://i.imgur.com/OK5xj.png)

3. Place CSV into assets folder.

4. Try it out!
```java
LocalizedIAPs iaps = new LocalizedIAPs( this, "in_app_products.csv", locale );

Map< String, Product > products = iaps.getProducts();
```

License
==============
MIT