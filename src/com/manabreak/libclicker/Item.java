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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Base class for all the purchasable "items".
 * 
 * @author Harri Pellikka
 */
public abstract class Item implements Serializable
{
    /**
     * The base price of the item (i.e. the price of the first level of this item)
     */
    protected BigInteger mBasePrice = BigInteger.ONE;
    
    /**
     * Name of this item
     */
    protected String mName = "Nameless Item";
    
    /**
     * Description text for this item
     */
    protected String mDescription = "No description.";
    
    /**
     * Current level of this item
     */
    protected long mItemLevel = 0;
    
    /**
     * Max. item level
     */
    protected long mMaxItemLevel = Long.MAX_VALUE;
    
    /**
     * Price multiplier per level. This is used in the price formula
     * like this: price = (base price) * (price multiplier) ^ (item level)
     */
    protected double mPriceMultiplier = 1.145;
    
    /**
     * World this item belongs to
     */
    private final World mWorld;
    
    /**
     * Modifiers applied to this item
     */
    final ArrayList<Modifier> mModifiers = new ArrayList<>();
    
    /**
     * Constructs a new item
     * @param world World this item belongs to
     */
    protected Item(World world)
    {
        mWorld = world;
    }
    
    /**
     * Constructs a new item
     * @param world World this item belongs to
     * @param name Name of this item
     */
    protected Item(World world, String name)
    {
        mWorld = world;
        setName(name);
    }
    
    /**
     * Retrieves the name of this item
     * @return Name of this item 
     */
    public String getName()
    {
        return mName;
    }
    
    /**
     * Sets the name of this item
     * @param name New name for this item
     */
    public void setName(String name)
    {
        if(name == null || name.isEmpty()) throw new RuntimeException("Item name cannot be null or empty");
        mName = name;
    }
    
    public String getDescription()
    {
        return mDescription;
    }
    
    public void setDescription(String description)
    {
        mDescription = description;
    }
    
    /**
     * Retrieves the base price of this item
     * @return Base price of this item
     */
    public BigInteger getBasePrice()
    {
        return mBasePrice;
    }
    
    public BigInteger getPrice()
    {
        BigDecimal tmp = new BigDecimal(mBasePrice);
        tmp = tmp.multiply(new BigDecimal(Math.pow(mPriceMultiplier, mItemLevel)));
        return tmp.toBigInteger();
    }
    
    public PurchaseResult buyWith(Currency currency)
    {
        if(currency == null) throw new IllegalArgumentException("Currency cannot be null");
        if(mItemLevel >= mMaxItemLevel) return PurchaseResult.MAX_LEVEL_REACHED;
        
        BigInteger price = getPrice();
        BigInteger result = currency.getValue().subtract(price);
        if(result.signum() < 0)
        {
            return PurchaseResult.INSUFFICIENT_FUNDS;
        }
        currency.sub(price);
        upgrade();
        return PurchaseResult.OK;
    }
    
    /**
     * Sets the base price of this item
     * @param basePrice New base price for this item
     */
    public void setBasePrice(BigInteger basePrice)
    {
        if(basePrice == null) throw new RuntimeException("Base price cannot be null");
        if(basePrice.equals(BigInteger.ZERO)) throw new RuntimeException("Base price cannot be zero");
        mBasePrice = basePrice;
    }
    
    public void setBasePrice(long basePrice)
    {
        mBasePrice = new BigInteger("" + basePrice);
    }
    
    public void setBasePrice(int basePrice)
    {
        mBasePrice = new BigInteger("" + basePrice);
    }
    
    /**
     * Retrieves the price multiplier
     * @return Price multiplier
     */
    public double getPriceMultiplier()
    {
        return mPriceMultiplier;
    }
    
    /**
     * Sets the price multiplier of this item
     * @param multiplier Price multiplier
     */
    public void setPriceMultiplier(double multiplier)
    {
        mPriceMultiplier = multiplier;
    }
    
    public long getMaxItemLevel()
    {
        return mMaxItemLevel;
    }
    
    public void setMaxItemLevel(long maxLvl)
    {
        if(maxLvl <= 0) throw new RuntimeException("Max item level cannot be zero or negative");
        mMaxItemLevel = maxLvl;
    }
    
    public long getItemLevel()
    {
        return mItemLevel;
    }
    
    public void setItemLevel(long lvl)
    {
        mItemLevel = lvl < 0 ? 0 : lvl > mMaxItemLevel ? mMaxItemLevel : lvl;
    }
    
    public void upgrade()
    {
        if(mItemLevel < mMaxItemLevel)
        {
            mItemLevel++;
        }
    }
    
    public void downgrade()
    {
        if(mItemLevel > 0)
        {
            mItemLevel--;
        }
    }
    
    public void maximize()
    {
        mItemLevel = mMaxItemLevel;
    }
    
    protected World getWorld()
    {
        return mWorld;
    }
}
