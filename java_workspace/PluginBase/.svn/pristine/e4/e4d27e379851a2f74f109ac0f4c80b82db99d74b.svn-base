package com.crestech.opkey.plugin.functiondispatch;

import java.util.ArrayList;
import java.util.List;

import com.crestech.opkey.plugin.communication.contracts.functioncall.FunctionCall;
import com.crestech.opkey.plugin.communication.contracts.functioncall.FunctionCall.DataArguments.DataArgument;
import com.crestech.opkey.plugin.communication.contracts.functioncall.FunctionCall.MobilityArguments.MobileApplicationArguments.Application;
import com.crestech.opkey.plugin.communication.contracts.functioncall.FunctionCall.MobilityArguments.MobileDeviceArguments.Device;
import com.crestech.opkey.plugin.communication.contracts.functioncall.FunctionCall.ObjectArguments.ObjectArgument;
import com.crestech.opkey.plugin.communication.contracts.functioncall.MobileApplication;
import com.crestech.opkey.plugin.communication.contracts.functioncall.MobileDevice;

public class ArgumentFormatter {

	public Object[] Format(FunctionCall fc) throws Exception {
		List<Object> list = new ArrayList<Object>();

		// Add the objects
		if (fc.getObjectArguments() != null) {
			for (ObjectArgument obj : fc.getObjectArguments().getObjectArgument()) {
				list.add(FormatObjectArgument(obj));
			}
		}

		if (fc.getMobilityArguments() != null) {
			// Add the Mobile Devices
			if (fc.getMobilityArguments().getMobileDeviceArguments() != null) {
				for (Device device : fc.getMobilityArguments().getMobileDeviceArguments().getDevice()) {
					list.add(FormatMobileDevice(device));
				}
			}
			// Add the Mobile Application
			if (fc.getMobilityArguments().getMobileApplicationArguments() != null) {
				for (Application device : fc.getMobilityArguments().getMobileApplicationArguments().getApplication()) {
					list.add(FormatMobileApplication(device));
				}
			}
		}
		// Add non-object data
		if (fc.getDataArguments() != null) {
			for (DataArgument data : fc.getDataArguments().getDataArgument()) {
				list.add(FormatDataArgument(data));
			}
		}
		return list.toArray();
	}

	protected MobileDevice FormatMobileDevice(Device device) {
		return device.getMobileDevice();
	}

	protected MobileApplication FormatMobileApplication(Application app) {
		return app.getMobileApplication();
	}

	/*
	 * This method converts the incoming object argument into something the
	 * client methods can easily understand and consume
	 */
	protected Object FormatObjectArgument(ObjectArgument oArg) throws Exception {
		/*
		 * the default implementation returns the actual object as is. It does
		 * not consider other information like argument name etc. This behavior
		 * can be altered in a suitable subclasses
		 */
		return oArg.getObject();
	}

	/*
	 * This method converts the incoming data argument into something the client
	 * methods can easily understand and consume
	 */
	protected Object FormatDataArgument(DataArgument dArg) {
		String argValue = dArg.getValue();

		switch (dArg.getDataType().toLowerCase()) {
		case "integer":
			return FormatInteger(argValue);

		case "double":
			return FormatDouble(argValue);

		case "boolean":
			return FormatBoolean(argValue);

		case "string":
			return argValue;

		default:
			throw new IllegalArgumentException("Unknown data type '" + dArg.getDataType() + "'");
		}
	}

	protected Object FormatInteger(String integerValue) {
		// Check If OpKey Sends an blank value <""> by SKS 9
		integerValue = (integerValue.equalsIgnoreCase("")) ? "0" : integerValue;
		return Integer.parseInt(integerValue);
	}

	protected Object FormatDouble(String doubleValue) {
		return Double.parseDouble(doubleValue);
	}

	protected Object FormatBoolean(String booleanValue) {
		return Boolean.parseBoolean(booleanValue);
	}

}
