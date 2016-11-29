package com.test.xu.k3search;

import java.util.ArrayList;

import com.teamcenter.rac.aif.AbstractAIFOperation;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentFolder;
import com.teamcenter.rac.kernel.TCComponentFolderType;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.ui.common.RACUIUtil;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PlatformHelper;
import com.test.xu.handler.TestUtil;

public class K3SearchOp extends AbstractAIFOperation {
	final TCSession session = RACUIUtil.getTCSession();
	private String idString;
	private String pathStr;

	public K3SearchOp(K3SearchDialog k3SearchDialog) {
		// 获得ID输入框中的内容和文件夹NAME
		idString = k3SearchDialog.getIdITextArea().getText();
		pathStr = k3SearchDialog.getPathTextField().getText();
		// TODO 自动生成的构造函数存根
	}

	@Override
	public void executeOperation() throws Exception {
		// ArrayList<String> idList = new ArrayList<>();
		// 存放需要拷贝到文件夹的版本
		ArrayList<TCComponentItemRevision> tcItemRevs = new ArrayList<>();
		String[] idStrs = idString.split("\n");
		// /
		// 搜索处理所有ID
		for (int i = 0; i < idStrs.length; i++) {
			System.out.println(idStrs[i]);
			// 用ID搜索长虹零件
			TCComponent[] results = TestUtil.comm_qry("长虹零件", "ID", idStrs[i]);
			if (results.length < 1) {
				TCComponent[] otherResult = TestUtil.comm_qry("零组件...",
						"零组件 ID", idStrs[i]);
				if (otherResult.length >= 1) {
					MessageBox.post(PlatformHelper.getCurrentShell(), "ID："
							+ idStrs[i] + "已被占用", "提示", MessageBox.INFORMATION);
				} else {
					// 创建Item

					try {
						TCComponentItemType itemtype = (TCComponentItemType) session
								.getTypeComponent("K3_Item");
						TCComponent[] tcUnits = session.getTypeComponent(
								"UnitOfMeasure").extent();
						TCComponent unit = null;
						for (TCComponent tcComponent : tcUnits) {
							if (tcComponent.getProperty("symbol").equals("EA")) {
								unit = tcComponent;
							}
						}
						TCComponentItem newitem = itemtype.create(idStrs[i], // ID
								idStrs[i], // RevID
								"K3_Item", // 类型
								"新建Item", // 名称
								"", // 描述
								unit); // 单位(对象)
						tcItemRevs.add(newitem.getLatestItemRevision());
					} catch (TCException e) {
						// TODO 自动生成的 catch 块
						MessageBox.post(e);
					}
				}
				continue;
			}
			for (TCComponent result : results) {
				TCComponentItemRevision revResult = ((TCComponentItem) result)
						.getLatestItemRevision();
				String reSta = revResult.getProperty("release_statuses");
				if (reSta.equals("")) {
					tcItemRevs.add(revResult);
				} else {
					TCComponentItemRevision rev2 = revResult.saveAs("");
					tcItemRevs.add(rev2);
				}
			}
		}
		TCComponentFolder folder = session.getUser().getHomeFolder();
		TCComponentFolderType folderType = (TCComponentFolderType) session
				.getTypeComponent("Folder");
		TCComponentFolder childFolder = null;
		AIFComponentContext[] aifComponent = folder.getChildren();
		for (AIFComponentContext aContext : aifComponent) {
			TCComponent tcComponent = (TCComponent) aContext.getComponent();
			if (tcComponent.isTypeOf("Folder")
					&& tcComponent.getProperty("object_name").equals(pathStr)) {
				childFolder = (TCComponentFolder) tcComponent;
			}
		}
		if (childFolder == null) {
			childFolder = folderType.create(pathStr, "存储K3_ItemRevision",
					"Folder");
			folder.add("contents", childFolder);
		}
		try {
			if (tcItemRevs.size() > 0) {
				childFolder.add("contents", tcItemRevs);
			}
		} catch (TCException e) {
			// TODO 自动生成的 catch 块
			MessageBox.post(e);
		}
		// TODO 自动生成的方法存根

	}

}
