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
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.ui.common.RACUIUtil;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PlatformHelper;

public class ReloadBOM extends AbstractHandler implements IExecutableExtension {

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
				if (selectedComponents == null
						|| selectedComponents.length != 1) {
					MessageBox.post(PlatformHelper.getCurrentShell(),
							"��ѡ��һ������", "��ʾ", MessageBox.INFORMATION);
					return;
				}
				TCComponentItemRevision itemRevision = (TCComponentItemRevision)selectedComponents[0];
				TCComponent[] bomviews = itemRevision.getRelatedComponents("structure_revisions");
				if(bomviews == null){
					MessageBox.post(PlatformHelper.getCurrentShell(),
							"���ȴ���EBOM��", "��ʾ", MessageBox.INFORMATION);
				}else if(bomviews.length == 1){
					TCComponentItemRevision bomview = (TCComponentItemRevision)bomviews[0];
					TCComponent mbom = bomview.saveAs("");
				}
				for(TCComponent bom:bomviews){
					System.out.println(bom.getType());
				}
				
			}
		});
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public void setInitializationData(IConfigurationElement arg0, String arg1,
			Object arg2) throws CoreException {
		// TODO �Զ����ɵķ������

	}

}
