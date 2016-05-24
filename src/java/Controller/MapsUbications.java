/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Beans.UbicacionesFacade;
import Entitys.Ubicaciones;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

@ManagedBean
@ViewScoped
public class MapsUbications implements Serializable {

    private MapModel simpleModel;

    @EJB
    private Beans.UbicacionesFacade ejbFacade;
    private List<Ubicaciones> items = null;

    @PostConstruct
    public void init() {
        simpleModel = new DefaultMapModel();

        for (Ubicaciones ubicacion : getItems()) {
            simpleModel.addOverlay(new Marker(new LatLng(ubicacion.getLat(), ubicacion.getLon()), ubicacion.getTitulo()));
        }

    }

    private UbicacionesFacade getFacade() {
        return ejbFacade;
    }

    public List<Ubicaciones> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public MapModel getSimpleModel() {
        return simpleModel;
    }
}
