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
 * A formatter for BigInteger values.
 *
 * @author Harri Pellikka
 */
public class Formatter
{
    /**
     * Formats a currency to a nice string representation.
     */
    public static class CurrencyFormatter extends Formatter
    {
        private final Currency mCurrency;
        
        private CurrencyFormatter(Builder builder, Currency currency)
        {
            super(builder);
            mCurrency = currency;
        }

        @Override
        public String toString()
        {
            setRawString(mCurrency.getValue().toString());
            return super.toString();
        }
    }
    
    /**
     * Formats an item's price to a nice string representation.
     */
    public static class ItemPriceFormatter extends Formatter
    {
        private final Item mItem;
        
        private ItemPriceFormatter(Builder builder, Item item)
        {
            super(builder);
            mItem = item;
        }

        @Override
        public String toString()
        {
            setRawString(mItem.getPrice().toString());
            return super.toString();
        }
    }
    
    protected final boolean mGroupDigits;
    protected final String mThousandSeparator;
    protected final boolean mShowDecimals;
    protected final int mDecimals;
    protected final String mDecimalSeparator;
    protected final boolean mCutAtHighest;
    protected final String[] mAbbreviations;
    
    protected Formatter(Builder builder)
    {
        mGroupDigits = builder.mGroupDigits;
        mThousandSeparator = builder.mThousandSeparator;
        mShowDecimals = builder.mShowDecimals;
        mDecimals = builder.mDecimals;
        mDecimalSeparator = builder.mDecimalSeparator;
        mCutAtHighest = builder.mCutAtHighest;
        mAbbreviations = builder.mAbbreviations;
    }
    
    public static class ForItemPrice extends Builder
    {
        private Item mItem;
        
        public ForItemPrice(Item item)
        {
            mItem = item;
        }
        
        @Override
        public ItemPriceFormatter build()
        {
            return new ItemPriceFormatter(this, mItem);
        }
    }
    
    public static class ForCurrency extends Builder
    {
        private Currency mCurrency;
        
        public ForCurrency(Currency c)
        {
            mCurrency = c;
        }
        
        public CurrencyFormatter build()
        {
            return new CurrencyFormatter(this, mCurrency);
        }
    }
    
    public static abstract class Builder
    {   
        private boolean mGroupDigits = true;
        private String mThousandSeparator = ",";
        private boolean mShowDecimals = false;
        private int mDecimals = 2;
        private String mDecimalSeparator;
        private boolean mCutAtHighest = true;
        private String[] mAbbreviations = null;
        
        private Builder()
        {
            
        }
        
        public Builder showHighestThousand()
        {
            mCutAtHighest = true;
            return this;
        }
        
        public Builder showFully()
        {
            mCutAtHighest = false;
            return this;
        }
        
        public Builder groupDigits() 
        {
            return groupDigits(",");
        }
        
        public Builder groupDigits(String separator)
        {
            mGroupDigits = true;
            mThousandSeparator = separator;
            return this;
        }
        
        public Builder dontGroupDigits()
        {
            mGroupDigits = false;
            mThousandSeparator = null;
            return this;
        }
        
        public Builder showDecimals()
        {
            return showDecimals(2, ".");
        }
        
        public Builder showDecimals(int count)
        {
            return showDecimals(count, ".");
        }
        
        public Builder showDecimals(String separator)
        {
            return showDecimals(2, separator);
        }
        
        public Builder showDecimals(int count, String separator)
        {
            mShowDecimals = true;
            mDecimals = count;
            mDecimalSeparator = separator;
            return this;
        }
        
        public Builder dontShowDecimals()
        {
            mShowDecimals = false;
            mDecimals = 0;
            mDecimalSeparator = null;
            return this;
        }
        
        public Builder useAbbreviations(String[] abbreviations)
        {
            mAbbreviations = abbreviations;
            return this;
        }
        
        public abstract Formatter build();
    }
    
    
    
    private String mRawString = "";
    
    public void setRawString(String raw)
    {
        mRawString = raw;
        if(mRawString == null) mRawString = "";
    }

    @Override
    public String toString()
    {
        String raw = mRawString;
        if(mCutAtHighest)
        {
            int length = raw.length();
            if(length < 4) return raw;
            int rem = length % 3;
            rem = rem == 0 ? 3 : rem;
            String top = raw.substring(0, rem);
            
            if(mShowDecimals)
            {
                top += mDecimalSeparator;
                int decimals = Math.min(mDecimals, length - rem);
                top += raw.substring(rem, rem + decimals);
            }
            
            if(mAbbreviations != null)
            {
                int tri = (raw.length() - 1) / 3;
                if(tri > 0 && tri <= mAbbreviations.length)
                {
                    top += mAbbreviations[tri - 1];
                }
            }
            
            return top;
        }
        else
        {
            if(mGroupDigits)
            {
                int len = raw.length() - 3;
                for(int i = len; i > 0; --i)
                {
                    if((len - i) % 3 == 0)
                    {
                        raw = raw.substring(0, i) + mThousandSeparator + raw.substring(i);
                    }
                }
            }
            return raw;
        }
    }
}
