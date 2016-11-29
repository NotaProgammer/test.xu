package com.test.xu.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class MyView extends ViewPart{

	@Override
	public void createPartControl(Composite parent) {
		// TODO 自动生成的方法存根
		parent.setLayout( new FillLayout() );
		Text t = new Text( parent, SWT.BORDER );
		t.setBackground( parent.getDisplay().getSystemColor( SWT.COLOR_WHITE ));
		t.setForeground( parent.getDisplay().getSystemColor( SWT.COLOR_BLUE ));
		Font initialFont = t.getFont();
		FontData[] fontData = initialFont.getFontData();
		for (int i = 0; i < fontData.length; i++) {
		fontData[i].setHeight(18);
		}
		Font newFont = new Font(parent.getDisplay(), fontData);
		t.setFont( newFont );
		t.setText( " Hello from CustomView!! ");
	}

	@Override
	public void setFocus() {
		// TODO 自动生成的方法存根
		
	}

}
