/**
 * Copyright (c) 2007 Michael A. MacDonald
 */
 package com.antlersoft.ilanalyze.parseildasm;
 
import com.antlersoft.parser.*;

class IldasmParser extends IldasmParserBase
{
	IldasmParser( ParseState[] states)
	{
		super( states);
	}

	private static final ReservedScope reservedScope=new ReservedScope();
	public ReservedScope getReservedScope() { return reservedScope; }
// Output goes here

static Symbol errorSymbol=_error_;
static Symbol n_INSTR_BRTARGET=Symbol.get( "n_INSTR_BRTARGET");
static Symbol n_INSTR_FIELD=Symbol.get( "n_INSTR_FIELD");
static Symbol n_INSTR_I=Symbol.get( "n_INSTR_I");
static Symbol n_INSTR_I8=Symbol.get( "n_INSTR_I8");
static Symbol n_INSTR_METHOD=Symbol.get( "n_INSTR_METHOD");
static Symbol n_INSTR_NONE=Symbol.get( "n_INSTR_NONE");
static Symbol n_INSTR_R=Symbol.get( "n_INSTR_R");
static Symbol n_INSTR_SIG=Symbol.get( "n_INSTR_SIG");
static Symbol n_INSTR_STRING=Symbol.get( "n_INSTR_STRING");
static Symbol n_INSTR_SWITCH=Symbol.get( "n_INSTR_SWITCH");
static Symbol n_INSTR_TOK=Symbol.get( "n_INSTR_TOK");
static Symbol n_INSTR_TYPE=Symbol.get( "n_INSTR_TYPE");
static Symbol n_INSTR_VAR=Symbol.get( "n_INSTR_VAR");
static Symbol n_START=Symbol.get( "n_START");
static Symbol n_asmAttr=Symbol.get( "n_asmAttr");
static Symbol n_asmOrRefDecl=Symbol.get( "n_asmOrRefDecl");
static Symbol n_assemblyDecl=Symbol.get( "n_assemblyDecl");
static Symbol n_assemblyDecls=Symbol.get( "n_assemblyDecls");
static Symbol n_assemblyHead=Symbol.get( "n_assemblyHead");
static Symbol n_assemblyRefDecl=Symbol.get( "n_assemblyRefDecl");
static Symbol n_assemblyRefDecls=Symbol.get( "n_assemblyRefDecls");
static Symbol n_assemblyRefHead=Symbol.get( "n_assemblyRefHead");
static Symbol n_atOpt=Symbol.get( "n_atOpt");
static Symbol n_bound=Symbol.get( "n_bound");
static Symbol n_bounds1=Symbol.get( "n_bounds1");
static Symbol n_bytearrayhead=Symbol.get( "n_bytearrayhead");
static Symbol n_bytes=Symbol.get( "n_bytes");
static Symbol n_caValue=Symbol.get( "n_caValue");
static Symbol n_callConv=Symbol.get( "n_callConv");
static Symbol n_callKind=Symbol.get( "n_callKind");
static Symbol n_catchClause=Symbol.get( "n_catchClause");
static Symbol n_classAttr=Symbol.get( "n_classAttr");
static Symbol n_classDecl=Symbol.get( "n_classDecl");
static Symbol n_classDecls=Symbol.get( "n_classDecls");
static Symbol n_classHead=Symbol.get( "n_classHead");
static Symbol n_className=Symbol.get( "n_className");
static Symbol n_classNames=Symbol.get( "n_classNames");
static Symbol n_compQstring=Symbol.get( "n_compQstring");
static Symbol n_comtAttr=Symbol.get( "n_comtAttr");
static Symbol n_comtypeDecl=Symbol.get( "n_comtypeDecl");
static Symbol n_comtypeDecls=Symbol.get( "n_comtypeDecls");
static Symbol n_comtypeHead=Symbol.get( "n_comtypeHead");
static Symbol n_customAttrDecl=Symbol.get( "n_customAttrDecl");
static Symbol n_customHead=Symbol.get( "n_customHead");
static Symbol n_customHeadWithOwner=Symbol.get( "n_customHeadWithOwner");
static Symbol n_customType=Symbol.get( "n_customType");
static Symbol n_dataDecl=Symbol.get( "n_dataDecl");
static Symbol n_ddBody=Symbol.get( "n_ddBody");
static Symbol n_ddHead=Symbol.get( "n_ddHead");
static Symbol n_ddItem=Symbol.get( "n_ddItem");
static Symbol n_ddItemCount=Symbol.get( "n_ddItemCount");
static Symbol n_ddItemList=Symbol.get( "n_ddItemList");
static Symbol n_decl=Symbol.get( "n_decl");
static Symbol n_decls=Symbol.get( "n_decls");
static Symbol n_eventAttr=Symbol.get( "n_eventAttr");
static Symbol n_eventDecl=Symbol.get( "n_eventDecl");
static Symbol n_eventDecls=Symbol.get( "n_eventDecls");
static Symbol n_eventHead=Symbol.get( "n_eventHead");
static Symbol n_exportHead=Symbol.get( "n_exportHead");
static Symbol n_extSourcePosition=Symbol.get( "n_extSourcePosition");
static Symbol n_extSourceSpec=Symbol.get( "n_extSourceSpec");
static Symbol n_extendsClause=Symbol.get( "n_extendsClause");
static Symbol n_faultClause=Symbol.get( "n_faultClause");
static Symbol n_fieldAttr=Symbol.get( "n_fieldAttr");
static Symbol n_fieldDecl=Symbol.get( "n_fieldDecl");
static Symbol n_fieldInit=Symbol.get( "n_fieldInit");
static Symbol n_fileAttr=Symbol.get( "n_fileAttr");
static Symbol n_fileDecl=Symbol.get( "n_fileDecl");
static Symbol n_fileEntry=Symbol.get( "n_fileEntry");
static Symbol n_filterClause=Symbol.get( "n_filterClause");
static Symbol n_filterHead=Symbol.get( "n_filterHead");
static Symbol n_finallyClause=Symbol.get( "n_finallyClause");
static Symbol n_float64=Symbol.get( "n_float64");
static Symbol n_handlerBlock=Symbol.get( "n_handlerBlock");
static Symbol n_hashHead=Symbol.get( "n_hashHead");
static Symbol n_hexbytes=Symbol.get( "n_hexbytes");
static Symbol n_id=Symbol.get( "n_id");
static Symbol n_implAttr=Symbol.get( "n_implAttr");
static Symbol n_implClause=Symbol.get( "n_implClause");
static Symbol n_initOpt=Symbol.get( "n_initOpt");
static Symbol n_instr=Symbol.get( "n_instr");
static Symbol n_instr_r_head=Symbol.get( "n_instr_r_head");
static Symbol n_instr_tok_head=Symbol.get( "n_instr_tok_head");
static Symbol n_int16s=Symbol.get( "n_int16s");
static Symbol n_int32=Symbol.get( "n_int32");
static Symbol n_int64=Symbol.get( "n_int64");
static Symbol n_labels=Symbol.get( "n_labels");
static Symbol n_languageDecl=Symbol.get( "n_languageDecl");
static Symbol n_localeHead=Symbol.get( "n_localeHead");
static Symbol n_localsHead=Symbol.get( "n_localsHead");
static Symbol n_manifestResDecl=Symbol.get( "n_manifestResDecl");
static Symbol n_manifestResDecls=Symbol.get( "n_manifestResDecls");
static Symbol n_manifestResHead=Symbol.get( "n_manifestResHead");
static Symbol n_manresAttr=Symbol.get( "n_manresAttr");
static Symbol n_memberRef=Symbol.get( "n_memberRef");
static Symbol n_methAttr=Symbol.get( "n_methAttr");
static Symbol n_methodDecl=Symbol.get( "n_methodDecl");
static Symbol n_methodDecls=Symbol.get( "n_methodDecls");
static Symbol n_methodHead=Symbol.get( "n_methodHead");
static Symbol n_methodHeadPart1=Symbol.get( "n_methodHeadPart1");
static Symbol n_methodName=Symbol.get( "n_methodName");
static Symbol n_methodSpec=Symbol.get( "n_methodSpec");
static Symbol n_moduleHead=Symbol.get( "n_moduleHead");
static Symbol n_name1=Symbol.get( "n_name1");
static Symbol n_nameSpaceHead=Symbol.get( "n_nameSpaceHead");
static Symbol n_nameValPair=Symbol.get( "n_nameValPair");
static Symbol n_nameValPairs=Symbol.get( "n_nameValPairs");
static Symbol n_nativeType=Symbol.get( "n_nativeType");
static Symbol n_ownerType=Symbol.get( "n_ownerType");
static Symbol n_paramAttr=Symbol.get( "n_paramAttr");
static Symbol n_pinvAttr=Symbol.get( "n_pinvAttr");
static Symbol n_propAttr=Symbol.get( "n_propAttr");
static Symbol n_propDecl=Symbol.get( "n_propDecl");
static Symbol n_propDecls=Symbol.get( "n_propDecls");
static Symbol n_propHead=Symbol.get( "n_propHead");
static Symbol n_psetHead=Symbol.get( "n_psetHead");
static Symbol n_publicKeyHead=Symbol.get( "n_publicKeyHead");
static Symbol n_publicKeyTokenHead=Symbol.get( "n_publicKeyTokenHead");
static Symbol n_repeatOpt=Symbol.get( "n_repeatOpt");
static Symbol n_scopeBlock=Symbol.get( "n_scopeBlock");
static Symbol n_scopeOpen=Symbol.get( "n_scopeOpen");
static Symbol n_secAction=Symbol.get( "n_secAction");
static Symbol n_secDecl=Symbol.get( "n_secDecl");
static Symbol n_sehBlock=Symbol.get( "n_sehBlock");
static Symbol n_sehClause=Symbol.get( "n_sehClause");
static Symbol n_sehClauses=Symbol.get( "n_sehClauses");
static Symbol n_sigArg=Symbol.get( "n_sigArg");
static Symbol n_sigArgs0=Symbol.get( "n_sigArgs0");
static Symbol n_sigArgs1=Symbol.get( "n_sigArgs1");
static Symbol n_slashedName=Symbol.get( "n_slashedName");
static Symbol n_tlsSpec=Symbol.get( "n_tlsSpec");
static Symbol n_truefalse=Symbol.get( "n_truefalse");
static Symbol n_tryBlock=Symbol.get( "n_tryBlock");
static Symbol n_tryHead=Symbol.get( "n_tryHead");
static Symbol n_type=Symbol.get( "n_type");
static Symbol n_typeSpec=Symbol.get( "n_typeSpec");
static Symbol n_variantType=Symbol.get( "n_variantType");
static Symbol n_vtableDecl=Symbol.get( "n_vtableDecl");
static Symbol n_vtableHead=Symbol.get( "n_vtableHead");
static Symbol n_vtfixupAttr=Symbol.get( "n_vtfixupAttr");
static Symbol n_vtfixupDecl=Symbol.get( "n_vtfixupDecl");
static Symbol t_exclamation=reservedScope.getReserved( "!");
static Symbol t_ampersand=reservedScope.getReserved( "&");
static Symbol t_left_paren=reservedScope.getReserved( "(");
static Symbol t_right_paren=reservedScope.getReserved( ")");
static Symbol t_asterisk=reservedScope.getReserved( "*");
static Symbol t_plus=reservedScope.getReserved( "+");
static Symbol t_comma=reservedScope.getReserved( ",");
static Symbol t_period=reservedScope.getReserved( ".");
static Symbol t_ellipsis=reservedScope.getReserved( "...");
static Symbol t_addonDirective=reservedScope.getReserved( ".addon");
static Symbol t_assemblyDirective=reservedScope.getReserved( ".assembly");
static Symbol t_cctorDirective=reservedScope.getReserved( ".cctor");
static Symbol t_classDirective=reservedScope.getReserved( ".class");
static Symbol t_corflagsDirective=reservedScope.getReserved( ".corflags");
static Symbol t_ctorDirective=reservedScope.getReserved( ".ctor");
static Symbol t_customDirective=reservedScope.getReserved( ".custom");
static Symbol t_dataDirective=reservedScope.getReserved( ".data");
static Symbol t_emitbyteDirective=reservedScope.getReserved( ".emitbyte");
static Symbol t_entrypointDirective=reservedScope.getReserved( ".entrypoint");
static Symbol t_eventDirective=reservedScope.getReserved( ".event");
static Symbol t_exportDirective=reservedScope.getReserved( ".export");
static Symbol t_fieldDirective=reservedScope.getReserved( ".field");
static Symbol t_fileDirective=reservedScope.getReserved( ".file");
static Symbol t_fireDirective=reservedScope.getReserved( ".fire");
static Symbol t_getDirective=reservedScope.getReserved( ".get");
static Symbol t_hashDirective=reservedScope.getReserved( ".hash");
static Symbol t_imagebaseDirective=reservedScope.getReserved( ".imagebase");
static Symbol t_languageDirective=reservedScope.getReserved( ".language");
static Symbol t_lineDirective=reservedScope.getReserved( ".line");
static Symbol t_localeDirective=reservedScope.getReserved( ".locale");
static Symbol t_localsDirective=reservedScope.getReserved( ".locals");
static Symbol t_maxstackDirective=reservedScope.getReserved( ".maxstack");
static Symbol t_methodDirective=reservedScope.getReserved( ".method");
static Symbol t_moduleDirective=reservedScope.getReserved( ".module");
static Symbol t_mresourceDirective=reservedScope.getReserved( ".mresource");
static Symbol t_namespaceDirective=reservedScope.getReserved( ".namespace");
static Symbol t_otherDirective=reservedScope.getReserved( ".other");
static Symbol t_overrideDirective=reservedScope.getReserved( ".override");
static Symbol t_packDirective=reservedScope.getReserved( ".pack");
static Symbol t_paramDirective=reservedScope.getReserved( ".param");
static Symbol t_permissionDirective=reservedScope.getReserved( ".permission");
static Symbol t_permissionsetDirective=reservedScope.getReserved( ".permissionset");
static Symbol t_propertyDirective=reservedScope.getReserved( ".property");
static Symbol t_publickeyDirective=reservedScope.getReserved( ".publickey");
static Symbol t_publickeytokenDirective=reservedScope.getReserved( ".publickeytoken");
static Symbol t_removeonDirective=reservedScope.getReserved( ".removeon");
static Symbol t_setDirective=reservedScope.getReserved( ".set");
static Symbol t_sizeDirective=reservedScope.getReserved( ".size");
static Symbol t_subsystemDirective=reservedScope.getReserved( ".subsystem");
static Symbol t_tryDirective=reservedScope.getReserved( ".try");
static Symbol t_verDirective=reservedScope.getReserved( ".ver");
static Symbol t_vtableDirective=reservedScope.getReserved( ".vtable");
static Symbol t_vtentryDirective=reservedScope.getReserved( ".vtentry");
static Symbol t_vtfixupDirective=reservedScope.getReserved( ".vtfixup");
static Symbol t_zeroinitDirective=reservedScope.getReserved( ".zeroinit");
static Symbol t_slash=reservedScope.getReserved( "/");
static Symbol t_colon=reservedScope.getReserved( ":");
static Symbol t_member_op=reservedScope.getReserved( "::");
static Symbol t_equals=reservedScope.getReserved( "=");
static Symbol t_left_square=reservedScope.getReserved( "[");
static Symbol t_right_square=reservedScope.getReserved( "]");
static Symbol t_abstract=reservedScope.getReserved( "abstract");
static Symbol t_add=reservedScope.getReserved( "add");
static Symbol t_add_ovf=reservedScope.getReserved( "add.ovf");
static Symbol t_add_ovf_un=reservedScope.getReserved( "add.ovf.un");
static Symbol t_algorithm=reservedScope.getReserved( "algorithm");
static Symbol t_alignment=reservedScope.getReserved( "alignment");
static Symbol t_and=reservedScope.getReserved( "and");
static Symbol t_ansi=reservedScope.getReserved( "ansi");
static Symbol t_any=reservedScope.getReserved( "any");
static Symbol t_arglist=reservedScope.getReserved( "arglist");
static Symbol t_array=reservedScope.getReserved( "array");
static Symbol t_as=reservedScope.getReserved( "as");
static Symbol t_assembly=reservedScope.getReserved( "assembly");
static Symbol t_assert=reservedScope.getReserved( "assert");
static Symbol t_at=reservedScope.getReserved( "at");
static Symbol t_auto=reservedScope.getReserved( "auto");
static Symbol t_autochar=reservedScope.getReserved( "autochar");
static Symbol t_beforefieldinit=reservedScope.getReserved( "beforefieldinit");
static Symbol t_beq=reservedScope.getReserved( "beq");
static Symbol t_beq_s=reservedScope.getReserved( "beq.s");
static Symbol t_bge=reservedScope.getReserved( "bge");
static Symbol t_bge_s=reservedScope.getReserved( "bge.s");
static Symbol t_bge_un=reservedScope.getReserved( "bge.un");
static Symbol t_bge_un_s=reservedScope.getReserved( "bge.un.s");
static Symbol t_bgt=reservedScope.getReserved( "bgt");
static Symbol t_bgt_s=reservedScope.getReserved( "bgt.s");
static Symbol t_bgt_un=reservedScope.getReserved( "bgt.un");
static Symbol t_bgt_un_s=reservedScope.getReserved( "bgt.un.s");
static Symbol t_ble=reservedScope.getReserved( "ble");
static Symbol t_ble_s=reservedScope.getReserved( "ble.s");
static Symbol t_ble_un=reservedScope.getReserved( "ble.un");
static Symbol t_ble_un_s=reservedScope.getReserved( "ble.un.s");
static Symbol t_blob=reservedScope.getReserved( "blob");
static Symbol t_blob_object=reservedScope.getReserved( "blob_object");
static Symbol t_blt=reservedScope.getReserved( "blt");
static Symbol t_blt_s=reservedScope.getReserved( "blt.s");
static Symbol t_blt_un=reservedScope.getReserved( "blt.un");
static Symbol t_blt_un_s=reservedScope.getReserved( "blt.un.s");
static Symbol t_bne_un=reservedScope.getReserved( "bne.un");
static Symbol t_bne_un_s=reservedScope.getReserved( "bne.un.s");
static Symbol t_bool=reservedScope.getReserved( "bool");
static Symbol t_box=reservedScope.getReserved( "box");
static Symbol t_br=reservedScope.getReserved( "br");
static Symbol t_br_s=reservedScope.getReserved( "br.s");
static Symbol t_break=reservedScope.getReserved( "break");
static Symbol t_brfalse=reservedScope.getReserved( "brfalse");
static Symbol t_brfalse_s=reservedScope.getReserved( "brfalse.s");
static Symbol t_brinst=reservedScope.getReserved( "brinst");
static Symbol t_brinst_s=reservedScope.getReserved( "brinst.s");
static Symbol t_brnull=reservedScope.getReserved( "brnull");
static Symbol t_brnull_s=reservedScope.getReserved( "brnull.s");
static Symbol t_brtrue=reservedScope.getReserved( "brtrue");
static Symbol t_brtrue_s=reservedScope.getReserved( "brtrue.s");
static Symbol t_brzero=reservedScope.getReserved( "brzero");
static Symbol t_brzero_s=reservedScope.getReserved( "brzero.s");
static Symbol t_bstr=reservedScope.getReserved( "bstr");
static Symbol t_bytearray=reservedScope.getReserved( "bytearray");
static Symbol t_byvalstr=reservedScope.getReserved( "byvalstr");
static Symbol t_call=reservedScope.getReserved( "call");
static Symbol t_calli=reservedScope.getReserved( "calli");
static Symbol t_callmostderived=reservedScope.getReserved( "callmostderived");
static Symbol t_callvirt=reservedScope.getReserved( "callvirt");
static Symbol t_carray=reservedScope.getReserved( "carray");
static Symbol t_castclass=reservedScope.getReserved( "castclass");
static Symbol t_catch=reservedScope.getReserved( "catch");
static Symbol t_cdecl=reservedScope.getReserved( "cdecl");
static Symbol t_ceq=reservedScope.getReserved( "ceq");
static Symbol t_cf=reservedScope.getReserved( "cf");
static Symbol t_cgt=reservedScope.getReserved( "cgt");
static Symbol t_cgt_un=reservedScope.getReserved( "cgt.un");
static Symbol t_char=reservedScope.getReserved( "char");
static Symbol t_cil=reservedScope.getReserved( "cil");
static Symbol t_ckfinite=reservedScope.getReserved( "ckfinite");
static Symbol t_class=reservedScope.getReserved( "class");
static Symbol t_clsid=reservedScope.getReserved( "clsid");
static Symbol t_clt=reservedScope.getReserved( "clt");
static Symbol t_clt_un=reservedScope.getReserved( "clt.un");
static Symbol t_conv_i=reservedScope.getReserved( "conv.i");
static Symbol t_conv_i1=reservedScope.getReserved( "conv.i1");
static Symbol t_conv_i2=reservedScope.getReserved( "conv.i2");
static Symbol t_conv_i4=reservedScope.getReserved( "conv.i4");
static Symbol t_conv_i8=reservedScope.getReserved( "conv.i8");
static Symbol t_conv_ovf_i=reservedScope.getReserved( "conv.ovf.i");
static Symbol t_conv_ovf_i_un=reservedScope.getReserved( "conv.ovf.i.un");
static Symbol t_conv_ovf_i1=reservedScope.getReserved( "conv.ovf.i1");
static Symbol t_conv_ovf_i1_un=reservedScope.getReserved( "conv.ovf.i1.un");
static Symbol t_conv_ovf_i2=reservedScope.getReserved( "conv.ovf.i2");
static Symbol t_conv_ovf_i2_un=reservedScope.getReserved( "conv.ovf.i2.un");
static Symbol t_conv_ovf_i4=reservedScope.getReserved( "conv.ovf.i4");
static Symbol t_conv_ovf_i4_un=reservedScope.getReserved( "conv.ovf.i4.un");
static Symbol t_conv_ovf_i8=reservedScope.getReserved( "conv.ovf.i8");
static Symbol t_conv_ovf_i8_un=reservedScope.getReserved( "conv.ovf.i8.un");
static Symbol t_conv_ovf_u=reservedScope.getReserved( "conv.ovf.u");
static Symbol t_conv_ovf_u_un=reservedScope.getReserved( "conv.ovf.u.un");
static Symbol t_conv_ovf_u1=reservedScope.getReserved( "conv.ovf.u1");
static Symbol t_conv_ovf_u1_un=reservedScope.getReserved( "conv.ovf.u1.un");
static Symbol t_conv_ovf_u2=reservedScope.getReserved( "conv.ovf.u2");
static Symbol t_conv_ovf_u2_un=reservedScope.getReserved( "conv.ovf.u2.un");
static Symbol t_conv_ovf_u4=reservedScope.getReserved( "conv.ovf.u4");
static Symbol t_conv_ovf_u4_un=reservedScope.getReserved( "conv.ovf.u4.un");
static Symbol t_conv_ovf_u8=reservedScope.getReserved( "conv.ovf.u8");
static Symbol t_conv_ovf_u8_un=reservedScope.getReserved( "conv.ovf.u8.un");
static Symbol t_conv_r_un=reservedScope.getReserved( "conv.r.un");
static Symbol t_conv_r4=reservedScope.getReserved( "conv.r4");
static Symbol t_conv_r8=reservedScope.getReserved( "conv.r8");
static Symbol t_conv_u=reservedScope.getReserved( "conv.u");
static Symbol t_conv_u1=reservedScope.getReserved( "conv.u1");
static Symbol t_conv_u2=reservedScope.getReserved( "conv.u2");
static Symbol t_conv_u4=reservedScope.getReserved( "conv.u4");
static Symbol t_conv_u8=reservedScope.getReserved( "conv.u8");
static Symbol t_cpblk=reservedScope.getReserved( "cpblk");
static Symbol t_cpobj=reservedScope.getReserved( "cpobj");
static Symbol t_currency=reservedScope.getReserved( "currency");
static Symbol t_custom=reservedScope.getReserved( "custom");
static Symbol t_date=reservedScope.getReserved( "date");
static Symbol t_decimal=reservedScope.getReserved( "decimal");
static Symbol t_default=reservedScope.getReserved( "default");
static Symbol t_demand=reservedScope.getReserved( "demand");
static Symbol t_deny=reservedScope.getReserved( "deny");
static Symbol t_div=reservedScope.getReserved( "div");
static Symbol t_div_un=reservedScope.getReserved( "div.un");
static Symbol t_dup=reservedScope.getReserved( "dup");
static Symbol t_endfault=reservedScope.getReserved( "endfault");
static Symbol t_endfilter=reservedScope.getReserved( "endfilter");
static Symbol t_endfinally=reservedScope.getReserved( "endfinally");
static Symbol t_endmac=reservedScope.getReserved( "endmac");
static Symbol t_enum=reservedScope.getReserved( "enum");
static Symbol t_error=reservedScope.getReserved( "error");
static Symbol t_explicit=reservedScope.getReserved( "explicit");
static Symbol t_extends=reservedScope.getReserved( "extends");
static Symbol t_extern=reservedScope.getReserved( "extern");
static Symbol t_false=reservedScope.getReserved( "false");
static Symbol t_famandassem=reservedScope.getReserved( "famandassem");
static Symbol t_family=reservedScope.getReserved( "family");
static Symbol t_famorassem=reservedScope.getReserved( "famorassem");
static Symbol t_fastcall=reservedScope.getReserved( "fastcall");
static Symbol t_fault=reservedScope.getReserved( "fault");
static Symbol t_field=reservedScope.getReserved( "field");
static Symbol t_filetime=reservedScope.getReserved( "filetime");
static Symbol t_filter=reservedScope.getReserved( "filter");
static Symbol t_final=reservedScope.getReserved( "final");
static Symbol t_finally=reservedScope.getReserved( "finally");
static Symbol t_fixed=reservedScope.getReserved( "fixed");
static Symbol t_float=reservedScope.getReserved( "float");
static Symbol t_float32=reservedScope.getReserved( "float32");
static Symbol t_float64=reservedScope.getReserved( "float64");
static Symbol t_forwardref=reservedScope.getReserved( "forwardref");
static Symbol t_fromunmanaged=reservedScope.getReserved( "fromunmanaged");
static Symbol t_handler=reservedScope.getReserved( "handler");
static Symbol t_hidebysig=reservedScope.getReserved( "hidebysig");
static Symbol t_hresult=reservedScope.getReserved( "hresult");
static Symbol t_idispatch=reservedScope.getReserved( "idispatch");
static Symbol t_illegal=reservedScope.getReserved( "illegal");
static Symbol t_implements=reservedScope.getReserved( "implements");
static Symbol t_import=reservedScope.getReserved( "import");
static Symbol t_in=reservedScope.getReserved( "in");
static Symbol t_inheritcheck=reservedScope.getReserved( "inheritcheck");
static Symbol t_init=reservedScope.getReserved( "init");
static Symbol t_initblk=reservedScope.getReserved( "initblk");
static Symbol t_initobj=reservedScope.getReserved( "initobj");
static Symbol t_initonly=reservedScope.getReserved( "initonly");
static Symbol t_instance=reservedScope.getReserved( "instance");
static Symbol t_int=reservedScope.getReserved( "int");
static Symbol t_int16=reservedScope.getReserved( "int16");
static Symbol t_int32=reservedScope.getReserved( "int32");
static Symbol t_int64=reservedScope.getReserved( "int64");
static Symbol t_int8=reservedScope.getReserved( "int8");
static Symbol t_interface=reservedScope.getReserved( "interface");
static Symbol t_internalcall=reservedScope.getReserved( "internalcall");
static Symbol t_isinst=reservedScope.getReserved( "isinst");
static Symbol t_iunknown=reservedScope.getReserved( "iunknown");
static Symbol t_jmp=reservedScope.getReserved( "jmp");
static Symbol t_lasterr=reservedScope.getReserved( "lasterr");
static Symbol t_ldarg=reservedScope.getReserved( "ldarg");
static Symbol t_ldarg_0=reservedScope.getReserved( "ldarg.0");
static Symbol t_ldarg_1=reservedScope.getReserved( "ldarg.1");
static Symbol t_ldarg_2=reservedScope.getReserved( "ldarg.2");
static Symbol t_ldarg_3=reservedScope.getReserved( "ldarg.3");
static Symbol t_ldarg_s=reservedScope.getReserved( "ldarg.s");
static Symbol t_ldarga=reservedScope.getReserved( "ldarga");
static Symbol t_ldarga_s=reservedScope.getReserved( "ldarga.s");
static Symbol t_ldc_i4=reservedScope.getReserved( "ldc.i4");
static Symbol t_ldc_i4_0=reservedScope.getReserved( "ldc.i4.0");
static Symbol t_ldc_i4_1=reservedScope.getReserved( "ldc.i4.1");
static Symbol t_ldc_i4_2=reservedScope.getReserved( "ldc.i4.2");
static Symbol t_ldc_i4_3=reservedScope.getReserved( "ldc.i4.3");
static Symbol t_ldc_i4_4=reservedScope.getReserved( "ldc.i4.4");
static Symbol t_ldc_i4_5=reservedScope.getReserved( "ldc.i4.5");
static Symbol t_ldc_i4_6=reservedScope.getReserved( "ldc.i4.6");
static Symbol t_ldc_i4_7=reservedScope.getReserved( "ldc.i4.7");
static Symbol t_ldc_i4_8=reservedScope.getReserved( "ldc.i4.8");
static Symbol t_ldc_i4_M1=reservedScope.getReserved( "ldc.i4.M1");
static Symbol t_ldc_i4_m1=reservedScope.getReserved( "ldc.i4.m1");
static Symbol t_ldc_i4_s=reservedScope.getReserved( "ldc.i4.s");
static Symbol t_ldc_i8=reservedScope.getReserved( "ldc.i8");
static Symbol t_ldc_r4=reservedScope.getReserved( "ldc.r4");
static Symbol t_ldc_r8=reservedScope.getReserved( "ldc.r8");
static Symbol t_ldelem_i=reservedScope.getReserved( "ldelem.i");
static Symbol t_ldelem_i1=reservedScope.getReserved( "ldelem.i1");
static Symbol t_ldelem_i2=reservedScope.getReserved( "ldelem.i2");
static Symbol t_ldelem_i4=reservedScope.getReserved( "ldelem.i4");
static Symbol t_ldelem_i8=reservedScope.getReserved( "ldelem.i8");
static Symbol t_ldelem_r4=reservedScope.getReserved( "ldelem.r4");
static Symbol t_ldelem_r8=reservedScope.getReserved( "ldelem.r8");
static Symbol t_ldelem_ref=reservedScope.getReserved( "ldelem.ref");
static Symbol t_ldelem_u1=reservedScope.getReserved( "ldelem.u1");
static Symbol t_ldelem_u2=reservedScope.getReserved( "ldelem.u2");
static Symbol t_ldelem_u4=reservedScope.getReserved( "ldelem.u4");
static Symbol t_ldelem_u8=reservedScope.getReserved( "ldelem.u8");
static Symbol t_ldelema=reservedScope.getReserved( "ldelema");
static Symbol t_ldfld=reservedScope.getReserved( "ldfld");
static Symbol t_ldflda=reservedScope.getReserved( "ldflda");
static Symbol t_ldftn=reservedScope.getReserved( "ldftn");
static Symbol t_ldind_i=reservedScope.getReserved( "ldind.i");
static Symbol t_ldind_i1=reservedScope.getReserved( "ldind.i1");
static Symbol t_ldind_i2=reservedScope.getReserved( "ldind.i2");
static Symbol t_ldind_i4=reservedScope.getReserved( "ldind.i4");
static Symbol t_ldind_i8=reservedScope.getReserved( "ldind.i8");
static Symbol t_ldind_r4=reservedScope.getReserved( "ldind.r4");
static Symbol t_ldind_r8=reservedScope.getReserved( "ldind.r8");
static Symbol t_ldind_ref=reservedScope.getReserved( "ldind.ref");
static Symbol t_ldind_u1=reservedScope.getReserved( "ldind.u1");
static Symbol t_ldind_u2=reservedScope.getReserved( "ldind.u2");
static Symbol t_ldind_u4=reservedScope.getReserved( "ldind.u4");
static Symbol t_ldind_u8=reservedScope.getReserved( "ldind.u8");
static Symbol t_ldlen=reservedScope.getReserved( "ldlen");
static Symbol t_ldloc=reservedScope.getReserved( "ldloc");
static Symbol t_ldloc_0=reservedScope.getReserved( "ldloc.0");
static Symbol t_ldloc_1=reservedScope.getReserved( "ldloc.1");
static Symbol t_ldloc_2=reservedScope.getReserved( "ldloc.2");
static Symbol t_ldloc_3=reservedScope.getReserved( "ldloc.3");
static Symbol t_ldloc_s=reservedScope.getReserved( "ldloc.s");
static Symbol t_ldloca=reservedScope.getReserved( "ldloca");
static Symbol t_ldloca_s=reservedScope.getReserved( "ldloca.s");
static Symbol t_ldnull=reservedScope.getReserved( "ldnull");
static Symbol t_ldobj=reservedScope.getReserved( "ldobj");
static Symbol t_ldsfld=reservedScope.getReserved( "ldsfld");
static Symbol t_ldsflda=reservedScope.getReserved( "ldsflda");
static Symbol t_ldstr=reservedScope.getReserved( "ldstr");
static Symbol t_ldtoken=reservedScope.getReserved( "ldtoken");
static Symbol t_ldvirtftn=reservedScope.getReserved( "ldvirtftn");
static Symbol t_leave=reservedScope.getReserved( "leave");
static Symbol t_leave_s=reservedScope.getReserved( "leave.s");
static Symbol t_linkcheck=reservedScope.getReserved( "linkcheck");
static Symbol t_literal=reservedScope.getReserved( "literal");
static Symbol t_localloc=reservedScope.getReserved( "localloc");
static Symbol t_lpstr=reservedScope.getReserved( "lpstr");
static Symbol t_lpstruct=reservedScope.getReserved( "lpstruct");
static Symbol t_lptstr=reservedScope.getReserved( "lptstr");
static Symbol t_lpwstr=reservedScope.getReserved( "lpwstr");
static Symbol t_managed=reservedScope.getReserved( "managed");
static Symbol t_marshal=reservedScope.getReserved( "marshal");
static Symbol t_method=reservedScope.getReserved( "method");
static Symbol t_mkrefany=reservedScope.getReserved( "mkrefany");
static Symbol t_modopt=reservedScope.getReserved( "modopt");
static Symbol t_modreq=reservedScope.getReserved( "modreq");
static Symbol t_mul=reservedScope.getReserved( "mul");
static Symbol t_mul_ovf=reservedScope.getReserved( "mul.ovf");
static Symbol t_mul_ovf_un=reservedScope.getReserved( "mul.ovf.un");
static Symbol t_native=reservedScope.getReserved( "native");
static Symbol t_neg=reservedScope.getReserved( "neg");
static Symbol t_nested=reservedScope.getReserved( "nested");
static Symbol t_newarr=reservedScope.getReserved( "newarr");
static Symbol t_newobj=reservedScope.getReserved( "newobj");
static Symbol t_newslot=reservedScope.getReserved( "newslot");
static Symbol t_noappdomain=reservedScope.getReserved( "noappdomain");
static Symbol t_noinlining=reservedScope.getReserved( "noinlining");
static Symbol t_nomachine=reservedScope.getReserved( "nomachine");
static Symbol t_nomangle=reservedScope.getReserved( "nomangle");
static Symbol t_nometadata=reservedScope.getReserved( "nometadata");
static Symbol t_noncasdemand=reservedScope.getReserved( "noncasdemand");
static Symbol t_noncasinheritance=reservedScope.getReserved( "noncasinheritance");
static Symbol t_noncaslinkdemand=reservedScope.getReserved( "noncaslinkdemand");
static Symbol t_nop=reservedScope.getReserved( "nop");
static Symbol t_noprocess=reservedScope.getReserved( "noprocess");
static Symbol t_not=reservedScope.getReserved( "not");
static Symbol t_notserialized=reservedScope.getReserved( "notserialized");
static Symbol t_null=reservedScope.getReserved( "null");
static Symbol t_nullref=reservedScope.getReserved( "nullref");
static Symbol t_object=reservedScope.getReserved( "object");
static Symbol t_objectref=reservedScope.getReserved( "objectref");
static Symbol t_opt=reservedScope.getReserved( "opt");
static Symbol t_optil=reservedScope.getReserved( "optil");
static Symbol t_or=reservedScope.getReserved( "or");
static Symbol t_out=reservedScope.getReserved( "out");
static Symbol t_permitonly=reservedScope.getReserved( "permitonly");
static Symbol t_pinned=reservedScope.getReserved( "pinned");
static Symbol t_pinvokeimpl=reservedScope.getReserved( "pinvokeimpl");
static Symbol t_pop=reservedScope.getReserved( "pop");
static Symbol t_prefix1=reservedScope.getReserved( "prefix1");
static Symbol t_prefix2=reservedScope.getReserved( "prefix2");
static Symbol t_prefix3=reservedScope.getReserved( "prefix3");
static Symbol t_prefix4=reservedScope.getReserved( "prefix4");
static Symbol t_prefix5=reservedScope.getReserved( "prefix5");
static Symbol t_prefix6=reservedScope.getReserved( "prefix6");
static Symbol t_prefix7=reservedScope.getReserved( "prefix7");
static Symbol t_prefixref=reservedScope.getReserved( "prefixref");
static Symbol t_prejitdeny=reservedScope.getReserved( "prejitdeny");
static Symbol t_prejitgrant=reservedScope.getReserved( "prejitgrant");
static Symbol t_preservesig=reservedScope.getReserved( "preservesig");
static Symbol t_private=reservedScope.getReserved( "private");
static Symbol t_privatescope=reservedScope.getReserved( "privatescope");
static Symbol t_public=reservedScope.getReserved( "public");
static Symbol t_record=reservedScope.getReserved( "record");
static Symbol t_refanytype=reservedScope.getReserved( "refanytype");
static Symbol t_refanyval=reservedScope.getReserved( "refanyval");
static Symbol t_rem=reservedScope.getReserved( "rem");
static Symbol t_rem_un=reservedScope.getReserved( "rem.un");
static Symbol t_reqmin=reservedScope.getReserved( "reqmin");
static Symbol t_reqopt=reservedScope.getReserved( "reqopt");
static Symbol t_reqrefuse=reservedScope.getReserved( "reqrefuse");
static Symbol t_reqsecobj=reservedScope.getReserved( "reqsecobj");
static Symbol t_request=reservedScope.getReserved( "request");
static Symbol t_ret=reservedScope.getReserved( "ret");
static Symbol t_rethrow=reservedScope.getReserved( "rethrow");
static Symbol t_rtspecialname=reservedScope.getReserved( "rtspecialname");
static Symbol t_runtime=reservedScope.getReserved( "runtime");
static Symbol t_safearray=reservedScope.getReserved( "safearray");
static Symbol t_sealed=reservedScope.getReserved( "sealed");
static Symbol t_sequential=reservedScope.getReserved( "sequential");
static Symbol t_serializable=reservedScope.getReserved( "serializable");
static Symbol t_shl=reservedScope.getReserved( "shl");
static Symbol t_shr=reservedScope.getReserved( "shr");
static Symbol t_shr_un=reservedScope.getReserved( "shr.un");
static Symbol t_sizeof=reservedScope.getReserved( "sizeof");
static Symbol t_specialname=reservedScope.getReserved( "specialname");
static Symbol t_starg=reservedScope.getReserved( "starg");
static Symbol t_starg_s=reservedScope.getReserved( "starg.s");
static Symbol t_static=reservedScope.getReserved( "static");
static Symbol t_stdcall=reservedScope.getReserved( "stdcall");
static Symbol t_stelem_i=reservedScope.getReserved( "stelem.i");
static Symbol t_stelem_i1=reservedScope.getReserved( "stelem.i1");
static Symbol t_stelem_i2=reservedScope.getReserved( "stelem.i2");
static Symbol t_stelem_i4=reservedScope.getReserved( "stelem.i4");
static Symbol t_stelem_i8=reservedScope.getReserved( "stelem.i8");
static Symbol t_stelem_r4=reservedScope.getReserved( "stelem.r4");
static Symbol t_stelem_r8=reservedScope.getReserved( "stelem.r8");
static Symbol t_stelem_ref=reservedScope.getReserved( "stelem.ref");
static Symbol t_stfld=reservedScope.getReserved( "stfld");
static Symbol t_stind_i=reservedScope.getReserved( "stind.i");
static Symbol t_stind_i1=reservedScope.getReserved( "stind.i1");
static Symbol t_stind_i2=reservedScope.getReserved( "stind.i2");
static Symbol t_stind_i4=reservedScope.getReserved( "stind.i4");
static Symbol t_stind_i8=reservedScope.getReserved( "stind.i8");
static Symbol t_stind_r4=reservedScope.getReserved( "stind.r4");
static Symbol t_stind_r8=reservedScope.getReserved( "stind.r8");
static Symbol t_stind_ref=reservedScope.getReserved( "stind.ref");
static Symbol t_stloc=reservedScope.getReserved( "stloc");
static Symbol t_stloc_0=reservedScope.getReserved( "stloc.0");
static Symbol t_stloc_1=reservedScope.getReserved( "stloc.1");
static Symbol t_stloc_2=reservedScope.getReserved( "stloc.2");
static Symbol t_stloc_3=reservedScope.getReserved( "stloc.3");
static Symbol t_stloc_s=reservedScope.getReserved( "stloc.s");
static Symbol t_stobj=reservedScope.getReserved( "stobj");
static Symbol t_storage=reservedScope.getReserved( "storage");
static Symbol t_stored_object=reservedScope.getReserved( "stored_object");
static Symbol t_stream=reservedScope.getReserved( "stream");
static Symbol t_streamed_object=reservedScope.getReserved( "streamed_object");
static Symbol t_string=reservedScope.getReserved( "string");
static Symbol t_struct=reservedScope.getReserved( "struct");
static Symbol t_stsfld=reservedScope.getReserved( "stsfld");
static Symbol t_sub=reservedScope.getReserved( "sub");
static Symbol t_sub_ovf=reservedScope.getReserved( "sub.ovf");
static Symbol t_sub_ovf_un=reservedScope.getReserved( "sub.ovf.un");
static Symbol t_switch=reservedScope.getReserved( "switch");
static Symbol t_synchronized=reservedScope.getReserved( "synchronized");
static Symbol t_syschar=reservedScope.getReserved( "syschar");
static Symbol t_sysstring=reservedScope.getReserved( "sysstring");
static Symbol t_tail_=reservedScope.getReserved( "tail.");
static Symbol t_tbstr=reservedScope.getReserved( "tbstr");
static Symbol t_thiscall=reservedScope.getReserved( "thiscall");
static Symbol t_throw=reservedScope.getReserved( "throw");
static Symbol t_tls=reservedScope.getReserved( "tls");
static Symbol t_to=reservedScope.getReserved( "to");
static Symbol t_true=reservedScope.getReserved( "true");
static Symbol t_typedref=reservedScope.getReserved( "typedref");
static Symbol t_unaligned_=reservedScope.getReserved( "unaligned.");
static Symbol t_unbox=reservedScope.getReserved( "unbox");
static Symbol t_unicode=reservedScope.getReserved( "unicode");
static Symbol t_unmanaged=reservedScope.getReserved( "unmanaged");
static Symbol t_unmanagedexp=reservedScope.getReserved( "unmanagedexp");
static Symbol t_unsigned=reservedScope.getReserved( "unsigned");
static Symbol t_userdefined=reservedScope.getReserved( "userdefined");
static Symbol t_value=reservedScope.getReserved( "value");
static Symbol t_valuetype=reservedScope.getReserved( "valuetype");
static Symbol t_vararg=reservedScope.getReserved( "vararg");
static Symbol t_variant=reservedScope.getReserved( "variant");
static Symbol t_vector=reservedScope.getReserved( "vector");
static Symbol t_virtual=reservedScope.getReserved( "virtual");
static Symbol t_void=reservedScope.getReserved( "void");
static Symbol t_volatile_=reservedScope.getReserved( "volatile.");
static Symbol t_winapi=reservedScope.getReserved( "winapi");
static Symbol t_with=reservedScope.getReserved( "with");
static Symbol t_xor=reservedScope.getReserved( "xor");
static Symbol t_left_brace=reservedScope.getReserved( "{");
static Symbol t_right_brace=reservedScope.getReserved( "}");
static Symbol t_DOTTEDNAME=reservedScope.getReserved( "DOTTEDNAME");
static Symbol t_FLOAT64=reservedScope.getReserved( "FLOAT64");
static Symbol t_HEXBYTE=reservedScope.getReserved( "HEXBYTE");
static Symbol t_ID=reservedScope.getReserved( "ID");
static Symbol t_INSTR_PHI=reservedScope.getReserved( "INSTR_PHI");
static Symbol t_INSTR_RVA=reservedScope.getReserved( "INSTR_RVA");
static Symbol t_INT64=reservedScope.getReserved( "INT64");
static Symbol t_P_LINE=reservedScope.getReserved( "P_LINE");
static Symbol t_QSTRING=reservedScope.getReserved( "QSTRING");
static Symbol t_SQSTRING=reservedScope.getReserved( "SQSTRING");
static ParseState get_ParseState0() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_decls, 1)
},
new ReduceRule( n_decls, 0, null)
);
}
static ParseState get_ParseState1() { return
new ParseState( new ShiftRule[] { new ShiftRule( _end_, 2),
 new ShiftRule( t_classDirective, 5),
 new ShiftRule( t_namespaceDirective, 7),
 new ShiftRule( t_methodDirective, 10),
 new ShiftRule( t_fieldDirective, 12),
 new ShiftRule( t_dataDirective, 15),
 new ShiftRule( t_vtableDirective, 18),
 new ShiftRule( t_vtfixupDirective, 20),
 new ShiftRule( t_lineDirective, 22),
 new ShiftRule( t_P_LINE, 23),
 new ShiftRule( t_fileDirective, 25),
 new ShiftRule( t_assemblyDirective, 27),
 new ShiftRule( t_mresourceDirective, 31),
 new ShiftRule( t_moduleDirective, 33),
 new ShiftRule( t_permissionDirective, 35),
 new ShiftRule( t_permissionsetDirective, 37),
 new ShiftRule( t_customDirective, 39),
 new ShiftRule( t_subsystemDirective, 42),
 new ShiftRule( t_corflagsDirective, 43),
 new ShiftRule( t_imagebaseDirective, 44),
 new ShiftRule( t_languageDirective, 46)
},
new GotoRule[] { new GotoRule( n_decl, 3),
 new GotoRule( n_classHead, 4),
 new GotoRule( n_nameSpaceHead, 6),
 new GotoRule( n_methodHead, 8),
 new GotoRule( n_methodHeadPart1, 9),
 new GotoRule( n_fieldDecl, 11),
 new GotoRule( n_dataDecl, 13),
 new GotoRule( n_ddHead, 14),
 new GotoRule( n_vtableDecl, 16),
 new GotoRule( n_vtableHead, 17),
 new GotoRule( n_vtfixupDecl, 19),
 new GotoRule( n_extSourceSpec, 21),
 new GotoRule( n_fileDecl, 24),
 new GotoRule( n_assemblyHead, 26),
 new GotoRule( n_assemblyRefHead, 28),
 new GotoRule( n_comtypeHead, 29),
 new GotoRule( n_manifestResHead, 30),
 new GotoRule( n_moduleHead, 32),
 new GotoRule( n_secDecl, 34),
 new GotoRule( n_psetHead, 36),
 new GotoRule( n_customAttrDecl, 38),
 new GotoRule( n_customHead, 40),
 new GotoRule( n_customHeadWithOwner, 41),
 new GotoRule( n_languageDecl, 45)
},
null);
}
static ParseState get_ParseState2() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( _complete, 2, null)
);
}
static ParseState get_ParseState3() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_decls, 2, null)
);
}
static ParseState get_ParseState4() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_brace, 47)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState5() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_extern, 49)
},
new GotoRule[] { new GotoRule( n_classAttr, 48)
},
new ReduceRule( n_classAttr, 0, null)
);
}
static ParseState get_ParseState6() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_brace, 50)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState7() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_name1, 51),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState8() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_methodDecls, 56)
},
new ReduceRule( n_methodDecls, 0, null)
);
}
static ParseState get_ParseState9() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_methAttr, 57)
},
new ReduceRule( n_methAttr, 0, null)
);
}
static ParseState get_ParseState10() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methodHeadPart1, 1, null)
);
}
static ParseState get_ParseState11() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_decl, 1, null)
);
}
static ParseState get_ParseState12() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 59)
},
new GotoRule[] { new GotoRule( n_repeatOpt, 58)
},
new ReduceRule( n_repeatOpt, 0, null)
);
}
static ParseState get_ParseState13() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_decl, 1, null)
);
}
static ParseState get_ParseState14() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_brace, 61),
 new ShiftRule( t_char, 63),
 new ShiftRule( t_ampersand, 64),
 new ShiftRule( t_bytearray, 66),
 new ShiftRule( t_float32, 67),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_INT64, 71),
 new ShiftRule( t_int16, 73),
 new ShiftRule( t_int8, 74)
},
new GotoRule[] { new GotoRule( n_ddBody, 60),
 new GotoRule( n_ddItem, 62),
 new GotoRule( n_bytearrayhead, 65),
 new GotoRule( n_float64, 68),
 new GotoRule( n_int64, 70),
 new GotoRule( n_int32, 72)
},
null);
}
static ParseState get_ParseState15() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_tls, 76)
},
new GotoRule[] { new GotoRule( n_tlsSpec, 75)
},
new ReduceRule( n_tlsSpec, 0, null)
);
}
static ParseState get_ParseState16() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_decl, 1, null)
);
}
static ParseState get_ParseState17() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_HEXBYTE, 79)
},
new GotoRule[] { new GotoRule( n_bytes, 77),
 new GotoRule( n_hexbytes, 78)
},
new ReduceRule( n_bytes, 0, null)
);
}
static ParseState get_ParseState18() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_equals, 80)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState19() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_decl, 1, null)
);
}
static ParseState get_ParseState20() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 81)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState21() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_decl, 1, null)
);
}
static ParseState get_ParseState22() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_extSourcePosition, 82),
 new GotoRule( n_int32, 83)
},
null);
}
static ParseState get_ParseState23() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_extSourcePosition, 85),
 new GotoRule( n_int32, 83)
},
null);
}
static ParseState get_ParseState24() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_decl, 1, null)
);
}
static ParseState get_ParseState25() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_alignment, 87)
},
new GotoRule[] { new GotoRule( n_fileAttr, 86)
},
new ReduceRule( n_fileAttr, 0, null)
);
}
static ParseState get_ParseState26() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_brace, 88)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState27() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_extern, 90)
},
new GotoRule[] { new GotoRule( n_asmAttr, 89)
},
new ReduceRule( n_asmAttr, 0, null)
);
}
static ParseState get_ParseState28() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_brace, 91)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState29() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_brace, 92)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState30() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_brace, 93)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState31() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_manresAttr, 94)
},
new ReduceRule( n_manresAttr, 0, null)
);
}
static ParseState get_ParseState32() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_decl, 1, null)
);
}
static ParseState get_ParseState33() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_extern, 96)
},
new GotoRule[] { new GotoRule( n_name1, 95),
 new GotoRule( n_id, 52)
},
new ReduceRule( n_moduleHead, 1, null)
);
}
static ParseState get_ParseState34() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_decl, 1, null)
);
}
static ParseState get_ParseState35() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_request, 98),
 new ShiftRule( t_demand, 99),
 new ShiftRule( t_assert, 100),
 new ShiftRule( t_deny, 101),
 new ShiftRule( t_permitonly, 102),
 new ShiftRule( t_linkcheck, 103),
 new ShiftRule( t_inheritcheck, 104),
 new ShiftRule( t_reqmin, 105),
 new ShiftRule( t_reqopt, 106),
 new ShiftRule( t_reqrefuse, 107),
 new ShiftRule( t_prejitgrant, 108),
 new ShiftRule( t_prejitdeny, 109),
 new ShiftRule( t_noncasdemand, 110),
 new ShiftRule( t_noncaslinkdemand, 111),
 new ShiftRule( t_noncasinheritance, 112)
},
new GotoRule[] { new GotoRule( n_secAction, 97)
},
null);
}
static ParseState get_ParseState36() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_HEXBYTE, 79)
},
new GotoRule[] { new GotoRule( n_bytes, 113),
 new GotoRule( n_hexbytes, 78)
},
new ReduceRule( n_bytes, 0, null)
);
}
static ParseState get_ParseState37() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_request, 98),
 new ShiftRule( t_demand, 99),
 new ShiftRule( t_assert, 100),
 new ShiftRule( t_deny, 101),
 new ShiftRule( t_permitonly, 102),
 new ShiftRule( t_linkcheck, 103),
 new ShiftRule( t_inheritcheck, 104),
 new ShiftRule( t_reqmin, 105),
 new ShiftRule( t_reqopt, 106),
 new ShiftRule( t_reqrefuse, 107),
 new ShiftRule( t_prejitgrant, 108),
 new ShiftRule( t_prejitdeny, 109),
 new ShiftRule( t_noncasdemand, 110),
 new ShiftRule( t_noncaslinkdemand, 111),
 new ShiftRule( t_noncasinheritance, 112)
},
new GotoRule[] { new GotoRule( n_secAction, 114)
},
null);
}
static ParseState get_ParseState38() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_decl, 1, null)
);
}
static ParseState get_ParseState39() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_instance, 117),
 new ShiftRule( t_explicit, 118),
 new ShiftRule( t_default, 120),
 new ShiftRule( t_vararg, 121),
 new ShiftRule( t_unmanaged, 122),
 new ShiftRule( t_left_paren, 123)
},
new GotoRule[] { new GotoRule( n_customType, 115),
 new GotoRule( n_callConv, 116),
 new GotoRule( n_callKind, 119)
},
new ReduceRule( n_callKind, 0, null)
);
}
static ParseState get_ParseState40() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_HEXBYTE, 79)
},
new GotoRule[] { new GotoRule( n_bytes, 124),
 new GotoRule( n_hexbytes, 78)
},
new ReduceRule( n_bytes, 0, null)
);
}
static ParseState get_ParseState41() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_HEXBYTE, 79)
},
new GotoRule[] { new GotoRule( n_bytes, 125),
 new GotoRule( n_hexbytes, 78)
},
new ReduceRule( n_bytes, 0, null)
);
}
static ParseState get_ParseState42() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 126)
},
null);
}
static ParseState get_ParseState43() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 127)
},
null);
}
static ParseState get_ParseState44() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 129)
},
new GotoRule[] { new GotoRule( n_int64, 128)
},
null);
}
static ParseState get_ParseState45() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_decl, 1, null)
);
}
static ParseState get_ParseState46() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_SQSTRING, 130)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState47() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_classDecls, 131)
},
new ReduceRule( n_classDecls, 0, null)
);
}
static ParseState get_ParseState48() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_public, 133),
 new ShiftRule( t_private, 134),
 new ShiftRule( t_value, 135),
 new ShiftRule( t_enum, 136),
 new ShiftRule( t_interface, 137),
 new ShiftRule( t_sealed, 138),
 new ShiftRule( t_abstract, 139),
 new ShiftRule( t_auto, 140),
 new ShiftRule( t_sequential, 141),
 new ShiftRule( t_explicit, 142),
 new ShiftRule( t_ansi, 143),
 new ShiftRule( t_unicode, 144),
 new ShiftRule( t_autochar, 145),
 new ShiftRule( t_import, 146),
 new ShiftRule( t_serializable, 147),
 new ShiftRule( t_nested, 148),
 new ShiftRule( t_beforefieldinit, 149),
 new ShiftRule( t_specialname, 150),
 new ShiftRule( t_rtspecialname, 151)
},
new GotoRule[] { new GotoRule( n_id, 132)
},
null);
}
static ParseState get_ParseState49() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_comtAttr, 152)
},
new ReduceRule( n_comtAttr, 0, null)
);
}
static ParseState get_ParseState50() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_decls, 153)
},
new ReduceRule( n_decls, 0, null)
);
}
static ParseState get_ParseState51() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_period, 154)
},
new GotoRule[] {},
new ReduceRule( n_nameSpaceHead, 2, null)
);
}
static ParseState get_ParseState52() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_name1, 1, null)
);
}
static ParseState get_ParseState53() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_id, 1, null)
);
}
static ParseState get_ParseState54() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_id, 1, null)
);
}
static ParseState get_ParseState55() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_name1, 1, null)
);
}
static ParseState get_ParseState56() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_brace, 155),
 new ShiftRule( t_emitbyteDirective, 157),
 new ShiftRule( t_tryDirective, 161),
 new ShiftRule( t_maxstackDirective, 162),
 new ShiftRule( t_localsDirective, 164),
 new ShiftRule( t_entrypointDirective, 165),
 new ShiftRule( t_zeroinitDirective, 166),
 new ShiftRule( t_dataDirective, 15),
 new ShiftRule( t_nop, 170),
 new ShiftRule( t_break, 171),
 new ShiftRule( t_ldarg_0, 172),
 new ShiftRule( t_ldarg_1, 173),
 new ShiftRule( t_ldarg_2, 174),
 new ShiftRule( t_ldarg_3, 175),
 new ShiftRule( t_ldloc_0, 176),
 new ShiftRule( t_ldloc_1, 177),
 new ShiftRule( t_ldloc_2, 178),
 new ShiftRule( t_ldloc_3, 179),
 new ShiftRule( t_stloc_0, 180),
 new ShiftRule( t_stloc_1, 181),
 new ShiftRule( t_stloc_2, 182),
 new ShiftRule( t_stloc_3, 183),
 new ShiftRule( t_ldnull, 184),
 new ShiftRule( t_ldc_i4_m1, 185),
 new ShiftRule( t_ldc_i4_0, 186),
 new ShiftRule( t_ldc_i4_1, 187),
 new ShiftRule( t_ldc_i4_2, 188),
 new ShiftRule( t_ldc_i4_3, 189),
 new ShiftRule( t_ldc_i4_4, 190),
 new ShiftRule( t_ldc_i4_5, 191),
 new ShiftRule( t_ldc_i4_6, 192),
 new ShiftRule( t_ldc_i4_7, 193),
 new ShiftRule( t_ldc_i4_8, 194),
 new ShiftRule( t_dup, 195),
 new ShiftRule( t_pop, 196),
 new ShiftRule( t_ret, 197),
 new ShiftRule( t_ldind_i1, 198),
 new ShiftRule( t_ldind_u1, 199),
 new ShiftRule( t_ldind_i2, 200),
 new ShiftRule( t_ldind_u2, 201),
 new ShiftRule( t_ldind_i4, 202),
 new ShiftRule( t_ldind_u4, 203),
 new ShiftRule( t_ldind_i8, 204),
 new ShiftRule( t_ldind_i, 205),
 new ShiftRule( t_ldind_r4, 206),
 new ShiftRule( t_ldind_r8, 207),
 new ShiftRule( t_ldind_ref, 208),
 new ShiftRule( t_stind_ref, 209),
 new ShiftRule( t_stind_i1, 210),
 new ShiftRule( t_stind_i2, 211),
 new ShiftRule( t_stind_i4, 212),
 new ShiftRule( t_stind_i8, 213),
 new ShiftRule( t_stind_r4, 214),
 new ShiftRule( t_stind_r8, 215),
 new ShiftRule( t_add, 216),
 new ShiftRule( t_sub, 217),
 new ShiftRule( t_mul, 218),
 new ShiftRule( t_div, 219),
 new ShiftRule( t_div_un, 220),
 new ShiftRule( t_rem, 221),
 new ShiftRule( t_rem_un, 222),
 new ShiftRule( t_and, 223),
 new ShiftRule( t_or, 224),
 new ShiftRule( t_xor, 225),
 new ShiftRule( t_shl, 226),
 new ShiftRule( t_shr, 227),
 new ShiftRule( t_shr_un, 228),
 new ShiftRule( t_neg, 229),
 new ShiftRule( t_not, 230),
 new ShiftRule( t_conv_i1, 231),
 new ShiftRule( t_conv_i2, 232),
 new ShiftRule( t_conv_i4, 233),
 new ShiftRule( t_conv_i8, 234),
 new ShiftRule( t_conv_r4, 235),
 new ShiftRule( t_conv_r8, 236),
 new ShiftRule( t_conv_u4, 237),
 new ShiftRule( t_conv_u8, 238),
 new ShiftRule( t_conv_r_un, 239),
 new ShiftRule( t_throw, 240),
 new ShiftRule( t_conv_ovf_i1_un, 241),
 new ShiftRule( t_conv_ovf_i2_un, 242),
 new ShiftRule( t_conv_ovf_i4_un, 243),
 new ShiftRule( t_conv_ovf_i8_un, 244),
 new ShiftRule( t_conv_ovf_u1_un, 245),
 new ShiftRule( t_conv_ovf_u2_un, 246),
 new ShiftRule( t_conv_ovf_u4_un, 247),
 new ShiftRule( t_conv_ovf_u8_un, 248),
 new ShiftRule( t_conv_ovf_i_un, 249),
 new ShiftRule( t_conv_ovf_u_un, 250),
 new ShiftRule( t_ldlen, 251),
 new ShiftRule( t_ldelem_i1, 252),
 new ShiftRule( t_ldelem_u1, 253),
 new ShiftRule( t_ldelem_i2, 254),
 new ShiftRule( t_ldelem_u2, 255),
 new ShiftRule( t_ldelem_i4, 256),
 new ShiftRule( t_ldelem_u4, 257),
 new ShiftRule( t_ldelem_i8, 258),
 new ShiftRule( t_ldelem_i, 259),
 new ShiftRule( t_ldelem_r4, 260),
 new ShiftRule( t_ldelem_r8, 261),
 new ShiftRule( t_ldelem_ref, 262),
 new ShiftRule( t_stelem_i, 263),
 new ShiftRule( t_stelem_i1, 264),
 new ShiftRule( t_stelem_i2, 265),
 new ShiftRule( t_stelem_i4, 266),
 new ShiftRule( t_stelem_i8, 267),
 new ShiftRule( t_stelem_r4, 268),
 new ShiftRule( t_stelem_r8, 269),
 new ShiftRule( t_stelem_ref, 270),
 new ShiftRule( t_conv_ovf_i1, 271),
 new ShiftRule( t_conv_ovf_u1, 272),
 new ShiftRule( t_conv_ovf_i2, 273),
 new ShiftRule( t_conv_ovf_u2, 274),
 new ShiftRule( t_conv_ovf_i4, 275),
 new ShiftRule( t_conv_ovf_u4, 276),
 new ShiftRule( t_conv_ovf_i8, 277),
 new ShiftRule( t_conv_ovf_u8, 278),
 new ShiftRule( t_ckfinite, 279),
 new ShiftRule( t_conv_u2, 280),
 new ShiftRule( t_conv_u1, 281),
 new ShiftRule( t_conv_i, 282),
 new ShiftRule( t_conv_ovf_i, 283),
 new ShiftRule( t_conv_ovf_u, 284),
 new ShiftRule( t_add_ovf, 285),
 new ShiftRule( t_add_ovf_un, 286),
 new ShiftRule( t_mul_ovf, 287),
 new ShiftRule( t_mul_ovf_un, 288),
 new ShiftRule( t_sub_ovf, 289),
 new ShiftRule( t_sub_ovf_un, 290),
 new ShiftRule( t_endfinally, 291),
 new ShiftRule( t_stind_i, 292),
 new ShiftRule( t_conv_u, 293),
 new ShiftRule( t_prefix7, 294),
 new ShiftRule( t_prefix6, 295),
 new ShiftRule( t_prefix5, 296),
 new ShiftRule( t_prefix4, 297),
 new ShiftRule( t_prefix3, 298),
 new ShiftRule( t_prefix2, 299),
 new ShiftRule( t_prefix1, 300),
 new ShiftRule( t_prefixref, 301),
 new ShiftRule( t_arglist, 302),
 new ShiftRule( t_ceq, 303),
 new ShiftRule( t_cgt, 304),
 new ShiftRule( t_cgt_un, 305),
 new ShiftRule( t_clt, 306),
 new ShiftRule( t_clt_un, 307),
 new ShiftRule( t_localloc, 308),
 new ShiftRule( t_endfilter, 309),
 new ShiftRule( t_volatile_, 310),
 new ShiftRule( t_tail_, 311),
 new ShiftRule( t_cpblk, 312),
 new ShiftRule( t_initblk, 313),
 new ShiftRule( t_rethrow, 314),
 new ShiftRule( t_refanytype, 315),
 new ShiftRule( t_illegal, 316),
 new ShiftRule( t_endmac, 317),
 new ShiftRule( t_ldind_u8, 318),
 new ShiftRule( t_ldelem_u8, 319),
 new ShiftRule( t_ldc_i4_M1, 320),
 new ShiftRule( t_endfault, 321),
 new ShiftRule( t_ldarg_s, 323),
 new ShiftRule( t_ldarga_s, 324),
 new ShiftRule( t_starg_s, 325),
 new ShiftRule( t_ldloc_s, 326),
 new ShiftRule( t_ldloca_s, 327),
 new ShiftRule( t_stloc_s, 328),
 new ShiftRule( t_ldarg, 329),
 new ShiftRule( t_ldarga, 330),
 new ShiftRule( t_starg, 331),
 new ShiftRule( t_ldloc, 332),
 new ShiftRule( t_ldloca, 333),
 new ShiftRule( t_stloc, 334),
 new ShiftRule( t_ldc_i4_s, 336),
 new ShiftRule( t_ldc_i4, 337),
 new ShiftRule( t_unaligned_, 338),
 new ShiftRule( t_ldc_i8, 340),
 new ShiftRule( t_ldc_r4, 342),
 new ShiftRule( t_ldc_r8, 343),
 new ShiftRule( t_br_s, 346),
 new ShiftRule( t_brfalse_s, 347),
 new ShiftRule( t_brtrue_s, 348),
 new ShiftRule( t_beq_s, 349),
 new ShiftRule( t_bge_s, 350),
 new ShiftRule( t_bgt_s, 351),
 new ShiftRule( t_ble_s, 352),
 new ShiftRule( t_blt_s, 353),
 new ShiftRule( t_bne_un_s, 354),
 new ShiftRule( t_bge_un_s, 355),
 new ShiftRule( t_bgt_un_s, 356),
 new ShiftRule( t_ble_un_s, 357),
 new ShiftRule( t_blt_un_s, 358),
 new ShiftRule( t_br, 359),
 new ShiftRule( t_brfalse, 360),
 new ShiftRule( t_brtrue, 361),
 new ShiftRule( t_beq, 362),
 new ShiftRule( t_bge, 363),
 new ShiftRule( t_bgt, 364),
 new ShiftRule( t_ble, 365),
 new ShiftRule( t_blt, 366),
 new ShiftRule( t_bne_un, 367),
 new ShiftRule( t_bge_un, 368),
 new ShiftRule( t_bgt_un, 369),
 new ShiftRule( t_ble_un, 370),
 new ShiftRule( t_blt_un, 371),
 new ShiftRule( t_leave, 372),
 new ShiftRule( t_leave_s, 373),
 new ShiftRule( t_brnull, 374),
 new ShiftRule( t_brnull_s, 375),
 new ShiftRule( t_brzero, 376),
 new ShiftRule( t_brzero_s, 377),
 new ShiftRule( t_brinst, 378),
 new ShiftRule( t_brinst_s, 379),
 new ShiftRule( t_jmp, 381),
 new ShiftRule( t_call, 382),
 new ShiftRule( t_callvirt, 383),
 new ShiftRule( t_newobj, 384),
 new ShiftRule( t_ldftn, 385),
 new ShiftRule( t_ldvirtftn, 386),
 new ShiftRule( t_ldfld, 388),
 new ShiftRule( t_ldflda, 389),
 new ShiftRule( t_stfld, 390),
 new ShiftRule( t_ldsfld, 391),
 new ShiftRule( t_ldsflda, 392),
 new ShiftRule( t_stsfld, 393),
 new ShiftRule( t_cpobj, 395),
 new ShiftRule( t_ldobj, 396),
 new ShiftRule( t_castclass, 397),
 new ShiftRule( t_isinst, 398),
 new ShiftRule( t_unbox, 399),
 new ShiftRule( t_stobj, 400),
 new ShiftRule( t_box, 401),
 new ShiftRule( t_newarr, 402),
 new ShiftRule( t_ldelema, 403),
 new ShiftRule( t_refanyval, 404),
 new ShiftRule( t_mkrefany, 405),
 new ShiftRule( t_initobj, 406),
 new ShiftRule( t_sizeof, 407),
 new ShiftRule( t_ldstr, 409),
 new ShiftRule( t_calli, 411),
 new ShiftRule( t_INSTR_RVA, 412),
 new ShiftRule( t_ldtoken, 415),
 new ShiftRule( t_switch, 417),
 new ShiftRule( t_INSTR_PHI, 418),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_permissionDirective, 35),
 new ShiftRule( t_permissionsetDirective, 37),
 new ShiftRule( t_lineDirective, 22),
 new ShiftRule( t_P_LINE, 23),
 new ShiftRule( t_languageDirective, 46),
 new ShiftRule( t_customDirective, 39),
 new ShiftRule( t_exportDirective, 424),
 new ShiftRule( t_vtentryDirective, 425),
 new ShiftRule( t_overrideDirective, 426),
 new ShiftRule( t_left_brace, 429),
 new ShiftRule( t_paramDirective, 430)
},
new GotoRule[] { new GotoRule( n_methodDecl, 156),
 new GotoRule( n_sehBlock, 158),
 new GotoRule( n_tryBlock, 159),
 new GotoRule( n_tryHead, 160),
 new GotoRule( n_localsHead, 163),
 new GotoRule( n_dataDecl, 167),
 new GotoRule( n_ddHead, 14),
 new GotoRule( n_instr, 168),
 new GotoRule( n_INSTR_NONE, 169),
 new GotoRule( n_INSTR_VAR, 322),
 new GotoRule( n_INSTR_I, 335),
 new GotoRule( n_INSTR_I8, 339),
 new GotoRule( n_INSTR_R, 341),
 new GotoRule( n_instr_r_head, 344),
 new GotoRule( n_INSTR_BRTARGET, 345),
 new GotoRule( n_INSTR_METHOD, 380),
 new GotoRule( n_INSTR_FIELD, 387),
 new GotoRule( n_INSTR_TYPE, 394),
 new GotoRule( n_INSTR_STRING, 408),
 new GotoRule( n_INSTR_SIG, 410),
 new GotoRule( n_instr_tok_head, 413),
 new GotoRule( n_INSTR_TOK, 414),
 new GotoRule( n_INSTR_SWITCH, 416),
 new GotoRule( n_id, 419),
 new GotoRule( n_secDecl, 420),
 new GotoRule( n_psetHead, 36),
 new GotoRule( n_extSourceSpec, 421),
 new GotoRule( n_languageDecl, 422),
 new GotoRule( n_customAttrDecl, 423),
 new GotoRule( n_customHead, 40),
 new GotoRule( n_customHeadWithOwner, 41),
 new GotoRule( n_scopeBlock, 427),
 new GotoRule( n_scopeOpen, 428)
},
null);
}
static ParseState get_ParseState57() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_instance, 117),
 new ShiftRule( t_explicit, 118),
 new ShiftRule( t_default, 120),
 new ShiftRule( t_vararg, 121),
 new ShiftRule( t_unmanaged, 122),
 new ShiftRule( t_static, 432),
 new ShiftRule( t_public, 433),
 new ShiftRule( t_private, 434),
 new ShiftRule( t_family, 435),
 new ShiftRule( t_final, 436),
 new ShiftRule( t_specialname, 437),
 new ShiftRule( t_virtual, 438),
 new ShiftRule( t_abstract, 439),
 new ShiftRule( t_assembly, 440),
 new ShiftRule( t_famandassem, 441),
 new ShiftRule( t_famorassem, 442),
 new ShiftRule( t_privatescope, 443),
 new ShiftRule( t_hidebysig, 444),
 new ShiftRule( t_newslot, 445),
 new ShiftRule( t_rtspecialname, 446),
 new ShiftRule( t_unmanagedexp, 447),
 new ShiftRule( t_reqsecobj, 448),
 new ShiftRule( t_pinvokeimpl, 449)
},
new GotoRule[] { new GotoRule( n_callConv, 431),
 new GotoRule( n_callKind, 119)
},
new ReduceRule( n_callKind, 0, null)
);
}
static ParseState get_ParseState58() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_fieldAttr, 450)
},
new ReduceRule( n_fieldAttr, 0, null)
);
}
static ParseState get_ParseState59() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 451)
},
null);
}
static ParseState get_ParseState60() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_dataDecl, 2, null)
);
}
static ParseState get_ParseState61() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_char, 63),
 new ShiftRule( t_ampersand, 64),
 new ShiftRule( t_bytearray, 66),
 new ShiftRule( t_float32, 67),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_INT64, 71),
 new ShiftRule( t_int16, 73),
 new ShiftRule( t_int8, 74)
},
new GotoRule[] { new GotoRule( n_ddItemList, 452),
 new GotoRule( n_ddItem, 453),
 new GotoRule( n_bytearrayhead, 65),
 new GotoRule( n_float64, 68),
 new GotoRule( n_int64, 70),
 new GotoRule( n_int32, 72)
},
null);
}
static ParseState get_ParseState62() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_ddBody, 1, null)
);
}
static ParseState get_ParseState63() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_asterisk, 454)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState64() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 455)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState65() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_HEXBYTE, 79)
},
new GotoRule[] { new GotoRule( n_bytes, 456),
 new GotoRule( n_hexbytes, 78)
},
new ReduceRule( n_bytes, 0, null)
);
}
static ParseState get_ParseState66() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 457)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState67() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 458),
 new ShiftRule( t_left_square, 460)
},
new GotoRule[] { new GotoRule( n_ddItemCount, 459)
},
new ReduceRule( n_ddItemCount, 0, null)
);
}
static ParseState get_ParseState68() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 461),
 new ShiftRule( t_left_square, 460)
},
new GotoRule[] { new GotoRule( n_ddItemCount, 462)
},
new ReduceRule( n_ddItemCount, 0, null)
);
}
static ParseState get_ParseState69() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_float64, 1, null)
);
}
static ParseState get_ParseState70() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 463),
 new ShiftRule( t_left_square, 460)
},
new GotoRule[] { new GotoRule( n_ddItemCount, 464)
},
new ReduceRule( n_ddItemCount, 0, null)
);
}
static ParseState get_ParseState71() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_int32, 1, null)
);
}
static ParseState get_ParseState72() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 465),
 new ShiftRule( t_left_square, 460)
},
new GotoRule[] { new GotoRule( n_ddItemCount, 466)
},
new ReduceRule( n_ddItemCount, 0, null)
);
}
static ParseState get_ParseState73() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 467),
 new ShiftRule( t_left_square, 460)
},
new GotoRule[] { new GotoRule( n_ddItemCount, 468)
},
new ReduceRule( n_ddItemCount, 0, null)
);
}
static ParseState get_ParseState74() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 469),
 new ShiftRule( t_left_square, 460)
},
new GotoRule[] { new GotoRule( n_ddItemCount, 470)
},
new ReduceRule( n_ddItemCount, 0, null)
);
}
static ParseState get_ParseState75() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54)
},
new GotoRule[] { new GotoRule( n_id, 471)
},
new ReduceRule( n_ddHead, 2, null)
);
}
static ParseState get_ParseState76() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_tlsSpec, 1, null)
);
}
static ParseState get_ParseState77() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 472)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState78() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_HEXBYTE, 473)
},
new GotoRule[] {},
new ReduceRule( n_bytes, 1, null)
);
}
static ParseState get_ParseState79() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_hexbytes, 1, null)
);
}
static ParseState get_ParseState80() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 474)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState81() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 475)
},
null);
}
static ParseState get_ParseState82() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_SQSTRING, 476),
 new ShiftRule( t_colon, 477)
},
new GotoRule[] {},
new ReduceRule( n_extSourceSpec, 2, null)
);
}
static ParseState get_ParseState83() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_comma, 478)
},
new GotoRule[] {},
new ReduceRule( n_extSourcePosition, 1, null)
);
}
static ParseState get_ParseState84() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_int32, 1, null)
);
}
static ParseState get_ParseState85() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_QSTRING, 479)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState86() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_nometadata, 481)
},
new GotoRule[] { new GotoRule( n_name1, 480),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState87() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 482)
},
null);
}
static ParseState get_ParseState88() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_assemblyDecls, 483)
},
new ReduceRule( n_assemblyDecls, 0, null)
);
}
static ParseState get_ParseState89() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_noappdomain, 485),
 new ShiftRule( t_noprocess, 486),
 new ShiftRule( t_nomachine, 487)
},
new GotoRule[] { new GotoRule( n_name1, 484),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState90() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_name1, 488),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState91() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_assemblyRefDecls, 489)
},
new ReduceRule( n_assemblyRefDecls, 0, null)
);
}
static ParseState get_ParseState92() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_comtypeDecls, 490)
},
new ReduceRule( n_comtypeDecls, 0, null)
);
}
static ParseState get_ParseState93() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_manifestResDecls, 491)
},
new ReduceRule( n_manifestResDecls, 0, null)
);
}
static ParseState get_ParseState94() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_public, 493),
 new ShiftRule( t_private, 494)
},
new GotoRule[] { new GotoRule( n_name1, 492),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState95() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_period, 154)
},
new GotoRule[] {},
new ReduceRule( n_moduleHead, 2, null)
);
}
static ParseState get_ParseState96() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_name1, 495),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState97() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 498),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522)
},
new GotoRule[] { new GotoRule( n_typeSpec, 496),
 new GotoRule( n_className, 497),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 500),
 new GotoRule( n_id, 52),
 new GotoRule( n_type, 501),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState98() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_secAction, 1, null)
);
}
static ParseState get_ParseState99() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_secAction, 1, null)
);
}
static ParseState get_ParseState100() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_secAction, 1, null)
);
}
static ParseState get_ParseState101() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_secAction, 1, null)
);
}
static ParseState get_ParseState102() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_secAction, 1, null)
);
}
static ParseState get_ParseState103() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_secAction, 1, null)
);
}
static ParseState get_ParseState104() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_secAction, 1, null)
);
}
static ParseState get_ParseState105() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_secAction, 1, null)
);
}
static ParseState get_ParseState106() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_secAction, 1, null)
);
}
static ParseState get_ParseState107() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_secAction, 1, null)
);
}
static ParseState get_ParseState108() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_secAction, 1, null)
);
}
static ParseState get_ParseState109() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_secAction, 1, null)
);
}
static ParseState get_ParseState110() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_secAction, 1, null)
);
}
static ParseState get_ParseState111() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_secAction, 1, null)
);
}
static ParseState get_ParseState112() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_secAction, 1, null)
);
}
static ParseState get_ParseState113() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 523)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState114() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_equals, 524)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState115() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_equals, 525)
},
new GotoRule[] {},
new ReduceRule( n_customAttrDecl, 2, null)
);
}
static ParseState get_ParseState116() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522)
},
new GotoRule[] { new GotoRule( n_type, 526),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState117() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_instance, 117),
 new ShiftRule( t_explicit, 118),
 new ShiftRule( t_default, 120),
 new ShiftRule( t_vararg, 121),
 new ShiftRule( t_unmanaged, 122)
},
new GotoRule[] { new GotoRule( n_callConv, 527),
 new GotoRule( n_callKind, 119)
},
new ReduceRule( n_callKind, 0, null)
);
}
static ParseState get_ParseState118() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_instance, 117),
 new ShiftRule( t_explicit, 118),
 new ShiftRule( t_default, 120),
 new ShiftRule( t_vararg, 121),
 new ShiftRule( t_unmanaged, 122)
},
new GotoRule[] { new GotoRule( n_callConv, 528),
 new GotoRule( n_callKind, 119)
},
new ReduceRule( n_callKind, 0, null)
);
}
static ParseState get_ParseState119() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_callConv, 1, null)
);
}
static ParseState get_ParseState120() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_callKind, 1, null)
);
}
static ParseState get_ParseState121() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_callKind, 1, null)
);
}
static ParseState get_ParseState122() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_cdecl, 529),
 new ShiftRule( t_stdcall, 530),
 new ShiftRule( t_thiscall, 531),
 new ShiftRule( t_fastcall, 532)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState123() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 498),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522),
 new ShiftRule( t_field, 537)
},
new GotoRule[] { new GotoRule( n_ownerType, 533),
 new GotoRule( n_typeSpec, 534),
 new GotoRule( n_className, 497),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 500),
 new GotoRule( n_id, 52),
 new GotoRule( n_type, 501),
 new GotoRule( n_methodSpec, 535),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520),
 new GotoRule( n_memberRef, 536)
},
null);
}
static ParseState get_ParseState124() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 538)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState125() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 539)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState126() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_decl, 2, null)
);
}
static ParseState get_ParseState127() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_decl, 2, null)
);
}
static ParseState get_ParseState128() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_decl, 2, null)
);
}
static ParseState get_ParseState129() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_int64, 1, null)
);
}
static ParseState get_ParseState130() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_comma, 540)
},
new GotoRule[] {},
new ReduceRule( n_languageDecl, 2, null)
);
}
static ParseState get_ParseState131() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_brace, 541),
 new ShiftRule( t_methodDirective, 10),
 new ShiftRule( t_classDirective, 545),
 new ShiftRule( t_eventDirective, 547),
 new ShiftRule( t_propertyDirective, 549),
 new ShiftRule( t_fieldDirective, 12),
 new ShiftRule( t_dataDirective, 15),
 new ShiftRule( t_permissionDirective, 35),
 new ShiftRule( t_permissionsetDirective, 37),
 new ShiftRule( t_lineDirective, 22),
 new ShiftRule( t_P_LINE, 23),
 new ShiftRule( t_customDirective, 39),
 new ShiftRule( t_sizeDirective, 555),
 new ShiftRule( t_packDirective, 556),
 new ShiftRule( t_exportDirective, 558),
 new ShiftRule( t_overrideDirective, 559),
 new ShiftRule( t_languageDirective, 46)
},
new GotoRule[] { new GotoRule( n_classDecl, 542),
 new GotoRule( n_methodHead, 543),
 new GotoRule( n_methodHeadPart1, 9),
 new GotoRule( n_classHead, 544),
 new GotoRule( n_eventHead, 546),
 new GotoRule( n_propHead, 548),
 new GotoRule( n_fieldDecl, 550),
 new GotoRule( n_dataDecl, 551),
 new GotoRule( n_ddHead, 14),
 new GotoRule( n_secDecl, 552),
 new GotoRule( n_psetHead, 36),
 new GotoRule( n_extSourceSpec, 553),
 new GotoRule( n_customAttrDecl, 554),
 new GotoRule( n_customHead, 40),
 new GotoRule( n_customHeadWithOwner, 41),
 new GotoRule( n_exportHead, 557),
 new GotoRule( n_languageDecl, 560)
},
null);
}
static ParseState get_ParseState132() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_extends, 562)
},
new GotoRule[] { new GotoRule( n_extendsClause, 561)
},
new ReduceRule( n_extendsClause, 0, null)
);
}
static ParseState get_ParseState133() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classAttr, 2, null)
);
}
static ParseState get_ParseState134() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classAttr, 2, null)
);
}
static ParseState get_ParseState135() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classAttr, 2, null)
);
}
static ParseState get_ParseState136() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classAttr, 2, null)
);
}
static ParseState get_ParseState137() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classAttr, 2, null)
);
}
static ParseState get_ParseState138() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classAttr, 2, null)
);
}
static ParseState get_ParseState139() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classAttr, 2, null)
);
}
static ParseState get_ParseState140() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classAttr, 2, null)
);
}
static ParseState get_ParseState141() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classAttr, 2, null)
);
}
static ParseState get_ParseState142() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classAttr, 2, null)
);
}
static ParseState get_ParseState143() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classAttr, 2, null)
);
}
static ParseState get_ParseState144() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classAttr, 2, null)
);
}
static ParseState get_ParseState145() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classAttr, 2, null)
);
}
static ParseState get_ParseState146() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classAttr, 2, null)
);
}
static ParseState get_ParseState147() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classAttr, 2, null)
);
}
static ParseState get_ParseState148() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_public, 563),
 new ShiftRule( t_private, 564),
 new ShiftRule( t_family, 565),
 new ShiftRule( t_assembly, 566),
 new ShiftRule( t_famandassem, 567),
 new ShiftRule( t_famorassem, 568)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState149() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classAttr, 2, null)
);
}
static ParseState get_ParseState150() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classAttr, 2, null)
);
}
static ParseState get_ParseState151() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classAttr, 2, null)
);
}
static ParseState get_ParseState152() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_private, 570),
 new ShiftRule( t_public, 571),
 new ShiftRule( t_nested, 572)
},
new GotoRule[] { new GotoRule( n_name1, 569),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState153() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_brace, 573),
 new ShiftRule( t_classDirective, 5),
 new ShiftRule( t_namespaceDirective, 7),
 new ShiftRule( t_methodDirective, 10),
 new ShiftRule( t_fieldDirective, 12),
 new ShiftRule( t_dataDirective, 15),
 new ShiftRule( t_vtableDirective, 18),
 new ShiftRule( t_vtfixupDirective, 20),
 new ShiftRule( t_lineDirective, 22),
 new ShiftRule( t_P_LINE, 23),
 new ShiftRule( t_fileDirective, 25),
 new ShiftRule( t_assemblyDirective, 27),
 new ShiftRule( t_mresourceDirective, 31),
 new ShiftRule( t_moduleDirective, 33),
 new ShiftRule( t_permissionDirective, 35),
 new ShiftRule( t_permissionsetDirective, 37),
 new ShiftRule( t_customDirective, 39),
 new ShiftRule( t_subsystemDirective, 42),
 new ShiftRule( t_corflagsDirective, 43),
 new ShiftRule( t_imagebaseDirective, 44),
 new ShiftRule( t_languageDirective, 46)
},
new GotoRule[] { new GotoRule( n_decl, 3),
 new GotoRule( n_classHead, 4),
 new GotoRule( n_nameSpaceHead, 6),
 new GotoRule( n_methodHead, 8),
 new GotoRule( n_methodHeadPart1, 9),
 new GotoRule( n_fieldDecl, 11),
 new GotoRule( n_dataDecl, 13),
 new GotoRule( n_ddHead, 14),
 new GotoRule( n_vtableDecl, 16),
 new GotoRule( n_vtableHead, 17),
 new GotoRule( n_vtfixupDecl, 19),
 new GotoRule( n_extSourceSpec, 21),
 new GotoRule( n_fileDecl, 24),
 new GotoRule( n_assemblyHead, 26),
 new GotoRule( n_assemblyRefHead, 28),
 new GotoRule( n_comtypeHead, 29),
 new GotoRule( n_manifestResHead, 30),
 new GotoRule( n_moduleHead, 32),
 new GotoRule( n_secDecl, 34),
 new GotoRule( n_psetHead, 36),
 new GotoRule( n_customAttrDecl, 38),
 new GotoRule( n_customHead, 40),
 new GotoRule( n_customHeadWithOwner, 41),
 new GotoRule( n_languageDecl, 45)
},
null);
}
static ParseState get_ParseState154() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_name1, 574),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState155() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_decl, 3, null)
);
}
static ParseState get_ParseState156() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methodDecls, 2, null)
);
}
static ParseState get_ParseState157() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 575)
},
null);
}
static ParseState get_ParseState158() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methodDecl, 1, null)
);
}
static ParseState get_ParseState159() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_catch, 579),
 new ShiftRule( t_filter, 582),
 new ShiftRule( t_finally, 584),
 new ShiftRule( t_fault, 586)
},
new GotoRule[] { new GotoRule( n_sehClauses, 576),
 new GotoRule( n_sehClause, 577),
 new GotoRule( n_catchClause, 578),
 new GotoRule( n_filterClause, 580),
 new GotoRule( n_filterHead, 581),
 new GotoRule( n_finallyClause, 583),
 new GotoRule( n_faultClause, 585)
},
null);
}
static ParseState get_ParseState160() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_brace, 429),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_scopeBlock, 587),
 new GotoRule( n_scopeOpen, 428),
 new GotoRule( n_id, 588),
 new GotoRule( n_int32, 589)
},
null);
}
static ParseState get_ParseState161() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_tryHead, 1, null)
);
}
static ParseState get_ParseState162() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 590)
},
null);
}
static ParseState get_ParseState163() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 591),
 new ShiftRule( t_init, 592)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState164() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_localsHead, 1, null)
);
}
static ParseState get_ParseState165() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methodDecl, 1, null)
);
}
static ParseState get_ParseState166() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methodDecl, 1, null)
);
}
static ParseState get_ParseState167() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methodDecl, 1, null)
);
}
static ParseState get_ParseState168() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methodDecl, 1, null)
);
}
static ParseState get_ParseState169() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_instr, 1, null)
);
}
static ParseState get_ParseState170() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState171() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState172() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState173() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState174() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState175() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState176() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState177() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState178() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState179() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState180() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState181() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState182() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState183() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState184() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState185() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState186() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState187() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState188() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState189() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState190() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState191() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState192() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState193() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState194() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState195() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState196() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState197() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState198() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState199() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState200() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState201() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState202() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState203() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState204() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState205() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState206() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState207() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState208() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState209() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState210() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState211() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState212() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState213() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState214() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState215() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState216() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState217() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState218() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState219() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState220() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState221() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState222() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState223() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState224() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState225() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState226() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState227() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState228() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState229() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState230() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState231() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState232() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState233() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState234() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState235() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState236() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState237() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState238() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState239() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState240() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState241() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState242() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState243() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState244() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState245() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState246() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState247() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState248() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState249() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState250() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState251() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState252() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState253() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState254() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState255() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState256() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState257() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState258() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState259() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState260() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState261() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState262() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState263() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState264() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState265() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState266() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState267() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState268() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState269() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState270() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState271() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState272() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState273() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState274() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState275() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState276() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState277() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState278() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState279() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState280() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState281() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState282() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState283() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState284() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState285() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState286() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState287() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState288() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState289() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState290() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState291() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState292() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState293() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState294() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState295() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState296() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState297() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState298() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState299() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState300() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState301() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState302() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState303() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState304() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState305() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState306() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState307() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState308() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState309() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState310() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState311() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState312() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState313() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState314() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState315() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState316() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState317() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState318() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState319() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState320() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState321() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_NONE, 1, null)
);
}
static ParseState get_ParseState322() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54)
},
new GotoRule[] { new GotoRule( n_int32, 593),
 new GotoRule( n_id, 594)
},
null);
}
static ParseState get_ParseState323() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_VAR, 1, null)
);
}
static ParseState get_ParseState324() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_VAR, 1, null)
);
}
static ParseState get_ParseState325() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_VAR, 1, null)
);
}
static ParseState get_ParseState326() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_VAR, 1, null)
);
}
static ParseState get_ParseState327() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_VAR, 1, null)
);
}
static ParseState get_ParseState328() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_VAR, 1, null)
);
}
static ParseState get_ParseState329() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_VAR, 1, null)
);
}
static ParseState get_ParseState330() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_VAR, 1, null)
);
}
static ParseState get_ParseState331() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_VAR, 1, null)
);
}
static ParseState get_ParseState332() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_VAR, 1, null)
);
}
static ParseState get_ParseState333() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_VAR, 1, null)
);
}
static ParseState get_ParseState334() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_VAR, 1, null)
);
}
static ParseState get_ParseState335() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 595)
},
null);
}
static ParseState get_ParseState336() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_I, 1, null)
);
}
static ParseState get_ParseState337() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_I, 1, null)
);
}
static ParseState get_ParseState338() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_I, 1, null)
);
}
static ParseState get_ParseState339() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 129)
},
new GotoRule[] { new GotoRule( n_int64, 596)
},
null);
}
static ParseState get_ParseState340() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_I8, 1, null)
);
}
static ParseState get_ParseState341() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_float32, 598),
 new ShiftRule( t_INT64, 129),
 new ShiftRule( t_left_paren, 600)
},
new GotoRule[] { new GotoRule( n_float64, 597),
 new GotoRule( n_int64, 599)
},
null);
}
static ParseState get_ParseState342() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_R, 1, null)
);
}
static ParseState get_ParseState343() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_R, 1, null)
);
}
static ParseState get_ParseState344() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_HEXBYTE, 79)
},
new GotoRule[] { new GotoRule( n_bytes, 601),
 new GotoRule( n_hexbytes, 78)
},
new ReduceRule( n_bytes, 0, null)
);
}
static ParseState get_ParseState345() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54)
},
new GotoRule[] { new GotoRule( n_int32, 602),
 new GotoRule( n_id, 603)
},
null);
}
static ParseState get_ParseState346() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState347() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState348() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState349() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState350() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState351() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState352() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState353() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState354() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState355() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState356() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState357() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState358() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState359() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState360() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState361() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState362() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState363() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState364() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState365() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState366() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState367() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState368() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState369() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState370() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState371() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState372() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState373() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState374() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState375() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState376() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState377() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState378() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState379() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_BRTARGET, 1, null)
);
}
static ParseState get_ParseState380() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_instance, 117),
 new ShiftRule( t_explicit, 118),
 new ShiftRule( t_default, 120),
 new ShiftRule( t_vararg, 121),
 new ShiftRule( t_unmanaged, 122)
},
new GotoRule[] { new GotoRule( n_callConv, 604),
 new GotoRule( n_callKind, 119)
},
new ReduceRule( n_callKind, 0, null)
);
}
static ParseState get_ParseState381() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_METHOD, 1, null)
);
}
static ParseState get_ParseState382() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_METHOD, 1, null)
);
}
static ParseState get_ParseState383() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_METHOD, 1, null)
);
}
static ParseState get_ParseState384() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_METHOD, 1, null)
);
}
static ParseState get_ParseState385() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_METHOD, 1, null)
);
}
static ParseState get_ParseState386() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_METHOD, 1, null)
);
}
static ParseState get_ParseState387() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522)
},
new GotoRule[] { new GotoRule( n_type, 605),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState388() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_FIELD, 1, null)
);
}
static ParseState get_ParseState389() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_FIELD, 1, null)
);
}
static ParseState get_ParseState390() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_FIELD, 1, null)
);
}
static ParseState get_ParseState391() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_FIELD, 1, null)
);
}
static ParseState get_ParseState392() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_FIELD, 1, null)
);
}
static ParseState get_ParseState393() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_FIELD, 1, null)
);
}
static ParseState get_ParseState394() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 498),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522)
},
new GotoRule[] { new GotoRule( n_typeSpec, 606),
 new GotoRule( n_className, 497),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 500),
 new GotoRule( n_id, 52),
 new GotoRule( n_type, 501),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState395() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_TYPE, 1, null)
);
}
static ParseState get_ParseState396() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_TYPE, 1, null)
);
}
static ParseState get_ParseState397() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_TYPE, 1, null)
);
}
static ParseState get_ParseState398() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_TYPE, 1, null)
);
}
static ParseState get_ParseState399() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_TYPE, 1, null)
);
}
static ParseState get_ParseState400() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_TYPE, 1, null)
);
}
static ParseState get_ParseState401() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_TYPE, 1, null)
);
}
static ParseState get_ParseState402() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_TYPE, 1, null)
);
}
static ParseState get_ParseState403() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_TYPE, 1, null)
);
}
static ParseState get_ParseState404() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_TYPE, 1, null)
);
}
static ParseState get_ParseState405() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_TYPE, 1, null)
);
}
static ParseState get_ParseState406() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_TYPE, 1, null)
);
}
static ParseState get_ParseState407() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_TYPE, 1, null)
);
}
static ParseState get_ParseState408() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_QSTRING, 608),
 new ShiftRule( t_bytearray, 66)
},
new GotoRule[] { new GotoRule( n_compQstring, 607),
 new GotoRule( n_bytearrayhead, 609)
},
null);
}
static ParseState get_ParseState409() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_STRING, 1, null)
);
}
static ParseState get_ParseState410() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_instance, 117),
 new ShiftRule( t_explicit, 118),
 new ShiftRule( t_default, 120),
 new ShiftRule( t_vararg, 121),
 new ShiftRule( t_unmanaged, 122)
},
new GotoRule[] { new GotoRule( n_callConv, 610),
 new GotoRule( n_callKind, 119)
},
new ReduceRule( n_callKind, 0, null)
);
}
static ParseState get_ParseState411() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_SIG, 1, null)
);
}
static ParseState get_ParseState412() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_id, 611),
 new GotoRule( n_int32, 612)
},
null);
}
static ParseState get_ParseState413() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 498),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522),
 new ShiftRule( t_field, 537)
},
new GotoRule[] { new GotoRule( n_ownerType, 613),
 new GotoRule( n_typeSpec, 534),
 new GotoRule( n_className, 497),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 500),
 new GotoRule( n_id, 52),
 new GotoRule( n_type, 501),
 new GotoRule( n_methodSpec, 535),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520),
 new GotoRule( n_memberRef, 536)
},
null);
}
static ParseState get_ParseState414() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_instr_tok_head, 1, null)
);
}
static ParseState get_ParseState415() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_TOK, 1, null)
);
}
static ParseState get_ParseState416() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 614)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState417() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_INSTR_SWITCH, 1, null)
);
}
static ParseState get_ParseState418() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_int16s, 615)
},
new ReduceRule( n_int16s, 0, null)
);
}
static ParseState get_ParseState419() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_colon, 616)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState420() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methodDecl, 1, null)
);
}
static ParseState get_ParseState421() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methodDecl, 1, null)
);
}
static ParseState get_ParseState422() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methodDecl, 1, null)
);
}
static ParseState get_ParseState423() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methodDecl, 1, null)
);
}
static ParseState get_ParseState424() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 617)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState425() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 618)
},
null);
}
static ParseState get_ParseState426() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 498),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522)
},
new GotoRule[] { new GotoRule( n_typeSpec, 619),
 new GotoRule( n_className, 497),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 500),
 new GotoRule( n_id, 52),
 new GotoRule( n_type, 501),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState427() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methodDecl, 1, null)
);
}
static ParseState get_ParseState428() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_methodDecls, 620)
},
new ReduceRule( n_methodDecls, 0, null)
);
}
static ParseState get_ParseState429() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_scopeOpen, 1, null)
);
}
static ParseState get_ParseState430() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 621)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState431() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_paramAttr, 622)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState432() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methAttr, 2, null)
);
}
static ParseState get_ParseState433() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methAttr, 2, null)
);
}
static ParseState get_ParseState434() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methAttr, 2, null)
);
}
static ParseState get_ParseState435() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methAttr, 2, null)
);
}
static ParseState get_ParseState436() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methAttr, 2, null)
);
}
static ParseState get_ParseState437() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methAttr, 2, null)
);
}
static ParseState get_ParseState438() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methAttr, 2, null)
);
}
static ParseState get_ParseState439() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methAttr, 2, null)
);
}
static ParseState get_ParseState440() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methAttr, 2, null)
);
}
static ParseState get_ParseState441() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methAttr, 2, null)
);
}
static ParseState get_ParseState442() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methAttr, 2, null)
);
}
static ParseState get_ParseState443() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methAttr, 2, null)
);
}
static ParseState get_ParseState444() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methAttr, 2, null)
);
}
static ParseState get_ParseState445() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methAttr, 2, null)
);
}
static ParseState get_ParseState446() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methAttr, 2, null)
);
}
static ParseState get_ParseState447() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methAttr, 2, null)
);
}
static ParseState get_ParseState448() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methAttr, 2, null)
);
}
static ParseState get_ParseState449() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 623)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState450() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522),
 new ShiftRule( t_static, 625),
 new ShiftRule( t_public, 626),
 new ShiftRule( t_private, 627),
 new ShiftRule( t_family, 628),
 new ShiftRule( t_initonly, 629),
 new ShiftRule( t_rtspecialname, 630),
 new ShiftRule( t_specialname, 631),
 new ShiftRule( t_marshal, 632),
 new ShiftRule( t_assembly, 633),
 new ShiftRule( t_famandassem, 634),
 new ShiftRule( t_famorassem, 635),
 new ShiftRule( t_privatescope, 636),
 new ShiftRule( t_literal, 637),
 new ShiftRule( t_notserialized, 638)
},
new GotoRule[] { new GotoRule( n_type, 624),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState451() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_square, 639)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState452() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_brace, 640)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState453() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_comma, 641)
},
new GotoRule[] {},
new ReduceRule( n_ddItemList, 1, null)
);
}
static ParseState get_ParseState454() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 642)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState455() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54)
},
new GotoRule[] { new GotoRule( n_id, 643)
},
null);
}
static ParseState get_ParseState456() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 644)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState457() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_bytearrayhead, 2, null)
);
}
static ParseState get_ParseState458() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_float32, 598),
 new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_float64, 645),
 new GotoRule( n_int32, 646)
},
null);
}
static ParseState get_ParseState459() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_ddItem, 2, null)
);
}
static ParseState get_ParseState460() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 647)
},
null);
}
static ParseState get_ParseState461() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_float32, 598),
 new ShiftRule( t_INT64, 129)
},
new GotoRule[] { new GotoRule( n_float64, 648),
 new GotoRule( n_int64, 649)
},
null);
}
static ParseState get_ParseState462() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_ddItem, 2, null)
);
}
static ParseState get_ParseState463() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 129)
},
new GotoRule[] { new GotoRule( n_int64, 650)
},
null);
}
static ParseState get_ParseState464() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_ddItem, 2, null)
);
}
static ParseState get_ParseState465() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 651)
},
null);
}
static ParseState get_ParseState466() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_ddItem, 2, null)
);
}
static ParseState get_ParseState467() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 652)
},
null);
}
static ParseState get_ParseState468() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_ddItem, 2, null)
);
}
static ParseState get_ParseState469() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 653)
},
null);
}
static ParseState get_ParseState470() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_ddItem, 2, null)
);
}
static ParseState get_ParseState471() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_equals, 654)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState472() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_vtableDecl, 3, null)
);
}
static ParseState get_ParseState473() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_hexbytes, 2, null)
);
}
static ParseState get_ParseState474() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_vtableHead, 3, null)
);
}
static ParseState get_ParseState475() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_square, 655)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState476() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_extSourceSpec, 3, null)
);
}
static ParseState get_ParseState477() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_extSourcePosition, 656),
 new GotoRule( n_int32, 83)
},
null);
}
static ParseState get_ParseState478() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 657)
},
null);
}
static ParseState get_ParseState479() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_extSourceSpec, 3, null)
);
}
static ParseState get_ParseState480() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_entrypointDirective, 659),
 new ShiftRule( t_period, 154)
},
new GotoRule[] { new GotoRule( n_fileEntry, 658)
},
new ReduceRule( n_fileEntry, 0, null)
);
}
static ParseState get_ParseState481() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fileAttr, 2, null)
);
}
static ParseState get_ParseState482() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_decl, 3, null)
);
}
static ParseState get_ParseState483() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_brace, 660),
 new ShiftRule( t_hashDirective, 662),
 new ShiftRule( t_permissionDirective, 35),
 new ShiftRule( t_permissionsetDirective, 37),
 new ShiftRule( t_publickeyDirective, 666),
 new ShiftRule( t_verDirective, 667),
 new ShiftRule( t_localeDirective, 668),
 new ShiftRule( t_customDirective, 39)
},
new GotoRule[] { new GotoRule( n_assemblyDecl, 661),
 new GotoRule( n_secDecl, 663),
 new GotoRule( n_psetHead, 36),
 new GotoRule( n_asmOrRefDecl, 664),
 new GotoRule( n_publicKeyHead, 665),
 new GotoRule( n_localeHead, 669),
 new GotoRule( n_customAttrDecl, 670),
 new GotoRule( n_customHead, 40),
 new GotoRule( n_customHeadWithOwner, 41)
},
null);
}
static ParseState get_ParseState484() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_period, 154)
},
new GotoRule[] {},
new ReduceRule( n_assemblyHead, 3, null)
);
}
static ParseState get_ParseState485() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_asmAttr, 2, null)
);
}
static ParseState get_ParseState486() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_asmAttr, 2, null)
);
}
static ParseState get_ParseState487() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_asmAttr, 2, null)
);
}
static ParseState get_ParseState488() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_period, 154),
 new ShiftRule( t_as, 671)
},
new GotoRule[] {},
new ReduceRule( n_assemblyRefHead, 3, null)
);
}
static ParseState get_ParseState489() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_brace, 672),
 new ShiftRule( t_hashDirective, 675),
 new ShiftRule( t_publickeyDirective, 666),
 new ShiftRule( t_verDirective, 667),
 new ShiftRule( t_localeDirective, 668),
 new ShiftRule( t_customDirective, 39),
 new ShiftRule( t_publickeytokenDirective, 678)
},
new GotoRule[] { new GotoRule( n_assemblyRefDecl, 673),
 new GotoRule( n_hashHead, 674),
 new GotoRule( n_asmOrRefDecl, 676),
 new GotoRule( n_publicKeyHead, 665),
 new GotoRule( n_localeHead, 669),
 new GotoRule( n_customAttrDecl, 670),
 new GotoRule( n_customHead, 40),
 new GotoRule( n_customHeadWithOwner, 41),
 new GotoRule( n_publicKeyTokenHead, 677)
},
null);
}
static ParseState get_ParseState490() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_brace, 679),
 new ShiftRule( t_fileDirective, 681),
 new ShiftRule( t_classDirective, 682),
 new ShiftRule( t_customDirective, 39)
},
new GotoRule[] { new GotoRule( n_comtypeDecl, 680),
 new GotoRule( n_customAttrDecl, 683),
 new GotoRule( n_customHead, 40),
 new GotoRule( n_customHeadWithOwner, 41)
},
null);
}
static ParseState get_ParseState491() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_brace, 684),
 new ShiftRule( t_fileDirective, 686),
 new ShiftRule( t_assemblyDirective, 687),
 new ShiftRule( t_customDirective, 39)
},
new GotoRule[] { new GotoRule( n_manifestResDecl, 685),
 new GotoRule( n_customAttrDecl, 688),
 new GotoRule( n_customHead, 40),
 new GotoRule( n_customHeadWithOwner, 41)
},
null);
}
static ParseState get_ParseState492() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_period, 154)
},
new GotoRule[] {},
new ReduceRule( n_manifestResHead, 3, null)
);
}
static ParseState get_ParseState493() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_manresAttr, 2, null)
);
}
static ParseState get_ParseState494() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_manresAttr, 2, null)
);
}
static ParseState get_ParseState495() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_period, 154)
},
new GotoRule[] {},
new ReduceRule( n_moduleHead, 3, null)
);
}
static ParseState get_ParseState496() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 689)
},
new GotoRule[] {},
new ReduceRule( n_secDecl, 3, null)
);
}
static ParseState get_ParseState497() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_typeSpec, 1, null)
);
}
static ParseState get_ParseState498() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_moduleDirective, 691)
},
new GotoRule[] { new GotoRule( n_name1, 690),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState499() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_slash, 692)
},
new GotoRule[] {},
new ReduceRule( n_className, 1, null)
);
}
static ParseState get_ParseState500() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_period, 154)
},
new GotoRule[] {},
new ReduceRule( n_slashedName, 1, null)
);
}
static ParseState get_ParseState501() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 693),
 new ShiftRule( t_ampersand, 694),
 new ShiftRule( t_asterisk, 695),
 new ShiftRule( t_pinned, 696),
 new ShiftRule( t_modreq, 697),
 new ShiftRule( t_modopt, 698)
},
new GotoRule[] {},
new ReduceRule( n_typeSpec, 1, null)
);
}
static ParseState get_ParseState502() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 700),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_className, 699),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 500),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState503() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 1, null)
);
}
static ParseState get_ParseState504() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 1, null)
);
}
static ParseState get_ParseState505() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_class, 701)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState506() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 700),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_className, 702),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 500),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState507() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 703)
},
null);
}
static ParseState get_ParseState508() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_instance, 117),
 new ShiftRule( t_explicit, 118),
 new ShiftRule( t_default, 120),
 new ShiftRule( t_vararg, 121),
 new ShiftRule( t_unmanaged, 122)
},
new GotoRule[] { new GotoRule( n_callConv, 704),
 new GotoRule( n_callKind, 119)
},
new ReduceRule( n_callKind, 0, null)
);
}
static ParseState get_ParseState509() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methodSpec, 1, null)
);
}
static ParseState get_ParseState510() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 1, null)
);
}
static ParseState get_ParseState511() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 1, null)
);
}
static ParseState get_ParseState512() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 1, null)
);
}
static ParseState get_ParseState513() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 1, null)
);
}
static ParseState get_ParseState514() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 1, null)
);
}
static ParseState get_ParseState515() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 1, null)
);
}
static ParseState get_ParseState516() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 1, null)
);
}
static ParseState get_ParseState517() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_int32, 1, null)
);
}
static ParseState get_ParseState518() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 1, null)
);
}
static ParseState get_ParseState519() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 705)
},
new GotoRule[] {},
new ReduceRule( n_type, 1, null)
);
}
static ParseState get_ParseState520() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 706)
},
new GotoRule[] {},
new ReduceRule( n_type, 1, null)
);
}
static ParseState get_ParseState521() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_int8, 707),
 new ShiftRule( t_int16, 708),
 new ShiftRule( t_INT64, 517)
},
new GotoRule[] { new GotoRule( n_int32, 709),
 new GotoRule( n_int64, 710)
},
null);
}
static ParseState get_ParseState522() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_int, 711),
 new ShiftRule( t_unsigned, 712),
 new ShiftRule( t_float, 713)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState523() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_secDecl, 3, null)
);
}
static ParseState get_ParseState524() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 714)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState525() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_QSTRING, 608),
 new ShiftRule( t_left_paren, 716)
},
new GotoRule[] { new GotoRule( n_compQstring, 715)
},
null);
}
static ParseState get_ParseState526() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 718),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522),
 new ShiftRule( t_ampersand, 694),
 new ShiftRule( t_asterisk, 695),
 new ShiftRule( t_pinned, 696),
 new ShiftRule( t_modreq, 697),
 new ShiftRule( t_modopt, 698),
 new ShiftRule( t_ctorDirective, 719)
},
new GotoRule[] { new GotoRule( n_typeSpec, 717),
 new GotoRule( n_className, 497),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 500),
 new GotoRule( n_id, 52),
 new GotoRule( n_type, 501),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState527() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_callConv, 2, null)
);
}
static ParseState get_ParseState528() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_callConv, 2, null)
);
}
static ParseState get_ParseState529() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_callKind, 2, null)
);
}
static ParseState get_ParseState530() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_callKind, 2, null)
);
}
static ParseState get_ParseState531() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_callKind, 2, null)
);
}
static ParseState get_ParseState532() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_callKind, 2, null)
);
}
static ParseState get_ParseState533() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 720)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState534() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_ownerType, 1, null)
);
}
static ParseState get_ParseState535() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_instance, 117),
 new ShiftRule( t_explicit, 118),
 new ShiftRule( t_default, 120),
 new ShiftRule( t_vararg, 121),
 new ShiftRule( t_unmanaged, 122)
},
new GotoRule[] { new GotoRule( n_callConv, 721),
 new GotoRule( n_callKind, 119)
},
new ReduceRule( n_callKind, 0, null)
);
}
static ParseState get_ParseState536() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_ownerType, 1, null)
);
}
static ParseState get_ParseState537() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522)
},
new GotoRule[] { new GotoRule( n_type, 722),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState538() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_customAttrDecl, 3, null)
);
}
static ParseState get_ParseState539() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_customAttrDecl, 3, null)
);
}
static ParseState get_ParseState540() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_SQSTRING, 723)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState541() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_decl, 4, null)
);
}
static ParseState get_ParseState542() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classDecls, 2, null)
);
}
static ParseState get_ParseState543() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_methodDecls, 724)
},
new ReduceRule( n_methodDecls, 0, null)
);
}
static ParseState get_ParseState544() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_brace, 725)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState545() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_classAttr, 48)
},
new ReduceRule( n_classAttr, 0, null)
);
}
static ParseState get_ParseState546() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_brace, 726)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState547() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_eventAttr, 727)
},
new ReduceRule( n_eventAttr, 0, null)
);
}
static ParseState get_ParseState548() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_brace, 728)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState549() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_propAttr, 729)
},
new ReduceRule( n_propAttr, 0, null)
);
}
static ParseState get_ParseState550() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classDecl, 1, null)
);
}
static ParseState get_ParseState551() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classDecl, 1, null)
);
}
static ParseState get_ParseState552() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classDecl, 1, null)
);
}
static ParseState get_ParseState553() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classDecl, 1, null)
);
}
static ParseState get_ParseState554() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classDecl, 1, null)
);
}
static ParseState get_ParseState555() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 730)
},
null);
}
static ParseState get_ParseState556() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 731)
},
null);
}
static ParseState get_ParseState557() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_brace, 732)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState558() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_comtAttr, 733)
},
new ReduceRule( n_comtAttr, 0, null)
);
}
static ParseState get_ParseState559() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 498),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522)
},
new GotoRule[] { new GotoRule( n_typeSpec, 734),
 new GotoRule( n_className, 497),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 500),
 new GotoRule( n_id, 52),
 new GotoRule( n_type, 501),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState560() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classDecl, 1, null)
);
}
static ParseState get_ParseState561() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_implements, 736)
},
new GotoRule[] { new GotoRule( n_implClause, 735)
},
new ReduceRule( n_implClause, 0, null)
);
}
static ParseState get_ParseState562() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 700),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_className, 737),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 500),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState563() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classAttr, 3, null)
);
}
static ParseState get_ParseState564() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classAttr, 3, null)
);
}
static ParseState get_ParseState565() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classAttr, 3, null)
);
}
static ParseState get_ParseState566() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classAttr, 3, null)
);
}
static ParseState get_ParseState567() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classAttr, 3, null)
);
}
static ParseState get_ParseState568() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classAttr, 3, null)
);
}
static ParseState get_ParseState569() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_period, 154)
},
new GotoRule[] {},
new ReduceRule( n_comtypeHead, 4, null)
);
}
static ParseState get_ParseState570() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_comtAttr, 2, null)
);
}
static ParseState get_ParseState571() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_comtAttr, 2, null)
);
}
static ParseState get_ParseState572() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_public, 738),
 new ShiftRule( t_private, 739),
 new ShiftRule( t_family, 740),
 new ShiftRule( t_assembly, 741),
 new ShiftRule( t_famandassem, 742),
 new ShiftRule( t_famorassem, 743)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState573() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_decl, 4, null)
);
}
static ParseState get_ParseState574() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_period, 154)
},
new GotoRule[] {},
new ReduceRule( n_name1, 3, null)
);
}
static ParseState get_ParseState575() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methodDecl, 2, null)
);
}
static ParseState get_ParseState576() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_sehBlock, 2, null)
);
}
static ParseState get_ParseState577() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_catch, 579),
 new ShiftRule( t_filter, 582),
 new ShiftRule( t_finally, 584),
 new ShiftRule( t_fault, 586)
},
new GotoRule[] { new GotoRule( n_sehClauses, 744),
 new GotoRule( n_sehClause, 577),
 new GotoRule( n_catchClause, 578),
 new GotoRule( n_filterClause, 580),
 new GotoRule( n_filterHead, 581),
 new GotoRule( n_finallyClause, 583),
 new GotoRule( n_faultClause, 585)
},
new ReduceRule( n_sehClauses, 1, null)
);
}
static ParseState get_ParseState578() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_brace, 429),
 new ShiftRule( t_handler, 747)
},
new GotoRule[] { new GotoRule( n_handlerBlock, 745),
 new GotoRule( n_scopeBlock, 746),
 new GotoRule( n_scopeOpen, 428)
},
null);
}
static ParseState get_ParseState579() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 700),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_className, 748),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 500),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState580() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_brace, 429),
 new ShiftRule( t_handler, 747)
},
new GotoRule[] { new GotoRule( n_handlerBlock, 749),
 new GotoRule( n_scopeBlock, 746),
 new GotoRule( n_scopeOpen, 428)
},
null);
}
static ParseState get_ParseState581() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_brace, 429),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_scopeBlock, 750),
 new GotoRule( n_scopeOpen, 428),
 new GotoRule( n_id, 751),
 new GotoRule( n_int32, 752)
},
null);
}
static ParseState get_ParseState582() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_filterHead, 1, null)
);
}
static ParseState get_ParseState583() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_brace, 429),
 new ShiftRule( t_handler, 747)
},
new GotoRule[] { new GotoRule( n_handlerBlock, 753),
 new GotoRule( n_scopeBlock, 746),
 new GotoRule( n_scopeOpen, 428)
},
null);
}
static ParseState get_ParseState584() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_finallyClause, 1, null)
);
}
static ParseState get_ParseState585() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_brace, 429),
 new ShiftRule( t_handler, 747)
},
new GotoRule[] { new GotoRule( n_handlerBlock, 754),
 new GotoRule( n_scopeBlock, 746),
 new GotoRule( n_scopeOpen, 428)
},
null);
}
static ParseState get_ParseState586() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_faultClause, 1, null)
);
}
static ParseState get_ParseState587() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_tryBlock, 2, null)
);
}
static ParseState get_ParseState588() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_to, 755)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState589() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_to, 756)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState590() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methodDecl, 2, null)
);
}
static ParseState get_ParseState591() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 757),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState592() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 762)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState593() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_instr, 2, null)
);
}
static ParseState get_ParseState594() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_instr, 2, null)
);
}
static ParseState get_ParseState595() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_instr, 2, null)
);
}
static ParseState get_ParseState596() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_instr, 2, null)
);
}
static ParseState get_ParseState597() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 706)
},
new GotoRule[] {},
new ReduceRule( n_instr, 2, null)
);
}
static ParseState get_ParseState598() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 705)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState599() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_instr, 2, null)
);
}
static ParseState get_ParseState600() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_instr_r_head, 2, null)
);
}
static ParseState get_ParseState601() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 763)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState602() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_instr, 2, null)
);
}
static ParseState get_ParseState603() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_instr, 2, null)
);
}
static ParseState get_ParseState604() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522)
},
new GotoRule[] { new GotoRule( n_type, 764),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState605() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 718),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522),
 new ShiftRule( t_ampersand, 694),
 new ShiftRule( t_asterisk, 695),
 new ShiftRule( t_pinned, 696),
 new ShiftRule( t_modreq, 697),
 new ShiftRule( t_modopt, 698)
},
new GotoRule[] { new GotoRule( n_typeSpec, 765),
 new GotoRule( n_className, 497),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 500),
 new GotoRule( n_id, 766),
 new GotoRule( n_type, 501),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState606() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_instr, 2, null)
);
}
static ParseState get_ParseState607() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_plus, 767)
},
new GotoRule[] {},
new ReduceRule( n_instr, 2, null)
);
}
static ParseState get_ParseState608() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_compQstring, 1, null)
);
}
static ParseState get_ParseState609() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_HEXBYTE, 79)
},
new GotoRule[] { new GotoRule( n_bytes, 768),
 new GotoRule( n_hexbytes, 78)
},
new ReduceRule( n_bytes, 0, null)
);
}
static ParseState get_ParseState610() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522)
},
new GotoRule[] { new GotoRule( n_type, 769),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState611() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_instr, 2, null)
);
}
static ParseState get_ParseState612() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_instr, 2, null)
);
}
static ParseState get_ParseState613() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_instr, 2, null)
);
}
static ParseState get_ParseState614() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_labels, 770),
 new GotoRule( n_id, 771),
 new GotoRule( n_int32, 772)
},
new ReduceRule( n_labels, 0, null)
);
}
static ParseState get_ParseState615() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 773)
},
new ReduceRule( n_instr, 2, null)
);
}
static ParseState get_ParseState616() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methodDecl, 2, null)
);
}
static ParseState get_ParseState617() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 774)
},
null);
}
static ParseState get_ParseState618() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_colon, 775)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState619() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_member_op, 776)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState620() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_brace, 777),
 new ShiftRule( t_emitbyteDirective, 157),
 new ShiftRule( t_tryDirective, 161),
 new ShiftRule( t_maxstackDirective, 162),
 new ShiftRule( t_localsDirective, 164),
 new ShiftRule( t_entrypointDirective, 165),
 new ShiftRule( t_zeroinitDirective, 166),
 new ShiftRule( t_dataDirective, 15),
 new ShiftRule( t_nop, 170),
 new ShiftRule( t_break, 171),
 new ShiftRule( t_ldarg_0, 172),
 new ShiftRule( t_ldarg_1, 173),
 new ShiftRule( t_ldarg_2, 174),
 new ShiftRule( t_ldarg_3, 175),
 new ShiftRule( t_ldloc_0, 176),
 new ShiftRule( t_ldloc_1, 177),
 new ShiftRule( t_ldloc_2, 178),
 new ShiftRule( t_ldloc_3, 179),
 new ShiftRule( t_stloc_0, 180),
 new ShiftRule( t_stloc_1, 181),
 new ShiftRule( t_stloc_2, 182),
 new ShiftRule( t_stloc_3, 183),
 new ShiftRule( t_ldnull, 184),
 new ShiftRule( t_ldc_i4_m1, 185),
 new ShiftRule( t_ldc_i4_0, 186),
 new ShiftRule( t_ldc_i4_1, 187),
 new ShiftRule( t_ldc_i4_2, 188),
 new ShiftRule( t_ldc_i4_3, 189),
 new ShiftRule( t_ldc_i4_4, 190),
 new ShiftRule( t_ldc_i4_5, 191),
 new ShiftRule( t_ldc_i4_6, 192),
 new ShiftRule( t_ldc_i4_7, 193),
 new ShiftRule( t_ldc_i4_8, 194),
 new ShiftRule( t_dup, 195),
 new ShiftRule( t_pop, 196),
 new ShiftRule( t_ret, 197),
 new ShiftRule( t_ldind_i1, 198),
 new ShiftRule( t_ldind_u1, 199),
 new ShiftRule( t_ldind_i2, 200),
 new ShiftRule( t_ldind_u2, 201),
 new ShiftRule( t_ldind_i4, 202),
 new ShiftRule( t_ldind_u4, 203),
 new ShiftRule( t_ldind_i8, 204),
 new ShiftRule( t_ldind_i, 205),
 new ShiftRule( t_ldind_r4, 206),
 new ShiftRule( t_ldind_r8, 207),
 new ShiftRule( t_ldind_ref, 208),
 new ShiftRule( t_stind_ref, 209),
 new ShiftRule( t_stind_i1, 210),
 new ShiftRule( t_stind_i2, 211),
 new ShiftRule( t_stind_i4, 212),
 new ShiftRule( t_stind_i8, 213),
 new ShiftRule( t_stind_r4, 214),
 new ShiftRule( t_stind_r8, 215),
 new ShiftRule( t_add, 216),
 new ShiftRule( t_sub, 217),
 new ShiftRule( t_mul, 218),
 new ShiftRule( t_div, 219),
 new ShiftRule( t_div_un, 220),
 new ShiftRule( t_rem, 221),
 new ShiftRule( t_rem_un, 222),
 new ShiftRule( t_and, 223),
 new ShiftRule( t_or, 224),
 new ShiftRule( t_xor, 225),
 new ShiftRule( t_shl, 226),
 new ShiftRule( t_shr, 227),
 new ShiftRule( t_shr_un, 228),
 new ShiftRule( t_neg, 229),
 new ShiftRule( t_not, 230),
 new ShiftRule( t_conv_i1, 231),
 new ShiftRule( t_conv_i2, 232),
 new ShiftRule( t_conv_i4, 233),
 new ShiftRule( t_conv_i8, 234),
 new ShiftRule( t_conv_r4, 235),
 new ShiftRule( t_conv_r8, 236),
 new ShiftRule( t_conv_u4, 237),
 new ShiftRule( t_conv_u8, 238),
 new ShiftRule( t_conv_r_un, 239),
 new ShiftRule( t_throw, 240),
 new ShiftRule( t_conv_ovf_i1_un, 241),
 new ShiftRule( t_conv_ovf_i2_un, 242),
 new ShiftRule( t_conv_ovf_i4_un, 243),
 new ShiftRule( t_conv_ovf_i8_un, 244),
 new ShiftRule( t_conv_ovf_u1_un, 245),
 new ShiftRule( t_conv_ovf_u2_un, 246),
 new ShiftRule( t_conv_ovf_u4_un, 247),
 new ShiftRule( t_conv_ovf_u8_un, 248),
 new ShiftRule( t_conv_ovf_i_un, 249),
 new ShiftRule( t_conv_ovf_u_un, 250),
 new ShiftRule( t_ldlen, 251),
 new ShiftRule( t_ldelem_i1, 252),
 new ShiftRule( t_ldelem_u1, 253),
 new ShiftRule( t_ldelem_i2, 254),
 new ShiftRule( t_ldelem_u2, 255),
 new ShiftRule( t_ldelem_i4, 256),
 new ShiftRule( t_ldelem_u4, 257),
 new ShiftRule( t_ldelem_i8, 258),
 new ShiftRule( t_ldelem_i, 259),
 new ShiftRule( t_ldelem_r4, 260),
 new ShiftRule( t_ldelem_r8, 261),
 new ShiftRule( t_ldelem_ref, 262),
 new ShiftRule( t_stelem_i, 263),
 new ShiftRule( t_stelem_i1, 264),
 new ShiftRule( t_stelem_i2, 265),
 new ShiftRule( t_stelem_i4, 266),
 new ShiftRule( t_stelem_i8, 267),
 new ShiftRule( t_stelem_r4, 268),
 new ShiftRule( t_stelem_r8, 269),
 new ShiftRule( t_stelem_ref, 270),
 new ShiftRule( t_conv_ovf_i1, 271),
 new ShiftRule( t_conv_ovf_u1, 272),
 new ShiftRule( t_conv_ovf_i2, 273),
 new ShiftRule( t_conv_ovf_u2, 274),
 new ShiftRule( t_conv_ovf_i4, 275),
 new ShiftRule( t_conv_ovf_u4, 276),
 new ShiftRule( t_conv_ovf_i8, 277),
 new ShiftRule( t_conv_ovf_u8, 278),
 new ShiftRule( t_ckfinite, 279),
 new ShiftRule( t_conv_u2, 280),
 new ShiftRule( t_conv_u1, 281),
 new ShiftRule( t_conv_i, 282),
 new ShiftRule( t_conv_ovf_i, 283),
 new ShiftRule( t_conv_ovf_u, 284),
 new ShiftRule( t_add_ovf, 285),
 new ShiftRule( t_add_ovf_un, 286),
 new ShiftRule( t_mul_ovf, 287),
 new ShiftRule( t_mul_ovf_un, 288),
 new ShiftRule( t_sub_ovf, 289),
 new ShiftRule( t_sub_ovf_un, 290),
 new ShiftRule( t_endfinally, 291),
 new ShiftRule( t_stind_i, 292),
 new ShiftRule( t_conv_u, 293),
 new ShiftRule( t_prefix7, 294),
 new ShiftRule( t_prefix6, 295),
 new ShiftRule( t_prefix5, 296),
 new ShiftRule( t_prefix4, 297),
 new ShiftRule( t_prefix3, 298),
 new ShiftRule( t_prefix2, 299),
 new ShiftRule( t_prefix1, 300),
 new ShiftRule( t_prefixref, 301),
 new ShiftRule( t_arglist, 302),
 new ShiftRule( t_ceq, 303),
 new ShiftRule( t_cgt, 304),
 new ShiftRule( t_cgt_un, 305),
 new ShiftRule( t_clt, 306),
 new ShiftRule( t_clt_un, 307),
 new ShiftRule( t_localloc, 308),
 new ShiftRule( t_endfilter, 309),
 new ShiftRule( t_volatile_, 310),
 new ShiftRule( t_tail_, 311),
 new ShiftRule( t_cpblk, 312),
 new ShiftRule( t_initblk, 313),
 new ShiftRule( t_rethrow, 314),
 new ShiftRule( t_refanytype, 315),
 new ShiftRule( t_illegal, 316),
 new ShiftRule( t_endmac, 317),
 new ShiftRule( t_ldind_u8, 318),
 new ShiftRule( t_ldelem_u8, 319),
 new ShiftRule( t_ldc_i4_M1, 320),
 new ShiftRule( t_endfault, 321),
 new ShiftRule( t_ldarg_s, 323),
 new ShiftRule( t_ldarga_s, 324),
 new ShiftRule( t_starg_s, 325),
 new ShiftRule( t_ldloc_s, 326),
 new ShiftRule( t_ldloca_s, 327),
 new ShiftRule( t_stloc_s, 328),
 new ShiftRule( t_ldarg, 329),
 new ShiftRule( t_ldarga, 330),
 new ShiftRule( t_starg, 331),
 new ShiftRule( t_ldloc, 332),
 new ShiftRule( t_ldloca, 333),
 new ShiftRule( t_stloc, 334),
 new ShiftRule( t_ldc_i4_s, 336),
 new ShiftRule( t_ldc_i4, 337),
 new ShiftRule( t_unaligned_, 338),
 new ShiftRule( t_ldc_i8, 340),
 new ShiftRule( t_ldc_r4, 342),
 new ShiftRule( t_ldc_r8, 343),
 new ShiftRule( t_br_s, 346),
 new ShiftRule( t_brfalse_s, 347),
 new ShiftRule( t_brtrue_s, 348),
 new ShiftRule( t_beq_s, 349),
 new ShiftRule( t_bge_s, 350),
 new ShiftRule( t_bgt_s, 351),
 new ShiftRule( t_ble_s, 352),
 new ShiftRule( t_blt_s, 353),
 new ShiftRule( t_bne_un_s, 354),
 new ShiftRule( t_bge_un_s, 355),
 new ShiftRule( t_bgt_un_s, 356),
 new ShiftRule( t_ble_un_s, 357),
 new ShiftRule( t_blt_un_s, 358),
 new ShiftRule( t_br, 359),
 new ShiftRule( t_brfalse, 360),
 new ShiftRule( t_brtrue, 361),
 new ShiftRule( t_beq, 362),
 new ShiftRule( t_bge, 363),
 new ShiftRule( t_bgt, 364),
 new ShiftRule( t_ble, 365),
 new ShiftRule( t_blt, 366),
 new ShiftRule( t_bne_un, 367),
 new ShiftRule( t_bge_un, 368),
 new ShiftRule( t_bgt_un, 369),
 new ShiftRule( t_ble_un, 370),
 new ShiftRule( t_blt_un, 371),
 new ShiftRule( t_leave, 372),
 new ShiftRule( t_leave_s, 373),
 new ShiftRule( t_brnull, 374),
 new ShiftRule( t_brnull_s, 375),
 new ShiftRule( t_brzero, 376),
 new ShiftRule( t_brzero_s, 377),
 new ShiftRule( t_brinst, 378),
 new ShiftRule( t_brinst_s, 379),
 new ShiftRule( t_jmp, 381),
 new ShiftRule( t_call, 382),
 new ShiftRule( t_callvirt, 383),
 new ShiftRule( t_newobj, 384),
 new ShiftRule( t_ldftn, 385),
 new ShiftRule( t_ldvirtftn, 386),
 new ShiftRule( t_ldfld, 388),
 new ShiftRule( t_ldflda, 389),
 new ShiftRule( t_stfld, 390),
 new ShiftRule( t_ldsfld, 391),
 new ShiftRule( t_ldsflda, 392),
 new ShiftRule( t_stsfld, 393),
 new ShiftRule( t_cpobj, 395),
 new ShiftRule( t_ldobj, 396),
 new ShiftRule( t_castclass, 397),
 new ShiftRule( t_isinst, 398),
 new ShiftRule( t_unbox, 399),
 new ShiftRule( t_stobj, 400),
 new ShiftRule( t_box, 401),
 new ShiftRule( t_newarr, 402),
 new ShiftRule( t_ldelema, 403),
 new ShiftRule( t_refanyval, 404),
 new ShiftRule( t_mkrefany, 405),
 new ShiftRule( t_initobj, 406),
 new ShiftRule( t_sizeof, 407),
 new ShiftRule( t_ldstr, 409),
 new ShiftRule( t_calli, 411),
 new ShiftRule( t_INSTR_RVA, 412),
 new ShiftRule( t_ldtoken, 415),
 new ShiftRule( t_switch, 417),
 new ShiftRule( t_INSTR_PHI, 418),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_permissionDirective, 35),
 new ShiftRule( t_permissionsetDirective, 37),
 new ShiftRule( t_lineDirective, 22),
 new ShiftRule( t_P_LINE, 23),
 new ShiftRule( t_languageDirective, 46),
 new ShiftRule( t_customDirective, 39),
 new ShiftRule( t_exportDirective, 424),
 new ShiftRule( t_vtentryDirective, 425),
 new ShiftRule( t_overrideDirective, 426),
 new ShiftRule( t_left_brace, 429),
 new ShiftRule( t_paramDirective, 430)
},
new GotoRule[] { new GotoRule( n_methodDecl, 156),
 new GotoRule( n_sehBlock, 158),
 new GotoRule( n_tryBlock, 159),
 new GotoRule( n_tryHead, 160),
 new GotoRule( n_localsHead, 163),
 new GotoRule( n_dataDecl, 167),
 new GotoRule( n_ddHead, 14),
 new GotoRule( n_instr, 168),
 new GotoRule( n_INSTR_NONE, 169),
 new GotoRule( n_INSTR_VAR, 322),
 new GotoRule( n_INSTR_I, 335),
 new GotoRule( n_INSTR_I8, 339),
 new GotoRule( n_INSTR_R, 341),
 new GotoRule( n_instr_r_head, 344),
 new GotoRule( n_INSTR_BRTARGET, 345),
 new GotoRule( n_INSTR_METHOD, 380),
 new GotoRule( n_INSTR_FIELD, 387),
 new GotoRule( n_INSTR_TYPE, 394),
 new GotoRule( n_INSTR_STRING, 408),
 new GotoRule( n_INSTR_SIG, 410),
 new GotoRule( n_instr_tok_head, 413),
 new GotoRule( n_INSTR_TOK, 414),
 new GotoRule( n_INSTR_SWITCH, 416),
 new GotoRule( n_id, 419),
 new GotoRule( n_secDecl, 420),
 new GotoRule( n_psetHead, 36),
 new GotoRule( n_extSourceSpec, 421),
 new GotoRule( n_languageDecl, 422),
 new GotoRule( n_customAttrDecl, 423),
 new GotoRule( n_customHead, 40),
 new GotoRule( n_customHeadWithOwner, 41),
 new GotoRule( n_scopeBlock, 427),
 new GotoRule( n_scopeOpen, 428)
},
null);
}
static ParseState get_ParseState621() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 778)
},
null);
}
static ParseState get_ParseState622() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522),
 new ShiftRule( t_left_square, 780)
},
new GotoRule[] { new GotoRule( n_type, 779),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState623() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_QSTRING, 608)
},
new GotoRule[] { new GotoRule( n_compQstring, 781),
 new GotoRule( n_pinvAttr, 782)
},
new ReduceRule( n_pinvAttr, 0, null)
);
}
static ParseState get_ParseState624() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_left_square, 693),
 new ShiftRule( t_ampersand, 694),
 new ShiftRule( t_asterisk, 695),
 new ShiftRule( t_pinned, 696),
 new ShiftRule( t_modreq, 697),
 new ShiftRule( t_modopt, 698)
},
new GotoRule[] { new GotoRule( n_id, 783)
},
null);
}
static ParseState get_ParseState625() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldAttr, 2, null)
);
}
static ParseState get_ParseState626() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldAttr, 2, null)
);
}
static ParseState get_ParseState627() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldAttr, 2, null)
);
}
static ParseState get_ParseState628() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldAttr, 2, null)
);
}
static ParseState get_ParseState629() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldAttr, 2, null)
);
}
static ParseState get_ParseState630() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldAttr, 2, null)
);
}
static ParseState get_ParseState631() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldAttr, 2, null)
);
}
static ParseState get_ParseState632() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 784)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState633() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldAttr, 2, null)
);
}
static ParseState get_ParseState634() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldAttr, 2, null)
);
}
static ParseState get_ParseState635() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldAttr, 2, null)
);
}
static ParseState get_ParseState636() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldAttr, 2, null)
);
}
static ParseState get_ParseState637() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldAttr, 2, null)
);
}
static ParseState get_ParseState638() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldAttr, 2, null)
);
}
static ParseState get_ParseState639() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_repeatOpt, 3, null)
);
}
static ParseState get_ParseState640() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_ddBody, 3, null)
);
}
static ParseState get_ParseState641() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_char, 63),
 new ShiftRule( t_ampersand, 64),
 new ShiftRule( t_bytearray, 66),
 new ShiftRule( t_float32, 67),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_INT64, 71),
 new ShiftRule( t_int16, 73),
 new ShiftRule( t_int8, 74)
},
new GotoRule[] { new GotoRule( n_ddItemList, 785),
 new GotoRule( n_ddItem, 453),
 new GotoRule( n_bytearrayhead, 65),
 new GotoRule( n_float64, 68),
 new GotoRule( n_int64, 70),
 new GotoRule( n_int32, 72)
},
null);
}
static ParseState get_ParseState642() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_QSTRING, 608)
},
new GotoRule[] { new GotoRule( n_compQstring, 786)
},
null);
}
static ParseState get_ParseState643() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 787)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState644() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_ddItem, 3, null)
);
}
static ParseState get_ParseState645() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 788),
 new ShiftRule( t_left_paren, 706)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState646() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 789)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState647() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_square, 790)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState648() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 791),
 new ShiftRule( t_left_paren, 706)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState649() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 792)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState650() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 793)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState651() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 794)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState652() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 795)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState653() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 796)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState654() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_ddHead, 4, null)
);
}
static ParseState get_ParseState655() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_vtfixupAttr, 797)
},
new ReduceRule( n_vtfixupAttr, 0, null)
);
}
static ParseState get_ParseState656() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_SQSTRING, 798)
},
new GotoRule[] {},
new ReduceRule( n_extSourceSpec, 4, null)
);
}
static ParseState get_ParseState657() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_extSourcePosition, 3, null)
);
}
static ParseState get_ParseState658() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_hashDirective, 675)
},
new GotoRule[] { new GotoRule( n_hashHead, 799)
},
new ReduceRule( n_fileDecl, 4, null)
);
}
static ParseState get_ParseState659() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fileEntry, 1, null)
);
}
static ParseState get_ParseState660() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_decl, 4, null)
);
}
static ParseState get_ParseState661() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_assemblyDecls, 2, null)
);
}
static ParseState get_ParseState662() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_algorithm, 800)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState663() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_assemblyDecl, 1, null)
);
}
static ParseState get_ParseState664() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_assemblyDecl, 1, null)
);
}
static ParseState get_ParseState665() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_HEXBYTE, 79)
},
new GotoRule[] { new GotoRule( n_bytes, 801),
 new GotoRule( n_hexbytes, 78)
},
new ReduceRule( n_bytes, 0, null)
);
}
static ParseState get_ParseState666() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_equals, 802)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState667() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 803)
},
null);
}
static ParseState get_ParseState668() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_QSTRING, 608),
 new ShiftRule( t_equals, 805)
},
new GotoRule[] { new GotoRule( n_compQstring, 804)
},
null);
}
static ParseState get_ParseState669() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_HEXBYTE, 79)
},
new GotoRule[] { new GotoRule( n_bytes, 806),
 new GotoRule( n_hexbytes, 78)
},
new ReduceRule( n_bytes, 0, null)
);
}
static ParseState get_ParseState670() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_asmOrRefDecl, 1, null)
);
}
static ParseState get_ParseState671() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_name1, 807),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState672() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_decl, 4, null)
);
}
static ParseState get_ParseState673() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_assemblyRefDecls, 2, null)
);
}
static ParseState get_ParseState674() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_HEXBYTE, 79)
},
new GotoRule[] { new GotoRule( n_bytes, 808),
 new GotoRule( n_hexbytes, 78)
},
new ReduceRule( n_bytes, 0, null)
);
}
static ParseState get_ParseState675() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_equals, 809)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState676() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_assemblyRefDecl, 1, null)
);
}
static ParseState get_ParseState677() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_HEXBYTE, 79)
},
new GotoRule[] { new GotoRule( n_bytes, 810),
 new GotoRule( n_hexbytes, 78)
},
new ReduceRule( n_bytes, 0, null)
);
}
static ParseState get_ParseState678() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_equals, 811)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState679() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_decl, 4, null)
);
}
static ParseState get_ParseState680() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_comtypeDecls, 2, null)
);
}
static ParseState get_ParseState681() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_name1, 812),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState682() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_extern, 813),
 new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 814)
},
null);
}
static ParseState get_ParseState683() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_comtypeDecl, 1, null)
);
}
static ParseState get_ParseState684() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_decl, 4, null)
);
}
static ParseState get_ParseState685() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_manifestResDecls, 2, null)
);
}
static ParseState get_ParseState686() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_name1, 815),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState687() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_extern, 816)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState688() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_manifestResDecl, 1, null)
);
}
static ParseState get_ParseState689() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_QSTRING, 608)
},
new GotoRule[] { new GotoRule( n_nameValPairs, 817),
 new GotoRule( n_nameValPair, 818),
 new GotoRule( n_compQstring, 819)
},
null);
}
static ParseState get_ParseState690() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_square, 820),
 new ShiftRule( t_period, 154)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState691() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_name1, 821),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState692() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_name1, 822),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState693() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_square, 823),
 new ShiftRule( t_ellipsis, 826),
 new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_bounds1, 824),
 new GotoRule( n_bound, 825),
 new GotoRule( n_int32, 827)
},
new ReduceRule( n_bound, 0, null)
);
}
static ParseState get_ParseState694() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 2, null)
);
}
static ParseState get_ParseState695() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 2, null)
);
}
static ParseState get_ParseState696() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 2, null)
);
}
static ParseState get_ParseState697() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 828)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState698() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 829)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState699() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 2, null)
);
}
static ParseState get_ParseState700() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_moduleDirective, 831)
},
new GotoRule[] { new GotoRule( n_name1, 830),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState701() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 700),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_className, 832),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 500),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState702() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 2, null)
);
}
static ParseState get_ParseState703() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 2, null)
);
}
static ParseState get_ParseState704() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522)
},
new GotoRule[] { new GotoRule( n_type, 833),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState705() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 646)
},
null);
}
static ParseState get_ParseState706() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 129)
},
new GotoRule[] { new GotoRule( n_int64, 649)
},
null);
}
static ParseState get_ParseState707() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 2, null)
);
}
static ParseState get_ParseState708() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 2, null)
);
}
static ParseState get_ParseState709() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 2, null)
);
}
static ParseState get_ParseState710() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 2, null)
);
}
static ParseState get_ParseState711() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 2, null)
);
}
static ParseState get_ParseState712() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_int, 834)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState713() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 2, null)
);
}
static ParseState get_ParseState714() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_psetHead, 4, null)
);
}
static ParseState get_ParseState715() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_plus, 767)
},
new GotoRule[] {},
new ReduceRule( n_customAttrDecl, 4, null)
);
}
static ParseState get_ParseState716() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_customHead, 4, null)
);
}
static ParseState get_ParseState717() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_member_op, 835)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState718() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_moduleDirective, 691),
 new ShiftRule( t_right_square, 823),
 new ShiftRule( t_ellipsis, 826),
 new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_name1, 690),
 new GotoRule( n_id, 52),
 new GotoRule( n_bounds1, 824),
 new GotoRule( n_bound, 825),
 new GotoRule( n_int32, 827)
},
new ReduceRule( n_bound, 0, null)
);
}
static ParseState get_ParseState719() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 836)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState720() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_instance, 117),
 new ShiftRule( t_explicit, 118),
 new ShiftRule( t_default, 120),
 new ShiftRule( t_vararg, 121),
 new ShiftRule( t_unmanaged, 122)
},
new GotoRule[] { new GotoRule( n_customType, 837),
 new GotoRule( n_callConv, 116),
 new GotoRule( n_callKind, 119)
},
new ReduceRule( n_callKind, 0, null)
);
}
static ParseState get_ParseState721() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522)
},
new GotoRule[] { new GotoRule( n_type, 838),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState722() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 718),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522),
 new ShiftRule( t_ampersand, 694),
 new ShiftRule( t_asterisk, 695),
 new ShiftRule( t_pinned, 696),
 new ShiftRule( t_modreq, 697),
 new ShiftRule( t_modopt, 698)
},
new GotoRule[] { new GotoRule( n_typeSpec, 839),
 new GotoRule( n_className, 497),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 500),
 new GotoRule( n_id, 840),
 new GotoRule( n_type, 501),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState723() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_comma, 841)
},
new GotoRule[] {},
new ReduceRule( n_languageDecl, 4, null)
);
}
static ParseState get_ParseState724() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_brace, 842),
 new ShiftRule( t_emitbyteDirective, 157),
 new ShiftRule( t_tryDirective, 161),
 new ShiftRule( t_maxstackDirective, 162),
 new ShiftRule( t_localsDirective, 164),
 new ShiftRule( t_entrypointDirective, 165),
 new ShiftRule( t_zeroinitDirective, 166),
 new ShiftRule( t_dataDirective, 15),
 new ShiftRule( t_nop, 170),
 new ShiftRule( t_break, 171),
 new ShiftRule( t_ldarg_0, 172),
 new ShiftRule( t_ldarg_1, 173),
 new ShiftRule( t_ldarg_2, 174),
 new ShiftRule( t_ldarg_3, 175),
 new ShiftRule( t_ldloc_0, 176),
 new ShiftRule( t_ldloc_1, 177),
 new ShiftRule( t_ldloc_2, 178),
 new ShiftRule( t_ldloc_3, 179),
 new ShiftRule( t_stloc_0, 180),
 new ShiftRule( t_stloc_1, 181),
 new ShiftRule( t_stloc_2, 182),
 new ShiftRule( t_stloc_3, 183),
 new ShiftRule( t_ldnull, 184),
 new ShiftRule( t_ldc_i4_m1, 185),
 new ShiftRule( t_ldc_i4_0, 186),
 new ShiftRule( t_ldc_i4_1, 187),
 new ShiftRule( t_ldc_i4_2, 188),
 new ShiftRule( t_ldc_i4_3, 189),
 new ShiftRule( t_ldc_i4_4, 190),
 new ShiftRule( t_ldc_i4_5, 191),
 new ShiftRule( t_ldc_i4_6, 192),
 new ShiftRule( t_ldc_i4_7, 193),
 new ShiftRule( t_ldc_i4_8, 194),
 new ShiftRule( t_dup, 195),
 new ShiftRule( t_pop, 196),
 new ShiftRule( t_ret, 197),
 new ShiftRule( t_ldind_i1, 198),
 new ShiftRule( t_ldind_u1, 199),
 new ShiftRule( t_ldind_i2, 200),
 new ShiftRule( t_ldind_u2, 201),
 new ShiftRule( t_ldind_i4, 202),
 new ShiftRule( t_ldind_u4, 203),
 new ShiftRule( t_ldind_i8, 204),
 new ShiftRule( t_ldind_i, 205),
 new ShiftRule( t_ldind_r4, 206),
 new ShiftRule( t_ldind_r8, 207),
 new ShiftRule( t_ldind_ref, 208),
 new ShiftRule( t_stind_ref, 209),
 new ShiftRule( t_stind_i1, 210),
 new ShiftRule( t_stind_i2, 211),
 new ShiftRule( t_stind_i4, 212),
 new ShiftRule( t_stind_i8, 213),
 new ShiftRule( t_stind_r4, 214),
 new ShiftRule( t_stind_r8, 215),
 new ShiftRule( t_add, 216),
 new ShiftRule( t_sub, 217),
 new ShiftRule( t_mul, 218),
 new ShiftRule( t_div, 219),
 new ShiftRule( t_div_un, 220),
 new ShiftRule( t_rem, 221),
 new ShiftRule( t_rem_un, 222),
 new ShiftRule( t_and, 223),
 new ShiftRule( t_or, 224),
 new ShiftRule( t_xor, 225),
 new ShiftRule( t_shl, 226),
 new ShiftRule( t_shr, 227),
 new ShiftRule( t_shr_un, 228),
 new ShiftRule( t_neg, 229),
 new ShiftRule( t_not, 230),
 new ShiftRule( t_conv_i1, 231),
 new ShiftRule( t_conv_i2, 232),
 new ShiftRule( t_conv_i4, 233),
 new ShiftRule( t_conv_i8, 234),
 new ShiftRule( t_conv_r4, 235),
 new ShiftRule( t_conv_r8, 236),
 new ShiftRule( t_conv_u4, 237),
 new ShiftRule( t_conv_u8, 238),
 new ShiftRule( t_conv_r_un, 239),
 new ShiftRule( t_throw, 240),
 new ShiftRule( t_conv_ovf_i1_un, 241),
 new ShiftRule( t_conv_ovf_i2_un, 242),
 new ShiftRule( t_conv_ovf_i4_un, 243),
 new ShiftRule( t_conv_ovf_i8_un, 244),
 new ShiftRule( t_conv_ovf_u1_un, 245),
 new ShiftRule( t_conv_ovf_u2_un, 246),
 new ShiftRule( t_conv_ovf_u4_un, 247),
 new ShiftRule( t_conv_ovf_u8_un, 248),
 new ShiftRule( t_conv_ovf_i_un, 249),
 new ShiftRule( t_conv_ovf_u_un, 250),
 new ShiftRule( t_ldlen, 251),
 new ShiftRule( t_ldelem_i1, 252),
 new ShiftRule( t_ldelem_u1, 253),
 new ShiftRule( t_ldelem_i2, 254),
 new ShiftRule( t_ldelem_u2, 255),
 new ShiftRule( t_ldelem_i4, 256),
 new ShiftRule( t_ldelem_u4, 257),
 new ShiftRule( t_ldelem_i8, 258),
 new ShiftRule( t_ldelem_i, 259),
 new ShiftRule( t_ldelem_r4, 260),
 new ShiftRule( t_ldelem_r8, 261),
 new ShiftRule( t_ldelem_ref, 262),
 new ShiftRule( t_stelem_i, 263),
 new ShiftRule( t_stelem_i1, 264),
 new ShiftRule( t_stelem_i2, 265),
 new ShiftRule( t_stelem_i4, 266),
 new ShiftRule( t_stelem_i8, 267),
 new ShiftRule( t_stelem_r4, 268),
 new ShiftRule( t_stelem_r8, 269),
 new ShiftRule( t_stelem_ref, 270),
 new ShiftRule( t_conv_ovf_i1, 271),
 new ShiftRule( t_conv_ovf_u1, 272),
 new ShiftRule( t_conv_ovf_i2, 273),
 new ShiftRule( t_conv_ovf_u2, 274),
 new ShiftRule( t_conv_ovf_i4, 275),
 new ShiftRule( t_conv_ovf_u4, 276),
 new ShiftRule( t_conv_ovf_i8, 277),
 new ShiftRule( t_conv_ovf_u8, 278),
 new ShiftRule( t_ckfinite, 279),
 new ShiftRule( t_conv_u2, 280),
 new ShiftRule( t_conv_u1, 281),
 new ShiftRule( t_conv_i, 282),
 new ShiftRule( t_conv_ovf_i, 283),
 new ShiftRule( t_conv_ovf_u, 284),
 new ShiftRule( t_add_ovf, 285),
 new ShiftRule( t_add_ovf_un, 286),
 new ShiftRule( t_mul_ovf, 287),
 new ShiftRule( t_mul_ovf_un, 288),
 new ShiftRule( t_sub_ovf, 289),
 new ShiftRule( t_sub_ovf_un, 290),
 new ShiftRule( t_endfinally, 291),
 new ShiftRule( t_stind_i, 292),
 new ShiftRule( t_conv_u, 293),
 new ShiftRule( t_prefix7, 294),
 new ShiftRule( t_prefix6, 295),
 new ShiftRule( t_prefix5, 296),
 new ShiftRule( t_prefix4, 297),
 new ShiftRule( t_prefix3, 298),
 new ShiftRule( t_prefix2, 299),
 new ShiftRule( t_prefix1, 300),
 new ShiftRule( t_prefixref, 301),
 new ShiftRule( t_arglist, 302),
 new ShiftRule( t_ceq, 303),
 new ShiftRule( t_cgt, 304),
 new ShiftRule( t_cgt_un, 305),
 new ShiftRule( t_clt, 306),
 new ShiftRule( t_clt_un, 307),
 new ShiftRule( t_localloc, 308),
 new ShiftRule( t_endfilter, 309),
 new ShiftRule( t_volatile_, 310),
 new ShiftRule( t_tail_, 311),
 new ShiftRule( t_cpblk, 312),
 new ShiftRule( t_initblk, 313),
 new ShiftRule( t_rethrow, 314),
 new ShiftRule( t_refanytype, 315),
 new ShiftRule( t_illegal, 316),
 new ShiftRule( t_endmac, 317),
 new ShiftRule( t_ldind_u8, 318),
 new ShiftRule( t_ldelem_u8, 319),
 new ShiftRule( t_ldc_i4_M1, 320),
 new ShiftRule( t_endfault, 321),
 new ShiftRule( t_ldarg_s, 323),
 new ShiftRule( t_ldarga_s, 324),
 new ShiftRule( t_starg_s, 325),
 new ShiftRule( t_ldloc_s, 326),
 new ShiftRule( t_ldloca_s, 327),
 new ShiftRule( t_stloc_s, 328),
 new ShiftRule( t_ldarg, 329),
 new ShiftRule( t_ldarga, 330),
 new ShiftRule( t_starg, 331),
 new ShiftRule( t_ldloc, 332),
 new ShiftRule( t_ldloca, 333),
 new ShiftRule( t_stloc, 334),
 new ShiftRule( t_ldc_i4_s, 336),
 new ShiftRule( t_ldc_i4, 337),
 new ShiftRule( t_unaligned_, 338),
 new ShiftRule( t_ldc_i8, 340),
 new ShiftRule( t_ldc_r4, 342),
 new ShiftRule( t_ldc_r8, 343),
 new ShiftRule( t_br_s, 346),
 new ShiftRule( t_brfalse_s, 347),
 new ShiftRule( t_brtrue_s, 348),
 new ShiftRule( t_beq_s, 349),
 new ShiftRule( t_bge_s, 350),
 new ShiftRule( t_bgt_s, 351),
 new ShiftRule( t_ble_s, 352),
 new ShiftRule( t_blt_s, 353),
 new ShiftRule( t_bne_un_s, 354),
 new ShiftRule( t_bge_un_s, 355),
 new ShiftRule( t_bgt_un_s, 356),
 new ShiftRule( t_ble_un_s, 357),
 new ShiftRule( t_blt_un_s, 358),
 new ShiftRule( t_br, 359),
 new ShiftRule( t_brfalse, 360),
 new ShiftRule( t_brtrue, 361),
 new ShiftRule( t_beq, 362),
 new ShiftRule( t_bge, 363),
 new ShiftRule( t_bgt, 364),
 new ShiftRule( t_ble, 365),
 new ShiftRule( t_blt, 366),
 new ShiftRule( t_bne_un, 367),
 new ShiftRule( t_bge_un, 368),
 new ShiftRule( t_bgt_un, 369),
 new ShiftRule( t_ble_un, 370),
 new ShiftRule( t_blt_un, 371),
 new ShiftRule( t_leave, 372),
 new ShiftRule( t_leave_s, 373),
 new ShiftRule( t_brnull, 374),
 new ShiftRule( t_brnull_s, 375),
 new ShiftRule( t_brzero, 376),
 new ShiftRule( t_brzero_s, 377),
 new ShiftRule( t_brinst, 378),
 new ShiftRule( t_brinst_s, 379),
 new ShiftRule( t_jmp, 381),
 new ShiftRule( t_call, 382),
 new ShiftRule( t_callvirt, 383),
 new ShiftRule( t_newobj, 384),
 new ShiftRule( t_ldftn, 385),
 new ShiftRule( t_ldvirtftn, 386),
 new ShiftRule( t_ldfld, 388),
 new ShiftRule( t_ldflda, 389),
 new ShiftRule( t_stfld, 390),
 new ShiftRule( t_ldsfld, 391),
 new ShiftRule( t_ldsflda, 392),
 new ShiftRule( t_stsfld, 393),
 new ShiftRule( t_cpobj, 395),
 new ShiftRule( t_ldobj, 396),
 new ShiftRule( t_castclass, 397),
 new ShiftRule( t_isinst, 398),
 new ShiftRule( t_unbox, 399),
 new ShiftRule( t_stobj, 400),
 new ShiftRule( t_box, 401),
 new ShiftRule( t_newarr, 402),
 new ShiftRule( t_ldelema, 403),
 new ShiftRule( t_refanyval, 404),
 new ShiftRule( t_mkrefany, 405),
 new ShiftRule( t_initobj, 406),
 new ShiftRule( t_sizeof, 407),
 new ShiftRule( t_ldstr, 409),
 new ShiftRule( t_calli, 411),
 new ShiftRule( t_INSTR_RVA, 412),
 new ShiftRule( t_ldtoken, 415),
 new ShiftRule( t_switch, 417),
 new ShiftRule( t_INSTR_PHI, 418),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_permissionDirective, 35),
 new ShiftRule( t_permissionsetDirective, 37),
 new ShiftRule( t_lineDirective, 22),
 new ShiftRule( t_P_LINE, 23),
 new ShiftRule( t_languageDirective, 46),
 new ShiftRule( t_customDirective, 39),
 new ShiftRule( t_exportDirective, 424),
 new ShiftRule( t_vtentryDirective, 425),
 new ShiftRule( t_overrideDirective, 426),
 new ShiftRule( t_left_brace, 429),
 new ShiftRule( t_paramDirective, 430)
},
new GotoRule[] { new GotoRule( n_methodDecl, 156),
 new GotoRule( n_sehBlock, 158),
 new GotoRule( n_tryBlock, 159),
 new GotoRule( n_tryHead, 160),
 new GotoRule( n_localsHead, 163),
 new GotoRule( n_dataDecl, 167),
 new GotoRule( n_ddHead, 14),
 new GotoRule( n_instr, 168),
 new GotoRule( n_INSTR_NONE, 169),
 new GotoRule( n_INSTR_VAR, 322),
 new GotoRule( n_INSTR_I, 335),
 new GotoRule( n_INSTR_I8, 339),
 new GotoRule( n_INSTR_R, 341),
 new GotoRule( n_instr_r_head, 344),
 new GotoRule( n_INSTR_BRTARGET, 345),
 new GotoRule( n_INSTR_METHOD, 380),
 new GotoRule( n_INSTR_FIELD, 387),
 new GotoRule( n_INSTR_TYPE, 394),
 new GotoRule( n_INSTR_STRING, 408),
 new GotoRule( n_INSTR_SIG, 410),
 new GotoRule( n_instr_tok_head, 413),
 new GotoRule( n_INSTR_TOK, 414),
 new GotoRule( n_INSTR_SWITCH, 416),
 new GotoRule( n_id, 419),
 new GotoRule( n_secDecl, 420),
 new GotoRule( n_psetHead, 36),
 new GotoRule( n_extSourceSpec, 421),
 new GotoRule( n_languageDecl, 422),
 new GotoRule( n_customAttrDecl, 423),
 new GotoRule( n_customHead, 40),
 new GotoRule( n_customHeadWithOwner, 41),
 new GotoRule( n_scopeBlock, 427),
 new GotoRule( n_scopeOpen, 428)
},
null);
}
static ParseState get_ParseState725() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_classDecls, 843)
},
new ReduceRule( n_classDecls, 0, null)
);
}
static ParseState get_ParseState726() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_eventDecls, 844)
},
new ReduceRule( n_eventDecls, 0, null)
);
}
static ParseState get_ParseState727() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 498),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522),
 new ShiftRule( t_rtspecialname, 847),
 new ShiftRule( t_specialname, 848)
},
new GotoRule[] { new GotoRule( n_typeSpec, 845),
 new GotoRule( n_className, 497),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 500),
 new GotoRule( n_id, 846),
 new GotoRule( n_type, 501),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState728() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_propDecls, 849)
},
new ReduceRule( n_propDecls, 0, null)
);
}
static ParseState get_ParseState729() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_instance, 117),
 new ShiftRule( t_explicit, 118),
 new ShiftRule( t_default, 120),
 new ShiftRule( t_vararg, 121),
 new ShiftRule( t_unmanaged, 122),
 new ShiftRule( t_rtspecialname, 851),
 new ShiftRule( t_specialname, 852)
},
new GotoRule[] { new GotoRule( n_callConv, 850),
 new GotoRule( n_callKind, 119)
},
new ReduceRule( n_callKind, 0, null)
);
}
static ParseState get_ParseState730() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classDecl, 2, null)
);
}
static ParseState get_ParseState731() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classDecl, 2, null)
);
}
static ParseState get_ParseState732() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_comtypeDecls, 853)
},
new ReduceRule( n_comtypeDecls, 0, null)
);
}
static ParseState get_ParseState733() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_private, 570),
 new ShiftRule( t_public, 571),
 new ShiftRule( t_nested, 572)
},
new GotoRule[] { new GotoRule( n_name1, 854),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState734() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_member_op, 855)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState735() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classHead, 5, null)
);
}
static ParseState get_ParseState736() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 700),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_classNames, 856),
 new GotoRule( n_className, 857),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 500),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState737() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_extendsClause, 2, null)
);
}
static ParseState get_ParseState738() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_comtAttr, 3, null)
);
}
static ParseState get_ParseState739() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_comtAttr, 3, null)
);
}
static ParseState get_ParseState740() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_comtAttr, 3, null)
);
}
static ParseState get_ParseState741() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_comtAttr, 3, null)
);
}
static ParseState get_ParseState742() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_comtAttr, 3, null)
);
}
static ParseState get_ParseState743() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_comtAttr, 3, null)
);
}
static ParseState get_ParseState744() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_sehClauses, 2, null)
);
}
static ParseState get_ParseState745() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_sehClause, 2, null)
);
}
static ParseState get_ParseState746() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_handlerBlock, 1, null)
);
}
static ParseState get_ParseState747() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_id, 858),
 new GotoRule( n_int32, 859)
},
null);
}
static ParseState get_ParseState748() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_catchClause, 2, null)
);
}
static ParseState get_ParseState749() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_sehClause, 2, null)
);
}
static ParseState get_ParseState750() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_filterClause, 2, null)
);
}
static ParseState get_ParseState751() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_filterClause, 2, null)
);
}
static ParseState get_ParseState752() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_filterClause, 2, null)
);
}
static ParseState get_ParseState753() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_sehClause, 2, null)
);
}
static ParseState get_ParseState754() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_sehClause, 2, null)
);
}
static ParseState get_ParseState755() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54)
},
new GotoRule[] { new GotoRule( n_id, 860)
},
null);
}
static ParseState get_ParseState756() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 861)
},
null);
}
static ParseState get_ParseState757() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 862)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState758() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_comma, 863)
},
new GotoRule[] {},
new ReduceRule( n_sigArgs0, 1, null)
);
}
static ParseState get_ParseState759() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_sigArgs1, 1, null)
);
}
static ParseState get_ParseState760() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_sigArg, 1, null)
);
}
static ParseState get_ParseState761() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522),
 new ShiftRule( t_left_square, 780)
},
new GotoRule[] { new GotoRule( n_type, 864),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState762() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 865),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState763() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_instr, 3, null)
);
}
static ParseState get_ParseState764() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 718),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522),
 new ShiftRule( t_ampersand, 694),
 new ShiftRule( t_asterisk, 695),
 new ShiftRule( t_pinned, 696),
 new ShiftRule( t_modreq, 697),
 new ShiftRule( t_modopt, 698),
 new ShiftRule( t_ctorDirective, 869),
 new ShiftRule( t_cctorDirective, 870)
},
new GotoRule[] { new GotoRule( n_typeSpec, 866),
 new GotoRule( n_className, 497),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 867),
 new GotoRule( n_id, 52),
 new GotoRule( n_type, 501),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520),
 new GotoRule( n_methodName, 868)
},
null);
}
static ParseState get_ParseState765() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_member_op, 871)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState766() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_instr, 3, null)
);
}
static ParseState get_ParseState767() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_QSTRING, 872)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState768() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 873)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState769() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 874),
 new ShiftRule( t_left_square, 693),
 new ShiftRule( t_ampersand, 694),
 new ShiftRule( t_asterisk, 695),
 new ShiftRule( t_pinned, 696),
 new ShiftRule( t_modreq, 697),
 new ShiftRule( t_modopt, 698)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState770() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 875)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState771() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_comma, 876)
},
new GotoRule[] {},
new ReduceRule( n_labels, 1, null)
);
}
static ParseState get_ParseState772() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_comma, 877)
},
new GotoRule[] {},
new ReduceRule( n_labels, 1, null)
);
}
static ParseState get_ParseState773() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_int16s, 2, null)
);
}
static ParseState get_ParseState774() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_square, 878)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState775() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 879)
},
null);
}
static ParseState get_ParseState776() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ctorDirective, 869),
 new ShiftRule( t_cctorDirective, 870),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_methodName, 880),
 new GotoRule( n_name1, 881),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState777() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_scopeBlock, 3, null)
);
}
static ParseState get_ParseState778() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_square, 882)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState779() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ctorDirective, 869),
 new ShiftRule( t_cctorDirective, 870),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_left_square, 693),
 new ShiftRule( t_ampersand, 694),
 new ShiftRule( t_asterisk, 695),
 new ShiftRule( t_pinned, 696),
 new ShiftRule( t_modreq, 697),
 new ShiftRule( t_modopt, 698),
 new ShiftRule( t_marshal, 884)
},
new GotoRule[] { new GotoRule( n_methodName, 883),
 new GotoRule( n_name1, 881),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState780() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_in, 885),
 new ShiftRule( t_out, 886),
 new ShiftRule( t_opt, 887),
 new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 888)
},
null);
}
static ParseState get_ParseState781() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_as, 889),
 new ShiftRule( t_plus, 767)
},
new GotoRule[] { new GotoRule( n_pinvAttr, 890)
},
new ReduceRule( n_pinvAttr, 0, null)
);
}
static ParseState get_ParseState782() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 891),
 new ShiftRule( t_nomangle, 892),
 new ShiftRule( t_ansi, 893),
 new ShiftRule( t_unicode, 894),
 new ShiftRule( t_autochar, 895),
 new ShiftRule( t_lasterr, 896),
 new ShiftRule( t_winapi, 897),
 new ShiftRule( t_cdecl, 898),
 new ShiftRule( t_stdcall, 899),
 new ShiftRule( t_thiscall, 900),
 new ShiftRule( t_fastcall, 901)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState783() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_at, 903)
},
new GotoRule[] { new GotoRule( n_atOpt, 902)
},
new ReduceRule( n_atOpt, 0, null)
);
}
static ParseState get_ParseState784() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_custom, 905),
 new ShiftRule( t_fixed, 906),
 new ShiftRule( t_variant, 907),
 new ShiftRule( t_currency, 908),
 new ShiftRule( t_syschar, 909),
 new ShiftRule( t_void, 910),
 new ShiftRule( t_bool, 911),
 new ShiftRule( t_int8, 912),
 new ShiftRule( t_int16, 913),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 916),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_error, 918),
 new ShiftRule( t_unsigned, 919),
 new ShiftRule( t_decimal, 920),
 new ShiftRule( t_date, 921),
 new ShiftRule( t_bstr, 922),
 new ShiftRule( t_lpstr, 923),
 new ShiftRule( t_lpwstr, 924),
 new ShiftRule( t_lptstr, 925),
 new ShiftRule( t_objectref, 926),
 new ShiftRule( t_iunknown, 927),
 new ShiftRule( t_idispatch, 928),
 new ShiftRule( t_struct, 929),
 new ShiftRule( t_interface, 930),
 new ShiftRule( t_safearray, 931),
 new ShiftRule( t_int, 932),
 new ShiftRule( t_nested, 933),
 new ShiftRule( t_byvalstr, 934),
 new ShiftRule( t_ansi, 935),
 new ShiftRule( t_tbstr, 936),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_as, 938),
 new ShiftRule( t_lpstruct, 939)
},
new GotoRule[] { new GotoRule( n_nativeType, 904),
 new GotoRule( n_int32, 914),
 new GotoRule( n_int64, 915),
 new GotoRule( n_float64, 917),
 new GotoRule( n_methodSpec, 937)
},
new ReduceRule( n_nativeType, 0, null)
);
}
static ParseState get_ParseState785() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_ddItemList, 3, null)
);
}
static ParseState get_ParseState786() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 940),
 new ShiftRule( t_plus, 767)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState787() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_ddItem, 4, null)
);
}
static ParseState get_ParseState788() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 460)
},
new GotoRule[] { new GotoRule( n_ddItemCount, 941)
},
new ReduceRule( n_ddItemCount, 0, null)
);
}
static ParseState get_ParseState789() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_float64, 4, null)
);
}
static ParseState get_ParseState790() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_ddItemCount, 3, null)
);
}
static ParseState get_ParseState791() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 460)
},
new GotoRule[] { new GotoRule( n_ddItemCount, 942)
},
new ReduceRule( n_ddItemCount, 0, null)
);
}
static ParseState get_ParseState792() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_float64, 4, null)
);
}
static ParseState get_ParseState793() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 460)
},
new GotoRule[] { new GotoRule( n_ddItemCount, 943)
},
new ReduceRule( n_ddItemCount, 0, null)
);
}
static ParseState get_ParseState794() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 460)
},
new GotoRule[] { new GotoRule( n_ddItemCount, 944)
},
new ReduceRule( n_ddItemCount, 0, null)
);
}
static ParseState get_ParseState795() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 460)
},
new GotoRule[] { new GotoRule( n_ddItemCount, 945)
},
new ReduceRule( n_ddItemCount, 0, null)
);
}
static ParseState get_ParseState796() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 460)
},
new GotoRule[] { new GotoRule( n_ddItemCount, 946)
},
new ReduceRule( n_ddItemCount, 0, null)
);
}
static ParseState get_ParseState797() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_at, 947),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_fromunmanaged, 950),
 new ShiftRule( t_callmostderived, 951)
},
new GotoRule[] { new GotoRule( n_int32, 948),
 new GotoRule( n_int64, 949)
},
null);
}
static ParseState get_ParseState798() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_extSourceSpec, 5, null)
);
}
static ParseState get_ParseState799() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_HEXBYTE, 79)
},
new GotoRule[] { new GotoRule( n_bytes, 952),
 new GotoRule( n_hexbytes, 78)
},
new ReduceRule( n_bytes, 0, null)
);
}
static ParseState get_ParseState800() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 953)
},
null);
}
static ParseState get_ParseState801() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 954)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState802() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 955)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState803() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_colon, 956)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState804() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_plus, 767)
},
new GotoRule[] {},
new ReduceRule( n_asmOrRefDecl, 2, null)
);
}
static ParseState get_ParseState805() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 957)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState806() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 958)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState807() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_period, 154)
},
new GotoRule[] {},
new ReduceRule( n_assemblyRefHead, 5, null)
);
}
static ParseState get_ParseState808() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 959)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState809() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 960)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState810() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 961)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState811() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 962)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState812() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_period, 154)
},
new GotoRule[] {},
new ReduceRule( n_comtypeDecl, 2, null)
);
}
static ParseState get_ParseState813() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_name1, 963),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState814() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_comtypeDecl, 2, null)
);
}
static ParseState get_ParseState815() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_at, 964),
 new ShiftRule( t_period, 154)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState816() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_name1, 965),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState817() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 966)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState818() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_comma, 967)
},
new GotoRule[] {},
new ReduceRule( n_nameValPairs, 1, null)
);
}
static ParseState get_ParseState819() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_equals, 968),
 new ShiftRule( t_plus, 767)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState820() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_slashedName, 969),
 new GotoRule( n_name1, 500),
 new GotoRule( n_id, 52)
},
new ReduceRule( n_typeSpec, 3, null)
);
}
static ParseState get_ParseState821() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_square, 970),
 new ShiftRule( t_period, 154)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState822() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_period, 154)
},
new GotoRule[] {},
new ReduceRule( n_slashedName, 3, null)
);
}
static ParseState get_ParseState823() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 3, null)
);
}
static ParseState get_ParseState824() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_square, 971),
 new ShiftRule( t_comma, 972)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState825() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_bounds1, 1, null)
);
}
static ParseState get_ParseState826() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_bound, 1, null)
);
}
static ParseState get_ParseState827() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 973)
},
new GotoRule[] {},
new ReduceRule( n_bound, 1, null)
);
}
static ParseState get_ParseState828() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 700),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_className, 974),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 500),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState829() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 700),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_className, 975),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 500),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState830() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_square, 976),
 new ShiftRule( t_period, 154)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState831() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_name1, 977),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState832() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 3, null)
);
}
static ParseState get_ParseState833() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_asterisk, 978),
 new ShiftRule( t_left_square, 693),
 new ShiftRule( t_ampersand, 694),
 new ShiftRule( t_pinned, 696),
 new ShiftRule( t_modreq, 697),
 new ShiftRule( t_modopt, 698)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState834() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 3, null)
);
}
static ParseState get_ParseState835() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ctorDirective, 979)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState836() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 980),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState837() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_equals, 981)
},
new GotoRule[] {},
new ReduceRule( n_customAttrDecl, 5, null)
);
}
static ParseState get_ParseState838() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_asterisk, 978),
 new ShiftRule( t_left_square, 982),
 new ShiftRule( t_ampersand, 694),
 new ShiftRule( t_pinned, 696),
 new ShiftRule( t_modreq, 697),
 new ShiftRule( t_modopt, 698),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522),
 new ShiftRule( t_ctorDirective, 869),
 new ShiftRule( t_cctorDirective, 870)
},
new GotoRule[] { new GotoRule( n_typeSpec, 983),
 new GotoRule( n_className, 497),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 867),
 new GotoRule( n_id, 52),
 new GotoRule( n_type, 501),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520),
 new GotoRule( n_methodName, 984)
},
null);
}
static ParseState get_ParseState839() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_member_op, 985)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState840() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_memberRef, 3, null)
);
}
static ParseState get_ParseState841() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_SQSTRING, 986)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState842() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classDecl, 3, null)
);
}
static ParseState get_ParseState843() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_brace, 987),
 new ShiftRule( t_methodDirective, 10),
 new ShiftRule( t_classDirective, 545),
 new ShiftRule( t_eventDirective, 547),
 new ShiftRule( t_propertyDirective, 549),
 new ShiftRule( t_fieldDirective, 12),
 new ShiftRule( t_dataDirective, 15),
 new ShiftRule( t_permissionDirective, 35),
 new ShiftRule( t_permissionsetDirective, 37),
 new ShiftRule( t_lineDirective, 22),
 new ShiftRule( t_P_LINE, 23),
 new ShiftRule( t_customDirective, 39),
 new ShiftRule( t_sizeDirective, 555),
 new ShiftRule( t_packDirective, 556),
 new ShiftRule( t_exportDirective, 558),
 new ShiftRule( t_overrideDirective, 559),
 new ShiftRule( t_languageDirective, 46)
},
new GotoRule[] { new GotoRule( n_classDecl, 542),
 new GotoRule( n_methodHead, 543),
 new GotoRule( n_methodHeadPart1, 9),
 new GotoRule( n_classHead, 544),
 new GotoRule( n_eventHead, 546),
 new GotoRule( n_propHead, 548),
 new GotoRule( n_fieldDecl, 550),
 new GotoRule( n_dataDecl, 551),
 new GotoRule( n_ddHead, 14),
 new GotoRule( n_secDecl, 552),
 new GotoRule( n_psetHead, 36),
 new GotoRule( n_extSourceSpec, 553),
 new GotoRule( n_customAttrDecl, 554),
 new GotoRule( n_customHead, 40),
 new GotoRule( n_customHeadWithOwner, 41),
 new GotoRule( n_exportHead, 557),
 new GotoRule( n_languageDecl, 560)
},
null);
}
static ParseState get_ParseState844() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_brace, 988),
 new ShiftRule( t_addonDirective, 990),
 new ShiftRule( t_removeonDirective, 991),
 new ShiftRule( t_fireDirective, 992),
 new ShiftRule( t_otherDirective, 993),
 new ShiftRule( t_lineDirective, 22),
 new ShiftRule( t_P_LINE, 23),
 new ShiftRule( t_customDirective, 39),
 new ShiftRule( t_languageDirective, 46)
},
new GotoRule[] { new GotoRule( n_eventDecl, 989),
 new GotoRule( n_extSourceSpec, 994),
 new GotoRule( n_customAttrDecl, 995),
 new GotoRule( n_customHead, 40),
 new GotoRule( n_customHeadWithOwner, 41),
 new GotoRule( n_languageDecl, 996)
},
null);
}
static ParseState get_ParseState845() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54)
},
new GotoRule[] { new GotoRule( n_id, 997)
},
null);
}
static ParseState get_ParseState846() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_eventHead, 3, null)
);
}
static ParseState get_ParseState847() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_eventAttr, 2, null)
);
}
static ParseState get_ParseState848() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_eventAttr, 2, null)
);
}
static ParseState get_ParseState849() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_brace, 998),
 new ShiftRule( t_setDirective, 1000),
 new ShiftRule( t_getDirective, 1001),
 new ShiftRule( t_otherDirective, 1002),
 new ShiftRule( t_customDirective, 39),
 new ShiftRule( t_lineDirective, 22),
 new ShiftRule( t_P_LINE, 23),
 new ShiftRule( t_languageDirective, 46)
},
new GotoRule[] { new GotoRule( n_propDecl, 999),
 new GotoRule( n_customAttrDecl, 1003),
 new GotoRule( n_customHead, 40),
 new GotoRule( n_customHeadWithOwner, 41),
 new GotoRule( n_extSourceSpec, 1004),
 new GotoRule( n_languageDecl, 1005)
},
null);
}
static ParseState get_ParseState850() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522)
},
new GotoRule[] { new GotoRule( n_type, 1006),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState851() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_propAttr, 2, null)
);
}
static ParseState get_ParseState852() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_propAttr, 2, null)
);
}
static ParseState get_ParseState853() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_brace, 1007),
 new ShiftRule( t_fileDirective, 681),
 new ShiftRule( t_classDirective, 682),
 new ShiftRule( t_customDirective, 39)
},
new GotoRule[] { new GotoRule( n_comtypeDecl, 680),
 new GotoRule( n_customAttrDecl, 683),
 new GotoRule( n_customHead, 40),
 new GotoRule( n_customHeadWithOwner, 41)
},
null);
}
static ParseState get_ParseState854() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_period, 154)
},
new GotoRule[] {},
new ReduceRule( n_exportHead, 3, null)
);
}
static ParseState get_ParseState855() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ctorDirective, 869),
 new ShiftRule( t_cctorDirective, 870),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_methodName, 1008),
 new GotoRule( n_name1, 881),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState856() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_comma, 1009)
},
new GotoRule[] {},
new ReduceRule( n_implClause, 2, null)
);
}
static ParseState get_ParseState857() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classNames, 1, null)
);
}
static ParseState get_ParseState858() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_to, 1010)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState859() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_to, 1011)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState860() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_tryBlock, 4, null)
);
}
static ParseState get_ParseState861() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_tryBlock, 4, null)
);
}
static ParseState get_ParseState862() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methodDecl, 4, null)
);
}
static ParseState get_ParseState863() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArg, 1012),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState864() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 693),
 new ShiftRule( t_ampersand, 694),
 new ShiftRule( t_asterisk, 695),
 new ShiftRule( t_pinned, 696),
 new ShiftRule( t_modreq, 697),
 new ShiftRule( t_modopt, 698),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_marshal, 1014)
},
new GotoRule[] { new GotoRule( n_id, 1013)
},
new ReduceRule( n_sigArg, 2, null)
);
}
static ParseState get_ParseState865() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1015)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState866() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_member_op, 1016)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState867() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_period, 154)
},
new GotoRule[] {},
new ReduceRule( n_methodName, 1, null)
);
}
static ParseState get_ParseState868() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1017)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState869() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methodName, 1, null)
);
}
static ParseState get_ParseState870() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methodName, 1, null)
);
}
static ParseState get_ParseState871() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54)
},
new GotoRule[] { new GotoRule( n_id, 1018)
},
null);
}
static ParseState get_ParseState872() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_compQstring, 3, null)
);
}
static ParseState get_ParseState873() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_instr, 4, null)
);
}
static ParseState get_ParseState874() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 1019),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState875() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_instr, 4, null)
);
}
static ParseState get_ParseState876() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_labels, 1020),
 new GotoRule( n_id, 771),
 new GotoRule( n_int32, 772)
},
new ReduceRule( n_labels, 0, null)
);
}
static ParseState get_ParseState877() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_labels, 1021),
 new GotoRule( n_id, 771),
 new GotoRule( n_int32, 772)
},
new ReduceRule( n_labels, 0, null)
);
}
static ParseState get_ParseState878() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_as, 1022)
},
new GotoRule[] {},
new ReduceRule( n_methodDecl, 4, null)
);
}
static ParseState get_ParseState879() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methodDecl, 4, null)
);
}
static ParseState get_ParseState880() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methodDecl, 4, null)
);
}
static ParseState get_ParseState881() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_period, 154)
},
new GotoRule[] {},
new ReduceRule( n_methodName, 1, null)
);
}
static ParseState get_ParseState882() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_equals, 1024)
},
new GotoRule[] { new GotoRule( n_initOpt, 1023)
},
new ReduceRule( n_initOpt, 0, null)
);
}
static ParseState get_ParseState883() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1025)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState884() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1026)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState885() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_square, 1027)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState886() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_square, 1028)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState887() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_square, 1029)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState888() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_square, 1030)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState889() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_QSTRING, 608)
},
new GotoRule[] { new GotoRule( n_compQstring, 1031)
},
null);
}
static ParseState get_ParseState890() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1032),
 new ShiftRule( t_nomangle, 892),
 new ShiftRule( t_ansi, 893),
 new ShiftRule( t_unicode, 894),
 new ShiftRule( t_autochar, 895),
 new ShiftRule( t_lasterr, 896),
 new ShiftRule( t_winapi, 897),
 new ShiftRule( t_cdecl, 898),
 new ShiftRule( t_stdcall, 899),
 new ShiftRule( t_thiscall, 900),
 new ShiftRule( t_fastcall, 901)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState891() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methAttr, 5, null)
);
}
static ParseState get_ParseState892() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_pinvAttr, 2, null)
);
}
static ParseState get_ParseState893() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_pinvAttr, 2, null)
);
}
static ParseState get_ParseState894() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_pinvAttr, 2, null)
);
}
static ParseState get_ParseState895() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_pinvAttr, 2, null)
);
}
static ParseState get_ParseState896() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_pinvAttr, 2, null)
);
}
static ParseState get_ParseState897() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_pinvAttr, 2, null)
);
}
static ParseState get_ParseState898() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_pinvAttr, 2, null)
);
}
static ParseState get_ParseState899() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_pinvAttr, 2, null)
);
}
static ParseState get_ParseState900() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_pinvAttr, 2, null)
);
}
static ParseState get_ParseState901() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_pinvAttr, 2, null)
);
}
static ParseState get_ParseState902() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_equals, 1024)
},
new GotoRule[] { new GotoRule( n_initOpt, 1033)
},
new ReduceRule( n_initOpt, 0, null)
);
}
static ParseState get_ParseState903() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54)
},
new GotoRule[] { new GotoRule( n_id, 1034)
},
null);
}
static ParseState get_ParseState904() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1035),
 new ShiftRule( t_asterisk, 1036),
 new ShiftRule( t_left_square, 1037)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState905() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1038)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState906() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_sysstring, 1039),
 new ShiftRule( t_array, 1040)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState907() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_bool, 1041)
},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState908() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState909() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState910() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState911() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState912() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState913() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState914() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState915() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState916() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 705)
},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState917() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 706)
},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState918() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState919() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_int8, 1042),
 new ShiftRule( t_int16, 1043),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_int, 1046)
},
new GotoRule[] { new GotoRule( n_int32, 1044),
 new GotoRule( n_int64, 1045)
},
null);
}
static ParseState get_ParseState920() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState921() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState922() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState923() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState924() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState925() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState926() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState927() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState928() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState929() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState930() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState931() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_null, 1048),
 new ShiftRule( t_variant, 1049),
 new ShiftRule( t_currency, 1050),
 new ShiftRule( t_void, 1051),
 new ShiftRule( t_bool, 1052),
 new ShiftRule( t_int8, 1053),
 new ShiftRule( t_int16, 1054),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 1057),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 1059),
 new ShiftRule( t_asterisk, 1060),
 new ShiftRule( t_decimal, 1061),
 new ShiftRule( t_date, 1062),
 new ShiftRule( t_bstr, 1063),
 new ShiftRule( t_lpstr, 1064),
 new ShiftRule( t_lpwstr, 1065),
 new ShiftRule( t_iunknown, 1066),
 new ShiftRule( t_idispatch, 1067),
 new ShiftRule( t_safearray, 1068),
 new ShiftRule( t_int, 1069),
 new ShiftRule( t_error, 1070),
 new ShiftRule( t_hresult, 1071),
 new ShiftRule( t_carray, 1072),
 new ShiftRule( t_userdefined, 1073),
 new ShiftRule( t_record, 1074),
 new ShiftRule( t_filetime, 1075),
 new ShiftRule( t_blob, 1076),
 new ShiftRule( t_stream, 1077),
 new ShiftRule( t_storage, 1078),
 new ShiftRule( t_streamed_object, 1079),
 new ShiftRule( t_stored_object, 1080),
 new ShiftRule( t_blob_object, 1081),
 new ShiftRule( t_cf, 1082),
 new ShiftRule( t_clsid, 1083)
},
new GotoRule[] { new GotoRule( n_variantType, 1047),
 new GotoRule( n_int32, 1055),
 new GotoRule( n_int64, 1056),
 new GotoRule( n_float64, 1058)
},
new ReduceRule( n_variantType, 0, null)
);
}
static ParseState get_ParseState932() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState933() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_struct, 1084)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState934() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState935() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_bstr, 1085)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState936() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState937() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState938() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_any, 1086)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState939() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 1, null)
);
}
static ParseState get_ParseState940() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_ddItem, 5, null)
);
}
static ParseState get_ParseState941() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_ddItem, 5, null)
);
}
static ParseState get_ParseState942() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_ddItem, 5, null)
);
}
static ParseState get_ParseState943() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_ddItem, 5, null)
);
}
static ParseState get_ParseState944() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_ddItem, 5, null)
);
}
static ParseState get_ParseState945() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_ddItem, 5, null)
);
}
static ParseState get_ParseState946() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_ddItem, 5, null)
);
}
static ParseState get_ParseState947() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54)
},
new GotoRule[] { new GotoRule( n_id, 1087)
},
null);
}
static ParseState get_ParseState948() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_vtfixupAttr, 2, null)
);
}
static ParseState get_ParseState949() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_vtfixupAttr, 2, null)
);
}
static ParseState get_ParseState950() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_vtfixupAttr, 2, null)
);
}
static ParseState get_ParseState951() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_vtfixupAttr, 2, null)
);
}
static ParseState get_ParseState952() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1088)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState953() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_assemblyDecl, 3, null)
);
}
static ParseState get_ParseState954() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_asmOrRefDecl, 3, null)
);
}
static ParseState get_ParseState955() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_publicKeyHead, 3, null)
);
}
static ParseState get_ParseState956() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 1089)
},
null);
}
static ParseState get_ParseState957() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_localeHead, 3, null)
);
}
static ParseState get_ParseState958() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_asmOrRefDecl, 3, null)
);
}
static ParseState get_ParseState959() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_assemblyRefDecl, 3, null)
);
}
static ParseState get_ParseState960() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_hashHead, 3, null)
);
}
static ParseState get_ParseState961() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_assemblyRefDecl, 3, null)
);
}
static ParseState get_ParseState962() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_publicKeyTokenHead, 3, null)
);
}
static ParseState get_ParseState963() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_period, 154)
},
new GotoRule[] {},
new ReduceRule( n_comtypeDecl, 3, null)
);
}
static ParseState get_ParseState964() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 1090)
},
null);
}
static ParseState get_ParseState965() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_period, 154)
},
new GotoRule[] {},
new ReduceRule( n_manifestResDecl, 3, null)
);
}
static ParseState get_ParseState966() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_secDecl, 6, null)
);
}
static ParseState get_ParseState967() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_QSTRING, 608)
},
new GotoRule[] { new GotoRule( n_nameValPairs, 1091),
 new GotoRule( n_nameValPair, 818),
 new GotoRule( n_compQstring, 819)
},
null);
}
static ParseState get_ParseState968() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_true, 1094),
 new ShiftRule( t_false, 1095),
 new ShiftRule( t_INT64, 84),
 new ShiftRule( t_QSTRING, 608),
 new ShiftRule( t_left_square, 700),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_caValue, 1092),
 new GotoRule( n_truefalse, 1093),
 new GotoRule( n_int32, 1096),
 new GotoRule( n_compQstring, 1097),
 new GotoRule( n_className, 1098),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 500),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState969() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_slash, 692)
},
new GotoRule[] {},
new ReduceRule( n_className, 4, null)
);
}
static ParseState get_ParseState970() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_slashedName, 1099),
 new GotoRule( n_name1, 500),
 new GotoRule( n_id, 52)
},
new ReduceRule( n_typeSpec, 4, null)
);
}
static ParseState get_ParseState971() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 4, null)
);
}
static ParseState get_ParseState972() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 826),
 new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_bound, 1100),
 new GotoRule( n_int32, 827)
},
new ReduceRule( n_bound, 0, null)
);
}
static ParseState get_ParseState973() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 1101)
},
new ReduceRule( n_bound, 2, null)
);
}
static ParseState get_ParseState974() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1102)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState975() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1103)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState976() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_slashedName, 969),
 new GotoRule( n_name1, 500),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState977() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_square, 1104),
 new ShiftRule( t_period, 154)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState978() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1105)
},
new GotoRule[] {},
new ReduceRule( n_type, 2, null)
);
}
static ParseState get_ParseState979() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1106)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState980() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1107)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState981() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_QSTRING, 608),
 new ShiftRule( t_left_paren, 1109)
},
new GotoRule[] { new GotoRule( n_compQstring, 1108)
},
null);
}
static ParseState get_ParseState982() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_square, 823),
 new ShiftRule( t_ellipsis, 826),
 new ShiftRule( t_INT64, 84),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_moduleDirective, 691)
},
new GotoRule[] { new GotoRule( n_bounds1, 824),
 new GotoRule( n_bound, 825),
 new GotoRule( n_int32, 827),
 new GotoRule( n_name1, 690),
 new GotoRule( n_id, 52)
},
new ReduceRule( n_bound, 0, null)
);
}
static ParseState get_ParseState983() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_member_op, 1110)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState984() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1111)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState985() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54)
},
new GotoRule[] { new GotoRule( n_id, 1112)
},
null);
}
static ParseState get_ParseState986() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_languageDecl, 6, null)
);
}
static ParseState get_ParseState987() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classDecl, 4, null)
);
}
static ParseState get_ParseState988() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classDecl, 4, null)
);
}
static ParseState get_ParseState989() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_eventDecls, 2, null)
);
}
static ParseState get_ParseState990() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_instance, 117),
 new ShiftRule( t_explicit, 118),
 new ShiftRule( t_default, 120),
 new ShiftRule( t_vararg, 121),
 new ShiftRule( t_unmanaged, 122)
},
new GotoRule[] { new GotoRule( n_callConv, 1113),
 new GotoRule( n_callKind, 119)
},
new ReduceRule( n_callKind, 0, null)
);
}
static ParseState get_ParseState991() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_instance, 117),
 new ShiftRule( t_explicit, 118),
 new ShiftRule( t_default, 120),
 new ShiftRule( t_vararg, 121),
 new ShiftRule( t_unmanaged, 122)
},
new GotoRule[] { new GotoRule( n_callConv, 1114),
 new GotoRule( n_callKind, 119)
},
new ReduceRule( n_callKind, 0, null)
);
}
static ParseState get_ParseState992() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_instance, 117),
 new ShiftRule( t_explicit, 118),
 new ShiftRule( t_default, 120),
 new ShiftRule( t_vararg, 121),
 new ShiftRule( t_unmanaged, 122)
},
new GotoRule[] { new GotoRule( n_callConv, 1115),
 new GotoRule( n_callKind, 119)
},
new ReduceRule( n_callKind, 0, null)
);
}
static ParseState get_ParseState993() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_instance, 117),
 new ShiftRule( t_explicit, 118),
 new ShiftRule( t_default, 120),
 new ShiftRule( t_vararg, 121),
 new ShiftRule( t_unmanaged, 122)
},
new GotoRule[] { new GotoRule( n_callConv, 1116),
 new GotoRule( n_callKind, 119)
},
new ReduceRule( n_callKind, 0, null)
);
}
static ParseState get_ParseState994() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_eventDecl, 1, null)
);
}
static ParseState get_ParseState995() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_eventDecl, 1, null)
);
}
static ParseState get_ParseState996() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_eventDecl, 1, null)
);
}
static ParseState get_ParseState997() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_eventHead, 4, null)
);
}
static ParseState get_ParseState998() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classDecl, 4, null)
);
}
static ParseState get_ParseState999() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_propDecls, 2, null)
);
}
static ParseState get_ParseState1000() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_instance, 117),
 new ShiftRule( t_explicit, 118),
 new ShiftRule( t_default, 120),
 new ShiftRule( t_vararg, 121),
 new ShiftRule( t_unmanaged, 122)
},
new GotoRule[] { new GotoRule( n_callConv, 1117),
 new GotoRule( n_callKind, 119)
},
new ReduceRule( n_callKind, 0, null)
);
}
static ParseState get_ParseState1001() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_instance, 117),
 new ShiftRule( t_explicit, 118),
 new ShiftRule( t_default, 120),
 new ShiftRule( t_vararg, 121),
 new ShiftRule( t_unmanaged, 122)
},
new GotoRule[] { new GotoRule( n_callConv, 1118),
 new GotoRule( n_callKind, 119)
},
new ReduceRule( n_callKind, 0, null)
);
}
static ParseState get_ParseState1002() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_instance, 117),
 new ShiftRule( t_explicit, 118),
 new ShiftRule( t_default, 120),
 new ShiftRule( t_vararg, 121),
 new ShiftRule( t_unmanaged, 122)
},
new GotoRule[] { new GotoRule( n_callConv, 1119),
 new GotoRule( n_callKind, 119)
},
new ReduceRule( n_callKind, 0, null)
);
}
static ParseState get_ParseState1003() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_propDecl, 1, null)
);
}
static ParseState get_ParseState1004() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_propDecl, 1, null)
);
}
static ParseState get_ParseState1005() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_propDecl, 1, null)
);
}
static ParseState get_ParseState1006() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_left_square, 693),
 new ShiftRule( t_ampersand, 694),
 new ShiftRule( t_asterisk, 695),
 new ShiftRule( t_pinned, 696),
 new ShiftRule( t_modreq, 697),
 new ShiftRule( t_modopt, 698)
},
new GotoRule[] { new GotoRule( n_id, 1120)
},
null);
}
static ParseState get_ParseState1007() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classDecl, 4, null)
);
}
static ParseState get_ParseState1008() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_with, 1121)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1009() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 700),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_className, 1122),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 500),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState1010() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54)
},
new GotoRule[] { new GotoRule( n_id, 1123)
},
null);
}
static ParseState get_ParseState1011() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 1124)
},
null);
}
static ParseState get_ParseState1012() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_sigArgs1, 3, null)
);
}
static ParseState get_ParseState1013() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_sigArg, 3, null)
);
}
static ParseState get_ParseState1014() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1125)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1015() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methodDecl, 5, null)
);
}
static ParseState get_ParseState1016() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ctorDirective, 869),
 new ShiftRule( t_cctorDirective, 870),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_methodName, 1126),
 new GotoRule( n_name1, 881),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState1017() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 1127),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState1018() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_instr, 5, null)
);
}
static ParseState get_ParseState1019() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1128)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1020() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_labels, 3, null)
);
}
static ParseState get_ParseState1021() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_labels, 3, null)
);
}
static ParseState get_ParseState1022() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54)
},
new GotoRule[] { new GotoRule( n_id, 1129)
},
null);
}
static ParseState get_ParseState1023() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methodDecl, 5, null)
);
}
static ParseState get_ParseState1024() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_float32, 1131),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_INT64, 71),
 new ShiftRule( t_int16, 1135),
 new ShiftRule( t_char, 1136),
 new ShiftRule( t_int8, 1137),
 new ShiftRule( t_bool, 1138),
 new ShiftRule( t_QSTRING, 608),
 new ShiftRule( t_bytearray, 66),
 new ShiftRule( t_nullref, 1141)
},
new GotoRule[] { new GotoRule( n_fieldInit, 1130),
 new GotoRule( n_float64, 1132),
 new GotoRule( n_int64, 1133),
 new GotoRule( n_int32, 1134),
 new GotoRule( n_compQstring, 1139),
 new GotoRule( n_bytearrayhead, 1140)
},
null);
}
static ParseState get_ParseState1025() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 1142),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState1026() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_custom, 905),
 new ShiftRule( t_fixed, 906),
 new ShiftRule( t_variant, 907),
 new ShiftRule( t_currency, 908),
 new ShiftRule( t_syschar, 909),
 new ShiftRule( t_void, 910),
 new ShiftRule( t_bool, 911),
 new ShiftRule( t_int8, 912),
 new ShiftRule( t_int16, 913),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 916),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_error, 918),
 new ShiftRule( t_unsigned, 919),
 new ShiftRule( t_decimal, 920),
 new ShiftRule( t_date, 921),
 new ShiftRule( t_bstr, 922),
 new ShiftRule( t_lpstr, 923),
 new ShiftRule( t_lpwstr, 924),
 new ShiftRule( t_lptstr, 925),
 new ShiftRule( t_objectref, 926),
 new ShiftRule( t_iunknown, 927),
 new ShiftRule( t_idispatch, 928),
 new ShiftRule( t_struct, 929),
 new ShiftRule( t_interface, 930),
 new ShiftRule( t_safearray, 931),
 new ShiftRule( t_int, 932),
 new ShiftRule( t_nested, 933),
 new ShiftRule( t_byvalstr, 934),
 new ShiftRule( t_ansi, 935),
 new ShiftRule( t_tbstr, 936),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_as, 938),
 new ShiftRule( t_lpstruct, 939)
},
new GotoRule[] { new GotoRule( n_nativeType, 1143),
 new GotoRule( n_int32, 914),
 new GotoRule( n_int64, 915),
 new GotoRule( n_float64, 917),
 new GotoRule( n_methodSpec, 937)
},
new ReduceRule( n_nativeType, 0, null)
);
}
static ParseState get_ParseState1027() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_paramAttr, 4, null)
);
}
static ParseState get_ParseState1028() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_paramAttr, 4, null)
);
}
static ParseState get_ParseState1029() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_paramAttr, 4, null)
);
}
static ParseState get_ParseState1030() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_paramAttr, 4, null)
);
}
static ParseState get_ParseState1031() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_plus, 767)
},
new GotoRule[] { new GotoRule( n_pinvAttr, 1144)
},
new ReduceRule( n_pinvAttr, 0, null)
);
}
static ParseState get_ParseState1032() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methAttr, 6, null)
);
}
static ParseState get_ParseState1033() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldDecl, 7, null)
);
}
static ParseState get_ParseState1034() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_atOpt, 2, null)
);
}
static ParseState get_ParseState1035() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldAttr, 5, null)
);
}
static ParseState get_ParseState1036() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 2, null)
);
}
static ParseState get_ParseState1037() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_square, 1145),
 new ShiftRule( t_INT64, 84),
 new ShiftRule( t_plus, 1147)
},
new GotoRule[] { new GotoRule( n_int32, 1146)
},
null);
}
static ParseState get_ParseState1038() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_QSTRING, 608)
},
new GotoRule[] { new GotoRule( n_compQstring, 1148)
},
null);
}
static ParseState get_ParseState1039() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 1149)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1040() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 1150)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1041() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 2, null)
);
}
static ParseState get_ParseState1042() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 2, null)
);
}
static ParseState get_ParseState1043() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 2, null)
);
}
static ParseState get_ParseState1044() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 2, null)
);
}
static ParseState get_ParseState1045() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 2, null)
);
}
static ParseState get_ParseState1046() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 2, null)
);
}
static ParseState get_ParseState1047() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 1151),
 new ShiftRule( t_vector, 1152),
 new ShiftRule( t_ampersand, 1153),
 new ShiftRule( t_comma, 1154)
},
new GotoRule[] {},
new ReduceRule( n_nativeType, 2, null)
);
}
static ParseState get_ParseState1048() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1049() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1050() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1051() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1052() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1053() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1054() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1055() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1056() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1057() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 705)
},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1058() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 706)
},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1059() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_int8, 1155),
 new ShiftRule( t_int16, 1156),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_int, 1159)
},
new GotoRule[] { new GotoRule( n_int32, 1157),
 new GotoRule( n_int64, 1158)
},
null);
}
static ParseState get_ParseState1060() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1061() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1062() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1063() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1064() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1065() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1066() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1067() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1068() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1069() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1070() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1071() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1072() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1073() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1074() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1075() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1076() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1077() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1078() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1079() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1080() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1081() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1082() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1083() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 1, null)
);
}
static ParseState get_ParseState1084() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 2, null)
);
}
static ParseState get_ParseState1085() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 2, null)
);
}
static ParseState get_ParseState1086() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 2, null)
);
}
static ParseState get_ParseState1087() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_vtfixupDecl, 7, null)
);
}
static ParseState get_ParseState1088() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_entrypointDirective, 659)
},
new GotoRule[] { new GotoRule( n_fileEntry, 1160)
},
new ReduceRule( n_fileEntry, 0, null)
);
}
static ParseState get_ParseState1089() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_colon, 1161)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1090() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_manifestResDecl, 4, null)
);
}
static ParseState get_ParseState1091() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nameValPairs, 3, null)
);
}
static ParseState get_ParseState1092() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nameValPair, 3, null)
);
}
static ParseState get_ParseState1093() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_caValue, 1, null)
);
}
static ParseState get_ParseState1094() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_truefalse, 1, null)
);
}
static ParseState get_ParseState1095() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_truefalse, 1, null)
);
}
static ParseState get_ParseState1096() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1162)
},
new GotoRule[] {},
new ReduceRule( n_caValue, 1, null)
);
}
static ParseState get_ParseState1097() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_plus, 767)
},
new GotoRule[] {},
new ReduceRule( n_caValue, 1, null)
);
}
static ParseState get_ParseState1098() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1163)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1099() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_slash, 692)
},
new GotoRule[] {},
new ReduceRule( n_className, 5, null)
);
}
static ParseState get_ParseState1100() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_bounds1, 3, null)
);
}
static ParseState get_ParseState1101() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_bound, 3, null)
);
}
static ParseState get_ParseState1102() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 5, null)
);
}
static ParseState get_ParseState1103() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 5, null)
);
}
static ParseState get_ParseState1104() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_slashedName, 1099),
 new GotoRule( n_name1, 500),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState1105() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 1164),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState1106() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 1165),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState1107() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_customType, 6, null)
);
}
static ParseState get_ParseState1108() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_plus, 767)
},
new GotoRule[] {},
new ReduceRule( n_customAttrDecl, 7, null)
);
}
static ParseState get_ParseState1109() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_customHeadWithOwner, 7, null)
);
}
static ParseState get_ParseState1110() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ctorDirective, 869),
 new ShiftRule( t_cctorDirective, 870),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_methodName, 1166),
 new GotoRule( n_name1, 881),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState1111() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 1167),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState1112() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_memberRef, 5, null)
);
}
static ParseState get_ParseState1113() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522)
},
new GotoRule[] { new GotoRule( n_type, 1168),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState1114() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522)
},
new GotoRule[] { new GotoRule( n_type, 1169),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState1115() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522)
},
new GotoRule[] { new GotoRule( n_type, 1170),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState1116() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522)
},
new GotoRule[] { new GotoRule( n_type, 1171),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState1117() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522)
},
new GotoRule[] { new GotoRule( n_type, 1172),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState1118() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522)
},
new GotoRule[] { new GotoRule( n_type, 1173),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState1119() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522)
},
new GotoRule[] { new GotoRule( n_type, 1174),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState1120() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1175)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1121() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_instance, 117),
 new ShiftRule( t_explicit, 118),
 new ShiftRule( t_default, 120),
 new ShiftRule( t_vararg, 121),
 new ShiftRule( t_unmanaged, 122)
},
new GotoRule[] { new GotoRule( n_callConv, 1176),
 new GotoRule( n_callKind, 119)
},
new ReduceRule( n_callKind, 0, null)
);
}
static ParseState get_ParseState1122() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classNames, 3, null)
);
}
static ParseState get_ParseState1123() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_handlerBlock, 4, null)
);
}
static ParseState get_ParseState1124() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_handlerBlock, 4, null)
);
}
static ParseState get_ParseState1125() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_custom, 905),
 new ShiftRule( t_fixed, 906),
 new ShiftRule( t_variant, 907),
 new ShiftRule( t_currency, 908),
 new ShiftRule( t_syschar, 909),
 new ShiftRule( t_void, 910),
 new ShiftRule( t_bool, 911),
 new ShiftRule( t_int8, 912),
 new ShiftRule( t_int16, 913),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 916),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_error, 918),
 new ShiftRule( t_unsigned, 919),
 new ShiftRule( t_decimal, 920),
 new ShiftRule( t_date, 921),
 new ShiftRule( t_bstr, 922),
 new ShiftRule( t_lpstr, 923),
 new ShiftRule( t_lpwstr, 924),
 new ShiftRule( t_lptstr, 925),
 new ShiftRule( t_objectref, 926),
 new ShiftRule( t_iunknown, 927),
 new ShiftRule( t_idispatch, 928),
 new ShiftRule( t_struct, 929),
 new ShiftRule( t_interface, 930),
 new ShiftRule( t_safearray, 931),
 new ShiftRule( t_int, 932),
 new ShiftRule( t_nested, 933),
 new ShiftRule( t_byvalstr, 934),
 new ShiftRule( t_ansi, 935),
 new ShiftRule( t_tbstr, 936),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_as, 938),
 new ShiftRule( t_lpstruct, 939)
},
new GotoRule[] { new GotoRule( n_nativeType, 1177),
 new GotoRule( n_int32, 914),
 new GotoRule( n_int64, 915),
 new GotoRule( n_float64, 917),
 new GotoRule( n_methodSpec, 937)
},
new ReduceRule( n_nativeType, 0, null)
);
}
static ParseState get_ParseState1126() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1178)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1127() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1179)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1128() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_instr, 6, null)
);
}
static ParseState get_ParseState1129() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methodDecl, 6, null)
);
}
static ParseState get_ParseState1130() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_initOpt, 2, null)
);
}
static ParseState get_ParseState1131() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1180)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1132() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1181)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1133() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1182)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1134() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1183)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1135() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1184)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1136() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1185)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1137() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1186)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1138() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1187)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1139() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_plus, 767)
},
new GotoRule[] {},
new ReduceRule( n_fieldInit, 1, null)
);
}
static ParseState get_ParseState1140() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_HEXBYTE, 79)
},
new GotoRule[] { new GotoRule( n_bytes, 1188),
 new GotoRule( n_hexbytes, 78)
},
new ReduceRule( n_bytes, 0, null)
);
}
static ParseState get_ParseState1141() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldInit, 1, null)
);
}
static ParseState get_ParseState1142() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1189)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1143() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1190),
 new ShiftRule( t_asterisk, 1036),
 new ShiftRule( t_left_square, 1037)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1144() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1191),
 new ShiftRule( t_nomangle, 892),
 new ShiftRule( t_ansi, 893),
 new ShiftRule( t_unicode, 894),
 new ShiftRule( t_autochar, 895),
 new ShiftRule( t_lasterr, 896),
 new ShiftRule( t_winapi, 897),
 new ShiftRule( t_cdecl, 898),
 new ShiftRule( t_stdcall, 899),
 new ShiftRule( t_thiscall, 900),
 new ShiftRule( t_fastcall, 901)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1145() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 3, null)
);
}
static ParseState get_ParseState1146() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_square, 1192),
 new ShiftRule( t_plus, 1193)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1147() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 1194)
},
null);
}
static ParseState get_ParseState1148() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_comma, 1195),
 new ShiftRule( t_plus, 767)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1149() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 1196)
},
null);
}
static ParseState get_ParseState1150() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 1197)
},
null);
}
static ParseState get_ParseState1151() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_square, 1198)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1152() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 2, null)
);
}
static ParseState get_ParseState1153() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 2, null)
);
}
static ParseState get_ParseState1154() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_QSTRING, 608)
},
new GotoRule[] { new GotoRule( n_compQstring, 1199)
},
null);
}
static ParseState get_ParseState1155() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 2, null)
);
}
static ParseState get_ParseState1156() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 2, null)
);
}
static ParseState get_ParseState1157() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 2, null)
);
}
static ParseState get_ParseState1158() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 2, null)
);
}
static ParseState get_ParseState1159() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 2, null)
);
}
static ParseState get_ParseState1160() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fileDecl, 8, null)
);
}
static ParseState get_ParseState1161() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 1200)
},
null);
}
static ParseState get_ParseState1162() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 1201)
},
null);
}
static ParseState get_ParseState1163() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_int8, 1202),
 new ShiftRule( t_int16, 1203),
 new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 1204)
},
null);
}
static ParseState get_ParseState1164() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1205)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1165() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1206)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1166() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1207)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1167() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1208)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1168() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 718),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522),
 new ShiftRule( t_ampersand, 694),
 new ShiftRule( t_asterisk, 695),
 new ShiftRule( t_pinned, 696),
 new ShiftRule( t_modreq, 697),
 new ShiftRule( t_modopt, 698),
 new ShiftRule( t_ctorDirective, 869),
 new ShiftRule( t_cctorDirective, 870)
},
new GotoRule[] { new GotoRule( n_typeSpec, 1209),
 new GotoRule( n_className, 497),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 867),
 new GotoRule( n_id, 52),
 new GotoRule( n_type, 501),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520),
 new GotoRule( n_methodName, 1210)
},
null);
}
static ParseState get_ParseState1169() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 718),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522),
 new ShiftRule( t_ampersand, 694),
 new ShiftRule( t_asterisk, 695),
 new ShiftRule( t_pinned, 696),
 new ShiftRule( t_modreq, 697),
 new ShiftRule( t_modopt, 698),
 new ShiftRule( t_ctorDirective, 869),
 new ShiftRule( t_cctorDirective, 870)
},
new GotoRule[] { new GotoRule( n_typeSpec, 1211),
 new GotoRule( n_className, 497),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 867),
 new GotoRule( n_id, 52),
 new GotoRule( n_type, 501),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520),
 new GotoRule( n_methodName, 1212)
},
null);
}
static ParseState get_ParseState1170() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 718),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522),
 new ShiftRule( t_ampersand, 694),
 new ShiftRule( t_asterisk, 695),
 new ShiftRule( t_pinned, 696),
 new ShiftRule( t_modreq, 697),
 new ShiftRule( t_modopt, 698),
 new ShiftRule( t_ctorDirective, 869),
 new ShiftRule( t_cctorDirective, 870)
},
new GotoRule[] { new GotoRule( n_typeSpec, 1213),
 new GotoRule( n_className, 497),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 867),
 new GotoRule( n_id, 52),
 new GotoRule( n_type, 501),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520),
 new GotoRule( n_methodName, 1214)
},
null);
}
static ParseState get_ParseState1171() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 718),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522),
 new ShiftRule( t_ampersand, 694),
 new ShiftRule( t_asterisk, 695),
 new ShiftRule( t_pinned, 696),
 new ShiftRule( t_modreq, 697),
 new ShiftRule( t_modopt, 698),
 new ShiftRule( t_ctorDirective, 869),
 new ShiftRule( t_cctorDirective, 870)
},
new GotoRule[] { new GotoRule( n_typeSpec, 1215),
 new GotoRule( n_className, 497),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 867),
 new GotoRule( n_id, 52),
 new GotoRule( n_type, 501),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520),
 new GotoRule( n_methodName, 1216)
},
null);
}
static ParseState get_ParseState1172() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 718),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522),
 new ShiftRule( t_ampersand, 694),
 new ShiftRule( t_asterisk, 695),
 new ShiftRule( t_pinned, 696),
 new ShiftRule( t_modreq, 697),
 new ShiftRule( t_modopt, 698),
 new ShiftRule( t_ctorDirective, 869),
 new ShiftRule( t_cctorDirective, 870)
},
new GotoRule[] { new GotoRule( n_typeSpec, 1217),
 new GotoRule( n_className, 497),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 867),
 new GotoRule( n_id, 52),
 new GotoRule( n_type, 501),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520),
 new GotoRule( n_methodName, 1218)
},
null);
}
static ParseState get_ParseState1173() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 718),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522),
 new ShiftRule( t_ampersand, 694),
 new ShiftRule( t_asterisk, 695),
 new ShiftRule( t_pinned, 696),
 new ShiftRule( t_modreq, 697),
 new ShiftRule( t_modopt, 698),
 new ShiftRule( t_ctorDirective, 869),
 new ShiftRule( t_cctorDirective, 870)
},
new GotoRule[] { new GotoRule( n_typeSpec, 1219),
 new GotoRule( n_className, 497),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 867),
 new GotoRule( n_id, 52),
 new GotoRule( n_type, 501),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520),
 new GotoRule( n_methodName, 1220)
},
null);
}
static ParseState get_ParseState1174() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 718),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522),
 new ShiftRule( t_ampersand, 694),
 new ShiftRule( t_asterisk, 695),
 new ShiftRule( t_pinned, 696),
 new ShiftRule( t_modreq, 697),
 new ShiftRule( t_modopt, 698),
 new ShiftRule( t_ctorDirective, 869),
 new ShiftRule( t_cctorDirective, 870)
},
new GotoRule[] { new GotoRule( n_typeSpec, 1221),
 new GotoRule( n_className, 497),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 867),
 new GotoRule( n_id, 52),
 new GotoRule( n_type, 501),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520),
 new GotoRule( n_methodName, 1222)
},
null);
}
static ParseState get_ParseState1175() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 1223),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState1176() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522)
},
new GotoRule[] { new GotoRule( n_type, 1224),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState1177() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1225),
 new ShiftRule( t_asterisk, 1036),
 new ShiftRule( t_left_square, 1037)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1178() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 1226),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState1179() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_instr, 7, null)
);
}
static ParseState get_ParseState1180() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_float32, 598),
 new ShiftRule( t_INT64, 517)
},
new GotoRule[] { new GotoRule( n_float64, 1227),
 new GotoRule( n_int32, 646),
 new GotoRule( n_int64, 1228)
},
null);
}
static ParseState get_ParseState1181() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_float32, 598),
 new ShiftRule( t_INT64, 129)
},
new GotoRule[] { new GotoRule( n_float64, 1229),
 new GotoRule( n_int64, 1230)
},
null);
}
static ParseState get_ParseState1182() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 129)
},
new GotoRule[] { new GotoRule( n_int64, 1231)
},
null);
}
static ParseState get_ParseState1183() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 129)
},
new GotoRule[] { new GotoRule( n_int64, 1232)
},
null);
}
static ParseState get_ParseState1184() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 129)
},
new GotoRule[] { new GotoRule( n_int64, 1233)
},
null);
}
static ParseState get_ParseState1185() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 129)
},
new GotoRule[] { new GotoRule( n_int64, 1234)
},
null);
}
static ParseState get_ParseState1186() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 129)
},
new GotoRule[] { new GotoRule( n_int64, 1235)
},
null);
}
static ParseState get_ParseState1187() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_true, 1094),
 new ShiftRule( t_false, 1095)
},
new GotoRule[] { new GotoRule( n_truefalse, 1236)
},
null);
}
static ParseState get_ParseState1188() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1237)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1189() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_implAttr, 1238)
},
new ReduceRule( n_implAttr, 0, null)
);
}
static ParseState get_ParseState1190() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ctorDirective, 869),
 new ShiftRule( t_cctorDirective, 870),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_methodName, 1239),
 new GotoRule( n_name1, 881),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState1191() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methAttr, 8, null)
);
}
static ParseState get_ParseState1192() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 4, null)
);
}
static ParseState get_ParseState1193() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 1240)
},
null);
}
static ParseState get_ParseState1194() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_square, 1241)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1195() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_QSTRING, 608)
},
new GotoRule[] { new GotoRule( n_compQstring, 1242)
},
null);
}
static ParseState get_ParseState1196() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_square, 1243)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1197() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_square, 1244)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1198() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_variantType, 3, null)
);
}
static ParseState get_ParseState1199() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_plus, 767)
},
new GotoRule[] {},
new ReduceRule( n_nativeType, 4, null)
);
}
static ParseState get_ParseState1200() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_colon, 1245)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1201() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1246)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1202() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_colon, 1247)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1203() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_colon, 1248)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1204() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_colon, 1249),
 new ShiftRule( t_right_paren, 1250)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1205() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_type, 7, null)
);
}
static ParseState get_ParseState1206() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_customType, 8, null)
);
}
static ParseState get_ParseState1207() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 1251),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState1208() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_memberRef, 7, null)
);
}
static ParseState get_ParseState1209() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_member_op, 1252)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1210() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1253)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1211() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_member_op, 1254)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1212() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1255)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1213() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_member_op, 1256)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1214() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1257)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1215() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_member_op, 1258)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1216() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1259)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1217() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_member_op, 1260)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1218() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1261)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1219() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_member_op, 1262)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1220() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1263)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1221() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_member_op, 1264)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1222() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1265)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1223() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1266)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1224() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_square, 718),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55),
 new ShiftRule( t_class, 502),
 new ShiftRule( t_object, 503),
 new ShiftRule( t_string, 504),
 new ShiftRule( t_value, 505),
 new ShiftRule( t_valuetype, 506),
 new ShiftRule( t_exclamation, 507),
 new ShiftRule( t_method, 509),
 new ShiftRule( t_typedref, 510),
 new ShiftRule( t_char, 511),
 new ShiftRule( t_void, 512),
 new ShiftRule( t_bool, 513),
 new ShiftRule( t_int8, 514),
 new ShiftRule( t_int16, 515),
 new ShiftRule( t_INT64, 517),
 new ShiftRule( t_float32, 519),
 new ShiftRule( t_FLOAT64, 69),
 new ShiftRule( t_unsigned, 521),
 new ShiftRule( t_native, 522),
 new ShiftRule( t_ampersand, 694),
 new ShiftRule( t_asterisk, 695),
 new ShiftRule( t_pinned, 696),
 new ShiftRule( t_modreq, 697),
 new ShiftRule( t_modopt, 698)
},
new GotoRule[] { new GotoRule( n_typeSpec, 1267),
 new GotoRule( n_className, 497),
 new GotoRule( n_slashedName, 499),
 new GotoRule( n_name1, 500),
 new GotoRule( n_id, 52),
 new GotoRule( n_type, 501),
 new GotoRule( n_methodSpec, 508),
 new GotoRule( n_int32, 516),
 new GotoRule( n_int64, 518),
 new GotoRule( n_float64, 520)
},
null);
}
static ParseState get_ParseState1225() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54)
},
new GotoRule[] { new GotoRule( n_id, 1268)
},
new ReduceRule( n_sigArg, 6, null)
);
}
static ParseState get_ParseState1226() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1269)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1227() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1270),
 new ShiftRule( t_left_paren, 706)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1228() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1271)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1229() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1272),
 new ShiftRule( t_left_paren, 706)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1230() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1273)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1231() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1274)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1232() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1275)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1233() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1276)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1234() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1277)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1235() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1278)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1236() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1279)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1237() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldInit, 3, null)
);
}
static ParseState get_ParseState1238() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_brace, 1280),
 new ShiftRule( t_native, 1281),
 new ShiftRule( t_cil, 1282),
 new ShiftRule( t_optil, 1283),
 new ShiftRule( t_managed, 1284),
 new ShiftRule( t_unmanaged, 1285),
 new ShiftRule( t_forwardref, 1286),
 new ShiftRule( t_preservesig, 1287),
 new ShiftRule( t_runtime, 1288),
 new ShiftRule( t_internalcall, 1289),
 new ShiftRule( t_synchronized, 1290),
 new ShiftRule( t_noinlining, 1291)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1239() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1292)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1240() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_square, 1293)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1241() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 5, null)
);
}
static ParseState get_ParseState1242() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_comma, 1294),
 new ShiftRule( t_plus, 767),
 new ShiftRule( t_right_paren, 1295)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1243() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 5, null)
);
}
static ParseState get_ParseState1244() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 5, null)
);
}
static ParseState get_ParseState1245() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 1296)
},
null);
}
static ParseState get_ParseState1246() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_caValue, 4, null)
);
}
static ParseState get_ParseState1247() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 1297)
},
null);
}
static ParseState get_ParseState1248() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 1298)
},
null);
}
static ParseState get_ParseState1249() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_INT64, 84)
},
new GotoRule[] { new GotoRule( n_int32, 1299)
},
null);
}
static ParseState get_ParseState1250() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_caValue, 4, null)
);
}
static ParseState get_ParseState1251() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1300)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1252() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ctorDirective, 869),
 new ShiftRule( t_cctorDirective, 870),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_methodName, 1301),
 new GotoRule( n_name1, 881),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState1253() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 1302),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState1254() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ctorDirective, 869),
 new ShiftRule( t_cctorDirective, 870),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_methodName, 1303),
 new GotoRule( n_name1, 881),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState1255() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 1304),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState1256() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ctorDirective, 869),
 new ShiftRule( t_cctorDirective, 870),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_methodName, 1305),
 new GotoRule( n_name1, 881),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState1257() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 1306),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState1258() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ctorDirective, 869),
 new ShiftRule( t_cctorDirective, 870),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_methodName, 1307),
 new GotoRule( n_name1, 881),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState1259() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 1308),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState1260() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ctorDirective, 869),
 new ShiftRule( t_cctorDirective, 870),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_methodName, 1309),
 new GotoRule( n_name1, 881),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState1261() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 1310),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState1262() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ctorDirective, 869),
 new ShiftRule( t_cctorDirective, 870),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_methodName, 1311),
 new GotoRule( n_name1, 881),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState1263() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 1312),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState1264() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ctorDirective, 869),
 new ShiftRule( t_cctorDirective, 870),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_methodName, 1313),
 new GotoRule( n_name1, 881),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState1265() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 1314),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState1266() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_equals, 1024)
},
new GotoRule[] { new GotoRule( n_initOpt, 1315)
},
new ReduceRule( n_initOpt, 0, null)
);
}
static ParseState get_ParseState1267() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_member_op, 1316)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1268() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_sigArg, 7, null)
);
}
static ParseState get_ParseState1269() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_instr, 9, null)
);
}
static ParseState get_ParseState1270() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldInit, 4, null)
);
}
static ParseState get_ParseState1271() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldInit, 4, null)
);
}
static ParseState get_ParseState1272() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldInit, 4, null)
);
}
static ParseState get_ParseState1273() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldInit, 4, null)
);
}
static ParseState get_ParseState1274() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldInit, 4, null)
);
}
static ParseState get_ParseState1275() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldInit, 4, null)
);
}
static ParseState get_ParseState1276() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldInit, 4, null)
);
}
static ParseState get_ParseState1277() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldInit, 4, null)
);
}
static ParseState get_ParseState1278() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldInit, 4, null)
);
}
static ParseState get_ParseState1279() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_fieldInit, 4, null)
);
}
static ParseState get_ParseState1280() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methodHead, 11, null)
);
}
static ParseState get_ParseState1281() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_implAttr, 2, null)
);
}
static ParseState get_ParseState1282() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_implAttr, 2, null)
);
}
static ParseState get_ParseState1283() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_implAttr, 2, null)
);
}
static ParseState get_ParseState1284() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_implAttr, 2, null)
);
}
static ParseState get_ParseState1285() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_implAttr, 2, null)
);
}
static ParseState get_ParseState1286() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_implAttr, 2, null)
);
}
static ParseState get_ParseState1287() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_implAttr, 2, null)
);
}
static ParseState get_ParseState1288() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_implAttr, 2, null)
);
}
static ParseState get_ParseState1289() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_implAttr, 2, null)
);
}
static ParseState get_ParseState1290() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_implAttr, 2, null)
);
}
static ParseState get_ParseState1291() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_implAttr, 2, null)
);
}
static ParseState get_ParseState1292() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 1317),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState1293() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 6, null)
);
}
static ParseState get_ParseState1294() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_QSTRING, 608)
},
new GotoRule[] { new GotoRule( n_compQstring, 1318)
},
null);
}
static ParseState get_ParseState1295() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 6, null)
);
}
static ParseState get_ParseState1296() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_asmOrRefDecl, 8, null)
);
}
static ParseState get_ParseState1297() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1319)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1298() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1320)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1299() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1321)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1300() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_memberRef, 9, null)
);
}
static ParseState get_ParseState1301() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1322)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1302() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1323)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1303() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1324)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1304() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1325)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1305() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1326)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1306() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1327)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1307() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1328)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1308() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1329)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1309() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1330)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1310() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1331)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1311() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1332)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1312() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1333)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1313() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1334)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1314() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1335)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1315() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_propHead, 9, null)
);
}
static ParseState get_ParseState1316() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ctorDirective, 869),
 new ShiftRule( t_cctorDirective, 870),
 new ShiftRule( t_ID, 53),
 new ShiftRule( t_SQSTRING, 54),
 new ShiftRule( t_DOTTEDNAME, 55)
},
new GotoRule[] { new GotoRule( n_methodName, 1336),
 new GotoRule( n_name1, 881),
 new GotoRule( n_id, 52)
},
null);
}
static ParseState get_ParseState1317() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1337)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1318() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_comma, 1338),
 new ShiftRule( t_plus, 767)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1319() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_caValue, 6, null)
);
}
static ParseState get_ParseState1320() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_caValue, 6, null)
);
}
static ParseState get_ParseState1321() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_caValue, 6, null)
);
}
static ParseState get_ParseState1322() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 1339),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState1323() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_eventDecl, 7, null)
);
}
static ParseState get_ParseState1324() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 1340),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState1325() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_eventDecl, 7, null)
);
}
static ParseState get_ParseState1326() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 1341),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState1327() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_eventDecl, 7, null)
);
}
static ParseState get_ParseState1328() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 1342),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState1329() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_eventDecl, 7, null)
);
}
static ParseState get_ParseState1330() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 1343),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState1331() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_propDecl, 7, null)
);
}
static ParseState get_ParseState1332() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 1344),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState1333() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_propDecl, 7, null)
);
}
static ParseState get_ParseState1334() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 1345),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState1335() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_propDecl, 7, null)
);
}
static ParseState get_ParseState1336() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_paren, 1346)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1337() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] { new GotoRule( n_implAttr, 1347)
},
new ReduceRule( n_implAttr, 0, null)
);
}
static ParseState get_ParseState1338() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_QSTRING, 608)
},
new GotoRule[] { new GotoRule( n_compQstring, 1348)
},
null);
}
static ParseState get_ParseState1339() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1349)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1340() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1350)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1341() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1351)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1342() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1352)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1343() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1353)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1344() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1354)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1345() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1355)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1346() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_ellipsis, 760)
},
new GotoRule[] { new GotoRule( n_sigArgs0, 1356),
 new GotoRule( n_sigArgs1, 758),
 new GotoRule( n_sigArg, 759),
 new GotoRule( n_paramAttr, 761)
},
new ReduceRule( n_paramAttr, 0, null)
);
}
static ParseState get_ParseState1347() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_left_brace, 1357),
 new ShiftRule( t_native, 1281),
 new ShiftRule( t_cil, 1282),
 new ShiftRule( t_optil, 1283),
 new ShiftRule( t_managed, 1284),
 new ShiftRule( t_unmanaged, 1285),
 new ShiftRule( t_forwardref, 1286),
 new ShiftRule( t_preservesig, 1287),
 new ShiftRule( t_runtime, 1288),
 new ShiftRule( t_internalcall, 1289),
 new ShiftRule( t_synchronized, 1290),
 new ShiftRule( t_noinlining, 1291)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1348() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1358),
 new ShiftRule( t_plus, 767)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1349() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_eventDecl, 9, null)
);
}
static ParseState get_ParseState1350() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_eventDecl, 9, null)
);
}
static ParseState get_ParseState1351() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_eventDecl, 9, null)
);
}
static ParseState get_ParseState1352() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_eventDecl, 9, null)
);
}
static ParseState get_ParseState1353() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_propDecl, 9, null)
);
}
static ParseState get_ParseState1354() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_propDecl, 9, null)
);
}
static ParseState get_ParseState1355() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_propDecl, 9, null)
);
}
static ParseState get_ParseState1356() { return
new ParseState( new ShiftRule[] { new ShiftRule( t_right_paren, 1359)
},
new GotoRule[] {},
null);
}
static ParseState get_ParseState1357() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_methodHead, 15, null)
);
}
static ParseState get_ParseState1358() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_nativeType, 10, null)
);
}
static ParseState get_ParseState1359() { return
new ParseState( new ShiftRule[] {},
new GotoRule[] {},
new ReduceRule( n_classDecl, 13, null)
);
}
static ParseState[] parseStates={
get_ParseState0(),
get_ParseState1(),
get_ParseState2(),
get_ParseState3(),
get_ParseState4(),
get_ParseState5(),
get_ParseState6(),
get_ParseState7(),
get_ParseState8(),
get_ParseState9(),
get_ParseState10(),
get_ParseState11(),
get_ParseState12(),
get_ParseState13(),
get_ParseState14(),
get_ParseState15(),
get_ParseState16(),
get_ParseState17(),
get_ParseState18(),
get_ParseState19(),
get_ParseState20(),
get_ParseState21(),
get_ParseState22(),
get_ParseState23(),
get_ParseState24(),
get_ParseState25(),
get_ParseState26(),
get_ParseState27(),
get_ParseState28(),
get_ParseState29(),
get_ParseState30(),
get_ParseState31(),
get_ParseState32(),
get_ParseState33(),
get_ParseState34(),
get_ParseState35(),
get_ParseState36(),
get_ParseState37(),
get_ParseState38(),
get_ParseState39(),
get_ParseState40(),
get_ParseState41(),
get_ParseState42(),
get_ParseState43(),
get_ParseState44(),
get_ParseState45(),
get_ParseState46(),
get_ParseState47(),
get_ParseState48(),
get_ParseState49(),
get_ParseState50(),
get_ParseState51(),
get_ParseState52(),
get_ParseState53(),
get_ParseState54(),
get_ParseState55(),
get_ParseState56(),
get_ParseState57(),
get_ParseState58(),
get_ParseState59(),
get_ParseState60(),
get_ParseState61(),
get_ParseState62(),
get_ParseState63(),
get_ParseState64(),
get_ParseState65(),
get_ParseState66(),
get_ParseState67(),
get_ParseState68(),
get_ParseState69(),
get_ParseState70(),
get_ParseState71(),
get_ParseState72(),
get_ParseState73(),
get_ParseState74(),
get_ParseState75(),
get_ParseState76(),
get_ParseState77(),
get_ParseState78(),
get_ParseState79(),
get_ParseState80(),
get_ParseState81(),
get_ParseState82(),
get_ParseState83(),
get_ParseState84(),
get_ParseState85(),
get_ParseState86(),
get_ParseState87(),
get_ParseState88(),
get_ParseState89(),
get_ParseState90(),
get_ParseState91(),
get_ParseState92(),
get_ParseState93(),
get_ParseState94(),
get_ParseState95(),
get_ParseState96(),
get_ParseState97(),
get_ParseState98(),
get_ParseState99(),
get_ParseState100(),
get_ParseState101(),
get_ParseState102(),
get_ParseState103(),
get_ParseState104(),
get_ParseState105(),
get_ParseState106(),
get_ParseState107(),
get_ParseState108(),
get_ParseState109(),
get_ParseState110(),
get_ParseState111(),
get_ParseState112(),
get_ParseState113(),
get_ParseState114(),
get_ParseState115(),
get_ParseState116(),
get_ParseState117(),
get_ParseState118(),
get_ParseState119(),
get_ParseState120(),
get_ParseState121(),
get_ParseState122(),
get_ParseState123(),
get_ParseState124(),
get_ParseState125(),
get_ParseState126(),
get_ParseState127(),
get_ParseState128(),
get_ParseState129(),
get_ParseState130(),
get_ParseState131(),
get_ParseState132(),
get_ParseState133(),
get_ParseState134(),
get_ParseState135(),
get_ParseState136(),
get_ParseState137(),
get_ParseState138(),
get_ParseState139(),
get_ParseState140(),
get_ParseState141(),
get_ParseState142(),
get_ParseState143(),
get_ParseState144(),
get_ParseState145(),
get_ParseState146(),
get_ParseState147(),
get_ParseState148(),
get_ParseState149(),
get_ParseState150(),
get_ParseState151(),
get_ParseState152(),
get_ParseState153(),
get_ParseState154(),
get_ParseState155(),
get_ParseState156(),
get_ParseState157(),
get_ParseState158(),
get_ParseState159(),
get_ParseState160(),
get_ParseState161(),
get_ParseState162(),
get_ParseState163(),
get_ParseState164(),
get_ParseState165(),
get_ParseState166(),
get_ParseState167(),
get_ParseState168(),
get_ParseState169(),
get_ParseState170(),
get_ParseState171(),
get_ParseState172(),
get_ParseState173(),
get_ParseState174(),
get_ParseState175(),
get_ParseState176(),
get_ParseState177(),
get_ParseState178(),
get_ParseState179(),
get_ParseState180(),
get_ParseState181(),
get_ParseState182(),
get_ParseState183(),
get_ParseState184(),
get_ParseState185(),
get_ParseState186(),
get_ParseState187(),
get_ParseState188(),
get_ParseState189(),
get_ParseState190(),
get_ParseState191(),
get_ParseState192(),
get_ParseState193(),
get_ParseState194(),
get_ParseState195(),
get_ParseState196(),
get_ParseState197(),
get_ParseState198(),
get_ParseState199(),
get_ParseState200(),
get_ParseState201(),
get_ParseState202(),
get_ParseState203(),
get_ParseState204(),
get_ParseState205(),
get_ParseState206(),
get_ParseState207(),
get_ParseState208(),
get_ParseState209(),
get_ParseState210(),
get_ParseState211(),
get_ParseState212(),
get_ParseState213(),
get_ParseState214(),
get_ParseState215(),
get_ParseState216(),
get_ParseState217(),
get_ParseState218(),
get_ParseState219(),
get_ParseState220(),
get_ParseState221(),
get_ParseState222(),
get_ParseState223(),
get_ParseState224(),
get_ParseState225(),
get_ParseState226(),
get_ParseState227(),
get_ParseState228(),
get_ParseState229(),
get_ParseState230(),
get_ParseState231(),
get_ParseState232(),
get_ParseState233(),
get_ParseState234(),
get_ParseState235(),
get_ParseState236(),
get_ParseState237(),
get_ParseState238(),
get_ParseState239(),
get_ParseState240(),
get_ParseState241(),
get_ParseState242(),
get_ParseState243(),
get_ParseState244(),
get_ParseState245(),
get_ParseState246(),
get_ParseState247(),
get_ParseState248(),
get_ParseState249(),
get_ParseState250(),
get_ParseState251(),
get_ParseState252(),
get_ParseState253(),
get_ParseState254(),
get_ParseState255(),
get_ParseState256(),
get_ParseState257(),
get_ParseState258(),
get_ParseState259(),
get_ParseState260(),
get_ParseState261(),
get_ParseState262(),
get_ParseState263(),
get_ParseState264(),
get_ParseState265(),
get_ParseState266(),
get_ParseState267(),
get_ParseState268(),
get_ParseState269(),
get_ParseState270(),
get_ParseState271(),
get_ParseState272(),
get_ParseState273(),
get_ParseState274(),
get_ParseState275(),
get_ParseState276(),
get_ParseState277(),
get_ParseState278(),
get_ParseState279(),
get_ParseState280(),
get_ParseState281(),
get_ParseState282(),
get_ParseState283(),
get_ParseState284(),
get_ParseState285(),
get_ParseState286(),
get_ParseState287(),
get_ParseState288(),
get_ParseState289(),
get_ParseState290(),
get_ParseState291(),
get_ParseState292(),
get_ParseState293(),
get_ParseState294(),
get_ParseState295(),
get_ParseState296(),
get_ParseState297(),
get_ParseState298(),
get_ParseState299(),
get_ParseState300(),
get_ParseState301(),
get_ParseState302(),
get_ParseState303(),
get_ParseState304(),
get_ParseState305(),
get_ParseState306(),
get_ParseState307(),
get_ParseState308(),
get_ParseState309(),
get_ParseState310(),
get_ParseState311(),
get_ParseState312(),
get_ParseState313(),
get_ParseState314(),
get_ParseState315(),
get_ParseState316(),
get_ParseState317(),
get_ParseState318(),
get_ParseState319(),
get_ParseState320(),
get_ParseState321(),
get_ParseState322(),
get_ParseState323(),
get_ParseState324(),
get_ParseState325(),
get_ParseState326(),
get_ParseState327(),
get_ParseState328(),
get_ParseState329(),
get_ParseState330(),
get_ParseState331(),
get_ParseState332(),
get_ParseState333(),
get_ParseState334(),
get_ParseState335(),
get_ParseState336(),
get_ParseState337(),
get_ParseState338(),
get_ParseState339(),
get_ParseState340(),
get_ParseState341(),
get_ParseState342(),
get_ParseState343(),
get_ParseState344(),
get_ParseState345(),
get_ParseState346(),
get_ParseState347(),
get_ParseState348(),
get_ParseState349(),
get_ParseState350(),
get_ParseState351(),
get_ParseState352(),
get_ParseState353(),
get_ParseState354(),
get_ParseState355(),
get_ParseState356(),
get_ParseState357(),
get_ParseState358(),
get_ParseState359(),
get_ParseState360(),
get_ParseState361(),
get_ParseState362(),
get_ParseState363(),
get_ParseState364(),
get_ParseState365(),
get_ParseState366(),
get_ParseState367(),
get_ParseState368(),
get_ParseState369(),
get_ParseState370(),
get_ParseState371(),
get_ParseState372(),
get_ParseState373(),
get_ParseState374(),
get_ParseState375(),
get_ParseState376(),
get_ParseState377(),
get_ParseState378(),
get_ParseState379(),
get_ParseState380(),
get_ParseState381(),
get_ParseState382(),
get_ParseState383(),
get_ParseState384(),
get_ParseState385(),
get_ParseState386(),
get_ParseState387(),
get_ParseState388(),
get_ParseState389(),
get_ParseState390(),
get_ParseState391(),
get_ParseState392(),
get_ParseState393(),
get_ParseState394(),
get_ParseState395(),
get_ParseState396(),
get_ParseState397(),
get_ParseState398(),
get_ParseState399(),
get_ParseState400(),
get_ParseState401(),
get_ParseState402(),
get_ParseState403(),
get_ParseState404(),
get_ParseState405(),
get_ParseState406(),
get_ParseState407(),
get_ParseState408(),
get_ParseState409(),
get_ParseState410(),
get_ParseState411(),
get_ParseState412(),
get_ParseState413(),
get_ParseState414(),
get_ParseState415(),
get_ParseState416(),
get_ParseState417(),
get_ParseState418(),
get_ParseState419(),
get_ParseState420(),
get_ParseState421(),
get_ParseState422(),
get_ParseState423(),
get_ParseState424(),
get_ParseState425(),
get_ParseState426(),
get_ParseState427(),
get_ParseState428(),
get_ParseState429(),
get_ParseState430(),
get_ParseState431(),
get_ParseState432(),
get_ParseState433(),
get_ParseState434(),
get_ParseState435(),
get_ParseState436(),
get_ParseState437(),
get_ParseState438(),
get_ParseState439(),
get_ParseState440(),
get_ParseState441(),
get_ParseState442(),
get_ParseState443(),
get_ParseState444(),
get_ParseState445(),
get_ParseState446(),
get_ParseState447(),
get_ParseState448(),
get_ParseState449(),
get_ParseState450(),
get_ParseState451(),
get_ParseState452(),
get_ParseState453(),
get_ParseState454(),
get_ParseState455(),
get_ParseState456(),
get_ParseState457(),
get_ParseState458(),
get_ParseState459(),
get_ParseState460(),
get_ParseState461(),
get_ParseState462(),
get_ParseState463(),
get_ParseState464(),
get_ParseState465(),
get_ParseState466(),
get_ParseState467(),
get_ParseState468(),
get_ParseState469(),
get_ParseState470(),
get_ParseState471(),
get_ParseState472(),
get_ParseState473(),
get_ParseState474(),
get_ParseState475(),
get_ParseState476(),
get_ParseState477(),
get_ParseState478(),
get_ParseState479(),
get_ParseState480(),
get_ParseState481(),
get_ParseState482(),
get_ParseState483(),
get_ParseState484(),
get_ParseState485(),
get_ParseState486(),
get_ParseState487(),
get_ParseState488(),
get_ParseState489(),
get_ParseState490(),
get_ParseState491(),
get_ParseState492(),
get_ParseState493(),
get_ParseState494(),
get_ParseState495(),
get_ParseState496(),
get_ParseState497(),
get_ParseState498(),
get_ParseState499(),
get_ParseState500(),
get_ParseState501(),
get_ParseState502(),
get_ParseState503(),
get_ParseState504(),
get_ParseState505(),
get_ParseState506(),
get_ParseState507(),
get_ParseState508(),
get_ParseState509(),
get_ParseState510(),
get_ParseState511(),
get_ParseState512(),
get_ParseState513(),
get_ParseState514(),
get_ParseState515(),
get_ParseState516(),
get_ParseState517(),
get_ParseState518(),
get_ParseState519(),
get_ParseState520(),
get_ParseState521(),
get_ParseState522(),
get_ParseState523(),
get_ParseState524(),
get_ParseState525(),
get_ParseState526(),
get_ParseState527(),
get_ParseState528(),
get_ParseState529(),
get_ParseState530(),
get_ParseState531(),
get_ParseState532(),
get_ParseState533(),
get_ParseState534(),
get_ParseState535(),
get_ParseState536(),
get_ParseState537(),
get_ParseState538(),
get_ParseState539(),
get_ParseState540(),
get_ParseState541(),
get_ParseState542(),
get_ParseState543(),
get_ParseState544(),
get_ParseState545(),
get_ParseState546(),
get_ParseState547(),
get_ParseState548(),
get_ParseState549(),
get_ParseState550(),
get_ParseState551(),
get_ParseState552(),
get_ParseState553(),
get_ParseState554(),
get_ParseState555(),
get_ParseState556(),
get_ParseState557(),
get_ParseState558(),
get_ParseState559(),
get_ParseState560(),
get_ParseState561(),
get_ParseState562(),
get_ParseState563(),
get_ParseState564(),
get_ParseState565(),
get_ParseState566(),
get_ParseState567(),
get_ParseState568(),
get_ParseState569(),
get_ParseState570(),
get_ParseState571(),
get_ParseState572(),
get_ParseState573(),
get_ParseState574(),
get_ParseState575(),
get_ParseState576(),
get_ParseState577(),
get_ParseState578(),
get_ParseState579(),
get_ParseState580(),
get_ParseState581(),
get_ParseState582(),
get_ParseState583(),
get_ParseState584(),
get_ParseState585(),
get_ParseState586(),
get_ParseState587(),
get_ParseState588(),
get_ParseState589(),
get_ParseState590(),
get_ParseState591(),
get_ParseState592(),
get_ParseState593(),
get_ParseState594(),
get_ParseState595(),
get_ParseState596(),
get_ParseState597(),
get_ParseState598(),
get_ParseState599(),
get_ParseState600(),
get_ParseState601(),
get_ParseState602(),
get_ParseState603(),
get_ParseState604(),
get_ParseState605(),
get_ParseState606(),
get_ParseState607(),
get_ParseState608(),
get_ParseState609(),
get_ParseState610(),
get_ParseState611(),
get_ParseState612(),
get_ParseState613(),
get_ParseState614(),
get_ParseState615(),
get_ParseState616(),
get_ParseState617(),
get_ParseState618(),
get_ParseState619(),
get_ParseState620(),
get_ParseState621(),
get_ParseState622(),
get_ParseState623(),
get_ParseState624(),
get_ParseState625(),
get_ParseState626(),
get_ParseState627(),
get_ParseState628(),
get_ParseState629(),
get_ParseState630(),
get_ParseState631(),
get_ParseState632(),
get_ParseState633(),
get_ParseState634(),
get_ParseState635(),
get_ParseState636(),
get_ParseState637(),
get_ParseState638(),
get_ParseState639(),
get_ParseState640(),
get_ParseState641(),
get_ParseState642(),
get_ParseState643(),
get_ParseState644(),
get_ParseState645(),
get_ParseState646(),
get_ParseState647(),
get_ParseState648(),
get_ParseState649(),
get_ParseState650(),
get_ParseState651(),
get_ParseState652(),
get_ParseState653(),
get_ParseState654(),
get_ParseState655(),
get_ParseState656(),
get_ParseState657(),
get_ParseState658(),
get_ParseState659(),
get_ParseState660(),
get_ParseState661(),
get_ParseState662(),
get_ParseState663(),
get_ParseState664(),
get_ParseState665(),
get_ParseState666(),
get_ParseState667(),
get_ParseState668(),
get_ParseState669(),
get_ParseState670(),
get_ParseState671(),
get_ParseState672(),
get_ParseState673(),
get_ParseState674(),
get_ParseState675(),
get_ParseState676(),
get_ParseState677(),
get_ParseState678(),
get_ParseState679(),
get_ParseState680(),
get_ParseState681(),
get_ParseState682(),
get_ParseState683(),
get_ParseState684(),
get_ParseState685(),
get_ParseState686(),
get_ParseState687(),
get_ParseState688(),
get_ParseState689(),
get_ParseState690(),
get_ParseState691(),
get_ParseState692(),
get_ParseState693(),
get_ParseState694(),
get_ParseState695(),
get_ParseState696(),
get_ParseState697(),
get_ParseState698(),
get_ParseState699(),
get_ParseState700(),
get_ParseState701(),
get_ParseState702(),
get_ParseState703(),
get_ParseState704(),
get_ParseState705(),
get_ParseState706(),
get_ParseState707(),
get_ParseState708(),
get_ParseState709(),
get_ParseState710(),
get_ParseState711(),
get_ParseState712(),
get_ParseState713(),
get_ParseState714(),
get_ParseState715(),
get_ParseState716(),
get_ParseState717(),
get_ParseState718(),
get_ParseState719(),
get_ParseState720(),
get_ParseState721(),
get_ParseState722(),
get_ParseState723(),
get_ParseState724(),
get_ParseState725(),
get_ParseState726(),
get_ParseState727(),
get_ParseState728(),
get_ParseState729(),
get_ParseState730(),
get_ParseState731(),
get_ParseState732(),
get_ParseState733(),
get_ParseState734(),
get_ParseState735(),
get_ParseState736(),
get_ParseState737(),
get_ParseState738(),
get_ParseState739(),
get_ParseState740(),
get_ParseState741(),
get_ParseState742(),
get_ParseState743(),
get_ParseState744(),
get_ParseState745(),
get_ParseState746(),
get_ParseState747(),
get_ParseState748(),
get_ParseState749(),
get_ParseState750(),
get_ParseState751(),
get_ParseState752(),
get_ParseState753(),
get_ParseState754(),
get_ParseState755(),
get_ParseState756(),
get_ParseState757(),
get_ParseState758(),
get_ParseState759(),
get_ParseState760(),
get_ParseState761(),
get_ParseState762(),
get_ParseState763(),
get_ParseState764(),
get_ParseState765(),
get_ParseState766(),
get_ParseState767(),
get_ParseState768(),
get_ParseState769(),
get_ParseState770(),
get_ParseState771(),
get_ParseState772(),
get_ParseState773(),
get_ParseState774(),
get_ParseState775(),
get_ParseState776(),
get_ParseState777(),
get_ParseState778(),
get_ParseState779(),
get_ParseState780(),
get_ParseState781(),
get_ParseState782(),
get_ParseState783(),
get_ParseState784(),
get_ParseState785(),
get_ParseState786(),
get_ParseState787(),
get_ParseState788(),
get_ParseState789(),
get_ParseState790(),
get_ParseState791(),
get_ParseState792(),
get_ParseState793(),
get_ParseState794(),
get_ParseState795(),
get_ParseState796(),
get_ParseState797(),
get_ParseState798(),
get_ParseState799(),
get_ParseState800(),
get_ParseState801(),
get_ParseState802(),
get_ParseState803(),
get_ParseState804(),
get_ParseState805(),
get_ParseState806(),
get_ParseState807(),
get_ParseState808(),
get_ParseState809(),
get_ParseState810(),
get_ParseState811(),
get_ParseState812(),
get_ParseState813(),
get_ParseState814(),
get_ParseState815(),
get_ParseState816(),
get_ParseState817(),
get_ParseState818(),
get_ParseState819(),
get_ParseState820(),
get_ParseState821(),
get_ParseState822(),
get_ParseState823(),
get_ParseState824(),
get_ParseState825(),
get_ParseState826(),
get_ParseState827(),
get_ParseState828(),
get_ParseState829(),
get_ParseState830(),
get_ParseState831(),
get_ParseState832(),
get_ParseState833(),
get_ParseState834(),
get_ParseState835(),
get_ParseState836(),
get_ParseState837(),
get_ParseState838(),
get_ParseState839(),
get_ParseState840(),
get_ParseState841(),
get_ParseState842(),
get_ParseState843(),
get_ParseState844(),
get_ParseState845(),
get_ParseState846(),
get_ParseState847(),
get_ParseState848(),
get_ParseState849(),
get_ParseState850(),
get_ParseState851(),
get_ParseState852(),
get_ParseState853(),
get_ParseState854(),
get_ParseState855(),
get_ParseState856(),
get_ParseState857(),
get_ParseState858(),
get_ParseState859(),
get_ParseState860(),
get_ParseState861(),
get_ParseState862(),
get_ParseState863(),
get_ParseState864(),
get_ParseState865(),
get_ParseState866(),
get_ParseState867(),
get_ParseState868(),
get_ParseState869(),
get_ParseState870(),
get_ParseState871(),
get_ParseState872(),
get_ParseState873(),
get_ParseState874(),
get_ParseState875(),
get_ParseState876(),
get_ParseState877(),
get_ParseState878(),
get_ParseState879(),
get_ParseState880(),
get_ParseState881(),
get_ParseState882(),
get_ParseState883(),
get_ParseState884(),
get_ParseState885(),
get_ParseState886(),
get_ParseState887(),
get_ParseState888(),
get_ParseState889(),
get_ParseState890(),
get_ParseState891(),
get_ParseState892(),
get_ParseState893(),
get_ParseState894(),
get_ParseState895(),
get_ParseState896(),
get_ParseState897(),
get_ParseState898(),
get_ParseState899(),
get_ParseState900(),
get_ParseState901(),
get_ParseState902(),
get_ParseState903(),
get_ParseState904(),
get_ParseState905(),
get_ParseState906(),
get_ParseState907(),
get_ParseState908(),
get_ParseState909(),
get_ParseState910(),
get_ParseState911(),
get_ParseState912(),
get_ParseState913(),
get_ParseState914(),
get_ParseState915(),
get_ParseState916(),
get_ParseState917(),
get_ParseState918(),
get_ParseState919(),
get_ParseState920(),
get_ParseState921(),
get_ParseState922(),
get_ParseState923(),
get_ParseState924(),
get_ParseState925(),
get_ParseState926(),
get_ParseState927(),
get_ParseState928(),
get_ParseState929(),
get_ParseState930(),
get_ParseState931(),
get_ParseState932(),
get_ParseState933(),
get_ParseState934(),
get_ParseState935(),
get_ParseState936(),
get_ParseState937(),
get_ParseState938(),
get_ParseState939(),
get_ParseState940(),
get_ParseState941(),
get_ParseState942(),
get_ParseState943(),
get_ParseState944(),
get_ParseState945(),
get_ParseState946(),
get_ParseState947(),
get_ParseState948(),
get_ParseState949(),
get_ParseState950(),
get_ParseState951(),
get_ParseState952(),
get_ParseState953(),
get_ParseState954(),
get_ParseState955(),
get_ParseState956(),
get_ParseState957(),
get_ParseState958(),
get_ParseState959(),
get_ParseState960(),
get_ParseState961(),
get_ParseState962(),
get_ParseState963(),
get_ParseState964(),
get_ParseState965(),
get_ParseState966(),
get_ParseState967(),
get_ParseState968(),
get_ParseState969(),
get_ParseState970(),
get_ParseState971(),
get_ParseState972(),
get_ParseState973(),
get_ParseState974(),
get_ParseState975(),
get_ParseState976(),
get_ParseState977(),
get_ParseState978(),
get_ParseState979(),
get_ParseState980(),
get_ParseState981(),
get_ParseState982(),
get_ParseState983(),
get_ParseState984(),
get_ParseState985(),
get_ParseState986(),
get_ParseState987(),
get_ParseState988(),
get_ParseState989(),
get_ParseState990(),
get_ParseState991(),
get_ParseState992(),
get_ParseState993(),
get_ParseState994(),
get_ParseState995(),
get_ParseState996(),
get_ParseState997(),
get_ParseState998(),
get_ParseState999(),
get_ParseState1000(),
get_ParseState1001(),
get_ParseState1002(),
get_ParseState1003(),
get_ParseState1004(),
get_ParseState1005(),
get_ParseState1006(),
get_ParseState1007(),
get_ParseState1008(),
get_ParseState1009(),
get_ParseState1010(),
get_ParseState1011(),
get_ParseState1012(),
get_ParseState1013(),
get_ParseState1014(),
get_ParseState1015(),
get_ParseState1016(),
get_ParseState1017(),
get_ParseState1018(),
get_ParseState1019(),
get_ParseState1020(),
get_ParseState1021(),
get_ParseState1022(),
get_ParseState1023(),
get_ParseState1024(),
get_ParseState1025(),
get_ParseState1026(),
get_ParseState1027(),
get_ParseState1028(),
get_ParseState1029(),
get_ParseState1030(),
get_ParseState1031(),
get_ParseState1032(),
get_ParseState1033(),
get_ParseState1034(),
get_ParseState1035(),
get_ParseState1036(),
get_ParseState1037(),
get_ParseState1038(),
get_ParseState1039(),
get_ParseState1040(),
get_ParseState1041(),
get_ParseState1042(),
get_ParseState1043(),
get_ParseState1044(),
get_ParseState1045(),
get_ParseState1046(),
get_ParseState1047(),
get_ParseState1048(),
get_ParseState1049(),
get_ParseState1050(),
get_ParseState1051(),
get_ParseState1052(),
get_ParseState1053(),
get_ParseState1054(),
get_ParseState1055(),
get_ParseState1056(),
get_ParseState1057(),
get_ParseState1058(),
get_ParseState1059(),
get_ParseState1060(),
get_ParseState1061(),
get_ParseState1062(),
get_ParseState1063(),
get_ParseState1064(),
get_ParseState1065(),
get_ParseState1066(),
get_ParseState1067(),
get_ParseState1068(),
get_ParseState1069(),
get_ParseState1070(),
get_ParseState1071(),
get_ParseState1072(),
get_ParseState1073(),
get_ParseState1074(),
get_ParseState1075(),
get_ParseState1076(),
get_ParseState1077(),
get_ParseState1078(),
get_ParseState1079(),
get_ParseState1080(),
get_ParseState1081(),
get_ParseState1082(),
get_ParseState1083(),
get_ParseState1084(),
get_ParseState1085(),
get_ParseState1086(),
get_ParseState1087(),
get_ParseState1088(),
get_ParseState1089(),
get_ParseState1090(),
get_ParseState1091(),
get_ParseState1092(),
get_ParseState1093(),
get_ParseState1094(),
get_ParseState1095(),
get_ParseState1096(),
get_ParseState1097(),
get_ParseState1098(),
get_ParseState1099(),
get_ParseState1100(),
get_ParseState1101(),
get_ParseState1102(),
get_ParseState1103(),
get_ParseState1104(),
get_ParseState1105(),
get_ParseState1106(),
get_ParseState1107(),
get_ParseState1108(),
get_ParseState1109(),
get_ParseState1110(),
get_ParseState1111(),
get_ParseState1112(),
get_ParseState1113(),
get_ParseState1114(),
get_ParseState1115(),
get_ParseState1116(),
get_ParseState1117(),
get_ParseState1118(),
get_ParseState1119(),
get_ParseState1120(),
get_ParseState1121(),
get_ParseState1122(),
get_ParseState1123(),
get_ParseState1124(),
get_ParseState1125(),
get_ParseState1126(),
get_ParseState1127(),
get_ParseState1128(),
get_ParseState1129(),
get_ParseState1130(),
get_ParseState1131(),
get_ParseState1132(),
get_ParseState1133(),
get_ParseState1134(),
get_ParseState1135(),
get_ParseState1136(),
get_ParseState1137(),
get_ParseState1138(),
get_ParseState1139(),
get_ParseState1140(),
get_ParseState1141(),
get_ParseState1142(),
get_ParseState1143(),
get_ParseState1144(),
get_ParseState1145(),
get_ParseState1146(),
get_ParseState1147(),
get_ParseState1148(),
get_ParseState1149(),
get_ParseState1150(),
get_ParseState1151(),
get_ParseState1152(),
get_ParseState1153(),
get_ParseState1154(),
get_ParseState1155(),
get_ParseState1156(),
get_ParseState1157(),
get_ParseState1158(),
get_ParseState1159(),
get_ParseState1160(),
get_ParseState1161(),
get_ParseState1162(),
get_ParseState1163(),
get_ParseState1164(),
get_ParseState1165(),
get_ParseState1166(),
get_ParseState1167(),
get_ParseState1168(),
get_ParseState1169(),
get_ParseState1170(),
get_ParseState1171(),
get_ParseState1172(),
get_ParseState1173(),
get_ParseState1174(),
get_ParseState1175(),
get_ParseState1176(),
get_ParseState1177(),
get_ParseState1178(),
get_ParseState1179(),
get_ParseState1180(),
get_ParseState1181(),
get_ParseState1182(),
get_ParseState1183(),
get_ParseState1184(),
get_ParseState1185(),
get_ParseState1186(),
get_ParseState1187(),
get_ParseState1188(),
get_ParseState1189(),
get_ParseState1190(),
get_ParseState1191(),
get_ParseState1192(),
get_ParseState1193(),
get_ParseState1194(),
get_ParseState1195(),
get_ParseState1196(),
get_ParseState1197(),
get_ParseState1198(),
get_ParseState1199(),
get_ParseState1200(),
get_ParseState1201(),
get_ParseState1202(),
get_ParseState1203(),
get_ParseState1204(),
get_ParseState1205(),
get_ParseState1206(),
get_ParseState1207(),
get_ParseState1208(),
get_ParseState1209(),
get_ParseState1210(),
get_ParseState1211(),
get_ParseState1212(),
get_ParseState1213(),
get_ParseState1214(),
get_ParseState1215(),
get_ParseState1216(),
get_ParseState1217(),
get_ParseState1218(),
get_ParseState1219(),
get_ParseState1220(),
get_ParseState1221(),
get_ParseState1222(),
get_ParseState1223(),
get_ParseState1224(),
get_ParseState1225(),
get_ParseState1226(),
get_ParseState1227(),
get_ParseState1228(),
get_ParseState1229(),
get_ParseState1230(),
get_ParseState1231(),
get_ParseState1232(),
get_ParseState1233(),
get_ParseState1234(),
get_ParseState1235(),
get_ParseState1236(),
get_ParseState1237(),
get_ParseState1238(),
get_ParseState1239(),
get_ParseState1240(),
get_ParseState1241(),
get_ParseState1242(),
get_ParseState1243(),
get_ParseState1244(),
get_ParseState1245(),
get_ParseState1246(),
get_ParseState1247(),
get_ParseState1248(),
get_ParseState1249(),
get_ParseState1250(),
get_ParseState1251(),
get_ParseState1252(),
get_ParseState1253(),
get_ParseState1254(),
get_ParseState1255(),
get_ParseState1256(),
get_ParseState1257(),
get_ParseState1258(),
get_ParseState1259(),
get_ParseState1260(),
get_ParseState1261(),
get_ParseState1262(),
get_ParseState1263(),
get_ParseState1264(),
get_ParseState1265(),
get_ParseState1266(),
get_ParseState1267(),
get_ParseState1268(),
get_ParseState1269(),
get_ParseState1270(),
get_ParseState1271(),
get_ParseState1272(),
get_ParseState1273(),
get_ParseState1274(),
get_ParseState1275(),
get_ParseState1276(),
get_ParseState1277(),
get_ParseState1278(),
get_ParseState1279(),
get_ParseState1280(),
get_ParseState1281(),
get_ParseState1282(),
get_ParseState1283(),
get_ParseState1284(),
get_ParseState1285(),
get_ParseState1286(),
get_ParseState1287(),
get_ParseState1288(),
get_ParseState1289(),
get_ParseState1290(),
get_ParseState1291(),
get_ParseState1292(),
get_ParseState1293(),
get_ParseState1294(),
get_ParseState1295(),
get_ParseState1296(),
get_ParseState1297(),
get_ParseState1298(),
get_ParseState1299(),
get_ParseState1300(),
get_ParseState1301(),
get_ParseState1302(),
get_ParseState1303(),
get_ParseState1304(),
get_ParseState1305(),
get_ParseState1306(),
get_ParseState1307(),
get_ParseState1308(),
get_ParseState1309(),
get_ParseState1310(),
get_ParseState1311(),
get_ParseState1312(),
get_ParseState1313(),
get_ParseState1314(),
get_ParseState1315(),
get_ParseState1316(),
get_ParseState1317(),
get_ParseState1318(),
get_ParseState1319(),
get_ParseState1320(),
get_ParseState1321(),
get_ParseState1322(),
get_ParseState1323(),
get_ParseState1324(),
get_ParseState1325(),
get_ParseState1326(),
get_ParseState1327(),
get_ParseState1328(),
get_ParseState1329(),
get_ParseState1330(),
get_ParseState1331(),
get_ParseState1332(),
get_ParseState1333(),
get_ParseState1334(),
get_ParseState1335(),
get_ParseState1336(),
get_ParseState1337(),
get_ParseState1338(),
get_ParseState1339(),
get_ParseState1340(),
get_ParseState1341(),
get_ParseState1342(),
get_ParseState1343(),
get_ParseState1344(),
get_ParseState1345(),
get_ParseState1346(),
get_ParseState1347(),
get_ParseState1348(),
get_ParseState1349(),
get_ParseState1350(),
get_ParseState1351(),
get_ParseState1352(),
get_ParseState1353(),
get_ParseState1354(),
get_ParseState1355(),
get_ParseState1356(),
get_ParseState1357(),
get_ParseState1358(),
get_ParseState1359()
};
}
