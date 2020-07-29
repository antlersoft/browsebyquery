package com.antlersoft.bbq_eclipse.popup.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.antlersoft.bbq_eclipse.Bbq_eclipsePlugin;

public class AddToBBQJob extends Job {

  private IFile toAnalyze;

  public AddToBBQJob(IFile toAnalyze) {

    super("Analyzing IFile : " + toAnalyze.getName());
    this.toAnalyze = toAnalyze;
  }

  @Override protected IStatus run(IProgressMonitor monitor) {

    try {

      if(monitor != null && monitor.isCanceled() == true)
        return Status.CANCEL_STATUS;
      
      BBQResourceAnalyzer bbqBuilder = new BBQResourceAnalyzer(monitor,toAnalyze);
      bbqBuilder.analyzeResource();

    } catch (CoreException ex) {

      Bbq_eclipsePlugin.getDefault().logNoThrow("Failed to analyze : " + toAnalyze, ex);
    }

    return Status.OK_STATUS;
  }
}
