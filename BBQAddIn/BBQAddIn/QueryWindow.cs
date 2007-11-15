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

        public QueryWindow()
        {
            InitializeComponent();
        }
    }
}
