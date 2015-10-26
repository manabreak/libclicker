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
     * Tick rate (how much should the world advance when update() is called)
     */
    private double m_tickRate;
    
    /**
     * Active generators
     */
    private ArrayList<Generator> m_generators = new ArrayList<Generator>();
    
    /**
     * Global multiplier (per second) which is applied
 as the final step during each update
     */
    private double m_globalMultiplierPerSec = 1.0;
    
    /**
     * List of active automators
     */
    private ArrayList<Automator> m_automators = new ArrayList<Automator>();
    
    /**
     * Currencies in use
     */
    private ArrayList<Currency> m_currencies = new ArrayList<Currency>();
    
    /**
     * Constructs a new world with a default update rate of 1/60th of a second
     */
    public World()
    {
        m_tickRate = 1f / 60f;
    }

    /**
     * Constructs a new world.
     * 
     * @param tickRate Tick rate
     */
    public World(double tickRate)
    {
        m_tickRate = tickRate;
    }
    
    /**
     * Returns the current update rate of the world.
     * @return The update rate of the world
     */
    public double getTickRate()
    {
        return m_tickRate;
    }

    /**
     * Sets the update rate of the world.
     * 
     * @param tickRate Tick rate, in seconds
     */
    public void setTickRate(double tickRate)
    {
        m_tickRate = tickRate;
    }

    /**
     * Adds a new generator to this world
     * @param generator Generator to add
     */
    public void addGenerator(Generator generator)
    {
        if(generator != null && !m_generators.contains(generator))
        {
            m_generators.add(generator);
            generator.onAdd(this);
        }
    }

    /**
     * Returns the number of generators in this world
     * @return The number of generators in this world
     */
    public Object getGeneratorCount()
    {
        return m_generators.size();
    }

    /**
     * Removes a generator
     * @param generator Generator to remove
     */
    public void removeGenerator(Generator generator)
    {
        if(generator != null && m_generators.contains(generator))
        {
            generator.onRemove(this);
            m_generators.remove(generator);
        }
    }
    
    /**
     * Removes all the generators from this world
     */
    public void removeAllGenerators()
    {
        m_generators.clear();
    }
    
    public void addCurrency(Currency c)
    {
        if(c != null && !m_currencies.contains(c))
        {
            m_currencies.add(c);
        }
    }
    
    public void removeCurrency(Currency c)
    {
        if(c != null)
        {
            m_currencies.remove(c);
        }
    }
    
    public Currency getCurrency(int index)
    {
        return m_currencies.get(index);
    }
    
    public List<Currency> getCurrencies()
    {
        return m_currencies;
    }
    
    public void removeAllCurrencies()
    {
        m_currencies.clear();
    }
    
    /**
     * Advances the world state by the default update rate.
     */
    public void update()
    {
        update(m_tickRate);
    }
    
    /**
     * Advances the world state by the given amount of seconds.
     * Useful when calculating away-from-keyboard income etc.
     * 
     * @param seconds Seconds to advance
     */
    public void update(double seconds)
    {
        for(Automator a : m_automators)
        {
            a.update(seconds);
        }
    }

    public void addAutomator(Automator automator)
    {
        if(automator != null && !m_automators.contains(automator))
        {
            m_automators.add(automator);
        }
    }
}
