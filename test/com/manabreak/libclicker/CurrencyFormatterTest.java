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

import java.math.BigInteger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the currency formatter
 *
 * @author Harri Pellikka
 */
public class CurrencyFormatterTest
{
    World w;
    Currency c;
    CurrencyFormatter cf;
    
    public CurrencyFormatterTest()
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
        w = new World();
        c = new Currency.Builder(w).name("Gold").build();
        cf = null;
    }
    
    @After
    public void tearDown()
    {
        w = null;
        c = null;
        cf = null;
    }

    @Test
    public void testDigitGrouping()
    {
        System.out.println("toString()");
        
        // Test the 
        cf = new CurrencyFormatter.Builder(c)
            .groupDigits()
            .showFully()
            .build();
            
        assertEquals("0", cf.toString());
        
        c.set(new BigInteger("1"));
        assertEquals("1", cf.toString());
        
        c.set(new BigInteger("12"));
        assertEquals("12", cf.toString());
        
        c.set(new BigInteger("123"));
        assertEquals("123", cf.toString());
        
        c.set(new BigInteger("1234"));
        assertEquals("1,234", cf.toString());
        
        c.set(new BigInteger("12345"));
        assertEquals("12,345", cf.toString());
        
        c.set(new BigInteger("123456"));
        assertEquals("123,456", cf.toString());
        
        c.set(new BigInteger("1234567"));
        assertEquals("1,234,567", cf.toString());
        
        c.set(new BigInteger("12345678"));
        assertEquals("12,345,678", cf.toString());
    }
    
    @Test
    public void testRaw() throws Exception
    {
        cf = new CurrencyFormatter.Builder(c)
            .dontGroupDigits()
            .showFully()
            .build();
        
        assertEquals("0", cf.toString());
        
        c.set(new BigInteger("1"));
        assertEquals("1", cf.toString());
        
        c.set(new BigInteger("12"));
        assertEquals("12", cf.toString());
        
        c.set(new BigInteger("123"));
        assertEquals("123", cf.toString());
        
        c.set(new BigInteger("1234"));
        assertEquals("1234", cf.toString());
        
        c.set(new BigInteger("12345"));
        assertEquals("12345", cf.toString());
        
        c.set(new BigInteger("123456"));
        assertEquals("123456", cf.toString());
        
        c.set(new BigInteger("1234567"));
        assertEquals("1234567", cf.toString());
        
        c.set(new BigInteger("12345678"));
        assertEquals("12345678", cf.toString());
    }
    
    @Test
    public void testCutAtHighestWithDecimals() throws Exception
    {
        cf = new CurrencyFormatter.Builder(c)
            .showHighestThousand()
            .showDecimals(2, ".")
            .build();
        
        assertEquals("0", cf.toString());
        
        c.set(new BigInteger("1"));
        assertEquals("1", cf.toString());
        
        c.set(new BigInteger("123"));
        assertEquals("123", cf.toString());
        
        c.set(new BigInteger("1234"));
        assertEquals("1.23", cf.toString());
        
        c.set(new BigInteger("12345"));
        assertEquals("12.34", cf.toString());
        
        c.set(new BigInteger("123456"));
        assertEquals("123.45", cf.toString());
        
        c.set(new BigInteger("1234567"));
        assertEquals("1.23", cf.toString());
    }
    
    @Test
    public void testCutAtHighestNoDecimals() throws Exception
    {
        cf = new CurrencyFormatter.Builder(c)
            .showHighestThousand()
            .dontShowDecimals()
            .build();
        
        assertEquals("0", cf.toString());
        
        c.set(new BigInteger("1"));
        assertEquals("1", cf.toString());
        
        c.set(new BigInteger("12"));
        assertEquals("12", cf.toString());
        
        c.set(new BigInteger("123"));
        assertEquals("123", cf.toString());
        
        c.set(new BigInteger("1234"));
        assertEquals("1", cf.toString());
        
        c.set(new BigInteger("5432"));
        assertEquals("5", cf.toString());
        
        c.set(new BigInteger("54321"));
        assertEquals("54", cf.toString());
        
        c.set(new BigInteger("123456"));
        assertEquals("123", cf.toString());
        
        c.set(new BigInteger("98712345"));
        assertEquals("98", cf.toString());
    }
    
    @Test
    public void testSeparators() throws Exception
    {
        cf = new CurrencyFormatter.Builder(c)
            .groupDigits()
            .showFully()
            .build();
        
        // Default thousands separator ','
        c.set(new BigInteger("123456789"));
        assertEquals("123,456,789", cf.toString());
        
        // Set a single space as thousands separator
        cf = new CurrencyFormatter.Builder(c)
            .groupDigits(" ")
            .showFully()
            .build();
        assertEquals("123 456 789", cf.toString());
        
        // Default decimal separator '.'
        cf = new CurrencyFormatter.Builder(c)
            .showDecimals()
            .showHighestThousand()
            .build();
        assertEquals("123.45", cf.toString());
        
        // Custom separator '#'
        cf = new CurrencyFormatter.Builder(c)
            .showDecimals("#")
            .showHighestThousand()
            .build();
        assertEquals("123#45", cf.toString());
        
        // Show more decimals
        cf = new CurrencyFormatter.Builder(c)
            .showDecimals(3)
            .showHighestThousand()
            .build();
        assertEquals("123.456", cf.toString());
        
        // Show just one decimal
        cf = new CurrencyFormatter.Builder(c)
            .showDecimals(1)
            .showHighestThousand()
            .build();
        assertEquals("123.4", cf.toString());
        
    }
}
