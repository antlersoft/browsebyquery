/**
 * Copyright (c) 2010 Michael A. MacDonald
 */
package com.antlersoft.odb;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;


/**
 * <p>Provides thread-safe implementations of most methods in DiskAllocator.
 * </p><p>
 * Note that the iterator returned by iterator() is not thread-safe and will be rendered
 * invalid if any changes are made.
 * </p>
 * @author Michael A. MacDonald
 *
 */
public class SafeDiskAllocator extends DiskAllocator {

	/**
	 * @param file
	 * @param initialRegion
	 * @param chunkSize
	 * @param sizeIncrement
	 * @param creationFlags
	 * @throws IOException
	 * @throws DiskAllocatorException
	 */
	public SafeDiskAllocator(File file, int initialRegion, int chunkSize,
			int sizeIncrement, int creationFlags) throws IOException,
			DiskAllocatorException {
		super(file, initialRegion, chunkSize, sizeIncrement, creationFlags);
	}

	/**
	 * @param file
	 * @throws IOException
	 * @throws DiskAllocatorException
	 */
	public SafeDiskAllocator(File file) throws IOException,
			DiskAllocatorException {
		super(file);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.DiskAllocator#allocate(int)
	 */
	@Override
	public synchronized int allocate(int size) throws IOException, DiskAllocatorException {
		return super.allocate(size);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.DiskAllocator#close()
	 */
	@Override
	public synchronized void close() throws IOException {
		super.close();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.DiskAllocator#free(int)
	 */
	@Override
	public synchronized void free(int regionOffset) throws IOException,
			DiskAllocatorException {
		super.free(regionOffset);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.DiskAllocator#getRegionSize(int)
	 */
	@Override
	public synchronized int getRegionSize(int regionOffset) throws IOException,
			DiskAllocatorException {
		return super.getRegionSize(regionOffset);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.DiskAllocator#iterator()
	 */
	@Override
	public synchronized Iterator<Integer> iterator() throws IOException {
		return super.iterator();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.DiskAllocator#read(int, int)
	 */
	@Override
	public synchronized byte[] read(int offset, int size) throws IOException,
			DiskAllocatorException {
		return super.read(offset, size);			
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.DiskAllocator#sync()
	 */
	@Override
	public synchronized void sync() throws IOException {
		super.sync();
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.DiskAllocator#walkInternalFreeList(java.io.PrintStream)
	 */
	@Override
	public synchronized void walkInternalFreeList(PrintStream ps) {
		super.walkInternalFreeList(ps);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.DiskAllocator#walkRegions(java.io.PrintStream)
	 */
	@Override
	public synchronized void walkRegions(PrintStream ps) throws IOException,
			DiskAllocatorException {
		super.walkRegions(ps);
	}

	/* (non-Javadoc)
	 * @see com.antlersoft.odb.DiskAllocator#write(int, byte[])
	 */
	@Override
	public synchronized void write(int offset, byte[] toWrite) throws IOException,
			DiskAllocatorException {
		super.write(offset, toWrite);
	}
}
