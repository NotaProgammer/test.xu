package com.test.xu.task;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Label;
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
import com.teamcenter.rac.common.lov.LOVUIComponent;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.ui.common.RACUIUtil;
import com.teamcenter.rac.util.ButtonLayout;
import com.teamcenter.rac.util.HorizontalLayout;
import com.teamcenter.rac.util.PropertyLayout;
import com.teamcenter.rac.util.Separator;
import com.test.xu.dialog.ChangeProsDialog;
import com.test.xu.dialog.ChangeProsOp;

public class SetRecDialog  extends AbstractAIFDialog implements
InterfaceAIFOperationListener {
	TCSession session = RACUIUtil.getTCSession();
	private LOVUIComponent lovuiComponent;
	private JButton startBtn;
	private JButton cancelBtn;
	TCComponent task;
	public SetRecDialog(TCComponent task) {
		super(AIFDesktop.getActiveDesktop());
		this.task = task;
		lovuiComponent = new LOVUIComponent(session,"User Ids");
		// TODO 自动生成的构造函数存根
	}
	@Override
	public void run() {
		Container dlgPanel = getContentPane();
		dlgPanel.setBackground(Color.white);;
		JPanel basicInfoPanel = new JPanel(
				new PropertyLayout(5, 5, 5, 15, 5, 5));
		basicInfoPanel.setBackground(Color.white);
		JLabel label = new JLabel("下一任务接收人：");
		basicInfoPanel.add("1.1.left.top", label);
		label.setBorder(new EmptyBorder(2, 20, 0, 0));
		basicInfoPanel.add("1.2.left.top", lovuiComponent);
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
						SetRecOp op = new SetRecOp(SetRecDialog.this);
						op.addOperationListener(SetRecDialog.this);
						session.queueOperation(op);

						SetRecDialog.this.dispose();
					}
				});
				btnPanel.add(startBtn);
				cancelBtn = new JButton("取消");
				cancelBtn.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						SetRecDialog.this.dispose();
					}
				});
				btnPanel.add(cancelBtn);
				JPanel btnOutter = new JPanel(new BorderLayout());
				btnOutter.setBorder(new EmptyBorder(5, 10, 5, 10));
				btnOutter.add(BorderLayout.NORTH, new Separator());
				btnOutter.add(BorderLayout.CENTER, btnPanel);
				dlgPanel.add(BorderLayout.SOUTH, btnOutter);

				setPreferredSize(new Dimension(350, 200));
		// TODO 自动生成的方法存根
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
	public LOVUIComponent getLovuiComponent() {
		return lovuiComponent;
	}
	public void setLovuiComponent(LOVUIComponent lovuiComponent) {
		this.lovuiComponent = lovuiComponent;
	}
	public TCComponent getTask() {
		return task;
	}
	public void setTask(TCComponent task) {
		this.task = task;
	}
	
}
