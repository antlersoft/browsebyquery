/**
 * Copyright (c) 2008 Michael A. MacDonald
 */
package com.antlersoft.query.environment.ui;

import java.io.File;

import com.antlersoft.query.DataSource;

/**
 * A given app can talk to multiple DataSource objects, one at a time.
 * <p>
 * This is the interface between the user interface and the object that manages
 * these DataSource objects.
 *
 * @author Michael A. MacDonald
 *
 */
public interface DBContainer {

    /**
     * Get the DataSource used for this app.
     * The returned value may be null, which indicates to the caller
     * that openDB needs to be called to populate this container.
     * @return Object implementing DataSource, or null indicating
     * a call to openDB is expected
     */
    abstract public DataSource getDataSource();
    
    /**
     * Close the datasource on shutdown
     *
     */
    abstract public void closeDB() throws Exception;
    
    /**
     * Make sure query db is sync'd to filesystem
     *
     */
    abstract public void saveDB() throws Exception;
    
    /**
     * Open a new data source at the given file system location
     * @param File Place to open the database
     */
    abstract public void openDB( File location) throws Exception;
    
    /**
     * Clear the queried db
     */
    abstract public void clearDB() throws Exception;
    
    /**
     * Return true if the database has been cleared since the last call to analyzeFiles
     */
    abstract public boolean isCleared() throws Exception;
    
    /**
     * Message resulting from opening the database
     * @return String for message; or null if no message needs to be displayed
     */
    abstract public String getOpenMessage();

    /**
     * Name of currently open database as it might be displayed in the title bar.
     * This method should always return a valid string, even if the implementation
     * does not support the openDB functionality.
     * @return Name of currently open database.
     */
    abstract public String titleOfDB();
    
    /**
     * Returns file used to persist the environment
     */
    abstract public File getEnvironmentFile();

    /**
     * Enter the selected files into the database to be queried.  The default implementation does nothing.
     * The frame may run this command in a separate thread-- the implementation should not
     * interact directly with objects on the UI thread.
     * @param selected_files
     */
    abstract public void analyze( File[] selected_files) throws Exception;
}
