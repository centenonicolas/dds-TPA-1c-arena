package ar.edu.utn.frba.dds.repositorios;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import ar.edu.utn.frba.dds.metodologia.Metodologia;
import ar.edu.utn.frba.dds.servicio.ServicioMetodologias;
import ar.edu.utn.frba.dds.util.ProveedorAcceso;

public class RepositorioMetodologias {
	private static RepositorioMetodologias repositorioMetodologias = null;
	
	private static List<Metodologia> metodologias;
	
	private static ProveedorAcceso ProveedorServicio = new ProveedorAcceso();
	
	private RepositorioMetodologias() { }
	
	public static RepositorioMetodologias getInstance() {
		if (repositorioMetodologias != null)
			return repositorioMetodologias;
		else {
			repositorioMetodologias = new RepositorioMetodologias();
			metodologias = ProveedorServicio.obtenerMetodologias();
			return repositorioMetodologias;
		}
	}
	
	public List<Metodologia> getMetodologias() {
		return metodologias;
	}
	
	public void agregarMetodologia(Metodologia unaMetodologia){
		/*List<Metodologia> unaLista = null;
		unaLista = metodologias.stream().filter(metodologia -> metodologia.getNombre().equals(unaMetodologia.getNombre())).collect(Collectors.toList());
		if (unaLista == null)
			metodologias.add(unaMetodologia);
		else {
			metodologias.remove(unaLista.get(0));
			metodologias.add(unaMetodologia);
		}*/
		if(metodologias.contains(unaMetodologia))
			metodologias.remove(unaMetodologia);
		
		metodologias.add(unaMetodologia);
		try {
			ProveedorServicio.guardarMetodologia(unaMetodologia);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Metodologia obtenerMetodologia(String nombreMetodologia) {
		return metodologias.stream().filter(unaMetodologia -> unaMetodologia.getNombre().equals(nombreMetodologia)).collect(Collectors.toList()).get(0);
	}
	
}
