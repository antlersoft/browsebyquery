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
            this.querySplit.Panel1.Controls.Add(this.queryText);
            // 
            // querySplit.Panel2
            // 
            this.querySplit.Panel2.Controls.Add(this.historyList);
            this.querySplit.Size = new System.Drawing.Size(567, 258);
            this.querySplit.SplitterDistance = 112;
            this.querySplit.TabIndex = 2;
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
    }
}
