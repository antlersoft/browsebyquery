package analyzer;

import java.util.Vector;

class CodeReader
{
    AnalyzeClass ac;
    AnalyzeClass.CodeAttribute ca;
    AnalyzeClass.LineNumberTableAttribute la;
    AnalyzerDB db;
    DBMethod method;
    int currentPos;
    static OpCode[] opCodes;

    static
    {
try
{
	opCodes=new OpCode[256];
	new OpCode( 50, 1, "aaload");
	new OpCode( 83, 1, "aastore");
	new OpCode( 1, 1, "aconst_null");
	new OpCode( 25, 2, "aload");
	new OpCode( 42, 1, "aload_0");
	new OpCode( 43, 1, "aload_1");
	new OpCode( 44, 1, "aload_2");
	new OpCode( 45, 1, "aload_3");
	new OpCode( 189, 3, "anewarray");
	new OpCode( 176, 1, "areturn");
	new OpCode( 190, 1, "arraylength");
	new OpCode( 58, 2, "astore");
	new OpCode( 75, 1, "astore_0");
	new OpCode( 76, 1, "astore_1");
	new OpCode( 77, 1, "astore_2");
	new OpCode( 78, 1, "astore_3");
	new OpCode( 191, 1, "athrow");
	new OpCode( 51, 1, "baload");
	new OpCode( 84, 1, "bastore");
	new OpCode( 16, 2, "bipush");
	new OpCode( 52, 1, "caload");
	new OpCode( 85, 1, "castore");
	new OpCode( 192, 3, "checkcast");
	new OpCode( 144, 1, "d2f");
	new OpCode( 142, 1, "d2i");
	new OpCode( 143, 1, "d2l");
	new OpCode( 99, 1, "dadd");
	new OpCode( 49, 1, "daload");
	new OpCode( 82, 1, "dastore");
	new OpCode( 152, 1, "dcmpg");
	new OpCode( 151, 1, "dcmpl");
	new OpCode( 14, 1, "dconst_0");
	new OpCode( 15, 1, "dconst_1");
	new OpCode( 111, 1, "ddiv");
	new OpCode( 24, 2, "dload");
	new OpCode( 38, 1, "dload_0");
	new OpCode( 39, 1, "dload_1");
	new OpCode( 40, 1, "dload_2");
	new OpCode( 41, 1, "dload_3");
	new OpCode( 107, 1, "dmul");
	new OpCode( 119, 1, "dneg");
	new OpCode( 115, 1, "drem");
	new OpCode( 175, 1, "dreturn");
	new OpCode( 57, 2, "dstore");
	new OpCode( 71, 1, "dstore_0");
	new OpCode( 72, 1, "dstore_1");
	new OpCode( 73, 1, "dstore_2");
	new OpCode( 74, 1, "dstore_3");
	new OpCode( 103, 1, "dsub");
	new OpCode( 89, 1, "dup");
	new OpCode( 90, 1, "dup_x1");
	new OpCode( 91, 1, "dup_x2");
	new OpCode( 92, 1, "dup2");
	new OpCode( 93, 1, "dup2_x1");
	new OpCode( 94, 1, "dup2_x4");
	new OpCode( 141, 1, "f2d");
	new OpCode( 139, 1, "f2i");
	new OpCode( 140, 1, "f2l");
	new OpCode( 98, 1, "fadd");
	new OpCode( 48, 1, "faload");
	new OpCode( 81, 1, "fastore");
	new OpCode( 150, 1, "fcmpg");
	new OpCode( 149, 1, "fcmpl");
	new OpCode( 11, 1, "fconst_0");
	new OpCode( 12, 1, "fconst_1");
	new OpCode( 13, 1, "fconst_2");
	new OpCode( 110, 1, "fdiv");
	new OpCode( 23, 2, "fload");
	new OpCode( 34, 1, "fload_0");
	new OpCode( 35, 1, "fload_1");
	new OpCode( 36, 1, "fload_2");
	new OpCode( 37, 1, "fload_3");
	new OpCode( 106, 1, "fmul");
	new OpCode( 118, 1, "fneg");
	new OpCode( 114, 1, "frem");
	new OpCode( 174, 1, "freturn");
	new OpCode( 56, 2, "fstore");
	new OpCode( 67, 1, "fstore_0");
	new OpCode( 68, 1, "fstore_1");
	new OpCode( 69, 1, "fstore_2");
	new OpCode( 70, 1, "fstore_3");
	new OpCode( 102, 1, "fsub");
	new OpCode( 180, 3, "getfield");
	new OpCode( 178, 3, "getstatic");
	new OpCode( 167, 3, "goto");
	new OpCode( 200, 3, "goto_w");
	new OpCode( 145, 1, "i2b");
	new OpCode( 146, 1, "i2c");
	new OpCode( 135, 1, "i2d");
	new OpCode( 134, 1, "i2f");
	new OpCode( 133, 1, "i2l");
	new OpCode( 147, 1, "i2s");
	new OpCode( 96, 1, "iadd");
	new OpCode( 46, 1, "iaload");
	new OpCode( 126, 1, "iand");
	new OpCode( 79, 1, "iastore");
	new OpCode( 2, 1, "iconst_m1");
	new OpCode( 3, 1, "iconst_0");
	new OpCode( 4, 1, "iconst_1");
	new OpCode( 5, 1, "iconst_2");
	new OpCode( 6, 1, "iconst_3");
	new OpCode( 7, 1, "iconst_4");
	new OpCode( 8, 1, "iconst_5");
	new OpCode( 108, 1, "idiv");
	new OpCode( 165, 3, "if_acmpeq");
	new OpCode( 166, 3, "if_acmpne");
	new OpCode( 159, 3, "if_icmpeq");
	new OpCode( 160, 3, "if_icmpne");
	new OpCode( 161, 3, "if_icmplt");
	new OpCode( 162, 3, "if_icmpge");
	new OpCode( 163, 3, "if_icmpgt");
	new OpCode( 164, 3, "if_icmple");
	new OpCode( 153, 3, "ifeq");
	new OpCode( 154, 3, "ifne");
	new OpCode( 155, 3, "iflt");
	new OpCode( 156, 3, "ifge");
	new OpCode( 157, 3, "ifgt");
	new OpCode( 158, 3, "ifle");
	new OpCode( 199, 3, "ifnonnull");
	new OpCode( 198, 3, "ifnull");
	new OpCode( 132, 3, "iinc");
	new OpCode( 21, 2, "iload");
	new OpCode( 26, 1, "iload_0");
	new OpCode( 27, 1, "iload_1");
	new OpCode( 28, 1, "iload_2");
	new OpCode( 29, 1, "iload_3");
	new OpCode( 104, 1, "imul");
	new OpCode( 116, 1, "ineg");
	new OpCode( 193, 3, "instanceof");
	new MethodInvocation( 185, 5, "invokeinterface");
	new MethodInvocation( 183, 3, "invokespecial");
	new MethodInvocation( 184, 3, "invokestatic");
	new MethodInvocation( 182, 3, "invokevirtual");
	new OpCode( 128, 1, "ior");
	new OpCode( 112, 1, "irem");
	new OpCode( 172, 1, "ireturn");
	new OpCode( 120, 1, "ishl");
	new OpCode( 122, 1, "ishr");
	new OpCode( 54, 2, "istore");
	new OpCode( 59, 1, "istore_0");
	new OpCode( 60, 1, "istore_1");
	new OpCode( 61, 1, "istore_2");
	new OpCode( 62, 1, "istore_3");
	new OpCode( 100, 1, "isub");
	new OpCode( 124, 1, "iushr");
	new OpCode( 130, 1, "ixor");
	new OpCode( 168, 3, "jsr");
	new OpCode( 201, 5, "jsr_w");
	new OpCode( 138, 1, "l2d");
	new OpCode( 137, 1, "l2f");
	new OpCode( 136, 1, "l2i");
	new OpCode( 97, 1, "ladd");
	new OpCode( 47, 1, "laload");
	new OpCode( 127, 1, "land");
	new OpCode( 80, 1, "lastore");
	new OpCode( 148, 1, "lcmp");
	new OpCode( 9, 1, "lconst_0");
	new OpCode( 10, 1, "lconst_1");
	new OpCode( 18, 2, "ldc");
	new OpCode( 19, 3, "ldc_w");
	new OpCode( 20, 3, "ldc2_w");
	new OpCode( 109, 1, "ldiv");
	new OpCode( 22, 2, "lload");
	new OpCode( 30, 1, "lload_0");
	new OpCode( 31, 1, "lload_1");
	new OpCode( 32, 1, "lload_2");
	new OpCode( 33, 1, "lload_3");
	new OpCode( 105, 1, "lmul");
	new OpCode( 117, 1, "lneg");
	new LookupSwitch( 171, 1, "lookupswitch");
	new OpCode( 129, 1, "lor");
	new OpCode( 113, 1, "lrem");
	new OpCode( 173, 1, "lreturn");
	new OpCode( 121, 1, "lshl");
	new OpCode( 123, 1, "lshr");
	new OpCode( 55, 2, "lstore");
	new OpCode( 63, 1, "lstore_0");
	new OpCode( 64, 1, "lstore_1");
	new OpCode( 65, 1, "lstore_2");
	new OpCode( 66, 1, "lstore_3");
	new OpCode( 101, 1, "lsub");
	new OpCode( 125, 1, "lushr");
	new OpCode( 131, 1, "lxor");
	new OpCode( 194, 1, "monitorenter");
	new OpCode( 195, 1, "monitorexit");
	new OpCode( 197, 4, "multianewarray");
	new OpCode( 187, 3, "new");
	new OpCode( 188, 2, "newarray");
	new OpCode( 0, 1, "nop");
	new OpCode( 87, 1, "pop");
	new OpCode( 88, 1, "pop2");
	new OpCode( 181, 3, "putfield");
	new OpCode( 179, 3, "putstatic");
	new OpCode( 169, 2, "ret");
	new OpCode( 177, 1, "return");
	new OpCode( 53, 1, "saload");
	new OpCode( 86, 1, "sastore");
	new OpCode( 17, 3, "sipush");
	new OpCode( 95, 1, "swap");
	new TableSwitch( 170, 1, "tableswitch");
	new Wide( 196, 1, "wide");
}
catch ( Throwable t)
{
t.printStackTrace();
}
    }

