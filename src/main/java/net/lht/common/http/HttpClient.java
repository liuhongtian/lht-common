package net.lht.common.http;

import java.io.IOException;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 发送HTTP GET、POST的工具。<br>
 * 这是极简的工具，仅适用于所有信息都保存在报文体中的情形。另外，当发送multipart报文时，只支持String，不支持二进制内容。
 * 
 * @author liuhongtian
 *
 */
public class HttpClient {

	private Logger logger = LoggerFactory.getLogger(HttpClient.class);

	/**
	 * get
	 * 
	 * @param url
	 * @return statusCode and rspStr as String[]
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String[] get(String url) throws ClientProtocolException, IOException {
		HttpGet httpget = new HttpGet(url);

		String rspStr = null;
		StatusLine sl = null;

		try (CloseableHttpClient httpclient = HttpClients.createDefault();
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
	public String[] post(String url, String content, String contentType)
			throws ClientProtocolException, IOException {
		HttpPost httppost = new HttpPost(url);
		httppost.addHeader("Content-Type", contentType);
		httppost.setEntity(new StringEntity(content));

		String rspStr = null;
		StatusLine sl = null;

		try (CloseableHttpClient httpclient = HttpClients.createDefault();
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

		try (CloseableHttpClient httpclient = HttpClients.createDefault();
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

	public static void main(String[] args) {
		new HttpClient().logger.debug("HttpClient.main");
	}

}