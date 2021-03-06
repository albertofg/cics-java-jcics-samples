/* Licensed Materials - Property of IBM                                   */
/*                                                                        */
/* SAMPLE                                                                 */
/*                                                                        */
/* (c) Copyright IBM Corp. 2017 All Rights Reserved                       */
/*                                                                        */
/* US Government Users Restricted Rights - Use, duplication or disclosure */
/* restricted by GSA ADP Schedule Contract with IBM Corp                  */
/*                                                                        */

package com.ibm.cicsdev.vsam.rrds;

import java.util.List;

import com.ibm.cics.server.Task;
import com.ibm.cicsdev.bean.StockPart;
import com.ibm.cicsdev.vsam.StockPartHelper;

/**
 * Simple example to demonstrate browsing a VSAM RRDS file using JCICS.
 * 
 * This class is just the driver of the test. The main JCICS work is done in the
 * common class {@link RrdsExampleCommon}.
 */
public class RrdsExample5 
{
    /**
     * Number of records to add and then browse through.
     */
    private static final int RECORDS_TO_BROWSE = 5;
    
    /**
     * Main entry point to a CICS OSGi program.
     * 
     * The FQ name of this class should be added to the CICS-MainClass entry in
     * the parent OSGi bundle's manifest.
     */    
    public static void main(String[] args)
    {
        // Get details about our current CICS task
        Task task = Task.getTask();
        task.out.println(" - Starting RrdsExample5");
        task.out.println("VSAM RRDS file browse example");

        // Create a new instance of the common example class
        RrdsExampleCommon ex = new RrdsExampleCommon();

        // Unlike the KSDS and ESDS examples, we need an empty file before we start
        ex.emptyFile();
        
        // We will always start from RRN 1
        long rrnStart = 1;
        
        
        /*
         * Create some records in the file so we have something to work with.
         */
        
        // Add records
        for ( long rrn = rrnStart; rrn <= RECORDS_TO_BROWSE; rrn++ ) {
            
            // Add a new record to the file 
            StockPart sp = StockPartHelper.generate();
            ex.addRecord(rrn, sp);
            
            // Write out the key and description
            task.out.println( String.format("Wrote to RRN 0x%016X", rrn) );
        }
        
        // Commit the unit of work to harden the inserts to the file
        ex.commitUnitOfWork();            
        

        /*
         * Browse through the file, starting at the lowest key.
         * 
         * The above code will have guaranteed that sufficient records exist.
         */
        
        // Browse through the records, starting at the initial RRN
        List<StockPart> list = ex.browse(rrnStart, RECORDS_TO_BROWSE);
        
        // Iterate over this list
        for ( StockPart sp : list ) {
            
            // Display the description
            String strMsg = "Read record with description %s";
            task.out.println( String.format(strMsg, sp.getDescription().trim()) );
        }
        
        // Completion message
        task.out.println("Completed RrdsExample5");
    }
}
