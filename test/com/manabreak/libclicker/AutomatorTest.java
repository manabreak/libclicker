package com.manabreak.libclicker;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Harri
 */
public class AutomatorTest
{
    @Test
    public void testUpdate()
    {
        World world = new World();
        
        System.out.println("update()");
        Generator g = new Generator.Builder(world)
            .build();
        
        Automator a = new Automator.Builder(world)
            .automate(g)
            .every(1.0)
            .build();
        world.update(1.0);
        assertEquals(1, g.getTimesProcessed());
        
        world.update(9.0);
        assertEquals(10, g.getTimesProcessed());
    }
    
}
