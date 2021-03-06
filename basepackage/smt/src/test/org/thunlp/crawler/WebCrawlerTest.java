package org.thunlp.crawler;

import java.io.UnsupportedEncodingException;
import junit.framework.Assert;
import junit.framework.TestCase;

public class WebCrawlerTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testBasicCrawling() throws UnsupportedEncodingException {
		StubCrawlerListener listener = new StubCrawlerListener();
		WebCrawler wc = new WebCrawler(listener);
		String[] urls = { "http://www.baidu.com", "http://www.google.cn" };
		wc.start();
		String shit = "shit";
		wc.schedule(urls[0], null, (Object) shit);
		wc.schedule(urls[1], null, (Object) shit);
		wc.waitForAll(300 * 1000);
		Assert.assertEquals(2, listener.nSuccess);
		Assert.assertNotNull(listener.crawledContent);
		Assert.assertNotNull(listener.crawledUrl);
		Assert.assertNotNull(listener.crawledIp);
		String pageContent = null;
		if (listener.crawledUrl.equals(urls[0])) {
			pageContent = new String(listener.crawledContent, "gb2312");
			Assert.assertTrue(pageContent.contains("百度"));
		} else if (listener.crawledUrl.equals(urls[1])) {
			pageContent = new String(listener.crawledContent, "utf8");
			System.out.println(pageContent);
			Assert.assertTrue(pageContent.contains("Google"));
		} else {
			Assert.fail("page not crawled correctly");
		}
		Assert.assertNotNull(listener.customData);
		Assert.assertEquals(String.class, listener.customData.getClass());
	}

	public void testWaitCrawling() throws UnsupportedEncodingException {
		StubCrawlerListener listener = new StubCrawlerListener();
		WebCrawler wc = new WebCrawler(listener);
		String url = "http://www.baidu.com";
		// String url = "http://localhost/test.html";
		wc.start();
		byte[] data = wc.scheduleAndWait(url, null);
		Assert.assertNotNull(data);
		String pageContent = new String(data, "gb2312");
		Assert.assertTrue(pageContent.contains("百度"));
		wc.waitForAll(300 * 1000);
	}
}
