package net.lht.common.http;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpsClient {

	private Logger logger = LoggerFactory.getLogger(HttpsClient.class);

	private SSLConnectionSocketFactory scsf;

	public HttpsClient(String keyStoreFile, String keyStorePassword) {
		try {
			this.scsf = initSSL(keyStoreFile, keyStoreFile);
		} catch (KeyManagementException | UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException
				| CertificateException | IOException e) {
			logger.error("SSLConnectionSocketFactory init faild, keyStoreFile=" + keyStoreFile + ", keyStorePassword="
					+ keyStorePassword, e);
			System.exit(1);
		}
	}

	private SSLConnectionSocketFactory initSSL(String keyStoreFile, String password)
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException,
			KeyManagementException, UnrecoverableKeyException {
		try (InputStream in = HttpsClient.class.getClassLoader().getResourceAsStream(keyStoreFile);) {
			KeyStore keyStore = KeyStore.getInstance("jks");
			keyStore.load(in, StringUtils.isEmpty(password) ? null : password.toCharArray());
			SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(keyStore, new TrustSelfSignedStrategy())
					.loadKeyMaterial(keyStore, StringUtils.isEmpty(password) ? null : password.toCharArray()).build();
			// Allow TLSv1 protocol only
			return new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1", "TLSv1.1", "TLSv1.2" }, null,
					SSLConnectionSocketFactory.getDefaultHostnameVerifier());
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
	public String[] get(String url) throws ClientProtocolException, IOException {
		if (this.scsf == null) {
			logger.error("SSLConnectionSocketFactory not init.");
			return null;
		}

		HttpGet httpget = new HttpGet(url);

		String rspStr = null;
		StatusLine sl = null;
		try (CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(scsf).build();
				CloseableHttpResponse response = httpclient.execute(httpget);) {
			HttpEntity entity = response.getEntity();
			sl = response.getStatusLine();
			if (entity != null) {
				rspStr = EntityUtils.toString(entity);
				EntityUtils.consume(entity);
			}
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
	public String[] post(String url, String content, String contentType) throws ClientProtocolException, IOException {
		if (this.scsf == null) {
			logger.error("SSLConnectionSocketFactory not init.");
			return null;
		}

		HttpPost httppost = new HttpPost(url);
		httppost.addHeader("Content-Type", contentType);
		httppost.setEntity(new StringEntity(content));

		String rspStr = null;
		StatusLine sl = null;
		try (CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(scsf).build();
				CloseableHttpResponse response = httpclient.execute(httppost);) {
			HttpEntity entity = response.getEntity();
			sl = response.getStatusLine();
			if (entity != null) {
				rspStr = EntityUtils.toString(entity);
				EntityUtils.consume(entity);
			}
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
	public String[] postMultipart(String url, String[] names, String contents[], String[] contentTypes)
			throws ClientProtocolException, IOException {
		if (this.scsf == null) {
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

		String rspStr = null;
		StatusLine sl = null;
		try (CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(scsf).build();
				CloseableHttpResponse response = httpclient.execute(httppost);) {
			HttpEntity entity = response.getEntity();
			sl = response.getStatusLine();
			if (entity != null) {
				rspStr = EntityUtils.toString(entity);
				EntityUtils.consume(entity);
			}
		}

		String[] rsp = { sl == null ? null : new Integer(sl.getStatusCode()).toString(), rspStr };
		return rsp;
	}

	public static void main(String[] args) throws Exception {
		HttpsClient client = new HttpsClient("keystore.jks", "password");
		client.logger.debug("HttpsClient.main");
		String[] res = client.get(
				"https://sandbox.scop.chinamobile.com:8102/api/v2/token?appKey=290KNRKKXQXPDJ95&secret=8ddca75947c10aa614396464c8a38f1b1e7bf29053e0ba559fa18662365976b7&timestamp=20160818133801008");
		System.out.println("=============\n" + res[0] + "\n=============\n" + res[1]);
	}

}