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

import React, {useState, useEffect} from 'react';
import ClayColorPicker from '@clayui/color-picker';
import getConnectedReactComponentAdapter from './ReactComponentAdapter.es';
import templates from './ColorPickerComponent.soy.js';

const ColorPickerWithState = ({onChange, value, ref, ...props}) => {
	const [customColors, setCustoms] = useState(['008000', '00FFFF', '0000FF']);
	const [color, setColor] = useState(customColors[0]);

	if (!value) {
		value = color;
	}

	useEffect(() => {
		setColor(value);
	}, [value]);

	return (
		<ClayColorPicker
			colors={customColors}
			onColorsChange={setCustoms}
			onValueChange={value => {
				setColor(value);
				onChange({type: 'value', newValue: value});
			}}
			value={color}
		/>
	);
};

const ColorPickerComponent = getConnectedReactComponentAdapter(
	ColorPickerWithState,
	templates
);

export {ColorPickerComponent};
export default ColorPickerComponent;
