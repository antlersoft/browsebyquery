using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
using System.IO;
using System.Text;
using System.Windows.Forms;

using EnvDTE;
using EnvDTE80;
using EnvDTE90;

namespace com.antlersoft.BBQAddIn
{
    public partial class ResultWindow : UserControl
    {
        private DTE2 applicationObject;
        private ResponseObject[] currentResponses;

        public DTE2 ApplicationObject
        {
            get { return applicationObject; }
            set { applicationObject = value; }
        }

        public Window2 ContainingWindow
        {
            get;
            set;
        }

        internal void HandleResponses(ResponseObject[] results)
        {
            resultList.Items.Clear();
            currentResponses = results;
            foreach (ResponseObject response in results)
            {
                resultList.Items.Add(response.Description);
            }
            if (ContainingWindow != null)
            {
                ContainingWindow.Activate();
            }
        }

        public ResultWindow()
        {
            InitializeComponent();
        }

        public ListBox ResultListBox
        {
            get { return resultList; }
        }

        public void AddSelectedObjectKeys(List<String> collection)
        {
            foreach (int i in resultList.SelectedIndices)
            {
                collection.Add(currentResponses[i].ObjectKey);
            }
        }

        private void JumpToSelected(object sender, EventArgs e)
        {
            ListBox.SelectedIndexCollection indices = resultList.SelectedIndices;
            int index = -1;
            if (indices.Count > 0)
                index = indices[0];
            if (index >= 0 && index < currentResponses.Length)
            {
                ResponseObject obj = currentResponses[index];
                if (!String.IsNullOrEmpty(obj.FileName) && File.Exists(obj.FileName))
                {
                    Window2 docWindow = (Window2)ApplicationObject.ItemOperations.OpenFile(obj.FileName, Constants.vsViewKindTextView);
                    if (docWindow != null)
                    {
                        docWindow.Activate();
                        TextDocument td = ApplicationObject.ActiveDocument.Object("") as TextDocument;
                        int maxLine = -1;
                        if (td != null)
                        {
                            maxLine = td.EndPoint.Line;
                        }
                        if (obj.LineNumber > 0 && obj.LineNumber < maxLine)
                        {
                            TextSelection sel = (TextSelection)ApplicationObject.ActiveDocument.Selection;
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
        }

        private void Copy(object sender, EventArgs e)
        {
            StringBuilder sb = new StringBuilder();
            foreach (String s in resultList.SelectedItems)
            {
                if (sb.Length > 0)
                    sb.Append("\r\n");
                sb.Append(s);
            }
            Clipboard.SetText(sb.ToString());
        }

        private void resultMenu_Opening(object sender, CancelEventArgs e)
        {

        }

        private void SelectAll(object sender, EventArgs e)
        {
            for (int i = 0; i < currentResponses.Length; i++)
                resultList.SetSelected(i, true);
        }
    }
}
