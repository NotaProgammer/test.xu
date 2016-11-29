package com.test.xu.task;

import com.teamcenter.rac.aif.AbstractAIFOperation;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.ui.common.RACUIUtil;
import com.test.xu.handler.TestUtil;

public class SetRecOp  extends AbstractAIFOperation{
	TCSession session = RACUIUtil.getTCSession();
	TCComponent task;
	Object object;
	public SetRecOp(SetRecDialog dialog) {
		object = dialog.getLovuiComponent().getSelectedObject();
		this.task = dialog.getTask();
		// TODO �Զ����ɵĹ��캯�����
	}

	@Override
	public void executeOperation() throws Exception {
		System.out.println(object);
		TCComponent[] tcComponents = TestUtil.comm_qry("searchUser", "ID",(String)object);
		if(tcComponents.length>0){
		task.setReferenceProperty("k3_nextReceive", tcComponents[0]);
		}
		
		// TODO �Զ����ɵķ������
		
	}
}
