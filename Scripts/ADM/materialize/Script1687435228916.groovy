import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.inspectus.materialize.discovery.Sitemap
import com.kazurayam.inspectus.materialize.discovery.SitemapLoader
import com.kazurayam.inspectus.materialize.discovery.Target
import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

/**
 * Test Cases/main/ADM/materialize
 *
 */




// check params which should be passed as the arguments of WebUI.callTestCases() call
Objects.requireNonNull(store)
Objects.requireNonNull(jobName)
Objects.requireNonNull(jobTimestamp)
Objects.requireNonNull(environment)


String executionProfile = environment.toString()
println "executionProfile=${executionProfile}"
List<Target> targetList = getTargetList(executionProfile)
WebUI.comment("targetList.size()=" + targetList.size())

WebUI.openBrowser('')
WebUI.setViewPortSize(1200, 1000)
WebUI.navigateToUrl(GlobalVariable.topPageURL)

// if required, click the "Accept Cookies" button
TestObject btn = makeTestObject("Accept Cookies", "//button[@id='onetrust-accept-btn-handler']")
if (WebUI.waitForElementPresent(btn, 10)) {
	WebUI.click(btn)
	WebUI.delay(1)
}

// then take screenshots of pages liested in the external CSV file
WebUI.callTestCase(findTestCase("Test Cases/ADM/processTargetList"),
						[
							"store": store,
							"jobName": jobName,
							"jobTimestamp": jobTimestamp,
							"targetList": targetList
						])
// done, bye.
WebUI.closeBrowser()


/**
 * function to make TestObject by XPath
 * @param id
 * @param xpath
 * @return
 */
TestObject makeTestObject(String id, String xpath) {
	TestObject tObj = new TestObject(id)
	tObj.addProperty("xpath", ConditionType.EQUALS, xpath)
	return tObj
}

/**
 * look at the Execution Profile to find a CSV file
 * where list of multiple target URLs are written
 */
List<Target> getTargetList(String executionProfile) {
	
	// utility class that loads specified Execution Profiles to make the GlobalVariable.CSV accessible
	ExecutionProfilesLoader profilesLoader = new ExecutionProfilesLoader()
	profilesLoader.loadProfile(executionProfile)
	
	WebUI.comment("GlobalVariable.topPageURL=" + GlobalVariable.topPageURL)
	WebUI.comment("GlobalVariable.CSV=" + GlobalVariable.CSV)
	
	// identify the URL of the top page
	Target topPage = Target.builder(GlobalVariable.topPageURL).build()
	
	// identify the target CSV file
	Path csvFile = Paths.get(RunConfiguration.getProjectDir()).resolve(GlobalVariable.CSV)
	
	// create an instance of Sitemap
	SitemapLoader loader = new SitemapLoader(topPage)
	loader.setWithHeaderRecord(true)
	Sitemap sitemap = loader.parseCSV(csvFile)
	
	WebUI.comment("sitemap.size()=" + sitemap.size())
	assert sitemap.size() > 0
	
	// return the list or targets
	return sitemap.getBaseTargetList()
}
