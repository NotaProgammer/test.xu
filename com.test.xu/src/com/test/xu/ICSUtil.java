package com.test.xu;

import com.teamcenter.rac.kernel.TCClassificationService;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentBOMLine;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.kernel.ics.ICSAdminClass;
import com.teamcenter.rac.kernel.ics.ICSApplicationObject;
import com.teamcenter.rac.kernel.ics.ICSHierarchyNodeDescriptor;
import com.teamcenter.rac.kernel.ics.ICSProperty;

public class ICSUtil {

	public final TCClassificationService classificationService;
	public final ICSApplicationObject icsApp;

	public ICSUtil(TCSession session) throws TCException {
		this.classificationService = session.getClassificationService();
		this.icsApp = classificationService.newICSApplicationObject("ICM");
	}

	public ICSUtil(TCClassificationService classificationService,
			ICSApplicationObject icsApp) throws TCException {
		this.classificationService = classificationService;
		this.icsApp = icsApp;
	}

	public boolean searchComponent(TCComponent cmp) throws TCException {
		if (cmp instanceof TCComponentBOMLine) {
			Object obj = ((TCComponentBOMLine) cmp).getItemRevision();
			if (obj == null)
				obj = ((TCComponentBOMLine) cmp).getItem();
			cmp = (TCComponent) obj;
		}
		if (cmp == null)
			return false;
		String cmpUid = classificationService.getTCComponentUid(cmp);
		if (!classificationService.isObjectClassified(cmp)) {
			cmp = classificationService.getActualClassifiedComponent(cmp);
			if (cmp == null)
				return false;
			cmpUid = classificationService.getTCComponentUid(cmp);
		}

		int resultCount = icsApp.searchById("", cmpUid);
		if (resultCount > 0) {
			icsApp.read(1);
			return true;
		}
		return false;
	}

	public ICSHierarchyNodeDescriptor[] getClassificationPath(TCComponent cmp)
			throws TCException {
		if (!searchComponent(cmp))
			return null;
		return getClassificationPath();
	}

	public ICSHierarchyNodeDescriptor[] getClassificationPath()
			throws TCException {
		String leafClassID = icsApp.getClassId();
		String[] clsIDs = classificationService.getParents(leafClassID);
		assert clsIDs != null && clsIDs.length > 0;
		ICSHierarchyNodeDescriptor[] path = new ICSHierarchyNodeDescriptor[clsIDs.length + 1];
		for (int i = 0; i < clsIDs.length; i++) {
			path[i] = classificationService.describeNode(clsIDs[i], 0);
		}
		path[clsIDs.length] = classificationService
				.describeNode(leafClassID, 0);
		return path;
	}

	public ICSProperty[] getProperties() {
		return icsApp.getProperties();
	}

	public ICSAdminClass getClassInfo() throws TCException {
		String classId = icsApp.getClassId();
		if (classId == null)
			return null;
		ICSAdminClass c = classificationService.newICSAdminClass();
		c.load(classId);
		return c;
	}

}
