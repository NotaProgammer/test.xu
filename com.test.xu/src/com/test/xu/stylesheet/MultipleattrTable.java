package com.test.xu.stylesheet;

import java.awt.Dimension;
import java.awt.List;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemType;
import com.teamcenter.rac.kernel.TCComponentType;
import com.teamcenter.rac.kernel.TCPreferenceService;
import com.teamcenter.rac.kernel.TCProperty;
import com.teamcenter.rac.kernel.TCPropertyDescriptor;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.stylesheet.InterfaceBufferedPropertyComponent;
import com.teamcenter.rac.stylesheet.InterfacePropertyComponent;
import com.teamcenter.rac.ui.common.RACUIUtil;
import com.teamcenter.rac.util.HorizontalLayout;

public class MultipleattrTable extends JPanel implements
		InterfacePropertyComponent, InterfaceBufferedPropertyComponent {

	final TCSession session = RACUIUtil.getTCSession();
	private static final long serialVersionUID = 1L;
	JTable table;
	JScrollPane scrollPane;

	public MultipleattrTable() {
		setLayout(new HorizontalLayout(0, 0, 0, 0, 0));
		setOpaque(false);

		String[] headers = { "������", "����ֵ" };
		Object[][] cellData = null;
		DefaultTableModel model = new DefaultTableModel(cellData, headers) {
			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				if (column == 1) {
					return true;
				}
				return false;
			}
		};
		table = new JTable(model);
		table.setAutoscrolls(true);
		scrollPane = new JScrollPane(table);
		setPreferredSize(new Dimension(200, 100));
		add("unbound.bind.left.top", scrollPane);
	}

	@Override
	public TCProperty saveProperty(TCComponent arg0) throws Exception {
		TCComponent itemrev = arg0;
		TCComponent[] tcComponents = itemrev
				.getRelatedComponents("k3_multipleAttr");
		System.out.println(tcComponents.length);
		ArrayList<TCComponent> list = new ArrayList<>();
			Collections.addAll(list, tcComponents);
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		int rowCount = tableModel.getRowCount();
		for (int i = 0; i < rowCount; i++) {
			boolean flag = false;
			String configItem = (String) table.getValueAt(i, 0);
			String configValue = (String) table.getValueAt(i, 1);
			for (int j = 0; j < tcComponents.length; j++) {
				if (tcComponents[j].getProperty("object_name").equals(configItem)) {
					flag = true;
					tcComponents[j].setProperty("k3_value", configValue);
					break;
				}
			}
			if (flag == false) {
				TCComponentItemType itemtype = (TCComponentItemType) session
						.getTypeComponent("K3_saveItem");
				TCComponentItem newitem = itemtype.create("", // ID
						"", // RevID
						"K3_saveItem", // ����
						configItem, // ����
						"", // ����
						null); // ��λ(����)
				newitem.setProperty("k3_value", configValue);
				list.add((TCComponent)newitem);
			}
		}
		TCComponent[] components = new TCComponent[list.size()];
		for(int i =0;i<list.size();i++){
			components[i] = list.get(i);
		}
		itemrev.setRelated("k3_multipleAttr", components);
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public TCProperty saveProperty(TCProperty arg0) throws Exception {
		
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public Object getEditableValue() {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public String getProperty() {
		// TODO �Զ����ɵķ������
		return null;
	}

	@Override
	public boolean isMandatory() {
		// TODO �Զ����ɵķ������
		return false;
	}

	@Override
	public boolean isPropertyModified(TCComponent arg0) throws Exception {
		// TODO �Զ����ɵķ������
		return false;
	}

	@Override
	public boolean isPropertyModified(TCProperty arg0) throws Exception {
		// TODO �Զ����ɵķ������
		return false;
	}

	@Override
	public void load(TCComponent arg0) throws Exception {
		TCPreferenceService prefs = session.getPreferenceService();
		String[] attrs = prefs.getStringValues("attrs");
		TCComponent itemrev = arg0;

		TCComponent[] tcComponents = itemrev
				.getRelatedComponents("k3_multipleAttr");
		System.out.println(tcComponents.length + "---------");
		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
		for (int i = 0; i < attrs.length; i++) {
			String value = " ";
			for (int j = 0; j < tcComponents.length; j++) {
				System.out.println(tcComponents[j].getProperty("object_name")+"##########"+attrs[i]);
				if (tcComponents[j].getProperty("object_name").equals(attrs[i])) {
					value = tcComponents[j].getProperty("k3_value");
					System.out.println(value+"$$$$$$$$$$");
				}
			}
			System.out.println(attrs[i] + "-----------" + value);
			Object[] row = new Object[] { attrs[i], value };
			tableModel.addRow(row);
		}
		repaint();
		// TODO �Զ����ɵķ������

	}

	@Override
	public void load(TCProperty arg0) throws Exception {

		// TODO �Զ����ɵķ������

	}

	@Override
	public void load(TCComponentType arg0) throws Exception {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void load(TCPropertyDescriptor arg0) throws Exception {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void save(TCComponent arg0) throws Exception {

	}

	@Override
	public void save(TCProperty arg0) throws Exception {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void setMandatory(boolean arg0) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void setModifiable(boolean arg0) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void setProperty(String arg0) {
		// TODO �Զ����ɵķ������

	}

	@Override
	public void setUIFValue(Object arg0) {
		// TODO �Զ����ɵķ������

	}

}
