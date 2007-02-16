package com.antlersoft.bbq_eclipse.builder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;

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

	public static final String BUILDER_ID = "com.antlersoft.bbq_eclipse.BBQBuilder";

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
		if (resource instanceof IFile && resource.getName().endsWith(".class")) {
			try
			{
				DBClass.addFileToDB(((IFile)resource).getLocation().toFile(), Bbq_eclipsePlugin.getDefault().getDB());
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
	
	public static void buildSingleProject( IProject project, IProgressMonitor monitor)
		throws CoreException
	{
		project.build( FULL_BUILD, BUILDER_ID, null, monitor);
	}
	
	public static void buildWorkspace( IProgressMonitor monitor)
		throws CoreException
	{
		Bbq_eclipsePlugin.getDefault().clearDB();
		IWorkspaceRoot root=ResourcesPlugin.getWorkspace().getRoot();
		
		IProject[] projects=root.getProjects();

		ArrayList bbq_projects=new ArrayList( projects.length);
		for ( int i=0; i<projects.length; ++i)
		{
			IProjectDescription description = projects[i].getDescription();
			String[] natures = description.getNatureIds();

			for (int j = 0; j < natures.length; ++j) {
				if (BBQNature.NATURE_ID.equals(natures[j])) {
					bbq_projects.add( projects[i]);
					break;
				}
			}
		}
		monitor.beginTask( "Rebuild BBQ database", 10*bbq_projects.size());
		for ( Iterator i=bbq_projects.iterator(); i.hasNext() && ! monitor.isCanceled();)
		{
			SubProgressMonitor spm=new SubProgressMonitor( monitor, 10);
			buildSingleProject( (IProject)i.next(), spm);
		}
		monitor.done();
	}
}
