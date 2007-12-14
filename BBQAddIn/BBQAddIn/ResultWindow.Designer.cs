namespace com.antlersoft.BBQAddIn
{
    partial class ResultWindow
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
            this.resultList = new System.Windows.Forms.ListBox();
            this.SuspendLayout();
            // 
            // resultList
            // 
            this.resultList.Dock = System.Windows.Forms.DockStyle.Fill;
            this.resultList.FormattingEnabled = true;
            this.resultList.Location = new System.Drawing.Point(0, 0);
            this.resultList.MinimumSize = new System.Drawing.Size(80, 30);
            this.resultList.Name = "resultList";
            this.resultList.Size = new System.Drawing.Size(80, 30);
            this.resultList.TabIndex = 0;
            this.resultList.MouseDoubleClick += new System.Windows.Forms.MouseEventHandler(this.JumpToSelected);
            // 
            // ResultWindow
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.AutoSize = true;
            this.AutoSizeMode = System.Windows.Forms.AutoSizeMode.GrowAndShrink;
            this.Controls.Add(this.resultList);
            this.MinimumSize = new System.Drawing.Size(80, 30);
            this.Name = "ResultWindow";
            this.Size = new System.Drawing.Size(80, 30);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.ListBox resultList;
    }
}
