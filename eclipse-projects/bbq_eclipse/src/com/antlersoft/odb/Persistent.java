package odb;

import java.io.Serializable;

/**
 * Interface that defines the only method objects need to support to be persistent.
 * Obviously, we assume that the objects are also serializable.  It is also possible
 * to implement this interface so persistence breaks, but you would have to avoid
 * the obvious implementation.
 *
 * The first class in the class hierarchy that is persistent should contain a
 * private transient PersistentImpl member, which this class will return.  It should initialize
 * this member when constructed by calling its empty constructor.  Classes derived
 * from a Persistent class won't have to do anything.
 */
public interface Persistent extends Serializable
{
    public PersistentImpl _getPersistentImpl();
}
