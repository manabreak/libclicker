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

import com.manabreak.libclicker.Formatter.CurrencyFormatter;
import java.math.BigInteger;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Harri Pellikka
 */
public class GeneratorTest
{
    
    @Test
    public void testGeneration() throws Exception
    {
        World w = new World();
        Currency c = new Currency.Builder(w).name("Gold").build();
        
        Formatter cf = new Formatter.ForCurrency(c)
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
