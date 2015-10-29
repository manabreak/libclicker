/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
