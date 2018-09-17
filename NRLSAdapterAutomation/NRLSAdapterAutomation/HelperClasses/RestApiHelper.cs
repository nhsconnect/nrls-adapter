using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Net;
using RestSharp;


namespace NRLSAdapterAutomation.HelperClasses
{
    public static class RestApiHelper
    {
        public static RestClient client;
        public static RestRequest restRequest;
        public static string baseUrlPointers = "http://ec2-35-178-3-184.eu-west-2.compute.amazonaws.com:8080/api/pointers";
        public static string baseUrlCount = "http://ec2-35-178-3-184.eu-west-2.compute.amazonaws.com:8080/api/pointers/count";
        public static string sessionUser = "?sessionId=7232837238473248237482&userId=234234234&nhsNumber=";

        public static RestClient SetUrl(string endPoint, string nhsNumber)
        {
            string url;
            if (endPoint == "pointers")
            {
                 url = baseUrlPointers + sessionUser + nhsNumber;
            }
            else
            {
                url = baseUrlCount + sessionUser + nhsNumber;
            }
            return client = new RestClient(url);
        }

        public static RestRequest CreateRequest()
        {
            restRequest = new RestRequest(Method.GET);
            return restRequest;
        }

        public static IRestResponse GetResponse()
        {
            return client.Execute(restRequest);
        }
    }
}
