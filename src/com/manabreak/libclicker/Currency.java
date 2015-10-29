package com.manabreak.libclicker;

import java.math.BigDecimal;
import java.math.BigInteger;

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
    private String mName;
    
    /**
     * Huge number to hold the amount for this currency
     */
    private BigInteger mValue = BigInteger.ZERO;
    
    private final World mWorld;
    
    public static class Builder
    {
        private final World mWorld;
        private String mName = "Gold";
        
        public Builder(World world)
        {
            mWorld = world;
        }
        
        public Builder name(String name)
        {
            mName = name;
            return this;
        }
        
        public Currency build()
        {
            Currency c = new Currency(mWorld, mName);
            mWorld.addCurrency(c);
            return c;
        }
    }
    
    /**
     * Constructs a new currency with initial amount of 0
     * @param name 
     */
    private Currency(World world, String name)
    {
        mWorld = world;
        mName = name;
    }
    
    /**
     * Retrieves the name of this currency
     * @return 
     */
    public String getName()
    {
        return mName;
    }
    
    public String getAmountAsString()
    {
        return mValue.toString();
    }
    
    @Override
    public String toString()
    {
        return mName + ": " + getAmountAsString();
    }

    BigInteger getValue()
    {
        return mValue;
    }
    
    public void add(BigInteger other)
    {
        mValue = mValue.add(other);
    }
    
    public void sub(BigInteger other)
    {
        mValue = mValue.subtract(other);
    }
    
    public void multiply(double multiplier)
    {
        BigDecimal tmp = new BigDecimal(mValue);
        tmp = tmp.multiply(new BigDecimal(multiplier));
        mValue = tmp.toBigInteger();
    }

    void set(BigInteger newValue)
    {
        mValue = newValue;
    }
}
