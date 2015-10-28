package com.manabreak.libclicker;

/**
 * A base class for all the modifiers.
 * 
 * A modifier does "something" to a component (generator, automator, the
 * world etc), for example speeds up, slows down, increases production
 * or something similar.
 *
 * @author Harri Pellikka
 */
public abstract class Modifier
{
    private boolean m_enabled = false;
    
    /**
     * Modifier for worlds
     */
    static class WorldModifier extends Modifier
    {
        private World m_world;
        private double m_speedMultiplier;
        private boolean m_disableActivators;
        
        private double m_speedMultiplierBefore;
        private double m_speedMultiplierAfter;
        
        WorldModifier(World world)
        {
            m_world = world;
        }

        @Override
        protected void onEnable()
        {
            if(m_speedMultiplier != 1.0)
            {
                m_speedMultiplierBefore = m_world.getSpeedMultiplier();
                m_speedMultiplierAfter = m_speedMultiplier * m_speedMultiplierBefore;
                m_world.setSpeedMultiplier(m_speedMultiplierAfter);
            }
            
            if(m_disableActivators)
            {
                m_world.disableAutomators();
            }
        }

        @Override
        protected void onDisable()
        {
            if(m_speedMultiplier != 1.0)
            {
                double d = m_world.getSpeedMultiplier();
                d /= m_speedMultiplier;
                m_world.setSpeedMultiplier(d);
            }

            if(m_disableActivators)
            {
                m_world.enableAutomators();
            }
        }
    }
    
    /**
     * Modifier for generators.
     */
    static class GeneratorModifier extends Modifier
    {
        private final Generator m_generator;
        private double m_multiplier = 1.0;
        
        GeneratorModifier(Generator generator)
        {
            m_generator = generator;
        }

        @Override
        protected void onEnable()
        {
            m_generator.attachModifier(this);
        }

        @Override
        protected void onDisable()
        {
            m_generator.detachModifier(this);
        }
        
        double getMultiplier()
        {
            return m_multiplier;
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
            World m_world;
            private double m_speedMultiplier = 1.0;
            private boolean m_disableActivators = false;
            
            WorldTarget(World w)
            {
                m_world = w;
            }
            
            /**
             * Speeds up all the processing by the given multiplier.
             * @param multiplier Multiplier for advancing the time
             * @return This target for chaining
             */
            public WorldTarget speedBy(double multiplier)
            {
                m_speedMultiplier = multiplier;
                return this;
            }
            
            /**
             * Disables all the activators
             * @return This target for chaining
             */
            public WorldTarget disableActivators()
            {
                m_disableActivators = true;
                return this;
            }
            
            /**
             * Creates the actual modifier based on the given settings
             * @return Modifier 
             */
            public Modifier build()
            {
                WorldModifier m = new WorldModifier(m_world);
                m.m_speedMultiplier = m_speedMultiplier;
                m.m_disableActivators = m_disableActivators;
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
            private Generator m_generator;
            private double m_multiplier = 1.0;
            
            GeneratorTarget(Generator gen)
            {
                m_generator = gen;
            }
            
            /**
             * Multiplies the production of the generator.
             * 
             * @param multiplier Multiplier
             * @return This target for chaining
             */
            public GeneratorTarget multiplier(double multiplier)
            {
                m_multiplier = multiplier;
                return this;
            }
            
            /**
             * Constructs the actual modifier with the given settings
             * @return Modifier as per the given settings
             */
            public Modifier build()
            {
                GeneratorModifier m = new GeneratorModifier(m_generator);
                m.m_multiplier = m_multiplier;
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
    
    private Modifier()
    {
        
    }
    
    protected abstract void onEnable();
    protected abstract void onDisable();
    
    /**
     * Enables this modifier, i.e. makes it active
     */
    public void enable()
    {
        if(!m_enabled)
        {
            m_enabled = true;
            onEnable();
        }
    }
    
    /**
     * Disables this modifier, i.e. makes it inactive
     */
    public void disable()
    {
        if(m_enabled)
        { 
            onDisable();
            m_enabled = false;
        }
    }
    
    /**
     * Checks whether or not this modifier is enabled
     * @return True if enabled, false otherwise
     */
    public boolean isEnabled()
    {
        return m_enabled;
    }
}
