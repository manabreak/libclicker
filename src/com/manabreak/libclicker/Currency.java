package com.manabreak.libclicker;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 *
 * @author Harri
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
     * Names of the orders. Defaults to the standard names (million, billion...).3
     */
    private ArrayList<String> m_orderNames = new ArrayList<String>();
    
    /**
     * Shorthand names of the orders. Defaults to the standard shorthands
     * such as K, M, B...
     */
    private ArrayList<String> m_shortHands = new ArrayList<String>();
    
    /**
     * Separator character between every triplet of numbers
     */
    private String m_thousandSeparator = ",";
    
    /**
     * Separator between the decimals and the integers
     */
    private String m_decimalSeparator = ".";
    
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
     * Resets the order names to the default ones.
     */
    public void useDefaultNames()
    {
        m_orderNames.clear();
        m_orderNames.add("");
        m_orderNames.add("thousand");
        m_orderNames.add("million");
        m_orderNames.add("billion");
        m_orderNames.add("trillion");
        m_orderNames.add("quadrillion");
        m_orderNames.add("quintillion");
        m_orderNames.add("sextillion");
        m_orderNames.add("septillion");
        m_orderNames.add("octillion");
        m_orderNames.add("nonillion");
        m_orderNames.add("decillion");
        m_orderNames.add("undecillion");
        m_orderNames.add("duodecillion");
        m_orderNames.add("tredecillion");
        m_orderNames.add("quattuordecillion");
        m_orderNames.add("quindecillion");
        m_orderNames.add("sexdecillion");
        m_orderNames.add("septendecillion");
        m_orderNames.add("octadecillion");
        m_orderNames.add("novemdecillion");
        m_orderNames.add("vigintillion");
        m_orderNames.add("unvigintillion");
        m_orderNames.add("duovigintillion");
        m_orderNames.add("trevigintillion");
        m_orderNames.add("quattuorvigintillion");
        m_orderNames.add("quinvigintillion");
        m_orderNames.add("sexvigintillion");
        m_orderNames.add("septenvigintillion");
        m_orderNames.add("octavigintillion");
        m_orderNames.add("novenvigintillion");
        m_orderNames.add("trigintillion");
        m_orderNames.add("untrigintillion");
        m_orderNames.add("duotrigintillion");
        m_orderNames.add("tretrigintillion");
        m_orderNames.add("quattuortrigintillion");
        m_orderNames.add("quintrigintillion");
        m_orderNames.add("sextrigintillion");
        m_orderNames.add("septentrigintillion");
    }
    
    /**
     * Resets the order shorthands to the default ones.
     */
    public void useDefaultShorthands()
    {
        
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
        return getAmountAsString(true);
    }
    
    public String getAmountAsString(boolean addSeparators)
    {
        // if(m_value.getOrdersCount() == 0) return "0";
        if(m_value.equals(BigInteger.ZERO)) return "0";
        if(!addSeparators) return m_value.toString();
        
        String s = m_value.toString();
        int len = s.length();
        for(int i = len - 1; i > 0; --i)
        {
            if((len - i - 1) % 3 == 2) s = s.substring(0, i) + m_thousandSeparator + s.substring(i);
        }
        return s;
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
