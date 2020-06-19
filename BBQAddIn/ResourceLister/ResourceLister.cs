using System;
using System.Collections;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using System.Resources;
using System.Linq;
using System.Text;

namespace ResourceLister
{
    class ResourceLister
    {
        static String escapeString(String s)
        {
            StringBuilder sb = new StringBuilder(s.Length+2);
            sb.Append('"');
            foreach (char ch in s)
            {
                if ( ch=='\\' || ch=='"' || (ch>=0x2018 && ch<=0x201F))
                {
                    sb.Append('\\');
                }
                sb.Append(ch);
            }
            sb.Append('"');
            return sb.ToString();
        }

        static void Main(string[] args)
        {
            var assembly = Assembly.ReflectionOnlyLoadFrom(args[0]);
            Console.WriteLine("assembly " + escapeString(assembly.FullName));
            Console.WriteLine("{");
            foreach (String r in assembly.GetManifestResourceNames())
            {
                if (r.EndsWith(".resources"))
                {
                    var basename = r.Substring(0, r.Length - (".resources").Length);
                    try
                    {
                        ResourceManager rm = new ResourceManager(basename, assembly);
                        ResourceSet rs = rm.GetResourceSet(System.Globalization.CultureInfo.InvariantCulture, true, true);
                        if (rs != null)
                        {
                            Console.WriteLine("bundle " + escapeString(basename));
                            Console.WriteLine("{");
                            // Create an IDictionaryEnumerator to read the data in the ResourceSet.
                            IDictionaryEnumerator id = rs.GetEnumerator();

                            // Iterate through the ResourceSet and display the contents to the console. 
                            while (id.MoveNext())
                                Console.WriteLine("{0} = {1}", escapeString(id.Key.ToString()), escapeString(id.Value.ToString()));
                            Console.WriteLine("}");
                        }
                    }
                    catch (Exception)
                    {
                        // Continue with next resource if possible
                    }
                }
                else if (! r.EndsWith(".zip") && ! r.EndsWith(".gz") && ! r.EndsWith(".png") && ! r.EndsWith(".jpg") && ! r.EndsWith(".bmp"))
                {
                    Stream strm = assembly.GetManifestResourceStream(r);
                    StreamReader sr = new StreamReader(strm);
                    String contents = sr.ReadToEnd();
                    var lenToCheck = Math.Max(contents.Length, 256);
                    bool badChars = false;
                    for (int i=0; i<lenToCheck && ! badChars; i++)
                    {
                        var ch = contents[i];
                        if (ch == 0 || ch > 127)
                        {
                            badChars = true;
                        }
                    }
                    if (badChars)
                    {
                        continue;
                    }
                    String basename = r;
                    int lastIndex = r.LastIndexOf('.');
                    if (lastIndex != -1)
                    {
                        String withoutExt = r.Substring(0, lastIndex);
                        lastIndex = withoutExt.LastIndexOf('.');
                        if (lastIndex != -1)
                        {
                            basename = r.Substring(lastIndex + 1);
                        }
                    }
                    sr.Close();
                    Console.WriteLine("bundle " + escapeString(r));
                    Console.WriteLine("{");
                    Console.WriteLine("{0} = {1}", escapeString(basename), escapeString(contents));
                    Console.WriteLine("}");
                }
            }
            Console.WriteLine("}");
        }
    }
}
