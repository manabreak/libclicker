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
        System.out.println("Currency now: " + c.getAmountAsString());
        
        g.upgrade();
        g.process();
        BigInteger amount = new BigInteger("" + 100);
        amount = amount.add(g.getGeneratedAmount());
        assertEquals(amount, c.getValue());
        System.out.println("Currency now: " + c.getAmountAsString());
        
        for(int i = 0; i < 100; ++i)
        {
            g.process();
            System.out.println("Currency now: " + c.getAmountAsString(true));
        }
    }
}
