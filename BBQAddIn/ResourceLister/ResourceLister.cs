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
        static void Main(string[] args)
        {
            var assembly = Assembly.LoadFrom(args[0]);
            foreach (String r in assembly.GetManifestResourceNames())
            {
                Console.WriteLine(r + "{");
                var basename = r.Substring( 0, r.Length - (".resources").Length);
                ResourceManager rm = new ResourceManager(basename, assembly);
                ResourceSet rs=rm.GetResourceSet(System.Globalization.CultureInfo.InvariantCulture,true,true);

// Create an IDictionaryEnumerator to read the data in the ResourceSet.
        IDictionaryEnumerator id = rs.GetEnumerator(); 

        // Iterate through the ResourceSet and display the contents to the console. 
        while(id.MoveNext())
          Console.WriteLine("\n[{0}] \t{1}", id.Key, id.Value); 

            }
        }
    }
}
