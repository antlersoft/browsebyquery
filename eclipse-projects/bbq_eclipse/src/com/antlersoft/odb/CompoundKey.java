package com.antlersoft.odb;

public class CompoundKey implements Comparable {
	private Comparable key1;
	private Comparable key2;

	public CompoundKey( Comparable k1, Comparable k2)
	{
		key1=k1;
		key2=k2;
	}
    public int compareTo(Object o) {
		CompoundKey ck=(CompoundKey)o;
		int result=key1.compareTo( ck.key1);
		if ( result==0)
			result=key2.compareTo( ck.key2);
		return result;
    }
}