package com.test.xu.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.teamcenter.rac.aif.AIFDesktop;
import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.aif.InterfaceAIFOperationListener;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.common.lov.LOVUIComponent;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentItem;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.stylesheet.PropertyLOVDisplayer;
import com.teamcenter.rac.ui.common.RACUIUtil;
import com.teamcenter.rac.util.ButtonLayout;
import com.teamcenter.rac.util.FilterDocument;
import com.teamcenter.rac.util.HorizontalLayout;
import com.teamcenter.rac.util.PropertyLayout;
import com.teamcenter.rac.util.Separator;
import com.teamcenter.rac.util.iTextField;
import com.teamcenter.rac.util.combobox.iComboBox;
import com.teamcenter.soaictstubs.int64_s;
import com.test.xu.dialog.ChangeProsOp;

public class ChangeProsDialog extends AbstractAIFDialog implements
		InterfaceAIFOperationListener {
	private static final long serialVersionUID = 1L;
	private ArrayList<InterfaceAIFComponent> selectedComponents;
	protected TCSession session = RACUIUtil.getTCSession();

	private JButton startBtn;
	private JButton cancelBtn;
	private ArrayList<LOVUIComponent> lovuiComponents;
	public ChangeProsDialog(ArrayList<InterfaceAIFComponent> list) {
		super(AIFDesktop.getActiveDesktop());
		this.selectedComponents = list;
		lovuiComponents = new ArrayList<LOVUIComponent>();
		// 根据获得零组件长度 初始化iTextField
		for (int i = 0; i < list.size(); i++) {
			lovuiComponents.add(new LOVUIComponent(session,"K3_myLOV"));
		}
		this.setTitle("修改属性");
		// TODO 自动生成的构造函数存根
	}

	public void run() {

		// 获得容器
		Container dlgPanel = getContentPane();
		dlgPanel.setBackground(Color.white);
		JPanel basicInfoPanel = new JPanel(
				new PropertyLayout(5, 5, 5, 15, 5, 5));
		basicInfoPanel.setBackground(Color.white);
		// 循环判断是版本还是零组件 ，获得属性1添加到界面
		for (int i = 0; i < selectedComponents.size(); i++) {
			TCComponent tcComponent = (TCComponent) selectedComponents.get(i);
			try {
				String id = tcComponent.getProperty("current_id");
				JLabel idLabel = new JLabel(id + "：");
				basicInfoPanel.add(i + 1 + ".1.left.top", idLabel);
				idLabel.setBorder(new EmptyBorder(2, 20, 0, 0));

				String attr = tcComponent.getProperty("k3_StringAttr1");
				lovuiComponents.get(i).setSelectedValue(attr);
			} catch (TCException e1) {
				e1.printStackTrace();
			}
			basicInfoPanel.add(i + 1 + ".2.left.top", lovuiComponents.get(i));
		}

		// 添加Button
		JPanel attrLayOut = new JPanel(new HorizontalLayout(10, 1, 1, 1, 0));
		attrLayOut.add("unbound.nobind.center.center", basicInfoPanel);
		attrLayOut.setBackground(Color.white);
		dlgPanel.add(BorderLayout.CENTER, attrLayOut);

		dlgPanel.add(BorderLayout.NORTH, basicInfoPanel);

		JPanel btnPanel = new JPanel(new ButtonLayout(ButtonLayout.HORIZONTAL,
				ButtonLayout.CENTER, 7));
		startBtn = new JButton("确定");
		startBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ChangeProsOp op = new ChangeProsOp(ChangeProsDialog.this);
				op.addOperationListener(ChangeProsDialog.this);
				session.queueOperation(op);

				ChangeProsDialog.this.dispose();
			}
		});
		btnPanel.add(startBtn);
		cancelBtn = new JButton("取消");
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ChangeProsDialog.this.dispose();
			}
		});
		btnPanel.add(cancelBtn);
		JPanel btnOutter = new JPanel(new BorderLayout());
		btnOutter.setBorder(new EmptyBorder(5, 10, 5, 10));
		btnOutter.add(BorderLayout.NORTH, new Separator());
		btnOutter.add(BorderLayout.CENTER, btnPanel);
		dlgPanel.add(BorderLayout.SOUTH, btnOutter);

		setPreferredSize(new Dimension(350, 200));

		super.run();
	}

	@Override
	public void endOperation() {
		// TODO 自动生成的方法存根

	}

	@Override
	public void startOperation(String arg0) {
		// TODO 自动生成的方法存根

	}

	public ArrayList<InterfaceAIFComponent>getSelectedComponents() {
		return selectedComponents;
	}

	public void setSelectedComponents(ArrayList<InterfaceAIFComponent>selectedComponents) {
		this.selectedComponents = selectedComponents;
	}

	public TCSession getSession() {
		return session;
	}

	public void setSession(TCSession session) {
		this.session = session;
	}

	public JButton getStartBtn() {
		return startBtn;
	}

	public void setStartBtn(JButton startBtn) {
		this.startBtn = startBtn;
	}

	public JButton getCancelBtn() {
		return cancelBtn;
	}

	public void setCancelBtn(JButton cancelBtn) {
		this.cancelBtn = cancelBtn;
	}


	public ArrayList<LOVUIComponent> getLovuiComponents() {
		return lovuiComponents;
	}

	public void setLovuiComponents(ArrayList<LOVUIComponent> lovuiComponents) {
		this.lovuiComponents = lovuiComponents;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}