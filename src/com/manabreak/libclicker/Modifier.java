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

/**
 * A base class for all the modifiers.
 * 
 * A modifier does "something" to a component (generator, automator, the
 * world etc), for example speeds up, slows down, increases production
 * or something similar.
 *
 * @author Harri Pellikka
 */
public abstract class Modifier extends Item implements Serializable
{
    private boolean mEnabled = false;
    
    /**
     * Modifier for worlds
     */
    static class WorldModifier extends Modifier
    {
        private double mSpeedMultiplier;
        private boolean mDisableActivators;
        
        private double mSpeedMultiplierBefore;
        private double mSpeedMultiplierAfter;
        
        WorldModifier(World world)
        {
            super(world);
        }

        @Override
        protected void onEnable()
        {
            if(mSpeedMultiplier != 1.0)
            {
                mSpeedMultiplierBefore = getWorld().getSpeedMultiplier();
                mSpeedMultiplierAfter = mSpeedMultiplier * mSpeedMultiplierBefore;
                getWorld().setSpeedMultiplier(mSpeedMultiplierAfter);
            }
            
            if(mDisableActivators)
            {
                getWorld().disableAutomators();
            }
        }

        @Override
        protected void onDisable()
        {
            if(mSpeedMultiplier != 1.0)
            {
                double d = getWorld().getSpeedMultiplier();
                d /= mSpeedMultiplier;
                getWorld().setSpeedMultiplier(d);
            }

            if(mDisableActivators)
            {
                getWorld().enableAutomators();
            }
        }
    }
    
    /**
     * Modifier for generators.
     */
    static class GeneratorModifier extends Modifier
    {
        private final Generator mGenerator;
        private double mMultiplier = 1.0;
        
        GeneratorModifier(Generator generator)
        {
            super(generator.getWorld());
            mGenerator = generator;
        }

        @Override
        protected void onEnable()
        {
            mGenerator.attachModifier(this);
        }

        @Override
        protected void onDisable()
        {
            mGenerator.detachModifier(this);
        }
        
        double getMultiplier()
        {
            return mMultiplier;
        }
    }
    
    /**
     * Builder class for the modifiers
     */
    public static class Builder
    {
        /**
         * A modifier settings class for world modifiers.
         * Keeps track of all the parameters the modifier should
         * modify.
         */
        public static class WorldTarget
        {
            World mWorld;
            private double mSpeedMultiplier = 1.0;
            private boolean mDisableActivators = false;
            
            WorldTarget(World w)
            {
                mWorld = w;
            }
            
            /**
             * Speeds up all the processing by the given multiplier.
             * @param multiplier Multiplier for advancing the time
             * @return This target for chaining
             */
            public WorldTarget speedBy(double multiplier)
            {
                mSpeedMultiplier = multiplier;
                return this;
            }
            
            /**
             * Disables all the activators
             * @return This target for chaining
             */
            public WorldTarget disableActivators()
            {
                mDisableActivators = true;
                return this;
            }
            
            /**
             * Creates the actual modifier based on the given settings
             * @return Modifier 
             */
            public Modifier build()
            {
                WorldModifier m = new WorldModifier(mWorld);
                m.mSpeedMultiplier = mSpeedMultiplier;
                m.mDisableActivators = mDisableActivators;
                return m;
            }
        }
        
        /**
         * A modifier settings class for generator modifiers.
         * Keeps track of all the parameters the modifier should
         * modify.
         */
        public static class GeneratorTarget
        {
            private Generator mGenerator;
            private double mMultiplier = 1.0;
            
            GeneratorTarget(Generator gen)
            {
                mGenerator = gen;
            }
            
            /**
             * Multiplies the production of the generator.
             * 
             * @param multiplier Multiplier
             * @return This target for chaining
             */
            public GeneratorTarget multiplier(double multiplier)
            {
                mMultiplier = multiplier;
                return this;
            }
            
            /**
             * Constructs the actual modifier with the given settings
             * @return Modifier as per the given settings
             */
            public Modifier build()
            {
                GeneratorModifier m = new GeneratorModifier(mGenerator);
                m.mMultiplier = mMultiplier;
                return m;
            }
        }
        
        /**
         * Constructs a new modifier builder
         */
        public Builder()
        {
            
        }
        
        /**
         * Apply the modifier to a world
         * @param world World to modify
         * @return A world target to set the modification details
         */
        public final WorldTarget modify(World world)
        {
            return new WorldTarget(world);
        }
        
        /**
         * Apply the modifier to a generator
         * @param gen Generator to modify
         * @return A generator target to set the modification details
         */
        public final GeneratorTarget modify(Generator gen)
        {
            return new GeneratorTarget(gen);
        }
    }
    
    private Modifier(World world)
    {
        super(world);
    }
    
    protected abstract void onEnable();
    protected abstract void onDisable();
    
    /**
     * Enables this modifier, i.e. makes it active
     */
    public void enable()
    {
        if(!mEnabled)
        {
            mEnabled = true;
            getWorld().addModifier(this);
            onEnable();
        }
    }
    
    /**
     * Disables this modifier, i.e. makes it inactive
     */
    public void disable()
    {
        if(mEnabled)
        { 
            onDisable();
            getWorld().removeModifier(this);
            mEnabled = false;
        }
    }
    
    /**
     * Checks whether or not this modifier is enabled
     * @return True if enabled, false otherwise
     */
    public boolean isEnabled()
    {
        return mEnabled;
    }
}
