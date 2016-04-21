package com.v2gogo.project.utils.http.upload;

public interface LoadListener
{

	void onStart();

	void onSuccess(byte[] data, String url, int actionId);

	void onError(String errorMsg, String url, int actionId);
}
