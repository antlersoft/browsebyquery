package com.antlersoft.bbqForIdea.builder;

import com.intellij.compiler.instrumentation.InstrumentationClassFinder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.jps.ModuleChunk;
import org.jetbrains.jps.incremental.BuilderCategory;
import org.jetbrains.jps.incremental.CompileContext;
import org.jetbrains.jps.incremental.ModuleLevelBuilder;
import org.jetbrains.jps.incremental.instrumentation.ClassProcessingBuilder;
import org.jetbrains.jps.incremental.messages.BuildMessage;
import org.jetbrains.jps.incremental.messages.CustomBuilderMessage;
import org.jetbrains.jps.incremental.messages.DoneSomethingNotification;

/**
 * Created by mike on 3/28/18.
 */
public class BbqBuilder extends ClassProcessingBuilder {
    public BbqBuilder()
    {
        super(BuilderCategory.CLASS_POST_PROCESSOR);
    }
    @Override
    protected boolean isEnabled(CompileContext compileContext, ModuleChunk moduleChunk) {
        return true;
    }

    @Override
    protected String getProgressMessage() {
        return "Invoked BbqBuilder progress message";
    }

    @Override
    protected ExitCode performBuild(CompileContext compileContext, ModuleChunk moduleChunk, InstrumentationClassFinder instrumentationClassFinder, OutputConsumer outputConsumer) {
        compileContext.processMessage(new CustomBuilderMessage(getPresentableName(),"INFO", "BrowseByQuery builder invoked"));
        return ExitCode.NOTHING_DONE;
    }

    @NotNull
    @Override
    public String getPresentableName() {
        return "Browse-by-Query builder";
    }
}
