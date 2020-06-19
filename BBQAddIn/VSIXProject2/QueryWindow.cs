namespace BBQVSIX19
{
    using System;
    using System.Runtime.InteropServices;
    using Microsoft.VisualStudio.Shell;

    /// <summary>
    /// This class implements the tool window exposed by this package and hosts a user control.
    /// </summary>
    /// <remarks>
    /// In Visual Studio tool windows are composed of a frame (implemented by the shell) and a pane,
    /// usually implemented by the package implementer.
    /// <para>
    /// This class derives from the ToolWindowPane class provided from the MPF in order to use its
    /// implementation of the IVsUIElementPane interface.
    /// </para>
    /// </remarks>
    [Guid("f2ee9ae9-2a67-40b1-8f58-e0bbeba57cf0")]
    public class QueryWindow : ToolWindowPane
    {
        /// <summary>
        /// Initializes a new instance of the <see cref="QueryWindow"/> class.
        /// </summary>
        public QueryWindow() : base(null)
        {
            this.Caption = "Browse by Query";

            // This is the user control hosted by the tool window; Note that, even if this class implements IDisposable,
            // we are not calling Dispose on this object. This is because ToolWindowPane calls Dispose on
            // the object returned by the Content property.
            this.Content = new QueryWindowControl();
        }
    }
}
