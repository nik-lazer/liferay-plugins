package com.liferay.wsrp.util;

import com.liferay.portal.kernel.util.PropsUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

/**
 * Util methods for WSRP headers forwarding
 * @author lazarev_nv  06.10.2015   14:47
 */
public class WsrpUtils {
	public static final String WSRP_FORWARD_RESPONSE_HEADERS = "wsrp.forward.response.headers";

	public static Set getWsrpHeadersForForwarding() {
		String setting = PropsUtil.get(WSRP_FORWARD_RESPONSE_HEADERS);
		return getWsrpHeadersForForwarding(setting);
	}

	public static Set getWsrpHeadersForForwarding(String data) {
		if (data != null) {
			Set<String> stringSet = new HashSet<String>();
			stringSet.addAll(Arrays.asList(data.toLowerCase().split("[ ]*,[ ]*")));
			return stringSet;
		}
		return new HashSet();
	}

	public static boolean containsIgnoreCase(Set<String> set, String search) {
		if (search != null) {
			return set.contains(search.toLowerCase());
		}
		return false;
	}
}
