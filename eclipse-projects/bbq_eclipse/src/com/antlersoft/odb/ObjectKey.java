package odb;

import java.io.Serializable;

/**
 * Tag interface marking an object as an ObjectKey; an opaque identifier
 * used by an object store to store and retrieve objects.  The implementation
 * of the object store provides the implementation of the object key.
 */
public interface ObjectKey extends Serializable
{
}
