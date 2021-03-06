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

package com.liferay.calendar.lar;

import com.liferay.calendar.DuplicateCalendarResourceException;
import com.liferay.calendar.model.Calendar;
import com.liferay.calendar.model.CalendarResource;
import com.liferay.calendar.service.CalendarLocalServiceUtil;
import com.liferay.calendar.service.CalendarResourceLocalServiceUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.lar.BaseStagedModelDataHandler;
import com.liferay.portal.kernel.lar.ExportImportPathUtil;
import com.liferay.portal.kernel.lar.PortletDataContext;
import com.liferay.portal.kernel.lar.StagedModelDataHandlerUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.LocalizationUtil;
import com.liferay.portal.kernel.util.MapUtil;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.User;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portal.util.PortalUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Andrea Di Giorgi
 * @author Daniel Kocsis
 */
public class CalendarResourceStagedModelDataHandler
	extends BaseStagedModelDataHandler<CalendarResource> {

	public static final String[] CLASS_NAMES =
		{CalendarResource.class.getName()};

	@Override
	public void deleteStagedModel(
			String uuid, long groupId, String className, String extraData)
		throws PortalException, SystemException {

		CalendarResource calendarResource =
			CalendarResourceLocalServiceUtil.
				fetchCalendarResourceByUuidAndGroupId(uuid, groupId);

		if (calendarResource != null) {
			CalendarResourceLocalServiceUtil.deleteCalendarResource(
				calendarResource);
		}
	}

	@Override
	public String[] getClassNames() {
		return CLASS_NAMES;
	}

	@Override
	public String getDisplayName(CalendarResource calendarResource) {
		return calendarResource.getNameCurrentValue();
	}

	@Override
	protected boolean countStagedModel(
		PortletDataContext portletDataContext,
		CalendarResource calendarResource) {

		if (calendarResource.getClassNameId() ==
				PortalUtil.getClassNameId(CalendarResource.class)) {

			return true;
		}

		return false;
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext,
			CalendarResource calendarResource)
		throws Exception {

		Element calendarResourceElement =
			portletDataContext.getExportDataElement(calendarResource);

		for (Calendar calendar : calendarResource.getCalendars()) {
			StagedModelDataHandlerUtil.exportReferenceStagedModel(
				portletDataContext, calendarResource, calendar,
				PortletDataContext.REFERENCE_TYPE_STRONG);
		}

		if (calendarResource.getClassNameId() ==
				PortalUtil.getClassNameId(User.class)) {

			User user = UserLocalServiceUtil.getUser(
				calendarResource.getClassPK());

			portletDataContext.addReferenceElement(
				calendarResource, calendarResourceElement, user, User.class,
				PortletDataContext.REFERENCE_TYPE_DEPENDENCY_DISPOSABLE, true);
		}

		portletDataContext.addClassedModel(
			calendarResourceElement,
			ExportImportPathUtil.getModelPath(calendarResource),
			calendarResource);
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext,
			CalendarResource calendarResource)
		throws Exception {

		prepareLanguagesForImport(calendarResource);

		long userId = portletDataContext.getUserId(
			calendarResource.getUserUuid());

		StagedModelDataHandlerUtil.importReferenceStagedModels(
			portletDataContext, calendarResource, Calendar.class);

		long classPK = getClassPK(portletDataContext, calendarResource, userId);
		Map<Locale, String> calendarResourceNameMap =
			getCalendarResourceNameMap(portletDataContext, calendarResource);

		ServiceContext serviceContext = portletDataContext.createServiceContext(
			calendarResource);

		CalendarResource importedCalendarResource = null;

		if (portletDataContext.isDataStrategyMirror()) {
			CalendarResource existingCalendarResource =
				CalendarResourceLocalServiceUtil.
					fetchCalendarResourceByUuidAndGroupId(
						calendarResource.getUuid(),
						portletDataContext.getScopeGroupId());

			if (existingCalendarResource == null) {
				existingCalendarResource =
					CalendarResourceLocalServiceUtil.fetchCalendarResource(
						calendarResource.getClassNameId(), classPK);
			}

			if (existingCalendarResource == null) {
				serviceContext.setUuid(calendarResource.getUuid());

				importedCalendarResource =
					CalendarResourceLocalServiceUtil.addCalendarResource(
						userId, portletDataContext.getScopeGroupId(),
						calendarResource.getClassNameId(), classPK,
						calendarResource.getClassUuid(),
						calendarResource.getCode(), calendarResourceNameMap,
						calendarResource.getDescriptionMap(),
						calendarResource.isActive(), serviceContext);
			}
			else {
				importedCalendarResource =
					CalendarResourceLocalServiceUtil.updateCalendarResource(
						existingCalendarResource.getCalendarResourceId(),
						calendarResource.getNameMap(),
						calendarResource.getDescriptionMap(),
						calendarResource.isActive(), serviceContext);
			}
		}
		else {
			try {
				importedCalendarResource =
					CalendarResourceLocalServiceUtil.addCalendarResource(
						userId, portletDataContext.getScopeGroupId(),
						calendarResource.getClassNameId(), classPK,
						calendarResource.getClassUuid(),
						calendarResource.getCode(), calendarResourceNameMap,
						calendarResource.getDescriptionMap(),
						calendarResource.isActive(), serviceContext);
			}
			catch (DuplicateCalendarResourceException dcre) {

				// The calendar resource for the site's default calendar is
				// always generated beforehand, so we only want to add it once

				importedCalendarResource =
					CalendarResourceLocalServiceUtil.fetchCalendarResource(
						calendarResource.getClassNameId(), classPK);
			}
		}

		updateCalendars(
			portletDataContext, calendarResource, importedCalendarResource);

		portletDataContext.importClassedModel(
			calendarResource, importedCalendarResource);
	}

	protected Map<Locale, String> getCalendarResourceNameMap(
			PortletDataContext portletDataContext,
			CalendarResource calendarResource)
		throws Exception {

		String calendarResourceName = calendarResource.getName(
			LocaleUtil.getDefault());

		Group sourceGroup = GroupLocalServiceUtil.fetchGroup(
			portletDataContext.getSourceGroupId());

		if ((sourceGroup == null) ||
			!calendarResourceName.equals(sourceGroup.getDescriptiveName())) {

			return calendarResource.getNameMap();
		}

		Map<Locale, String> calendarResourceNameMap =
			new HashMap<Locale, String>();

		Group scopeGroup = GroupLocalServiceUtil.getGroup(
			portletDataContext.getScopeGroupId());

		calendarResourceNameMap.put(
			LocaleUtil.getDefault(), scopeGroup.getName());

		return calendarResourceNameMap;
	}

	protected long getClassPK(
		PortletDataContext portletDataContext,
		CalendarResource calendarResource, long userId) {

		long classPK = 0;

		if (calendarResource.getClassNameId() ==
				PortalUtil.getClassNameId(Group.class)) {

			classPK = portletDataContext.getScopeGroupId();
		}
		else if (calendarResource.getClassNameId() ==
					PortalUtil.getClassNameId(User.class)) {

			classPK = userId;
		}

		return classPK;
	}

	protected void prepareLanguagesForImport(CalendarResource calendarResource)
		throws PortalException {

		Locale defaultLocale = LocaleUtil.fromLanguageId(
			calendarResource.getDefaultLanguageId());

		Locale[] availableLocales = LocaleUtil.fromLanguageIds(
			calendarResource.getAvailableLanguageIds());

		Locale defaultImportLocale = LocalizationUtil.getDefaultImportLocale(
			CalendarResource.class.getName(), calendarResource.getPrimaryKey(),
			defaultLocale, availableLocales);

		calendarResource.prepareLocalizedFieldsForImport(defaultImportLocale);
	}

	protected void updateCalendars(
			PortletDataContext portletDataContext,
			CalendarResource calendarResource,
			CalendarResource importedCalendarResource)
		throws SystemException {

		Map<Long, Long> calendarIds =
			(Map<Long, Long>)portletDataContext.getNewPrimaryKeysMap(
				Calendar.class);

		List<Element> referenceElements =
			portletDataContext.getReferenceElements(
				calendarResource, Calendar.class);

		for (Element referenceElement : referenceElements) {
			long calendarId = GetterUtil.getLong(
				referenceElement.attributeValue("class-pk"));

			Calendar calendar = CalendarLocalServiceUtil.fetchCalendar(
				MapUtil.getLong(calendarIds, calendarId));

			if (calendar != null) {
				calendar.setCalendarResourceId(
					importedCalendarResource.getCalendarResourceId());

				CalendarLocalServiceUtil.updateCalendar(calendar);
			}
		}
	}

}