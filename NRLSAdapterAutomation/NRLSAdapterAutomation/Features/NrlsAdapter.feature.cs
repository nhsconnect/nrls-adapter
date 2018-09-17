﻿// ------------------------------------------------------------------------------
//  <auto-generated>
//      This code was generated by SpecFlow (http://www.specflow.org/).
//      SpecFlow Version:2.4.0.0
//      SpecFlow Generator Version:2.4.0.0
// 
//      Changes to this file may cause incorrect behavior and will be lost if
//      the code is regenerated.
//  </auto-generated>
// ------------------------------------------------------------------------------
#region Designer generated code
#pragma warning disable
namespace NRLSAdapterAutomation.Features
{
    using TechTalk.SpecFlow;
    
    
    [System.CodeDom.Compiler.GeneratedCodeAttribute("TechTalk.SpecFlow", "2.4.0.0")]
    [System.Runtime.CompilerServices.CompilerGeneratedAttribute()]
    [NUnit.Framework.TestFixtureAttribute()]
    [NUnit.Framework.DescriptionAttribute("NrlsAdapter")]
    public partial class NrlsAdapterFeature
    {
        
        private TechTalk.SpecFlow.ITestRunner testRunner;
        
#line 1 "NrlsAdapter.feature"
#line hidden
        
        [NUnit.Framework.OneTimeSetUpAttribute()]
        public virtual void FeatureSetup()
        {
            testRunner = TechTalk.SpecFlow.TestRunnerManager.GetTestRunner();
            TechTalk.SpecFlow.FeatureInfo featureInfo = new TechTalk.SpecFlow.FeatureInfo(new System.Globalization.CultureInfo("en-US"), "NrlsAdapter", null, ProgrammingLanguage.CSharp, ((string[])(null)));
            testRunner.OnFeatureStart(featureInfo);
        }
        
        [NUnit.Framework.OneTimeTearDownAttribute()]
        public virtual void FeatureTearDown()
        {
            testRunner.OnFeatureEnd();
            testRunner = null;
        }
        
        [NUnit.Framework.SetUpAttribute()]
        public virtual void TestInitialize()
        {
        }
        
        [NUnit.Framework.TearDownAttribute()]
        public virtual void ScenarioTearDown()
        {
            testRunner.OnScenarioEnd();
        }
        
        public virtual void ScenarioInitialize(TechTalk.SpecFlow.ScenarioInfo scenarioInfo)
        {
            testRunner.OnScenarioInitialize(scenarioInfo);
            testRunner.ScenarioContext.ScenarioContainer.RegisterInstanceAs<NUnit.Framework.TestContext>(NUnit.Framework.TestContext.CurrentContext);
        }
        
        public virtual void ScenarioStart()
        {
            testRunner.OnScenarioStart();
        }
        
        public virtual void ScenarioCleanup()
        {
            testRunner.CollectScenarioErrors();
        }
        
        [NUnit.Framework.TestAttribute()]
        [NUnit.Framework.DescriptionAttribute("Check API response from NRLS Adapter giving an NHS Number")]
        [NUnit.Framework.CategoryAttribute("mytag")]
        [NUnit.Framework.TestCaseAttribute("pointers", "9462205957", "200", "None", null)]
        [NUnit.Framework.TestCaseAttribute("pointers", "9462206031", "200", "None", null)]
        [NUnit.Framework.TestCaseAttribute("pointers", "9462205965", "200", "None", null)]
        [NUnit.Framework.TestCaseAttribute("pointers", "9462205655", "400", "400 Invalid NHS Number", null)]
        [NUnit.Framework.TestCaseAttribute("pointers", "9462205671", "400", "400 Invalid Parameter", null)]
        [NUnit.Framework.TestCaseAttribute("pointers", "9462205833", "400", "400 Missing Or Invalid Header", null)]
        [NUnit.Framework.TestCaseAttribute("pointers", "9462205701", "404", "404 No Record Found", null)]
        [NUnit.Framework.TestCaseAttribute("pointers", "9462205841", "415", "415 Unsupported Media Type", null)]
        [NUnit.Framework.TestCaseAttribute("count", "9462205922", "200", "None", null)]
        [NUnit.Framework.TestCaseAttribute("count", "9462205981", "200", "None", null)]
        [NUnit.Framework.TestCaseAttribute("count", "9462205655", "400", "400 Invalid NHS Number", null)]
        [NUnit.Framework.TestCaseAttribute("count", "9462205671", "400", "400 Invalid Parameter", null)]
        [NUnit.Framework.TestCaseAttribute("count", "9462205833", "400", "400 Missing Or Invalid Header", null)]
        [NUnit.Framework.TestCaseAttribute("count", "9462205701", "404", "404 No Record Found", null)]
        [NUnit.Framework.TestCaseAttribute("count", "9462205841", "415", "415 Unsupported Media Type", null)]
        public virtual void CheckAPIResponseFromNRLSAdapterGivingAnNHSNumber(string endPoint, string nhsNumber, string responseCode, string responseMessage, string[] exampleTags)
        {
            string[] @__tags = new string[] {
                    "mytag"};
            if ((exampleTags != null))
            {
                @__tags = System.Linq.Enumerable.ToArray(System.Linq.Enumerable.Concat(@__tags, exampleTags));
            }
            TechTalk.SpecFlow.ScenarioInfo scenarioInfo = new TechTalk.SpecFlow.ScenarioInfo("Check API response from NRLS Adapter giving an NHS Number", null, @__tags);
#line 4
this.ScenarioInitialize(scenarioInfo);
            this.ScenarioStart();
#line 5
 testRunner.Given(string.Format("I have a {0} and {1}", endPoint, nhsNumber), ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "Given ");
#line 6
 testRunner.When("I call the the API", ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "When ");
#line 7
 testRunner.Then(string.Format("reponse will match the expected {0} and {1}", responseCode, responseMessage), ((string)(null)), ((TechTalk.SpecFlow.Table)(null)), "Then ");
#line hidden
            this.ScenarioCleanup();
        }
    }
}
#pragma warning restore
#endregion
