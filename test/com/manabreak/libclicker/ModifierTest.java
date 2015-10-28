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
 * Unit tests for modifiers
 *
 * @author Harri Pellikka
 */
public class ModifierTest
{
    
    public ModifierTest()
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
     * Test of enable method, of class Modifier.
     */
    @Test
    public void testSingleWorldSpeedModifier()
    {
        System.out.println("testWorldModifier()");
        World w = new World();
        assertEquals(1.0, w.getSpeedMultiplier(), 0.01);
        
        Modifier m = new Modifier.Builder()
            .modify(w)
            .speedBy(2.0)
            .build();
        
        m.enable();
        
        assertEquals(1.0 * 2.0, w.getSpeedMultiplier(), 0.01);
        
        m.disable();
        
        assertEquals(1.0, w.getSpeedMultiplier(), 0.01);
    }
    
    @Test
    public void testMultipleWorldSpeedModifiers()
    {
        System.out.println("test multiple speed by");
        World w = new World();
        
        Modifier m = new Modifier.Builder()
            .modify(w)
            .speedBy(2.0)
            .build();
        
        Modifier m2 = new Modifier.Builder()
            .modify(w)
            .speedBy(3.0)
            .build();
        
        m.enable();
        m2.enable();
        
        assertEquals(1.0 * 2.0 * 3.0, w.getSpeedMultiplier(), 0.01);
        
        m.disable();
        
        assertEquals(1.0 * 3.0, w.getSpeedMultiplier(), 0.01);
        
        m2.disable();
        
        assertEquals(1.0, w.getSpeedMultiplier(), 0.01);
    }

    @Test
    public void testDisableAllAutomators()
    {
        World w = new World();
        
        Currency c = new Currency.Builder(w)
            .name("Gold")
            .build();
        
        Generator g = new Generator.Builder(w)
            .generate(c)
            .baseAmount(1)
            .build();
        
        g.upgrade();
        
        Automator a = new Automator.Builder(w)
            .automate(g)
            .every(1.0)
            .build();
        
        assertEquals(BigInteger.ZERO, c.getValue());
        
        w.update(10.0);
        
        assertEquals(new BigInteger("10"), c.getValue());
        
        Modifier m = new Modifier.Builder()
            .modify(w)
            .disableGenerators()
            .build();
        
        m.enable();
        
        w.update(10.0);
        
        assertEquals(new BigInteger("10"), c.getValue());
        
        m.disable();
        
        w.update(10.0);
        
        assertEquals(new BigInteger("20"), c.getValue());
    }
    
    /**
     * Test of isEnabled method, of class Modifier.
     */
    @Test
    public void testIsEnabled()
    {
        System.out.println("isEnabled");
        
        World w = new World();
        
        Modifier m = new Modifier.Builder()
            .modify(w)
            .build();
        
        assertFalse(m.isEnabled());
        m.enable();
        assertTrue(m.isEnabled());
        m.disable();
        assertFalse(m.isEnabled());
    }
    
}
