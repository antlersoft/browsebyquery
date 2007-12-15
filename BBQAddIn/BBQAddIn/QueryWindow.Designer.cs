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
            this.tableLayoutPanel1 = new System.Windows.Forms.TableLayoutPanel();
            this.queryButton = new System.Windows.Forms.Button();
            this.queryWorker = new System.ComponentModel.BackgroundWorker();
            this.querySplit.Panel1.SuspendLayout();
            this.querySplit.Panel2.SuspendLayout();
            this.querySplit.SuspendLayout();
            this.tableLayoutPanel1.SuspendLayout();
            this.SuspendLayout();
            // 
            // queryText
            // 
            this.queryText.AcceptsReturn = true;
            this.queryText.Dock = System.Windows.Forms.DockStyle.Fill;
            this.queryText.Location = new System.Drawing.Point(78, 3);
            this.queryText.Multiline = true;
            this.queryText.Name = "queryText";
            this.queryText.ScrollBars = System.Windows.Forms.ScrollBars.Both;
            this.queryText.Size = new System.Drawing.Size(219, 106);
            this.queryText.TabIndex = 0;
            // 
            // historyList
            // 
            this.historyList.Dock = System.Windows.Forms.DockStyle.Fill;
            this.historyList.FormattingEnabled = true;
            this.historyList.Location = new System.Drawing.Point(0, 0);
            this.historyList.Name = "historyList";
            this.historyList.Size = new System.Drawing.Size(300, 134);
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
            this.querySplit.Panel1.Controls.Add(this.tableLayoutPanel1);
            // 
            // querySplit.Panel2
            // 
            this.querySplit.Panel2.Controls.Add(this.historyList);
            this.querySplit.Size = new System.Drawing.Size(300, 258);
            this.querySplit.SplitterDistance = 112;
            this.querySplit.TabIndex = 2;
            // 
            // tableLayoutPanel1
            // 
            this.tableLayoutPanel1.ColumnCount = 2;
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle(System.Windows.Forms.SizeType.Absolute, 75F));
            this.tableLayoutPanel1.ColumnStyles.Add(new System.Windows.Forms.ColumnStyle());
            this.tableLayoutPanel1.Controls.Add(this.queryText, 1, 0);
            this.tableLayoutPanel1.Controls.Add(this.queryButton, 0, 0);
            this.tableLayoutPanel1.Dock = System.Windows.Forms.DockStyle.Fill;
            this.tableLayoutPanel1.Location = new System.Drawing.Point(0, 0);
            this.tableLayoutPanel1.Name = "tableLayoutPanel1";
            this.tableLayoutPanel1.RowCount = 1;
            this.tableLayoutPanel1.RowStyles.Add(new System.Windows.Forms.RowStyle(System.Windows.Forms.SizeType.Percent, 100F));
            this.tableLayoutPanel1.Size = new System.Drawing.Size(300, 112);
            this.tableLayoutPanel1.TabIndex = 2;
            // 
            // queryButton
            // 
            this.queryButton.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
            this.queryButton.Location = new System.Drawing.Point(3, 3);
            this.queryButton.Name = "queryButton";
            this.queryButton.Size = new System.Drawing.Size(69, 46);
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
            this.Size = new System.Drawing.Size(300, 258);
            this.querySplit.Panel1.ResumeLayout(false);
            this.querySplit.Panel2.ResumeLayout(false);
            this.querySplit.ResumeLayout(false);
            this.tableLayoutPanel1.ResumeLayout(false);
            this.tableLayoutPanel1.PerformLayout();
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.TextBox queryText;
        private System.Windows.Forms.ListBox historyList;
        private System.Windows.Forms.SplitContainer querySplit;
        private System.Windows.Forms.Button queryButton;
        private System.ComponentModel.BackgroundWorker queryWorker;
        private System.Windows.Forms.TableLayoutPanel tableLayoutPanel1;
    }
}
