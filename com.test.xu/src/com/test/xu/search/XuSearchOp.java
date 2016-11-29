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
		// TODO 自动生成的构造函数存根
	}

	@Override
	public void executeOperation() throws Exception {
		ArrayList<TCComponentItemRevision> tcComponentItemRevisions = new ArrayList<>();
		String errorStr = "";
		// 查询多个文本框获取的内容
		for (iTextField iTextField : itextFields) {
			String str = iTextField.getText();
			if (!str.equals("")) {
				// 按照字符串1查找K3_ItemRevision
				TCComponent[] results = TestUtil.comm_qry("查询1", "字符串1", str);
				if (results.length < 1) {
					errorStr = errorStr + "未找到" + iTextField.getText()
							+ "相对应的版本\n";
				}
				for (TCComponent result : results) {
					TCComponentItemRevision revResult = (TCComponentItemRevision) result;
					// 添加到被选中对象
					tcComponentItemRevisions.add(revResult);

				}
			}
		}
		// 如果没有搜索结果
		if (tcComponentItemRevisions.size() < 1) {
			MessageBox.post(PlatformHelper.getCurrentShell(), errorStr, "提示",
					MessageBox.INFORMATION);
			return;
		}
		// 生成文件夹和存入文件夹
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
			childFolder = folderType.create(dateStr, "存储K3_ItemRevision",
					"Folder");
			folder.add("contents", childFolder);
		}
		try {
			childFolder.add("contents", tcComponentItemRevisions);
		} catch (TCException e) {
			// TODO 自动生成的 catch 块
			MessageBox.post(e);
		} finally {
			MessageBox.post(PlatformHelper.getCurrentShell(), "搜索拷贝成功！！", "提示",
					MessageBox.INFORMATION);
		}
		// 如果错误信息不为空 弹出提示框
		if (!errorStr.equals("")) {
			MessageBox.post(PlatformHelper.getCurrentShell(), errorStr, "提示",
					MessageBox.INFORMATION);
		}
		// TODO 自动生成的方法存根

	}
}
