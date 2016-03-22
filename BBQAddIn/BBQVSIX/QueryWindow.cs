//------------------------------------------------------------------------------
// <copyright file="QueryWindow.cs" company="Company">
//     Copyright (c) Company.  All rights reserved.
// </copyright>
//------------------------------------------------------------------------------

using com.antlersoft.BBQClient;

namespace BBQVSIX
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
    [Guid("8fb4947c-a6e8-40c5-8b50-f217586c5f95")]
    public class QueryWindow : ToolWindowPane
    {
        private BrowseByQueryBySocket _bbq;
        /// <summary>
        /// Initializes a new instance of the <see cref="QueryWindow"/> class.
        /// </summary>
        public QueryWindow() : base(null)
        {
            this.Caption = "Browse by Query";

            _bbq = new BrowseByQueryBySocket();
            // This is the user control hosted by the tool window; Note that, even if this class implements IDisposable,
            // we are not calling Dispose on this object. This is because ToolWindowPane calls Dispose on
            // the object returned by the Content property.
            var content = new QueryWindowControl();
            content.BrowseByQuery = _bbq;
            this.Content = content;
        }

        protected override void OnClose()
        {
            _bbq.Close();
            base.OnClose();
        }
    }
}
