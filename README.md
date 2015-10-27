# libclicker
Libclicker is a library for creating idle / procedural / clicker games.
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

### Currency formatters

You can query the amount of currency by calling its `getAmountAsString()` method, but
this produces a rather ugly output that may not even fit on the screen.
Instead, you can create a currency formatter to produce a nice output:

```java
CurrencyFormatter printGold = new CurrencyFormatter.Builder(gold)
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

In the near future, names (thousands, millions, billions etc.) and shorthands
(K, M, B, T...) will be implemented to the currency formatter.