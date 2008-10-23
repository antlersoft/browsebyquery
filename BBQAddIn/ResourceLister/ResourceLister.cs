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
                if ( ch=='\\' || ch=='"')
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
            var assembly = Assembly.LoadFrom(args[0]);
            Console.WriteLine("assembly " + escapeString(assembly.FullName));
            Console.WriteLine("{");
            foreach (String r in assembly.GetManifestResourceNames())
            {
                if (r.EndsWith(".resources"))
                {
                    var basename = r.Substring(0, r.Length - (".resources").Length);
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
                else
                {
                    Stream strm = assembly.GetManifestResourceStream(r);
                    StreamReader sr = new StreamReader(strm);
                    String contents = sr.ReadToEnd();
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
