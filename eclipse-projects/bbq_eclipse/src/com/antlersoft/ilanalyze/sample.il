
//  Microsoft (R) .NET Framework IL Disassembler.  Version 2.0.50727.42
//  Copyright (c) Microsoft Corporation.  All rights reserved.



// Metadata version: v2.0.50727
.assembly extern mscorlib
{
  .publickeytoken = (B7 7A 5C 56 19 34 E0 89 )                         // .z\V.4..
  .ver 2:0:0:0
}
.assembly extern System.Web
{
  .publickeytoken = (B0 3F 5F 7F 11 D5 0A 3A )                         // .?_....:
  .ver 2:0:0:0
}
.assembly extern System.Xml
{
  .publickeytoken = (B7 7A 5C 56 19 34 E0 89 )                         // .z\V.4..
  .ver 2:0:0:0
}
.assembly FP
{
  .custom instance void [mscorlib]System.Reflection.AssemblyFileVersionAttribute::.ctor(string) = ( 01 00 07 31 2E 30 2E 30 2E 30 00 00 )             // ...1.0.0.0..
  .custom instance void [mscorlib]System.Runtime.InteropServices.GuidAttribute::.ctor(string) = ( 01 00 24 64 39 39 36 33 62 38 34 2D 61 66 64 61   // ..$d9963b84-afda
                                                                                                  2D 34 39 33 31 2D 62 39 35 31 2D 62 61 66 65 37   // -4931-b951-bafe7
                                                                                                  32 63 37 39 61 36 38 00 00 )                      // 2c79a68..
  .custom instance void [mscorlib]System.Runtime.InteropServices.ComVisibleAttribute::.ctor(bool) = ( 01 00 00 00 00 ) 
  .custom instance void [mscorlib]System.Reflection.AssemblyTrademarkAttribute::.ctor(string) = ( 01 00 00 00 00 ) 
  .custom instance void [mscorlib]System.Reflection.AssemblyCopyrightAttribute::.ctor(string) = ( 01 00 12 43 6F 70 79 72 69 67 68 74 20 C2 A9 20   // ...Copyright .. 
                                                                                                  20 32 30 30 37 00 00 )                            //  2007..
  .custom instance void [mscorlib]System.Reflection.AssemblyProductAttribute::.ctor(string) = ( 01 00 02 46 50 00 00 )                            // ...FP..
  .custom instance void [mscorlib]System.Reflection.AssemblyCompanyAttribute::.ctor(string) = ( 01 00 00 00 00 ) 
  .custom instance void [mscorlib]System.Reflection.AssemblyConfigurationAttribute::.ctor(string) = ( 01 00 00 00 00 ) 
  .custom instance void [mscorlib]System.Reflection.AssemblyDescriptionAttribute::.ctor(string) = ( 01 00 00 00 00 ) 
  .custom instance void [mscorlib]System.Reflection.AssemblyTitleAttribute::.ctor(string) = ( 01 00 02 46 50 00 00 )                            // ...FP..

  // --- The following custom attribute is added automatically, do not uncomment -------
  //  .custom instance void [mscorlib]System.Diagnostics.DebuggableAttribute::.ctor(valuetype [mscorlib]System.Diagnostics.DebuggableAttribute/DebuggingModes) = ( 01 00 07 01 00 00 00 00 ) 

  .custom instance void [mscorlib]System.Runtime.CompilerServices.CompilationRelaxationsAttribute::.ctor(int32) = ( 01 00 08 00 00 00 00 00 ) 
  .custom instance void [mscorlib]System.Runtime.CompilerServices.RuntimeCompatibilityAttribute::.ctor() = ( 01 00 01 00 54 02 16 57 72 61 70 4E 6F 6E 45 78   // ....T..WrapNonEx
                                                                                                             63 65 70 74 69 6F 6E 54 68 72 6F 77 73 01 )       // ceptionThrows.
  .hash algorithm 0x00008004
  .ver 1:0:0:0
}
.module FP.dll
// MVID: {C76DA155-C4D3-4F9E-B384-22A902022C23}
.imagebase 0x00400000
.file alignment 0x00001000
.stackreserve 0x00100000
.subsystem 0x0003       // WINDOWS_CUI
.corflags 0x00000001    //  ILONLY
// Image base: 0x013B0000


