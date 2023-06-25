import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import java.nio.file.Path
import java.nio.file.Paths

import com.kazurayam.inspectus.materialize.discovery.Sitemap
import com.kazurayam.inspectus.materialize.discovery.SitemapLoader
import com.kazurayam.inspectus.materialize.discovery.Target
import com.kazurayam.ks.globalvariable.ExecutionProfilesLoader
import com.kms.katalon.core.configuration.RunConfiguration
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

WebUI.callTestCase(findTestCase("Test Cases/ADM/processTargetList"),
						[
							"store": store,
							"jobName": jobName,
							"jobTimestamp": jobTimestamp,
							"targetList": targetList
						])

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
