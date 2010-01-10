package com.antlersoft.bbq_eclipse.preferences;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.antlersoft.bbq_eclipse.Bbq_eclipsePlugin;

/**
 * This class represents a preference page that
 * is contributed to the Preferences dialog. By 
 * subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows
 * us to create a page that is small and knows how to 
 * save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They
 * are stored in the preference store that belongs to
 * the main plug-in class. That way, preferences can
 * be accessed directly via the preference store.
 */

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

  public PreferencePage() {

    super(GRID);

    setPreferenceStore(Bbq_eclipsePlugin.getDefault().getPreferenceStore());
    setDescription("Browse-By-Query Preferences");
  }

  /**
   * Creates the field editors. Field editors are abstractions of
   * the common GUI blocks needed to manipulate various types
   * of preferences. Each field editor knows how to save and
   * restore itself.
   */
  public void createFieldEditors() {

    addField(new DirectoryFieldEditor(PreferenceConstants.P_DB_PATH,
                                      "&Directory for object db:",
                                      getFieldEditorParent()));
  }

  @Override protected Control createContents(Composite parent) {

    Control result = super.createContents(parent);
    
    new Label((Composite) result, SWT.SINGLE);
    getEraseButton((Composite) result);

    return result;
  }

  protected Button getEraseButton(Composite parent) {

    Button eraseDBButton = new Button(parent, SWT.PUSH);
    eraseDBButton.setText("Erase Browse by Query Database");
    eraseDBButton.setFont(parent.getFont());
    eraseDBButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
    
    // selection: button clicked

    eraseDBButton.addSelectionListener(new SelectionAdapter() {

      public void widgetSelected(SelectionEvent evt) {

        try {

          // clear db

          Bbq_eclipsePlugin.getDefault().clearDB();

        } catch (CoreException ex) {

          Bbq_eclipsePlugin.getDefault().logNoThrow("Failed to clear db ", ex);
        }
      }
    });
    
    return eraseDBButton;
  }

  public void init(IWorkbench workbench) {

  }
}