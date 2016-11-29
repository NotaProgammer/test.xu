package com.test.xu;

import java.util.Arrays;

import com.teamcenter.rac.aif.AbstractAIFCommand;
import com.teamcenter.rac.classification.common.AbstractG4MContext;
import com.teamcenter.rac.kernel.TCClassificationService;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCPreferenceService;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.kernel.ics.ICSAdminClass;
import com.teamcenter.rac.kernel.ics.ICSApplicationObject;
import com.teamcenter.rac.ui.common.RACUIUtil;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PlatformHelper;

public class GenerateFigure extends AbstractAIFCommand {
	protected final AbstractG4MContext g4mCtx;
	final TCSession session = RACUIUtil.getTCSession();

	public GenerateFigure(AbstractG4MContext g4mCtx) {
		this.g4mCtx = g4mCtx;
		// TODO 自动生成的构造函数存根
	}

	@Override
	protected void executeCommand() throws Exception {

		System.out.println("进入");
		TCPreferenceService prefs = session.getPreferenceService();

		TCComponent tcComponent = g4mCtx.getClassifiedComponent();
		if (tcComponent.isTypeOf("K3_ItemRevision")) {
			System.out.println("K3_ItemRevision");
			TCComponent item = ((TCComponentItemRevision) tcComponent)
					.getItem();
			TCComponent master = tcComponent
					.getRelatedComponent("IMAN_master_form_rev");
			String type = GetType(item);
			String typename = type.replace(".", "_");
			if (type != null) {
				if (master.getProperty("k3_figure_number").equals("")) {
					prefs.refresh();
					String[] strs = prefs.getStringValues(typename);
		
					if (strs != null) {
						int[] numbers = new int[strs.length];
						for (int i = 0; i < strs.length; i++) {
						
							numbers[i] = Integer.valueOf(strs[i]);
						}
						Arrays.sort(numbers);
						for (int i = 0; i < strs.length; i++) {
							
							strs[i] = String.valueOf(numbers[i]);
								System.out.print("--"+strs[i]);
						}
						for (int i = 0; i < strs.length; i++) {
							if (!strs[i].equals("0")) {
								String countstr = strs[i];
								while (countstr.length() < 3) {
									countstr = "0" + countstr;
								}
								strs[i] = "0";
								prefs.setStringValues(typename, strs);
								master.setProperty("k3_figure_number", type
										+ "." + countstr);
								break;
							}
							if (i == strs.length - 1) {
								MessageBox.post(
										PlatformHelper.getCurrentShell(),
										"没有可用的图号！", "提示",
										MessageBox.INFORMATION);
							}
						}

					} else {
						String[] values = new String[999];
						for (int i = 0; i < values.length; i++) {
							values[i] = String.valueOf(i + 1);
						}
						String countstr = values[0];
						while (countstr.length() < 3) {
							countstr = "0" + countstr;
						}
						master.setProperty("k3_figure_number", type + "."
								+ countstr);
						values[0] = "0";
						prefs.create(
								typename,
								TCPreferenceService.TCPreferenceProtectionScope.USER_PROTECTION_SCOPE,
								"保存图号",
								TCPreferenceService.TCPreferenceType.STRING_TYPE,
								true,
								true,
								TCPreferenceService.TCPreferenceLocation.USER_LOCATION,
								"", values);
					}

					MessageBox.post(PlatformHelper.getCurrentShell(),
							"成功自动生成图号！", "提示", MessageBox.INFORMATION);
				}

			}
		}
		// TODO 自动生成的方法存根
		super.executeCommand();
	}

	// private String GetType(TCComponent item) throws Exception {
	// ICSUtil icsUtil = new ICSUtil(item.getSession());
	// if (icsUtil.searchComponent(item)) {
	// ICSAdminClass icsAdminClass = icsUtil.getClassInfo();
	// return icsAdminClass.getUser1();
	//
	// }
	// return null;
	// }
	private String GetType(TCComponent item) throws Exception {
		TCClassificationService classificationService = item.getSession()
				.getClassificationService();
		ICSApplicationObject icsApp = classificationService
				.newICSApplicationObject("ICM");
		String itemUid = classificationService.getTCComponentUid(item);
		if (!classificationService.isObjectClassified(item)) {
			item = classificationService.getActualClassifiedComponent(item);
			itemUid = classificationService.getTCComponentUid(item);
		}
		int result = icsApp.searchById("", itemUid);
		if (result > 0) {
			icsApp.read(1);
		}
		String classId = icsApp.getClassId();
		if (classId == null) {

			return null;
		}
		ICSAdminClass c = classificationService.newICSAdminClass();
		c.load(classId);
		return c.getUser1();
	}
}
