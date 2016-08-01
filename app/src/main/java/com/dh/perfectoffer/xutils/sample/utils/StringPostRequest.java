package com.dh.perfectoffer.xutils.sample.utils;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 重写String volley
 * 
 * @author Administrator
 * 
 */
public class StringPostRequest extends Request<String> {

	private Map<String, String> mMap;
	private Response.Listener<String> mListener;
	public String cookieFromResponse;
	private String mHeader;
	private Map<String, String> sendHeader = new HashMap<String, String>(1);

	public StringPostRequest(String url, Response.Listener<String> listener,
			Response.ErrorListener errorListener, Map map) {
		super(Request.Method.POST, url, errorListener);
		mListener = listener;
		mMap = map;
	}

	// 当http请求是post时，则需要该使用该函数设置往里面添加的键值对
	@Override
	protected Map<String, String> getParams() throws AuthFailureError {
		return mMap;
	}

	public StringPostRequest(int method, String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(method, url, errorListener);
		mListener = listener;
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		try {
			Map<String, String> responseHeaders = response.headers;
			String rawCookies = responseHeaders.get("cookies");
			String dataString = new String(response.data, "UTF-8");
			return Response.success(rawCookies + ";" + dataString,
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		}

	}

	@Override
	protected void deliverResponse(String response) {
		mListener.onResponse(response);

	}

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		return sendHeader;
	}

	private void setSendCookie(String cookie) {
		sendHeader.put("Cookie", cookie);

	}

}
