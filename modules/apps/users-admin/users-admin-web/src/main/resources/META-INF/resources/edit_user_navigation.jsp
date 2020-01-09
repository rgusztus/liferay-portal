<%--
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
--%>

<%@ include file="/init.jsp" %>

<%
String actionCommandName = (String)request.getAttribute(UsersAdminWebKeys.ACTION_COMMAND_NAME);
boolean editable = (boolean)request.getAttribute(UsersAdminWebKeys.EDITABLE);
String formLabel = (String)request.getAttribute(UsersAdminWebKeys.FORM_LABEL);
String jspPath = (String)request.getAttribute(UsersAdminWebKeys.JSP_PATH);

User selUser = PortalUtil.getSelectedUser(request);

request.setAttribute(UsersAdminWebKeys.SELECTED_USER, selUser);

if (selUser != null) {
	PortalUtil.setPageSubtitle(HtmlUtil.escape(selUser.getFullName()), request);
}

long selUserId = (selUser != null) ? selUser.getUserId() : 0;

String screenNavigationCategoryKey = ParamUtil.getString(request, "screenNavigationCategoryKey", UserScreenNavigationEntryConstants.CATEGORY_KEY_GENERAL);
String screenNavigationEntryKey = ParamUtil.getString(request, "screenNavigationEntryKey");
%>

<%
PortletURL viewURL = renderResponse.createRenderURL();

String backURL = ParamUtil.getString(request, "backURL", viewURL.toString());

if (!portletName.equals(UsersAdminPortletKeys.MY_ACCOUNT)) {
	portletDisplay.setShowBackIcon(true);
	portletDisplay.setURLBack(backURL);

	renderResponse.setTitle((selUser == null) ? LanguageUtil.get(request, "add-user") : LanguageUtil.format(request, "edit-user-x", HtmlUtil.escape(selUser.getFullName()), false));
}

String redirect = ParamUtil.getString(request, "redirect");

if (Validator.isNull(redirect)) {
	PortletURL redirectURL = renderResponse.createRenderURL();

	redirectURL.setParameter("mvcRenderCommandName", "/users_admin/edit_user");
	redirectURL.setParameter("backURL", backURL);
	redirectURL.setParameter("p_u_i_d", String.valueOf(selUserId));

	redirect = redirectURL.toString();
}

redirect = HttpUtil.addParameter(redirect, renderResponse.getNamespace() + "screenNavigationCategoryKey", screenNavigationCategoryKey);
redirect = HttpUtil.addParameter(redirect, renderResponse.getNamespace() + "screenNavigationEntryKey", screenNavigationEntryKey);
%>

<liferay-ui:success key="userAdded" message="the-user-was-created-successfully" />

<portlet:actionURL name="<%= actionCommandName %>" var="actionCommandURL" />

<aui:form action="<%= actionCommandURL %>" cssClass="portlet-users-admin-edit-user" data-senna-off="true" method="post" name="fm">
	<aui:input name="redirect" type="hidden" value="<%= redirect %>" />
	<aui:input name="p_u_i_d" type="hidden" value="<%= selUserId %>" />
	<aui:input name="screenNavigationCategoryKey" type="hidden" value="<%= screenNavigationCategoryKey %>" />
	<aui:input name="screenNavigationEntryKey" type="hidden" value="<%= screenNavigationEntryKey %>" />

	<div class="sheet sheet-lg">
		<c:if test="<%= (boolean)request.getAttribute(UsersAdminWebKeys.SHOW_TITLE) %>">
			<div class="sheet-header">
				<h2 class="sheet-title"><%= formLabel %></h2>
			</div>
		</c:if>

		<div class="sheet-section">
			<liferay-util:include page="<%= jspPath %>" servletContext="<%= application %>" />
		</div>

		<c:if test="<%= editable && (boolean)request.getAttribute(UsersAdminWebKeys.SHOW_CONTROLS) %>">
			<div class="sheet-footer">
				<aui:button primary="<%= true %>" type="submit" />

				<c:if test="<%= !portletName.equals(UsersAdminPortletKeys.MY_ACCOUNT) %>">
					<aui:button href="<%= backURL %>" type="cancel" />
				</c:if>
			</div>
		</c:if>
	</div>
</aui:form>