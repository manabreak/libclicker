/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author Harri Pellikka
 */
public class CurrencyTest
{
    
    public CurrencyTest()
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

    /**
     * Test of getName method, of class Currency.
     */
    @Test
    public void testGetName()
    {
        World world = new World();
        System.out.println("getName()");
        Currency instance = new Currency.Builder(world).name("TestCurrency").build();
        String expResult = "TestCurrency";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of add method, of class Currency.
     */
    @Test
    public void testArithmetic()
    {
        World world = new World();
        
        System.out.println("testArithmetic()");
        Currency c = new Currency.Builder(world).build();
        assertEquals(BigInteger.ZERO, c.getValue());
        
        c.add(new BigInteger("1"));
        assertEquals(BigInteger.ONE, c.getValue());
        
        c.add(new BigInteger("12344"));
        assertEquals(new BigInteger("12345"), c.getValue());
        
        c.sub(new BigInteger("300"));
        assertEquals(new BigInteger("12045"), c.getValue());
        
        c.set(new BigInteger("100"));
        assertEquals(new BigInteger("100"), c.getValue());
        
        c.multiply(2.0);
        assertEquals(new BigInteger("200"), c.getValue());
        
        c.multiply(1.145);
        int targetVal = (int)(1.145 * 200);
        assertEquals(new BigInteger("" + targetVal), c.getValue());
    }
}
