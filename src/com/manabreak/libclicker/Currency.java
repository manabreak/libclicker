package com.manabreak.libclicker;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Base class for all currencies.
 *
 * @author Harri Pellikka
 */
public class Currency
{
    /**
     * Name of this currency
     */
    private String m_name;
    
    /**
     * Huge number to hold the amount for this currency
     */
    private BigInteger m_value = BigInteger.ZERO;
    
    /**
     * How many decimals should be shown?
     */
    private int m_decimals = 2;
    
    private final World m_world;
    
    public static class Builder
    {
        private final World m_world;
        private String m_name = "Gold";
        
        public Builder(World world)
        {
            m_world = world;
        }
        
        public Builder name(String name)
        {
            m_name = name;
            return this;
        }
        
        public Currency build()
        {
            Currency c = new Currency(m_world, m_name);
            m_world.addCurrency(c);
            return c;
        }
    }
    
    /**
     * Constructs a new currency with initial amount of 0
     * @param name 
     */
    private Currency(World world, String name)
    {
        m_world = world;
        m_name = name;
    }
    
    /**
     * Retrieves the name of this currency
     * @return 
     */
    public String getName()
    {
        return m_name;
    }
    
    public String getAmountAsString()
    {
        return m_value.toString();
    }
    
    @Override
    public String toString()
    {
        return m_name + ": " + getAmountAsString();
    }

    BigInteger getValue()
    {
        return m_value;
    }
    
    public void add(BigInteger other)
    {
        m_value = m_value.add(other);
    }
    
    public void sub(BigInteger other)
    {
        m_value = m_value.subtract(other);
    }
    
    public void multiply(double multiplier)
    {
        BigDecimal tmp = new BigDecimal(m_value);
        tmp = tmp.multiply(new BigDecimal(multiplier));
        m_value = tmp.toBigInteger();
    }

    void set(BigInteger newValue)
    {
        m_value = newValue;
    }
}
