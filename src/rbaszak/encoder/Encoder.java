package rbaszak.encoder;

public class Encoder {

    //================================================================================
    // Properties
    //================================================================================

    private int Crc16Table[] = new int[256];
    //================================================================================
    // Methods
    //================================================================================

    //region GDL90 Messages Generation
    public String genHeartbeat(){

        String payload = "00010000000000";  //Heartbeat ForeFlight payload


        StringBuilder sb = new StringBuilder();

        sb.append(payload); //Payload

        //CRC
        String crc16 = crcCompute(payload);

        sb.append(crc16);

        String checkedPayload = new StringBuilder(FCS(sb.toString())).toString();

        sb = new StringBuilder();
        sb.append("7e");    //Flag byte
        sb.append(checkedPayload); //Sprawdzona zawartość
        sb.append("7e");    //Flag byte

        return sb.toString();
    }

    public String genOwnship(){

        StringBuilder payloadBuilder = new StringBuilder();

        payloadBuilder.append("0a"); //Message ID
        payloadBuilder.append("00"); //Alert Status / Address Type

        String tempIcao = Integer.toHexString(DummyData.ICAOaddress);
        payloadBuilder.append(tempIcao.substring(0,2));
        payloadBuilder.append(tempIcao.substring(2,4));
        payloadBuilder.append(tempIcao.substring(4,6));

        int valLat = (int) (DummyData.lat/(180.0 / 8388608.0));
        String tempLat = Integer.toHexString(valLat);	//Make latitude
        payloadBuilder.append(tempLat.substring(tempLat.length()-6,tempLat.length()-4));
        payloadBuilder.append(tempLat.substring(tempLat.length()-4,tempLat.length()-2));
        payloadBuilder.append(tempLat.substring(tempLat.length()-2,tempLat.length()));

        int valLon = (int) (DummyData.lon/(180.0 / 8388608.0));
        String tempLon = Integer.toHexString(valLon);	//Make latitude
        payloadBuilder.append(tempLon.substring(tempLon.length()-6,tempLon.length()-4));
        payloadBuilder.append(tempLon.substring(tempLon.length()-4,tempLon.length()-2));
        payloadBuilder.append(tempLon.substring(tempLon.length()-2,tempLon.length()));

        int valAlt = ((DummyData.alt+1000)/25);   //Altitude
        valAlt = valAlt << 4 ^ DummyData.misc;          //Append Misc
        payloadBuilder.append(String.format("%04X", valAlt));

        int valNicNacp = DummyData.NIC << 4 ^ DummyData.NACp;   //NIC and NACp
        payloadBuilder.append(String.format("%02X", valNicNacp));

        int valHor = DummyData.velHorizontal;       //Horizontal Velocity
        payloadBuilder.append(String.format("%03X", valHor));

        int valVer = DummyData.velVertical/64;      //Vertical Velocity
        payloadBuilder.append(String.format("%03X", valVer));

        Double valHead = DummyData.velHorDeg/(360.0/256.0);     //Heading
        payloadBuilder.append(String.format("%02X", valHead.intValue()));

        payloadBuilder.append(String.format("%02X", DummyData.emitterCat));     //Emitter cat

        char[] array = new char[8];         //Callsign/Tail Number
        DummyData.tailNum.getChars(0,DummyData.tailNum.length(),array,0);

        for(int i = 0; i<array.length; i++)
        {
            payloadBuilder.append(String.format("%02X", Integer.valueOf(array[i])));
        }

        payloadBuilder.append(String.format("%02X", DummyData.priorityCode));   //Priority code

        StringBuilder sb = new StringBuilder();

        sb.append(payloadBuilder.toString()); //Payload

        //CRC - lsb first
        String crc16 = crcCompute(payloadBuilder.toString());

        sb.append(crc16);

        String checkedPayload = new StringBuilder(FCS(sb.toString())).toString();

        sb = new StringBuilder();
        sb.append("7e");    //Flag byte
        sb.append(checkedPayload); //Sprawdzona zawartość
        sb.append("7e");    //Flag byte

        return sb.toString();
    }

