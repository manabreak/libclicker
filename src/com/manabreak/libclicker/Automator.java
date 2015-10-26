

package com.manabreak.libclicker;

/**
 *
 * @author Harri
 */
public class Automator extends Item
{
    private Generator m_generator;
    private double m_tickRate = 1.0;
    private double m_tickTimer = 0.0;
    
    public static class Builder
    {
        private final World m_world;
        private Generator m_generator;
        private double m_tickRate = 1.0;
        private String m_name = "Nameless automator";
        
        public Builder(World world)
        {
            m_world = world;
        }
        
        public Builder automate(Generator generator)
        {
            m_generator = generator;
            return this;
        }
        
        public Builder name(String name)
        {
            m_name = name;
            return this;
        }
        
        public Builder every(double seconds)
        {
            m_tickRate = seconds;
            return this;
        }
        
        public Automator build()
        {
            if(m_generator == null) throw new IllegalStateException("Generator cannot be null");
            
            Automator a = new Automator(m_world, m_name);
            a.m_generator = m_generator;
            a.m_tickRate = m_tickRate;
            m_world.addAutomator(a);
            return a;
        }
    }
    
    private Automator(World world, String name)
    {
        super(world, name);
    }
    
    public void update(double delta)
    {
        m_tickTimer += delta;
        while(m_tickTimer >= m_tickRate)
        {
            m_tickTimer -= m_tickRate;
            m_generator.process();
        }
    }
    
    public double getTickRate()
    {
        return m_tickRate;
    }
    
    public void setTickRate(double tickRate)
    {
        m_tickRate = tickRate;
        if(m_tickRate < 0.0) m_tickRate = 0.0;
    }
    
    /**
     * Retrieves the percentage of the tick. Useful
     * for when creating progress bars for generators.
     * 
     * @return Percentage of tick completion
     */
    public double getTimerPercentage()
    {
        return m_tickRate != 0.0 ? m_tickTimer / m_tickRate : 1.0;
    }
}
