package com.manabreak.libclicker;

import java.util.ArrayList;
import java.util.List;

/**
 * A container for all the clicker objects
 *
 * @author Harri Pellikka
 */
public class World
{
    /**
     * Active generators
     */
    private final ArrayList<Generator> mGenerators = new ArrayList<>();
    
    /**
     * Active automators
     */
    private final ArrayList<Automator> mAutomators = new ArrayList<>();
    
    /**
     * Currencies in use
     */
    private final ArrayList<Currency> mCurrencies = new ArrayList<>();
    
    /**
     * Modifiers in use
     */
    private final ArrayList<Modifier> mModifiers = new ArrayList<>();
    
    /**
     * Speed multiplier - used to multiply the time the world advances
     */
    private double mSpeedMultiplier = 1.0;
    
    /**
     * Should automators be updated?
     */
    private boolean mUpdateAutomators = true;
    
    /**
     * Constructs a new world. All the other components require an existing
     * "world" to function. A world is a container for the whole system.
     */
    public World()
    {
        
    }

    /**
     * Adds a new generator to this world
     * @param generator Generator to add
     */
    void addGenerator(Generator generator)
    {
        if(generator != null && !mGenerators.contains(generator))
        {
            mGenerators.add(generator);
            generator.onAdd(this);
        }
    }

    /**
     * Returns the number of generators in this world
     * @return The number of generators in this world
     */
    public int getGeneratorCount()
    {
        return mGenerators.size();
    }

    /**
     * Removes a generator
     * @param generator Generator to remove
     */
    void removeGenerator(Generator generator)
    {
        if(generator != null && mGenerators.contains(generator))
        {
            generator.onRemove(this);
            mGenerators.remove(generator);
        }
    }
    
    /**
     * Removes all the generators from this world
     */
    void removeAllGenerators()
    {
        mGenerators.clear();
    }
    
    void addCurrency(Currency c)
    {
        if(c != null && !mCurrencies.contains(c))
        {
            mCurrencies.add(c);
        }
    }
    
    void removeCurrency(Currency c)
    {
        if(c != null)
        {
            mCurrencies.remove(c);
        }
    }
    
    Currency getCurrency(int index)
    {
        return mCurrencies.get(index);
    }
    
    List<Currency> getCurrencies()
    {
        return mCurrencies;
    }
    
    void removeAllCurrencies()
    {
        mCurrencies.clear();
    }
    
    /**
     * Advances the world state by the given amount of seconds.
     * Useful when calculating away-from-keyboard income etc.
     * 
     * @param seconds Seconds to advance
     */
    public void update(double seconds)
    {
        seconds *= mSpeedMultiplier;
        
        if(mUpdateAutomators)
        {
            for(Automator a : mAutomators)
            {
                a.update(seconds);
            }
        }
    }

    void addAutomator(Automator automator)
    {
        if(automator != null && !mAutomators.contains(automator))
        {
            mAutomators.add(automator);
        }
    }
    
    void addModifier(Modifier modifier)
    {
        if(modifier != null && !mModifiers.contains(modifier))
        {
            mModifiers.add(modifier);
        }
    }

    double getSpeedMultiplier()
    {
        return mSpeedMultiplier;
    }

    void setSpeedMultiplier(double multiplier)
    {
        mSpeedMultiplier = multiplier;
    }

    void disableAutomators()
    {
        mUpdateAutomators = false;
    }
    
    void enableAutomators()
    {
        mUpdateAutomators = true;
    }
}
