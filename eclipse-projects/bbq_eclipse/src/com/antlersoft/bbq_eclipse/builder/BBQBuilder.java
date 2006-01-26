package com.antlersoft.bbq_eclipse.builder;

import java.util.Map;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.antlersoft.analyzer.DBClass;
import com.antlersoft.bbq_eclipse.Bbq_eclipsePlugin;

public class BBQBuilder extends IncrementalProjectBuilder {

	class BBQDeltaVisitor implements IResourceDeltaVisitor {
		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse.core.resources.IResourceDelta)
		 */
		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
			switch (delta.getKind()) {
			case IResourceDelta.ADDED:
				// handle added resource
				analyzeFile(resource);
				break;
			case IResourceDelta.REMOVED:
				// handle removed resource
				break;
			case IResourceDelta.CHANGED:
				// handle changed resource
				analyzeFile(resource);
				break;
			}
			//return true to continue visiting children.
			return true;
		}
	}

	class BBQResourceVisitor implements IResourceVisitor {
		public boolean visit(IResource resource) throws CoreException {
			analyzeFile(resource);
			//return true to continue visiting children.
			return true;
		}
	}

	public static final String BUILDER_ID = "bbq_eclipse.com.antlersoft.bbqBuilder";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.internal.events.InternalBuilder#build(int,
	 *      java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
		if (kind == FULL_BUILD) {
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

	void analyzeFile(IResource resource) throws CoreException {
		if ( resource==null)
		{
			Bbq_eclipsePlugin.getDefault().getLog()
			.log( 
					Bbq_eclipsePlugin.
					createStatus( "null resource",new Exception("benign"),
							org.eclipse.core.runtime.IStatus.INFO, 0));
			return;
		}
		Bbq_eclipsePlugin.getDefault().getLog()
		.log( 
				Bbq_eclipsePlugin.
				createStatus( "visiting "+
						resource.getFullPath().toOSString()+
						resource.getClass().getName(),new Exception("benign"),
						org.eclipse.core.runtime.IStatus.INFO, 0));
		if (resource instanceof IFile && resource.getName().endsWith(".class")) {
			try
			{
				DBClass.addFileToDB(((IFile)resource).getFullPath().toFile(), Bbq_eclipsePlugin.getDefault().getDB());
			}
			catch ( Exception e)
			{
				Bbq_eclipsePlugin.getDefault().logError(
					"Error adding file:"+((IFile)resource).getFullPath().toOSString(), e);
			}
		}
	}

	protected void fullBuild(final IProgressMonitor monitor)
			throws CoreException {
		getProject().accept(new BBQResourceVisitor());
	}

	protected void incrementalBuild(IResourceDelta delta,
			IProgressMonitor monitor) throws CoreException {
		// the visitor does the work.
		delta.accept(new BBQDeltaVisitor());
	}
}
