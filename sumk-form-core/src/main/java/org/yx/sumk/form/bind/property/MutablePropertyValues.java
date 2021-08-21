package org.yx.sumk.form.bind.property;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 参数属性 list
 * @author wjiajun
 */
public class MutablePropertyValues implements Serializable {

    private final List<PropertyValue> propertyValueList;

    public MutablePropertyValues() {
        propertyValueList = new ArrayList<>(0);
    }

	public MutablePropertyValues(Map<?, ?> original) {
		if (original != null) {
			this.propertyValueList = new ArrayList<>(original.size());
			for (Map.Entry<?, ?> entry : original.entrySet()) {
				this.propertyValueList.add(new PropertyValue(entry.getKey().toString(), entry.getValue()));
			}
		}
		else {
			this.propertyValueList = new ArrayList<>(0);
		}
	}

	public MutablePropertyValues(List<PropertyValue> propertyValueList) {
		this.propertyValueList =
				(propertyValueList != null ? propertyValueList : new ArrayList<>());
	}

	public List<PropertyValue> getPropertyValueList() {
		return this.propertyValueList;
	}
}