using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace com.antlersoft.BBQClient
{
    public class BbqConfig : IBbqConfig
    {
        public BbqConfig(IBbqConfig b)
        {
            UseLegacyService = b.UseLegacyService;
            WebServiceUrl = b.WebServiceUrl;
            UserName = b.UserName;
            ApiKey = b.ApiKey;
            Substitutions = b.Substitutions?.ToList() ?? new List<Substitution>();
            SourcelessSearchStartPoints = b.SourcelessSearchStartPoints?.ToList() ?? new List<string>();
        }

        public override bool Equals(object obj)
        {
            bool result = false;
            if (obj is IBbqConfig b)
            {
                if (UseLegacyService == b.UseLegacyService
                    && WebServiceUrl == b.WebServiceUrl
                    && UserName == b.UserName
                    && ApiKey == b.ApiKey
                    && Substitutions != null && b.Substitutions != null
                    && SourcelessSearchStartPoints != null && b.SourcelessSearchStartPoints != null
                    && Substitutions.Count == b.Substitutions.Count
                    && SourcelessSearchStartPoints.Count == b.SourcelessSearchStartPoints.Count)
                {
                    bool diff = false;
                    for (int i = Substitutions.Count - 1; i>=0; i--)
                    {
                        if (Substitutions[i].MatchExpression != b.Substitutions[i].MatchExpression
                            || Substitutions[i].ReplaceExpression != b.Substitutions[i].ReplaceExpression)
                        {
                            diff = true;
                            break;
                        }
                    }
                    for (int i = SourcelessSearchStartPoints.Count - 1; i>=0; i--)
                    {
                        if (SourcelessSearchStartPoints[i] != b.SourcelessSearchStartPoints[i])
                        {
                            diff = true;
                            break;
                        }
                    }
                    result = !diff;
                }
            }
            return result;
        }
        public bool UseLegacyService { get; private set; }
        public string WebServiceUrl { get; private set; }

        public string UserName { get; private set; }

        public string ApiKey { get; private set; }

        public List<Substitution> Substitutions { get; private set; } = new List<Substitution>();

        public List<string> SourcelessSearchStartPoints { get; private set; } = new List<string>();
    }
}
