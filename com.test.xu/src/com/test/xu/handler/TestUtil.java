package com.test.xu.handler;

import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentQuery;
import com.teamcenter.rac.kernel.TCComponentQueryType;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.ui.common.RACUIUtil;
import com.teamcenter.rac.util.MessageBox;
import com.teamcenter.rac.util.PlatformHelper;

public class TestUtil {

	public static void main(String[] args) {

	}

	public static TCComponent[] comm_qry(String searchName, String... args) throws TCException {
		if (args.length % 2 != 0) {
			System.err.println("查询参数错误！");
			return null;
		}

		TCSession session = RACUIUtil.getTCSession();
		TCComponentQueryType queryType = (TCComponentQueryType) session.getTypeComponent("ImanQuery");
		TCComponentQuery query = (TCComponentQuery) queryType.find(searchName);
		if (query == null) {
			MessageBox.post(PlatformHelper.getCurrentShell(), "查询‘" + searchName + "’未配置，请联系系统管理员！", "查询未配置",
					MessageBox.INFORMATION);
			return null;
		}
		int num = args.length / 2;
		String attr_name[] = new String[num];
		String attr_value[] = new String[num];
		for (int i = 0; i < num; i++) {
			attr_name[i] = args[i * 2];
			attr_value[i] = args[i * 2 + 1];
		}

		return query.execute(attr_name, attr_value);
	}
}
