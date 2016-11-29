package com.test.xu.search;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.ui.internal.handlers.WizardHandler.New;

import com.teamcenter.rac.aif.AbstractAIFOperation;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentFolder;
import com.teamcenter.rac.kernel.TCComponentFolderType;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.ui.common.RACUIUtil;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PlatformHelper;
import com.teamcenter.rac.util.iTextField;
import com.test.xu.handler.TestUtil;

public class XuSearchOp extends AbstractAIFOperation {
	private ArrayList<iTextField> itextFields;
	protected TCSession session = RACUIUtil.getTCSession();

	public XuSearchOp(XuSearchDialog xuSearchDialog) {
		itextFields = xuSearchDialog.getItextFields();
		// TODO �Զ����ɵĹ��캯�����
	}

	@Override
	public void executeOperation() throws Exception {
		ArrayList<TCComponentItemRevision> tcComponentItemRevisions = new ArrayList<>();
		String errorStr = "";
		// ��ѯ����ı����ȡ������
		for (iTextField iTextField : itextFields) {
			String str = iTextField.getText();
			if (!str.equals("")) {
				// �����ַ���1����K3_ItemRevision
				TCComponent[] results = TestUtil.comm_qry("��ѯ1", "�ַ���1", str);
				if (results.length < 1) {
					errorStr = errorStr + "δ�ҵ�" + iTextField.getText()
							+ "���Ӧ�İ汾\n";
				}
				for (TCComponent result : results) {
					TCComponentItemRevision revResult = (TCComponentItemRevision) result;
					// ��ӵ���ѡ�ж���
					tcComponentItemRevisions.add(revResult);

				}
			}
		}
		// ���û���������
		if (tcComponentItemRevisions.size() < 1) {
			MessageBox.post(PlatformHelper.getCurrentShell(), errorStr, "��ʾ",
					MessageBox.INFORMATION);
			return;
		}
		// �����ļ��кʹ����ļ���
		TCComponentFolder folder = session.getUser().getHomeFolder();
		TCComponentFolderType folderType = (TCComponentFolderType) session
				.getTypeComponent("Folder");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String dateStr = dateFormat.format(new Date());
		TCComponentFolder childFolder = null;
		AIFComponentContext[] aifComponent = folder.getChildren();
		for (AIFComponentContext aContext : aifComponent) {
			InterfaceAIFComponent tcComponent = aContext.getComponent();
			if (tcComponent.getType().equals("Folder")
					&& tcComponent.getProperty("object_name").equals(dateStr)) {
				childFolder = (TCComponentFolder) tcComponent;
			}
		}
		if (childFolder == null) {
			childFolder = folderType.create(dateStr, "�洢K3_ItemRevision",
					"Folder");
			folder.add("contents", childFolder);
		}
		try {
			childFolder.add("contents", tcComponentItemRevisions);
		} catch (TCException e) {
			// TODO �Զ����ɵ� catch ��
			MessageBox.post(e);
		} finally {
			MessageBox.post(PlatformHelper.getCurrentShell(), "���������ɹ�����", "��ʾ",
					MessageBox.INFORMATION);
		}
		// ���������Ϣ��Ϊ�� ������ʾ��
		if (!errorStr.equals("")) {
			MessageBox.post(PlatformHelper.getCurrentShell(), errorStr, "��ʾ",
					MessageBox.INFORMATION);
		}
		// TODO �Զ����ɵķ������

	}
}
