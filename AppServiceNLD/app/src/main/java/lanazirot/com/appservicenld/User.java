package lanazirot.com.appservicenld;

public class User {
    public User(String id, String nombre, String correo, String contra, String edad, String ocupacion, String servicio, String tipousuario) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.contra = contra;
        this.edad = edad;
        this.ocupacion = ocupacion;
        this.servicio = servicio;
        this.tipousuario = tipousuario;
    }

    public String nombre;

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setContra(String contra) {
        this.contra = contra;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public void setOcupacion(String ocupacion) {
        this.ocupacion = ocupacion;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public void setTipousuario(String tipousuario) {
        this.tipousuario = tipousuario;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String correo;
    public String contra;
    public String edad;
    public String ocupacion;
    public String servicio;
    public String tipousuario;
    public String id;


    public User() {
    }


    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getContra() {
        return contra;
    }

    public String getEdad() {
        return edad;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public String getServicio() {
        return servicio;
    }

    public String getTipousuario() {
        return tipousuario;
    }

    public String getId() {
        return id;
    }
}