    static final int mU( byte b)
    {
	int retval=b;
	if ( retval<0)
	{
	    retval+=256;
	}
	return retval;
    }

    CodeReader( DBMethod inMethod, AnalyzeClass a, AnalyzeClass.CodeAttribute q, AnalyzerDB d)
    {
	method=inMethod;
	ac=a;
	ca=q;
	currentPos=0;
	db=d;
	la=null;
	for ( int i=0; i<ca.attributeCount; i++)
	{
	    if ( ca.attributes[i].value instanceof AnalyzeClass.LineNumberTableAttribute)
	    {
		la=(AnalyzeClass.LineNumberTableAttribute)ca.attributes[i].value;
		break;
	    }
	}
    }

    void processOpCodes()
    {
	while ( currentPos<ca.codeLength)
	{
	    opCodes[mU(ca.code[currentPos])].read( this);
	}
    }

    static class OpCode
    {
	int value;
	int length;
	String mnemonic;

	OpCode( int v, int l, String m)
	{
	    value=v;
	    length=l;
	    mnemonic=m;
	    if ( opCodes[v]!=null)
	    {
		throw new IllegalStateException();
	    }
	    opCodes[v]=this;
	}

	void read( CodeReader cr)
	{
	    cr.currentPos+=length;
	}
    }

