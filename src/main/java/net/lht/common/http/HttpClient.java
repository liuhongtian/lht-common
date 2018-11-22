package net.lht.common.http;

//import java.io.BufferedReader;
import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
//import org.apache.log4j.Logger;

public class HttpClient {
	//private static Logger logger = Logger.getLogger(HttpClient.class);

	/**
	 * get
	 * 
	 * @param url
	 * @return statusCode and rspStr as String[]
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String[] get(String url) throws ClientProtocolException, IOException {
		HttpGet httpget = new HttpGet(url);
//		if (logger.isDebugEnabled()) {
//			logger.debug("executing request: " + httpget.getRequestLine());
//		}
		String rspStr = null;
		StatusLine sl = null;
		try (CloseableHttpClient httpclient = HttpClients.createDefault();
				CloseableHttpResponse response = httpclient.execute(httpget);) {
			HttpEntity entity = response.getEntity();
//			if (logger.isDebugEnabled()) {
//				logger.debug("----------------------------------------");
//			}
			sl = response.getStatusLine();
//			if (logger.isDebugEnabled()) {
//				logger.debug(sl);
//			}
			if (entity != null) {
//				if (logger.isDebugEnabled()) {
//					logger.debug("Response content length: " + entity.getContentLength());
//				}
				rspStr = EntityUtils.toString(entity);
//				if (logger.isDebugEnabled()) {
//					logger.debug(rspStr);
//				}
				EntityUtils.consume(entity);
			}
//			if (logger.isDebugEnabled()) {
//				logger.debug("----------------------------------------");
//			}
		}

		String[] rsp = { sl == null ? null : new Integer(sl.getStatusCode()).toString(), rspStr };
		return rsp;
	}

	/**
	 * post
	 * 
	 * @param url
	 * @param content
	 * @param contentType
	 * @return statusCode and rspStr as String[]
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String[] post(String url, String content, String contentType)
			throws ClientProtocolException, IOException {
		HttpPost httppost = new HttpPost(url);
		httppost.addHeader("Content-Type", contentType);
		httppost.setEntity(new StringEntity(content));

//		if (logger.isDebugEnabled()) {
//			logger.debug("executing request: " + httppost.getRequestLine());
//			logger.debug("with content: " + content);
//		}

		String rspStr = null;
		StatusLine sl = null;
		try (CloseableHttpClient httpclient = HttpClients.createDefault();
				CloseableHttpResponse response = httpclient.execute(httppost);) {
			HttpEntity entity = response.getEntity();
//			if (logger.isDebugEnabled()) {
//				logger.debug("----------------------------------------");
//			}
			sl = response.getStatusLine();
//			if (logger.isDebugEnabled()) {
//				logger.debug(sl);
//			}
			if (entity != null) {
//				if (logger.isDebugEnabled()) {
//					logger.debug("Response content length: " + entity.getContentLength());
//				}
				rspStr = EntityUtils.toString(entity);
//				if (logger.isDebugEnabled()) {
//					logger.debug(rspStr);
//				}
				EntityUtils.consume(entity);
			}
//			if (logger.isDebugEnabled()) {
//				logger.debug("----------------------------------------");
//			}
			// response.close();
			// httpclient.close();
		}

		String[] rsp = { sl == null ? null : new Integer(sl.getStatusCode()).toString(), rspStr };
		return rsp;
	}

	/**
	 * post multipart
	 * 
	 * @param url
	 * @param names
	 * @param contents
	 * @param contentTypes
	 * @return statusCode and rspStr as String[]
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String[] postMultipart(String url, String[] names, String contents[], String[] contentTypes)
			throws ClientProtocolException, IOException {
		HttpPost httppost = new HttpPost(url);
		httppost.addHeader("Content-Type", "multipart/form-data");

		MultipartEntityBuilder mb = MultipartEntityBuilder.create();
		for (int i = 0; i < names.length; i++) {
			mb = mb.addPart(names[i], new StringBody(contents[i], ContentType.create(contentTypes[i], Consts.UTF_8)));
		}

		HttpEntity reqEntity = mb.build();
		httppost.setEntity(reqEntity);

//		if (logger.isDebugEnabled()) {
//			logger.debug("executing request: " + httppost.getRequestLine());
//			logger.debug("with content: ");
//			try (InputStream content = reqEntity.getContent();) {
//				BufferedReader br = new BufferedReader(new InputStreamReader(content));
//				String line = null;
//				while ((line = br.readLine()) != null) {
//					logger.debug(line);
//				}
//			}
//		}

		String rspStr = null;
		StatusLine sl = null;
		try (CloseableHttpClient httpclient = HttpClients.createDefault();
				CloseableHttpResponse response = httpclient.execute(httppost);) {
			HttpEntity entity = response.getEntity();
//			if (logger.isDebugEnabled()) {
//				logger.debug("----------------------------------------");
//			}
			sl = response.getStatusLine();
//			if (logger.isDebugEnabled()) {
//				logger.debug(sl);
//			}
			if (entity != null) {
//				if (logger.isDebugEnabled()) {
//					logger.debug("Response content length: " + entity.getContentLength());
//				}
				rspStr = EntityUtils.toString(entity);
//				if (logger.isDebugEnabled()) {
//					logger.debug(rspStr);
//				}
				EntityUtils.consume(entity);
			}
//			if (logger.isDebugEnabled()) {
//				logger.debug("----------------------------------------");
//			}
		}

		String[] rsp = { sl == null ? null : new Integer(sl.getStatusCode()).toString(), rspStr };
		return rsp;
	}

	public static void main(String[] args) throws Exception {
		/*
		 * String[] res1 =
		 * get("http://127.0.0.1:8080/sig_web/JustTest?a=1&b=2");
		 * System.out.println("=============\n" + res1[0] + "\n=============\n"
		 * + res1[1]);
		 */
		/*
		 * String[] res2 = post("http://127.0.0.1:8080/sig_web/JustTest",
		 * "a=3&b=4", "application/x-www-form-urlencoded");
		 * System.out.println("=============\n" + res2[0] + "\n=============\n"
		 * + res2[1]);
		 */
		String[] names = { "body_a", "body_b" };
		String[] contents = { "abc", "<?xml version=\"1.0\" encoding=\"UTF-8\"?><test>aaa</test>" };
		String[] contentTypes = { "text/plain", "application/xml" };
		String[] res3 = postMultipart("http://ecs2.liuhongtian.cn:9080/", names, contents, contentTypes);
		System.out.println("=============\n" + res3[0] + "\n=============\n" + res3[1]);
	}

}