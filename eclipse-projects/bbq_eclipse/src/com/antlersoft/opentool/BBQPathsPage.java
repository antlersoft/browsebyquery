
/**
 * Title:        Remote File System for OpenTools<p>
 * Description:  An OpenTools vfs filesystem for accessing files on a remote host<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      antlersoft<p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.opentool;

import java.io.File;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JFileChooser;

import com.borland.primetime.ide.Browser;
import com.borland.primetime.properties.*;
import com.borland.primetime.help.HelpTopic;
import com.borland.primetime.vfs.Url;

import com.borland.jbuilder.JBuilderHelp;
import com.borland.jbuilder.node.JBProject;

import com.antlersoft.analyzer.ObjectAnalyzeDB;

class BBQPathsPage extends PropertyPage
{
    private JLabel pathDisplay;
    private JFileChooser chooser;
    private JTextField classEntry;
    private JButton setPath;
    private BBQNode node;

    BBQPathsPage( BBQNode proj)
    {
        node=proj;
        chooser=new JFileChooser();
        chooser.setFileSelectionMode( JFileChooser.FILES_AND_DIRECTORIES);
        Box box=Box.createVerticalBox();
        JButton setPath=new JButton( "Change database");
        setPath.addActionListener( new SetPathListener());
        box.add( new JLabel("Currently selected database:"));
        pathDisplay=new JLabel();
        box.add( pathDisplay);
        box.add( setPath);
        box.add( new JLabel( "Non-default class path:"));
        classEntry=new JTextField( 60);
        box.add( classEntry);
        add( box);
    }

    public void readProperties()
    {
        String path=BBQPathsGroup.pathsProperty.getValue( node);
        if ( path.length()==0)
        {
            path=new File( node.getProject().getProjectPath().getFileObject(),
                "analyzer.pj").getPath();
        }
        pathDisplay.setText( path);
        classEntry.setText( BBQPathsGroup.classPathProperty.getValue( node));
    }

    public boolean isValid()
    {
        try
        {
            Url.parsePath( classEntry.getText());
        }
        catch ( Exception e)
        {
            reportValidationError( classEntry, e.getMessage());
            return false;
        }
        return true;
    }

    public HelpTopic getHelpTopic()
    {
        return JBuilderHelp.TOPIC_DEFAULT;
    }

    public void writeProperties()
    {
        node.getTool().setAnalyzerPath( node, pathDisplay.getText());
        BBQPathsGroup.classPathProperty.setValue( node, classEntry.getText());
    }

    class SetPathListener implements ActionListener
    {
        public void actionPerformed( ActionEvent ae)
        {
            chooser.setCurrentDirectory( node.getProject().getProjectPath().
                getFileObject());
            if ( chooser.showDialog( Browser.findBrowser( node), "Select")==
                JFileChooser.APPROVE_OPTION)
            {
                pathDisplay.setText( chooser.getSelectedFile().getPath());
            }
        }
    }
}