using com.antlersoft.BBQClient;
using Microsoft.VisualStudio.Shell;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Text;
using System.Threading.Tasks;

namespace BBQVSIX19
{
    public class BbqOptions : DialogPage, IBbqConfig
    {
        public bool UseLegacyService { get; set; }
        public string WebServiceUrl { get; set; }

        public string UserName { get; set; }

        public string ApiKey { get; set; }

        [DisplayName("Default Database")]
        public string DefaultDatabase { get; set; }

        public List<Substitution> Substitutions { get; set; } = new List<Substitution>();
        public List<string> SourcelessSearchStartPoints { get; set; } = new List<string>();
    }
}
