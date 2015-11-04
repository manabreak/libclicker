# libclicker
Libclicker is a library for creating idle / incremental / clicker games.
It features resources, resource generators, automators for generators and
modifiers, as well as utilities e.g. for presentation.

## Usage

Here's a basic introduction on how to use libclicker.

### World

First off, you will need a `World` container for all your objects.
A `World` object will keep track of the generators, automators and other
items.

```java
World world = new World();
```

To update the world as your game progresses, call the `update()` method
with your frame time as the parameter:

```java
// Advance the world by 1/60th of a seconds, or 60 times per second
world.update(1.0 / 60.0);
```
### Currencies

Quite often, procedural games have some sort of resources (gold, cookies etc.).
To declare a new currency, you can use a `Currency.Builder`:

```java
// Creates a new currency called "Gold"
Currency gold = new Currency.Builder(world)
                         .name("Gold")
                         .build();
```

The whole system will only have one instance of a currency at a time. The
currency object will keep track of how much of the said currency has been
generated.

### Generators

To produce a currency, you have to create a generator. Again, like currencies,
this happens through a Builder class:

```java
// Creates a generator that creates gold
Generator goldMine = new Generator.Builder(world)
      .generate(gold)   // Generate gold
      .baseAmount(10)   // Defaults to 10 gold per tick
      .multiplier(1.15) // Increase amount by 15 % per level
      .price(100)       // Price of level 1 gold mine
      .priceMultiplier(1.25) // Increase price by 25 % per level
      .build();

// Manually generate some gold
goldMine.process();
```

Do note that generators start at level 0, producing nothing. To activate the
generator, "upgrade" it to level one to produce the base amount:

```java
// Upgrade the gold mine to level 1
goldMine.upgrade();
```

### Automators

Commonly generators can be automated in clicker games. To automate the gold mine
from the previous example:

```java
// Create a "gold digger" to automatically dig gold
Automator goldDigger = new Automator.Builder(world)
      .automate(goldMine)
      .every(3.0) // Tick every three seconds
      .build();
      
// Advance the world by 30 seconds to make the automator work
world.update(30.0);
```

### Modifiers

Yet another common thing found in procedural games is the use of upgrades, bonuses
or other modifiers that change the outcome of the system in some way. Modifiers
are supported in libclicker and can be applied to any other elements, modifying
just about any of their properties (at least in the near future).

The usage of modifiers follows the same pattern:

```java
// World to modify
World w = new World();

// Modify the world by creating a "double speed bonus" modifiers
Modifier m = new Modifier.Builder()
      .modify(w)
      .speedBy(2.0)
      .build();
      
// By enabling the modifier, the speed bonus turns on
m.enable();

// World is now actually advanced by 20 seconds instead of 10
w.update(10.0);

// Let's disable the modifier ("back to normal")
m.disable();

// Back to normal 10-second updates
w.update(10.0);
```

Note that multiple modifiers can be stacked:

```java
// Double speed bonus
Modifier m1 = new Modifier.Builder()
      .modify(w)
      .speedBy(2.0)
      .build();

// Triple speed bonus
Modifier m2 = new Modifier.Builder()
      .modify(w)
      .speedBy(3.0)
      .build();
      
// Enable both bonuses, result is 6X speed bonus
m1.enable();
m2.enable();

// World advances 6 seconds now, instead of 1
w.update(1.0);

// You can disable a single modifier and leave the other on
m1.disable();

// Only the triple bonus is now enabled --> 3 seconds advancement
w.update(1.0);
```

The modifier support is still pretty small, but more modifiers and
modifiable properties will be available in the near future.

### Purchasing items

Every item starts from level 0, meaning they don't "do" anything.
You can query the price of an item by calling its `getPrice()` method:

```java
Item item = ...;

// Query the price of the NEXT level of the item
BigInteger price = item.getPrice();
```

To upgrade an item with currency, call the item's `buyWith()` method:

```java
Currency gold = ...;
Item item = ...;

PurchaseResult result = item.buyWith(gold);
```

The returned `PurchaseResult` is an enum denoting the result. The
possible out outcomes are:

- `PurchaseResult.OK` when the item was successfully purchased or upgraded and the money was deducted

- `PurchaseResult.INSUFFICIENT_FUNDS` when there was not enough money to buy the item

- `PurchaseResult.MAX_LEVEL_REACHED` when the item has already reached its max level and cannot be upgraded any further

### Formatters

You can query the amount of currency by calling its `getAmountAsString()` method, but
this produces a rather ugly output that may not even fit on the screen.
Instead, you can create a currency formatter to produce a nice output:

```java
Formatter printGold = new Formatter.ForCurrency(gold)
      .groupDigits() // Group digits into groups of three
      .showHighestThousand() // Show only the highest triplet in the amount
      .showDecimals(2) // Show two decimals if "truncating" lower numbers
      .build();
      
// The formatter is now ready and prints out the current amount of gold
System.out.println("Gold now: " + printGold);
```

In this example, if there's 123,456,789 gold, the output would be:

```java
Gold now: 123.45
```

You usually want to use some indicators about the multitude of the currency (K for thousand,
M for millions etc.). You can do this by supplying an array of "names" for each "thousand":

```java
Formatter printGold = new Formatter.ForCurrency(gold)
      .groupDigits()
      .showHighestThousand()
      .showDecimals(2)
      .useAbbreviations(new String[] {"K", "M", "B", "T"})
      .build();
      
System.out.println("Gold now: " + printGold);
```

In this case, if there's 123,456,789 gold, the output would be:

```java
Gold now: 123.45M
```

If the amount of currency is higher than the amount of abbreviations supplied,
the abbreviation will be omitted. Similarly, if the amount is less than 1,000,
no abbreviation will be added.

You can also format other things, e.g. the price of an item:

```java
Formatter printItemPrice = new Formatter.ForItemPrice(item)
      .build();
```