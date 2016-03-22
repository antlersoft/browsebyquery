//------------------------------------------------------------------------------
// <copyright file="ResultWindowControl.xaml.cs" company="Company">
//     Copyright (c) Company.  All rights reserved.
// </copyright>
//------------------------------------------------------------------------------

using System;
using System.Collections.Generic;
using System.Deployment.Internal;
using System.IO;
using System.Windows.Controls;
using System.Windows.Input;
using com.antlersoft.BBQClient;
using EnvDTE;
using EnvDTE80;

namespace BBQVSIX
{

    /// <summary>
    /// Interaction logic for ResultWindowControl.
    /// </summary>
    public partial class ResultWindowControl : UserControl
    {
        private DTE2 applicationObject;
        private ResponseObject[] _currentResponses;

        internal QueryWindowPackage Package
        {
            get { return (QueryWindowPackage)QueryWindowCommand.Instance.ServiceProvider; }
        }

        /// <summary>
        /// Initializes a new instance of the <see cref="ResultWindowControl"/> class.
        /// </summary>
        public ResultWindowControl()
        {
            InitializeComponent();
            ResultList.MouseDoubleClick += ResultListOnMouseDoubleClick;
        }

        private void ResultListOnMouseDoubleClick(object sender, MouseButtonEventArgs mouseButtonEventArgs)
        {
            ResponseObject obj = (ResponseObject) ResultList.SelectedItem;
            if (!String.IsNullOrEmpty(obj.FileName) && File.Exists(obj.FileName))
            {
                DTE2 applicationObject = (DTE2) (((IServiceProvider) Package).GetService(typeof (DTE)));
                Window2 docWindow = (Window2)applicationObject.ItemOperations.OpenFile(obj.FileName, Constants.vsViewKindTextView);
                if (docWindow != null)
                {
                    docWindow.Activate();
                    TextDocument td = applicationObject.ActiveDocument.Object("") as TextDocument;
                    int maxLine = -1;
                    if (td != null)
                    {
                        maxLine = td.EndPoint.Line;
                    }
                    if (obj.LineNumber > 0 && obj.LineNumber < maxLine)
                    {
                        TextSelection sel = (TextSelection)applicationObject.ActiveDocument.Selection;
                        if (sel != null)
                        {
                            //sel.SelectLine();
                            //sel.GotoLine(obj.LineNumber, false);
                            sel.MoveToLineAndOffset(obj.LineNumber, 1, false);
                            sel.SelectLine();
                            sel.TopPoint.TryToShow(vsPaneShowHow.vsPaneShowCentered, 1);
                        }
                    }
                }
            }
        }

        public void AddSelectedObjectKeys(List<string> collection)
        {
            foreach (ResponseObject i in ResultList.SelectedItems)
            {
                var key = i.ObjectKey;
                if (!string.IsNullOrEmpty(key))
                    collection.Add(key);
            }
        }

        internal void HandleResponses(ResponseObject[] results)
        {
            ResultList.Items.Clear();
            _currentResponses = results;
            foreach (ResponseObject response in results)
            {
                ResultList.Items.Add(response);
            }
        }
    }
}