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

package com.liferay.taglib.ui;

import com.liferay.portal.kernel.dao.search.DisplayTerms;
import com.liferay.taglib.util.IncludeTag;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Drew Brokke
 * @deprecated As of Athanasius (7.3.x), with no direct replacement
 */
@Deprecated
public class UserGroupSearchContainerResultsTag<R> extends IncludeTag {

	public DisplayTerms getSearchTerms() {
		return _searchTerms;
	}

	public LinkedHashMap<String, Object> getUserGroupParams() {
		return _userGroupParams;
	}

	public void setSearchTerms(DisplayTerms searchTerms) {
		_searchTerms = searchTerms;
	}

	public void setUserGroupParams(
		LinkedHashMap<String, Object> userGroupParams) {

		_userGroupParams = userGroupParams;
	}

	@Override
	protected void cleanUp() {
		super.cleanUp();

		_searchTerms = null;
		_userGroupParams = null;
	}

	@Override
	protected String getPage() {
		return _PAGE;
	}

	@Override
	protected void setAttributes(HttpServletRequest httpServletRequest) {
		SearchContainerTag<R> searchContainerTag =
			(SearchContainerTag<R>)findAncestorWithClass(
				this, SearchContainerTag.class);

		httpServletRequest.setAttribute(
			"liferay-ui:user-group-search-container-results:searchContainer",
			searchContainerTag.getSearchContainer());

		httpServletRequest.setAttribute(
			"liferay-ui:user-group-search-container-results:searchTerms",
			_searchTerms);
		httpServletRequest.setAttribute(
			"liferay-ui:user-group-search-container-results:userGroupParams",
			_userGroupParams);
	}

	private static final String _PAGE =
		"/html/taglib/ui/user_group_search_container_results/page.jsp";

	private DisplayTerms _searchTerms;
	private LinkedHashMap<String, Object> _userGroupParams;

}