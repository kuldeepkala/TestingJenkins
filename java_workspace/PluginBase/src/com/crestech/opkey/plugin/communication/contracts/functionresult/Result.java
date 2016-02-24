package com.crestech.opkey.plugin.communication.contracts.functionresult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.crestech.opkey.plugin.ExecutionStatus;
import com.crestech.opkey.plugin.ResultCodes;

public class Result {

	public static ResultBuilder PASS() {
		return (new Result()).new ResultBuilder(ExecutionStatus.Pass);
	}

	@Deprecated()
	public static ResultBuilder FAIL() {
		return (new Result()).new ResultBuilder(ExecutionStatus.Fail);
	}

	public static ResultBuilder FAIL(ResultCodes errorCode) {
		return (new Result()).new ResultBuilder(ExecutionStatus.Fail).setResultCode(errorCode);
	}

	public class ResultBuilder {

		private ExecutionStatus _status;
		private String _message = "";
		private String _output = "";
		private int _resultCode = 0;
		private String _snapshotPath = null;

		public ResultBuilder(ExecutionStatus status) {
			_status = status;
		}

		public ResultBuilder setOutput(String output) {
			_output = output;
			return this;
		}

		public ResultBuilder setOutput(int output) {
			_output = String.valueOf(output);
			return this;
		}

		public ResultBuilder setOutput(double output) {
			_output = String.valueOf(output);
			return this;
		}

		public ResultBuilder setOutput(boolean output) {
			_output = String.valueOf(output);
			return this;
		}

		public ResultBuilder setOutput(Date output) {
			// Create an instance of SimpleDateFormat used for formatting
			// the string representation of date (month/day/year)
			DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

			// Get the date today using Calendar object.
			// Using DateFormat format method we can create a string
			// representation of a date with the defined format.
			_output = df.format(output);
			return this;
		}

		public ResultBuilder setMessage(String message) {
			_message = message;
			return this;
		}

		public ResultBuilder setResultCode(int resultCode) {
			_resultCode = resultCode;
			return this;
		}

		public ResultBuilder setResultCode(ResultCodes resultCodesEnum) {
			return setResultCode(resultCodesEnum.Code());
		}

		public ResultBuilder setSnapshotPath(String imageFilePath) {
			this._snapshotPath = imageFilePath;
			return this;
		}

		public FunctionResult make() {
			FunctionResult res = new FunctionResult();

			String message1 = filterNonXMLCharecters(_message);
			res.setMessage(message1);

			String output1 = filterNonXMLCharecters(_output);
			res.setOutput(output1);

			res.setResultCode(_resultCode);
			res.setStatus(_status.toString());

			if (_snapshotPath != null && _snapshotPath.length() > 0)
				res.setSnapshotPath(this._snapshotPath);

			return res;
		}

		private String filterNonXMLCharecters(String data) {
			return data;
		}
	}
}