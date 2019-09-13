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

package com.liferay.wiki.model.impl;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.expando.kernel.util.ExpandoBridgeFactoryUtil;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.bean.AutoEscapeBeanHandler;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.model.impl.BaseModelImpl;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.wiki.model.WikiPageResource;
import com.liferay.wiki.model.WikiPageResourceModel;

import java.io.Serializable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;

import java.sql.Types;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * The base model implementation for the WikiPageResource service. Represents a row in the &quot;WikiPageResource&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This implementation and its corresponding interface </code>WikiPageResourceModel</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in {@link WikiPageResourceImpl}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see WikiPageResourceImpl
 * @generated
 */
public class WikiPageResourceModelImpl
	extends BaseModelImpl<WikiPageResource> implements WikiPageResourceModel {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. All methods that expect a wiki page resource model instance should use the <code>WikiPageResource</code> interface instead.
	 */
	public static final String TABLE_NAME = "WikiPageResource";

	public static final Object[][] TABLE_COLUMNS = {
		{"mvccVersion", Types.BIGINT}, {"uuid_", Types.VARCHAR},
		{"resourcePrimKey", Types.BIGINT}, {"groupId", Types.BIGINT},
		{"companyId", Types.BIGINT}, {"nodeId", Types.BIGINT},
		{"title", Types.VARCHAR}
	};

	public static final Map<String, Integer> TABLE_COLUMNS_MAP =
		new HashMap<String, Integer>();

	static {
		TABLE_COLUMNS_MAP.put("mvccVersion", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("uuid_", Types.VARCHAR);
		TABLE_COLUMNS_MAP.put("resourcePrimKey", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("groupId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("companyId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("nodeId", Types.BIGINT);
		TABLE_COLUMNS_MAP.put("title", Types.VARCHAR);
	}

	public static final String TABLE_SQL_CREATE =
		"create table WikiPageResource (mvccVersion LONG default 0 not null,uuid_ VARCHAR(75) null,resourcePrimKey LONG not null primary key,groupId LONG,companyId LONG,nodeId LONG,title VARCHAR(255) null)";

	public static final String TABLE_SQL_DROP = "drop table WikiPageResource";

	public static final String ORDER_BY_JPQL =
		" ORDER BY wikiPageResource.resourcePrimKey ASC";

	public static final String ORDER_BY_SQL =
		" ORDER BY WikiPageResource.resourcePrimKey ASC";

	public static final String DATA_SOURCE = "liferayDataSource";

	public static final String SESSION_FACTORY = "liferaySessionFactory";

	public static final String TX_MANAGER = "liferayTransactionManager";

	public static final long COMPANYID_COLUMN_BITMASK = 1L;

	public static final long GROUPID_COLUMN_BITMASK = 2L;

	public static final long NODEID_COLUMN_BITMASK = 4L;

	public static final long TITLE_COLUMN_BITMASK = 8L;

	public static final long UUID_COLUMN_BITMASK = 16L;

	public static final long RESOURCEPRIMKEY_COLUMN_BITMASK = 32L;

	public static void setEntityCacheEnabled(boolean entityCacheEnabled) {
		_entityCacheEnabled = entityCacheEnabled;
	}

	public static void setFinderCacheEnabled(boolean finderCacheEnabled) {
		_finderCacheEnabled = finderCacheEnabled;
	}

	public WikiPageResourceModelImpl() {
	}

	@Override
	public long getPrimaryKey() {
		return _resourcePrimKey;
	}

	@Override
	public void setPrimaryKey(long primaryKey) {
		setResourcePrimKey(primaryKey);
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _resourcePrimKey;
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		setPrimaryKey(((Long)primaryKeyObj).longValue());
	}

	@Override
	public Class<?> getModelClass() {
		return WikiPageResource.class;
	}

	@Override
	public String getModelClassName() {
		return WikiPageResource.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<WikiPageResource, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		for (Map.Entry<String, Function<WikiPageResource, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<WikiPageResource, Object> attributeGetterFunction =
				entry.getValue();

			attributes.put(
				attributeName,
				attributeGetterFunction.apply((WikiPageResource)this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<WikiPageResource, Object>>
			attributeSetterBiConsumers = getAttributeSetterBiConsumers();

		for (Map.Entry<String, Object> entry : attributes.entrySet()) {
			String attributeName = entry.getKey();

			BiConsumer<WikiPageResource, Object> attributeSetterBiConsumer =
				attributeSetterBiConsumers.get(attributeName);

			if (attributeSetterBiConsumer != null) {
				attributeSetterBiConsumer.accept(
					(WikiPageResource)this, entry.getValue());
			}
		}
	}

	public Map<String, Function<WikiPageResource, Object>>
		getAttributeGetterFunctions() {

		return _attributeGetterFunctions;
	}

	public Map<String, BiConsumer<WikiPageResource, Object>>
		getAttributeSetterBiConsumers() {

		return _attributeSetterBiConsumers;
	}

	private static Function<InvocationHandler, WikiPageResource>
		_getProxyProviderFunction() {

		Class<?> proxyClass = ProxyUtil.getProxyClass(
			WikiPageResource.class.getClassLoader(), WikiPageResource.class,
			ModelWrapper.class);

		try {
			Constructor<WikiPageResource> constructor =
				(Constructor<WikiPageResource>)proxyClass.getConstructor(
					InvocationHandler.class);

			return invocationHandler -> {
				try {
					return constructor.newInstance(invocationHandler);
				}
				catch (ReflectiveOperationException roe) {
					throw new InternalError(roe);
				}
			};
		}
		catch (NoSuchMethodException nsme) {
			throw new InternalError(nsme);
		}
	}

	private static final Map<String, Function<WikiPageResource, Object>>
		_attributeGetterFunctions;
	private static final Map<String, BiConsumer<WikiPageResource, Object>>
		_attributeSetterBiConsumers;

	static {
		Map<String, Function<WikiPageResource, Object>>
			attributeGetterFunctions =
				new LinkedHashMap<String, Function<WikiPageResource, Object>>();
		Map<String, BiConsumer<WikiPageResource, ?>>
			attributeSetterBiConsumers =
				new LinkedHashMap<String, BiConsumer<WikiPageResource, ?>>();

		attributeGetterFunctions.put(
			"mvccVersion", WikiPageResource::getMvccVersion);
		attributeSetterBiConsumers.put(
			"mvccVersion",
			(BiConsumer<WikiPageResource, Long>)
				WikiPageResource::setMvccVersion);
		attributeGetterFunctions.put("uuid", WikiPageResource::getUuid);
		attributeSetterBiConsumers.put(
			"uuid",
			(BiConsumer<WikiPageResource, String>)WikiPageResource::setUuid);
		attributeGetterFunctions.put(
			"resourcePrimKey", WikiPageResource::getResourcePrimKey);
		attributeSetterBiConsumers.put(
			"resourcePrimKey",
			(BiConsumer<WikiPageResource, Long>)
				WikiPageResource::setResourcePrimKey);
		attributeGetterFunctions.put("groupId", WikiPageResource::getGroupId);
		attributeSetterBiConsumers.put(
			"groupId",
			(BiConsumer<WikiPageResource, Long>)WikiPageResource::setGroupId);
		attributeGetterFunctions.put(
			"companyId", WikiPageResource::getCompanyId);
		attributeSetterBiConsumers.put(
			"companyId",
			(BiConsumer<WikiPageResource, Long>)WikiPageResource::setCompanyId);
		attributeGetterFunctions.put("nodeId", WikiPageResource::getNodeId);
		attributeSetterBiConsumers.put(
			"nodeId",
			(BiConsumer<WikiPageResource, Long>)WikiPageResource::setNodeId);
		attributeGetterFunctions.put("title", WikiPageResource::getTitle);
		attributeSetterBiConsumers.put(
			"title",
			(BiConsumer<WikiPageResource, String>)WikiPageResource::setTitle);

		_attributeGetterFunctions = Collections.unmodifiableMap(
			attributeGetterFunctions);
		_attributeSetterBiConsumers = Collections.unmodifiableMap(
			(Map)attributeSetterBiConsumers);
	}

	@Override
	public long getMvccVersion() {
		return _mvccVersion;
	}

	@Override
	public void setMvccVersion(long mvccVersion) {
		_mvccVersion = mvccVersion;
	}

	@Override
	public String getUuid() {
		if (_uuid == null) {
			return "";
		}
		else {
			return _uuid;
		}
	}

	@Override
	public void setUuid(String uuid) {
		_columnBitmask |= UUID_COLUMN_BITMASK;

		if (_originalUuid == null) {
			_originalUuid = _uuid;
		}

		_uuid = uuid;
	}

	public String getOriginalUuid() {
		return GetterUtil.getString(_originalUuid);
	}

	@Override
	public long getResourcePrimKey() {
		return _resourcePrimKey;
	}

	@Override
	public void setResourcePrimKey(long resourcePrimKey) {
		_resourcePrimKey = resourcePrimKey;
	}

	@Override
	public long getGroupId() {
		return _groupId;
	}

	@Override
	public void setGroupId(long groupId) {
		_columnBitmask |= GROUPID_COLUMN_BITMASK;

		if (!_setOriginalGroupId) {
			_setOriginalGroupId = true;

			_originalGroupId = _groupId;
		}

		_groupId = groupId;
	}

	public long getOriginalGroupId() {
		return _originalGroupId;
	}

	@Override
	public long getCompanyId() {
		return _companyId;
	}

	@Override
	public void setCompanyId(long companyId) {
		_columnBitmask |= COMPANYID_COLUMN_BITMASK;

		if (!_setOriginalCompanyId) {
			_setOriginalCompanyId = true;

			_originalCompanyId = _companyId;
		}

		_companyId = companyId;
	}

	public long getOriginalCompanyId() {
		return _originalCompanyId;
	}

	@Override
	public long getNodeId() {
		return _nodeId;
	}

	@Override
	public void setNodeId(long nodeId) {
		_columnBitmask |= NODEID_COLUMN_BITMASK;

		if (!_setOriginalNodeId) {
			_setOriginalNodeId = true;

			_originalNodeId = _nodeId;
		}

		_nodeId = nodeId;
	}

	public long getOriginalNodeId() {
		return _originalNodeId;
	}

	@Override
	public String getTitle() {
		if (_title == null) {
			return "";
		}
		else {
			return _title;
		}
	}

	@Override
	public void setTitle(String title) {
		_columnBitmask |= TITLE_COLUMN_BITMASK;

		if (_originalTitle == null) {
			_originalTitle = _title;
		}

		_title = title;
	}

	public String getOriginalTitle() {
		return GetterUtil.getString(_originalTitle);
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return ExpandoBridgeFactoryUtil.getExpandoBridge(
			getCompanyId(), WikiPageResource.class.getName(), getPrimaryKey());
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		ExpandoBridge expandoBridge = getExpandoBridge();

		expandoBridge.setAttributes(serviceContext);
	}

	@Override
	public WikiPageResource toEscapedModel() {
		if (_escapedModel == null) {
			Function<InvocationHandler, WikiPageResource>
				escapedModelProxyProviderFunction =
					EscapedModelProxyProviderFunctionHolder.
						_escapedModelProxyProviderFunction;

			_escapedModel = escapedModelProxyProviderFunction.apply(
				new AutoEscapeBeanHandler(this));
		}

		return _escapedModel;
	}

	@Override
	public Object clone() {
		WikiPageResourceImpl wikiPageResourceImpl = new WikiPageResourceImpl();

		wikiPageResourceImpl.setMvccVersion(getMvccVersion());
		wikiPageResourceImpl.setUuid(getUuid());
		wikiPageResourceImpl.setResourcePrimKey(getResourcePrimKey());
		wikiPageResourceImpl.setGroupId(getGroupId());
		wikiPageResourceImpl.setCompanyId(getCompanyId());
		wikiPageResourceImpl.setNodeId(getNodeId());
		wikiPageResourceImpl.setTitle(getTitle());

		wikiPageResourceImpl.resetOriginalValues();

		return wikiPageResourceImpl;
	}

	@Override
	public int compareTo(WikiPageResource wikiPageResource) {
		long primaryKey = wikiPageResource.getPrimaryKey();

		if (getPrimaryKey() < primaryKey) {
			return -1;
		}
		else if (getPrimaryKey() > primaryKey) {
			return 1;
		}
		else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof WikiPageResource)) {
			return false;
		}

		WikiPageResource wikiPageResource = (WikiPageResource)obj;

		long primaryKey = wikiPageResource.getPrimaryKey();

		if (getPrimaryKey() == primaryKey) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int)getPrimaryKey();
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _entityCacheEnabled;
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _finderCacheEnabled;
	}

	@Override
	public void resetOriginalValues() {
		WikiPageResourceModelImpl wikiPageResourceModelImpl = this;

		wikiPageResourceModelImpl._originalUuid =
			wikiPageResourceModelImpl._uuid;

		wikiPageResourceModelImpl._originalGroupId =
			wikiPageResourceModelImpl._groupId;

		wikiPageResourceModelImpl._setOriginalGroupId = false;

		wikiPageResourceModelImpl._originalCompanyId =
			wikiPageResourceModelImpl._companyId;

		wikiPageResourceModelImpl._setOriginalCompanyId = false;

		wikiPageResourceModelImpl._originalNodeId =
			wikiPageResourceModelImpl._nodeId;

		wikiPageResourceModelImpl._setOriginalNodeId = false;

		wikiPageResourceModelImpl._originalTitle =
			wikiPageResourceModelImpl._title;

		wikiPageResourceModelImpl._columnBitmask = 0;
	}

	@Override
	public CacheModel<WikiPageResource> toCacheModel() {
		WikiPageResourceCacheModel wikiPageResourceCacheModel =
			new WikiPageResourceCacheModel();

		wikiPageResourceCacheModel.mvccVersion = getMvccVersion();

		wikiPageResourceCacheModel.uuid = getUuid();

		String uuid = wikiPageResourceCacheModel.uuid;

		if ((uuid != null) && (uuid.length() == 0)) {
			wikiPageResourceCacheModel.uuid = null;
		}

		wikiPageResourceCacheModel.resourcePrimKey = getResourcePrimKey();

		wikiPageResourceCacheModel.groupId = getGroupId();

		wikiPageResourceCacheModel.companyId = getCompanyId();

		wikiPageResourceCacheModel.nodeId = getNodeId();

		wikiPageResourceCacheModel.title = getTitle();

		String title = wikiPageResourceCacheModel.title;

		if ((title != null) && (title.length() == 0)) {
			wikiPageResourceCacheModel.title = null;
		}

		return wikiPageResourceCacheModel;
	}

	@Override
	public String toString() {
		Map<String, Function<WikiPageResource, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			4 * attributeGetterFunctions.size() + 2);

		sb.append("{");

		for (Map.Entry<String, Function<WikiPageResource, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<WikiPageResource, Object> attributeGetterFunction =
				entry.getValue();

			sb.append(attributeName);
			sb.append("=");
			sb.append(attributeGetterFunction.apply((WikiPageResource)this));
			sb.append(", ");
		}

		if (sb.index() > 1) {
			sb.setIndex(sb.index() - 1);
		}

		sb.append("}");

		return sb.toString();
	}

	@Override
	public String toXmlString() {
		Map<String, Function<WikiPageResource, Object>>
			attributeGetterFunctions = getAttributeGetterFunctions();

		StringBundler sb = new StringBundler(
			5 * attributeGetterFunctions.size() + 4);

		sb.append("<model><model-name>");
		sb.append(getModelClassName());
		sb.append("</model-name>");

		for (Map.Entry<String, Function<WikiPageResource, Object>> entry :
				attributeGetterFunctions.entrySet()) {

			String attributeName = entry.getKey();
			Function<WikiPageResource, Object> attributeGetterFunction =
				entry.getValue();

			sb.append("<column><column-name>");
			sb.append(attributeName);
			sb.append("</column-name><column-value><![CDATA[");
			sb.append(attributeGetterFunction.apply((WikiPageResource)this));
			sb.append("]]></column-value></column>");
		}

		sb.append("</model>");

		return sb.toString();
	}

	private static class EscapedModelProxyProviderFunctionHolder {

		private static final Function<InvocationHandler, WikiPageResource>
			_escapedModelProxyProviderFunction = _getProxyProviderFunction();

	}

	private static boolean _entityCacheEnabled;
	private static boolean _finderCacheEnabled;

	private long _mvccVersion;
	private String _uuid;
	private String _originalUuid;
	private long _resourcePrimKey;
	private long _groupId;
	private long _originalGroupId;
	private boolean _setOriginalGroupId;
	private long _companyId;
	private long _originalCompanyId;
	private boolean _setOriginalCompanyId;
	private long _nodeId;
	private long _originalNodeId;
	private boolean _setOriginalNodeId;
	private String _title;
	private String _originalTitle;
	private long _columnBitmask;
	private WikiPageResource _escapedModel;

}