package analyzer;

import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Enumeration;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UIQuery
{
    AnalyzerDB analyzerDB;
    QueryParser qp;
    JTextArea queryArea;
    JTextArea outputArea;

    UIQuery()
    {
	analyzerDB=new SimpleAnalyzerDB();
	qp=new QueryParser();
    }

    Component createComponents()
    {
	// Create components
	JButton queryButton=new JButton( "Query");
	Dimension buttonDimension=queryButton.getPreferredSize();
	queryButton.setMinimumSize( buttonDimension);
	queryButton.setMaximumSize( buttonDimension);
	queryArea=new JTextArea( 2, 80);
	JScrollPane queryScroll=new JScrollPane( queryArea);
	Box upperPane=Box.createHorizontalBox();
	upperPane.add( queryButton);
	upperPane.add( queryScroll);
	outputArea=new JTextArea( 16, 80);
	JScrollPane lowerPane=new JScrollPane( outputArea);
        JSplitPane mainPane=new JSplitPane( JSplitPane.VERTICAL_SPLIT, upperPane, lowerPane);

	// Create actions
	outputArea.setEditable( false);
	queryButton.addActionListener( new ActionListener() {
		public void actionPerformed( ActionEvent ae)
		{
		    try
		    {
			String line=queryArea.getText();
			if ( line==null || line.length()==0)
			    return;
			qp.setLine( line);
			QueryParser.SetExpression se=qp.getExpression();
			Enumeration e=se.execute( analyzerDB);
			StringBuffer results=new StringBuffer();
			while ( e.hasMoreElements())
			{
			    results.append( e.nextElement().toString());
			    results.append( '\n');
			}
			outputArea.setText( results.toString());
		    }
		    catch ( Exception pe)
		    {
			JOptionPane.showMessageDialog( outputArea, pe.getMessage(),
			    "Parse Error", JOptionPane.ERROR_MESSAGE);
		    }		    
		}
	    });

	return mainPane;
    }

    public static void main( String argv[])
	throws Exception
    {
	final UIQuery app = new UIQuery();
	app.analyzerDB.openDB( argv[0]);

	JFrame appFrame=new JFrame( "Querying "+argv[0]);
        Component contents = app.createComponents();
        appFrame.getContentPane().add(contents, BorderLayout.CENTER);
        //Finish setting up the frame, and show it.
        appFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
		try
		{
		    app.analyzerDB.closeDB();
		}
		catch ( Exception exception)
		{
		}
                System.exit(0);      }        });
        appFrame.pack();
        appFrame.setVisible(true);
    }
}
