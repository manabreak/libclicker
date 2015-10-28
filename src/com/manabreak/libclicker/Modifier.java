package com.manabreak.libclicker;

import com.manabreak.libclicker.Generator;
import com.manabreak.libclicker.World;
import java.util.ArrayList;

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
    private boolean m_enabled = false;
    
    private static class WorldModifier extends Modifier
    {
        private World m_world;
        private double m_speedMultiplier;
        private boolean m_disableGenerators;
        private long m_levelBoostGenerators;
        
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
            
            if(m_disableGenerators)
            {
                m_world.disableAutomators();
            }
        }

        @Override
        protected void onDisable()
        {
            if(isEnabled())
            {
                if(m_speedMultiplier != 1.0)
                {
                    double d = m_world.getSpeedMultiplier();
                    d /= m_speedMultiplier;
                    m_world.setSpeedMultiplier(d);
                }
                
                if(m_disableGenerators)
                {
                    m_world.enableAutomators();
                }
            }
        }
    }
    
    public static class Builder
    {
        public static class WorldTarget
        {
            World m_world;
            private double m_speedMultiplier = 1.0;
            private boolean m_disableGenerators = false;
            
            public WorldTarget(World w)
            {
                m_world = w;
            }
            
            public WorldTarget speedBy(double multiplier)
            {
                m_speedMultiplier = multiplier;
                return this;
            }
            
            public WorldTarget disableGenerators()
            {
                m_disableGenerators = true;
                return this;
            }
            
            public Modifier build()
            {
                WorldModifier m = new WorldModifier(m_world);
                m.m_speedMultiplier = m_speedMultiplier;
                m.m_disableGenerators = m_disableGenerators;
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
    }
    
    private Modifier()
    {
        
    }
    
    protected abstract void onEnable();
    protected abstract void onDisable();
    
    public void enable()
    {
        m_enabled = true;
        onEnable();
    }
    
    public void disable()
    {
        onDisable();
        m_enabled = false;
    }
    
    public boolean isEnabled()
    {
        return m_enabled;
    }
    
    void update(double dt)
    {
        
    }
}
