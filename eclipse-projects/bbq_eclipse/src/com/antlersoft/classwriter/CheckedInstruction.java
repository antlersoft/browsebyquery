
/**
 * Title:        <p>
 * Description:  Java object database; also code analysis tool<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      <p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.classwriter;

import java.util.ArrayList;
import java.util.Stack;

public class CheckedInstruction
{
    public Instruction instruction;
    public Stack stack;
    public ArrayList previousCheckedInstructions;

    CheckedInstruction( Instruction i, Stack s)
    {
        instruction=i;
        stack=s;
        previousCheckedInstructions=new ArrayList();
    }
}