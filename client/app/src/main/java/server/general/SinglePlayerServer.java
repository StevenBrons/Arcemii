package server.general;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class SinglePlayerServer extends ArcemiiServer {

    private ObjectOutputStream oOut2;
    private ObjectInputStream oIn1;
    private ObjectOutputStream oOut1;
    private ObjectInputStream oIn2;

    /*          client              server
        1       in1     ---->        out1
        2       out2    <----        in2
     */

    SinglePlayerServer() {
        try {
            PipedInputStream in1 = new PipedInputStream();
            PipedOutputStream out1 = new PipedOutputStream(in1);

            PipedInputStream in2 = new PipedInputStream();
            PipedOutputStream out2 = new PipedOutputStream(in2);

            //client
            oOut1 = new ObjectOutputStream(out1);
            oIn1 = new ObjectInputStream(in1);

            //server
            oOut2 = new ObjectOutputStream(out2);
            oIn2 = new ObjectInputStream(in2);

            Client p = new Client(oIn2,oOut1);
            gameHandler.addPlayer(p);

            System.out.println("Singleplayer server started");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //relative to client
    public ObjectOutputStream getOutputStream() {
        return oOut2;
    }

    public ObjectInputStream getInputStream() {
        return oIn1;
    }

}
