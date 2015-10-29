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
    
    public static class Builder
    {
        private final World mWorld;
        private Generator mGenerator;
        private double mTickRate = 1.0;
        private String mName = "Nameless automator";
        
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
            mWorld.addAutomator(a);
            return a;
        }
    }
    
    private Automator(World world, String name)
    {
        super(world, name);
    }
    
    public void update(double delta)
    {
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
