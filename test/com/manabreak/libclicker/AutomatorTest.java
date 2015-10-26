package com.manabreak.libclicker;

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
public class AutomatorTest
{
    
    public AutomatorTest()
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
