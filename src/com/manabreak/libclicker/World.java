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
    private final ArrayList<Generator> m_generators = new ArrayList<>();
    
    /**
     * Active automators
     */
    private final ArrayList<Automator> m_automators = new ArrayList<>();
    
    /**
     * Currencies in use
     */
    private final ArrayList<Currency> m_currencies = new ArrayList<>();
    
    /**
     * Modifiers in use
     */
    private final ArrayList<Modifier> m_modifiers = new ArrayList<>();
    
    /**
     * Speed multiplier - used to multiply the time the world advances
     */
    private double m_speedMultiplier = 1.0;
    
    /**
     * Should automators be updated?
     */
    private boolean m_updateAutomators = true;
    
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
    public int getGeneratorCount()
    {
        return m_generators.size();
    }

    /**
     * Removes a generator
     * @param generator Generator to remove
     */
    void removeGenerator(Generator generator)
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
    void removeAllGenerators()
    {
        m_generators.clear();
    }
    
    void addCurrency(Currency c)
    {
        if(c != null && !m_currencies.contains(c))
        {
            m_currencies.add(c);
        }
    }
    
    void removeCurrency(Currency c)
    {
        if(c != null)
        {
            m_currencies.remove(c);
        }
    }
    
    Currency getCurrency(int index)
    {
        return m_currencies.get(index);
    }
    
    List<Currency> getCurrencies()
    {
        return m_currencies;
    }
    
    void removeAllCurrencies()
    {
        m_currencies.clear();
    }
    
    /**
     * Advances the world state by the given amount of seconds.
     * Useful when calculating away-from-keyboard income etc.
     * 
     * @param seconds Seconds to advance
     */
    public void update(double seconds)
    {
        for(Modifier m : m_modifiers)
        {
            m.update(seconds);
        }
        
        seconds *= m_speedMultiplier;
        
        if(m_updateAutomators)
        {
            for(Automator a : m_automators)
            {
                a.update(seconds);
            }
        }
    }

    void addAutomator(Automator automator)
    {
        if(automator != null && !m_automators.contains(automator))
        {
            m_automators.add(automator);
        }
    }
    
    void addModifier(Modifier modifier)
    {
        if(modifier != null && !m_modifiers.contains(modifier))
        {
            m_modifiers.add(modifier);
        }
    }

    double getSpeedMultiplier()
    {
        return m_speedMultiplier;
    }

    void setSpeedMultiplier(double multiplier)
    {
        m_speedMultiplier = multiplier;
    }

    void disableAutomators()
    {
        m_updateAutomators = false;
    }
    
    void enableAutomators()
    {
        m_updateAutomators = true;
    }
}
