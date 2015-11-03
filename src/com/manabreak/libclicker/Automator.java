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

import java.io.Serializable;
import java.math.BigInteger;

/**
 * Automator class for automating generators.
 * 
 * Normally generators are manually controlled, i.e. they generate resources
 * when explicitly told to. Automators are used to trigger generators
 * during the world's update cycles.
 *
 * @author Harri Pellikka
 */
public class Automator extends Item implements Serializable
{
    private Generator mGenerator;
    private double mTickRate = 1.0;
    private double mTickTimer = 0.0;
    private double mMultiplier;
    private boolean mEnabled;
    private double mActualTickRate;
    
    public static class Builder
    {
        private final World mWorld;
        private Generator mGenerator;
        private double mTickRate = 1.0;
        private String mName = "Nameless automator";
        private boolean mEnabled = true;
        private BigInteger mBasePrice = BigInteger.ONE;
        private double mPriceMultiplier = 1.1;
        private double mTickRateMultiplier = 1.08;
        
        /**
         * Constructs a new automator builder
         * @param world World the automator belongs to
         */
        public Builder(World world)
        {
            mWorld = world;
        }
        
        /**
         * Constructs a new automator builder for the given generator
         * @param world World the automator belongs to
         * @param generator Generator to automate
         */
        public Builder(World world, Generator generator)
        {
            mWorld = world;
            mGenerator = generator;
        }
        
        public Builder basePrice(int price)
        {
            mBasePrice = new BigInteger("" + price);
            return this;
        }
        
        public Builder basePrice(long price)
        {
            mBasePrice = new BigInteger("" + price);
            return this;
        }
        
        public Builder basePrice(BigInteger price)
        {
            mBasePrice = price;
            return this;
        }
        
        public Builder priceMultiplier(double multiplier)
        {
            mPriceMultiplier = multiplier;
            return this;
        }
        
        public Builder tickRateMultiplier(double multiplier)
        {
            mTickRateMultiplier = multiplier;
            return this;
        }
        
        /**
         * Sets the target generator this automator should automate.
         * 
         * @param generator Generator to automate
         * @return This builder for chaining
         */
        public Builder automate(Generator generator)
        {
            mGenerator = generator;
            return this;
        }
        
        /**
         * Sets the name for this automator.
         * 
         * @param name Name
         * @return This builder for chaining
         */
        public Builder name(String name)
        {
            mName = name;
            return this;
        }
        
        /**
         * Sets the tick rate of this automator, i.e. how often
         * this automator should do its business.
         * 
         * @param seconds Tick rate in seconds
         * @return This builder for chaining
         */
        public Builder every(double seconds)
        {
            mTickRate = seconds;
            return this;
        }
        
        /**
         * Constructs the automator based on the given properties.
         * @return The automator
         */
        public Automator build()
        {
            if(mGenerator == null) throw new IllegalStateException("Generator cannot be null");
            
            Automator a = new Automator(mWorld, mName);
            a.mGenerator = mGenerator;
            a.mTickRate = mTickRate;
            a.mEnabled = mEnabled;
            a.mBasePrice = mBasePrice;
            a.mPriceMultiplier = mPriceMultiplier;
            a.mMultiplier = mTickRateMultiplier;
            mWorld.addAutomator(a);
            return a;
        }
    }
    
    private Automator(World world, String name)
    {
        super(world, name);
    }
    
    /**
     * Enables this automator. Automators are enabled by default when
     * they are created.
     */
    public void enable()
    {
        if(!mEnabled)
        {
            getWorld().addAutomator(this);
            mEnabled = true;
        }
    }
    
    /**
     * Disables this automator, effectively turning the automation off.
     */
    public void disable()
    {
        if(mEnabled)
        {
            getWorld().removeAutomator(this);
            mEnabled = false;
        }
    }

    @Override
    public void upgrade()
    {
        super.upgrade(); //To change body of generated methods, choose Tools | Templates.
        mActualTickRate = getFinalTickRate();
        System.out.println("Upgraded, final tick rate now: " + mActualTickRate);
    }
    
    private double getFinalTickRate()
    {
        if(mItemLevel == 0) return 0.0;
        double r = mTickRate;
        double m = Math.pow(mMultiplier, mItemLevel - 1);
        return r / m;
    }
    
    void update(double delta)
    {
        if(!mEnabled || mItemLevel == 0) return;
        
        mTickTimer += delta;
        while(mTickTimer >= mActualTickRate)
        {
            mTickTimer -= mActualTickRate;
            mGenerator.process();
        }
    }
    
    /**
     * Retrieves the tick rate of this automator.
     * @return Tick rate in seconds
     */
    public double getTickRate()
    {
        return mTickRate;
    }
    
    /**
     * Sets the tick rate of this automator.
     * 
     * @param tickRate Tick rate in seconds
     */
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
