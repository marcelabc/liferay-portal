package com.liferay.data.engine.rest.internal.field.type.v1_0;

import com.liferay.data.engine.field.type.BaseFieldType;
import com.liferay.data.engine.field.type.FieldType;
import com.liferay.data.engine.spi.dto.SPIDataDefinitionField;
import com.liferay.portal.kernel.util.MapUtil;
import org.osgi.service.component.annotations.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Marcela Cunha
 */
@Component(
	immediate = true,
	property = {
		"data.engine.field.type.data.domain=color",
		"data.engine.field.type.description=color-field-type-description",
		"data.engine.field.type.display.order:Integer=8",
		"data.engine.field.type.group=basic",
		"data.engine.field.type.icon=color",
		"data.engine.field.type.js.module=dynamic-data-mapping-form-field-type/ColorPicker/ColorPicker.es",
		"data.engine.field.type.label=color-field-type-label"
	},
	service = FieldType.class
)
public class ColorFieldType extends BaseFieldType {

	@Override
	public String getName() {
		return "color";
	}

	@Override
	protected void includeContext(
		Map<String, Object> context, HttpServletRequest httpServletRequest,
		HttpServletResponse httpServletResponse,
		SPIDataDefinitionField spiDataDefinitionField) {

		context.put(
			"value",
			MapUtil.getString(
				spiDataDefinitionField.getCustomProperties(), "value"));
	}

}
