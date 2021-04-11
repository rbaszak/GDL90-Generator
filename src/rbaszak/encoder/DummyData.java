package rbaszak.encoder;

//Dummy data file - Aircraft and Device information

public class DummyData {

    //region Own Aircraft Data
    public static int ICAOaddress = 052642511; //Octal ICAO
    public static Double lat = 44.91602; //Latitude - North
    public static Double lon = -122.97991; //Longitude - West
    public static int alt = 5000; //Altitude (ft)
    public static int NIC = 10;
    public static int NACp = 9;
    public static int misc = 0x01;
    public static int velHorizontal = 123; //Knots
    public static Double velHorDeg = 45.000; //Track/Heading in degrees
    public static int velVertical = 64; //FPM climb
    public static int priorityCode = 0x00; //None
    public static int emitterCat = 0x01; //Light
    public static String tailNum = "N825V   ";
    //endregion

    //region Target Aircraft Data
    public static int tgtICAOaddress = 052644511; //Octal ICAO
    public static Double tgtLat = 44.90532; //Latitude - North
    public static Double tgtLon = -122.92701; //Longitude - West
    public static int tgtAlt = 8000; //Altitude (ft)
    public static int tgtNIC = 10;
    public static int tgtNACp = 9;
    public static int tgtMisc = 0x01;
    public static int tgtVelHorizontal = 120; //Knots
    public static Double tgtVelHorDeg = 270.000; //Track/Heading in degrees
    public static int tgtVelVertical = 64; //FPM climb
    public static byte tgtPriorityCode = 0x00; //None
    public static byte tgtEmitterCat = 0x01; //Light
    public static String tgtTailNum = "TEST1   ";
    //endregion
}
