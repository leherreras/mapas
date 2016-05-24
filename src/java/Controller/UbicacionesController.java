package Controller;

import Entitys.Ubicaciones;
import Controller.util.JsfUtil;
import Controller.util.JsfUtil.PersistAction;
import Beans.UbicacionesFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("ubicacionesController")
@SessionScoped
public class UbicacionesController implements Serializable {

    @EJB
    private Beans.UbicacionesFacade ejbFacade;
    private List<Ubicaciones> items = null;
    private Ubicaciones selected;

    public UbicacionesController() {
    }

    public Ubicaciones getSelected() {
        return selected;
    }

    public void setSelected(Ubicaciones selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private UbicacionesFacade getFacade() {
        return ejbFacade;
    }

    public Ubicaciones prepareCreate() {
        selected = new Ubicaciones();
        initializeEmbeddableKey();
        return selected;
    }
    
    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("Titulos/Bundle").getString("UbicacionesCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void createUbicacion() {
        prepareCreate();
        persist(PersistAction.CREATE, ResourceBundle.getBundle("Titulos/Bundle").getString("UbicacionesCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("Titulos/Bundle").getString("UbicacionesUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("Titulos/Bundle").getString("UbicacionesDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Ubicaciones> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("Titulos/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("Titulos/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Ubicaciones getUbicaciones(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Ubicaciones> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Ubicaciones> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Ubicaciones.class)
    public static class UbicacionesControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UbicacionesController controller = (UbicacionesController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "ubicacionesController");
            return controller.getUbicaciones(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Ubicaciones) {
                Ubicaciones o = (Ubicaciones) object;
                return getStringKey(o.getId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Ubicaciones.class.getName()});
                return null;
            }
        }

    }

}
