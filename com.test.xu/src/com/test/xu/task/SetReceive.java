package com.test.xu.task;

import javax.swing.SwingUtilities;

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
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.ui.common.RACUIUtil;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PlatformHelper;
import com.test.xu.k3search.K3SearchDialog;

public class SetReceive extends AbstractHandler implements IExecutableExtension {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		final TCSession session = RACUIUtil.getTCSession();
		session.queueOperation(new AbstractAIFOperation() {

			@Override
			public void executeOperation() throws Exception {
				AbstractAIFUIApplication app = AIFUtility
						.getCurrentApplication();
				InterfaceAIFComponent[] selectedComponents = app
						.getTargetComponents();
				if (selectedComponents.length > 1) {
					MessageBox.post(PlatformHelper.getCurrentShell(),
							"请选择一个对象！", "提示", MessageBox.INFORMATION);
					return;
				}

				SwingUtilities.invokeLater(new SetRecDialog(
						(TCComponent) selectedComponents[0]));
			}

		});

		return null;
		// TODO 自动生成的方法存根
	}

	@Override
	public void setInitializationData(IConfigurationElement arg0, String arg1,
			Object arg2) throws CoreException {
		// TODO 自动生成的方法存根

	}

}
