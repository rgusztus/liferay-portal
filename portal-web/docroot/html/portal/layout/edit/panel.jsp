<%--
/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

<%@ include file="/html/portal/layout/edit/init.jsp" %>

<%
UnicodeProperties typeSettingsProperties = selLayout.getTypeSettingsProperties();

String description = typeSettingsProperties.getProperty("description", StringPool.BLANK);
String panelSelectedPortlets = typeSettingsProperties.getProperty("panelSelectedPortlets", StringPool.BLANK);
%>

<aui:input cssClass="layout-description" label="description" name="TypeSettingsProperties--description--" type="textarea" value="<%= description %>" wrap="soft" />

<div class="portlet-msg-info">
	<liferay-ui:message key="select-the-applications-that-will-be-available-in-the-panel" />
</div>

<aui:input id="panelSelectedPortlets" name="TypeSettingsProperties--panelSelectedPortlets--" type="hidden" value="<%= panelSelectedPortlets %>" />

<%
String panelTreeKey = "panelSelectedPortletsPanelTree";
%>

<div class="lfr-tree-loading" id="<portlet:namespace />panelSelectPortletsTree">
	<span class="aui-icon aui-icon-loading lfr-tree-loading-icon"></span>
</div>

<div id="<portlet:namespace />panelSelectPortletsOutput" style="margin: 4px;"></div>

<aui:script use="aui-io-request,aui-tree-view,dataschema-xml,datatype-xml,json-parse">
	var panelSelectedPortletsEl = A.one('#<portlet:namespace />panelSelectedPortlets');
	var selectedPortlets = A.Array.hash(panelSelectedPortletsEl.val().split(','));

	var TreeUtil = {
		afterRenderTree: function(event) {
			var rootNode = event.target.item(0);

			var loadingEl = A.one('#<portlet:namespace />panelSelectPortletsTree');

			loadingEl.hide();

			rootNode.expand();
		},

		formatJSONResults: function(json) {
			var output = [];

			A.each(
				json.children.list,
				function(item, index, collection) {
					var childPortlets = [];
					var total = 0;

					var name = item.name;
					var nodeChildren = item.children;
					var id = item.id;
					var leaf = item.leaf;
					var plid = item.objId;

					var checked = plid && (plid in selectedPortlets);

					if (nodeChildren) {
						childPortlets = nodeChildren.list;
						total = childPortlets.length;
					}

					var newNode = {
						after: {
							checkedChange: function(event) {
								if (plid) {
									if (event.newVal && checked) {
										selectedPortlets[plid] = true;

										panelSelectedPortletsEl.val(A.Object.keys(selectedPortlets));
									}
									else {
										if (selectedPortlets[plid]) {
											delete selectedPortlets[plid];
										}

										panelSelectedPortletsEl.val(A.Object.keys(selectedPortlets));
									}
								}
							}
						},
						alwaysShowHitArea: total,
						checked: checked,
						draggable: false,
						expanded: false,
						id: id,
						label: name,
						leaf: leaf,
						type: 'task'
					}

					if (nodeChildren) {
						newNode.children = TreeUtil.formatJSONResults(item);
					}

					output.push(newNode);
				}
			);

			return output;
		}
	};

	var initPanelSelectPortlets = function(event) {

		<%
		PortletLister portletLister = PortletListerFactoryUtil.getPortletLister();

		portletLister.setIncludeInstanceablePortlets(false);
		portletLister.setIteratePortlets(true);
		portletLister.setLayoutTypePortlet(layoutTypePortlet);
		portletLister.setRootNodeName(LanguageUtil.get(pageContext, "application"));
		portletLister.setServletContext(application);
		portletLister.setThemeDisplay(themeDisplay);
		portletLister.setUser(user);
		portletLister.setHierarchicalTree(true);

		JSONObject portletsJSON = JSONFactoryUtil.createJSONObject(JSONFactoryUtil.serialize(portletLister.getTreeView()));
		%>

		var tree = <%= portletsJSON %>;

		var portletList = tree.serializable.list.list[0];

		var rootNode = new A.TreeNodeTask(
			{
				alwaysShowHitArea: true,
				children: TreeUtil.formatJSONResults(portletList),
				draggable: false,
				id: '<portlet:namespace />panelSelectPortlets',
				label: portletList.name,
				leaf: false
			}
		);

		var treeview = new A.TreeView(
			{
				after: {
					render: TreeUtil.afterRenderTree
				},
				boundingBox: '#<portlet:namespace />panelSelectPortletsOutput',
				children: [rootNode],
				type: 'file'
			}
		).render();

		initPanelSelectPortlets = A.Lang.emptyFn;
	};

	if (<%= selLayout.isTypePanel() %>) {
		initPanelSelectPortlets();
	}

	Liferay.on(
		'<portlet:namespace />toggleLayoutTypeFields',
		function(event) {
			if (event.type == 'panel') {
				initPanelSelectPortlets();
			}
		}
	);
</aui:script>