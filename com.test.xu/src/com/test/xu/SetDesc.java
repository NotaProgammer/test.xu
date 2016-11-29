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
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.ui.common.RACUIUtil;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PlatformHelper;
public class SetDesc extends AbstractHandler implements IExecutableExtension {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		final TCSession session = RACUIUtil.getTCSession();
		session.queueOperation(new AbstractAIFOperation() {

			@Override
			public void executeOperation() throws Exception {
				// TODO Auto-generated method stub
				AbstractAIFUIApplication app = AIFUtility.getCurrentApplication();
				InterfaceAIFComponent[] selectedComponents = app.getTargetComponents();
				if (selectedComponents == null||selectedComponents.length<1) {
					MessageBox.post(PlatformHelper.getCurrentShell(), "��ѡ������һ������", "��ʾ", MessageBox.INFORMATION);
					return;
				}	
				
				for (int i = 0; i < selectedComponents.length; i++) {
					if(selectedComponents[i] instanceof TCComponentItem){
						TCComponentItem ct = (TCComponentItem)selectedComponents[i];
						ct.setProperty("object_desc", "ͳһ������������");
					}else {
						continue;
					}
				}
				MessageBox.post(PlatformHelper.getCurrentShell(), "�����������Գɹ���", "��ʾ", MessageBox.INFORMATION);
			}
		});
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInitializationData(IConfigurationElement arg0, String arg1, Object arg2) throws CoreException {
		// TODO Auto-generated method stub
	}

}
