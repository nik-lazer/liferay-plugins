/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.asset.entry.set.handler;

import com.liferay.asset.entry.set.model.AssetEntrySet;
import com.liferay.asset.entry.set.service.AssetEntrySetLocalServiceUtil;
import com.liferay.asset.entry.set.util.AssetEntrySetConstants;
import com.liferay.asset.entry.set.util.AssetEntrySetParticipantInfoUtil;
import com.liferay.asset.entry.set.util.GeoNamesUtil;
import com.liferay.asset.entry.set.util.PortletPropsValues;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.json.JSONArray;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Calvin Keum
 */
public class BaseAssetEntrySetHandler implements AssetEntrySetHandler {

	public BaseAssetEntrySetHandler(String portletId) {
		_portletId = portletId;
	}

	@Override
	public String getPortletId() {
		return _portletId;
	}

	@Override
	public JSONObject interpret(
			long userId, long assetEntrySetId, JSONObject payloadJSONObject)
		throws PortalException, SystemException {

		JSONObject jsonObject = JSONFactoryUtil.createJSONObject();

		JSONObject geolocationJSONObject = JSONFactoryUtil.createJSONObject();

		AssetEntrySet assetEntrySet =
			AssetEntrySetLocalServiceUtil.fetchAssetEntrySet(assetEntrySetId);

		if (assetEntrySet != null) {
			if (isContentModified(
					JSONFactoryUtil.createJSONObject(
						assetEntrySet.getPayload()),
					payloadJSONObject)) {

				jsonObject.put(
					"contentModifiedTime", System.currentTimeMillis());
			}

			JSONObject oldPayloadJSONObject = JSONFactoryUtil.createJSONObject(
				assetEntrySet.getPayload());

			geolocationJSONObject = oldPayloadJSONObject.getJSONObject(
				"geolocation");
		}
		else {
			geolocationJSONObject = getGeolocationJSONObject(payloadJSONObject);
		}

		jsonObject.put("geolocation", geolocationJSONObject);

		jsonObject.put("linkData", payloadJSONObject.getString("linkData"));
		jsonObject.put("message", payloadJSONObject.getString("message"));
		jsonObject.put("type", payloadJSONObject.getString("type"));

		JSONArray sharedToJSONArray = payloadJSONObject.getJSONArray(
			AssetEntrySetConstants.PAYLOAD_KEY_SHARED_TO);

		String[] assetTagNames = StringUtil.split(
			payloadJSONObject.getString(
				AssetEntrySetConstants.PAYLOAD_KEY_ASSET_TAG_NAMES));

		JSONArray assetTagsJSONArray =
			AssetEntrySetParticipantInfoUtil.getAssetTagsJSONArray(
				userId, assetTagNames);

		for (int i = 0; i < assetTagsJSONArray.length(); i++) {
			sharedToJSONArray.put(assetTagsJSONArray.getJSONObject(i));
		}

		sharedToJSONArray = processSharedToJSONArray(sharedToJSONArray);

		jsonObject.put(
			AssetEntrySetConstants.PAYLOAD_KEY_SHARED_TO, sharedToJSONArray);

		return jsonObject;
	}

	protected JSONArray dedupeSharedToJSONArray(JSONArray sharedToJSONArray) {
		Map<Long, List<Long>> classNameIds = new HashMap<Long, List<Long>>();

		JSONArray newSharedToJSONArray = JSONFactoryUtil.createJSONArray();

		for (int i = 0; i < sharedToJSONArray.length(); i++) {
			JSONObject sharedToJSONObject = sharedToJSONArray.getJSONObject(i);

			long classNameId = sharedToJSONObject.getLong("classNameId");

			List<Long> classPKs = classNameIds.get(classNameId);

			if (classPKs == null) {
				classPKs = new ArrayList<Long>();
			}

			long classPK = sharedToJSONObject.getLong("classPK");

			if (classPKs.contains(classPK)) {
				continue;
			}

			classPKs.add(classPK);

			classNameIds.put(classNameId, classPKs);

			newSharedToJSONArray.put(sharedToJSONObject);
		}

		return newSharedToJSONArray;
	}

	protected JSONArray filterSharedToJSONArray(JSONArray sharedToJSONArray)
		throws PortalException {

		JSONArray newSharedToJSONArray = JSONFactoryUtil.createJSONArray();

		List<String> keys = ListUtil.toList(
			PortletPropsValues.ASSET_ENTRY_SET_SHARED_TO_JSON_OBJECT_KEYS);

		for (int i = 0; i < sharedToJSONArray.length(); i++) {
			JSONObject sharedToJSONObject = sharedToJSONArray.getJSONObject(i);

			JSONObject newSharedToJSONObject = JSONFactoryUtil.createJSONObject(
				sharedToJSONObject.toString());

			Iterator<String> itr = sharedToJSONObject.keys();

			while (itr.hasNext()) {
				String key = itr.next();

				if (!keys.contains(key)) {
					newSharedToJSONObject.remove(key);
				}
			}

			newSharedToJSONArray.put(newSharedToJSONObject);
		}

		return newSharedToJSONArray;
	}

	protected JSONObject getGeolocationJSONObject(
		JSONObject payloadJSONObject) {

		JSONObject geolocationJSONObject = payloadJSONObject.getJSONObject(
			"geolocation");

		String locationName = StringPool.BLANK;

		if (geolocationJSONObject == null) {
			geolocationJSONObject = JSONFactoryUtil.createJSONObject();
		}
		else {
			double latitude = geolocationJSONObject.getDouble("latitude");
			double longitude = geolocationJSONObject.getDouble("longitude");

			locationName = GeoNamesUtil.getLocationName(latitude, longitude);

			geolocationJSONObject.put("locationName", locationName);
		}

		return geolocationJSONObject;
	}

	protected boolean isContentModified(
		JSONObject oldPayloadJSONObject, JSONObject newPayloadJSONObject) {

		Iterator<String> keys = oldPayloadJSONObject.keys();

		while (keys.hasNext()) {
			String key = keys.next();

			if (key.equals(
					AssetEntrySetConstants.PAYLOAD_KEY_ASSET_TAG_NAMES) ||
				key.equals(AssetEntrySetConstants.PAYLOAD_KEY_SHARED_TO)) {

				continue;
			}

			String oldValue = oldPayloadJSONObject.getString(key);
			String newValue = newPayloadJSONObject.getString(key);

			if (!Validator.equals(oldValue, newValue)) {
				return true;
			}
		}

		return false;
	}

	protected JSONArray processSharedToJSONArray(JSONArray sharedToJSONArray)
		throws PortalException {

		JSONArray newSharedToJSONArray = dedupeSharedToJSONArray(
			sharedToJSONArray);

		newSharedToJSONArray = filterSharedToJSONArray(newSharedToJSONArray);

		return newSharedToJSONArray;
	}

	protected void setPortletId(String portletId) {
		_portletId = portletId;
	}

	private String _portletId;

}