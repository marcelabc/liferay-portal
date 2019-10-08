package com.liferay.dynamic.data.mapping.form.field.type.internal.color;

import com.liferay.dynamic.data.mapping.annotations.DDMForm;
import com.liferay.dynamic.data.mapping.annotations.DDMFormField;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayout;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutColumn;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutPage;
import com.liferay.dynamic.data.mapping.annotations.DDMFormLayoutRow;
import com.liferay.dynamic.data.mapping.form.field.type.DefaultDDMFormFieldTypeSettings;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;

/**
 * @author Marcela Cunha
 */
@DDMForm
@DDMFormLayout(
	paginationMode = com.liferay.dynamic.data.mapping.model.DDMFormLayout.TABBED_MODE,
	value = {
		@DDMFormLayoutPage(
			title = "%basic",
			value = {
				@DDMFormLayoutRow(
					{
						@DDMFormLayoutColumn(
							size = 12, value = {"label", "tip", "required"}
						)
					}
				)
			}
		),
		@DDMFormLayoutPage(
			title = "%properties",
			value = {
				@DDMFormLayoutRow(
					{
						@DDMFormLayoutColumn(
							size = 12,
							value = {
								"predefinedValue", "fieldNamespace", "indexType",
								"localizable", "readOnly", "name", "showLabel",
								"repeatable"
							}
						)
					}
				)
			}
		)
	}
)
public interface ColorDDMFormFieldTypeSettings extends
	DefaultDDMFormFieldTypeSettings {

	@DDMFormField(
		dataType = "string", label = "%predefined-value", type = "color"
	)
	@Override
	public LocalizedValue predefinedValue();

}
