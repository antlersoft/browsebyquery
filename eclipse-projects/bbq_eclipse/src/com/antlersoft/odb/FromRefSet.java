package odb;

import java.util.Collection;
import java.util.Set;

class FromRefSet extends FromRefCollection implements Set
{
    FromRefSet( Persistent containing, Set s)
    {
        super( containing, s);
    }
}