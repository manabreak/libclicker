package com.manabreak.libclicker;

/**
 * A base class for all the modifiers.
 *
 * @author Harri Pellikka
 */
public abstract class Modifier
{
    private double m_duration = 0.0;
    private double m_timer = 0.0;
    private double m_delay = 0.0;
    private boolean m_loop = false;
    
    private static class WorldModifier extends Modifier
    {
        private World m_world;
        private double m_speedMultiplier;
        private boolean m_disableGenerators;
        private boolean m_disableAutomators;
        private long m_levelBoostGenerators;
        
        WorldModifier(World world)
        {
            m_world = world;
        }

        @Override
        void apply()
        {
            
        }
        
    }
    
    public static class Builder
    {
        public static class WorldTarget
        {
            World m_world;
            private double m_speedMultiplier = 1.0;
            
            public WorldTarget(World w)
            {
                m_world = w;
            }
            
            public WorldTarget speedBy(double multiplier)
            {
                m_speedMultiplier = multiplier;
                return this;
            }
            
            public Modifier build()
            {
                WorldModifier m = new WorldModifier(m_world);
                m.m_speedMultiplier = m_speedMultiplier;
                return m;
            }
        }
        
        public static class GeneratorTarget extends Builder
        {
            private Generator m_generator;
            
            public GeneratorTarget(Generator gen)
            {
                m_generator = gen;
            }
        }
        
        public Builder()
        {
            
        }
        
        public final WorldTarget modify(World world)
        {
            return new WorldTarget(world);
        }
        
        public final GeneratorTarget modify(Generator gen)
        {
            return new GeneratorTarget(gen);
        }
        
        public Modifier build()
        {
            throw new IllegalStateException("No target set for modification");
        }
    }
    
    private Modifier()
    {
        
    }
    
    abstract void apply();
    
    void update(double dt)
    {
        
    }
}
