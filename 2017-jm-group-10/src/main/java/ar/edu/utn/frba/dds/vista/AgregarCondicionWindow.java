package ar.edu.utn.frba.dds.vista;

import org.uqbar.arena.widgets.Button;
import org.uqbar.arena.widgets.Panel;
import org.uqbar.arena.widgets.Selector;
import org.uqbar.arena.windows.SimpleWindow;
import org.uqbar.arena.windows.WindowOwner;

import ar.edu.utn.frba.dds.controlador.CondicionViewModel;
import ar.edu.utn.frba.dds.controlador.MetodologiaViewModel;
import ar.edu.utn.frba.dds.metodologia.Condicion;

public class AgregarCondicionWindow extends SimpleWindow<MetodologiaViewModel>  {

	public AgregarCondicionWindow(WindowOwner parent, MetodologiaViewModel model) {
		super(parent, model);
	}

	@Override
	protected void addActions(Panel panel) {
		new Button(panel)
		.setCaption("Crear nueva condicion")
		.onClick(this::abrirNuevaCondicionWindow);
		
		new Button(panel)
		.setCaption("Agregar a metodologia")
		.onClick(this::agregarCondicion);
		new Button(panel)
		.setCaption("Salir")
		.onClick(this::close)
		.setWidth(100);
	}

	public void abrirNuevaCondicionWindow() {
		NuevaCondicionWindow dialog = new NuevaCondicionWindow(this, new CondicionViewModel(this.getModelObject()));
		dialog.open();
	}
	
	public void agregarCondicion() {
		this.getModel().getSource().agregarCondicionSeleccionadaAMetodologia();
		this.close();
	}


	@Override
	protected void createFormPanel(Panel form) {
		Selector<Condicion> selectorMetodologia = new Selector<Condicion>(form).allowNull(true);	
		selectorMetodologia.bindValueToProperty("condicionAAgregarSeleccionada");
		selectorMetodologia.bindItemsToProperty("condicionesTotales").adaptWith(Condicion.class, "nombreCondicion");
		selectorMetodologia.setWidth(280);
	}

}
