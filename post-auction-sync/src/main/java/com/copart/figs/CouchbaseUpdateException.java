package com.copart.figs;

/**
 * Exception class for catching exception and performing operations on couchbase update exception 
 *
 */
public class CouchbaseUpdateException extends Exception
{

    /**
     * static long serial version id
     */
    private static final long serialVersionUID = 1L;
    
    public CouchbaseUpdateException(Exception ex){
        super(ex);
    }
}
