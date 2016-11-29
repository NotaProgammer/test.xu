package com.test.xu.output;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.teamcenter.rac.aif.AbstractAIFOperation;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCComponentBOMWindow;
import com.teamcenter.rac.kernel.TCComponentBOMWindowType;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.ui.common.RACUIUtil;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PlatformHelper;

public class OutputOp extends AbstractAIFOperation {

	protected TCSession session = RACUIUtil.getTCSession();
	private OutputDialog outputDialog;
	private String savePath;
	private int maxLevel;
	private static int count;
	private XSSFWorkbook tempBook;
	private XSSFSheet tempSheet;

	public OutputOp(OutputDialog outputDialog) {
		this.outputDialog = outputDialog;
		count = 0;
		tempBook = new XSSFWorkbook();
		tempSheet = tempBook.createSheet("输出BOM报表");
		savePath = outputDialog.getFile().getText();
		maxLevel = Integer.valueOf(outputDialog.getIcBox().getSelectedIndex());
		// TODO 自动生成的构造函数存根
	}

	@Override
	public void executeOperation() {
		try {
			saveBom1(outputDialog.getTcComponent());
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			MessageBox.post(e);
		}
		// TODO 自动生成的方法存根

	}

	// 获取BOMline
	public void saveBom1(TCComponent tcComponent) throws Exception{
			TCComponentBOMWindowType bomtype = (TCComponentBOMWindowType) session
					.getTypeComponent("BOMWindow");
			final TCComponentBOMWindow bomWindow = bomtype.create(null);
			TCComponentBOMLine aimLine = null;
			if (tcComponent instanceof TCComponentItem) {
				TCComponentItem tcItem = (TCComponentItem) tcComponent;
				TCComponentItemRevision tcItemRev = tcItem
						.getLatestItemRevision();
				aimLine = getAimLine(tcItemRev, bomWindow);
			} else if (tcComponent instanceof TCComponentItemRevision) {
				TCComponentItemRevision tcItemRev = (TCComponentItemRevision) tcComponent;
				aimLine = getAimLine(tcItemRev, bomWindow);
			} else {
				aimLine = (TCComponentBOMLine) tcComponent;
			}
			saveBom(aimLine);
			bomWindow.save();
			bomWindow.close();
	}

	// 得到BOMline
	public TCComponentBOMLine getAimLine(
			TCComponentItemRevision tcItemRevision,
			TCComponentBOMWindow bomWindow) throws Exception {
		TCComponentItem item = tcItemRevision.getItem();
		TCComponentBOMLine aimLine = bomWindow.setWindowTopLine(item,
				tcItemRevision, null, null);
		aimLine.refresh();
		return aimLine;
	}

	// 保存BOMline
	public void saveBom(TCComponentBOMLine aimBomLine) throws Exception{

			XSSFRow tempTitleRow = tempSheet.createRow(0);
			count++;
			XSSFCell tempTitleCell = tempTitleRow.createCell(0);
			tempTitleCell.setCellValue("序号");
			tempTitleCell = tempTitleRow.createCell(1);
			tempTitleCell.setCellValue("父项ID");
			tempTitleCell = tempTitleRow.createCell(2);
			tempTitleCell.setCellValue("父项名称");
			tempTitleCell = tempTitleRow.createCell(3);
			tempTitleCell.setCellValue("父项类型");
			tempTitleCell = tempTitleRow.createCell(4);
			tempTitleCell.setCellValue("父项版本");
			tempTitleCell = tempTitleRow.createCell(5);
			tempTitleCell.setCellValue("子项ID");
			tempTitleCell = tempTitleRow.createCell(6);
			tempTitleCell.setCellValue("子项名称");
			tempTitleCell = tempTitleRow.createCell(7);
			tempTitleCell.setCellValue("子项类型");
			tempTitleCell = tempTitleRow.createCell(8);
			tempTitleCell.setCellValue("子项版本");
			tempTitleCell = tempTitleRow.createCell(9);
			tempTitleCell.setCellValue("子项数量");
			getBom(aimBomLine, maxLevel);
			File file = new File(savePath);
			file.delete();
			FileOutputStream fOut = null;
			try {
				fOut = new FileOutputStream(file);
				tempBook.write(fOut);
				fOut.flush();
				MessageBox.post(PlatformHelper.getCurrentShell(),
						"多层Bom导出成功！！", "提示", MessageBox.INFORMATION);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
			} finally {
				if (fOut != null)
					fOut.close();
			}

	}

	// 递归
	public void getBom(TCComponentBOMLine tcBomline, int currentLv)
			throws Exception {
		if (--currentLv < 0) {
			return;
		}
		AIFComponentContext[] children = tcBomline.getChildren();
		// 将BOM数据加入到模板中
		for (int i = 0; i < children.length; i++) {
			TCComponentBOMLine childBom = (TCComponentBOMLine) children[i]
					.getComponent();
			XSSFRow tempRow = tempSheet.createRow(count);
			XSSFCell tempCell = tempRow.createCell(0);
			tempCell.setCellValue(count);
			tempCell = tempRow.createCell(1);
			tempCell.setCellValue(tcBomline.getProperty("bl_item_item_id"));
			tempCell = tempRow.createCell(2);
			tempCell.setCellValue(tcBomline.getProperty("bl_item_object_name"));
			tempCell = tempRow.createCell(3);
			tempCell.setCellValue(tcBomline.getItemRevision().getType());
			tempCell = tempRow.createCell(4);
			tempCell.setCellValue(tcBomline.getProperty("bl_rev_item_revision_id"));
			tempCell = tempRow.createCell(5);
			tempCell.setCellValue(childBom.getProperty("bl_item_item_id"));
			tempCell = tempRow.createCell(6);
			tempCell.setCellValue(childBom.getProperty("bl_item_object_name"));
			tempCell = tempRow.createCell(7);
			tempCell.setCellValue(childBom.getItemRevision().getType());
			tempCell = tempRow.createCell(8);
			tempCell.setCellValue(childBom.getProperty("bl_rev_item_revision_id"));
			tempCell = tempRow.createCell(9);
			tempCell.setCellValue(childBom.getProperty("bl_quantity"));
			count++;
			getBom(childBom, currentLv);
		}
	}
}
