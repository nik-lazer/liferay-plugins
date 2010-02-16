/**
 * Copyright (c) 2000-2010 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.socialcoding.service;

import com.liferay.portal.kernel.bean.PortletBeanLocatorUtil;
import com.liferay.portal.kernel.util.ClassLoaderProxy;

/**
 * <a href="JIRAChangeGroupLocalServiceUtil.java.html"><b><i>View Source</i></b>
 * </a>
 *
 * @author Brian Wing Shun Chan
 */
public class JIRAChangeGroupLocalServiceUtil {
	public static com.liferay.socialcoding.model.JIRAChangeGroup addJIRAChangeGroup(
		com.liferay.socialcoding.model.JIRAChangeGroup jiraChangeGroup)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().addJIRAChangeGroup(jiraChangeGroup);
	}

	public static com.liferay.socialcoding.model.JIRAChangeGroup createJIRAChangeGroup(
		long jiraChangeGroupId) {
		return getService().createJIRAChangeGroup(jiraChangeGroupId);
	}

	public static void deleteJIRAChangeGroup(long jiraChangeGroupId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		getService().deleteJIRAChangeGroup(jiraChangeGroupId);
	}

	public static void deleteJIRAChangeGroup(
		com.liferay.socialcoding.model.JIRAChangeGroup jiraChangeGroup)
		throws com.liferay.portal.kernel.exception.SystemException {
		getService().deleteJIRAChangeGroup(jiraChangeGroup);
	}

	public static java.util.List<Object> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().dynamicQuery(dynamicQuery);
	}

	public static java.util.List<Object> dynamicQuery(
		com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start,
		int end) throws com.liferay.portal.kernel.exception.SystemException {
		return getService().dynamicQuery(dynamicQuery, start, end);
	}

	public static com.liferay.socialcoding.model.JIRAChangeGroup getJIRAChangeGroup(
		long jiraChangeGroupId)
		throws com.liferay.portal.kernel.exception.PortalException,
			com.liferay.portal.kernel.exception.SystemException {
		return getService().getJIRAChangeGroup(jiraChangeGroupId);
	}

	public static java.util.List<com.liferay.socialcoding.model.JIRAChangeGroup> getJIRAChangeGroups(
		int start, int end)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().getJIRAChangeGroups(start, end);
	}

	public static int getJIRAChangeGroupsCount()
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().getJIRAChangeGroupsCount();
	}

	public static com.liferay.socialcoding.model.JIRAChangeGroup updateJIRAChangeGroup(
		com.liferay.socialcoding.model.JIRAChangeGroup jiraChangeGroup)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().updateJIRAChangeGroup(jiraChangeGroup);
	}

	public static com.liferay.socialcoding.model.JIRAChangeGroup updateJIRAChangeGroup(
		com.liferay.socialcoding.model.JIRAChangeGroup jiraChangeGroup,
		boolean merge)
		throws com.liferay.portal.kernel.exception.SystemException {
		return getService().updateJIRAChangeGroup(jiraChangeGroup, merge);
	}

	public static void clearService() {
		_service = null;
	}

	public static JIRAChangeGroupLocalService getService() {
		if (_service == null) {
			Object obj = PortletBeanLocatorUtil.locate(ClpSerializer.SERVLET_CONTEXT_NAME,
					JIRAChangeGroupLocalService.class.getName());
			ClassLoader portletClassLoader = (ClassLoader)PortletBeanLocatorUtil.locate(ClpSerializer.SERVLET_CONTEXT_NAME,
					"portletClassLoader");

			ClassLoaderProxy classLoaderProxy = new ClassLoaderProxy(obj,
					portletClassLoader);

			_service = new JIRAChangeGroupLocalServiceClp(classLoaderProxy);

			ClpSerializer.setClassLoader(portletClassLoader);
		}

		return _service;
	}

	public void setService(JIRAChangeGroupLocalService service) {
		_service = service;
	}

	private static JIRAChangeGroupLocalService _service;
}