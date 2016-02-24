package com.crestech.opkey.plugin.contexts;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SessionContext {

	private Object tool = null;
	private Map<String, String> settings = new HashMap<String, String>();

	public void setTool(Object tool) {
		this.tool = tool;
	}

	public Object getTool() {
		return tool;
	}

	public Map<String, String> getSettings() {
		return settings;
	}

	@SuppressWarnings("unchecked")
	public <T> T getSetting(String key, T defaultValue) {
		if (this.settings == null)
			return defaultValue;

		if (!this.settings.containsKey(key)) {
			return defaultValue;

		} else {
			String value = this.settings.get(key);

			Class<?> clazz = defaultValue.getClass();

			if (clazz == String.class) {
				return (T) value;

			} else { // not string. need to parse into specific type
				if (value.length() == 0)
					return defaultValue;

				if (clazz == Integer.class) {
					return (T) (Object) Integer.parseInt(value);

				} else if (clazz == Double.class) {
					return (T) (Object) Double.parseDouble(value);

				} else if (clazz == Boolean.class) {
					if (value == "0") {
						value = "False";
					}
					if (value == "1") {
						value = "True";
					}

					return (T) (Object) Boolean.parseBoolean(value);

				} else {
					throw new UnsupportedOperationException("Unable to convert from '" + value + "' to " + clazz.getSimpleName());
				}
			}
		}
	}

	public void setSettings(Map<String, String> settings) {
		this.settings = settings;
	}

	public String getScreenshotsDirectory() {
		String s = settings.get("_ScreenshotsDirectory_");
		return s;
	}

	public SessionSnapshotFrequency getSessionSnapshotFrequency() {
		String s = settings.get("_SessionSnapshotFrequency_");
		return SessionSnapshotFrequency.valueOf(s);
	}

	public SessionSnapshotQuality getSessionSnapshotQuality() {
		String s = settings.get("_SessionSnapshotQuality_");
		return SessionSnapshotQuality.valueOf(s);
	}

	public String getPluginSettingXmlPath() {
		String s = settings.get("_PluginSettingXmlPath_");
		return s;
	}

	public String getCustomLibraryFolderPath() {
		String s = settings.get("_CustomLibraryFolderPath_");
		return s;
	}

	public CommunicationProtocol getCommunicationProtocol() {
		String s = settings.get("_CommunicationProtocol_");
		return CommunicationProtocol.valueOf(s);
	}

	public String getLogSinkEndpoint() {
		String s = settings.get("_LogSinkEndpoint_");
		return s;
	}

	public int getEnginePID() {
		String s = settings.get("_EnginePID_");
		return Integer.parseInt(s);
	}

	public boolean isDebugMode() {
		String _DebugMode_ = "_DebugMode_";

		if (!settings.containsKey(_DebugMode_))
			return false;

		String s = settings.get(_DebugMode_);
		return Boolean.parseBoolean(s);
	}

	public String getCommunicationEndpoint() {
		String s = settings.get("_CommunicationEndpoint_");
		return s;
	}

	/*
	 * 
	 * 
	 * 
	 * 
	 * The following section is deprecated on 23rd july 2014. Delete the API
	 * after 1 year on August 2015
	 */

	private File snapshotsDirectory = null;

	/**
	 * @deprecated use SettingsLoader() instead.
	 */
	@Deprecated
	public void setSnapshotsDirectory(File snapshotsDirectory) {
		this.snapshotsDirectory = snapshotsDirectory;
	}

	/**
	 * @deprecated use getScreenshotsDirectory() instead.
	 */
	public File getSnapshotDirectory() {
		return this.snapshotsDirectory;
	}

	private SessionSnapshotFrequency snapshotFrequency = null;

	/**
	 * @deprecated use SettingsLoader() instead.
	 */
	@Deprecated
	public void setSnapshotFrequency(SessionSnapshotFrequency ssf) {
		this.snapshotFrequency = ssf;
	}

	/**
	 * @deprecated use getSessionSnapshotFrequency() instead.
	 */
	@Deprecated
	public SessionSnapshotFrequency getSnapshotFrequency() {
		return snapshotFrequency;
	}

}
