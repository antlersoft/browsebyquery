//------------------------------------------------------------------------------
// <copyright file="QueryWindowControl.xaml.cs" company="Company">
//     Copyright (c) Company.  All rights reserved.
// </copyright>
//------------------------------------------------------------------------------

using System;
using System.Collections.Generic;
using System.ComponentModel;
using com.antlersoft.BBQClient;

namespace BBQVSIX
{
    using System.Windows;
    using System.Windows.Controls;

    /// <summary>
    /// Interaction logic for QueryWindowControl.
    /// </summary>
    public partial class QueryWindowControl : UserControl
    {
        private BackgroundWorker queryWorker;
        /// <summary>
        /// Initializes a new instance of the <see cref="QueryWindowControl"/> class.
        /// </summary>
        public QueryWindowControl()
        {
            this.InitializeComponent();
            QueryButton.Click += OnClick;
            queryWorker = new BackgroundWorker();
            queryWorker.DoWork += new System.ComponentModel.DoWorkEventHandler(this.RunQuery);
            queryWorker.RunWorkerCompleted += new System.ComponentModel.RunWorkerCompletedEventHandler(this.PostQueryResults);
        }

        internal IBrowseByQuery BrowseByQuery { get; set; }

        internal QueryWindowPackage QueryPackage { get
        {
            return (QueryWindowPackage) QueryWindowCommand.Instance.ServiceProvider;
        } }

        internal ResultWindowControl ResultControl { get { return QueryPackage.ResultControl; } }

        private void OnClick(object sender, RoutedEventArgs args)
        {
            if (QueryPackage == null)
            {
                MessageBox.Show("Query Package not set", "Error");
                return;
            }
            if (ResultControl == null)
            {
                MessageBox.Show("Please open result window", "Result Window Missing");
                return;
            }
            string text = QueryText.Text;
            QueryRequest request = new QueryRequest(text);
            ResultControl.AddSelectedObjectKeys(request.ObjectKeys);
            queryWorker.RunWorkerAsync(request);
            QueryButton.IsEnabled = false;

        }
        private void RunQuery(object sender, DoWorkEventArgs e)
        {
            e.Result = new KeyValuePair<QueryRequest, QueryResponse>((QueryRequest)e.Argument, BrowseByQuery.PerformQuery((QueryRequest)e.Argument));
        }

        private void PostQueryResults(object sender, RunWorkerCompletedEventArgs e)
        {
            QueryButton.IsEnabled = true;
            if (e.Error != null)
            {
                MessageBox.Show(e.Error.Message + " " + e.Error.StackTrace, "Failed to Send Query");
            }
            else
            {
                QueryResponse response = ((KeyValuePair<QueryRequest, QueryResponse>)e.Result).Value;
                if (response.RequestException != null)
                {
                    MessageBox.Show(response.RequestException.Message, "Problem Running Query");
                }
                else
                {
                    ResultControl.HandleResponses(response.Responses);
                    String text = ((KeyValuePair<QueryRequest, QueryResponse>)e.Result).Key.QueryText;
                    foreach (Object o in HistoryListBox.Items)
                    {
                        if (o.ToString() == text)
                        {
                            HistoryListBox.Items.Remove(o);
                            break;
                        }
                    }
                    HistoryListBox.Items.Insert(0, text);
                }
            }
        }

    }
}