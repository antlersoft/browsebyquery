using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Drawing;
using System.Data;
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

        internal void HandleResponses(ResponseObject[] results)
        {
            resultList.Items.Clear();
            currentResponses = results;
            foreach (ResponseObject response in results)
            {
                resultList.Items.Add(response.Description);
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
                if (!String.IsNullOrEmpty(obj.FileName))
                {
                    Window2 docWindow = (Window2)ApplicationObject.ItemOperations.OpenFile(obj.FileName, Constants.vsViewKindTextView);
                    if (docWindow != null)
                    {
                        docWindow.Activate();
                        if (obj.LineNumber > 0)
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
    }
}
