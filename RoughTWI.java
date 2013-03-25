import ioio.lib.api.IOIO;
import ioio.lib.api.TwiMaster;
import ioio.lib.api.exception.ConnectionLostException;

private TwiMaster _twi;
private twiNum = 2; // TWI Pinset
private address = 0x00;//7bit address - OP1 - 7 pins

TwiMaster twi = ioio.openTwiMaster(twiNum, TwiMaster.Rate.RATE_100KHz, false);


//Check the mode (looking for average mode), run this at startup
byte[] request = new byte[] { 0x4D };
byte[] response = new byte[2];
try{
        twi.writeRead(address, false, request, request.length, response, response.length);
catch{
        //writeRead throws ConnectionLost and Interupted Exceptions
}
//If not average mode, set it to average mode. 
if(response[0] != '0x01'){
byte[] request = new byte[] { 0x4D,0x01 };
//Send the writeRead. Gives no response, restarts the board in mode
}



//Get last 32 Heart Rate Readings
byte[] request = new byte[] { 0x47, 0x20 };
byte[] response = new byte[34]; // Status, Count, Hr1, hr2...32

//Sends the request, Blocks until response
try{
	twi.writeRead(address, false, request, request.length, response, response.length);
catch{
	//writeRead throws ConnectionLost and Interupted Exceptions
}


