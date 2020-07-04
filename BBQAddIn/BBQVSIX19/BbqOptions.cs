using com.antlersoft.BBQClient;
using Microsoft.VisualStudio.Shell;
using Microsoft.VisualStudio.Shell.Interop;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Text;
using System.Threading.Tasks;

namespace BBQVSIX19
{
    public class BbqOptions : DialogPage, IBbqConfig
    {
        private List<Substitution> _substitutions;
        [DisplayName("Use Legacy Service")]
        [Description("If true, uses a local Browse-by-Query instead of Jenkins hosted web service")]
        public bool UseLegacyService { get; set; }
        
        [DisplayName("Web Service URL")]
        [Description("This is the URL of the home page of the Jenkins server hosting the BBQ databases, with /browseByQuery/query appended")]
        public string WebServiceUrl { get; set; }

        [DisplayName("User Name")]
        [Description("User name in jenkins")]
        public string UserName { get; set; }

        [DisplayName("API Key")]
        [Description("API Key used to log into Jenkins web service, obtained from user config page on Jenkins")]
        public string ApiKey { get; set; }


        [DisplayName("Default Database")]
        public string DefaultDatabase { get; set; }

        [Description("Regular expression matcher and replacement (C# regex) to convert Jenkins paths to your local dev environment")]
        public List<Substitution> Substitutions
        {
            get
            {
                if (_substitutions == null)
                {
                    if (SerializedSubstitutions == null)
                    {
                        _substitutions = new List<Substitution>();
                    }
                    else
                    {
                        _substitutions = JsonConvert.DeserializeObject<List<Substitution>>(SerializedSubstitutions);
                    }
                }
                return _substitutions;
            }
            set
            {
                _substitutions = value;
                if (_substitutions != null)
                {
                    SerializedSubstitutions = JsonConvert.SerializeObject(_substitutions);
                }
            }
        }

        [EditorBrowsable(EditorBrowsableState.Never)]
        public string SerializedSubstitutions { get; set; }

        public override void SaveSettingsToStorage()
        {
            if (_substitutions != null)
            {
                SerializedSubstitutions = JsonConvert.SerializeObject(_substitutions);
            }
            base.SaveSettingsToStorage();
        }

        public override void SaveSettingsToXml(IVsSettingsWriter writer)
        {
            if (_substitutions != null)
            {
                SerializedSubstitutions = JsonConvert.SerializeObject(_substitutions);
            }
            base.SaveSettingsToXml(writer);
        }
    }
}
