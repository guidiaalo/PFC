package com.mOpenXC;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class UpdateReceiver extends ResultReceiver {
	public static final int UPDATE_RESULT_CODE = 0;
	public static final String UPDATE_TEXT = "update_text";
	private ITextUpdater mListener;
	

	public UpdateReceiver(ITextUpdater aListener) {
		super(new Handler());
		mListener = aListener;
	}

	@Override
	protected void onReceiveResult(int aResultCode, Bundle aResultData) {
		if (aResultCode == UPDATE_RESULT_CODE && aResultData.containsKey(UPDATE_TEXT)) {
			mListener.updateText(aResultData.getString(UPDATE_TEXT));
		}
	}

}
