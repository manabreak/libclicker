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
    private final Currency m_currency;
    
    private boolean m_groupDigits;
    private String m_thousandSeparator;
    private boolean m_showDecimals;
    private int m_decimals;
    private String m_decimalSeparator;
    
    private boolean m_cutAtHighest;
    
    public static class Builder
    {
        private final Currency m_currency;
        
        private boolean m_groupDigits = true;
        private String m_thousandSeparator = ",";
        
        private boolean m_showDecimals = false;
        private int m_decimals = 2;
        private String m_decimalSeparator;
        
        private boolean m_cutAtHighest = true;
        
        public Builder(Currency c)
        {
            m_currency = c;
        }
        
        public Builder showHighestThousand()
        {
            m_cutAtHighest = true;
            return this;
        }
        
        public Builder showFully()
        {
            m_cutAtHighest = false;
            return this;
        }
        
        public Builder groupDigits() 
        {
            return groupDigits(",");
        }
        
        public Builder groupDigits(String separator)
        {
            m_groupDigits = true;
            m_thousandSeparator = separator;
            return this;
        }
        
        public Builder dontGroupDigits()
        {
            m_groupDigits = false;
            m_thousandSeparator = null;
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
            m_showDecimals = true;
            m_decimals = count;
            m_decimalSeparator = separator;
            return this;
        }
        
        public Builder dontShowDecimals()
        {
            m_showDecimals = false;
            m_decimals = 0;
            m_decimalSeparator = null;
            return this;
        }
        
        public CurrencyFormatter build()
        {
            CurrencyFormatter cf = new CurrencyFormatter(m_currency);
            cf.m_cutAtHighest = m_cutAtHighest;
            cf.m_showDecimals = m_showDecimals;
            cf.m_decimals = m_decimals;
            cf.m_decimalSeparator = m_decimalSeparator;
            cf.m_groupDigits = m_groupDigits;
            cf.m_thousandSeparator = m_thousandSeparator;
            return cf;
        }
    }
    
    private CurrencyFormatter(Currency currency)
    {
        m_currency = currency;
    }

    @Override
    public String toString()
    {
        String raw = m_currency.getAmountAsString();
        if(m_cutAtHighest)
        {
            int length = raw.length();
            if(length < 4) return raw;
            int rem = length % 3;
            rem = rem == 0 ? 3 : rem;
            String top = raw.substring(0, rem);
            
            if(m_showDecimals)
            {
                top += m_decimalSeparator;
                int decimals = Math.min(m_decimals, length - rem);
                top += raw.substring(rem, rem + decimals);
            }
            return top;
        }
        else
        {
            if(m_groupDigits)
            {
                int len = raw.length() - 3;
                for(int i = len; i > 0; --i)
                {
                    if((len - i) % 3 == 0)
                    {
                        raw = raw.substring(0, i) + m_thousandSeparator + raw.substring(i);
                    }
                }
            }
            return raw;
        }
    }
}
