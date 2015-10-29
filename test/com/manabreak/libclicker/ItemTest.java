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
 * Unit tests for the Item class.
 *
 * @author Harri Pellikka
 */
public class ItemTest
{

    /**
     * Test of getName method, of class Item.
     */
    @Test
    public void testName()
    {
        Item item = new ItemImpl();
        item.setName("Test");
        assertEquals("Test", item.getName());
    }

    /**
     * Test of getDescription method, of class Item.
     */
    @Test
    public void testDescription()
    {
        Item item = new ItemImpl();
        item.setDescription("Description text here.");
        assertEquals("Description text here.", item.getDescription());
    }

    /**
     * Test of getBasePrice method, of class Item.
     */
    @Test
    public void testBasePrice()
    {
        Item item = new ItemImpl();
        item.setBasePrice(10);
        assertEquals(new BigInteger("10"), item.getBasePrice());
    }

    /**
     * Test of getPrice method, of class Item.
     */
    @Test
    public void testPrice()
    {
        Item item = new ItemImpl();
        
        item.setBasePrice(10);
        item.setPriceMultiplier(1.5);
        
        assertEquals(new BigInteger("10"), item.getPrice());
        
        item.upgrade();
        
        assertEquals(new BigInteger("15"), item.getPrice());
    }

    /**
     * Test of buyWith method, of class Item.
     */
    @Test
    public void testPurchase()
    {
        World world = new World();
        Currency c = new Currency.Builder(world)
            .name("Gold")
            .build();
        
        Generator g = new Generator.Builder(world)
            .baseAmount(1000)
            .price(500)
            .generate(c)
            .build();
        
        assertEquals(BigInteger.ZERO, c.getValue());
        
        PurchaseResult pr = g.buyWith(c);
        assertEquals(PurchaseResult.INSUFFICIENT_FUNDS, pr);
        
        g.upgrade();
        g.process();
        
        assertEquals(1, g.getItemLevel());
        
        pr = g.buyWith(c);
        assertEquals(PurchaseResult.OK, pr);
        assertEquals(2, g.getItemLevel());
        
        g.setMaxItemLevel(2);
        
        g.process();
        pr = g.buyWith(c);
        assertEquals(PurchaseResult.MAX_LEVEL_REACHED, pr);
        assertEquals(2, g.getItemLevel());
    }

    /**
     * Test of setBasePrice method, of class Item.
     */
    @Test
    public void testSetBasePrice_BigInteger()
    {
        Item item = new ItemImpl();
        item.setBasePrice(new BigInteger("1234"));
        assertEquals(new BigInteger("1234"), item.getBasePrice());
    }

    /**
     * Test of setBasePrice method, of class Item.
     */
    @Test
    public void testSetBasePrice_long()
    {
        Item item = new ItemImpl();
        long price = 1234;
        item.setBasePrice(1234);
        assertEquals(new BigInteger("1234"), item.getBasePrice());
    }

    /**
     * Test of setBasePrice method, of class Item.
     */
    @Test
    public void testSetBasePrice_int()
    {
        Item item = new ItemImpl();
        int price = 1234;
        item.setBasePrice(1234);
        assertEquals(new BigInteger("1234"), item.getBasePrice());
    }

    /**
     * Test of getPriceMultiplier method, of class Item.
     */
    @Test
    public void testPriceMultiplier()
    {
        Item item = new ItemImpl();
        item.setPriceMultiplier(1.23);
        assertEquals(1.23, item.getPriceMultiplier(), 0.001);
        
    }

    /**
     * Test of getMaxItemLevel method, of class Item.
     */
    @Test
    public void testMaxItemLevel()
    {
        Item item = new ItemImpl();
        item.setMaxItemLevel(12);
        
        // Try to set the level greater than max level
        item.setItemLevel(14);
        assertEquals(12, item.getItemLevel());
        
        item.setItemLevel(5);
        assertEquals(5, item.getItemLevel());
        
        item.setItemLevel(12);
        assertEquals(12, item.getItemLevel());
        
        item.upgrade();
        assertEquals(12, item.getItemLevel());
        
        item.setMaxItemLevel(13);
        assertEquals(12, item.getItemLevel());
        
        item.upgrade();
        assertEquals(13, item.getItemLevel());
    }

    /**
     * Test of upgrade method, of class Item.
     */
    @Test
    public void testUpgradeDowngradeMaximize()
    {
        Item item = new ItemImpl();
        assertEquals(0, item.getItemLevel());
        item.upgrade();
        assertEquals(1, item.getItemLevel());
        item.upgrade();
        assertEquals(2, item.getItemLevel());
        item.downgrade();
        assertEquals(1, item.getItemLevel());
        item.downgrade();
        assertEquals(0, item.getItemLevel());
        item.downgrade();
        assertEquals(0, item.getItemLevel());
        
        item.setMaxItemLevel(10);
        item.maximize();
        assertEquals(item.getMaxItemLevel(), item.getItemLevel());
    }

    public class ItemImpl extends Item
    {

        public ItemImpl()
        {
            super(null);
        }
    }
    
}
