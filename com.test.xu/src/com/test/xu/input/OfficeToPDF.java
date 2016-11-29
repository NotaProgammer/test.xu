package com.test.xu.input;

import java.util.HashMap;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;

import com.k3.services.loose.office2pdf.Office2PDFService;
import com.teamcenter.rac.aif.AbstractAIFOperation;
import com.teamcenter.rac.aif.AbstractAIFUIApplication;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.ui.common.RACUIUtil;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PlatformHelper;

public class OfficeToPDF extends AbstractHandler implements
		IExecutableExtension {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		final TCSession session = RACUIUtil.getTCSession();
		session.queueOperation(new AbstractAIFOperation() {

			@Override
			public void executeOperation() throws Exception {
				Office2PDFService otpService = Office2PDFService
						.getService(session.getSoaConnection());
				AbstractAIFUIApplication app = AIFUtility
						.getCurrentApplication();
				InterfaceAIFComponent[] selectedComponents = app
						.getTargetComponents();
				HashMap<String, String> selComponents = new HashMap<String, String>();
				for (int i = 0; i < selectedComponents.length; i++) {
					selComponents.put(String.valueOf(i),
							selectedComponents[i].getUid());
				}
				if (!otpService.getBypass()) {
					otpService.setBypass(true);
				}
				boolean isSuccess = otpService.office2PDF(selComponents);
				if (otpService.getBypass()) {
					otpService.setBypass(false);
				}
				if (isSuccess) {
					MessageBox.post(PlatformHelper.getCurrentShell(),
							"office文件转换pdf文件成功！", "提示", MessageBox.INFORMATION);
				} else {
					MessageBox.post(PlatformHelper.getCurrentShell(),
							"office文件转换pdf文件失败！！", "提示", MessageBox.INFORMATION);
				}
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
