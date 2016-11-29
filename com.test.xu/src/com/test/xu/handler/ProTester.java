package com.test.xu.handler;

import org.eclipse.core.expressions.PropertyTester;

import com.teamcenter.rac.aif.AbstractAIFUIApplication;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCException;

public class ProTester extends PropertyTester {

	@Override
	public boolean test(Object arg0, String arg1, Object[] arg2, Object arg3) {

		AbstractAIFUIApplication thisapp = AIFUtility.getCurrentApplication();
		InterfaceAIFComponent[] aifs = thisapp.getTargetComponents();
		for (InterfaceAIFComponent aif : aifs) {
			if (aif instanceof TCComponentItem) {
				TCComponentItem tcItem = (TCComponentItem) aif;
				System.out.println("test_________________");
				try {
					String id = tcItem.getProperty("item_id");
					if (id.equals("81000101"))
						return true;
				} catch (TCException e) {
					e.printStackTrace();
				}
			}
		}

		return false;
	}

}
