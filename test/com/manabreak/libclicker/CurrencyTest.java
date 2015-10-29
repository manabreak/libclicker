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
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Harri Pellikka
 */
public class CurrencyTest
{

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
