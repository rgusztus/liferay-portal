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

package com.liferay.talend.runtime.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import com.liferay.talend.connection.LiferayConnectionProperties;
import com.liferay.talend.exception.MalformedURLException;
import com.liferay.talend.runtime.client.exception.ConnectionException;
import com.liferay.talend.runtime.client.exception.OAuth2Exception;
import com.liferay.talend.utils.URIUtils;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.talend.daikon.exception.TalendRuntimeException;
import org.talend.daikon.properties.property.Property;

/**
 * @author Zoltán Takács
 * @author Igor Beslic
 */
public class RESTClient {

	public RESTClient(LiferayConnectionProperties liferayConnectionProperties) {
		this(liferayConnectionProperties, null);
	}

	public RESTClient(
		LiferayConnectionProperties liferayConnectionProperties,
		String target) {

		_liferayConnectionProperties = liferayConnectionProperties;

		String targetURL = target;

		if ((targetURL == null) || targetURL.isEmpty()) {
			targetURL = _getValue(liferayConnectionProperties.apiSpecURL);
		}

		_target = targetURL;

		_client = ClientBuilder.newClient(_getClientConfig());

		if (_log.isDebugEnabled()) {
			_log.debug("Created new REST Client for endpoint {}", target);
		}
	}

	public Response executeDeleteRequest() {
		WebTarget webTarget = _client.target(_getTargetURI());

		if (_log.isDebugEnabled()) {
			_log.debug("Target: {}", _getTarget());
		}

		Invocation.Builder builder = webTarget.request(
			MediaType.APPLICATION_JSON_TYPE);

		builder.header(
			"Authorization",
			_getAuthorizationHeader(_liferayConnectionProperties));

		return _invokeBuilder(HttpMethod.DELETE, builder);
	}

	@Deprecated
	public Response executeGetRequest() {
		URI decoratedURI = URIUtils.updateWithQueryParameters(
			_getTargetURI(), _getQueryParametersMap());

		WebTarget webTarget = _client.target(decoratedURI);

		if (_log.isDebugEnabled()) {
			_log.debug("Target: {}", decoratedURI.toASCIIString());
		}

		Invocation.Builder builder = webTarget.request(
			MediaType.APPLICATION_JSON_TYPE);

		builder.header(
			"Authorization",
			_getAuthorizationHeader(_liferayConnectionProperties));

		return _invokeBuilder(HttpMethod.GET, builder);
	}

	public Response executePatchRequest(JsonNode jsonNode) {
		WebTarget webTarget = _client.target(_getTargetURI());

		if (_log.isDebugEnabled()) {
			_log.debug("Target: {}", _getTarget());
		}

		Invocation.Builder builder = webTarget.request(
			MediaType.APPLICATION_JSON_TYPE);

		builder.header(
			"Authorization",
			_getAuthorizationHeader(_liferayConnectionProperties));

		Entity<String> entity = Entity.json(_jsonNodeToPrettyString(jsonNode));

		return _invokeBuilder(HttpMethod.PATCH, builder, entity);
	}

	public Response executePostRequest(JsonNode jsonNode) {
		WebTarget webTarget = _client.target(_getTargetURI());

		if (_log.isDebugEnabled()) {
			_log.debug("Target: {}", _getTarget());
		}

		Invocation.Builder builder = webTarget.request(
			MediaType.APPLICATION_JSON_TYPE);

		builder.header(
			"Authorization",
			_getAuthorizationHeader(_liferayConnectionProperties));

		Entity<String> entity = Entity.json(_jsonNodeToPrettyString(jsonNode));

		return _invokeBuilder(HttpMethod.POST, builder, entity);
	}

	public Response executePutRequest(JsonNode jsonNode) {
		WebTarget webTarget = _client.target(_getTargetURI());

		if (_log.isDebugEnabled()) {
			_log.debug("Target: {}", _getTarget());
		}

		Invocation.Builder builder = webTarget.request(
			MediaType.APPLICATION_JSON_TYPE);

		builder.header(
			"Authorization",
			_getAuthorizationHeader(_liferayConnectionProperties));

		Entity<String> entity = Entity.json(_jsonNodeToPrettyString(jsonNode));

		return _invokeBuilder(HttpMethod.PUT, builder, entity);
	}

	public boolean matches(String target) {
		if (Objects.equals(_target, target)) {
			return true;
		}

		return false;
	}

	@Override
	public String toString() {
		return String.format("REST API Client [%s].", _getTarget());
	}

	protected static final String HTTP = "http://";

	protected static final String HTTPS = "https://";

	protected final ObjectMapper objectMapper = new ObjectMapper();

