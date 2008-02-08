This is an Add-In for Visual Studio 2005.  I don't know if it will work for VS 2008.

This add-in requires that you run the Browse by Query for C#/CIL
standalone Java process, version 0.1.5, found in bbqilstandalone_0.1.5.jar
on the same machine on which you are running Visual Studio and the add-in.
The Java process creates and updates the program datastore from your assembly
files, and processes queries sent from this add-in.

After installing the add-in, you will see a command for it in the Visual Studio
Tool menu (with a happy-face icon :) ).  Selecting this command will start the
add-in windows: a query window, which you'll want to dock in a vertical format,
and a result window, which docks in a horizontal format.


Enter queries in the query window and press the query button.  The query is
transmitted on TCP port 22017 to the standalone process, which processes it
and returns the results.  The results are displayed in the result window.  Doubleclick
(or right-click) a result to open the relevant file/line in the Visual Studio
editor (as in other BBQ IDE plugins...).