    public String genTraffic(){

        StringBuilder payloadBuilder = new StringBuilder();

        payloadBuilder.append("14"); //Message ID
        payloadBuilder.append("00"); //Alert Status / Address Type

        String tempIcao = Integer.toHexString(DummyData.tgtICAOaddress);
        payloadBuilder.append(tempIcao.substring(0,2));
        payloadBuilder.append(tempIcao.substring(2,4));
        payloadBuilder.append(tempIcao.substring(4,6));

        int valLat = (int) (DummyData.tgtLat/(180.0 / 8388608.0));
        String tempLat = Integer.toHexString(valLat);	//Make latitude
        payloadBuilder.append(tempLat.substring(tempLat.length()-6,tempLat.length()-4));
        payloadBuilder.append(tempLat.substring(tempLat.length()-4,tempLat.length()-2));
        payloadBuilder.append(tempLat.substring(tempLat.length()-2,tempLat.length()));

        int valLon = (int) (DummyData.tgtLon/(180.0 / 8388608.0));
        String tempLon = Integer.toHexString(valLon);	//Make latitude
        payloadBuilder.append(tempLon.substring(tempLon.length()-6,tempLon.length()-4));
        payloadBuilder.append(tempLon.substring(tempLon.length()-4,tempLon.length()-2));
        payloadBuilder.append(tempLon.substring(tempLon.length()-2,tempLon.length()));

        int valAlt = ((DummyData.tgtAlt+1000)/25);   //Altitude
        valAlt = valAlt << 4 ^ DummyData.tgtMisc;          //Append Misc
        payloadBuilder.append(String.format("%04X", valAlt));

        int valNicNacp = DummyData.tgtNIC << 4 ^ DummyData.tgtNACp;   //NIC and NACp
        payloadBuilder.append(String.format("%02X", valNicNacp));

        int valHor = DummyData.tgtVelHorizontal;       //Horizontal Velocity
        payloadBuilder.append(String.format("%03X", valHor));

        int valVer = DummyData.tgtVelVertical/64;      //Vertical Velocity
        payloadBuilder.append(String.format("%03X", valVer));

        Double valHead = DummyData.tgtVelHorDeg/(360.0/256.0);     //Heading
        payloadBuilder.append(String.format("%02X", valHead.intValue()));

        payloadBuilder.append(String.format("%02X", DummyData.tgtEmitterCat));     //Emitter cat

        char[] array = new char[8];         //Callsign/Tail Number
        DummyData.tgtTailNum.getChars(0,DummyData.tgtTailNum.length(),array,0);

        for(int i = 0; i<array.length; i++)
        {
            payloadBuilder.append(String.format("%02X", Integer.valueOf(array[i])));
        }

        payloadBuilder.append(String.format("%02X", DummyData.tgtPriorityCode));   //Priority code

        StringBuilder sb = new StringBuilder();

        sb.append(payloadBuilder.toString()); //Payload

        String newString = FCS(payloadBuilder.toString());

        //CRC
        String crc16 = crcCompute(payloadBuilder.toString());

        sb.append(crc16);

        String checkedPayload = new StringBuilder(FCS(sb.toString())).toString();

        sb = new StringBuilder();
        sb.append("7e");    //Flag byte
        sb.append(checkedPayload); //Sprawdzona zawartość
        sb.append("7e");    //Flag byte

        return sb.toString();
    }
    //endregion

    //region FCS - Frame Check Sequence
    public String FCS(String msg){

        StringBuilder sb = new StringBuilder();

        for(int i = 0; i<msg.length();)
        {
            if(msg.substring(i,i+2).equals("7d") || msg.substring(i,i+2).equals("7e"))	//XOR with 0x20 when found flag or control-escape character
            {
                sb.append("7d");
                int temp = (Character.digit(msg.charAt(i), 16) << 4)
                        + Character.digit(msg.charAt(i+1), 16);
                int xored = temp ^ 0x20;
                sb.append(Integer.toHexString(xored));
            }
            else
            {
                sb.append(msg.substring(i,i+2));		//Copy unchanged bytes
            }
            i=i+2;
        }

        return sb.toString();
    }
    //endregion

    //region CRC-CCITT
    public String crcCompute(String block) {

        int crc = 0xFFFF;
        int polynomial = 0x1021;   // 0001 0000 0010 0001  (0, 5, 12)

        byte[] bytes = hexStringToByteArray(block);

        for (byte b : bytes) {
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b   >> (7-i) & 1) == 1);
                boolean c15 = ((crc >> 15    & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) crc ^= polynomial;
            }
        }

        crc &= 0xffff;

        String msb = String.format("%04x",crc);
        String lsb = msb.substring(2,4) + msb.substring(0,2);

        return lsb;
    }
    //endregion

    //region Stratux MakeLatLng Function
    public int[] makeLatLng(Double input) {

        Double resolution = (180.0 / 8388608.0);	//Resolution 180/2^23 degrees

        int v = (int) (input / resolution);

        int[] bytes = new int[3];

        bytes[0] = (v & 0xFF0000) >> 16;
        bytes[1] = (v & 0x00FF00) >> 8;
        bytes[2] = v & 0x0000FF;

        return bytes;
    }
    //endregion

    public void crcInit()
    {
        int i, bitctr, crc;
        for (i = 0; i < 256; i++)
        {
            crc = (i << 8);
            for (bitctr = 0; bitctr < 8; bitctr++)
            {
                crc = (crc << 1) ^ (((crc & 0x8000)!=0) ? 0x1021 : 0);
            }
            Crc16Table[i] = crc;
        }
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
