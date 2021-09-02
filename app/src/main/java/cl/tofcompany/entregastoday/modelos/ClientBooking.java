package cl.tofcompany.entregastoday.modelos;

public class ClientBooking {
    String idHistoryBooking;
    String idClient;
    String destination;
    String origin;
    String time;
    String km;
    double originLat;
    double originLng;
    double destinationLat;
    double destinationLng;
    double price ;
    String image;
    String nombre;
    String apellido;
    String rut;
    String email;
    String phone;

    public ClientBooking() {
    }

    public ClientBooking(String idClient, String destination, String origin, String time, String km, double originLat, double originLng, double destinationLat, double destinationLng) {
        this.idClient = idClient;
        this.destination = destination;
        this.origin = origin;
        this.time = time;
        this.km = km;
        this.originLat = originLat;
        this.originLng = originLng;
        this.destinationLat = destinationLat;
        this.destinationLng = destinationLng;

    }

    public ClientBooking( String idClient, String destination, String origin, String time, String km, double originLat, double originLng, double destinationLat, double destinationLng, double price,  String nombre, String apellido, String rut, String email, String phone) {
        this.idClient = idClient;
        this.destination = destination;
        this.origin = origin;
        this.time = time;
        this.km = km;
        this.originLat = originLat;
        this.originLng = originLng;
        this.destinationLat = destinationLat;
        this.destinationLng = destinationLng;
        this.price = price;
        this.nombre = nombre;
        this.apellido = apellido;
        this.rut = rut;
        this.email = email;
        this.phone = phone;
    }

    public String getIdClient() {
        return idClient;
    }

    public void setIdClient(String idClient) {
        this.idClient = idClient;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getIdHistoryBooking() {
        return idHistoryBooking;
    }

    public void setIdHistoryBooking(String idHistoryBooking) {
        this.idHistoryBooking = idHistoryBooking;
    }

    public double getOriginLat() {
        return originLat;
    }

    public void setOriginLat(double originLat) {
        this.originLat = originLat;
    }

    public double getOriginLng() {
        return originLng;
    }

    public void setOriginLng(double originLng) {
        this.originLng = originLng;
    }

    public double getDestinationLat() {
        return destinationLat;
    }

    public void setDestinationLat(double destinationLat) {
        this.destinationLat = destinationLat;
    }

    public double getDestinationLng() {
        return destinationLng;
    }

    public void setDestinationLng(double destinationLng) {
        this.destinationLng = destinationLng;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
