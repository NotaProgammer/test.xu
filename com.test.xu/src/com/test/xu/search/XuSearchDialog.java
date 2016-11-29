package com.test.xu.search;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.teamcenter.rac.aif.AIFDesktop;
import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.aif.InterfaceAIFOperationListener;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.ui.common.RACUIUtil;
import com.teamcenter.rac.util.ButtonLayout;
import com.teamcenter.rac.util.FilterDocument;
import com.teamcenter.rac.util.HorizontalLayout;
import com.teamcenter.rac.util.PropertyLayout;
import com.teamcenter.rac.util.Separator;
import com.teamcenter.rac.util.iTextField;
import com.test.xu.dialog.ChangeProsDialog;
import com.test.xu.dialog.ChangeProsOp;

public class XuSearchDialog extends AbstractAIFDialog implements
		InterfaceAIFOperationListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 528239189437779615L;
	private ArrayList<iTextField> itextFields;
	private JButton startBtn;
	private JButton cancelBtn;
	protected TCSession session = RACUIUtil.getTCSession();

	public XuSearchDialog() {
		super(AIFDesktop.getActiveDesktop());
		itextFields = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			itextFields.add(new iTextField(new FilterDocument(40), "", 20, 40,
					false, null));
		}
		this.setTitle("搜索功能");
		// TODO 自动生成的构造函数存根
	}

	public void run() {
		// 获得容器
		Container dlgPanel = getContentPane();
		dlgPanel.setBackground(Color.white);
		JPanel basicInfoPanel = new JPanel(
				new PropertyLayout(5, 5, 5, 15, 5, 5));
		basicInfoPanel.setBackground(Color.white);

		JLabel idLabel = new JLabel("输入字符串1：");
		basicInfoPanel.add("1.1.left.top", idLabel);
		idLabel.setBorder(new EmptyBorder(2, 20, 0, 0));
		// 生成10个输入框
		for (int i = 0; i < 10; i++) {
			basicInfoPanel.add(i + 2 + ".1.left.top", itextFields.get(i));
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
				XuSearchOp op = new XuSearchOp(XuSearchDialog.this);
				op.addOperationListener(XuSearchDialog.this);
				session.queueOperation(op);

				XuSearchDialog.this.dispose();
			}
		});
		btnPanel.add(startBtn);
		cancelBtn = new JButton("取消");
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				XuSearchDialog.this.dispose();
			}
		});
		btnPanel.add(cancelBtn);
		JPanel btnOutter = new JPanel(new BorderLayout());
		btnOutter.setBorder(new EmptyBorder(5, 10, 5, 10));
		btnOutter.add(BorderLayout.NORTH, new Separator());
		btnOutter.add(BorderLayout.CENTER, btnPanel);
		dlgPanel.add(BorderLayout.SOUTH, btnOutter);

		setPreferredSize(new Dimension(300, 450));

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

	public ArrayList<iTextField> getItextFields() {
		return itextFields;
	}

	public void setItextFields(ArrayList<iTextField> itextFields) {
		this.itextFields = itextFields;
	}

}