	private JsonNode _asJsonNode(Response response) {
		try {
			String entity = response.readEntity(String.class);

			return objectMapper.readTree(entity);
		}
		catch (Throwable t) {
			throw TalendRuntimeException.createUnexpectedException(t);
		}
	}

	private Response _executeAccessTokenPostRequest(
		LiferayConnectionProperties liferayConnectionProperties) {

		String serverInstanceURL = _extractServerInstanceURL(_getTarget());

		WebTarget webTarget = _client.target(
			serverInstanceURL + _LIFERAY_OAUTH2_ACCESS_TOKEN_ENDPOINT);

		Invocation.Builder builder = webTarget.request(
			MediaType.APPLICATION_JSON_TYPE);

		XWWWFormURLEncoder xWWWFormURLEncoder = new XWWWFormURLEncoder();

		Entity<Form> entity = Entity.form(
			xWWWFormURLEncoder.toForm(
				"client_id",
				_getValue(liferayConnectionProperties.oauthClientId),
				"client_secret",
				_getValue(liferayConnectionProperties.oauthClientSecret),
				"grant_type", "client_credentials", "response_type", "code"));

		return _invokeBuilder(HttpMethod.POST, builder, entity);
	}

	private String _extractServerInstanceURL(String openAPISpecRef) {
		Matcher serverURLMatcher = _openAPISpecURLPattern.matcher(
			openAPISpecRef);

		if (!serverURLMatcher.matches()) {
			throw new MalformedURLException(
				"Unable to extract Open API endpoint from URL " +
					openAPISpecRef);
		}

		return serverURLMatcher.group(1);
	}

	private Response _follow3Redirects(Response currentResponse) {
		Response.StatusType statusType = currentResponse.getStatusInfo();

		if (statusType.getFamily() != Response.Status.Family.REDIRECTION) {
			return currentResponse;
		}

		AtomicInteger counter = new AtomicInteger();
		Response response = currentResponse;

		while ((statusType.getFamily() == Response.Status.Family.REDIRECTION) &&
			   (counter.incrementAndGet() <= 3)) {

			String location = response.getHeaderString(HttpHeaders.LOCATION);

			if (StringUtils.isEmpty(location)) {
				return response;
			}

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Redirect location {}#: {}", counter.get(), location);
			}

			response.close();

			WebTarget webTarget = _client.target(location);

			Invocation.Builder builder = webTarget.request(
				MediaType.APPLICATION_JSON_TYPE);

			response = builder.get();
		}

