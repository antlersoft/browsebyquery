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
            this.SuspendLayout();
            // 
            // queryText
            // 
            this.queryText.AcceptsReturn = true;
            this.queryText.Location = new System.Drawing.Point(16, 26);
            this.queryText.Multiline = true;
            this.queryText.Name = "queryText";
            this.queryText.ScrollBars = System.Windows.Forms.ScrollBars.Both;
            this.queryText.Size = new System.Drawing.Size(166, 152);
            this.queryText.TabIndex = 0;
            // 
            // historyList
            // 
            this.historyList.FormattingEnabled = true;
            this.historyList.Location = new System.Drawing.Point(16, 196);
            this.historyList.Name = "historyList";
            this.historyList.Size = new System.Drawing.Size(166, 186);
            this.historyList.TabIndex = 1;
            // 
            // QueryWindow
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Controls.Add(this.historyList);
            this.Controls.Add(this.queryText);
            this.Name = "QueryWindow";
            this.Size = new System.Drawing.Size(196, 396);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.TextBox queryText;
        private System.Windows.Forms.ListBox historyList;
    }
}
