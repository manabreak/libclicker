/*
 * The MIT License
 *
 * Copyright 2015 Harri Pellikka.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.manabreak.libclicker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A container for all the clicker objects
 *
 * @author Harri Pellikka
 */
public class World implements Serializable
{
    /**
     * Active generators
     */
    private ArrayList<Generator> mGenerators = new ArrayList<>();
    
    /**
     * Active automators
     */
    private ArrayList<Automator> mAutomators = new ArrayList<>();
    
    /**
     * Currencies in use
     */
    private ArrayList<Currency> mCurrencies = new ArrayList<>();
    
    /**
     * Modifiers in use
     */
    private ArrayList<Modifier> mModifiers = new ArrayList<>();
    
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

    void removeAutomator(Automator automator)
    {
        if(automator != null)
        {
            mAutomators.remove(automator);
        }
    }

    List<Automator> getAutomators()
    {
        return mAutomators;
    }
    
    List<Modifier> getModifiers()
    {
        return mModifiers;
    }
    
    void removeModifier(Modifier modifier)
    {
        if(modifier != null)
        {
            mModifiers.remove(modifier);
        }
    }
    
    boolean isAutomationEnabled()
    {
        return mUpdateAutomators;
    }
}
