package com.test.xu.output;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCComponentBOMWindow;
import com.teamcenter.rac.kernel.TCComponentBOMWindowType;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.ui.common.RACUIUtil;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PlatformHelper;

public class OutputBom extends AbstractHandler implements IExecutableExtension {
	final TCSession session = RACUIUtil.getTCSession();
	final TCComponentBOMWindow bomWindow;
	TCComponentBOMWindowType bomtype;

	public OutputBom() throws Exception {

		bomtype = (TCComponentBOMWindowType) session
				.getTypeComponent("BOMWindow");
		bomWindow = bomtype.create(null);
		// TODO �Զ����ɵĹ��캯�����
	}

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {

		session.queueOperation(new AbstractAIFOperation() {

			@Override
			public void executeOperation() throws Exception {

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
				TCComponentBOMLine aimLine = null;
				if (selectedComponents[0] instanceof TCComponentItem) {
					TCComponentItem tcItem = (TCComponentItem) selectedComponents[0];
					TCComponentItemRevision tcItemRev = tcItem
							.getLatestItemRevision();
					aimLine = getAimLine(tcItemRev);

				} else if (selectedComponents[0] instanceof TCComponentItemRevision) {
					TCComponentItemRevision tcItemRev = (TCComponentItemRevision) selectedComponents[0];
					aimLine = getAimLine(tcItemRev);
				} else {
					aimLine = (TCComponentBOMLine) selectedComponents[0];
				}
				saveBom(aimLine);
				bomWindow.close();
			}

		});
		// TODO �Զ����ɵķ������
		return null;
	}

	public TCComponentBOMLine getAimLine(

	TCComponentItemRevision tcItemRevision) throws Exception {
		TCComponentItem item = tcItemRevision.getItem();
		TCComponentBOMLine aimLine = bomWindow.setWindowTopLine(item,
				tcItemRevision, null, null);
		aimLine.refresh();
		return aimLine;
	}

	public void saveBom(TCComponentBOMLine aimBomLine) {
		try {
			XSSFWorkbook tempBook = new XSSFWorkbook();
			// ���ñ���ģ��
			XSSFSheet tempSheet = tempBook.createSheet("���BOM����");

			XSSFRow tempTitleRow = tempSheet.createRow(0);
			XSSFCell tempTitleCell = tempTitleRow.createCell(0);
			tempTitleCell.setCellValue("���");
			tempTitleCell = tempTitleRow.createCell(1);
			tempTitleCell.setCellValue("����ID");
			tempTitleCell = tempTitleRow.createCell(2);
			tempTitleCell.setCellValue("��������");
			tempTitleCell = tempTitleRow.createCell(3);
			tempTitleCell.setCellValue("��������");
			tempTitleCell = tempTitleRow.createCell(4);
			tempTitleCell.setCellValue("����汾");
			tempTitleCell = tempTitleRow.createCell(5);
			tempTitleCell.setCellValue("����ID");
			tempTitleCell = tempTitleRow.createCell(6);
			tempTitleCell.setCellValue("��������");
			tempTitleCell = tempTitleRow.createCell(7);
			tempTitleCell.setCellValue("��������");
			tempTitleCell = tempTitleRow.createCell(8);
			tempTitleCell.setCellValue("����汾");
			tempTitleCell = tempTitleRow.createCell(9);
			tempTitleCell.setCellValue("��������");
			TCComponentItemRevision tcItemRev = aimBomLine.getItemRevision();
			AIFComponentContext[] children = aimBomLine.getChildren();
			// ��BOM���ݼ��뵽ģ����
			for (int i = 0; i < children.length; i++) {
				TCComponentBOMLine childBom = (TCComponentBOMLine) children[i]
						.getComponent();
				TCComponentItemRevision childItemRev = childBom
						.getItemRevision();
				XSSFRow tempRow = tempSheet.createRow(i + 1);
				XSSFCell tempCell = tempRow.createCell(0);
				tempCell.setCellValue(i + 1);
				tempCell = tempRow.createCell(1);
				tempCell.setCellValue(tcItemRev.getProperty("item_id"));
				tempCell = tempRow.createCell(2);
				tempCell.setCellValue(tcItemRev.getProperty("object_name"));
				tempCell = tempRow.createCell(3);
				tempCell.setCellValue(tcItemRev.getType());
				tempCell = tempRow.createCell(4);
				tempCell.setCellValue(tcItemRev.getProperty("item_revision_id"));
				tempCell = tempRow.createCell(5);
				tempCell.setCellValue(childItemRev.getProperty("item_id"));
				tempCell = tempRow.createCell(6);
				tempCell.setCellValue(childItemRev.getProperty("object_name"));
				tempCell = tempRow.createCell(7);
				tempCell.setCellValue(childItemRev.getType());
				tempCell = tempRow.createCell(8);
				tempCell.setCellValue(childItemRev
						.getProperty("item_revision_id"));
				tempCell = tempRow.createCell(9);
				tempCell.setCellValue(childBom.getProperty("bl_quantity"));
			}

			File file = new File("c:\\temp\\BOM���㱨��.xlsx");
			file.delete();

			FileOutputStream fOut = null;
			try {
				fOut = new FileOutputStream(file);
				tempBook.write(fOut);
				fOut.flush();
				if (fOut != null)
					fOut.close();
				MessageBox.post(PlatformHelper.getCurrentShell(),
						"����Bom�����ɹ�����", "��ʾ", MessageBox.INFORMATION);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			}

		} catch (Exception e) {
			MessageBox.post(e);
		}
	}

	@Override
	public void setInitializationData(IConfigurationElement arg0, String arg1,
			Object arg2) throws CoreException {
		// TODO �Զ����ɵķ������

	}

}
