package com.test.xu;

import java.security.acl.Owner;

import javax.rmi.CORBA.Util;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;

import com.teamcenter.rac.aif.AbstractAIFOperation;
import com.teamcenter.rac.aif.AbstractAIFUIApplication;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.aifrcp.AIFUtility;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.ui.common.RACUIUtil;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PlatformHelper;
import com.test.xu.handler.TestUtil;

public class SaveAs extends AbstractHandler implements IExecutableExtension {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		// TODO 自动生成的方法存根
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
							"请选择一个对象！", "提示", MessageBox.INFORMATION);
					return;
				}
				TCComponentItemRevision itemrev = (TCComponentItemRevision) selectedComponents[0];
				TCComponent owner = itemrev.getReferenceProperty("owning_user");
				TCComponent current_user = session.getUser();
				if (itemrev.getProperty("process_stage").equals("")) {
					MessageBox.post(PlatformHelper.getCurrentShell(),
							"该对象版本不在流程中,不能进修订！", "提示", MessageBox.INFORMATION);
					return;
				} else if (itemrev.getProperty("release_status_list").equals("")) {
					MessageBox.post(PlatformHelper.getCurrentShell(),
							"该对象版本未发布，不能进行修订！", "提示", MessageBox.INFORMATION);
					return;
				} else if (!owner.equals(current_user)) {
					MessageBox.post(PlatformHelper.getCurrentShell(),
							"你不是该对象版本所有者，不能进行修订！", "提示", MessageBox.INFORMATION);
					return;
				}
				String rev = itemrev.getProperty("item_revision_id");
				String id = itemrev.getProperty("item_id");
				System.out.println(rev+"-----------"+id);
				TCComponent[] highRev = TestUtil.comm_qry("高于版本", "版本", rev,
						"ID", id);
				System.out.println(highRev.length+"________________");
				for (TCComponent therev : highRev) {
					if (!therev.getProperty("release_status_list").equals("")) {
						MessageBox.post(PlatformHelper.getCurrentShell(),
								"有高于该对象版本的已发布对象版本，不能进行修订！", "提示",
								MessageBox.INFORMATION);
						return;
					}
				}
				TCComponentItemRevision itemrev2 = itemrev.saveAs("");
				AIFComponentContext[] whereref = itemrev.whereReferenced();
				System.out.println("whereref:"+whereref.length);
				for (AIFComponentContext ref : whereref) {
					System.out.println("************"+ref.getComponent().getProperty("object_string"));
					if (((TCComponent)ref.getComponent()).isTypeOf("K3_DocumentRevision")) {
					
								((TCComponent) ref.getComponent()).add(
										"K3_afterModify", itemrev2);
								MessageBox.post(PlatformHelper.getCurrentShell(),
										"修订成功！！", "提示",
										MessageBox.INFORMATION);
					}
				}
			}
		});
		return null;
	}

	@Override
	public void setInitializationData(IConfigurationElement arg0, String arg1,
			Object arg2) throws CoreException {
		// TODO 自动生成的方法存根

	}

}
