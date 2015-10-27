package com.manabreak.libclicker;

import java.math.BigInteger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Harri
 */
public class GeneratorTest
{
    
    public GeneratorTest()
    {
    }
    
    @BeforeClass
    public static void setUpClass()
    {
    }
    
    @AfterClass
    public static void tearDownClass()
    {
    }
    
    @Before
    public void setUp()
    {
    }
    
    @After
    public void tearDown()
    {
    }
    
    @Test
    public void testGeneration() throws Exception
    {
        World w = new World();
        Currency c = new Currency.Builder(w).name("Gold").build();
        
        CurrencyFormatter cf = new CurrencyFormatter
            .Builder(c)
            .showHighestThousand()
            .showDecimals()
            .build();
        
        Generator g = new Generator.Builder(w)
            .baseAmount(100)
            .multiplier(1.2)
            .generate(c)
            .build();
        
        assertEquals(BigInteger.ZERO, g.getGeneratedAmount());
        g.process();
        assertEquals(BigInteger.ZERO, c.getValue());
        
        g.upgrade();
        assertEquals(new BigInteger("" + 100), g.getGeneratedAmount());
        g.process();
        assertEquals(new BigInteger("" + 100), c.getValue());
        
        BigInteger amount = g.getGeneratedAmount();
        g.upgrade();
        g.process();
        amount = amount.add(g.getGeneratedAmount());
        // assertEquals(amount, c.getValue());
        assertEquals(amount, c.getValue());
    }
    
    @Test
    public void testRemainderUsage() throws Exception
    {
        World w = new World();
        
        Currency c = new Currency.Builder(w)
            .name("Gold")
            .build();
        
        Generator g = new Generator.Builder(w)
            .baseAmount(1)
            .multiplier(1.2)
            .useRemainder()
            .generate(c)
            .build();
        
        // Set to level 2
        g.setItemLevel(2);
        
        assertEquals(BigInteger.ZERO, c.getValue());
        
        g.process();
        assertEquals(new BigInteger("1"), c.getValue());
        
        g.process();
        assertEquals(new BigInteger("2"), c.getValue());
        
        g.process();
        assertEquals(new BigInteger("3"), c.getValue());
        
        g.process();
        assertEquals(new BigInteger("4"), c.getValue());
        
        g.process();
        assertEquals(new BigInteger("6"), c.getValue());
    }
}
