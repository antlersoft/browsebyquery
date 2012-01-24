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
            this.components = new System.ComponentModel.Container();
            this.resultList = new System.Windows.Forms.ListBox();
            this.resultMenu = new System.Windows.Forms.ContextMenuStrip(this.components);
            this.jumpToToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.copyToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.selectAllToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.resultMenu.SuspendLayout();
            this.SuspendLayout();
            // 
            // resultList
            // 
            this.resultList.ContextMenuStrip = this.resultMenu;
            this.resultList.Dock = System.Windows.Forms.DockStyle.Fill;
            this.resultList.FormattingEnabled = true;
            this.resultList.HorizontalScrollbar = true;
            this.resultList.Location = new System.Drawing.Point(0, 0);
            this.resultList.MinimumSize = new System.Drawing.Size(80, 30);
            this.resultList.Name = "resultList";
            this.resultList.SelectionMode = System.Windows.Forms.SelectionMode.MultiExtended;
            this.resultList.Size = new System.Drawing.Size(80, 30);
            this.resultList.TabIndex = 0;
            this.resultList.DoubleClick += new System.EventHandler(this.JumpToSelected);
            // 
            // resultMenu
            // 
            this.resultMenu.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.jumpToToolStripMenuItem,
            this.copyToolStripMenuItem,
            this.selectAllToolStripMenuItem});
            this.resultMenu.Name = "resultMenu";
            this.resultMenu.Size = new System.Drawing.Size(153, 92);
            this.resultMenu.Opening += new System.ComponentModel.CancelEventHandler(this.resultMenu_Opening);
            // 
            // jumpToToolStripMenuItem
            // 
            this.jumpToToolStripMenuItem.Name = "jumpToToolStripMenuItem";
            this.jumpToToolStripMenuItem.Size = new System.Drawing.Size(152, 22);
            this.jumpToToolStripMenuItem.Text = "Jump To";
            this.jumpToToolStripMenuItem.Click += new System.EventHandler(this.JumpToSelected);
            // 
            // copyToolStripMenuItem
            // 
            this.copyToolStripMenuItem.Name = "copyToolStripMenuItem";
            this.copyToolStripMenuItem.Size = new System.Drawing.Size(152, 22);
            this.copyToolStripMenuItem.Text = "Copy";
            this.copyToolStripMenuItem.Click += new System.EventHandler(this.Copy);
            // 
            // selectAllToolStripMenuItem
            // 
            this.selectAllToolStripMenuItem.Name = "selectAllToolStripMenuItem";
            this.selectAllToolStripMenuItem.Size = new System.Drawing.Size(152, 22);
            this.selectAllToolStripMenuItem.Text = "Select All";
            this.selectAllToolStripMenuItem.Click += new System.EventHandler(this.SelectAll);
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
            this.resultMenu.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.ListBox resultList;
        private System.Windows.Forms.ContextMenuStrip resultMenu;
        private System.Windows.Forms.ToolStripMenuItem jumpToToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem copyToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem selectAllToolStripMenuItem;
    }
}
