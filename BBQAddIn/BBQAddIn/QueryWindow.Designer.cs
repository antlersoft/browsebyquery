namespace com.antlersoft.BBQAddIn
{
    partial class QueryWindow
    {
        /// <summary> 
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary> 
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Component Designer generated code

        /// <summary> 
        /// Required method for Designer support - do not modify 
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.queryText = new System.Windows.Forms.TextBox();
            this.historyList = new System.Windows.Forms.ListBox();
            this.querySplit = new System.Windows.Forms.SplitContainer();
            this.queryButton = new System.Windows.Forms.Button();
            this.queryWorker = new System.ComponentModel.BackgroundWorker();
            this.querySplit.Panel1.SuspendLayout();
            this.querySplit.Panel2.SuspendLayout();
            this.querySplit.SuspendLayout();
            this.SuspendLayout();
            // 
            // queryText
            // 
            this.queryText.AcceptsReturn = true;
            this.queryText.Dock = System.Windows.Forms.DockStyle.Fill;
            this.queryText.Location = new System.Drawing.Point(0, 0);
            this.queryText.Multiline = true;
            this.queryText.Name = "queryText";
            this.queryText.ScrollBars = System.Windows.Forms.ScrollBars.Both;
            this.queryText.Size = new System.Drawing.Size(567, 112);
            this.queryText.TabIndex = 0;
            // 
            // historyList
            // 
            this.historyList.Dock = System.Windows.Forms.DockStyle.Fill;
            this.historyList.FormattingEnabled = true;
            this.historyList.Location = new System.Drawing.Point(0, 0);
            this.historyList.Name = "historyList";
            this.historyList.Size = new System.Drawing.Size(567, 134);
            this.historyList.TabIndex = 1;
            this.historyList.DoubleClick += new System.EventHandler(this.CopyFromHistory);
            // 
            // querySplit
            // 
            this.querySplit.Dock = System.Windows.Forms.DockStyle.Fill;
            this.querySplit.Location = new System.Drawing.Point(0, 0);
            this.querySplit.Name = "querySplit";
            this.querySplit.Orientation = System.Windows.Forms.Orientation.Horizontal;
            // 
            // querySplit.Panel1
            // 
            this.querySplit.Panel1.Controls.Add(this.queryButton);
            this.querySplit.Panel1.Controls.Add(this.queryText);
            // 
            // querySplit.Panel2
            // 
            this.querySplit.Panel2.Controls.Add(this.historyList);
            this.querySplit.Size = new System.Drawing.Size(567, 258);
            this.querySplit.SplitterDistance = 112;
            this.querySplit.TabIndex = 2;
            // 
            // queryButton
            // 
            this.queryButton.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
            this.queryButton.Dock = System.Windows.Forms.DockStyle.Right;
            this.queryButton.Location = new System.Drawing.Point(492, 0);
            this.queryButton.Name = "queryButton";
            this.queryButton.Size = new System.Drawing.Size(75, 112);
            this.queryButton.TabIndex = 1;
            this.queryButton.Text = "Query";
            this.queryButton.UseVisualStyleBackColor = true;
            this.queryButton.Click += new System.EventHandler(this.StartQuery);
            // 
            // queryWorker
            // 
            this.queryWorker.DoWork += new System.ComponentModel.DoWorkEventHandler(this.RunQuery);
            this.queryWorker.RunWorkerCompleted += new System.ComponentModel.RunWorkerCompletedEventHandler(this.PostQueryResults);
            // 
            // QueryWindow
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
            this.Controls.Add(this.querySplit);
            this.Name = "QueryWindow";
            this.Size = new System.Drawing.Size(567, 258);
            this.querySplit.Panel1.ResumeLayout(false);
            this.querySplit.Panel1.PerformLayout();
            this.querySplit.Panel2.ResumeLayout(false);
            this.querySplit.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.TextBox queryText;
        private System.Windows.Forms.ListBox historyList;
        private System.Windows.Forms.SplitContainer querySplit;
        private System.Windows.Forms.Button queryButton;
        private System.ComponentModel.BackgroundWorker queryWorker;
    }
}