		return response;
	}

	private String _getAuthorizationHeader(
		LiferayConnectionProperties liferayConnectionProperties) {

		if (liferayConnectionProperties.isOAuth2Authorization()) {
			return "Bearer " + _getBearerToken(liferayConnectionProperties);
		}

		return "Basic " + _getBasicToken(liferayConnectionProperties);
	}

	private String _getBasicToken(
		LiferayConnectionProperties liferayConnectionProperties) {

		Base64.Encoder base64Encoder = Base64.getEncoder();

		StringBuilder sb = new StringBuilder();

		sb.append(_getValue(liferayConnectionProperties.userId));
		sb.append(":");
		sb.append(_getValue(liferayConnectionProperties.password));

		String base64Seed = sb.toString();

		return new String(base64Encoder.encode(base64Seed.getBytes()));
	}

	private String _getBearerToken(
		LiferayConnectionProperties liferayConnectionProperties) {

		JsonNode authorizationJsonNode = _requestAuthorizationJsonNode(
			liferayConnectionProperties);

		JsonNode tokenTypeJsonNode = authorizationJsonNode.get("token_type");

		String tokenType = tokenTypeJsonNode.asText();

		if (!Objects.equals(tokenType, "Bearer")) {
			throw new OAuth2Exception(
				"Unexpected token type received " + tokenType);
		}

		JsonNode accessTokenJsonNode = authorizationJsonNode.get(
			"access_token");

		return accessTokenJsonNode.asText();
	}

	private ClientConfig _getClientConfig() {
		ClientConfig clientConfig = new ClientConfig();

		clientConfig = clientConfig.property(
			ClientProperties.CONNECT_TIMEOUT,
			_liferayConnectionProperties.connectTimeout.getValue() * 1000);

		clientConfig = clientConfig.property(
			ClientProperties.READ_TIMEOUT,
			_liferayConnectionProperties.readTimeout.getValue() * 1000);

		return clientConfig;
	}

	private List<String> _getContentType(Response response) {
		String contentTypeHeader = response.getHeaderString("Content-Type");

		if ((contentTypeHeader == null) || contentTypeHeader.isEmpty()) {
			return Collections.emptyList();
		}

		String[] headers = contentTypeHeader.split(",");

		List<String> headerValues = new ArrayList<>();

		for (String header : headers) {
			headerValues.add(header);
		}

		return headerValues;
	}

	private Map<String, String> _getQueryParametersMap() {
		Map<String, String> parameters = new HashMap<>();

		parameters.put(
			"pageSize",
			_liferayConnectionProperties.itemsPerPage.getStringValue());

		return parameters;
	}

	private String _getTarget() {
		boolean forceHttps = _getValue(_liferayConnectionProperties.forceHttps);

		if (forceHttps) {
			return _replaceHttpSchemeWithHttps(_target);
		}

		return _target;
	}

	private URI _getTargetURI() {
		try {
			return new URI(_getTarget());
		}
		catch (URISyntaxException urise) {
			_log.error("Unable to parse {} as a URI reference", _getTarget());
		}

		return null;
	}

	private <T> T _getValue(Property<T> property) {
		return property.getValue();
	}

	private Response _handleResponse(
		String httpMethod, Invocation.Builder builder, Entity<?> entity) {

		boolean followRedirects =
			_liferayConnectionProperties.followRedirects.getValue();

		if (followRedirects) {
			return _follow3Redirects(builder.method(httpMethod, entity));
		}

		return builder.method(httpMethod, entity);
	}

	private Response _invokeBuilder(
		String httpMethod, Invocation.Builder builder) {

		return _invokeBuilder(httpMethod, builder, null);
	}

	private Response _invokeBuilder(
		String httpMethod, Invocation.Builder builder, Entity<?> entity) {

		Response response = _handleResponse(httpMethod, builder, entity);

		Response.StatusType statusType = response.getStatusInfo();

		if (statusType.getFamily() == Response.Status.Family.SUCCESSFUL) {
			return response;
		}

		String messageEntity = response.readEntity(String.class);
		int statusCode = response.getStatus();

		if (_log.isDebugEnabled()) {
			_log.debug(
				"{} request failed: {}. \n{}", httpMethod, statusCode,
				messageEntity);
		}

		throw TalendRuntimeException.createUnexpectedException(
			"HTTP Code: " + statusCode + "\nRequest failed: \n" +
				messageEntity);
	}

	private boolean _isApplicationJsonContentType(Response response) {
		List<String> strings = _getContentType(response);

		if (strings.contains("application/json")) {
			return true;
		}

		return false;
	}

	private String _jsonNodeToPrettyString(JsonNode jsonNode) {
		String json;

		try {
			ObjectWriter objectWriter =
				objectMapper.writerWithDefaultPrettyPrinter();

			json = objectWriter.writeValueAsString(jsonNode);
		}
		catch (JsonProcessingException jpe) {
			if (_log.isDebugEnabled()) {
				_log.debug(
					"Unable to convert JsonNode to a String representation");
			}

			throw TalendRuntimeException.createUnexpectedException(jpe);
		}

		return json;
	}

	private String _replaceHttpSchemeWithHttps(String url) {
		String lowerCasedUrl = StringUtils.lowerCase(url);

		if (lowerCasedUrl.startsWith(HTTP)) {
			return HTTPS.concat(url.substring(HTTP.length()));
		}

		return url;
	}

	private JsonNode _requestAuthorizationJsonNode(
			LiferayConnectionProperties liferayConnectionProperties)
		throws ConnectionException {

		Response response = _executeAccessTokenPostRequest(
			liferayConnectionProperties);

		if (response == null) {
			throw new OAuth2Exception(
				"Authorization request failed for unresponsive OAuth 2.0 " +
					"endpoint");
		}

		if (response.getStatus() != 200) {
			throw new OAuth2Exception(
				String.format(
					"OAuth 2.0 check failed with response status {%s}",
					response.getStatus()));
		}

		if (!_isApplicationJsonContentType(response)) {
			List<String> contentTypeValues = _getContentType(response);

			throw new OAuth2Exception(
				String.format(
					"OAuth 2.0 check failed with response status and {%s} " +
						"content type {%s}",
					response.getStatus(), contentTypeValues.get(0)));
		}

		return _asJsonNode(response);
	}

	private static final String _LIFERAY_OAUTH2_ACCESS_TOKEN_ENDPOINT =
		"/o/oauth2/token";

	private static final Logger _log = LoggerFactory.getLogger(
		RESTClient.class);

	private static final Pattern _openAPISpecURLPattern = Pattern.compile(
		"(https?://.+(:\\d+)?)(/o/(.+)/)(v\\d+(.\\d+)*)/openapi\\.(yaml|json)");

	private final Client _client;
	private final LiferayConnectionProperties _liferayConnectionProperties;
	private final String _target;

}