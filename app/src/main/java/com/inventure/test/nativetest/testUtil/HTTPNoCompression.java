package com.inventure.test.nativetest.testUtil;

import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * Created by Anand Chokshi on 7/30/2015.
 */
public class HTTPNoCompression {
    static private final int TIMEOUT_CONNECTION = 25000; // timeout in milliseconds
    static private final int TIMEOUT_SOCKET = 30000; // timeout in milliseconds
    static private final boolean DEBUG_MODE = false;
    static private final String INVENTURE_RECEIVER_PASSWORD = "FQR3yt!34t@$#GF3qyt";

    private String responseDataString = null;

    public String getB64Auth(String login, String password) {
        String source = login + ":" + password;
        String authorizationString = "Basic " + Base64.encodeToString(source.getBytes(), Base64.URL_SAFE | Base64.NO_WRAP);
        return authorizationString;
    }

    public boolean sendDataToInventure(String inputJsonEncodedData, String targetHost) {
        // JSON encoded data to be sent to InVenture
        boolean sentSuccessful = false;
        String dataContent = inputJsonEncodedData;

        try {
            // setup the connection to InVenture android data receiver - using SSL
            URI inventureURL = new URI(targetHost);
            HttpClient inventureHttpClient = (HttpClient) getNewHttpClient();
            //HttpClient inventureHttpClient = new DefaultHttpClient(); // DEFAULT - default HTTP connection
            HttpPost inventureHttpPost = new HttpPost(inventureURL);

            // set the proper header and encoding before sending the message
            inventureHttpPost.setHeader("Accept", "application/json");
            inventureHttpPost.setHeader("Content-type", "application/json");

            // TODO - disable after development
            String authorizationString = getB64Auth("inventure", "accessdev@IV");
            inventureHttpPost.setHeader("Authorization", authorizationString);

            // set the password needed for the receiver API
//                nameValuePairs.add(new BasicNameValuePair("pwd", INVENTURE_RECEIVER_PASSWORD));
            inventureHttpPost.setEntity(new StringEntity(dataContent));

            // execute - forward the data to InVenture
            HttpResponse inventureHttpResponse = inventureHttpClient.execute(inventureHttpPost);

            // check response from the server
            String serverResponse = EntityUtils.toString(inventureHttpResponse.getEntity());

            // check the response code
            int responseCode = inventureHttpResponse.getStatusLine().getStatusCode();
            if (responseCode == 200) {
                try {
                    // decode the response JSON
                    JSONObject serverResponseJson = new JSONObject(serverResponse);
                    String responseStatusString = serverResponseJson.getString("status");

                    // get the response data string
                    responseDataString = serverResponseJson.getString("data");

                    if (responseStatusString.equalsIgnoreCase("success")) {
                        sentSuccessful = true;
                    } else {
                        sentSuccessful = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sentSuccessful;
    }

    //    HttpURLConnection
    // Start code for HTTP client
    private HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sslSocket = new MySSLSocketFactory(trustStore);
            sslSocket.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            // set the preferred HTTP parameters
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            // set the default connection timeout
            HttpConnectionParams.setConnectionTimeout(params, TIMEOUT_CONNECTION);

            // Set the default socket timeout (SO_TIMEOUT)
            // in milliseconds which is the timeout for waiting for data.
            HttpConnectionParams.setSoTimeout(params, TIMEOUT_SOCKET);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sslSocket, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            if (DEBUG_MODE) {
                Log.i("InsightService", "Send through HTTPS");
            }
            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            if (DEBUG_MODE) {
                Log.i("InsightService", "Send through HTTP");
            }
            return new DefaultHttpClient();
        }
    }

    // Start code for SSL
    private class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        private MySSLSocketFactory(KeyStore truststore)
                throws NoSuchAlgorithmException, KeyManagementException,
                KeyStoreException, UnrecoverableKeyException {

            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port,
                                   boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }
}