    static class MethodInvocation extends OpCode
    {
	MethodInvocation( int v, int l, String m)
	{
	    super( v, l, m);
	}

	void read( CodeReader cr)
	{
	    AnalyzeClass.CPTypeRef methodRef=(AnalyzeClass.CPTypeRef)cr.ac.constantPool[(mU( cr.ca.code[cr.currentPos+1])<<8)|mU( cr.ca.code[cr.currentPos+2])];
	    AnalyzeClass.CPNameAndType nameAndType=(AnalyzeClass.CPNameAndType)cr.ac.constantPool[methodRef.nameAndTypeIndex];
	    try
	    {
		int lineNumber=0;
		for ( int i=0; i<cr.la.lineNumberEntryCount; i++)
		{
		    if ( cr.la.lineNumberEntries[i].start_pc<=cr.currentPos)
		    {
			lineNumber=cr.la.lineNumberEntries[i].line_number;
		    }
		}
		cr.method.calls.addElement( new DBCall(
		    cr.method,
		    (DBMethod)cr.db.getWithKey( "analyzer.DBMethod",
		    DBMethod.makeKey(
		    cr.ac.getClassName( methodRef.classIndex),
		    cr.ac.getString( nameAndType.nameIndex),
		    cr.ac.getString( nameAndType.descriptorIndex)
		    )), lineNumber));
	    }
	    catch ( Exception e)
	    {
	    }
	    super.read( cr);
	}
    }

    static class LookupSwitch extends OpCode
    {
	LookupSwitch( int v, int l, String m)
	{
	    super( v, l, m);
	}

	void read( CodeReader cr)
	{
	    cr.currentPos++;
	    cr.currentPos+=4-( cr.currentPos%4);
	    cr.currentPos+=4;
	    int npairs=( mU(cr.ca.code[cr.currentPos++])<<24)|(mU(cr.ca.code[cr.currentPos++])<<16)|(mU(cr.ca.code[cr.currentPos++])<<8)|mU(cr.ca.code[cr.currentPos++]);
	    cr.currentPos+=8*npairs;
	}
    }

    static class TableSwitch extends OpCode
    {
	TableSwitch( int v, int l, String m)
	{
	    super( v, l, m);
	}

	void read( CodeReader cr)
	{
	    cr.currentPos++;
	    cr.currentPos+=4-( cr.currentPos%4);
	    cr.currentPos+=4;
	    int lowend=( mU(cr.ca.code[cr.currentPos++])<<24)|(mU(cr.ca.code[cr.currentPos++])<<16)|(mU(cr.ca.code[cr.currentPos++])<<8)|mU(cr.ca.code[cr.currentPos++]);
	    int highend=( mU(cr.ca.code[cr.currentPos++])<<24)|(mU(cr.ca.code[cr.currentPos++])<<16)|(mU(cr.ca.code[cr.currentPos++])<<8)|mU(cr.ca.code[cr.currentPos++]);
	    cr.currentPos+=4*(highend-lowend+1);
	}
    }

    static class Wide extends OpCode
    {
	Wide( int v, int l, String m)
	{
	    super( v, l, m);
	}
	void read( CodeReader cr)
	{
	    cr.currentPos++;
	    if ( opCodes[mU(cr.ca.code[cr.currentPos])].mnemonic.equals( "iinc"))
	    {
		cr.currentPos+=5;
	    }
	    else
	    {
		cr.currentPos+=3;
	    }
	}
    }
}
