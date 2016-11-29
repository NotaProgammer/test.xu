package com.test.xu.stylesheet;

import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCProperty;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.stylesheet.PropertyLOVCombobox;
import com.teamcenter.rac.ui.common.RACUIUtil;

public class Setuomtag extends PropertyLOVCombobox {

	final TCSession session = RACUIUtil.getTCSession();

	private static final long serialVersionUID = 1L;

	@Override
	public void load(TCProperty tcproperty) throws Exception {
		TCComponent[] tcUnits = session.getTypeComponent("UnitOfMeasure")
				.extent();
		TCComponent unit = null;
		for (TCComponent tcComponent : tcUnits) {
			if (tcComponent.getProperty("symbol").equals("EA")) {
				unit = tcComponent;
			}
		}
		tcproperty.setReferenceValue(unit);
		// TODO 自动生成的方法存根
		super.load(tcproperty);
	}

}
