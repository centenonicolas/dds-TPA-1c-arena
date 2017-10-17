package ar.edu.utn.frba.dds;

import org.uqbar.arena.Application;
import org.uqbar.arena.windows.Window;

import ar.edu.utn.frba.dds.vista.MainMenuWindow;


public class CuentasApp extends Application {

	
	public static void main(String[] args) {
		// Instancio la clase de la aplicacion visual y inicio el entorno visual
		new CuentasApp().start();
	}
	
	
	@Override
	protected Window<?> createMainWindow() {
		// Llamo a la clase que es mi primer ventana.
		/*ServicioCuentas unServicio = new ServicioCuentas();
		return new CuentaWindow(this, new CuentaViewModel(unServicio));*/
		return new MainMenuWindow(this);
		
	}
}
	