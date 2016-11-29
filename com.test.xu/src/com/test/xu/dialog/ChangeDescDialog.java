package com.test.xu.dialog;

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
import javax.swing.border.MatteBorder;

import com.teamcenter.rac.aif.AIFDesktop;
import com.teamcenter.rac.aif.AbstractAIFDialog;
import com.teamcenter.rac.aif.InterfaceAIFOperationListener;
import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
import com.teamcenter.rac.kernel.TCComponent;
import com.teamcenter.rac.kernel.TCComponentItemRevision;
import com.teamcenter.rac.kernel.TCException;
import com.teamcenter.rac.kernel.TCSession;
import com.teamcenter.rac.ui.common.RACUIUtil;
import com.teamcenter.rac.util.ButtonLayout;
import com.teamcenter.rac.util.FilterDocument;
import com.teamcenter.rac.util.HorizontalLayout;
import com.teamcenter.rac.util.PropertyLayout;
import com.teamcenter.rac.util.Separator;
import com.teamcenter.rac.util.iTextArea;

public class ChangeDescDialog extends AbstractAIFDialog implements
		InterfaceAIFOperationListener {
	private static final long serialVersionUID = 1L;
	private TCComponentItemRevision itemRevision;
	protected TCSession session = RACUIUtil.getTCSession();
	private iTextArea itextArea;	
	private JButton startBtn;
	private JButton cancelBtn;
	public ChangeDescDialog(TCComponentItemRevision itemRevision) {
		super(AIFDesktop.getActiveDesktop());
		this.itemRevision = itemRevision;
		
	}

	@Override
	public void run() {

		// 获得容器
		Container dlgPanel = getContentPane();
		dlgPanel.setBackground(Color.white);
		JPanel basicInfoPanel = new JPanel(
				new PropertyLayout(5, 5, 5, 15, 5, 5));
		basicInfoPanel.setBackground(Color.white);
		// 循环判断是版本还是零组件 ，获得属性1添加到界面
				String desc = "";
				try {
					desc = itemRevision.getProperty("k3_jointDesc");
				} catch (TCException e1) {
					// TODO 自动生成的 catch 块
					e1.printStackTrace();
				}
				JLabel idLabel = new JLabel("描述属性：");
				basicInfoPanel.add("1.1.left.top", idLabel);
				idLabel.setBorder(new EmptyBorder(2, 20, 0, 0));
				String encoding = TCSession.getServerEncodingName(session);
				itextArea = 	new iTextArea(new FilterDocument(9999, encoding), desc,
						8, 20, 9999, false, null);
				itextArea.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
				basicInfoPanel.add("2.1.left.top",itextArea);


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
				ChangeDescOp op = new ChangeDescOp(ChangeDescDialog.this);
				op.addOperationListener(ChangeDescDialog.this);
				session.queueOperation(op);

				ChangeDescDialog.this.dispose();
			}
		});
		btnPanel.add(startBtn);
		cancelBtn = new JButton("取消");
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ChangeDescDialog.this.dispose();
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
		// TODO 自动生成的方法存根

	}

	@Override
	public void endOperation() {
		// TODO 自动生成的方法存根

	}

	@Override
	public void startOperation(String arg0) {
		// TODO 自动生成的方法存根

	}

	public TCComponentItemRevision getItemRevision() {
		return itemRevision;
	}

	public void setItemRevision(TCComponentItemRevision itemRevision) {
		this.itemRevision = itemRevision;
	}

	public iTextArea getItextArea() {
		return itextArea;
	}

	public void setItextArea(iTextArea itextArea) {
		this.itextArea = itextArea;
	}

}