// =============== CLASS MEMBERS DECLARATION ===================

.class public auto ansi beforefieldinit FP.HttpAspCompatHandler
       extends [mscorlib]System.Object
       implements [System.Web]System.Web.IHttpHandler
{
  .method public hidebysig specialname rtspecialname 
          instance void  .ctor() cil managed
  {
    // Code size       10 (0xa)
    .maxstack  8
    .language '{3F5162F8-07C6-11D3-9053-00C04FA302A1}', '{994B45C4-E6E9-11D2-903F-00C04FA302A1}', '{5A869D0B-6611-11D3-BD2A-0000F80849BD}'
    .line 14,14 : 9,38 'D:\\code2-vss\\Products\\Src\\FP\\HttpAspCompatHandler.cs'
    IL_0000:  ldarg.0
    IL_0001:  call       instance void [mscorlib]System.Object::.ctor()
    IL_0006:  nop
    .line 15,15 : 9,10 ''
    IL_0007:  nop
    .line 16,16 : 9,10 ''
    IL_0008:  nop
    IL_0009:  ret
  } // end of method HttpAspCompatHandler::.ctor

  .method public hidebysig newslot virtual final 
          instance void  ProcessRequest(class [System.Web]System.Web.HttpContext context) cil managed
  {
    // Code size       188 (0xbc)
    .maxstack  3
    .locals init ([0] class [System.Web]System.Web.HttpServerUtility Server,
             [1] class [System.Web]System.Web.HttpRequest Request,
             [2] class [System.Web]System.Web.HttpResponse Response,
             [3] class [System.Web]System.Web.SessionState.HttpSessionState Session,
             [4] int32 index,
             [5] string ExecutionType,
             [6] string path,
             [7] bool CS$4$0000,
             [8] string CS$4$0001)
    .line 23,23 : 9,10 ''
    IL_0000:  nop
    .line 24,24 : 13,55 ''
    IL_0001:  ldarg.1
    IL_0002:  callvirt   instance class [System.Web]System.Web.HttpServerUtility [System.Web]System.Web.HttpContext::get_Server()
    IL_0007:  stloc.0
    .line 25,25 : 13,51 ''
    IL_0008:  ldarg.1
    IL_0009:  callvirt   instance class [System.Web]System.Web.HttpRequest [System.Web]System.Web.HttpContext::get_Request()
    IL_000e:  stloc.1
    .line 26,26 : 13,54 ''
    IL_000f:  ldarg.1
    IL_0010:  callvirt   instance class [System.Web]System.Web.HttpResponse [System.Web]System.Web.HttpContext::get_Response()
    IL_0015:  stloc.2
    .line 27,27 : 13,56 ''
    IL_0016:  ldarg.1
    IL_0017:  callvirt   instance class [System.Web]System.Web.SessionState.HttpSessionState [System.Web]System.Web.HttpContext::get_Session()
    IL_001c:  stloc.3
    .line 28,28 : 13,27 ''
    IL_001d:  ldc.i4.0
    IL_001e:  stloc.s    index
    .line 29,29 : 13,41 ''
    IL_0020:  ldnull
    IL_0021:  stloc.s    ExecutionType
    .line 30,30 : 13,60 ''
    IL_0023:  ldloc.1
    IL_0024:  callvirt   instance string [System.Web]System.Web.HttpRequest::get_CurrentExecutionFilePath()
    IL_0029:  stloc.s    path
    .line 31,31 : 13,52 ''
    IL_002b:  ldloc.s    path
    IL_002d:  callvirt   instance string [mscorlib]System.String::ToUpper()
    IL_0032:  ldstr      ".ASP"
    IL_0037:  callvirt   instance int32 [mscorlib]System.String::IndexOf(string)
    IL_003c:  stloc.s    index
    .line 32,32 : 13,27 ''
    IL_003e:  ldloc.s    index
    IL_0040:  ldc.i4.0
    IL_0041:  cgt
    IL_0043:  ldc.i4.0
    IL_0044:  ceq
    IL_0046:  stloc.s    CS$4$0000
    IL_0048:  ldloc.s    CS$4$0000
    IL_004a:  brtrue.s   IL_0053

    .line 33,33 : 17,39 ''
    IL_004c:  ldstr      "ASP"
    IL_0051:  stloc.s    ExecutionType
    .line 35,35 : 13,52 ''
    IL_0053:  ldloc.s    path
    IL_0055:  callvirt   instance string [mscorlib]System.String::ToUpper()
    IL_005a:  ldstr      ".CSS"
    IL_005f:  callvirt   instance int32 [mscorlib]System.String::IndexOf(string)
    IL_0064:  stloc.s    index
    .line 36,36 : 13,27 ''
    IL_0066:  ldloc.s    index
    IL_0068:  ldc.i4.0
    IL_0069:  cgt
    IL_006b:  ldc.i4.0
    IL_006c:  ceq
    IL_006e:  stloc.s    CS$4$0000
    IL_0070:  ldloc.s    CS$4$0000
    IL_0072:  brtrue.s   IL_007b

    .line 37,37 : 17,39 ''
    IL_0074:  ldstr      "CSS"
    IL_0079:  stloc.s    ExecutionType
    .line 40,40 : 13,35 ''
    IL_007b:  ldloc.s    ExecutionType
    IL_007d:  stloc.s    CS$4$0001
    IL_007f:  ldloc.s    CS$4$0001
    IL_0081:  brfalse.s  IL_00b9

    IL_0083:  ldloc.s    CS$4$0001
    IL_0085:  ldstr      "ASP"
    IL_008a:  call       bool [mscorlib]System.String::op_Equality(string,
                                                                   string)
    IL_008f:  brtrue.s   IL_00a1

    IL_0091:  ldloc.s    CS$4$0001
    IL_0093:  ldstr      "CSS"
    IL_0098:  call       bool [mscorlib]System.String::op_Equality(string,
                                                                   string)
    IL_009d:  brtrue.s   IL_00ad

    IL_009f:  br.s       IL_00b9

    .line 43,43 : 21,47 ''
    IL_00a1:  ldarg.0
    IL_00a2:  ldloc.s    path
    IL_00a4:  ldarg.1
    IL_00a5:  call       instance void FP.HttpAspCompatHandler::AspHandler(string,
                                                                           class [System.Web]System.Web.HttpContext)
    IL_00aa:  nop
    .line 44,44 : 21,27 ''
    IL_00ab:  br.s       IL_00bb

    .line 46,46 : 21,47 ''
    IL_00ad:  ldarg.0
    IL_00ae:  ldloc.s    path
    IL_00b0:  ldarg.1
    IL_00b1:  call       instance void FP.HttpAspCompatHandler::CssHandler(string,
                                                                           class [System.Web]System.Web.HttpContext)
    IL_00b6:  nop
    .line 47,47 : 21,27 ''
    IL_00b7:  br.s       IL_00bb

    .line 49,49 : 21,27 ''
    IL_00b9:  br.s       IL_00bb

    .line 53,53 : 9,10 ''
    IL_00bb:  ret
  } // end of method HttpAspCompatHandler::ProcessRequest

  .method public hidebysig instance void 
          AspHandler(string Path,
                     class [System.Web]System.Web.HttpContext context) cil managed
  {
    // Code size       69 (0x45)
    .maxstack  3
    .locals init ([0] class [System.Web]System.Web.HttpServerUtility Server,
             [1] class [System.Web]System.Web.HttpResponse Response,
             [2] class [mscorlib]System.Text.StringBuilder sb,
             [3] class [mscorlib]System.IO.StringWriter sw)
    .line 55,55 : 9,10 ''
    IL_0000:  nop
    .line 56,56 : 13,55 ''
    IL_0001:  ldarg.2
    IL_0002:  callvirt   instance class [System.Web]System.Web.HttpServerUtility [System.Web]System.Web.HttpContext::get_Server()
    IL_0007:  stloc.0
    .line 57,57 : 13,54 ''
    IL_0008:  ldarg.2
    IL_0009:  callvirt   instance class [System.Web]System.Web.HttpResponse [System.Web]System.Web.HttpContext::get_Response()
    IL_000e:  stloc.1
    .line 58,58 : 13,50 ''
    IL_000f:  ldarg.1
    IL_0010:  ldstr      ".asp"
    IL_0015:  ldstr      ".aspx"
    IL_001a:  callvirt   instance string [mscorlib]System.String::Replace(string,
                                                                          string)
    IL_001f:  starg.s    Path
    .line 59,59 : 13,52 ''
    IL_0021:  newobj     instance void [mscorlib]System.Text.StringBuilder::.ctor()
    IL_0026:  stloc.2
    .line 60,60 : 13,52 ''
    IL_0027:  ldloc.2
    IL_0028:  newobj     instance void [mscorlib]System.IO.StringWriter::.ctor(class [mscorlib]System.Text.StringBuilder)
    IL_002d:  stloc.3
    .line 61,61 : 13,38 ''
    IL_002e:  ldloc.0
    IL_002f:  ldarg.1
    IL_0030:  ldloc.3
    IL_0031:  callvirt   instance void [System.Web]System.Web.HttpServerUtility::Execute(string,
                                                                                         class [mscorlib]System.IO.TextWriter)
    IL_0036:  nop
    .line 62,62 : 13,43 ''
    IL_0037:  ldloc.1
    IL_0038:  ldloc.2
    IL_0039:  callvirt   instance string [mscorlib]System.Object::ToString()
    IL_003e:  callvirt   instance void [System.Web]System.Web.HttpResponse::Write(string)
    IL_0043:  nop
    .line 63,63 : 9,10 ''
    IL_0044:  ret
  } // end of method HttpAspCompatHandler::AspHandler

  .method public hidebysig instance void 
          CssHandler(string Path,
                     class [System.Web]System.Web.HttpContext context) cil managed
  {
    // Code size       496 (0x1f0)
    .maxstack  3
    .locals init ([0] class [System.Web]System.Web.HttpRequest Request,
             [1] class [System.Web]System.Web.HttpServerUtility Server,
             [2] class [System.Web]System.Web.HttpCookie cookie,
             [3] class [System.Web]System.Web.HttpResponse Response,
             [4] string CookieXml,
             [5] string InstID,
             [6] string physicalPath,
             [7] string institutionPath,
             [8] string institutionDefaultPath,
             [9] class [System.Xml]System.Xml.XmlDocument doc,
             [10] bool CS$4$0000)
    .line 65,65 : 9,10 ''
    IL_0000:  nop
    .line 66,66 : 13,51 ''
    IL_0001:  ldarg.2
    IL_0002:  callvirt   instance class [System.Web]System.Web.HttpRequest [System.Web]System.Web.HttpContext::get_Request()
    IL_0007:  stloc.0
    .line 67,67 : 13,55 ''
    IL_0008:  ldarg.2
    IL_0009:  callvirt   instance class [System.Web]System.Web.HttpServerUtility [System.Web]System.Web.HttpContext::get_Server()
    IL_000e:  stloc.1
    .line 68,68 : 13,64 ''
    IL_000f:  ldloc.0
    IL_0010:  callvirt   instance class [System.Web]System.Web.HttpCookieCollection [System.Web]System.Web.HttpRequest::get_Cookies()
    IL_0015:  ldstr      "TASSOCookie"
    IL_001a:  callvirt   instance class [System.Web]System.Web.HttpCookie [System.Web]System.Web.HttpCookieCollection::get_Item(string)
    IL_001f:  stloc.2
    .line 69,69 : 13,54 ''
    IL_0020:  ldarg.2
    IL_0021:  callvirt   instance class [System.Web]System.Web.HttpResponse [System.Web]System.Web.HttpContext::get_Response()
    IL_0026:  stloc.3
    .line 70,70 : 13,37 ''
    IL_0027:  ldnull
    IL_0028:  stloc.s    CookieXml
    .line 71,71 : 13,34 ''
    IL_002a:  ldnull
    IL_002b:  stloc.s    InstID
    .line 72,72 : 13,61 ''
    IL_002d:  ldloc.1
    IL_002e:  ldstr      "/Shared"
    IL_0033:  callvirt   instance string [System.Web]System.Web.HttpServerUtility::MapPath(string)
    IL_0038:  stloc.s    physicalPath
    .line 73,73 : 13,43 ''
    IL_003a:  ldnull
    IL_003b:  stloc.s    institutionPath
    .line 74,74 : 13,50 ''
    IL_003d:  ldnull
    IL_003e:  stloc.s    institutionDefaultPath
    .line 75,75 : 13,96 ''
    IL_0040:  ldstr      "/shared/inst_default/avp/{0}"
    IL_0045:  ldstr      "t1wf.css"
    IL_004a:  call       string [mscorlib]System.String::Format(string,
                                                                object)
    IL_004f:  stloc.s    institutionDefaultPath
    .line 77,77 : 13,32 ''
    IL_0051:  ldloc.2
    IL_0052:  ldnull
    IL_0053:  ceq
    IL_0055:  stloc.s    CS$4$0000
    IL_0057:  ldloc.s    CS$4$0000
    IL_0059:  brtrue     IL_010c

    .line 78,78 : 13,14 ''
    IL_005e:  nop
    .line 79,79 : 17,78 ''
    IL_005f:  ldloc.2
    IL_0060:  ldstr      "cookieXMLString"
    IL_0065:  callvirt   instance string [System.Web]System.Web.HttpCookie::get_Item(string)
    IL_006a:  call       string [System.Web]System.Web.HttpUtility::UrlDecode(string)
    IL_006f:  stloc.s    CookieXml
    .line 80,80 : 17,53 ''
    IL_0071:  newobj     instance void [System.Xml]System.Xml.XmlDocument::.ctor()
    IL_0076:  stloc.s    doc
    .line 81,81 : 17,58 ''
    IL_0078:  ldloc.s    CookieXml
    IL_007a:  brfalse.s  IL_008d

    IL_007c:  ldloc.s    CookieXml
    IL_007e:  ldstr      ""
    IL_0083:  call       bool [mscorlib]System.String::op_Equality(string,
                                                                   string)
    IL_0088:  ldc.i4.0
    IL_0089:  ceq
    IL_008b:  br.s       IL_008e

    IL_008d:  ldc.i4.0
    IL_008e:  stloc.s    CS$4$0000
    IL_0090:  ldloc.s    CS$4$0000
    IL_0092:  brtrue.s   IL_0099

    .line 82,82 : 21,28 ''
    IL_0094:  br         IL_01ef

    .line 83,83 : 17,40 ''
    IL_0099:  ldloc.s    doc
    IL_009b:  ldloc.s    CookieXml
    IL_009d:  callvirt   instance void [System.Xml]System.Xml.XmlDocument::LoadXml(string)
    IL_00a2:  nop
    .line 84,84 : 17,70 ''
    IL_00a3:  ldloc.s    doc
    IL_00a5:  ldstr      "//inst_id"
    IL_00aa:  callvirt   instance class [System.Xml]System.Xml.XmlNode [System.Xml]System.Xml.XmlNode::SelectSingleNode(string)
    IL_00af:  callvirt   instance string [System.Xml]System.Xml.XmlNode::get_InnerText()
    IL_00b4:  stloc.s    InstID
    .line 85,85 : 17,90 ''
    IL_00b6:  ldstr      "/inst_{0}/avp/{1}"
    IL_00bb:  ldloc.s    InstID
    IL_00bd:  ldstr      "t1wf.css"
    IL_00c2:  call       string [mscorlib]System.String::Format(string,
                                                                object,
                                                                object)
    IL_00c7:  stloc.s    institutionPath
    .line 86,86 : 17,63 ''
    IL_00c9:  ldloc.s    physicalPath
    IL_00cb:  ldloc.s    institutionPath
    IL_00cd:  call       string [mscorlib]System.String::Concat(string,
                                                                string)
    IL_00d2:  stloc.s    physicalPath
    .line 87,87 : 17,63 ''
    IL_00d4:  ldstr      "/shared"
    IL_00d9:  ldloc.s    institutionPath
    IL_00db:  call       string [mscorlib]System.String::Concat(string,
                                                                string)
    IL_00e0:  stloc.s    institutionPath
    .line 88,88 : 17,47 ''
    IL_00e2:  ldloc.s    physicalPath
    IL_00e4:  call       bool [mscorlib]System.IO.File::Exists(string)
    IL_00e9:  ldc.i4.0
    IL_00ea:  ceq
    IL_00ec:  stloc.s    CS$4$0000
    IL_00ee:  ldloc.s    CS$4$0000
    IL_00f0:  brtrue.s   IL_00fd

    .line 88,88 : 48,83 ''
    IL_00f2:  ldloc.3
    IL_00f3:  ldloc.s    institutionPath
    IL_00f5:  callvirt   instance void [System.Web]System.Web.HttpResponse::Redirect(string)
    IL_00fa:  nop
    IL_00fb:  br.s       IL_0106

    .line 89,89 : 22,64 ''
    IL_00fd:  ldloc.3
    IL_00fe:  ldloc.s    institutionDefaultPath
    IL_0100:  callvirt   instance void [System.Web]System.Web.HttpResponse::Redirect(string)
    IL_0105:  nop
    .line 90,90 : 13,14 ''
    IL_0106:  nop
    IL_0107:  br         IL_01ef

    .line 91,91 : 18,171 ''
    IL_010c:  ldloc.0
    IL_010d:  callvirt   instance class [System.Web]System.Web.HttpCookieCollection [System.Web]System.Web.HttpRequest::get_Cookies()
    IL_0112:  ldstr      "LoginState"
    IL_0117:  callvirt   instance class [System.Web]System.Web.HttpCookie [System.Web]System.Web.HttpCookieCollection::get_Item(string)
    IL_011c:  brfalse.s  IL_0168

    IL_011e:  ldloc.0
    IL_011f:  callvirt   instance class [System.Web]System.Web.HttpCookieCollection [System.Web]System.Web.HttpRequest::get_Cookies()
    IL_0124:  ldstr      "LoginState"
    IL_0129:  callvirt   instance class [System.Web]System.Web.HttpCookie [System.Web]System.Web.HttpCookieCollection::get_Item(string)
    IL_012e:  ldstr      "InstID"
    IL_0133:  callvirt   instance string [System.Web]System.Web.HttpCookie::get_Item(string)
    IL_0138:  brfalse.s  IL_0168

    IL_013a:  ldloc.0
    IL_013b:  callvirt   instance class [System.Web]System.Web.HttpCookieCollection [System.Web]System.Web.HttpRequest::get_Cookies()
    IL_0140:  ldstr      "LoginState"
    IL_0145:  callvirt   instance class [System.Web]System.Web.HttpCookie [System.Web]System.Web.HttpCookieCollection::get_Item(string)
    IL_014a:  ldstr      "InstID"
    IL_014f:  callvirt   instance string [System.Web]System.Web.HttpCookie::get_Item(string)
    IL_0154:  callvirt   instance string [mscorlib]System.Object::ToString()
    IL_0159:  ldstr      ""
    IL_015e:  call       bool [mscorlib]System.String::op_Inequality(string,
                                                                     string)
    IL_0163:  ldc.i4.0
    IL_0164:  ceq
    IL_0166:  br.s       IL_0169

    IL_0168:  ldc.i4.1
    IL_0169:  stloc.s    CS$4$0000
    IL_016b:  ldloc.s    CS$4$0000
    IL_016d:  brtrue.s   IL_01e4

    .line 92,92 : 13,14 ''
    IL_016f:  nop
    .line 93,93 : 17,77 ''
    IL_0170:  ldloc.0
    IL_0171:  callvirt   instance class [System.Web]System.Web.HttpCookieCollection [System.Web]System.Web.HttpRequest::get_Cookies()
    IL_0176:  ldstr      "LoginState"
    IL_017b:  callvirt   instance class [System.Web]System.Web.HttpCookie [System.Web]System.Web.HttpCookieCollection::get_Item(string)
    IL_0180:  ldstr      "InstID"
    IL_0185:  callvirt   instance string [System.Web]System.Web.HttpCookie::get_Item(string)
    IL_018a:  callvirt   instance string [mscorlib]System.Object::ToString()
    IL_018f:  stloc.s    InstID
    .line 94,94 : 17,90 ''
    IL_0191:  ldstr      "/inst_{0}/avp/{1}"
    IL_0196:  ldloc.s    InstID
    IL_0198:  ldstr      "t1wf.css"
    IL_019d:  call       string [mscorlib]System.String::Format(string,
                                                                object,
                                                                object)
    IL_01a2:  stloc.s    institutionPath
    .line 95,95 : 17,63 ''
    IL_01a4:  ldloc.s    physicalPath
    IL_01a6:  ldloc.s    institutionPath
    IL_01a8:  call       string [mscorlib]System.String::Concat(string,
                                                                string)
    IL_01ad:  stloc.s    physicalPath
    .line 96,96 : 17,63 ''
    IL_01af:  ldstr      "/shared"
    IL_01b4:  ldloc.s    institutionPath
    IL_01b6:  call       string [mscorlib]System.String::Concat(string,
                                                                string)
    IL_01bb:  stloc.s    institutionPath
    .line 97,97 : 17,47 ''
    IL_01bd:  ldloc.s    physicalPath
    IL_01bf:  call       bool [mscorlib]System.IO.File::Exists(string)
    IL_01c4:  ldc.i4.0
    IL_01c5:  ceq
    IL_01c7:  stloc.s    CS$4$0000
    IL_01c9:  ldloc.s    CS$4$0000
    IL_01cb:  brtrue.s   IL_01d8

    .line 97,97 : 48,83 ''
    IL_01cd:  ldloc.3
    IL_01ce:  ldloc.s    institutionPath
    IL_01d0:  callvirt   instance void [System.Web]System.Web.HttpResponse::Redirect(string)
    IL_01d5:  nop
    IL_01d6:  br.s       IL_01e1

    .line 98,98 : 22,64 ''
    IL_01d8:  ldloc.3
    IL_01d9:  ldloc.s    institutionDefaultPath
    IL_01db:  callvirt   instance void [System.Web]System.Web.HttpResponse::Redirect(string)
    IL_01e0:  nop
    .line 102,102 : 13,14 ''
    IL_01e1:  nop
    IL_01e2:  br.s       IL_01ef

    .line 104,104 : 13,14 ''
    IL_01e4:  nop
    .line 105,105 : 17,59 ''
    IL_01e5:  ldloc.3
    IL_01e6:  ldloc.s    institutionDefaultPath
    IL_01e8:  callvirt   instance void [System.Web]System.Web.HttpResponse::Redirect(string)
    IL_01ed:  nop
    .line 106,106 : 13,14 ''
    IL_01ee:  nop
    .line 108,108 : 9,10 ''
    IL_01ef:  ret
  } // end of method HttpAspCompatHandler::CssHandler

  .method public hidebysig newslot specialname virtual final 
          instance bool  get_IsReusable() cil managed
  {
    // Code size       7 (0x7)
    .maxstack  1
    .locals init ([0] bool CS$1$0000)
    .line 113,113 : 13,14 ''
    IL_0000:  nop
    .line 115,115 : 17,29 ''
    IL_0001:  ldc.i4.1
    IL_0002:  stloc.0
    IL_0003:  br.s       IL_0005

    .line 116,116 : 13,14 ''
    IL_0005:  ldloc.0
    IL_0006:  ret
  } // end of method HttpAspCompatHandler::get_IsReusable

  .property instance bool IsReusable()
  {
    .get instance bool FP.HttpAspCompatHandler::get_IsReusable()
  } // end of property HttpAspCompatHandler::IsReusable
} // end of class FP.HttpAspCompatHandler


// =============================================================

// *********** DISASSEMBLY COMPLETE ***********************
// WARNING: Created Win32 resource file z:\sample.res
 ×´ö«·M5ÓP¼Ó^A:pö