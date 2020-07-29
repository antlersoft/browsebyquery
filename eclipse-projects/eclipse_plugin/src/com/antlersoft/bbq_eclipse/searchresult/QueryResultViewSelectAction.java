/**
 * 
 */
package com.antlersoft.bbq_eclipse.searchresult;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.texteditor.ITextEditor;

import com.antlersoft.analyzer.DBClass;
import com.antlersoft.analyzer.SourceObject;
import com.antlersoft.bbq_eclipse.Bbq_eclipsePlugin;

class QueryResultViewSelectAction extends Action {

  private final QueryResultView queryResultView;

  QueryResultViewSelectAction(QueryResultView queryResultView) {

    super("Jump to");
    this.queryResultView = queryResultView;
  }

  public void run() {

    if (this.queryResultView._selection != null && !this.queryResultView._selection.isEmpty()
        && this.queryResultView._selection instanceof IStructuredSelection) {

      Object selected = ((IStructuredSelection) this.queryResultView._selection).getFirstElement();

      if (selected instanceof SourceObject) {

        try {

          IJavaElement javaElementToOpen = null;

          SourceObject source = (SourceObject) selected;
          javaElementToOpen = findTypeInAllJavaProjects(source);

          // log error if we dont' have the file

          if (javaElementToOpen == null) {

            Bbq_eclipsePlugin.getDefault()
                             .getLog()
                             .log(Bbq_eclipsePlugin.createStatus("unable to find file: "
                                                                     + source.getDBClass()
                                                                             .getName(),
                                                                 new Exception("benign"),
                                                                 org.eclipse.core.runtime.IStatus.INFO,
                                                                 0));
            return;
          }

          openIJavaElement(javaElementToOpen, source);

        } catch (CoreException ce) {

          Bbq_eclipsePlugin.getDefault().getLog().log(ce.getStatus());
        }
      }
    }
  }

  /**
   * try to find sourceToFind in all Java projects
   */
  private IJavaElement findTypeInAllJavaProjects(SourceObject sourceToFind)
    throws JavaModelException {

	DBClass notContainedClass = sourceToFind.getDBClass();
	while (notContainedClass.getContainingClass() != null)
	{
		notContainedClass = notContainedClass.getContainingClass();
	}
    String fullyQualifiedName = notContainedClass.getName();

    // for all projects

    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    IProject[] projects = root.getProjects();
    for (int i = 0; i < projects.length; ++i) {

      try {

        // try to convert into a Java Project

        IJavaProject project = JavaCore.create(projects[i]);

        if (project == null)
          continue;

        // find source to look for

        IType type = project.findType(fullyQualifiedName);

        if (type != null)
          return type;

      } catch (JavaModelException jme) {

        // Catching this exception means that the
        // project wasn't really a
        // Java project
        continue;
      }
    }

    return null;
  }

  /**
   * open IJavaElement at SourceObject.getLineNumber
   */
  private void openIJavaElement(IJavaElement javaElementToOpen, SourceObject source)
    throws CoreException, JavaModelException, PartInitException {

    // open an editor with this string

    IEditorPart ep = JavaUI.openInEditor(javaElementToOpen);

    if (ep != null && ep instanceof ITextEditor) {

      ITextEditor textEditor = (ITextEditor) ep;
      IDocument document = textEditor.getDocumentProvider().getDocument(ep.getEditorInput());

      if (document != null) {

        // find line number of source location and turn it into offset and length
        // for selection in editor

        int currentLine = 1;
        int offset = 0;
        int lineStartOffset = 0;

        String javaSourceCode = document.get();

        while (offset < javaSourceCode.length() ) {

          char ch = javaSourceCode.charAt(offset);

          if (ch == '\n') {

            if (currentLine >= source.getLineNumber())
              break;

            currentLine++;
            lineStartOffset = offset+1;
          }

          offset++;
        }

        // find length of line 

        int length = 0;
        for (offset = lineStartOffset + 1; offset < javaSourceCode.length();) {

          char ch = javaSourceCode.charAt(offset);

          if (ch == '\n') {

            break;
          }

          offset++;
          length++;
        }

        // select lineStartOffset at full length of line

        ((ITextEditor) ep).selectAndReveal(lineStartOffset, length);
      }

    } else {

      Bbq_eclipsePlugin.getDefault()
                       .getLog()
                       .log(Bbq_eclipsePlugin.createStatus("editor part type is "
                                                               + ep.getClass().getName(),
                                                           new Exception("benign"),
                                                           org.eclipse.core.runtime.IStatus.INFO,
                                                           0));
    }
  }
}