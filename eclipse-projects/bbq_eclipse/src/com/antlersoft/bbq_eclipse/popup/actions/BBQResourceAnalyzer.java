package com.antlersoft.bbq_eclipse.popup.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.antlersoft.analyzer.DBClass;
import com.antlersoft.bbq_eclipse.Bbq_eclipsePlugin;

public class BBQResourceAnalyzer {

  private static final Object mutex     = new Object();
  private IProgressMonitor    monitor   = null;
  private IResource           toAnalyze = null;

  public BBQResourceAnalyzer(IProgressMonitor monitor, IResource toAnalyze) {

    this.monitor = monitor;
    this.toAnalyze = toAnalyze;
    
    if(this.monitor == null)
      this.monitor = new NullProgressMonitor();
  }

  public void analyzeResource() throws CoreException {

    // an action or a builder can call this method at the moment

    synchronized (mutex) {

      try {

        if (toAnalyze == null) {

          Bbq_eclipsePlugin.getDefault()
                           .getLog()
                           .log(Bbq_eclipsePlugin.createStatus("null resource",
                                                               new Exception("benign"),
                                                               org.eclipse.core.runtime.IStatus.INFO,
                                                               0));
          return;
        }

        // IFile

        if (toAnalyze instanceof IFile) {

          IFile file = (IFile) toAnalyze;

          DBClass.addFileToDB(file.getLocation().toFile(),
                              Bbq_eclipsePlugin.getDefault().getDB(),
                              monitor);
          
          monitor.setTaskName("Saving BBQ DB");
          Bbq_eclipsePlugin.getDefault().getDB().commit();
        }

      } catch (Exception e) {

        Bbq_eclipsePlugin.getDefault().logError("Error adding :" + toAnalyze, e);
      }
    }
  }
}
