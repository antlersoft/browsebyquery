
/**
 * Title:        Remote File System for OpenTools<p>
 * Description:  An OpenTools vfs filesystem for accessing files on a remote host<p>
 * Copyright:    Copyright (c) Michael MacDonald<p>
 * Company:      antlersoft<p>
 * @author Michael MacDonald
 * @version 1.0
 */
package com.antlersoft.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.net.Socket;

public class Messenger
{
    public Messenger( Socket sock)
        throws IOException
    {
        socket=sock;
        socketInput=new DataInputStream( new BufferedInputStream(
            socket.getInputStream()));
        socketOutput=new DataOutputStream( new BufferedOutputStream(
            socket.getOutputStream()));
        randomInput=new RandomInputStream();
        randomOutput=new RandomOutputStream();
        outMessage=new DataOutputStream( randomOutput);
        inMessage=new DataInputStream( randomInput);
        defaultBuffer=new byte[DEFAULT_BUFFER_LENGTH];
    }

    public DataInput getDataInput()
    {
        return inMessage;
    }

    public DataOutput getDataOutput()
    {
        return outMessage;
    }

    public void sendMessage()
        throws IOException
    {
        synchronized ( outLock)
        {
            outMessage.flush();
            byte[] outBuffer=randomOutput.getWrittenBytes();
            socketOutput.writeInt( outBuffer.length);
            socketOutput.write( outBuffer);
            socketOutput.flush();
        }
    }

    public void receiveMessage()
        throws IOException
    {
        synchronized ( inLock)
        {
            int messageLength=socketInput.readInt();
            byte[] inBuffer;
            if ( messageLength<=DEFAULT_BUFFER_LENGTH)
                inBuffer=defaultBuffer;
            else
                inBuffer=new byte[messageLength];
            socketInput.readFully( inBuffer, 0, messageLength);
            randomInput.emptyAddBytes( inBuffer);
        }
    }

    public void clearOutputMessage()
        throws IOException
    {
        synchronized( outLock)
        {
            outMessage.flush();
            randomOutput.getWrittenBytes();
        }
    }

    public void sendRequest()
        throws IOException
    {
        sendMessage();
        receiveMessage();
    }

    public void close()
        throws IOException
    {
        synchronized ( outLock)
        {
            synchronized( inLock)
            {
                socket.close();
                socket=null;
                socketInput=null;
                socketOutput=null;
                randomInput=null;
                randomOutput=null;
                inMessage=null;
                outMessage=null;
            }
        }
    }

    private final int DEFAULT_BUFFER_LENGTH=10240;
    private byte[] defaultBuffer;
    private Socket socket;
    private DataInputStream socketInput;
    private DataOutputStream socketOutput;
    private RandomInputStream randomInput;
    private RandomOutputStream randomOutput;
    private DataOutputStream outMessage;
    private DataInputStream inMessage;
    private Object inLock=new Object();
    private Object outLock=new Object();
}
