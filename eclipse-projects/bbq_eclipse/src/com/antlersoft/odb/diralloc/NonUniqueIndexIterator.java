package com.antlersoft.odb.diralloc;

class NonUniqueIndexIterator extends IndexIterator
{
	Comparable origKey;

	NonUniqueIndexIterator( Index b, IndexPage p, int o, boolean m, Comparable k)
	{
		super( b, p, o, m);
		origKey=k;
	}

	public Object next()
	{
		boolean oldMatch=exactMatch;

		Object result=super.next();

		if ( oldMatch && hasNext())
		{
			exactMatch=origKey.compareTo( ((UniqueKey)currentPage.keyArray[currentOffset]).base)==0;
		}
		return result;
	}
}