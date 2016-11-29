package com.test.xu.dialog;

import java.util.ArrayList;

import com.teamcenter.rac.aif.AbstractAIFOperation;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.common.lov.LOVUIComponent;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PlatformHelper;
import com.teamcenter.rac.util.iTextField;
import com.teamcenter.soaictstubs.int64_s;
import com.teamcenter.soaictstubs.preferenceContext_s;

public class ChangeProsOp extends AbstractAIFOperation {

	private ArrayList<InterfaceAIFComponent> selectedComponents;
	private ArrayList<LOVUIComponent> lovuiComponents;

	public ChangeProsOp(ChangeProsDialog cpDialog) {
		this.lovuiComponents = cpDialog.getLovuiComponents();
		this.selectedComponents = cpDialog.getSelectedComponents();
		// // TODO 自动生成的构造函数存根
	}

	@Override
	public void executeOperation() throws Exception {
		String errorStr = "";
		for (int i = 0; i < selectedComponents.size(); i++) {
			try {
					TCComponent tcComponent = (TCComponent) selectedComponents.get(i);
					tcComponent.setProperty("k3_StringAttr1", lovuiComponents.get(i).getText());
			} catch (Exception e) {
				errorStr = errorStr + e.toString();
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		if(errorStr.equals("")){
			MessageBox.post(PlatformHelper.getCurrentShell(), "修改成功！！", "提示",
					MessageBox.INFORMATION);
		}else {
			MessageBox.post(PlatformHelper.getCurrentShell(), errorStr, "提示",
					MessageBox.INFORMATION);
		}
	}
	// String oldname = cmp.getProperty("object_name");
	//
	// System.out.println("old name is : " + oldname);
	//
	// cmp.setProperty("object_name", name);
	// cmp.setProperty("object_desc", desc);
	// MessageBox.post(PlatformHelper.getCurrentShell(), "new name is : " +
	// name, "提示", MessageBox.INFORMATION);
	// // TODO 自动生成的方法存根
	// }
}
