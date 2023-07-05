import java.awt.image.BufferedImage

import javax.imageio.ImageIO

import org.apache.commons.io.FileUtils;

import org.openqa.selenium.OutputType
import org.openqa.selenium.WebDriver
import org.openqa.selenium.TakesScreenshot
import org.openqa.selenium.OutputType

import com.kazurayam.ashotwrapper.AShotWrapper
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import java.io.ByteArrayInputStream

BufferedImage toBufferedImage(byte[] imageBytes) throws IOException {
	try {
		ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes)
		return ImageIO.read(bais);
	} catch (IOException e) {
		throw new RuntimeException(e)
	}
}

String url = 'https://www.adm.com/en-us/insights-and-innovation/formulation-challenges/sugar-reduction/'

WebUI.openBrowser('')
WebUI.setViewPortSize(1440, 800)
WebUI.navigateToUrl(url)
WebDriver driver = DriverFactory.getWebDriver()

File png1 = new File("./sugarReduction_using_builtin_keyword.png")
WebUI.takeScreenshot(png1.toString())
BufferedImage bi1 = ImageIO.read(png1)
println "png1: width=" + bi1.getWidth() + ", height=" + bi1.getHeight() + ", size=" + png1.length()

File png2 = new File("./sugarReduction_by_ashot.png")
AShotWrapper.savePageImage(driver, png2);
BufferedImage bi2 = ImageIO.read(png2)
println "png2: width=" + bi2.getWidth() + ", height=" + bi2.getHeight() + ", size=" + png2.length()

File jpg3 = new File("./sugarReduction_by_ashot_as_jpeg.jpg")
AShotWrapper.savePageImageAsJpeg(driver, jpg3, 0.8f);
BufferedImage bi3 = ImageIO.read(jpg3)
println "jpg3: width=" + bi3.getWidth() + ", height=" + bi3.getHeight() + ", size=" + jpg3.length()

File png4 = new File("./sugarReduction_by_native_Selenium_API_OutputType.FILE.png")
TakesScreenshot scrShot4 = ((TakesScreenshot)driver)
File tmp = scrShot4.getScreenshotAs(OutputType.FILE)
FileUtils.copyFile(tmp, png4)
BufferedImage bi4 = ImageIO.read(png4)
println "png4: width=" + bi4.getWidth() + ", height=" + bi4.getHeight() + ", size=" + png4.length()

File png5 = new File("./sugarReduction_by_native_Selenium_API_OutputType.BYTEARRAY.png")
TakesScreenshot scrShot5 = ((TakesScreenshot)driver)
byte[] imageBytes = scrShot5.getScreenshotAs(OutputType.BYTES);
BufferedImage bi5 = toBufferedImage(imageBytes)
ImageIO.write(bi5, "png", png5)
println "png5: width=" + bi5.getWidth() + ", height=" + bi5.getHeight() + ", size=" + png5.length()






WebUI.closeBrowser()