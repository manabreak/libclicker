package com.manabreak.libclicker;

import java.util.ArrayList;

public class HugeNumber
{
    private ArrayList<Integer> m_amountsPerOrder = new ArrayList<Integer>();
    
    public ArrayList<Integer> getAmountsPerOrder()
    {
        return m_amountsPerOrder;
    }
    
    public int getOrdersCount()
    {
        return m_amountsPerOrder.size();
    }
    
    /**
     * Adds an amount to this currency.
     * The amounts are per-every-third-order of magnitude, so the first
     * index should have the amount to add between 0 and 999, the second
     * index should have the amount between 1000 and 999999 and so on.
     * 
     * There is some fault-tolerance added to allow larger additions to be
     * made. Negative values are also allowed.
     * 
     * @param amounts Amounts to be added.
     */
    public void add(Integer... amounts)
    {
        int overflow = 0;
        
        for(int i = 0; i < amounts.length; ++i)
        {
            if(m_amountsPerOrder.size() <= i)
            {
                m_amountsPerOrder.add(new Integer(0));
            }
            
            int amount = amounts[i];
            
            amount += overflow;
            overflow = 0;
            
            if(amount == 0) continue;
            
            while(amount > 999)
            {
                overflow++;
                amount -= 1000;
            }
            while(amount < 0)
            {
                overflow--;
                amount += 1000;
            }
            
            int oldAmount = m_amountsPerOrder.get(i);
            oldAmount += amount;
            
            overflow += oldAmount / 1000;
            oldAmount %= 1000;
            
            m_amountsPerOrder.set(i, oldAmount);
            
            if(overflow < 0 && m_amountsPerOrder.size() > i)
            {
                m_amountsPerOrder.set(i + 1, m_amountsPerOrder.get(i + 1) + overflow);
                overflow = 0;
            }
        }
        
        if(overflow > 0)
        {
            m_amountsPerOrder.add(new Integer(overflow));
        }
    }
    
    /**
     * Subtracts the given amounts from the currency.
     * @param amounts Amounts to subtract.
     */
    public void sub(Integer... amounts)
    {
        for(int i = 0; i < amounts.length; ++i)
        {
            amounts[i] = -amounts[i];
        }

        add(amounts);
    }
    
    /**
     * Retrieves the amount of this currency per the given order.
     * @param order Order
     * @return Amount of money per the order, or zero if there is no money.
     */
    public int get(int order)
    {
        return (order >= 0 && order < m_amountsPerOrder.size() ? m_amountsPerOrder.get(order) : 0);
    }
    
    public int getLargestOrder()
    {
        return (m_amountsPerOrder.size() == 0 ? 0 : m_amountsPerOrder.size() - 1);
    }

    public void exp(HugeNumber initial, double base, int level)
    {
        double mul = Math.pow(base, (double)level);
        for(int i = 0; i < initial.m_amountsPerOrder.size(); ++i)
        {
            int amount = initial.m_amountsPerOrder.get(i);
            if(amount == 0) continue;
            
            amount *= mul;
            
            
        }
    }
}
