
package io.gamemachine.client.messages;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.io.UnsupportedEncodingException;

import io.protostuff.ByteString;
import io.protostuff.GraphIOUtil;
import io.protostuff.Input;
import io.protostuff.Message;
import io.protostuff.Output;
import io.protostuff.ProtobufOutput;

import java.io.ByteArrayOutputStream;
import io.protostuff.JsonIOUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.runtime.RuntimeSchema;

import io.gamemachine.util.LocalLinkedBuffer;


import java.nio.charset.Charset;


import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;


@SuppressWarnings("unused")
public final class GridVerticle implements Externalizable, Message<GridVerticle>, Schema<GridVerticle>{



    public static Schema<GridVerticle> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static GridVerticle getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final GridVerticle DEFAULT_INSTANCE = new GridVerticle();

    			public Float x;
	    
        			public Float y;
	    
            public List<GridNode> gridNodes;
	  
    public GridVerticle()
    {
        
    }


	

	    
    public Boolean hasX()  {
        return x == null ? false : true;
    }
        
		public Float getX() {
		return x;
	}
	
	public GridVerticle setX(Float x) {
		this.x = x;
		return this;	}
	
		    
    public Boolean hasY()  {
        return y == null ? false : true;
    }
        
		public Float getY() {
		return y;
	}
	
	public GridVerticle setY(Float y) {
		this.y = y;
		return this;	}
	
		    
    public Boolean hasGridNodes()  {
        return gridNodes == null ? false : true;
    }
        
		public List<GridNode> getGridNodesList() {
		if(this.gridNodes == null)
            this.gridNodes = new ArrayList<GridNode>();
		return gridNodes;
	}

	public GridVerticle setGridNodesList(List<GridNode> gridNodes) {
		this.gridNodes = gridNodes;
		return this;
	}

	public GridNode getGridNodes(int index)  {
        return gridNodes == null ? null : gridNodes.get(index);
    }

    public int getGridNodesCount()  {
        return gridNodes == null ? 0 : gridNodes.size();
    }

    public GridVerticle addGridNodes(GridNode gridNodes)  {
        if(this.gridNodes == null)
            this.gridNodes = new ArrayList<GridNode>();
        this.gridNodes.add(gridNodes);
        return this;
    }
            	    	    	    	    	
    public GridVerticle removeGridNodesBySlope(GridNode gridNodes)  {
    	if(this.gridNodes == null)
           return this;
            
       	Iterator<GridNode> itr = this.gridNodes.iterator();
       	while (itr.hasNext()) {
    	GridNode obj = itr.next();
    	
    	    		if (gridNodes.slope.equals(obj.slope)) {
    	      			itr.remove();
    		}
		}
        return this;
    }
    
            	
    
    
    
	
  
    // java serialization

    public void readExternal(ObjectInput in) throws IOException
    {
        GraphIOUtil.mergeDelimitedFrom(in, this, this);
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        GraphIOUtil.writeDelimitedTo(out, this, this);
    }

    // message method

    public Schema<GridVerticle> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public GridVerticle newMessage()
    {
        return new GridVerticle();
    }

    public Class<GridVerticle> typeClass()
    {
        return GridVerticle.class;
    }

    public String messageName()
    {
        return GridVerticle.class.getSimpleName();
    }

    public String messageFullName()
    {
        return GridVerticle.class.getName();
    }

    public boolean isInitialized(GridVerticle message)
    {
        return true;
    }

    public void mergeFrom(Input input, GridVerticle message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 1:
            	                	                	message.x = input.readFloat();
                	break;
                	                	
                            	            	case 2:
            	                	                	message.y = input.readFloat();
                	break;
                	                	
                            	            	case 4:
            	            		if(message.gridNodes == null)
                        message.gridNodes = new ArrayList<GridNode>();
                                        message.gridNodes.add(input.mergeObject(null, GridNode.getSchema()));
                                        break;
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, GridVerticle message) throws IOException
    {
    	    	
    	    	if(message.x == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.x != null)
            output.writeFloat(1, message.x, false);
    	    	
    	            	
    	    	if(message.y == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.y != null)
            output.writeFloat(2, message.y, false);
    	    	
    	            	
    	    	
    	    	if(message.gridNodes != null)
        {
            for(GridNode gridNodes : message.gridNodes)
            {
                if(gridNodes != null) {
                   	    				output.writeObject(4, gridNodes, GridNode.getSchema(), true);
    				    			}
            }
        }
    	            	
    }

    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 1: return "x";
        	        	case 2: return "y";
        	        	case 4: return "gridNodes";
        	            default: return null;
        }
    }

    public int getFieldNumber(String name)
    {
        final Integer number = __fieldMap.get(name);
        return number == null ? 0 : number.intValue();
    }

    private static final java.util.HashMap<String,Integer> __fieldMap = new java.util.HashMap<String,Integer>();
    static
    {
    	    	__fieldMap.put("x", 1);
    	    	__fieldMap.put("y", 2);
    	    	__fieldMap.put("gridNodes", 4);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = GridVerticle.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static GridVerticle parseFrom(byte[] bytes) {
	GridVerticle message = new GridVerticle();
	ProtobufIOUtil.mergeFrom(bytes, message, GridVerticle.getSchema());
	return message;
}

public static GridVerticle parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	GridVerticle message = new GridVerticle();
	JsonIOUtil.mergeFrom(bytes, message, GridVerticle.getSchema(), false);
	return message;
}

public GridVerticle clone() {
	byte[] bytes = this.toByteArray();
	GridVerticle gridVerticle = GridVerticle.parseFrom(bytes);
	return gridVerticle;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, GridVerticle.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<GridVerticle> schema = GridVerticle.getSchema();
    
	final ByteArrayOutputStream out = new ByteArrayOutputStream();
    final ProtobufOutput output = new ProtobufOutput(buffer);
    try
    {
    	schema.writeTo(output, this);
        final int size = output.getSize();
        ProtobufOutput.writeRawVarInt32Bytes(out, size);
        final int msgSize = LinkedBuffer.writeTo(out, buffer);
        assert size == msgSize;
        
        buffer.clear();
        return out.toByteArray();
    }
    catch (IOException e)
    {
        throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                "(should never happen).", e);
    }
 
}

public byte[] toProtobuf() {
	LinkedBuffer buffer = LocalLinkedBuffer.get();
	byte[] bytes = null;

	try {
		bytes = ProtobufIOUtil.toByteArray(this, GridVerticle.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed");
	}
	return bytes;
}

}
