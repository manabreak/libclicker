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
 * A formatter for currencies. Formats the information and values of currencies
 * into various formats.
 *
 * @author Harri Pellikka
 */
public class CurrencyFormatter
{
    private final Currency mCurrency;
    
    private boolean mGroupDigits;
    private String mThousandSeparator;
    private boolean mShowDecimals;
    private int mDecimals;
    private String mDecimalSeparator;
    
    private boolean mCutAtHighest;
    
    public static class Builder
    {
        private final Currency mCurrency;
        
        private boolean mGroupDigits = true;
        private String mThousandSeparator = ",";
        
        private boolean mShowDecimals = false;
        private int mDecimals = 2;
        private String mDecimalSeparator;
        
        private boolean mCutAtHighest = true;
        
        public Builder(Currency c)
        {
            mCurrency = c;
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
        
        public CurrencyFormatter build()
        {
            CurrencyFormatter cf = new CurrencyFormatter(mCurrency);
            cf.mCutAtHighest = mCutAtHighest;
            cf.mShowDecimals = mShowDecimals;
            cf.mDecimals = mDecimals;
            cf.mDecimalSeparator = mDecimalSeparator;
            cf.mGroupDigits = mGroupDigits;
            cf.mThousandSeparator = mThousandSeparator;
            return cf;
        }
    }
    
    private CurrencyFormatter(Currency currency)
    {
        mCurrency = currency;
    }

    @Override
    public String toString()
    {
        String raw = mCurrency.getAmountAsString();
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
