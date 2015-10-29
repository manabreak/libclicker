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

/**
 * Automator class for automating generators.
 *
 * @author Harri Pellikka
 */
public class Automator extends Item
{
    private Generator mGenerator;
    private double mTickRate = 1.0;
    private double mTickTimer = 0.0;
    private boolean mEnabled;
    
    public static class Builder
    {
        private final World mWorld;
        private Generator mGenerator;
        private double mTickRate = 1.0;
        private String mName = "Nameless automator";
        private boolean mEnabled = true;
        
        public Builder(World world)
        {
            mWorld = world;
        }
        
        public Builder automate(Generator generator)
        {
            mGenerator = generator;
            return this;
        }
        
        public Builder name(String name)
        {
            mName = name;
            return this;
        }
        
        public Builder every(double seconds)
        {
            mTickRate = seconds;
            return this;
        }
        
        public Automator build()
        {
            if(mGenerator == null) throw new IllegalStateException("Generator cannot be null");
            
            Automator a = new Automator(mWorld, mName);
            a.mGenerator = mGenerator;
            a.mTickRate = mTickRate;
            a.mEnabled = mEnabled;
            mWorld.addAutomator(a);
            return a;
        }
    }
    
    private Automator(World world, String name)
    {
        super(world, name);
    }
    
    public void enable()
    {
        mEnabled = true;
    }
    
    public void disable()
    {
        mEnabled = false;
    }
    
    public void update(double delta)
    {
        if(!mEnabled) return;
        
        mTickTimer += delta;
        while(mTickTimer >= mTickRate)
        {
            mTickTimer -= mTickRate;
            mGenerator.process();
        }
    }
    
    public double getTickRate()
    {
        return mTickRate;
    }
    
    public void setTickRate(double tickRate)
    {
        mTickRate = tickRate;
        if(mTickRate < 0.0) mTickRate = 0.0;
    }
    
    /**
     * Retrieves the percentage of the tick. Useful
     * when creating progress bars for generators.
     * 
     * @return Percentage of tick completion
     */
    public double getTimerPercentage()
    {
        return mTickRate != 0.0 ? mTickTimer / mTickRate : 1.0;
    }
}
