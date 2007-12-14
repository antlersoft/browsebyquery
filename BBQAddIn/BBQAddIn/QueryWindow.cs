using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.Text;
using System.Windows.Forms;

using EnvDTE80;

namespace com.antlersoft.BBQAddIn
{
    public partial class QueryWindow : UserControl
    {
        private DTE2 applicationObject;

        public DTE2 ApplicationObject
        {
            get { return applicationObject; }
            set { applicationObject = value; }
        }

        private IBrowseByQuery bbq;

        public QueryWindow()
        {
            InitializeComponent();
            bbq = new BrowseByQueryBySocket();
        }

        private ResultWindow resultWindow;

        public ResultWindow ResultWindow
        {
            get { return resultWindow; }
            set { resultWindow = value; }
        }

        public TextBox QueryText
        {
            get { return queryText; }
        }

        // Start the query running on the queryWorker background worker
        private void StartQuery(object sender, EventArgs e)
        {
            String text = QueryText.Text;
            queryWorker.RunWorkerAsync(new QueryRequest(text));
            queryButton.Enabled=false;
            foreach (Object o in historyList.Items)
            {
                if (o.ToString() == text)
                {
                    historyList.Items.Remove(o);
                    break;
                }
            }
            historyList.Items.Insert(0, text);
        }

        private void RunQuery(object sender, DoWorkEventArgs e)
        {
            e.Result = bbq.PerformQuery((QueryRequest)e.Argument);
        }

        private void PostQueryResults(object sender, RunWorkerCompletedEventArgs e)
        {
            queryButton.Enabled=true;
            if (e.Error != null)
            {
                MessageBox.Show(e.Error.Message+" "+e.Error.StackTrace, "Failed to Send Query");
            }
            else
            {
                QueryResponse response = (QueryResponse)e.Result;
                if (response.RequestException != null)
                {
                    MessageBox.Show(response.RequestException.Message, "Problem Running Query");
                }
                else
                {
                    ResultWindow.HandleResponses(response.Responses);
                }
            }
        }

        private void CopyFromHistory(object sender, EventArgs e)
        {
            int index = historyList.SelectedIndex;
            if (index >= 0)
            {
                String text = historyList.Items[index].ToString();
                historyList.Items.RemoveAt(index);
                historyList.Items.Insert(0, text);
                queryText.Text = text;
            }
        }
    }
}
