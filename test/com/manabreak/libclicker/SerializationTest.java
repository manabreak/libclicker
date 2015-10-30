/*
 * The MIT License
 *
 * Copyright 2015 Harri.
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

import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
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
public class SerializationTest
{
    
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException
    {
        String currencyName = "Serialized Gold";
        String generatorName = "Serialized Gold Generator";
        int genBaseAmount = 1234;
        double genMultiplier = 1.34;
        int genMaxLevel = 42;
        int genLevel = 13;
        
        World world = new World();
        Currency gold = new Currency.Builder(world)
            .name(currencyName)
            .build();
        
        Generator gen = new Generator.Builder(world)
            .baseAmount(genBaseAmount)
            .multiplier(genMultiplier)
            .maxLevel(genMaxLevel)
            .generate(gold)
            .build();
        
        for(int i = 0; i < genLevel; ++i)
        {
            gen.upgrade();
        }
        
        Automator a = new Automator.Builder(world)
            .automate(gen)
            .every(2.3)
            .build();
        
        gen.process();
        
        Modifier mWorld = new Modifier.Builder()
            .modify(world)
            .disableActivators()
            .speedBy(3.5)
            .build();
        mWorld.enable();
        
        Modifier mGen = new Modifier.Builder()
            .modify(gen)
            .multiplier(4.2)
            .build();
        mGen.enable();
        
        BigInteger goldBeforeSerialization = gold.getValue();
        
        /* SERIALIZATION */
        ByteOutputStream bos = new ByteOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(world);
        
        byte[] bytes = bos.getBytes();
        oos.close();
        
        /* DESERIALIZATION */
        ByteInputStream bis = new ByteInputStream(bytes, bytes.length);
        ObjectInputStream ois = new ObjectInputStream(bis);
        World newWorld = (World)ois.readObject();
        
        assertEquals(world.getGeneratorCount(), newWorld.getGeneratorCount());
        assertEquals(world.getCurrencies().size(), newWorld.getCurrencies().size());
        assertEquals(world.getAutomators().size(), newWorld.getAutomators().size());
        assertEquals(world.getModifiers().size(), newWorld.getModifiers().size());
        assertEquals(world.getSpeedMultiplier(), newWorld.getSpeedMultiplier(), 0.001);
        assertEquals(world.isAutomationEnabled(), newWorld.isAutomationEnabled());
        
        for(int i = 0; i < world.getCurrencies().size(); ++i)
        {
            assertEquals(world.getCurrency(i).getName(), newWorld.getCurrency(i).getName());
            assertEquals(world.getCurrency(i).getValue(), newWorld.getCurrency(i).getValue());
        }
        
        for(int i = 0; i < world.getAutomators().size(); ++i)
        {
            Automator a0 = world.getAutomators().get(i);
            Automator a1 = newWorld.getAutomators().get(i);
            assertEquals(a0.getBasePrice(), a1.getBasePrice());
            assertEquals(a0.getDescription(), a1.getDescription());
            assertEquals(a0.getItemLevel(), a1.getItemLevel());
            assertEquals(a0.getMaxItemLevel(), a1.getMaxItemLevel());
            assertEquals(a0.getName(), a1.getName());
            assertEquals(a0.getPrice(), a1.getPrice());
            assertEquals(a0.getPriceMultiplier(), a1.getPriceMultiplier(), 0.001);
            assertEquals(a0.getTickRate(), a1.getTickRate(), 0.001);
            assertEquals(a0.getTimerPercentage(), a1.getTimerPercentage(), 0.001);
        }
    }
}
