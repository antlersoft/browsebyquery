//------------------------------------------------------------------------------
// <copyright file="ResultWindowControl.xaml.cs" company="Company">
//     Copyright (c) Company.  All rights reserved.
// </copyright>
//------------------------------------------------------------------------------

using System;
using System.Collections.Generic;
using System.Deployment.Internal;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Input;
using com.antlersoft.BBQClient;
using EnvDTE;
using EnvDTE80;

namespace BBQVSIX19
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
            var menu = ContextMenu;
            if (menu == null)
            {
                menu = new ContextMenu();
                ContextMenu = menu;
            }
            MenuItem showPaths = new MenuItem();
            showPaths.Header = "Show paths resulting from configured source expressions";
            showPaths.Click += ShowPaths_Click;
            menu.Items.Add(showPaths);
            MenuItem goTo = new MenuItem();
            goTo.Header = "Go to file/line (if available)";
            goTo.Click += GoTo_Click;
            menu.Items.Add(goTo);
            ResultList.MouseDoubleClick += ResultListOnMouseDoubleClick;
        }

        private void GoTo_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            bool hasInfo = false;
            var obj = (ResponseObject)ResultList.SelectedItem;
            if (obj != null)
            {
                hasInfo = GetPaths(obj).Any();
            }
            if (hasInfo)
            {
                ResultListOnMouseDoubleClick(sender, null);
            }
            else
            {
                MessageBox.Show("No path information for selected result");
            }
        }

        private void ShowPaths_Click(object sender, System.Windows.RoutedEventArgs e)
        {
            var obj = (ResponseObject)ResultList.SelectedItem;
            if (obj == null)
            {
                return;
            }
            StringBuilder sb = new StringBuilder();
            foreach (var s in GetPaths(obj))
            {
                sb.AppendLine(s);
            }
            if (sb.Length == 0)
            {
                MessageBox.Show("No path information available for selected result");
            }
            else
            {
                MessageBox.Show(sb.ToString());
            }
        }

        private IEnumerable<string> GetPaths(ResponseObject obj)
        {
            if (!String.IsNullOrEmpty(obj.FileName))
            {
                foreach (var sub in Package.BbqConfig.Substitutions??new List<Substitution>())
                {
                    string result = null;
                    try
                    {
                        result = new Regex(sub.MatchExpression).Replace(obj.FileName, sub.ReplaceExpression);
                    }
                    catch
                    {

                    }
                    if (result != null)
                    {
                        yield return result;
                    }
                }
                yield return obj.FileName;
            }
        }

        private void ResultListOnMouseDoubleClick(object sender, MouseButtonEventArgs mouseButtonEventArgs)
        {
            ResponseObject obj = (ResponseObject) ResultList.SelectedItem;
            if (obj == null)
            {
                return;
            }
            foreach (string path in GetPaths(obj))
            {
                if (File.Exists(path))
                {
                    DTE2 applicationObject = (DTE2)(((IServiceProvider)Package).GetService(typeof(DTE)));
                    Window2 docWindow = (Window2)applicationObject.ItemOperations.OpenFile(path, Constants.vsViewKindTextView);
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
                        // If successfully opened one window, we are done
                        return;
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