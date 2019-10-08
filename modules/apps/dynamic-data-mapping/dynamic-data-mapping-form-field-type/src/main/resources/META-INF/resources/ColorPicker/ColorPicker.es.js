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

import '../FieldBase/FieldBase.es';
import './ColorPickerComponent.soy.js';
import './ColorPickerRegister.soy.js';
import Component from 'metal-component';
import Soy from 'metal-soy';
import templates from './ColorPicker.soy.js';
import {Config} from 'metal-state';

class ColorPicker extends Component {
	_handleOnChange(event) {
		switch (event.payload.type) {
			case 'value':
				this.emit('fieldEdited', {
					fieldInstance: this,
					originalEvent: event,
					value: event.payload.newValue
				});
				break;
		}
	}

	willReceiveState(changes) {
		if (changes.value) {
			this.setState({
				value: changes.value.newVal
			});
		}
	}


}

ColorPicker.STATE = {
	/**
	 * @default undefined
	 * @instance
	 * @memberof Text
	 * @type {?(string|undefined)}
	 */

	errorMessage: Config.string(),

	/**
	 * @default false
	 * @instance
	 * @memberof ColorPicker
	 * @type {?bool}
	 */

	evaluable: Config.bool().value(false),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Text
	 * @type {?(string|undefined)}
	 */

	fieldName: Config.string(),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Text
	 * @type {?(string|undefined)}
	 */

	label: Config.string(),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Text
	 * @type {?(string|undefined)}
	 */

	name: Config.string().required(),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Text
	 * @type {?(string|undefined)}
	 */

	predefinedValue: Config.string(),

	/**
	 * @default false
	 * @instance
	 * @memberof Text
	 * @type {?bool}
	 */

	readOnly: Config.bool().value(false),

	/**
	 * @default undefined
	 * @instance
	 * @memberof FieldBase
	 * @type {?(bool|undefined)}
	 */

	repeatable: Config.bool().value(false),

	/**
	 * @default false
	 * @instance
	 * @memberof Text
	 * @type {?(bool|undefined)}
	 */

	required: Config.bool().value(false),

	/**
	 * @default true
	 * @instance
	 * @memberof Text
	 * @type {?(bool|undefined)}
	 */

	showLabel: Config.bool().value(true),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Text
	 * @type {?(string|undefined)}
	 */

	spritemap: Config.string(),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Text
	 * @type {?(string|undefined)}
	 */

	tip: Config.string(),

	/**
	 * @default undefined
	 * @instance
	 * @memberof Text
	 * @type {?(string|undefined)}
	 */

	value: Config.string()
};

Soy.register(ColorPicker, templates);

export default ColorPicker;
