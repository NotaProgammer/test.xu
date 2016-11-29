package com.test.xu;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;

import com.teamcenter.rac.aif.AbstractAIFOperation;
import com.teamcenter.rac.aif.AbstractAIFUIApplication;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.ui.common.RACUIUtil;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PlatformHelper;

public class GetCustomValue extends AbstractHandler implements
		IExecutableExtension {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		final TCSession session = RACUIUtil.getTCSession();
		session.queueOperation(new AbstractAIFOperation() {

			@Override
			public void executeOperation() throws Exception {
				// TODO Auto-generated method stub
				AbstractAIFUIApplication app = AIFUtility
						.getCurrentApplication();
				InterfaceAIFComponent[] selectedComponents = app
						.getTargetComponents();
				if (selectedComponents == null || selectedComponents.length < 1) {
					MessageBox.post(PlatformHelper.getCurrentShell(),
							"请选择至少一个对象！", "提示", MessageBox.INFORMATION);
					return;
				}
				String printStr = "";
				for (int i = 0; i < selectedComponents.length; i++) {
					//instanceof判断类型包括子项
					if (selectedComponents[i] instanceof TCComponentItemRevision) {
						TCComponentItemRevision ctr = (TCComponentItemRevision) selectedComponents[i];
						if (ctr.isTypeOf("K3_ItemRevision")) {
							String name = ctr.getProperty("object_name");
							String companyName = ctr
									.getProperty("k3_companyName");
							printStr = printStr + name + "   公司名称："
									+ companyName + "\n";
						}
					} else {
						continue;
					}
				}
				System.out.println(printStr);
				MessageBox.post(PlatformHelper.getCurrentShell(), printStr,
						"提示", MessageBox.INFORMATION);
			}
		});
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInitializationData(IConfigurationElement arg0, String arg1,
			Object arg2) throws CoreException {
		// TODO Auto-generated method stub

	}

}
