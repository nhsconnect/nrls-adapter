using Newtonsoft.Json.Linq;
using NUnit.Framework;
using System;
using System.Diagnostics;
using System.Net.Http;
using TechTalk.SpecFlow;

namespace NRLSAdapterAutomation.Steps
{
    [Binding]
    public class NrlsAdapterSteps
    {
        [Given(@"I have a (.*) and (.*)")]
        public void GivenIHaveAPointersAnd(string endPoint, string nhsNumber)
        {
            HelperClasses.RestApiHelper.SetUrl(endPoint, nhsNumber);
        }
        
        [When(@"I call the the API")]
        public void WhenICallTheTheAPI()
        {
            HelperClasses.RestApiHelper.CreateRequest();
        }
        
        [Then(@"reponse will match the expected (.*) and (.*)")]
        public void ThenReponseWillMatchTheExpectedAndNone(string responseCode, string responseMessage)
        {
            var apiResponse = HelperClasses.RestApiHelper.GetResponse();
            //Trace.WriteLine(apiResponse.Content);
            //JObject jsonResult = JObject.Parse(apiResponse.Content);
            //var error = jsonResult["error"];
            if (responseCode == "200")
            {
                Assert.IsTrue(apiResponse.StatusCode == System.Net.HttpStatusCode.OK);
            }
            else if (responseCode == "400")
            {
                Assert.IsTrue(apiResponse.StatusCode == System.Net.HttpStatusCode.BadRequest);
            }
            else if (responseCode == "404")
            {
                Assert.IsTrue(apiResponse.StatusCode == System.Net.HttpStatusCode.NotFound);
            }
            else if (responseCode == "415")
            {
                Assert.IsTrue(apiResponse.StatusCode == System.Net.HttpStatusCode.UnsupportedMediaType);
            }
        }
    }
}
