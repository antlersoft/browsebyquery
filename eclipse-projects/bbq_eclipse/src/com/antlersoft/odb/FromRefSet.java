package odb;

import java.util.Collection;
import java.util.Set;

class FromRefSet extends FromRefCollection implements Set
{
	FromRefSet( Set s)
	{
		super( s);
	}
}