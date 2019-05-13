package org.hf.experi.core;

import java.net.URL;

import org.hf.experi.utils.PropertyFileReader;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;

public class BaseTest {

	protected static AppiumDriver driver = null;
	private String host;
	private int port;
	private String deviceName;
	private String platform;
	private int appTimeOut;
	private String reportDirectory = "reports";
	private String reportFormat = "xml";

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatfrom(String Platform) {
		this.platform = Platform;
	}

	public int getappTimeOut() {
		return appTimeOut;
	}

	public void setappTimeOut(int timeOut) {
		this.appTimeOut = timeOut;
	}

	// method for getting the driver
	public static AppiumDriver getDriver() {
		return driver;
	}

	@BeforeSuite
	public void setUp() {
		if (driver == null) {
			try {

				init();
				String remoteUrl = "http://" + getHost() + ":" + getPort() + "/wd/hub";
				if ("Android".equalsIgnoreCase(getPlatform())) {
					driver = (AppiumDriver) new AndroidDriver(new URL(remoteUrl), this.generateAndroidCapabilities());
				} else if ("iOS".equalsIgnoreCase(getPlatform())) {
					driver = (AppiumDriver) new IOSDriver(new URL(remoteUrl), this.generateiOSCapabilities());
				} else {
					throw new Exception("Given platform is not implemented.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@AfterSuite
	public void tearDown() {

	}

	// init method reads value from the properties file
	private void init() {
		PropertyFileReader handler = new PropertyFileReader("properties/Execution.properties");
		setHost(handler.getproperty("HOST_IP"));
		setPort(Integer.parseInt(handler.getproperty("HOST_PORT")));
		setDeviceName(handler.getproperty("DEVICE_NAME"));
		setPlatfrom(handler.getproperty("PLATFORM_NAME"));
		setappTimeOut(Integer.parseInt(handler.getproperty("APP_TIMEOUT")));
	}

	/**
	 * @param application
	 * @return Android capabilities
	 */
	protected DesiredCapabilities generateAndroidCapabilities() {

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.FULL_RESET, true);
		capabilities.setCapability("reportDirectory", reportDirectory);
		capabilities.setCapability("reportFormat", reportFormat);

		capabilities.setCapability("app", System.getProperty("user.dir") + "/apps/android/EriBank.apk");
		capabilities.setCapability(AndroidMobileCapabilityType.APP_WAIT_ACTIVITY,
				"com.experitest.ExperiBank.LoginActivity");

		capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, getappTimeOut());
		return capabilities;
	}

	/**
	 * @param application
	 * @return iOS capabilities
	 */
	protected DesiredCapabilities generateiOSCapabilities() {

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability(MobileCapabilityType.FULL_RESET, false); // true
		capabilities.setCapability("instrumentApp", false); // true

		capabilities.setCapability("app", System.getProperty("user.dir") + "/apps/ios/EriBank.ipa");
		capabilities.setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.experitest.ExperiBank");

		capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, getappTimeOut());
		return capabilities;
	}

}
