//------------------------------------------------------------------------------
// <copyright file="QueryWindowControl.xaml.cs" company="Company">
//     Copyright (c) Company.  All rights reserved.
// </copyright>
//------------------------------------------------------------------------------

using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Windows.Input;
using com.antlersoft.BBQClient;

namespace BBQVSIX19
{
    using System.Windows;
    using System.Windows.Controls;

    /// <summary>
    /// Interaction logic for QueryWindowControl.
    /// </summary>
    public partial class QueryWindowControl : UserControl
    {
        private BackgroundWorker queryWorker;
        private IBbqConfig config;
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
            HistoryListBox.MouseDoubleClick += HistoryListBoxOnMouseDoubleClick;
        }

        private void HistoryListBoxOnMouseDoubleClick(object sender, MouseButtonEventArgs mouseButtonEventArgs)
        {
            QueryText.Text = HistoryListBox.SelectedItem.ToString();
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
            var newConfig = QueryPackage.BbqConfig;
            if (newConfig == null)
            {
                MessageBox.Show("Unable to retrieve configuration options for BBQ plugin");
                return;
            }
            if (config == null)
            {
                DatabaseName.Text = newConfig.DefaultDatabase ?? string.Empty;
            }
            if (config == null || ! config.Equals(newConfig))
            {
                config = new BbqConfig(newConfig);
                if (BrowseByQuery is IDisposable disposable)
                {
                    disposable.Dispose();
                }
                if (config.UseLegacyService)
                {
                    BrowseByQuery = new BrowseByQueryBySocket();
                }
                else
                {
                    BrowseByQuery = new BrowseByQueryByJsonWebService(config.WebServiceUrl, config.UserName, config.ApiKey);
                }
            }
            string text = QueryText.Text;
            QueryRequest request = new QueryRequest(text);
            request.DatabaseName = DatabaseName.Text;
            ResultControl.AddSelectedObjectKeys(request.ObjectKeys);
            queryWorker.RunWorkerAsync(request);
            QueryButton.IsEnabled = false;

        }
        private void RunQuery(object sender, DoWorkEventArgs e)
        {
            if (e == null || e.Argument == null || BrowseByQuery == null)
            {
                return;
            }
            e.Result = new KeyValuePair<QueryRequest, QueryResponse>((QueryRequest)e.Argument, BrowseByQuery.PerformQuery((QueryRequest)e.Argument));
        }

        private void PostQueryResults(object sender, RunWorkerCompletedEventArgs e)
        {
            QueryButton.IsEnabled = true;
            if (e?.Error != null)
            {
                MessageBox.Show(e.Error.Message + " " + e.Error.StackTrace, "Failed to Send Query");
            }
            else if (e == null || e.Result == null)
            {
                MessageBox.Show($"PostQueryResults args {(e == null ? "are null" : "Result null without error")}");
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