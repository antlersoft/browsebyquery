package com.antlersoft.util;

import java.awt.event.*;

import java.io.IOException;

import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * <p>Title: Warroom</p>
 * <p>Description: Whiteboarding for WWIIOL</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: antlersoft</p>
 * @author Michael MacDonald
 * @version 1.0
 */

public class URLFrame extends JFrame
{
    public URLFrame( URL url)
    {
        JEditorPane editor;
        try
        {
            editor=new JEditorPane( url);
        }
        catch ( IOException ioe)
        {
            editor=new JEditorPane( "text/plain",
                "Unable to open page "+url.toString()+"\r\n\r\n"+
                ioe.getMessage());
        }
        editor.setEditable( false);
        getContentPane().add( new JScrollPane( editor));
        addWindowListener( new WindowAdapter() {
                public void windowClosing( WindowEvent e)
                {
                    dispose();
                }
            });
    }
}