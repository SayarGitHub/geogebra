package geogebra.touch.gui.elements.ggt;

import geogebra.common.move.ggtapi.models.Material;
import geogebra.html5.main.AppWeb;
import geogebra.touch.FileManagerM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;

public class VerticalMaterialPanel extends FlowPanel
{
	public static final int SPACE = 20;
	private FlexTable contentPanel;
	private AppWeb app;
	private FileManagerM fm;
	private MaterialListElement lastSelected;
	private int columns = 2;
	private Map<String, MaterialListElement> titlesToPreviews = new HashMap<String, MaterialListElement>();
	
	private int start, end;
	private List<Material> materials; 

	public VerticalMaterialPanel(AppWeb app, FileManagerM fm)
	{
		this.getElement().getStyle().setFloat(Style.Float.LEFT);
		this.contentPanel = new FlexTable();
		this.app = app;
		this.fm = fm;

		//this.setWidget(this.contentPanel);
		this.add(this.contentPanel);
		this.contentPanel.setWidth("100%");
	}

	public void setMaterials(int cols, List<Material> materials)
	{
		setMaterials(cols, materials, 0);
	}

	private void setMaterials(int cols, List<Material> materials, int offset) {
		this.columns = cols;
		this.updateWidth();
		this.contentPanel.clear();
		this.start = offset;
		this.materials = materials;
		
		if (this.columns == 2) {
			this.contentPanel.getCellFormatter().setWidth(0, 0, "50%");
			this.contentPanel.getCellFormatter().setWidth(0, 1, "50%");
		} else {
			this.contentPanel.getCellFormatter().setWidth(0, 0, "100%");
		}

		int totalHeight = 0;
		for (int i = 0; i < materials.size() - this.start; i++)
		{
			Material m = materials.get(i+this.start);
			MaterialListElement preview = new MaterialListElement(m, this.app, this.fm, this);
			preview.initButtons();
			this.titlesToPreviews.put(m.getURL(), preview);
			this.contentPanel.setWidget(i / this.columns, i % this.columns, preview);
			totalHeight += 140;			
			this.end = this.start + i;
			if(totalHeight > Window.getClientHeight() - 200){
				break;
			}
		}
		
		
	}

	@Override
	public int getOffsetHeight()
	{
		return MaterialListElement.PANEL_HEIGHT;
	}

	public void unselectMaterials()
	{
		if (this.lastSelected != null)
		{
			this.lastSelected.markUnSelected();
		}
	}

	public void markByURL(String url)
	{
		MaterialListElement mle = this.titlesToPreviews.get(url);
		if (mle != null)
		{
			mle.markSelected();
		}
	}

	public void rememberSelected(MaterialListElement materialElement)
	{
		this.lastSelected = materialElement;
	}

	public int getColumns()
	{
		return this.columns;
	}

	public void updateWidth()
	{
		this.setWidth(((Window.getClientWidth()) / 2 * this.columns) + "px");
	}
	
	public MaterialListElement getChosenMaterial()
	{
		return this.lastSelected;
	}
	
	public void setLabels(){
		for(MaterialListElement e: this.titlesToPreviews.values()){
			e.setLabels();
		}
	}

	public void nextPage() {
		if(this.end+1 >= this.materials.size()){
			return;
		}
		this.setMaterials(this.columns, this.materials, this.end+1);	
	}
	
	public void prevPage() {
		if(this.start <= 0){
			return;
		}
		this.setMaterials(this.columns, this.materials, Math.max(0, this.start-(this.end+1 - this.start)));	
	}
}
