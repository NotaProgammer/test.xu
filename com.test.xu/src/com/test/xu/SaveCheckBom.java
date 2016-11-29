package com.test.xu;

import java.util.ArrayList;

import com.teamcenter.rac.aif.AbstractAIFCommand;
import com.teamcenter.rac.aif.kernel.AIFComponentContext;
import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.pse.common.InterfaceBOMEditingApplication;
import com.teamcenter.rac.pse.operations.SaveBOMOperation;
import com.teamcenter.rac.util.MessageBox;

public class SaveCheckBom extends AbstractAIFCommand {

	private static ArrayList<String> allId;
	boolean flag = true;

	public SaveCheckBom(InterfaceBOMEditingApplication app) {
		
		System.out.println("--------------------------111111111111111--------------------");
		allId = new ArrayList<>();
		if (!flag) {
			SaveBOMOperation savebomoperation = new SaveBOMOperation(app);
			app.getSession().queueOperation(savebomoperation);
			return;
		}
		TCComponentBOMLine topline = app.getTopBOMLine();
		try {
			checkUomtag(topline);
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		if(allId.size() == 0){
		SaveBOMOperation savebomoperation = new SaveBOMOperation(app);
		app.getSession().queueOperation(savebomoperation);
		}
		else {
			String message = "";
			for(int i =0;i<allId.size();i++){
				message = message+"\n"+allId.get(i);
			}
			MessageBox.post(message+"\n未填写单位或单位填写不规范", "错误 ！", MessageBox.INFORMATION);
		}
	}

	public void checkUomtag(TCComponentBOMLine bomLine) throws Exception {
		AIFComponentContext[] childrenLine = null;
		childrenLine = bomLine.getChildren();
		for (int i = 0; i < childrenLine.length; i++) {
			TCComponentBOMLine childLine = (TCComponentBOMLine) (childrenLine[i]
					.getComponent());
			String quantity = null;
			quantity = childLine.getProperty("bl_quantity");

			if (quantity == "") {
				allId.add(childLine.getProperty("bl_item_item_id"));
			} else if (Float.parseFloat(quantity) <= 0) {
				allId.add(childLine.getProperty("bl_item_item_id"));
				
			}
			System.out.println(childLine.getProperty("bl_item_item_id"));
			checkUomtag(childLine);
		}
	}
}
