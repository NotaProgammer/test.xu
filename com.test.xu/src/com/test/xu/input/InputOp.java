package com.test.xu.input;

import java.io.File;
import java.io.FileInputStream;

import javax.print.attribute.standard.MediaSize.NA;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.teamcenter.rac.aif.AbstractAIFOperation;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentDataset;
import com.teamcenter.rac.kernel.TCComponentDatasetType;
import com.teamcenter.rac.kernel.TCComponentFolder;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCComponentProcess;
import com.teamcenter.rac.kernel.TCComponentProcessType;
import com.teamcenter.rac.kernel.TCComponentTaskTemplate;
import com.teamcenter.rac.kernel.TCComponentTaskTemplateType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCPreferenceService;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.ui.common.RACUIUtil;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PlatformHelper;
import com.test.xu.handler.TestUtil;

public class InputOp extends AbstractAIFOperation {
	File filePath;
	final TCSession session = RACUIUtil.getTCSession();

	public InputOp(InputDialog inputDialog) {
		filePath = inputDialog.getExcelFilePanel().getFile();
		// TODO 自动生成的构造函数存根
	}

	@Override
	public void executeOperation() throws Exception {
		// TODO 自动生成的方法存根
		XSSFWorkbook tempBook = null;
		FileInputStream fIn = null;
		try {
			fIn = new FileInputStream(filePath);
			tempBook = new XSSFWorkbook(fIn);
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			fIn.close();
		}
		String errorStr = null;
		if (tempBook != null) {
			XSSFSheet tempSheet = tempBook.getSheet("Sheet1");
			if (tempSheet != null && tempSheet.getLastRowNum() > 0) {
				for (int i = tempSheet.getFirstRowNum() + 1; i <= tempSheet
						.getLastRowNum(); i++) {
					XSSFRow row = tempSheet.getRow(i);
					for (int j = 1; j <= 6; j++) {
						row.getCell(j).setCellType(XSSFCell.CELL_TYPE_STRING);
					}
					ExcelData excelData = new ExcelData();
					excelData.id = row.getCell(1).getStringCellValue();
					excelData.name = row.getCell(2).getStringCellValue();
					excelData.type = row.getCell(3).getStringCellValue();
					excelData.desc = row.getCell(4).getStringCellValue();
					excelData.str1 = row.getCell(5).getStringCellValue();
					excelData.standard = row.getCell(6).getStringCellValue();
					try {
						inputData(excelData);
					} catch (Exception e) {
						errorStr =errorStr+"\n"+ e;
					}
				}
			} else {
				MessageBox.post(PlatformHelper.getCurrentShell(),
						"Excel文件异常,请确认文件正确性！！", "提示", MessageBox.INFORMATION);
			}
		}
		if(errorStr!=null){
			MessageBox.post(PlatformHelper.getCurrentShell(),
					errorStr, "提示", MessageBox.INFORMATION);
		}
	}

	public void inputData(ExcelData excelData) throws Exception {
		TCComponentFolder tcFolder = getFolder();
		if (tcFolder == null) {
			tcFolder = session.getUser().getNewStuffFolder();
		}
		TCComponent[] otherResult = TestUtil.comm_qry("零组件...", "零组件 ID",
				excelData.id);
		if (otherResult.length >= 1) {
			throw new Exception("ID：" + excelData.id + "已被占用");
		} else {
			// 创建Item
			TCComponentItemType itemtype = (TCComponentItemType) session
					.getTypeComponent(excelData.type);
			TCComponentItem newitem = itemtype.create(excelData.id, // ID
					excelData.id, // RevID
					excelData.type, // 类型
					excelData.name, // 名称
					excelData.desc, // 描述
					null); // 单位(对象)
			TCComponentItemRevision tcItemRevision = newitem
					.getLatestItemRevision();
			tcItemRevision.setProperty("k3_StringAttr1", excelData.str1);
			tcFolder.add("contents", newitem);
			tcFolder.refresh();
			TCComponentDatasetType dataType = (TCComponentDatasetType) session
					.getTypeComponent("Dataset");
			TCComponentDataset data = dataType.create(excelData.standard,
					excelData.standard, "MSExcelX");
			String[] paths = new String[1];
			String[] refs = new String[1];
			paths[0] = filePath.getParent() + "\\" + excelData.standard;
			refs[0] = "excel";
			data.setFiles(paths, refs);
			tcItemRevision.add("IMAN_specification", data);
			TCComponentTaskTemplateType TaskTemplateType = (TCComponentTaskTemplateType) session
					.getTypeComponent("EPMTaskTemplate");
			TCComponentTaskTemplate taskTemplate = TaskTemplateType.find(
					"TCM Release Process", 0);
			// 构建流程
			TCComponentProcessType ProcessType = (TCComponentProcessType) session
					.getTypeComponent("EPMJob");
			@SuppressWarnings("unused")
			TCComponentProcess Process = (TCComponentProcess) ProcessType
					.create("发布流程", "", taskTemplate,
							new TCComponent[] { tcItemRevision },
							new int[] { 1 });

		}
	}

	public TCComponentFolder getFolder() throws Exception {
		TCComponentFolder tcFolder = null;
		String str = null;
		try {
			TCPreferenceService prefs = session.getPreferenceService();
			str = prefs.getStringValue("InputOp.saveFolder");
			System.out.println(str + "___________________");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			AIFComponentContext[] children = session.getUser().getHomeFolder()
					.getChildren();
			for (AIFComponentContext child : children) {
				InterfaceAIFComponent tcComponent = child.getComponent();
				if (tcComponent.getType().equals("Folder")
						&& tcComponent.getProperty("object_name").equals(str)) {
					tcFolder = (TCComponentFolder) tcComponent;
				}
			}
		} catch (TCException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return tcFolder;
	}

	public class ExcelData {
		String id;
		String name;
		String type;
		String desc;
		String str1;
		String standard;
	}
}
