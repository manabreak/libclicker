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

import static junit.framework.Assert.assertEquals;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Harri Pellikka
 */
public class WorldTest
{
    /**
     * Test of addGenerator method, of class World.
     */
    @Test
    public void testGenerators()
    {
        System.out.println("Add / remove generators");
        
        // Assert no generators in a new world
        World world = new World();
        assertEquals(0, world.getGeneratorCount());
        
        // Try to add null
        world.addGenerator(null);
        assertEquals(0, world.getGeneratorCount());
        
        Generator g = new Generator.Builder(world).build();
        world.addGenerator(g);
        assertEquals(1, world.getGeneratorCount());
        assertNotNull(g.getWorld());
        
        world.removeGenerator(g);
        assertEquals(0, world.getGeneratorCount());
        assertNull(g.getWorld());
        
        world.addGenerator(g);
        Generator g2 = new Generator.Builder(world).build();
        world.addGenerator(g2);
        assertEquals(2, world.getGeneratorCount());
        world.removeAllGenerators();
        assertEquals(0, world.getGeneratorCount());
    }

}
