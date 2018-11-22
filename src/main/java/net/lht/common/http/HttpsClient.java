package net.lht.common.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
//import org.apache.log4j.Logger;

public class HttpsClient {
	//private static Logger logger = Logger.getLogger(HttpsClient.class);

	private static final String KEY_STORE_FILE = "www.bhz.com.jks";
	private static final String KEY_STORE_PASSWORD = "bhz";

	private static SSLConnectionSocketFactory scsf = null;

	static {
		try {
			scsf = initSSL(KEY_STORE_FILE, KEY_STORE_PASSWORD);
		} catch (Exception e) {
			//logger.fatal("SSLConnectionSocketFactory init faild!", e);
		}
	}

	private static SSLConnectionSocketFactory initSSL(String keyStoreFile, String password) throws Exception {
		try (InputStream in = HttpsClient.class.getClassLoader().getResourceAsStream(keyStoreFile);) {
			KeyStore keyStore = KeyStore.getInstance("jks");
			keyStore.load(in, StringUtils.isEmpty(password) ? null : password.toCharArray());
			SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(keyStore, new TrustSelfSignedStrategy())
					.loadKeyMaterial(keyStore, StringUtils.isEmpty(password) ? null : password.toCharArray()).build();
			// Allow TLSv1 protocol only
			return new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1", "TLSv1.1", "TLSv1.2" }, null,
					SSLConnectionSocketFactory.getDefaultHostnameVerifier());
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * get
	 * 
	 * @param url
	 * @return statusCode and rspStr as String[]
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String[] get(String url) throws ClientProtocolException, IOException {
		if (scsf == null) {
			//logger.fatal("SSLConnectionSocketFactory not init.");
			return null;
		}

		HttpGet httpget = new HttpGet(url);
		//logger.debug("executing request: " + httpget.getRequestLine());

		String rspStr = null;
		StatusLine sl = null;
		try (CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(scsf).build();
				CloseableHttpResponse response = httpclient.execute(httpget);) {
			HttpEntity entity = response.getEntity();
			//logger.debug("----------------------------------------");
			sl = response.getStatusLine();
			//logger.debug(sl);
			if (entity != null) {
				//logger.debug("Response content length: " + entity.getContentLength());
				rspStr = EntityUtils.toString(entity);
				//logger.debug(rspStr);
				EntityUtils.consume(entity);
			}
			//logger.debug("----------------------------------------");
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
		if (scsf == null) {
			//logger.fatal("SSLConnectionSocketFactory not init.");
			return null;
		}

		HttpPost httppost = new HttpPost(url);
		httppost.addHeader("Content-Type", contentType);
		httppost.setEntity(new StringEntity(content));

		//logger.debug("executing request: " + httppost.getRequestLine());
		//logger.debug("with content: " + content);

		String rspStr = null;
		StatusLine sl = null;
		try (CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(scsf).build();
				CloseableHttpResponse response = httpclient.execute(httppost);) {
			HttpEntity entity = response.getEntity();
			//logger.debug("----------------------------------------");
			sl = response.getStatusLine();
			//logger.debug(sl);
			if (entity != null) {
				//logger.debug("Response content length: " + entity.getContentLength());
				rspStr = EntityUtils.toString(entity);
				//logger.debug(rspStr);
				EntityUtils.consume(entity);
			}
			//logger.debug("----------------------------------------");
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
		if (scsf == null) {
			//logger.fatal("SSLConnectionSocketFactory not init.");
			return null;
		}

		HttpPost httppost = new HttpPost(url);
		httppost.addHeader("Content-Type", "multipart/form-data");

		MultipartEntityBuilder mb = MultipartEntityBuilder.create();
		for (int i = 0; i < names.length; i++) {
			mb = mb.addPart(names[i], new StringBody(contents[i], ContentType.create(contentTypes[i], Consts.UTF_8)));
		}

		HttpEntity reqEntity = mb.build();
		httppost.setEntity(reqEntity);

		//logger.debug("executing request: " + httppost.getRequestLine());
		//logger.debug("with content: ");
		try (InputStream content = reqEntity.getContent();) {
			BufferedReader br = new BufferedReader(new InputStreamReader(content));
			String line = null;
			while ((line = br.readLine()) != null) {
				//logger.debug(line);
			}
		}

		String rspStr = null;
		StatusLine sl = null;
		try (CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(scsf).build();
				CloseableHttpResponse response = httpclient.execute(httppost);) {
			HttpEntity entity = response.getEntity();
			//logger.debug("----------------------------------------");
			sl = response.getStatusLine();
			//logger.debug(sl);
			if (entity != null) {
				//logger.debug("Response content length: " + entity.getContentLength());
				rspStr = EntityUtils.toString(entity);
				//logger.debug(rspStr);
				EntityUtils.consume(entity);
			}
			//logger.debug("----------------------------------------");
		}

		String[] rsp = { sl == null ? null : new Integer(sl.getStatusCode()).toString(), rspStr };
		return rsp;
	}

	public static void main(String[] args) throws Exception {
		String[] res = get("https://sandbox.scop.chinamobile.com:8102/api/v2/token?appKey=290KNRKKXQXPDJ95&secret=8ddca75947c10aa614396464c8a38f1b1e7bf29053e0ba559fa18662365976b7&timestamp=20160818133801008");
		System.out.println("=============\n" + res[0] + "\n=============\n" + res[1]);
	}

}