package ${packagePath}.uad.exporter;

import ${apiPackagePath}.model.${entity.name};
import ${apiPackagePath}.service.${entity.name}LocalService;
import ${packagePath}.uad.constants.${portletShortName}UADConstants;

import com.liferay.portal.kernel.dao.orm.ActionableDynamicQuery;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.user.associated.data.exporter.DynamicQueryUADExporter;
import com.liferay.user.associated.data.exporter.UADExporter;

import org.osgi.service.component.annotations.Reference;

/**
 * Provides the base implementation for the ${entity.humanName} UAD exporter.
 *
 * <p>
 * This implementation exists only as a container for the default methods
 * generated by ServiceBuilder. All custom service methods should be put in
 * {@link ${packagePath}.uad.exporter.${entity.name}UADExporter}.
 * </p>
 *
 * @author ${author}
 * @generated
 */
public abstract class Base${entity.name}UADExporter extends DynamicQueryUADExporter<${entity.name}> {

	@Override
	public String getApplicationName() {
		return ${portletShortName}UADConstants.APPLICATION_NAME;
	}

	@Override
	protected ActionableDynamicQuery doGetActionableDynamicQuery() {
		return ${entity.varName}LocalService.getActionableDynamicQuery();
	}

	@Override
	protected String[] doGetUserIdFieldNames() {
		return ${portletShortName}UADConstants.USER_ID_FIELD_NAMES_${entity.constantName};
	}

	@Override
	protected String toXmlString(${entity.name} ${entity.varName}) {
		StringBundler sb = new StringBundler(${entity.UADEntityColumns?size * 3 + 4});

		sb.append("<model><model-name>");
		sb.append("${apiPackagePath}.model.${entity.name}");
		sb.append("</model-name>");

		<#list entity.UADEntityColumns as entityColumn>
			<#if !stringUtil.equals(entityColumn.type, "Blob") || !entityColumn.lazy>
				sb.append("<column><column-name>${entityColumn.name}</column-name><column-value><![CDATA[");
				sb.append(${entity.varName}.get${entityColumn.methodName}());
				sb.append("]]></column-value></column>");
			</#if>
		</#list>

		sb.append("</model>");

		return sb.toString();
	}

	@Reference
	protected ${entity.name}LocalService ${entity.varName}LocalService;

}