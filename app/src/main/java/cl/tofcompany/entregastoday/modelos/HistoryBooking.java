package cl.tofcompany.entregastoday.modelos;

public class HistoryBooking {

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
    long timestamp;

    public HistoryBooking() {

    }

    public HistoryBooking( String idClient, String destination, String origin, String time, String km, double originLat, double originLng, double destinationLat, double destinationLng) {
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

    public String getIdHistoryBooking() {
        return idHistoryBooking;
    }

    public void setIdHistoryBooking(String idHistoryBooking) {
        this.idHistoryBooking = idHistoryBooking;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
