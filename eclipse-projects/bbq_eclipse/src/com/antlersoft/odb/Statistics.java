/*
 * <p>Copyright (c) 2000-2005  Michael A. MacDonald<p>
 * ----- - - -- - - --
 * <p>
 *     This package is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 * <p>
 *     This package is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * <p>
 *     You should have received a copy of the GNU General Public License
 *     along with the package (see gpl.txt); if not, see www.gnu.org
 * <p>
 * ----- - - -- - - --
 */
package com.antlersoft.odb;

public class Statistics
{
	public Statistics()
	{
		reset();
	}

	public void reset()
	{
		count=0;
		sum=0;
		sumSquares=0;
		max= -Double.MIN_VALUE;
		min= Double.MAX_VALUE;
		secondMax=max;
		secondMin=min;
	}

	public void addValue( double newValue)
	{
		count++;
		sum+=newValue;
		sumSquares+=(newValue*newValue);
		if ( newValue>secondMax)
		{
			secondMax=newValue;
			if ( newValue>max)
			{
				secondMax=max;
				max=newValue;
			}
		}
		else if ( newValue<secondMin)
		{
			secondMin=newValue;
			if ( newValue<min)
			{
				secondMin=min;
				min=newValue;
			}
		}
	}

	public String toString()
	{
		if ( count==0)
			return "No entries";
		return "Count: "+Integer.toString( count)+" Avg: "+Double.toString( sum/count)+" StdDev: "+Double.
			toString( Math.sqrt( sumSquares/count)-(sum/count))+"\nMin to max: "+Double.toString( min)+
			" "+Double.toString( secondMin)+" "+Double.toString( secondMax)+" "+Double.toString( max);
	}

	private int count;
	private double sum;
	private double sumSquares;
	private double max;
	private double min;
	private double secondMax;
	private double secondMin;
}