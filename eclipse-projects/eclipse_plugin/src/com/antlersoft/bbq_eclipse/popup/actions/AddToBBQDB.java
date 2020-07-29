package com.antlersoft.bbq_eclipse.popup.actions;

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

import com.antlersoft.bbq_eclipse.Bbq_eclipsePlugin;

public class AddToBBQDB implements IObjectActionDelegate {

  private ISelection lastSelection;

  public AddToBBQDB() {

    super();
  }

  public void setActivePart(IAction action, IWorkbenchPart targetPart) {

  }

  @SuppressWarnings("unchecked") public void run(IAction action) {

    // for all selected objects

    if (lastSelection == null)
      return;

    if (lastSelection.isEmpty() == true)
      return;

    if (lastSelection instanceof StructuredSelection == false)
      return;

    StructuredSelection structuredSelection = (StructuredSelection) lastSelection;

    Iterator<Object> it = structuredSelection.iterator();

    while (it.hasNext()) {

      Object selectedObject = it.next();

      try {

        if (selectedObject instanceof IFile == true) {

          // IFile

          IFile selectedFile = (IFile) selectedObject;
          AddToBBQJob job = new AddToBBQJob(selectedFile);
          job.schedule();

        } else if (selectedObject instanceof IProject == true) {

          IProject selectedProject = (IProject) selectedObject;

          // try to convert into a Java Project

          IJavaProject javaProject = JavaCore.create(selectedProject);

          if (javaProject != null) {

            // add a job for outputLocation of IJavaProject

            IPath outputLocation = javaProject.getOutputLocation();
            IFile outputLocationAsFile = javaProject.getProject()
                                                    .getWorkspace()
                                                    .getRoot()
                                                    .getFile(outputLocation);

            if (outputLocationAsFile != null) {

              AddToBBQJob job = new AddToBBQJob(outputLocationAsFile);
              job.schedule();
            }

          }
        }

      } catch (Exception ex) {

        Bbq_eclipsePlugin.getDefault().logNoThrow("Failed to analyze : " + selectedObject, ex);
      }
    }
  }

  public void selectionChanged(IAction action, ISelection selection) {

    lastSelection = selection;
  }
}
