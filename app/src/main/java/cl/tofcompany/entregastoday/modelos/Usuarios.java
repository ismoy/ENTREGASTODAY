package cl.tofcompany.entregastoday.modelos;

public class Usuarios {
    String id;
    String nombre ;
    String apellido;
    String email;
    String rut;
    String phone;
    String password;
    String image;

    public Usuarios() {
    }

    public Usuarios(String id, String nombre, String apellido,String email, String rut, String phone, String password) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.rut = rut;
        this.phone = phone;
        this.password = password;
    }

    public Usuarios(String nombre, String apellido, String email, String image, String phone) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.image = image;
        this.phone = phone;

    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
