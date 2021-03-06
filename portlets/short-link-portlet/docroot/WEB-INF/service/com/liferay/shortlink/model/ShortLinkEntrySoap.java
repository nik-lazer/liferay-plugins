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

package com.liferay.shortlink.model;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This class is used by SOAP remote services, specifically {@link com.liferay.shortlink.service.http.ShortLinkEntryServiceSoap}.
 *
 * @author Brian Wing Shun Chan
 * @see com.liferay.shortlink.service.http.ShortLinkEntryServiceSoap
 * @generated
 */
public class ShortLinkEntrySoap implements Serializable {
	public static ShortLinkEntrySoap toSoapModel(ShortLinkEntry model) {
		ShortLinkEntrySoap soapModel = new ShortLinkEntrySoap();

		soapModel.setUuid(model.getUuid());
		soapModel.setShortLinkEntryId(model.getShortLinkEntryId());
		soapModel.setCreateDate(model.getCreateDate());
		soapModel.setModifiedDate(model.getModifiedDate());
		soapModel.setOriginalURL(model.getOriginalURL());
		soapModel.setShortURL(model.getShortURL());
		soapModel.setAutogenerated(model.getAutogenerated());
		soapModel.setActive(model.getActive());

		return soapModel;
	}

	public static ShortLinkEntrySoap[] toSoapModels(ShortLinkEntry[] models) {
		ShortLinkEntrySoap[] soapModels = new ShortLinkEntrySoap[models.length];

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModel(models[i]);
		}

		return soapModels;
	}

	public static ShortLinkEntrySoap[][] toSoapModels(ShortLinkEntry[][] models) {
		ShortLinkEntrySoap[][] soapModels = null;

		if (models.length > 0) {
			soapModels = new ShortLinkEntrySoap[models.length][models[0].length];
		}
		else {
			soapModels = new ShortLinkEntrySoap[0][0];
		}

		for (int i = 0; i < models.length; i++) {
			soapModels[i] = toSoapModels(models[i]);
		}

		return soapModels;
	}

	public static ShortLinkEntrySoap[] toSoapModels(List<ShortLinkEntry> models) {
		List<ShortLinkEntrySoap> soapModels = new ArrayList<ShortLinkEntrySoap>(models.size());

		for (ShortLinkEntry model : models) {
			soapModels.add(toSoapModel(model));
		}

		return soapModels.toArray(new ShortLinkEntrySoap[soapModels.size()]);
	}

	public ShortLinkEntrySoap() {
	}

	public long getPrimaryKey() {
		return _shortLinkEntryId;
	}

	public void setPrimaryKey(long pk) {
		setShortLinkEntryId(pk);
	}

	public String getUuid() {
		return _uuid;
	}

	public void setUuid(String uuid) {
		_uuid = uuid;
	}

	public long getShortLinkEntryId() {
		return _shortLinkEntryId;
	}

	public void setShortLinkEntryId(long shortLinkEntryId) {
		_shortLinkEntryId = shortLinkEntryId;
	}

	public Date getCreateDate() {
		return _createDate;
	}

	public void setCreateDate(Date createDate) {
		_createDate = createDate;
	}

	public Date getModifiedDate() {
		return _modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		_modifiedDate = modifiedDate;
	}

	public String getOriginalURL() {
		return _originalURL;
	}

	public void setOriginalURL(String originalURL) {
		_originalURL = originalURL;
	}

	public String getShortURL() {
		return _shortURL;
	}

	public void setShortURL(String shortURL) {
		_shortURL = shortURL;
	}

	public boolean getAutogenerated() {
		return _autogenerated;
	}

	public boolean isAutogenerated() {
		return _autogenerated;
	}

	public void setAutogenerated(boolean autogenerated) {
		_autogenerated = autogenerated;
	}

	public boolean getActive() {
		return _active;
	}

	public boolean isActive() {
		return _active;
	}

	public void setActive(boolean active) {
		_active = active;
	}

	private String _uuid;
	private long _shortLinkEntryId;
	private Date _createDate;
	private Date _modifiedDate;
	private String _originalURL;
	private String _shortURL;
	private boolean _autogenerated;
	private boolean _active;
}